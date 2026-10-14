import zmq
import msgpack

ctx = zmq.Context()
remote = ctx.socket(zmq.REQ)
remote.connect("tcp://127.0.0.1:50020")

remote.send_string("t")
timestamp = float(remote.recv_string())

remote.send_multipart([
    b"notify.annotation",
    msgpack.packb({
        "subject": "annotation",
        "topic": "annotation",
        "label": "event_1",
        "timestamp": timestamp,
        "duration": 0.0,
    }, use_bin_type=True),
])
print(remote.recv_string())