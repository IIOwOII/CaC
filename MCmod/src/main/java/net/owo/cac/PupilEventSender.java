package net.owo.cac;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.owo.cac.PupilRemoteClient;

/**
* An optional asynchronous adapter for use with Forge event handlers. Requires Java 17.
* Call mark() or markAt() after start() completes, and call finish() when the experiment ends. 
* Ensure that any failure of the returned Future is reflected in the experiment log or status. 
* Use a single object instance for one experiment session; create a new object to resume after a failure. 
*/
public final class PupilEventSender {
    public enum Transport { NOTIFICATION, IPC_PUB }

    private final ThreadPoolExecutor worker;
    private final CompletableFuture<Void> finished = new CompletableFuture<>();
    private volatile PupilRemoteClient.ClockMapping clock;
    private PupilRemoteClient client;
    private Transport transport;
    private boolean started;
    private boolean finishing;
    private IOException ioFailure;

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

    /** Starts the connection and visual calibration without blocking the main thread. */
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

    /** The moment of the call is captured as the event time. Network transmission occurs later. */
    public CompletableFuture<String> mark(String label) {
        long eventNanos = System.nanoTime();
        return markAt(label, eventNanos);
    }

    /** Pass the System.nanoTime() value obtained at the moment of the event within the same JVM. */
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

    /** Call this between blocks or similar locations. The new value will be applied to events occurring after the correction value is updated. */
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
	* Stops accepting new events, processes already accepted tasks, and then closes the connection. 
	* The returned Future indicates the completion of socket cleanup but does not guarantee the success of preceding events. 
	* When using PUB, ensure the final event is visible in Capture before stopping the recording. 
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
            // Even if the queue is full, it does not send from the game thread instead or silently discard the data.
            result.completeExceptionally(e);
        }
        return result;
    }
}