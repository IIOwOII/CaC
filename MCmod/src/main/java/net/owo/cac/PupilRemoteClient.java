package net.owo.cac;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
* Pupil Core client using only the Java 17 standard library. 
*
* Supported features: TCP, ZMTP 3.0 NULL handshake, REQ/REP, and PUB for annotations.
* This is not a general-purpose ZeroMQ implementation; it does not support CURVE/PLAIN,
* or automatic reconnection/retransmission. 
* Communicates with ZMTP 3.1 peers using the 3.0 protocol. No independent heartbeat mechanism. 
*
* The constructor and all communication methods are blocking; do not execute them
* on the Minecraft main thread. Call them sequentially from a single dedicated worker thread. 
* After an IOException, close the object entirely and perform a fresh connection and clock synchronization. 
* Errors or timeouts during transmission do not trigger automatic retransmission,
* as delivery status is uncertain. 
*
* This code does not start/stop recordings or reset the clock in Pupil Capture.
*/
public class PupilRemoteClient implements AutoCloseable {
    private static final byte[] EMPTY = new byte[0];
    private static final byte[] ANNOTATION = utf8("annotation");
    private static final byte[] NOTIFY_ANNOTATION = utf8("notify.annotation");
    private static final int MAX_FRAME_BYTES = 1024 * 1024;
    private static final int MAX_LABEL_BYTES = 128 * 1024;

    private final String host;
    private final int timeoutMillis;
    private final Wire remote;
    private Wire publisher;

    public PupilRemoteClient(String host, int port, int timeoutMillis) throws IOException {
        this.host = Objects.requireNonNull(host, "host");
        if (port < 1 || port > 65535 || timeoutMillis < 1) {
            throw new IllegalArgumentException("Invalid port or timeoutMillis");
        }
        this.timeoutMillis = timeoutMillis;
        this.remote = new Wire(host, port, timeoutMillis, "REQ", "REP");
    }

