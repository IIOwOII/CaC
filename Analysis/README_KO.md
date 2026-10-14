# Pupil Core → Java 17 / Forge 1.20.1 연동

외부 JAR, JNI, Python 프로세스 없이 Java 표준 라이브러리만으로 동작하는 코드입니다.
`PupilRemoteClient.java`가 필요한 ZMTP 통신과 MessagePack 직렬화를 직접 구현합니다.
Forge 1.20.1은 Java 17을 사용합니다.

## 파일

| 파일 | 역할 |
| --- | --- |
| `PupilRemoteClient.java` | Python 원본과 동일한 요청/응답, 공식 예제의 PUB 전송, 시각 변환 |
| `PupilEventSender.java` | 게임 스레드에서 사용할 비동기 전송 어댑터. 단일 작업 스레드, 최대 256개 대기 작업 |
| `README_KO.md` | 실행 및 Forge 적용 방법, 검증 범위 |

추가 Gradle dependency는 필요하지 않습니다. Pupil Capture 자체는 별도로 실행되어 있어야 합니다.
`127.0.0.1`은 이 Java 코드가 실행되는 PC입니다. 다른 PC에서 Capture를 실행한다면 해당 PC의 주소를 사용합니다.

## 1. 질문의 Python 코드와 동일한 호출

```java
try (PupilRemoteClient remote =
         new PupilRemoteClient("127.0.0.1", 50020, 3000)) {

    // send_string("t") → recv_string() → float(...)
    double timestamp = remote.getTimestamp();

    // [b"notify.annotation", msgpack.packb({...}, use_bin_type=True)]
    String reply = remote.sendAnnotation("event_1", timestamp, 0.0);
    System.out.println(reply);
}
```

이 코드는 `IOException`이 발생할 수 있으므로 호출 메서드에 `throws IOException`을 붙이거나
`try/catch`로 처리해야 합니다. 위의 직접 호출은 통신 완료까지 기다리므로 연결 확인용으로 사용하고,
게임 이벤트 핸들러에는 아래 비동기 어댑터를 사용하세요.

MessagePack의 내용은 원본과 같습니다.

```text
subject:   "annotation"
topic:     "annotation"
label:     "event_1"
timestamp: Pupil Time의 double 값
duration:  0.0
```

일반 TCP 문자열 전송이나 JSON 전송으로 대체할 수 없습니다. 내부적으로 ZMTP greeting/READY,
REQ delimiter, multipart 프레임과 MessagePack map/string/float64를 구성합니다.

## 2. annotation 저장 경로 선택

두 메서드는 구분해서 사용합니다. 같은 이벤트에 둘 다 호출하면 중복 기록될 가능성이 있습니다.

| 메서드 | 경로 | 반환의 의미 |
| --- | --- | --- |
| `sendAnnotation(...)` | 원본 그대로 Pupil Remote에 `notify.annotation` 전송 | `Notification received` 등 서버의 응답 문자열 |
| `publishAnnotation(...)` | `PUB_PORT`로 연결한 PUB 소켓에 `annotation` 전송 | 로컬 TCP 쓰기 완료. 수신 ACK 없음 |

Pupil Labs의 공식 `remote_annotations.py` 예제는 두 번째 경로를 사용합니다.
첫 번째 경로는 제공하신 Python의 전송 동작을 그대로 보존한 것입니다.
notification의 수신 확인만으로 Annotation Capture의 처리나 녹화 파일 저장까지 확인된 것은 아닙니다.
사용 중인 Capture 버전에서 첫 번째 경로의 실제 저장 여부를 확인하세요.

공식 예제의 경로로 보내려면 다음과 같이 사용합니다.

```java
try (PupilRemoteClient remote =
         new PupilRemoteClient("127.0.0.1", 50020, 3000)) {
    remote.openAnnotationPublisher();
    PupilRemoteClient.ClockMapping clock = remote.synchronizeClock(10);

    long eventNanos = System.nanoTime();
    remote.publishAnnotation("event_1", clock.toPupilTime(eventNanos), 0.0);
}
```

실제 실험에서는 연결을 이벤트마다 새로 만들지 말고 세션 동안 유지하세요.
`openAnnotationPublisher()`는 subscription을 확인한 뒤 반환하여 초기 연결 직후의 전송 누락을 줄입니다.
그렇더라도 PUB 전송에는 녹화 저장에 대한 ACK가 없으며, Capture 종료·부하·네트워크 상태에 따라 누락될 수 있습니다.

## 3. 타임스탬프 처리

최초 조회한 `timestamp`를 여러 이벤트에 재사용하면 모든 이벤트가 같은 시각에 기록됩니다.
또한 이벤트마다 작업 스레드 안에서 `getTimestamp()`를 호출하면, 이벤트 발생 후의 대기열 지연과
통신 지연이 포함된 시각이 됩니다.

제공한 비동기 어댑터는 다음 순서를 사용합니다.

1. 실험 준비 시 `t`를 10회 조회해 Java의 `System.nanoTime()`과 Pupil Time의 대응을 추정합니다.
2. 이벤트 콜백에서 즉시 `System.nanoTime()` 값을 잡습니다.
3. 그 값을 Pupil Time으로 변환하고 별도 스레드에서 순서대로 전송합니다.

