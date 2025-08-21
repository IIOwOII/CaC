package net.owo.cac;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;

import net.owo.cac.CstState;
import net.owo.cac.CstKeyMessage;
import net.owo.cac.CacMod;

import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CstKeybind {
    // Store the key mapping reference
    public static final KeyMapping CAC_CAMERA_KEY = new KeyMapping("key.cac.cac_key_camera", GLFW.GLFW_KEY_RIGHT_CONTROL, "key.categories.cac") {
    	int KI = 0;
		boolean isDownOld = false;
		
		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			CstState.key_pressed[KI] = isDown;
			/*
			if (isDownOld != isDown && isDown) {
				CacMod.PACKET_HANDLER.sendToServer(new CstKeyMessage(0, 0, KI));
				CstKeyMessage.pressAction(Minecraft.getInstance().player, 0, 0, KI);
				CAC_CAMERA_KEY_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - CAC_CAMERA_KEY_LASTPRESS);
				CacMod.PACKET_HANDLER.sendToServer(new CstKeyMessage(1, dt, KI));
				CstKeyMessage.pressAction(Minecraft.getInstance().player, 1, dt, KI);
			}
			*/
			isDownOld = isDown;
		}
    };
    public static final KeyMapping CAC_RIGHT_KEY = new KeyMapping("key.cac.cac_key_right", GLFW.GLFW_KEY_RIGHT, "key.categories.cac") {
    	int KI = 1;
		boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			CstState.key_pressed[KI] = isDown;
			/*
			if (isDownOld != isDown && isDown) {
				CacMod.PACKET_HANDLER.sendToServer(new CstKeyMessage(0, 0, KI));
				CstKeyMessage.pressAction(Minecraft.getInstance().player, 0, 0, KI);
				CAC_RIGHT_KEY_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - CAC_RIGHT_KEY_LASTPRESS);
				CacMod.PACKET_HANDLER.sendToServer(new CstKeyMessage(1, dt, KI));
				CstKeyMessage.pressAction(Minecraft.getInstance().player, 1, dt, KI);
			}
			*/
			isDownOld = isDown;
		}
	};
	public static final KeyMapping CAC_LEFT_KEY = new KeyMapping("key.cac.cac_key_left", GLFW.GLFW_KEY_LEFT, "key.categories.cac") {
		int KI = 2;
		boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			CstState.key_pressed[KI] = isDown;
			/*
			if (isDownOld != isDown && isDown) {
				CacMod.PACKET_HANDLER.sendToServer(new CstKeyMessage(0, 0, KI));
				CstKeyMessage.pressAction(Minecraft.getInstance().player, 0, 0, KI);
				CAC_LEFT_KEY_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - CAC_LEFT_KEY_LASTPRESS);
				CacMod.PACKET_HANDLER.sendToServer(new CstKeyMessage(1, dt, KI));
				CstKeyMessage.pressAction(Minecraft.getInstance().player, 1, dt, KI);
			}
			*/
			isDownOld = isDown;
		}
	};
    public static final KeyMapping CAC_UP_KEY = new KeyMapping("key.cac.cac_key_up", GLFW.GLFW_KEY_UP, "key.categories.cac") {
    	int KI = 3;
		boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			CstState.key_pressed[KI] = isDown;
			/*
			if (isDownOld != isDown && isDown) {
				CacMod.PACKET_HANDLER.sendToServer(new CstKeyMessage(0, 0, KI));
				CstKeyMessage.pressAction(Minecraft.getInstance().player, 0, 0, KI);
				CAC_UP_KEY_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - CAC_UP_KEY_LASTPRESS);
				CacMod.PACKET_HANDLER.sendToServer(new CstKeyMessage(1, dt, KI));
				CstKeyMessage.pressAction(Minecraft.getInstance().player, 1, dt, KI);
			}
			*/
			isDownOld = isDown;
		}
    };
    public static final KeyMapping CAC_DOWN_KEY = new KeyMapping("key.cac.cac_key_down", GLFW.GLFW_KEY_DOWN, "key.categories.cac") {
    	int KI = 4;
		boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			CstState.key_pressed[KI] = isDown;
			/*
			if (isDownOld != isDown && isDown) {
				CacMod.PACKET_HANDLER.sendToServer(new CstKeyMessage(0, 0, KI));
				CstKeyMessage.pressAction(Minecraft.getInstance().player, 0, 0, KI);
				CAC_DOWN_KEY_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - CAC_DOWN_KEY_LASTPRESS);
				CacMod.PACKET_HANDLER.sendToServer(new CstKeyMessage(1, dt, KI));
				CstKeyMessage.pressAction(Minecraft.getInstance().player, 1, dt, KI);
			}
			*/
			isDownOld = isDown;
		}
    };
    private static long CAC_CAMERA_KEY_LASTPRESS = 0;
    private static long CAC_RIGHT_KEY_LASTPRESS = 0;
    private static long CAC_LEFT_KEY_LASTPRESS = 0;
    private static long CAC_UP_KEY_LASTPRESS = 0;
    private static long CAC_DOWN_KEY_LASTPRESS = 0;
    
    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(CAC_CAMERA_KEY);
        event.register(CAC_RIGHT_KEY);
        event.register(CAC_LEFT_KEY);
        event.register(CAC_UP_KEY);
        event.register(CAC_DOWN_KEY);
    }
}