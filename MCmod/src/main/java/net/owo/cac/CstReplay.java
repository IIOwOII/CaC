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

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import net.minecraft.client.Minecraft;

import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.Event;

import net.owo.cac.CacMod;
import net.owo.cac.network.CacModVariables;
import java.nio.file.Files;
import java.io.BufferedWriter;



@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstReplay {
 // it can be closed (such like file io)
    // The status of recording
    private static final AtomicBoolean RECORDING = new AtomicBoolean(false); // If render system uses multithreads, we need to make the one group value from them.
    private static final AtomicBoolean STOP_REQUEST = new AtomicBoolean(false);

    // Queue
    private static Thread writerThread;
    private static final int MAX_QUEUE = 8;
    private static final BlockingQueue<FramePacket> FRAME_QUEUE = new ArrayBlockingQueue<>(MAX_QUEUE);
    private static final ArrayDeque<ByteBuffer> BUFFER_POOL = new ArrayDeque<>();
    private static final Object STATE_LOCK = new Object();
    

    // file
    private static FileChannel fileChannel;
    private static Path rawFile;

    // Parameter
    private static int WIDTH;
    private static int HEIGHT;
    private static int FPS; // frame per second
    private static long NPF; // nanosecond per frame
    private static long BPF; // byte per frame
    private static long T_START; // recording start time
    private static long T_END; // recording end time
    private static long T_QUE; // Ideal time of capture
    private static long T_RECORD; // Actual time of capture

    private static int writtenFrames = 0;

    // on rendering
    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        if (isRecording) {
        	readFrame();
        }
    }

	

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
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return; // ex. Title
        try {
        	RenderTarget frame = mc.getMainRenderTarget();
	        
	        T_START = System.nanoTime(); // recording start time (absolute)
	        
	        WIDTH = frame.width;
	        HEIGHT = frame.height;
	        FRAME = 0L;
	        
	        FPS = Math.max(1, fps);
	        NPF = 1_000_000_000L/FPS;
	        T_QUE = T_START;
	        BPF = frame.width * frame.height * 3; // RGB24 = 3 bytes/pixel
			
	        // file setting
	        Path dir_main = Paths.get(FMLPaths.GAMEDIR.get().toString() + "/cacutil/replays");
	        F_RAW = dir_main.resolve("frames.rgb");
	        file_channel = FileChannel.open(
	        	F_RAW, 
	        	StandardOpenOption.CREATE,
	        	StandardOpenOption.TRUNCATE_EXISTING,
	        	StandardOpenOption.WRITE);
	
	        // writer setting
	        frame_que.clear();
	        writer_thread = new Thread(new writeFrame()); // thread allocate
	        writer_thread.setDaemon(true);
	        writer_thread.start();
	
	        IsRecording.set(true);
        } catch (IOException e) {
        	resetRecording();
        	e.printStackTrace();
        }
    }

    // end
    public static void stopRecording() {
        T_END = System.nanoTime(); // recording end time (absolute)
        IsRecording.set(false);
        
    }

    // Reset and clean up
    public static void resetRecording() {
    	closeChannel();
    	file_channel = null;
    	writer_thread = null;
    	frame_que.clear();
    	IsRecording.set(false);
    }

    // frame by frame
    public static void readFrame() {
    	long t_curr = System.nanoTime();
    	if (!RECORDING.get()) return;
        if (t_curr < T_QUE) return; // only capture the frame by FPS
        T_RECORD = t_curr;
        T_QUE += NPF;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
        	stopRecording();
        	return;
        }
        RenderTarget frame = mc.getMainRenderTarget();
        if (frame == null) {
        	stopRecording();
        	return;
        }
        if (frame.width != WIDTH || frame.height != HEIGHT) { // If resolution is changed
            stopRecording();
            CacMod.LOGGER.info("Do not change the resolution during recording.");
            return;
        }

        // GPU to CPU(ByteBuffer)
        ByteBuffer buffer = BufferUtils.createByteBuffer(BPF); // create the empty buffer to contain RGB24
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
        buffer.rewind(); // return to first place. (to prepare the next frame)
		
        // the worker seperate -> reader, writer
        stackFrame(new FramePacket(buffer));
    }

    //
    private static void stackFrame(FramePacket packet) {
    	frame_que.offer(packet);
    }
	
    private static class writeFrame implements Runnable {
    	@Override
    	public void run() {
    		try {
	    		while (true) {
	    			if (STOP_REQUEST.get() && FRAME_QUEUE.isEmpty()) {
	    				break;
	    			}
	    			FramePacket packet = FRAME_QUEUE.poll(100, TimeUnit.MILLISECONDS); // waiting the frame until 100ms
	    			if (packet == null) {
	    				continue;
	    			}
	    			writePacket(packet);
	    			writtenFrames++;
	    			recycleBuffer(packet.buffer());
	    		}
	    		writeMetadata();
	    		CacMod.LOGGER.info("end recording");
	    	} catch (InterruptedException e) {
	    		Thread.currentThread().interrupt();
	    	} catch (Exception e) {
	    		e.printStackTrace();
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
        long durationNano = (endNano > startNano) ? (endNano - startNano) : 0L;
        try (BufferedWriter bw = Files.newBufferedWriter(
                metaFile,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        )) {
            bw.write("width=" + width);
            bw.newLine();
            bw.write("height=" + height);
            bw.newLine();
            bw.write("fps=" + fps);
            bw.newLine();
            bw.write("pixel_format=rgb24");
            bw.newLine();
            bw.write("captured_frames=" + capturedFrames);
            bw.newLine();
            bw.write("written_frames=" + writtenFrames);
            bw.newLine();
            bw.write("dropped_frames=" + droppedFrames);
            bw.newLine();
            bw.write("duration_nano=" + durationNano);
            bw.newLine();
        } catch (IOException e) {
            CacMod.LOGGER.warn("Failed to write meta file.", e);
        }
    }
	
    //
     private static ByteBuffer borrowBuffer() {
        synchronized (BUFFER_POOL) {
            ByteBuffer pooled = BUFFER_POOL.pollFirst();
            if (pooled != null) {
                return pooled;
            }
        }
        if (FRAME_QUEUE.remainingCapacity() == 0) {
            return null;
        }
        return BufferUtils.createByteBuffer(bytesPerFrame);
    }

    private static void recycleBuffer(ByteBuffer buffer) {
        if (buffer == null) return;
        buffer.clear();
        synchronized (BUFFER_POOL) {
            if (BUFFER_POOL.size() < MAX_QUEUE) {
                BUFFER_POOL.addLast(buffer);
            }
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