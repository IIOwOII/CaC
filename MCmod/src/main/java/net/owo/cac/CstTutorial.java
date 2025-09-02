package net.owo.cac;

import javax.annotation.Nullable;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.Entity;

import net.owo.cac.CstState;
import net.owo.cac.network.CacModVariables;
import net.owo.cac.network.CacModVariables.MapVariables;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CstTutorial {
	public static String content = "";
	public static String content_old = "";
	public static boolean content_changed = false;
	public static int[] meowmove_footprint = {0,0,0,0,0,0,0,0};
	
	int moving_idx = 0;
	int[] moving_ord = {2,0,5,3,6,1,7,4};

	public static void resetMovingOrder() {
		moving_idx = 0;
	}
	
	public static int getMovingOrder() {
		return moving_ord[moving_idx];
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		@Nullable Entity _ent = null;
    	_ent = event.player;
    	if (_ent == null)
    		return;
    	LevelAccessor world = _ent.level();
    	if (world == null)
			return;
    	MapVariables cacvar = CacModVariables.MapVariables.get(world);

		content_changed = (content != content_old);
	}
}
