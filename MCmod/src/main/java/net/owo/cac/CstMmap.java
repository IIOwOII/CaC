package net.owo.cac;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstMmap {
	public static void go() {
		test();
	}
	public static void test() throws ClassNotFoundException {
        String filename = "/home/owo/CaC/Analysis/shared.dat";
        int size = 1024; // 1KB

        try (RandomAccessFile file = new RandomAccessFile(filename, "rw");
             FileChannel channel = file.getChannel()) {

            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, size);

            String message = "Hello from Java";
            buffer.put(message.getBytes());
            System.out.println("Java: Data written to " + filename);

            buffer.force();
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
        }
    }
}
