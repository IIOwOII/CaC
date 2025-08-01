package net.owo.cac;

import javax.annotation.Nullable;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.Mth;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import net.owo.cac.CstState;
import net.owo.cac.network.CacModVariables;
import net.owo.cac.network.CacModVariables.MapVariables;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstKeyHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        // Only run on the "end" phase to avoid doubling
        if (event.phase == TickEvent.Phase.END) {
        	// Arrow Control
            CstState.arrow_right = CstKeybind.CAC_RIGHT_KEY.isDown() ? 1 : 0;
            CstState.arrow_left = CstKeybind.CAC_LEFT_KEY.isDown() ? 1 : 0;
            CstState.arrow_up = CstKeybind.CAC_UP_KEY.isDown() ? 1 : 0;
            CstState.arrow_down = CstKeybind.CAC_DOWN_KEY.isDown() ? 1 : 0;
            
            if (CstKeybind.CAC_CAMERA_KEY.consumeClick()) {
                // Toggle the camera state
                CstState.switchMeowView();
            }

			if (CstState.CanMeowMove) {
	            int rot_re = CstState.arrow_right - CstState.arrow_left;
	            int rot_im = CstState.arrow_up - CstState.arrow_down;

	            if (rot_re != 0) {
	            	CstState.arrow_pressed[0] += rot_re;
	            } else {
	            	CstState.arrow_pressed[0] = 0;
	            }
	            if (rot_im != 0) {
	            	CstState.arrow_pressed[1] += rot_im;
	            } else {
	            	CstState.arrow_pressed[1] = 0;
	            }
	
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
    	} 
    	if (CstState.CanMeowMove && CstState.IsMeowMove_old) {
    		_ent.setYRot(CstState.rot_angle);
    	}
    	
    	if (event.phase == TickEvent.Phase.END) {
    		LevelAccessor world = _ent.level();
    		MapVariables cacvar = CacModVariables.MapVariables.get(world);
    		
    		// Survey Value
    		if (cacvar.Switch_survey) {
				if (CstState.arrow_right == 1) {
					cacvar.SuvT_value = (cacvar.SuvT_range_upper > cacvar.SuvT_value) ? (cacvar.SuvT_value + 1):(cacvar.SuvT_value);
    				cacvar.syncData(world);
				} else if (CstState.arrow_left == 1) {
					cacvar.SuvT_value = (cacvar.SuvT_range_lower < cacvar.SuvT_value) ? (cacvar.SuvT_value - 1):(cacvar.SuvT_value);
    				cacvar.syncData(world);
				}
    		} else if (cacvar.Switch_surrender) {
    			if ((CstState.arrow_right == 1) && (cacvar.Dat_survey_surrender != 1)) {
    				cacvar.Dat_survey_surrender = 1;
    				cacvar.syncData(world);
    			} else if ((CstState.arrow_left == 1) && (cacvar.Dat_survey_surrender != 0)) {
    				cacvar.Dat_survey_surrender = 0;
    				cacvar.syncData(world);
    			}
    		}
    	}
    }
    
}