
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.owo.cac.init;

import org.lwjgl.glfw.GLFW;

import net.owo.cac.network.CacKeySignalMessage;
import net.owo.cac.network.CacKeyRightMessage;
import net.owo.cac.network.CacKeyLeftMessage;
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
	public static final KeyMapping CAC_KEY_LEFT = new KeyMapping("key.cac.cac_key_left", GLFW.GLFW_KEY_LEFT, "key.categories.cac") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				CacMod.PACKET_HANDLER.sendToServer(new CacKeyLeftMessage(0, 0));
				CacKeyLeftMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping CAC_KEY_RIGHT = new KeyMapping("key.cac.cac_key_right", GLFW.GLFW_KEY_RIGHT, "key.categories.cac") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				CacMod.PACKET_HANDLER.sendToServer(new CacKeyRightMessage(0, 0));
				CacKeyRightMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
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
	private static long CAC_KEY_SIGNAL_LASTPRESS = 0;

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(CAC_KEY_LEFT);
		event.register(CAC_KEY_RIGHT);
		event.register(CAC_KEY_SIGNAL);
	}

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				CAC_KEY_LEFT.consumeClick();
				CAC_KEY_RIGHT.consumeClick();
				CAC_KEY_SIGNAL.consumeClick();
			}
		}
	}
}
