// PupilRemoteClient.java와 같은 package 선언을 추가하세요.
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Forge 이벤트 핸들러에서 사용할 수 있는 선택적 비동기 어댑터. Java 17만 필요합니다.
 * start() 완료 후 mark()/markAt()을 호출하고, 실험 종료 시 finish()를 호출하세요.
 * 반환된 Future의 실패를 반드시 실험 로그/상태에 반영하세요.
 * 하나의 객체는 한 실험 세션에 사용합니다. 실패 후 재개하려면 새 객체를 만드세요.
 */
public final class PupilEventSender {
    public enum Transport { NOTIFICATION, IPC_PUB }

    private final ThreadPoolExecutor worker;
    private final CompletableFuture<Void> finished = new CompletableFuture<>();
    private volatile PupilRemoteClient.ClockMapping clock;
    private PupilRemoteClient client; // 작업 스레드에서만 접근
    private Transport transport;
    private boolean started;
    private boolean finishing;
    private IOException ioFailure; // 작업 스레드에서만 접근

    public PupilEventSender() {
        worker = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(256), runnable -> {
                    Thread thread = new Thread(runnable, "pupil-events");
                    thread.setDaemon(true);
                    return thread;
                }, new ThreadPoolExecutor.AbortPolicy()) {
            @Override
            protected void terminated() {
                if (client != null) client.close();
                finished.complete(null);
            }
        };
    }

    /** 메인 스레드를 기다리게 하지 않고 연결과 시각 보정을 시작합니다. */
    public synchronized CompletableFuture<PupilRemoteClient.ClockMapping> start(
            String host, int port, int timeoutMillis, Transport transport) {
        Objects.requireNonNull(transport, "transport");
        if (started || finishing) {
            return CompletableFuture.failedFuture(new IllegalStateException("Already started/finished"));
        }
        started = true;
        return enqueue(() -> {
            this.transport = transport;
            client = new PupilRemoteClient(host, port, timeoutMillis);
            if (transport == Transport.IPC_PUB) client.openAnnotationPublisher();
            clock = client.synchronizeClock(10);
            return clock;
        });
    }

    /** 호출 순간을 이벤트 시각으로 잡습니다. 네트워크 송신은 나중에 수행됩니다. */
    public CompletableFuture<String> mark(String label) {
        long eventNanos = System.nanoTime();
        return markAt(label, eventNanos);
    }

    /** 같은 JVM에서 이벤트 순간에 얻은 System.nanoTime()을 전달하세요. */
    public synchronized CompletableFuture<String> markAt(String label, long eventNanos) {
        PupilRemoteClient.ClockMapping snapshot = clock;
        if (finishing || snapshot == null) {
            return CompletableFuture.failedFuture(new IllegalStateException("Pupil sender is not ready"));
        }
        return enqueue(() -> {
            double timestamp = snapshot.toPupilTime(eventNanos);
            if (transport == Transport.NOTIFICATION) {
                String reply = client.sendAnnotation(label, timestamp, 0.0);
                if (!reply.trim().equals("Notification received")) {
                    throw new IOException("Unexpected notification reply: " + reply);
                }
                return reply;
            }
            client.publishAnnotation(label, timestamp, 0.0);
            return "PUB write completed (no receiver acknowledgement)";
        });
    }

    /** 블록 사이 등에서 호출하세요. 보정값 갱신 이후의 이벤트에 새 값이 적용됩니다. */
    public synchronized CompletableFuture<PupilRemoteClient.ClockMapping> resynchronizeClock() {
        if (finishing || clock == null) {
            return CompletableFuture.failedFuture(new IllegalStateException("Pupil sender is not ready"));
        }
        return enqueue(() -> {
            clock = client.synchronizeClock(10);
            return clock;
        });
    }

    /**
     * 새 이벤트 접수를 중단하고 이미 접수한 작업을 처리한 뒤 연결을 닫습니다.
     * 반환 Future는 소켓 정리 완료를 뜻하며, 앞선 이벤트의 성공을 보증하지 않습니다.
     * PUB 사용 시에는 Capture에서 마지막 이벤트가 보이는지 확인하고 녹화를 종료하세요.
     */
    public synchronized CompletableFuture<Void> finish() {
        finishing = true;
        worker.shutdown();
        return finished;
    }

    private <T> CompletableFuture<T> enqueue(Callable<T> action) {
        CompletableFuture<T> result = new CompletableFuture<>();
        try {
            worker.execute(() -> {
                try {
                    if (ioFailure != null) {
                        throw new IOException("Earlier Pupil I/O failed; create a new sender", ioFailure);
                    }
                    result.complete(action.call());
                } catch (Exception e) {
                    if (e instanceof IOException && ioFailure == null) {
                        ioFailure = (IOException) e;
                        clock = null;
                        if (client != null) client.close();
                    }
                    result.completeExceptionally(e);
                }
            });
        } catch (RejectedExecutionException e) {
            // 큐가 가득 차도 게임 스레드에서 대신 송신하거나 조용히 버리지 않습니다.
            result.completeExceptionally(e);
        }
        return result;
    }
}
