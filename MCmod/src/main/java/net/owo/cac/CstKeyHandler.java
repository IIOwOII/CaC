package net.owo.cac;

import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import net.owo.cac.CstState;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstKeyHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        // Only run on the "end" phase to avoid doubling
        if (event.phase == TickEvent.Phase.END) {
        
            if (CstKeybind.CAC_CAMERA_KEY.consumeClick()) {
                // Toggle the camera state
                CstState.switchMeowview();
            }

			// Arrow Control
            int weight_right = CstKeybind.CAC_RIGHT_KEY.isDown() ? 1 : 0;
            int weight_left = CstKeybind.CAC_LEFT_KEY.isDown() ? 1 : 0;
            int weight_up = CstKeybind.CAC_UP_KEY.isDown() ? 1 : 0;
            int weight_down = CstKeybind.CAC_DOWN_KEY.isDown() ? 1 : 0;
            
            int rot_re = weight_right - weight_left;
            int rot_im = weight_up - weight_down;

            CstState.rot_angle = (float) Math.asin(-rot_re / Math.sqrt(rot_re*rot_re + rot_im*rot_im));
            
            InputConstants.Key key_forward = InputConstants.getKey("key.keyboard.w");
            if ((rot_re != 0) || (rot_im != 0)) {
            	KeyMapping.set(key_forward, true);
            	CstState.rot_old = true;
            } else if (CstState.rot_old) {
            	KeyMapping.set(key_forward, false);
            	CstState.rot_old = false;
            }
            
        }
    }
}