변환은 `pupilSeconds + (eventNanos - localMidpointNanos) / 1e9`입니다.
최소 왕복시간 표본의 요청/응답 중간 시각을 기준으로 삼습니다.
공식 예제의 지연 보정 개념을 참고하되, 표본 선택은 이 구현의 방식입니다.
왕복 지연의 대칭성과 두 시계의 속도 안정성을 가정하는 추정이며 정밀도를 보증하지 않습니다.

`timestamp`에는 `System.currentTimeMillis()`나 보정하지 않은 `System.nanoTime()/1e9`를 바로 넣지 마세요.
같은 JVM에서 잡은 `nanoTime()`만 이 대응표로 변환할 수 있습니다.
timestamp와 duration의 단위는 초이며, duration `0.0`은 순간 이벤트입니다.

Capture 재시작, 연결 재생성, Pupil Time 재설정 후에는 새로 보정해야 합니다.
긴 세션에서는 블록 사이에 `resynchronizeClock()`을 수행하고 보정값·왕복시간을 로그에 남겨
드리프트나 시각 변경을 점검하세요. 녹화 도중 시계를 바꾸는 `T` 명령을 이 코드는 보내지 않습니다.

## 4. Forge 모드에서 사용하기

두 Java 파일을 예를 들어 `src/main/java/com/example/mymod/eyetracking/`에 놓고,
두 파일 맨 위에 동일한 `package com.example.mymod.eyetracking;`을 추가하세요.
`com.example.mymod`는 본인의 패키지명으로 바꾸면 됩니다.
다른 패키지에서 호출할 때는 해당 클래스를 import합니다.

아래는 기존 실험 관리 클래스에 넣을 수 있는 예시 메서드입니다.
Forge 이벤트 등록 부분은 기존 모드의 시작/자극 제시/종료 위치에 연결하세요.

```java
private final PupilEventSender eye = new PupilEventSender();
private volatile boolean eyeReady;

// 실험 준비 단계에서 1회 호출
public void prepareEyeTracking() {
    eye.start("127.0.0.1", 50020, 3000,
              PupilEventSender.Transport.IPC_PUB)
       .whenComplete((clock, error) -> {
           if (error != null) {
               eyeReady = false;
               System.err.println("Pupil 준비 실패: " + error);
               return;
           }
           eyeReady = true;
           System.out.println("Pupil 준비 완료: " + clock);
       });
}

// 실제 이벤트 발생 위치에서 호출. label에는 세션/블록/trial 식별자를 넣으세요.
public void onTaskEvent(String label) {
    long eventNanos = System.nanoTime();
    if (!eyeReady) {
        System.err.println("Pupil 미준비: 이벤트 미전송 " + label);
        return;
    }
    eye.markAt(label, eventNanos).whenComplete((result, error) -> {
        if (error != null) {
            eyeReady = false;
            System.err.println("Pupil 전송 실패/결과 불확실: " + label + " " + error);
        } else {
            System.out.println(label + ": " + result);
        }
    });
}

// 실험 종료 시 호출. 모든 전송 Future의 결과도 각각 확인해야 합니다.
public void finishEyeTracking() {
    eyeReady = false;
    eye.finish().thenRun(() -> System.out.println("Pupil 연결 정리 완료"));
}
```

Python 원본과 같은 경로가 필요하면 `Transport.IPC_PUB`를 `Transport.NOTIFICATION`으로 변경합니다.

- `start()`가 성공적으로 끝난 뒤에 실험을 시작하도록 기존 실험 상태와 연결하세요.
- 콜백에서는 예시처럼 로그를 남길 수 있습니다. Minecraft 화면/월드 상태를 바꿀 때는
  게임 메인 스레드로 전달하세요. Future에 `join()`/`get()`을 호출해 게임 스레드를 기다리게 하지 마세요.
- 주시 자극의 소프트웨어 제시 시점을 기록한다면 해당 클라이언트에서 한 번만 발생시키세요.
  클라이언트와 서버 양쪽 핸들러에서 같은 이벤트를 중복 전송하지 않도록 기존 등록 위치를 확인하세요.
- `onTaskEvent()`의 예시 경고를 실제 실험 로그와 실패 상태에 연결하세요. 로그만 출력하면
  기존 task가 계속 진행될 수 있습니다. task의 중지/실패 처리는 기존 모드에서 결정해야 합니다.
- 큐가 가득 차거나 I/O가 실패하면 반환 Future가 실패합니다. 오류를 무시하지 마세요.
  `finish()` 완료는 이전 전송들이 모두 성공했다는 뜻이 아닙니다.
- 전송 도중 timeout은 수신 여부가 불확실합니다. 무조건 재전송하면 중복 마커가 생길 수 있습니다.
  이 코드는 자동 재연결/재전송을 하지 않습니다. 실패 후 새 sender와 새 시각 보정으로 재개하세요.