    /** Corresponds to Python's send_string("t") + float(recv_string()). Unit: seconds. */
    public synchronized double getTimestamp() throws IOException {
        String reply = requestText("t");
        try {
            double value = Double.parseDouble(reply.trim());
            if (!Double.isFinite(value)) {
                throw new NumberFormatException("Non-finite timestamp");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IOException("Invalid Pupil timestamp reply: " + reply, e);
        }
    }

    /**
	* Equivalent to the Python notify.annotation multipart transmission. 
	* The response indicates receipt by Pupil Remote, not confirmation that the annotation file has been saved. 
	* You must verify whether the annotation is actually saved in the version of Capture you are using. 
	*/
    public synchronized String sendAnnotation(String label, double timestamp,
                                               double duration) throws IOException {
        byte[] payload = packAnnotation(label, timestamp, duration, true);
        return remote.request(NOTIFY_ANNOTATION, payload);
    }

    /**
	* Official remote_annotations.py approach: Look up PUB_PORT and open a separate PUB connection. 
	* Wait for the subscription to receive annotations to arrive. 
	* Call this once during the experiment setup phase and reuse the connection for each event. 
	*/
    public synchronized void openAnnotationPublisher() throws IOException {
        if (publisher != null) {
            throw new IllegalStateException("Annotation publisher is already open");
        }
        String reply = requestText("PUB_PORT").trim();
        final int port;
        try {
            port = Integer.parseInt(reply);
            if (port < 1 || port > 65535) {
                throw new NumberFormatException("Port out of range");
            }
        } catch (NumberFormatException e) {
            throw new IOException("Invalid PUB_PORT reply: " + reply, e);
        }
        Wire candidate = new Wire(host, port, timeoutMillis, "PUB", "SUB", "XSUB");
        try {
            candidate.awaitSubscription(ANNOTATION);
            publisher = candidate;
        } catch (IOException | RuntimeException e) {
            candidate.close();
            throw e;
        }
    }

    /**
	* Sends a ["annotation", MessagePack] message via PUB, identical to the official example. 
	* The return value indicates only that the local socket write has completed; there is no receive ACK for PUB. 
	*/
    public synchronized void publishAnnotation(String label, double timestamp,
                                                double duration) throws IOException {
        if (publisher == null) {
            throw new IllegalStateException("Call openAnnotationPublisher() first");
        }
        publisher.publish(ANNOTATION, packAnnotation(label, timestamp, duration, false));
    }

    /**
	* Queries 't' multiple times and synchronizes with the JVM clock using the sample with the shortest round-trip time. 
	* This relies on the approximation that latency is symmetric for the round trip. It does not synchronize the SEEG clock. 
	* Perform this again after reconnection, restarting the capture, or resetting the clock. 
	*/
    public synchronized ClockMapping synchronizeClock(int samples) throws IOException {
        if (samples < 1 || samples > 100) {
            throw new IllegalArgumentException("samples must be 1..100");
        }
        ClockMapping best = null;
        for (int i = 0; i < samples; i++) {
            long before = System.nanoTime();
            double pupil = getTimestamp();
            long after = System.nanoTime();
            long rtt = after - before;
            if (best == null || rtt < best.roundTripNanos()) {
                best = new ClockMapping(before + rtt / 2, pupil, rtt);
            }
        }
        return best;
    }

    /** Convert only System.nanoTime() values from the same JVM. The unit is seconds. */
    public record ClockMapping(long localMidpointNanos, double pupilSeconds,
                               long roundTripNanos) {
        public double toPupilTime(long eventNanos) {
            return pupilSeconds + (eventNanos - localMidpointNanos) / 1_000_000_000.0;
        }

        public double roundTripMillis() {
            return roundTripNanos / 1_000_000.0;
        }
    }

    private String requestText(String text) throws IOException {
        return remote.request(utf8(text));
    }

    @Override
    public synchronized void close() {
        if (publisher != null) {
            publisher.close();
            publisher = null;
        }
        remote.close();
    }

    /** Instead of a full MessagePack implementation, encode only the formats required for this fixed structure. */
    private static byte[] packAnnotation(String label, double timestamp, double duration,
                                         boolean notification) throws IOException {
        Objects.requireNonNull(label, "label");
        if (!Double.isFinite(timestamp) || !Double.isFinite(duration) || duration < 0.0) {
            throw new IllegalArgumentException("timestamp/duration must be finite; duration >= 0");
        }
        // Limit large inputs before getBytes. The UTF-8 length is re-checked below.
        if (label.length() > MAX_LABEL_BYTES) {
            throw new IllegalArgumentException("Label is too large");
        }
        byte[] labelBytes = utf8(label);
        if (labelBytes.length > MAX_LABEL_BYTES) {
            throw new IllegalArgumentException("Label exceeds 128 KiB in UTF-8");
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        out.writeByte(notification ? 0x85 : 0x84); // fixmap: 5 or 4
        if (notification) {
            packString(out, utf8("subject"));
            packString(out, ANNOTATION);
        }
        packString(out, utf8("topic"));
        packString(out, ANNOTATION);
        packString(out, utf8("label"));
        packString(out, labelBytes);
        packString(out, utf8("timestamp"));
        out.writeByte(0xcb); // MessagePack float64, big endian
        out.writeDouble(timestamp);
        packString(out, utf8("duration"));
        out.writeByte(0xcb);
        out.writeDouble(duration);
        return bytes.toByteArray();
    }

    private static void packString(DataOutputStream out, byte[] bytes) throws IOException {
        int n = bytes.length; // UTF-8 byte length
        if (n < 32) {
            out.writeByte(0xa0 | n);
        } else if (n < 256) {
            out.writeByte(0xd9);
            out.writeByte(n);
        } else if (n < 65536) {
            out.writeByte(0xda);
            out.writeShort(n);
        } else {
            out.writeByte(0xdb);
            out.writeInt(n);
        }
        out.write(bytes);
    }

    private static byte[] utf8(String text) {
        return text.getBytes(StandardCharsets.UTF_8);
    }

    private record Frame(int flags, byte[] body) {
        boolean more() { return (flags & 1) != 0; }
        boolean command() { return (flags & 4) != 0; }
    }

    /** Limited ZMTP implementation. Uses an NIO selector to enforce timeouts for both read and write operations. */
    private static final class Wire implements AutoCloseable {
        private final SocketChannel channel;
        private final Selector selector;
        private final SelectionKey key;
        private final long timeoutNanos;

        Wire(String host, int port, int timeoutMillis, String socketType,
             String... acceptedPeerTypes) throws IOException {
            timeoutNanos = timeoutMillis * 1_000_000L;
            // DNS lookup time is not included in the I/O deadline below. The default is the numeric IP.
            InetSocketAddress address = new InetSocketAddress(host, port);
            channel = SocketChannel.open();
            try {
                selector = Selector.open();
            } catch (IOException | RuntimeException e) {
                channel.close();
                throw e;
            }
            try {
                channel.configureBlocking(false);
                channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
                key = channel.register(selector, 0);
                long end = deadline();
                if (!channel.connect(address)) {
                    while (!channel.finishConnect()) {
                        await(SelectionKey.OP_CONNECT, end);
                    }
                }
                handshake(socketType, acceptedPeerTypes, end);
            } catch (IOException | RuntimeException e) {
                close();
                throw e;
            }
        }

        String request(byte[]... parts) throws IOException {
            try {
                long end = deadline();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(bytes);
                writeFrame(out, 1, EMPTY); // REQ delimiter frame
                for (int i = 0; i < parts.length; i++) {
                    writeFrame(out, i + 1 < parts.length ? 1 : 0, parts[i]);
                }
                writeAll(bytes.toByteArray(), end);
                Frame delimiter = readDataFrame(end);
                if (delimiter.body().length != 0 || !delimiter.more()) {
                    throw new IOException("Invalid REP delimiter");
                }
                Frame reply = readDataFrame(end);
                if (reply.more()) {
                    throw new IOException("Expected a single text reply from Pupil Remote");
                }
                return new String(reply.body(), StandardCharsets.UTF_8);
            } catch (IOException | RuntimeException e) {
                close(); // Do not mistake a delayed response arriving after the timeout for the response to the next request.
                throw e;
            }
        }

        void publish(byte[] topic, byte[] payload) throws IOException {
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(bytes);
                writeFrame(out, 1, topic);
                writeFrame(out, 0, payload);
                writeAll(bytes.toByteArray(), deadline());
            } catch (IOException | RuntimeException e) {
                close();
                throw e;
            }
        }

        void awaitSubscription(byte[] topic) throws IOException {
            long end = deadline();
            while (true) {
                Frame frame = readDataFrame(end);
                byte[] body = frame.body();
                if (frame.more() || body.length == 0 || (body[0] != 0 && body[0] != 1)) {
                    throw new IOException("Invalid ZMTP 3.0 subscription");
                }
                int prefixLength = body.length - 1;
                if (body[0] != 1 || prefixLength > topic.length) continue;
                boolean matches = true;
                for (int i = 0; i < prefixLength; i++) {
                    if (body[i + 1] != topic[i]) matches = false;
                }
                if (matches) return;
            }
        }

        private void handshake(String type, String[] accepted, long end) throws IOException {
            byte[] greeting = new byte[64];
            greeting[0] = (byte) 0xff;
            greeting[8] = 1; // anonymous identity ZMTP signature
            greeting[9] = 0x7f;
            greeting[10] = 3;
            greeting[11] = 0;
            System.arraycopy(utf8("NULL"), 0, greeting, 12, 4);
            writeAll(greeting, end);
            byte[] peer = readExact(64, end);
            if (peer[0] != (byte) 0xff || peer[9] != 0x7f || peer[10] != 3) {
                throw new IOException("This client requires a ZMTP 3.x peer");
            }
            if (!Arrays.equals(Arrays.copyOfRange(greeting, 12, 32),
                               Arrays.copyOfRange(peer, 12, 32))) {
                throw new IOException("Only the NULL mechanism is supported");
            }
            ByteArrayOutputStream body = new ByteArrayOutputStream();
            DataOutputStream data = new DataOutputStream(body);
            data.writeByte(5);
            data.write(utf8("READY"));
            byte[] name = utf8("Socket-Type");
            data.writeByte(name.length);
            data.write(name);
            byte[] value = utf8(type);
            data.writeInt(value.length);
            data.write(value);
            ByteArrayOutputStream framed = new ByteArrayOutputStream();
            writeFrame(new DataOutputStream(framed), 4, body.toByteArray());
            writeAll(framed.toByteArray(), end);

            Frame ready = readFrame(end);
            if (!ready.command() || ready.more()) throw new IOException("Expected READY");
            byte[] command = ready.body();
            String commandName = commandName(command);
            if (!commandName.equals("READY")) {
                throw new IOException("Expected READY, received ZMTP " + commandName);
            }
            DataInputStream metadata = new DataInputStream(new ByteArrayInputStream(
                    command, 6, command.length - 6));
            String peerType = null;
            while (metadata.available() > 0) {
                int n = metadata.readUnsignedByte();
                if (n == 0 || n > metadata.available()) throw new IOException("Invalid metadata name");
                String property = new String(metadata.readNBytes(n), StandardCharsets.US_ASCII);
                int length = metadata.readInt();
                if (length < 0 || length > metadata.available()) throw new IOException("Invalid metadata value");
                byte[] propertyValue = metadata.readNBytes(length);
                if (property.equalsIgnoreCase("Socket-Type")) {
                    peerType = new String(propertyValue, StandardCharsets.US_ASCII);
                }
            }
            for (String allowed : accepted) {
                if (allowed.equals(peerType)) return;
            }
            throw new IOException("Unexpected peer Socket-Type: " + peerType);
        }

        private Frame readDataFrame(long end) throws IOException {
            Frame frame = readFrame(end);
            if (frame.command()) {
                throw new IOException("Unsupported ZMTP command: " + commandName(frame.body()));
            }
            return frame;
        }

        private static String commandName(byte[] body) throws IOException {
            if (body.length == 0) throw new IOException("Empty ZMTP command");
            int length = body[0] & 0xff;
            if (length == 0 || length > body.length - 1) throw new IOException("Invalid command name");
            return new String(body, 1, length, StandardCharsets.US_ASCII);
        }

        private Frame readFrame(long end) throws IOException {
            int flags = readExact(1, end)[0] & 0xff;
            if ((flags & ~7) != 0 || (flags & 5) == 5) throw new IOException("Invalid frame flags");
            long length = (flags & 2) != 0
                    ? ByteBuffer.wrap(readExact(8, end)).getLong()
                    : readExact(1, end)[0] & 0xff;
            if (length < 0 || length > MAX_FRAME_BYTES) throw new IOException("Invalid/oversized frame");
            return new Frame(flags, readExact((int) length, end));
        }

        private static void writeFrame(DataOutputStream out, int flags, byte[] body) throws IOException {
            if (body.length > MAX_FRAME_BYTES) throw new IOException("Outgoing frame is too large");
            boolean large = body.length > 255;
            out.writeByte(flags | (large ? 2 : 0));
            if (large) out.writeLong(body.length);
            else out.writeByte(body.length);
            out.write(body);
        }

        private byte[] readExact(int count, long end) throws IOException {
            ByteBuffer buffer = ByteBuffer.allocate(count);
            while (buffer.hasRemaining()) {
                checkDeadline(end);
                int n = channel.read(buffer);
                if (n < 0) throw new EOFException("Peer closed the connection");
                if (n == 0) await(SelectionKey.OP_READ, end);
            }
            return buffer.array();
        }

        private void writeAll(byte[] bytes, long end) throws IOException {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            while (buffer.hasRemaining()) {
                checkDeadline(end);
                if (channel.write(buffer) == 0) await(SelectionKey.OP_WRITE, end);
            }
        }

        private long deadline() {
            return System.nanoTime() + timeoutNanos;
        }

        private static long checkDeadline(long end) throws IOException {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedIOException("Pupil I/O interrupted");
            }
            long remaining = end - System.nanoTime();
            if (remaining <= 0) throw new SocketTimeoutException("Pupil I/O deadline exceeded");
            return remaining;
        }

        private void await(int operation, long end) throws IOException {
            long remaining = checkDeadline(end);
            if (!channel.isOpen()) throw new IOException("Connection is closed");
            key.interestOps(operation);
            selector.select(Math.max(1, (remaining + 999_999) / 1_000_000));
            selector.selectedKeys().clear();
        }

        @Override
        public void close() {
            try { channel.close(); } catch (IOException ignored) { }
            try { selector.close(); } catch (IOException ignored) { }
        }
    }

    /**
	* Standalone execution: java PupilRemoteClient [host] [port] [--ipc]
	* Default execution sends a single notification identical to the one in the Python example. 
	* Execution with --ipc sends a single annotation via the official PUB path. 
	* Please ensure the Capture Recording and Annotation Capture plugins are enabled beforehand. 
	*/
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 50020;
        try (PupilRemoteClient client = new PupilRemoteClient(host, port, 3000)) {
            if (args.length > 2 && args[2].equals("--ipc")) {
                client.openAnnotationPublisher();
                ClockMapping clock = client.synchronizeClock(10);
                double timestamp = clock.toPupilTime(System.nanoTime());
                client.publishAnnotation("event_1", timestamp, 0.0);
                System.out.println("PUB write completed; verify annotation in the recording.");
            } else {
                double timestamp = client.getTimestamp();
                System.out.println(client.sendAnnotation("event_1", timestamp, 0.0));
            }
        }
    }
}
