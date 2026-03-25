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

import java.io.BufferedWriter;
import java.io.IOException;
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

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstReplay {
    // The status of recording
    private static final AtomicBoolean RECORDING = new AtomicBoolean(false); // If render system uses multithreads, we need to make the one group value from them.
    private static final AtomicBoolean STOP_REQUEST = new AtomicBoolean(false);

    // Queue
    private static Thread writerThread;
    private static final int POOL_MAX = 16;
    private static final BlockingQueue<FramePacket> POOL_FRAME = new ArrayBlockingQueue<>(POOL_MAX);
    private static final ArrayDeque<ByteBuffer> POOL_BUFFER = new ArrayDeque<>();
    private static final Object STATE_LOCK = new Object();

    // file
    private static FileChannel fileChannel;
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
	
    // start
    public static void startRecording(int fps) {
    	synchronized (STATE_LOCK) {
    		if (RECORDING.get()) return;
    		Minecraft mc = Minecraft.getInstance();
    		RenderTarget frame = mc.getMainRenderTarget();
	        if (mc.player == null || mc.level == null) return; // ex. Title
	        if (frame == null) return;
	        try {
	        	// init metadata
	        	frameLoaded = 0L;
	        	frameWritten = 0L;
				frameDropped = 0L;
	        	
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
		        fileRaw = dir_file.resolve("replay.meow"); // raw file
		        fileMeta = dir_file.resolve("replay.txt"); // meta file
		        fileChannel = FileChannel.open(
		        	fileRaw,
		        	StandardOpenOption.CREATE,
		        	StandardOpenOption.TRUNCATE_EXISTING,
		        	StandardOpenOption.WRITE);

				// pool reset
				for (int i=0; i<POOL_MAX; i++) {
                    POOL_BUFFER.add(BufferUtils.createByteBuffer(BPF));
                }
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
            return;
		}

        try {
        	buffer.clear();
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
	        buffer.position(0); // return to first place. (to prepare the next frame)
	        buffer.limit(BPF);

	        FramePacket packet = new FramePacket(buffer, frameLoaded);
	        boolean offered = POOL_FRAME.offer(packet);
	        if (!offered) {
	        	recycleBuffer(buffer); // if frame pool is full, drop frame
	        	frameDropped++;
	        } else {
	        	frameLoaded++;
	        }
        } catch (Exception e) {
        	recycleBuffer(buffer);
        	CacMod.LOGGER.error("Failed to capture frame.", e);
            stopRecording();
        }
    }

	//
    private static void writeFrame() {
		try {
    		while (true) {
    			if (STOP_REQUEST.get() && POOL_FRAME.isEmpty()) break;
    			FramePacket packet = POOL_FRAME.poll(100, TimeUnit.MILLISECONDS); // waiting the frame until 100ms
    			if (packet == null) continue;
    			writePacket(packet);
    			frameWritten++;
    			recycleBuffer(packet.buffer());
    		}
    		writeMetadata();
    		CacMod.LOGGER.info(
                    "Recording finished. loaded={}, written={}, dropped={}",
                    frameLoaded, frameWritten, frameDropped);
    	} catch (InterruptedException e) {
    		Thread.currentThread().interrupt();
    		CacMod.LOGGER.warn("Writer thread interrupted.");
    	} catch (Exception e) {
    		CacMod.LOGGER.error("Writer loop failed", e);
    	} finally {
    		synchronized (STATE_LOCK) {
    			closeChannel();
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
    	ByteBuffer buf = packet.buffer();
    	while (buf.hasRemaining()) {
    		fileChannel.write(buf);
    	}
    }

	//
	private static void writeMetadata() {
        double duration = (T_END > T_START) ? (T_END-T_START)/1_000_000_000.0 : 0.0;
        try (BufferedWriter bw = Files.newBufferedWriter(
                fileMeta,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE))
        {
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
    	if (fileChannel == null) return;
    	try {fileChannel.close();} 
    	catch (IOException e) {}
    	finally {fileChannel = null;}
    }

    // record class already contains hashCode(), equals(), toString()
	private record FramePacket(ByteBuffer buffer, long index) {}
}