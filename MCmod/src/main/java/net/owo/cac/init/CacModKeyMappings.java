
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.owo.cac.init;

import org.lwjgl.glfw.GLFW;

import net.owo.cac.network.CacKeyStopwatchMessage;
import net.owo.cac.network.CacKeySignalMessage;
import net.owo.cac.network.CacKeyDecideMessage;
import net.owo.cac.CacMod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class CacModKeyMappings {
	public static final KeyMapping CAC_KEY_SIGNAL = new KeyMapping("key.cac.cac_key_signal", GLFW.GLFW_KEY_S, "key.categories.cac") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				CacMod.PACKET_HANDLER.sendToServer(new CacKeySignalMessage(0, 0));
				CacKeySignalMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				CAC_KEY_SIGNAL_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - CAC_KEY_SIGNAL_LASTPRESS);
				CacMod.PACKET_HANDLER.sendToServer(new CacKeySignalMessage(1, dt));
				CacKeySignalMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping CAC_KEY_STOPWATCH = new KeyMapping("key.cac.cac_key_stopwatch", GLFW.GLFW_KEY_RIGHT_SHIFT, "key.categories.cac") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				CacMod.PACKET_HANDLER.sendToServer(new CacKeyStopwatchMessage(0, 0));
				CacKeyStopwatchMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping CAC_KEY_DECIDE = new KeyMapping("key.cac.cac_key_decide", GLFW.GLFW_KEY_LEFT_CONTROL, "key.categories.cac") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				CacMod.PACKET_HANDLER.sendToServer(new CacKeyDecideMessage(0, 0));
				CacKeyDecideMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	private static long CAC_KEY_SIGNAL_LASTPRESS = 0;

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(CAC_KEY_SIGNAL);
		event.register(CAC_KEY_STOPWATCH);
		event.register(CAC_KEY_DECIDE);
	}

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				CAC_KEY_SIGNAL.consumeClick();
				CAC_KEY_STOPWATCH.consumeClick();
				CAC_KEY_DECIDE.consumeClick();
			}
		}
	}
}
