import zmq
import time

context = zmq.Context()
socket = context.socket(zmq.REP) # REP: Reply (응답)
socket.bind("tcp://127.0.0.1:50020")

print("서버가 시작되었습니다...")
while True:
    # 클라이언트로부터 메시지를 수신
    message = socket.recv()
    print(f"받은 요청: {message.decode('utf-8')}")

    # 0.1초 대기 후 응답
    time.sleep(0.1)
    socket.send(b"World")
