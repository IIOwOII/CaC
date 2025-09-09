package net.owo.cac;

import javax.annotation.Nullable;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

import net.owo.cac.CstState;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstTutorial {
	public static boolean is_tutorial = false;
	public static String content = "";
	public static String content_old = "";
	public static boolean content_changed = false;

	public static int tuto_idx = 0;
	public static int book_idx = 0;
	public static int moving_idx = 0;
	public static int[] moving_ord = {2,0,5,3,6,1,7,4};
	public static int[] meowmove_footprint = {0,0,0,0,0,0,0,0};

	public static void resetTutorial() {
		content = "";
		content_old = "";
		tuto_idx = 0;
		book_idx = 0;
		moving_idx = 0;
	}
	
	public static int getBookIndex() {
		return book_idx;
	}
	
	public static int getMovingOrder() {
		return moving_ord[moving_idx];
	}
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			@Nullable Entity _ent = event.player;
	    	if (_ent == null) return;
	    	LevelAccessor world = _ent.level();
	    	if (world == null) return;
	    	if (!is_tutorial) return;
	    	
			content_changed = (!content.equals(content_old));
			content_old = content;

			if (content.equals("book") && CstState.getKeyChanged(5) == 0) {
				book_idx += 1;
			} else if (content.equals("moving")) {
				if (content_changed) {
					meowmove_footprint = CstState.meowmove_tick.clone();
					moving_idx = 0;
				} else if ((moving_idx < 8) && (CstState.meowmove_tick[moving_ord[moving_idx]] > meowmove_footprint[moving_ord[moving_idx]] + 60)) {
					moving_idx += 1;
				} else if (moving_idx == 8) {
					content = "";
				}
			}
		}
	}
}
