package net.owo.cac;

import javax.annotation.Nullable;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;

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
                CstState.switchMeowView();
            }

			if (CstState.CanMeowMove) {
				// Arrow Control
	            int weight_right = CstKeybind.CAC_RIGHT_KEY.isDown() ? 1 : 0;
	            int weight_left = CstKeybind.CAC_LEFT_KEY.isDown() ? 1 : 0;
	            int weight_up = CstKeybind.CAC_UP_KEY.isDown() ? 1 : 0;
	            int weight_down = CstKeybind.CAC_DOWN_KEY.isDown() ? 1 : 0;
	            
	            int rot_re = weight_right - weight_left;
	            int rot_im = weight_up - weight_down;
	
	            double rot_norm = Math.sqrt(rot_re*rot_re + rot_im*rot_im);
	
	            if (rot_norm != 0) {
	            	float rot_angle_unsign = (float) (Math.acos(-rot_im / rot_norm) * Mth.RAD_TO_DEG);
	            	CstState.rot_angle = (rot_re > 0) ? -rot_angle_unsign : rot_angle_unsign;
	            }
	            
	            InputConstants.Key key_forward = InputConstants.getKey("key.keyboard.w");
	            if ((rot_re != 0) || (rot_im != 0)) {
	            	KeyMapping.set(key_forward, true);
	            	CstState.IsMeowMove_old = true;
	            } else if (CstState.IsMeowMove_old) {
	            	KeyMapping.set(key_forward, false);
	            	CstState.IsMeowMove_old = false;
	            }
			}
			
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    	@Nullable Entity _ent = null;
    	_ent = event.player;

    	if (_ent == null) {
    		return;
    	} else if (CstState.CanMeowMove && CstState.IsMeowMove_old) {
    		_ent.setYRot(CstState.rot_angle);
    	}
    }
    
}