- `finish()` 이후에는 새 sender를 만들어야 합니다. 세션 동안 같은 객체를 유지합니다.
- PUB의 마지막 이벤트가 녹화에 보이는지 확인한 뒤 Capture 녹화를 종료하세요.

이 어댑터는 순간 이벤트(duration 0)용입니다. 구간 길이가 필요한 경우 core 클라이언트의
`sendAnnotation`/`publishAnnotation`에 초 단위 duration을 전달하도록 작업을 확장할 수 있습니다.

## 5. 단독 연결 확인

JDK 17 환경에서 세 파일이 있는 폴더로 이동한 뒤 실행합니다.

```sh
javac -encoding UTF-8 --release 17 PupilRemoteClient.java PupilEventSender.java
java PupilRemoteClient 127.0.0.1 50020
```

두 번째 줄은 원본과 동일한 notification을 한 번 전송합니다.
공식 PUB 경로로 한 번 보내려면 다음을 사용하세요.

```sh
java PupilRemoteClient 127.0.0.1 50020 --ipc
```

실행 전에 Capture의 Pupil Remote/Network API 주소·포트를 확인하고, Annotation Capture를 켜고
짧은 테스트 녹화를 시작하세요. 종료 후 Pupil Player의 Annotation Player에서 이벤트를 확인하고
export한 `annotations.csv`의 label·개수·timestamp를 대조하세요.
이 코드는 녹화를 자동으로 시작하거나 정지하지 않습니다.

## 6. SEEG와 함께 사용할 때의 시각 의미

이 코드는 Java 이벤트와 Pupil Time 사이의 대응만 처리합니다. SEEG 장비의 시계나 샘플 인덱스는
다루지 않으므로 SEEG와의 동기화가 자동으로 이루어지는 것은 아닙니다.
기존 SEEG 마커와 동일한 event ID를 사용하고, 공통 이벤트의 시각 차이와 세션 중 변화를 확인하세요.
필요한 시간 정밀도는 실제 구성에서 측정해야 합니다.

이벤트 콜백 시각과 모니터의 실제 광학적 자극 시작 시각도 다를 수 있습니다.
실제 화면 onset 정밀도가 필요하면 렌더링 지연까지 별도로 측정해야 합니다.

## 7. 검증 범위와 제한

2026-10-02에 다음을 확인했습니다.

- Java 17.0.20에서 두 제공 클래스 컴파일 성공. 제공 클래스의 `-Xlint:all` 경고 없음.
- 실제 libzmq 4.3.5 REP/SUB와 통신. 검증 환경의 인터페이스 열거 제한 때문에 로컬 TCP 바이트
  릴레이를 통해 양쪽을 연결했으며, ZMTP 메시지는 수정하지 않았습니다.
- 원본 notification payload를 Python msgpack 1.2.1의 `packb(..., use_bin_type=True)`와
  바이트 단위로 대조. Python 패키지는 검증에만 사용되며 Java 실행에는 필요 없습니다.
- 한글, UTF-8 문자열 길이 분기, 31/32/255/256/65535/65536 바이트 라벨,
  짧은/긴 ZMTP 프레임, 분할 수신 및 PUB 구독 수신 확인.
- 연결 handshake timeout, 응답 timeout 후 연결 폐기, peer 연결 종료,
  과도한 프레임 길이 거부 확인.
- 비동기 송신 순서, 캡처한 이벤트 시각 유지, 종료 시 대기열 처리 확인.

Pupil Core 실장비, 실제 Pupil Capture 녹화, 사용자 Forge 프로젝트 빌드 및 SEEG 장비와의
종단 간 검증은 수행하지 못했습니다. 위 단독 테스트 녹화로 실제 저장을 확인한 뒤 적용하세요.

지원 범위는 ZMTP 3.0 NULL의 제한된 구현입니다. 3.1 peer와는 3.0으로 통신합니다.
인증/암호화(CURVE/PLAIN), ZMTP 1/2, heartbeat 확장, 자동 재연결 및 gaze 스트림 수집은 포함하지 않습니다.
실시간 운영체제 수준의 지연 보장도 제공하지 않습니다. 수신 프레임은 최대 1 MiB,
label은 UTF-8 기준 최대 128 KiB로 제한합니다.

## 공식 자료

- [Forge 1.20.1 — Java 17 요구 사항](https://docs.minecraftforge.net/en/1.20.1/gettingstarted/)
- [Pupil Core Network API — t, REQ/REP, notifications, annotations](https://docs.pupil-labs.com/core/developer/network-api/)
- [Pupil Labs 공식 remote_annotations.py](https://github.com/pupil-labs/pupil-helpers/blob/master/python/remote_annotations.py)
- [Pupil Capture — Annotation Capture](https://docs.pupil-labs.com/core/software/pupil-capture/#annotations)
- [Pupil Core 동기화 권장 방법](https://docs.pupil-labs.com/core/best-practices/#synchronization)
- [ZMTP 3.0 규격](https://rfc.zeromq.org/spec/23/)
- [REQ/REP 규격과 빈 delimiter 프레임](https://rfc.zeromq.org/spec/28/)
- [MessagePack 규격](https://github.com/msgpack/msgpack/blob/master/spec.md)
