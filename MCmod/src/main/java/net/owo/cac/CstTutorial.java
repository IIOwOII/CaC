package net.owo.cac;

import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;

import net.owo.cac.CstState;
import net.owo.cac.network.CacModVariables;


@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstTutorial {
	/*
	1: moving
	2: checkpoint
	3: racing
	100: book_0
	101: book_1 ...
	*/
	static final int[] TUTO_QUE = {100,101,102,1,103};
	
	static ResourceLocation[] tutorial_book = {
		//new ResourceLocation("cac:textures/screens/texture_book_0.png"),
		//new ResourceLocation("cac:textures/screens/texture_book_1.png"),
		//new ResourceLocation("cac:textures/screens/texture_book_2.png"),
		//new ResourceLocation("cac:textures/screens/texture_book_3.png")
	};
	static ResourceLocation[] tutorial_arrow = {
		new ResourceLocation("cac:textures/screens/texture_cac_direction_0.png"),
		new ResourceLocation("cac:textures/screens/texture_cac_direction_1.png"),
		new ResourceLocation("cac:textures/screens/texture_cac_direction_2.png"),
		new ResourceLocation("cac:textures/screens/texture_cac_direction_3.png"),
		new ResourceLocation("cac:textures/screens/texture_cac_direction_4.png"),
		new ResourceLocation("cac:textures/screens/texture_cac_direction_5.png"),
		new ResourceLocation("cac:textures/screens/texture_cac_direction_6.png"),
		new ResourceLocation("cac:textures/screens/texture_cac_direction_7.png")
	};
	
	public static boolean IsTutorial = false;
	public static int tuto_idx = 0;
	public static int tuto_id = 0;
	public static int tuto_id_old = 0;
	public static boolean tuto_changed = false;

	@SubscribeEvent
	public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
		if (!IsTutorial) return;
		
		GuiGraphics gg = event.getGuiGraphics();
		if (tuto_id >= 100) { // book
			renderBook(gg, tuto_id-100);
		}
	}
	
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (!IsTutorial) return;
		tuto_id = TUTO_QUE[tuto_idx];
		if (tuto_id != tuto_id_old) {
			tuto_changed = true;
		} else {
			tuto_changed = false;
		}
		tuto_id_old = tuto_id;
		
		if (event.phase == TickEvent.Phase.END) {
			if (tuto_id >= 100 && CstState.getKeyChanged(5) == 0) { // book
				tuto_idx = tuto_idx + 1;
			} else if (tuto_id == 1) { // moving
				return;
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (!IsTutorial) return;
		
		@Nullable Entity player = event.player;
		LevelAccessor world = player.level();
		MinecraftServer server = player.getServer();
		if ((player == null) || (world == null)) return;

		if (event.phase == TickEvent.Phase.END) {
			if (tuto_id == 1 && tuto_changed) {
				if ((!world.isClientSide()) && (server != null)) {
					server.getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), world instanceof ServerLevel ? (ServerLevel) world : null, 4, player.getName().getString(), player.getDisplayName(), server, player), "cac_tp tutorial_moving");
				}
			}
		}
	}
	
	public static void renderBook(GuiGraphics gg, int ID) {
		gg.blit(tutorial_book[ID], 0, 0, 0, 0, 427, 240, 427, 240);
	}

	public static void renderArrow(GuiGraphics gg, int ID) {
		gg.blit(tutorial_arrow[ID], 153, 60, 0, 0, 120, 120, 120, 120);
	}

	public static void startTutorial() {
		tuto_idx = 0;
		tuto_id = TUTO_QUE[tuto_idx];
		tuto_id_old = tuto_id;
		tuto_changed = false;
		IsTutorial = true;
	}
	public static void endTutorial() {
		IsTutorial = false;
		tuto_idx = 0;
		tuto_id = 0;
	}
	

	/*
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (!is_tutorial) return;
		if (event.phase == TickEvent.Phase.END) {
			@Nullable Entity _ent = event.player;
	    	if (_ent == null) return;
	    	LevelAccessor world = _ent.level();
	    	if (world == null) return;
	    	
			content_changed = (!content.equals(content_old));
			content_old = content;

			if (tuto_timer_switch) {
				tuto_timer += 1;
			}

			
			if (content.equals("finish")) {
				CacModVariables.Ev_content = "tutorial_off";
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
					CacModVariables.Msg_actionbar_text = "\uD654\uBA74\uC5D0 \uC9C0\uC2DC\uB41C \uBC29\uD5A5\uB300\uB85C \uACC4\uC18D \uC6C0\uC9C1\uC5EC\uC8FC\uC138\uC694.";
					CacModVariables.Msg_actionbar_switch = true;
					moving_idx = 0;
				} else if ((moving_idx < moving_ord.length) && (CstState.meowmove_tick[moving_ord[moving_idx]] > meowmove_footprint[moving_ord[moving_idx]] + 60)) {
					moving_idx += 1;
				} else if (moving_idx == moving_ord.length) {
					CacModVariables.Msg_actionbar_text = "\uC798\uD588\uC2B5\uB2C8\uB2E4!";
					CstState.CanMeowMove = false;
					content = "moving_end";
					startTimer();
				}
			} else if (content.equals("moving_end") && (tuto_timer == 100)) {
				CacModVariables.Msg_actionbar_switch = false;
				stopTimer();
				updateTutoQue();
			} else if (content.equals("checkpoint")) {
				if (content_changed) {
					TutoCheckpointReadyProcedure.execute(_ent);
					startTimer();
				} else if (tuto_timer == 100) {
					TutoCheckpointStartProcedure.execute(world, _ent.getX(), _ent.getY(), _ent.getZ(), _ent);
					stopTimer();
				} else if (CacModVariables.Tuto_checkpoint_index == CacModVariables.Tuto_checkpoint_route.size()) {
					content = "checkpoint_end";
					TutoCheckpointEndProcedure.execute(world, _ent.getX(), _ent.getY(), _ent.getZ(), _ent);
				}
			} else if (content.equals("checkpoint_end") && CstState.getKeyChanged(5) == 0) {
				TimActionbarClearProcedure.execute();
				updateTutoQue();
			} else if (content.equals("racing")) {
				if (content_changed) {
					TutoRacingReadyProcedure.execute(_ent);
					startTimer();
				} else if (tuto_timer == 100) {
					TutoRacingStartProcedure.execute(world, _ent.getX(), _ent.getY(), _ent.getZ(), _ent);
					stopTimer();
				}
			}
		}
	}

	
	
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
	*/
	
}
