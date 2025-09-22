package net.owo.cac;

import javax.annotation.Nullable;
import java.util.Arrays;

import com.google.gson.JsonArray;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.owo.cac.CstState;
import net.owo.cac.network.CacModVariables;
import net.owo.cac.network.CacModVariables.MapVariables;

import net.owo.cac.procedures.TimActionbarClearProcedure;
import net.owo.cac.procedures.EvQueCallProcedure;
import net.owo.cac.procedures.TutoCheckpointReadyProcedure;
import net.owo.cac.procedures.TutoCheckpointStartProcedure;
import net.owo.cac.procedures.TutoCheckpointEndProcedure;
import net.owo.cac.procedures.TutoRacingReadyProcedure;
import net.owo.cac.procedures.TutoRacingStartProcedure;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstTutorial {
	public static JsonArray tuto_que = new JsonArray();
	public static int tuto_idx = 0;

	public static boolean tuto_timer_switch = false;
	public static int tuto_timer = 0;
	
	public static boolean is_tutorial = false;
	public static String content = "";
	public static String content_old = "";
	public static boolean content_changed = false;
	
	public static int moving_idx = 0;
	public static int[] moving_ord = {2,0,5,3,6,1,7,4};
	public static int[] meowmove_footprint = {0,0,0,0,0,0,0,0};

	public static void resetTutorial() {
		content = "";
		content_old = "";
		tuto_idx = 0;
		stopTimer();
		moving_idx = 0;
		Arrays.fill(meowmove_footprint, 0);
	}

	public static void startTimer() {
		tuto_timer_switch = true;
		tuto_timer = 0;
	}

	public static void stopTimer() {
		tuto_timer_switch = false;
		tuto_timer = 0;
	}

	public static void initTutoQue() {
		content = tuto_que.get(0).getAsString();
	}
	
	public static void updateTutoQue() {
		tuto_idx += 1;
		if (tuto_idx == tuto_que.size()) {
			content = "finish";
			return;
		}
		content = tuto_que.get(tuto_idx).getAsString();
	}

	public static void undoBookPage() {
		tuto_que.get(tuto_idx-2).getAsString();
	}
	
	public static String getBookName() {
		return content.substring(5);
	}
	
	public static int getMovingOrder() {
		int ord = 0;
		if (moving_idx == moving_ord.length) {
			ord = moving_ord[moving_ord.length-1];
		} else {
			ord = moving_ord[moving_idx];
		}
		return ord;
	}
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (!is_tutorial) return;
		if (event.phase == TickEvent.Phase.END) {
			@Nullable Entity _ent = event.player;
	    	if (_ent == null) return;
	    	LevelAccessor world = _ent.level();
	    	if (world == null) return;

			MapVariables cacvar = CacModVariables.MapVariables.get(world);
	    	
			content_changed = (!content.equals(content_old));
			content_old = content;

			if (tuto_timer_switch) {
				tuto_timer += 1;
			}
			
			if (content.equals("finish")) {
				cacvar.Ev_content = "tutorial_off";
				cacvar.syncData(world);
				EvQueCallProcedure.execute(world, _ent.getX(), _ent.getY(), _ent.getZ(), _ent);
			} else if (content.startsWith("book")) {
				if (content_changed) {
					CstState.CanMeowMove = false;
				} else if (CstState.getKeyChanged(5) == 0) {
					updateTutoQue();
				}
			} else if (content.equals("moving")) {
				if (content_changed) {
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "cac_tp tutorial_moving");
					}
					meowmove_footprint = CstState.meowmove_tick.clone();
					cacvar.Msg_actionbar_text = "\uD654\uBA74\uC5D0 \uC9C0\uC2DC\uB41C \uBC29\uD5A5\uB300\uB85C \uACC4\uC18D \uC6C0\uC9C1\uC5EC\uC8FC\uC138\uC694.";
					cacvar.syncData(world);
					cacvar.Msg_actionbar_switch = true;
					cacvar.syncData(world);
					moving_idx = 0;
				} else if ((moving_idx < moving_ord.length) && (CstState.meowmove_tick[moving_ord[moving_idx]] > meowmove_footprint[moving_ord[moving_idx]] + 60)) {
					moving_idx += 1;
				} else if (moving_idx == moving_ord.length) {
					cacvar.Msg_actionbar_text = "\uC798\uD588\uC2B5\uB2C8\uB2E4!";
					cacvar.syncData(world);
					CstState.CanMeowMove = false;
					content = "moving_end";
					startTimer();
				}
			} else if (content.equals("moving_end") && (tuto_timer == 100)) {
				cacvar.Msg_actionbar_switch = false;
				cacvar.syncData(world);
				stopTimer();
				updateTutoQue();
			} else if (content.equals("checkpoint")) {
				if (content_changed) {
					TutoCheckpointReadyProcedure.execute(world, _ent);
					startTimer();
				} else if (tuto_timer == 100) {
					TutoCheckpointStartProcedure.execute(world, _ent.getX(), _ent.getY(), _ent.getZ(), _ent);
					stopTimer();
				} else if (cacvar.Tuto_checkpoint_index == cacvar.Tuto_checkpoint_route.size()) {
					content = "checkpoint_end";
					TutoCheckpointEndProcedure.execute(world, _ent.getX(), _ent.getY(), _ent.getZ(), _ent);
				}
			} else if (content.equals("checkpoint_end") && CstState.getKeyChanged(5) == 0) {
				TimActionbarClearProcedure.execute(world);
				updateTutoQue();
			} else if (content.equals("racing")) {
				if (content_changed) {
					TutoRacingReadyProcedure.execute(world, _ent);
					startTimer();
				} else if (tuto_timer == 100) {
					TutoRacingStartProcedure.execute(world, _ent.getX(), _ent.getY(), _ent.getZ(), _ent);
					stopTimer();
				}
			}
			
		}
	}
}
