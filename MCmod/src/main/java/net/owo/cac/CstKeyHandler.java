package net.owo.cac;

import javax.annotation.Nullable;
import java.util.Arrays;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import net.owo.cac.CstState;
import net.owo.cac.CstKeybind;
import net.owo.cac.CacMod;
import net.owo.cac.init.CacModItems;
import net.owo.cac.network.CacModVariables;
import net.owo.cac.network.CacModVariables.MapVariables;
import net.owo.cac.procedures.PrdItemOptionPlusProcedure;
import net.owo.cac.procedures.PrdItemOptionMinusProcedure;
import net.owo.cac.procedures.PrdItemOptionPrintProcedure;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstKeyHandler {
	
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
		
        if (event.phase == TickEvent.Phase.END) {
        	
			if (CstState.getKeyChanged(0) == 0) {
				CstState.switchMeowView();
			}
        	
			if (CstState.CanMeowMove) {
	            int rot_re = (CstState.key_pressed[1] ? 1 : 0) - (CstState.key_pressed[2] ? 1 : 0);
	            int rot_im = (CstState.key_pressed[3] ? 1 : 0) - (CstState.key_pressed[4] ? 1 : 0);
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
    	if (_ent == null)
    		return;
    	@Nullable LivingEntity _livent = (_ent instanceof LivingEntity) ? (LivingEntity) _ent : null;
    	LevelAccessor world = _ent.level();
    	MapVariables cacvar = CacModVariables.MapVariables.get(world);
    	
    	if (CstState.CanMeowMove && CstState.IsMeowMove_old) {
    		_ent.setYRot(CstState.rot_angle);
    	}
    	
    	if (event.phase == TickEvent.Phase.END) {
    		// always
    		if (!world.isClientSide() && world.getServer() != null) {
    			CstState.KeyTickUpdate();
    		}
    		
    		// Survey Value
    		if (cacvar.Switch_survey) {
				if (CstState.key_pressed[1]) {
					cacvar.SuvT_value = (cacvar.SuvT_range_upper > cacvar.SuvT_value) ? (cacvar.SuvT_value + 1):(cacvar.SuvT_value);
    				cacvar.syncData(world);
				} else if (CstState.key_pressed[2]) {
					cacvar.SuvT_value = (cacvar.SuvT_range_lower < cacvar.SuvT_value) ? (cacvar.SuvT_value - 1):(cacvar.SuvT_value);
    				cacvar.syncData(world);
				}
    		} 
    		if (cacvar.Switch_surrender) {
    			if ((CstState.key_pressed[1]) && (cacvar.Dat_survey_surrender != 1)) {
    				cacvar.Dat_survey_surrender = 1;
    				cacvar.syncData(world);
    			} else if ((CstState.key_pressed[2]) && (cacvar.Dat_survey_surrender != 0)) {
    				cacvar.Dat_survey_surrender = 0;
    				cacvar.syncData(world);
    			}
    		} 
    		if ((_livent != null) && ((_livent.getMainHandItem().getItem() == CacModItems.CAC_TEST_ITEM.get()) || (_livent.getMainHandItem().getItem() == CacModItems.CAC_BUILDER_TOOL.get()))) {
    			if (CstState.getKeyChanged(1) == 0) {
    				PrdItemOptionPlusProcedure.execute(world, _ent);
    				PrdItemOptionPrintProcedure.execute(world, _ent);
    			}
    			if (CstState.getKeyChanged(2) == 0) {
    				PrdItemOptionMinusProcedure.execute(world, _ent);
    				PrdItemOptionPrintProcedure.execute(world, _ent);
    			}
    		}
    	}
    	
    }
}