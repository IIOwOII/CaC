package net.owo.cac;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;

import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.owo.cac.CacMod;
import net.owo.cac.network.CacModVariables;
import net.owo.cac.CstState;
import net.owo.cac.CstRenderHandler;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstReplay {
    // The status of recording
    public static boolean is_record = false;
    private static final AtomicBoolean RECORDING = new AtomicBoolean(false); // If render system uses multithreads, we need to make the one group value from them.
    private static final AtomicBoolean STOP_REQUEST = new AtomicBoolean(false);

    // Thread
    private static Thread writerThread;
	private static Thread ffmpegThread;

    // Queue
    private static final int POOL_MAX = 32;
    private static final BlockingQueue<FramePacket> POOL_FRAME = new ArrayBlockingQueue<>(POOL_MAX);
    private static final ArrayDeque<ByteBuffer> POOL_BUFFER = new ArrayDeque<>();
    private static final Object STATE_LOCK = new Object();

    // file
    private static Process ffmpegProcess;
    private static OutputStream ffmpegInput;
    private static Path fileRaw;
    private static Path fileMeta;

    // Parameter
    private static int WIDTH;
    private static int HEIGHT;
    private static int FPS; // frame per second
    private static long NPF; // nanosecond per frame
    private static int BPF; // byte per frame
    private static long T_START; // recording start time
    private static long T_END; // recording end time
    private static long T_QUE; // Ideal time of capture

	// Info of frames
	private static long frameLoaded = 0;
    private static long frameWritten = 0;
    private static long frameDropped = 0;

    // profiling
	private static long captureTimeNsTotal = 0L;
	private static long captureTimeNsMax = 0L;
	
	private static long enqueueFailCount = 0L;
	
	private static long writeTimeNsTotal = 0L;
	private static long writeTimeNsMax = 0L;
	
	private static long queueSizeMax = 0L;
	private static long queueSizeSamples = 0L;
	private static long queueSizeSum = 0L;
	
	private static long statsLastPrintNs = 0L;
	private static final long STATS_PRINT_INTERVAL_NS = 1_000_000_000L; // 1 sec

	// get state of recording
	public static boolean isRecording() {
		return RECORDING.get();
	}
	
	// Toggle
	public static void toggleRecording(int fps) {
		if (isRecording()) {
			stopRecording();
		} else {
			startRecording(fps);
		}
	}

	// get Name
	public static String getName() {
		String dir_file = (Paths.get(FMLPaths.GAMEDIR.get().toString(), "cacutil", "replays")).toString();
		if ((CacModVariables.Exp_subject).equals("none")) {
			String name = "test";
			int num_file = CstState.getNumFiles(dir_file, name, "mp4");
			if (num_file != 0) {
				name = name + "_" + new java.text.DecimalFormat("##").format(num_file);
			}
			return name;
		}
		String name = new java.text.SimpleDateFormat("yyMMdd").format(Calendar.getInstance().getTime()) + "_" + CacModVariables.Exp_subject + "_" + CacModVariables.Exp_session;
		int num_file = CstState.getNumFiles(dir_file, name, "mp4");
		if (num_file != 0) {
			name = name + "_" + new java.text.DecimalFormat("##").format(num_file);
		}
		return name;
	}
	
    // start
    public static void startRecording(int fps) {
    	synchronized (STATE_LOCK) {
    		if (RECORDING.get()) return;
    		Minecraft mc = Minecraft.getInstance();
    		RenderTarget frame = mc.getMainRenderTarget();
	        if (mc.player == null || mc.level == null) return; // ex. Title
	        if (frame == null) return;
	        try {
	        	is_record = true;
	        	CstRenderHandler.camicon_timer = 60;
	        	
	        	// init metadata
	        	frameLoaded = 0L;
	        	frameWritten = 0L;
				frameDropped = 0L;

				// For debug
				captureTimeNsTotal = 0L;
				captureTimeNsMax = 0L;
				enqueueFailCount = 0L;
				writeTimeNsTotal = 0L;
				writeTimeNsMax = 0L;
				queueSizeMax = 0L;
				queueSizeSamples = 0L;
				queueSizeSum = 0L;
				statsLastPrintNs = System.nanoTime();
				
	        	// info of mov
		        WIDTH = frame.width;
		        HEIGHT = frame.height;
		        FPS = Math.max(1, fps);
		        NPF = 1_000_000_000L/FPS;
		
		        T_START = System.nanoTime(); // recording start time (absolute)
		        T_END = 0L;
		        T_QUE = T_START + NPF;
		        BPF = frame.width * frame.height * 3; // RGB24 = 3 bytes/pixel

		        POOL_FRAME.clear();
		        POOL_BUFFER.clear();
				
		        // file setting
		        Path dir_file = Paths.get(FMLPaths.GAMEDIR.get().toString(), "cacutil", "replays");
		        fileRaw = dir_file.resolve(getName() + ".mp4"); // raw file
		        fileMeta = dir_file.resolve(getName() + ".txt"); // meta file

				// pool reset
				for (int i=0; i<POOL_MAX; i++) {
                    POOL_BUFFER.add(BufferUtils.createByteBuffer(BPF));
                }

				startConvertProcess();

                STOP_REQUEST.set(false);
                RECORDING.set(true);

		        // writer setting
		        writerThread = new Thread(CstReplay::writeFrame, "cac-replay-writer"); // thread allocate
		        writerThread.setDaemon(false);
		        writerThread.start();

				// go!
		        CacMod.LOGGER.info("Recording started: {}x{} @ {}fps", WIDTH, HEIGHT, FPS);
	        } catch (IOException e) {
	        	CacMod.LOGGER.error("Failed to start recording.", e);
	        	resetRecording();
	        }
    	}
    }

    // end
    public static void stopRecording() {
    	Thread saveThread;
    	synchronized (STATE_LOCK) {
    		is_record = false;
    		CstRenderHandler.camicon_timer = 60;
    		if (!RECORDING.get() && writerThread == null) return;
            T_END = System.nanoTime();
            STOP_REQUEST.set(true);
            RECORDING.set(false);
            saveThread = writerThread;
    	}
        if (saveThread != null && saveThread != Thread.currentThread()) {
            try {
                saveThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                CacMod.LOGGER.warn("Interrupted while waiting for writer thread to finish.");
            }
        }
    }

    // Reset and clean up
    public static void resetRecording() {
    	RECORDING.set(false);
    	STOP_REQUEST.set(false);
    	POOL_FRAME.clear();
    	POOL_BUFFER.clear();
    	closeChannel();
    	removeConvertProcess();
    	writerThread = null;
    }

	
    // frame by frame
    public static void readFrame() {
    	long t_curr = System.nanoTime();
    	if (!RECORDING.get()) return;
        if (t_curr < T_QUE) return; // only capture the frame by FPS

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
        	stopRecording();
        	return;
        }
        RenderTarget frame = mc.getMainRenderTarget();
        if (frame == null) {
        	stopRecording();
        	CacMod.LOGGER.info("Frame is null!");
        	return;
        }
        if (frame.width != WIDTH || frame.height != HEIGHT) { // If resolution is changed
            stopRecording();
            CacMod.LOGGER.info("Do not change the resolution during recording.");
            return;
        }

        // GPU to CPU(ByteBuffer)
        ByteBuffer buffer = borrowBuffer(); // create the empty buffer to contain RGB24
		if (buffer == null) {
			frameDropped++; // if frame pool and buffer pool are stucked, frame drop
			T_QUE += NPF;
            if (t_curr-T_QUE > NPF*4L) { // if render lag is very long, time que adjusted
                T_QUE = t_curr + NPF;
            }
            printStatsIfNeeded(); // debug lag
            return;
		}
		
        try {
        	buffer.clear();
			long captureStartNs = System.nanoTime(); // debug
			
        	frame.bindRead(); // Binding the framebuffer for reading
        	RenderSystem.pixelStore(GL11.GL_PACK_ALIGNMENT, 1); // Force packing alignment to 1 to prevent row padding (1 row next is 2 row.)
        	// Read RGB24 Bytes
	        RenderSystem.readPixels(
	                0, 0, // Read Start Point
	                WIDTH, HEIGHT, // Size
	                GL11.GL_RGB, // RGB
	                GL11.GL_UNSIGNED_BYTE, // 255,255,255 Style
	                buffer // Predefined CPU memory to contain the pixel data
	        );
	        frame.unbindRead(); // Maybe like the file.close()
	
			// debug
			long captureElapsedNs = System.nanoTime() - captureStartNs;
			captureTimeNsTotal += captureElapsedNs;
			if (captureElapsedNs > captureTimeNsMax) {captureTimeNsMax = captureElapsedNs;}
			
	        // buffer rewind
	        buffer.position(0); // return to first place. (to prepare the next frame)
	        buffer.limit(BPF);
	
			// frame manage
	        FramePacket packet = new FramePacket(buffer, frameLoaded);
	        boolean offered = POOL_FRAME.offer(packet);
	
	        int queueSizeNow = POOL_FRAME.size();
			queueSizeSum += queueSizeNow;
			queueSizeSamples++;
			if (queueSizeNow > queueSizeMax) {
			    queueSizeMax = queueSizeNow;
			}
			
	        if (!offered) {
	        	recycleBuffer(buffer); // if frame pool is full, drop frame
	        	frameDropped++;
	        	enqueueFailCount++;
	        } else {
	        	frameLoaded++;
	        }

	        // FPS check
	        T_QUE += NPF;
	        if (t_curr-T_QUE > NPF*4L) {
	            T_QUE = t_curr + NPF;
	        }
        } catch (Exception e) {
        	recycleBuffer(buffer);
        	CacMod.LOGGER.error("Failed to capture frame.", e);
            stopRecording();
        }
    }

	//
    private static void writeFrame() {
    	int exitCode = -1;
		try {
    		while (true) {
    			if (STOP_REQUEST.get() && POOL_FRAME.isEmpty()) break;
    			FramePacket packet = POOL_FRAME.poll(100, TimeUnit.MILLISECONDS); // waiting the frame until 100ms
    			if (packet == null) continue;
    			
    			long writeStartNs = System.nanoTime();
    			writePacket(packet);
    			long writeElapsedNs = System.nanoTime() - writeStartNs;
    			
				writeTimeNsTotal += writeElapsedNs;
				if (writeElapsedNs > writeTimeNsMax) {writeTimeNsMax = writeElapsedNs;}
				
    			frameWritten++;
    			recycleBuffer(packet.buffer());
    		}
    		
    		// ffmpeg EOF
    		closeChannel();
    		if (ffmpegProcess != null) {
    			exitCode = ffmpegProcess.waitFor();
    		}
    		writeMetadata(exitCode);
    		CacMod.LOGGER.info(
                    "Recording finished. loaded={}, written={}, dropped={}, ffmpegExit={}",
                    frameLoaded, frameWritten, frameDropped, exitCode);
    	} catch (InterruptedException e) {
    		Thread.currentThread().interrupt();
    		CacMod.LOGGER.warn("Writer thread interrupted.");
    	} catch (Exception e) {
    		CacMod.LOGGER.error("Writer loop failed", e);
    	} finally {
    		synchronized (STATE_LOCK) {
    			closeChannel();
    			removeConvertProcess();
    			writerThread = null;
    			STOP_REQUEST.set(false);
    			RECORDING.set(false);
    			POOL_FRAME.clear();
    			POOL_BUFFER.clear();
    		}
    	}
    }

	// write byte
    private static void writePacket(FramePacket packet) throws IOException {
        if (ffmpegInput == null) {
            throw new IOException("ffmpeg input stream is null");
        }
        ByteBuffer buf = packet.buffer();
        if (buf.hasArray()) {
            ffmpegInput.write(buf.array(), buf.position(), buf.remaining());
            buf.position(buf.limit());
            return;
        }
        byte[] tmp = new byte[Math.min(buf.remaining(), 64 * 1024)];
        while (buf.hasRemaining()) {
            int len = Math.min(buf.remaining(), tmp.length);
            buf.get(tmp, 0, len);
            ffmpegInput.write(tmp, 0, len);
        }
    }

    private static void startConvertProcess() throws IOException {
        List<String> cmd = new ArrayList<>();
        cmd.add("ffmpeg");
        cmd.add("-y");

        // rawvideo를 stdin(pipe:0)으로 받음
        cmd.add("-f");
        cmd.add("rawvideo");
        cmd.add("-pix_fmt");
        cmd.add("rgb24");
        cmd.add("-s");
        cmd.add(WIDTH + "x" + HEIGHT);
        cmd.add("-r");
        cmd.add(String.valueOf(FPS));
        cmd.add("-i");
        cmd.add("pipe:0");

        // OpenGL readPixels는 보통 bottom-up이므로 뒤집기
        cmd.add("-vf");
        cmd.add("vflip");

        // 호환성 좋은 H.264
        cmd.add("-an");
        cmd.add("-c:v");
        cmd.add("libx264");
        cmd.add("-preset");
        cmd.add("veryfast");
        cmd.add("-crf");
        cmd.add("18");
        cmd.add("-pix_fmt");
        cmd.add("yuv420p");

        cmd.add(fileRaw.toAbsolutePath().toString());

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true); // stderr+stdout 합치기
        ffmpegProcess = pb.start();
		
        ffmpegInput = new BufferedOutputStream(ffmpegProcess.getOutputStream(), 1024 * 1024);
		
        ffmpegThread = new Thread(() -> drainProcessLog(ffmpegProcess.getInputStream()), "cac-ffmpeg-log");
        ffmpegThread.setDaemon(true);
        ffmpegThread.start();

        CacMod.LOGGER.info("Started ffmpeg: {}", String.join(" ", cmd));
    }

    private static void drainProcessLog(InputStream inputStream) {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            byte[] buffer = new byte[1024];
            int n;
            StringBuilder sb = new StringBuilder();

            while ((n = bis.read(buffer)) != -1) {
                for (int i = 0; i < n; i++) {
                    char c = (char) buffer[i];
                    if (c == '\n' || c == '\r') {
                        if (sb.length() > 0) {
                            CacMod.LOGGER.info("[ffmpeg] {}", sb.toString());
                            sb.setLength(0);
                        }
                    } else {
                        sb.append(c);
                    }
                }
            }

            if (sb.length() > 0) {
                CacMod.LOGGER.info("[ffmpeg] {}", sb.toString());
            }
        } catch (IOException e) {
            CacMod.LOGGER.warn("Failed to read ffmpeg log.", e);
        }
    }

	//
	private static void writeMetadata(int ffmpegExitCode) {
        double duration = (T_END > T_START) ? (T_END-T_START)/1_000_000_000.0 : 0.0;
        try (BufferedWriter bw = Files.newBufferedWriter(
                fileMeta,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE))
        {
        	// info
            bw.write("width: " + WIDTH);
            bw.newLine();
            bw.write("height: " + HEIGHT);
            bw.newLine();
            bw.write("fps: " + FPS);
            bw.newLine();
            bw.write("pixel_format: rgb24");
            bw.newLine();
            bw.write("frame_loaded: " + frameLoaded);
            bw.newLine();
            bw.write("frame_written: " + frameWritten);
            bw.newLine();
            bw.write("frame_dropped: " + frameDropped);
            bw.newLine();
            bw.write("duration(sec): " + duration);
            bw.newLine();
            bw.write("output: " + fileRaw.toAbsolutePath());
            bw.newLine();
            bw.write("ffmpeg_exit_code: " + ffmpegExitCode);
            bw.newLine();

            // debug
            double avgCaptureMs = (frameLoaded > 0) ? (captureTimeNsTotal / 1_000_000.0) / frameLoaded : 0.0;
			double avgWriteMs = (frameWritten > 0) ? (writeTimeNsTotal / 1_000_000.0) / frameWritten : 0.0;
			double avgQueueSize = (queueSizeSamples > 0) ? ((double) queueSizeSum / (double) queueSizeSamples) : 0.0;
			bw.write("enqueue_fail_count: " + enqueueFailCount);
			bw.newLine();
			bw.write("queue_size_max: " + queueSizeMax);
			bw.newLine();
			bw.write("queue_size_avg: " + avgQueueSize);
			bw.newLine();
			bw.write("capture_avg_ms: " + avgCaptureMs);
			bw.newLine();
			bw.write("capture_max_ms: " + (captureTimeNsMax / 1_000_000.0));
			bw.newLine();
			bw.write("write_avg_ms: " + avgWriteMs);
			bw.newLine();
			bw.write("write_max_ms: " + (writeTimeNsMax / 1_000_000.0));
			bw.newLine();
        } catch (IOException e) {
            CacMod.LOGGER.warn("Failed to write meta file.", e);
        }
    }
	
    //
     private static ByteBuffer borrowBuffer() {
        synchronized (POOL_BUFFER) {
            ByteBuffer pooled = POOL_BUFFER.pollFirst();
            if (pooled != null) return pooled;
        }
        // dropping
        if (POOL_FRAME.remainingCapacity() == 0) return null;
        return BufferUtils.createByteBuffer(BPF);
    }

    private static void recycleBuffer(ByteBuffer buffer) {
        if (buffer == null) return;
        buffer.clear();
        synchronized (POOL_BUFFER) {
            if (POOL_BUFFER.size() < POOL_MAX) POOL_BUFFER.addLast(buffer);
        }
    }

	// close file channel
    private static void closeChannel() {
    	if (ffmpegInput == null) return;
    	try {
            ffmpegInput.flush();
        } catch (IOException ignored) {
        }
        try {
            ffmpegInput.close();
        } catch (IOException ignored) {
        } finally {
            ffmpegInput = null;
        }
    }

    private static void removeConvertProcess() {
        if (ffmpegProcess == null) return;
        try {
            if (ffmpegProcess.isAlive()) {
                ffmpegProcess.destroy();
            }
        } catch (Exception ignored) {
        } finally {
            ffmpegProcess = null;
        }
    }

	// debug lag
    private static void printStatsIfNeeded() {
	    long now = System.nanoTime();
	    if (now - statsLastPrintNs < STATS_PRINT_INTERVAL_NS) return;
	    statsLastPrintNs = now;
	
	    double avgCaptureMs = (frameLoaded > 0)
	            ? (captureTimeNsTotal / 1_000_000.0) / frameLoaded
	            : 0.0;
	
	    double avgWriteMs = (frameWritten > 0)
	            ? (writeTimeNsTotal / 1_000_000.0) / frameWritten
	            : 0.0;
	
	    double maxCaptureMs = captureTimeNsMax / 1_000_000.0;
	    double maxWriteMs = writeTimeNsMax / 1_000_000.0;
	
	    double avgQueueSize = (queueSizeSamples > 0)
	            ? ((double) queueSizeSum / (double) queueSizeSamples)
	            : 0.0;
	
	    CacMod.LOGGER.info(
	            "[record-stats] loaded={}, written={}, dropped={}, enqueueFail={}, queue={}/{}, queueAvg={}," +
	            " captureAvgMs={}, captureMaxMs={}, writeAvgMs={}, writeMaxMs={}",
	            frameLoaded,
	            frameWritten,
	            frameDropped,
	            enqueueFailCount,
	            POOL_FRAME.size(),
	            POOL_MAX,
	            String.format("%.2f", avgQueueSize),
	            String.format("%.3f", avgCaptureMs),
	            String.format("%.3f", maxCaptureMs),
	            String.format("%.3f", avgWriteMs),
	            String.format("%.3f", maxWriteMs)
	    );
	}

    // record class already contains hashCode(), equals(), toString()
	private record FramePacket(ByteBuffer buffer, long index) {}
}