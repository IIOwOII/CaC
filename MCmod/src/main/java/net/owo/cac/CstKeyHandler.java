package net.owo.cac;

import javax.annotation.Nullable;
import java.util.Arrays;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import net.owo.cac.CstState;
import net.owo.cac.CstKeybind;
import net.owo.cac.CstItem;

import net.owo.cac.CacMod;
import net.owo.cac.network.CacModVariables;
import net.owo.cac.network.CacModVariables.MapVariables;


@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstKeyHandler {
	static final int KEY_RIGHT = InputConstants.KEY_RIGHT;
	static final int KEY_LEFT = InputConstants.KEY_LEFT;
	
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
    	InputConstants.Key key_forward = InputConstants.getKey("key.keyboard.w");
    	
        if (event.phase == TickEvent.Phase.END) {
			if (CstState.getKeyChanged(4) == 0) {
				CstState.switchMeowView();
			}
			CstState.KeyTickUpdate();
			if (CstState.CanMeowMove) {
				CstState.AngleUpdate();
	            if (CstState.getKeyCase() != -1) {
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
    	if (_ent == null)
    		return;
    	LevelAccessor world = _ent.level();
    	MapVariables cacvar = CacModVariables.MapVariables.get(world);
    	
    	if (CstState.CanMeowMove && CstState.IsMeowMove_old) {
    		_ent.setYRot(CstState.rot_angle);
    	}
    	
    	if (event.phase == TickEvent.Phase.END) {
    		// Survey Value
    		if (cacvar.Switch_surrender) {
    			if ((CstState.key_pressed[0]) && (cacvar.Dat_survey_surrender != 1)) {
    				cacvar.Dat_survey_surrender = 1;
    				cacvar.syncData(world);
    			} else if ((CstState.key_pressed[1]) && (cacvar.Dat_survey_surrender != 0)) {
    				cacvar.Dat_survey_surrender = 0;
    				cacvar.syncData(world);
    			}
    		}
    	}
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
		int condition = CstState.getCondition();
		if (condition == -1) return;
		int key_value = event.getKey();
		int key_action = event.getAction(); // 0: released, 1: pressed, 2: repeated

		if ((key_action == 1) && (Math.floorDiv(condition, 10) == 1)) {
			int item_id = Math.floorMod(condition, 10);
			if (key_value == KEY_RIGHT) {
				CstItem.modifyItemOption(item_id, 1);
			} else if (key_value == KEY_LEFT) {
				CstItem.modifyItemOption(item_id, -1);
			}
		}
    }
    
}