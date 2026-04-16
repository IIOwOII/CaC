package net.owo.cac;

import javax.annotation.Nullable;

import net.minecraft.world.phys.Vec3;
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
import net.minecraftforge.client.event.RenderGuiEvent;

import net.owo.cac.CstState;
import net.owo.cac.CstAgent;
import net.owo.cac.CstSurvey;
import net.owo.cac.CstSurrender;
import net.owo.cac.network.CacModVariables;

import net.owo.cac.procedures.TutoComebackProcedure;
import net.owo.cac.procedures.AdpBeginnerProcedure;
import net.owo.cac.procedures.AdpCheckpointProcedure;
import net.owo.cac.procedures.AdpRacingProcedure;
import net.owo.cac.procedures.AdpChasingProcedure;
import net.owo.cac.procedures.AdpChasedProcedure;
import net.owo.cac.procedures.AdpSurveyProcedure;
import net.owo.cac.procedures.AdpSurrenderProcedure;
import net.owo.cac.procedures.MeowViewOnProcedure;
import net.owo.cac.procedures.MeowMoveOnProcedure;
import net.owo.cac.procedures.TaskSpawnOpponentProcedure;
import net.owo.cac.procedures.EffApplyMorphPredatorProcedure;
import net.owo.cac.procedures.EffApplyMorphPreyProcedure;
import net.owo.cac.procedures.EffRemoveMorphProcedure;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstTutorial {
	/*
	1: moving
	2: checkpoint
	3: racing
	10: chasing inst
	15: chasing no inst
	20: chased inst
	25: chased no inst
	30: survey
	40: surrender
	100: book_0
	101: book_1 ...
	*/
	static final int[] TUTO_QUE = {100,101,102,1,103};
	static int[] MOVING_ORD = {2,0,5,3,6,1,7,4};
	
	public static int tuto_id = 0;
	public static int moving_idx = 0;
	public static int[] moving_footprint = {0,0,0,0,0,0,0,0};
	
	public static int timer = 0;
	public static boolean timer_switch = false;
	public static int adv_id = 0;
	public static boolean adv_switch = false;
	public static boolean adv_certificate = false;
	
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

	
	@SubscribeEvent
	public static void onRenderGui(RenderGuiEvent.Pre event) {
		if (tuto_id == 0) return;
		GuiGraphics gg = event.getGuiGraphics();
		int gw = event.getWindow().getGuiScaledWidth();
		int gh = event.getWindow().getGuiScaledHeight();
		if (tuto_id == 1) {
			renderArrow(gg, gw, gh, MOVING_ORD[moving_idx]);
		}
	}
	
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (tuto_id == 0) return;
		if (event.phase == TickEvent.Phase.END) {
			if (tuto_id == 1) {
				int moving_diff = CstState.meowmove_tick[MOVING_ORD[moving_idx]] - moving_footprint[MOVING_ORD[moving_idx]];
				if (moving_diff >= 60) {
					if (moving_idx < MOVING_ORD.length - 1) {
						moving_idx += 1;
						moving_footprint[MOVING_ORD[moving_idx]] = CstState.meowmove_tick[MOVING_ORD[moving_idx]];
					} else {
						completeMission(tuto_id, true);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		Entity _ent = event.player;
		LevelAccessor world = _ent.level();
		if (world.isClientSide() || _ent == null) return;
		if (event.phase == TickEvent.Phase.END) {
			if (tuto_id == 10 && CstState.getKeyChanged(5) == 0) { // Chasing Prep
				chasingGameTutorial(_ent);
			}
			if (tuto_id == 20 && CstState.getKeyChanged(5) == 0) { // Chased Prep
				chasedGameTutorial(_ent);
			}
			if (!timer_switch) return;
			if (adv_switch) {
				if (adv_id == 1) {
					AdpBeginnerProcedure.execute(_ent);
				} else if ((adv_id == 2) && (adv_certificate)) {
					AdpCheckpointProcedure.execute(_ent);
				} else if ((adv_id == 3) && (adv_certificate)) {
					AdpRacingProcedure.execute(_ent);
				} else if ((adv_id == 4) && (adv_certificate)) {
					AdpChasingProcedure.execute(_ent);
				} else if ((adv_id == 5) && (adv_certificate)) {
					AdpChasedProcedure.execute(_ent);
				} else if ((adv_id == 6) && (adv_certificate)) {
					AdpSurveyProcedure.execute(_ent);
				} else if ((adv_id == 7) && (adv_certificate)) {
					AdpSurrenderProcedure.execute(_ent);
				}
				adv_switch = false;
				adv_certificate = false;
			}
			if (timer <= 0) {
				CacModVariables.Msg_actionbar_switch = false;
				TutoComebackProcedure.execute(world, _ent);
				timer_switch = false;
			}
			timer = timer - 1;
		}
	}
	
	public static void renderBook(GuiGraphics gg, int gw, int gh, int ID) {
		gg.blit(tutorial_book[ID], 0, 0, 0, 0, gw, gh, gw, gh);
	}

	public static void renderArrow(GuiGraphics gg, int gw, int gh, int ID) {
		gg.blit(tutorial_arrow[ID], gw/2-60, gh/2-60, 0, 0, 120, 120, 120, 120);
	}

	public static void completeMission(int id, boolean is_win) {
		tuto_id = 0;
		if (id == 1) {
			CstState.offMeowMove();
			CacModVariables.Msg_actionbar_switch = true;
			CacModVariables.Msg_actionbar_text = "\uC798\uD588\uC2B5\uB2C8\uB2E4!";
		}
		timer = 60;
		timer_switch = true;
		adv_id = id;
		adv_certificate = is_win;
		adv_switch = true;
	}

	public static int getTutorialID() {
		return tuto_id;
	}
	
	public static void movingTutorial() {
		moving_footprint = CstState.meowmove_tick.clone();
		moving_idx = 0;
		CacModVariables.Msg_actionbar_text = "\uD654\uBA74\uC5D0 \uC9C0\uC2DC\uB41C \uBC29\uD5A5\uB300\uB85C \uACC4\uC18D \uC6C0\uC9C1\uC5EC\uC8FC\uC138\uC694.";
		CacModVariables.Msg_actionbar_switch = true;
		tuto_id = 1;
	}

	public static void checkpointTutorial() {
		tuto_id = 2;
	}

	public static void racingTutorial() {
		tuto_id = 3;
	}

	public static void surveyTutorial() {
		tuto_id = 30;
		CstSurvey.startSurvey();
	}

	public static void surrenderTutorial() {
		tuto_id = 40;
		CstSurrender.startSurrender();
	}

	public static void chasingPrepTutorial(Entity entity) {
		if (entity == null) return;
		LevelAccessor world = entity.level();
		if (world == null || world.isClientSide()) return;
		MinecraftServer server = entity.getServer();
		if (server == null) return;
		
		tuto_id = 10;
		CacModVariables.Switch_AI = false;
		server.getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), world instanceof ServerLevel ? (ServerLevel) world : null, 4,
			entity.getName().getString(), entity.getDisplayName(), server, entity), "cac_tp task");
		server.getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), world instanceof ServerLevel ? (ServerLevel) world : null, 4,
			entity.getName().getString(), entity.getDisplayName(), server, entity), "worldborder set 8");
		MeowViewOnProcedure.execute();
		MeowMoveOnProcedure.execute();
		CacModVariables.Dat_trial_spawnpoint_opponent = 0;
		CacModVariables.Dat_trial_type = 0;
		CacModVariables.Dat_difficulty = 0.8;
		TaskSpawnOpponentProcedure.execute(world);
		EffApplyMorphPredatorProcedure.execute(entity);
		CacModVariables.Msg_actionbar_text = "\uACE0\uC591\uC774\uB97C \uC870\uC885\uD558\uC5EC \uC950\uC758 \uC704\uCE58\uB97C \uBBF8\uB9AC \uD655\uC778\uD558\uACE0 \uC790\uB9AC\uB97C \uC7A1\uC73C\uC138\uC694.\\n\uC900\uBE44\uB418\uBA74 \u00A7e\uACB0\uC815 \uBC84\uD2BC\u00A7r\uC744 \uB20C\uB7EC \uC2DC\uC791\uD558\uAE30.";
		CacModVariables.Msg_actionbar_switch = true;
	}
	public static void chasingGameTutorial(Entity entity) {
		if (entity == null) return;
		LevelAccessor world = entity.level();
		if (world == null || world.isClientSide()) return;
		MinecraftServer server = entity.getServer();
		if (server == null) return;
		
		tuto_id = 11;
		server.getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), world instanceof ServerLevel ? (ServerLevel) world : null, 4,
			entity.getName().getString(), entity.getDisplayName(), server, entity), "worldborder set 10000000");
		CacModVariables.Msg_actionbar_text = "\uC950\uAC00 \uB3C4\uB9DD\uCE69\uB2C8\uB2E4! \uC81C\uC2DC\uAC04 \uC548\uC5D0 \uC7A1\uC544\uC8FC\uC138\uC694.";
		CstAgent.setDuration(600);
		CacModVariables.Switch_AI = true;
	}
	public static void chasingEndTutorial(Entity entity) {
		CacModVariables.Msg_actionbar_switch = false;
		tuto_id = 12;
		EffRemoveMorphProcedure.execute(entity);
	}

	public static void chasedPrepTutorial(Entity entity) {
		if (entity == null) return;
		LevelAccessor world = entity.level();
		if (world == null || world.isClientSide()) return;
		MinecraftServer server = entity.getServer();
		if (server == null) return;
		
		tuto_id = 20;
		CacModVariables.Switch_AI = false;
		server.getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), world instanceof ServerLevel ? (ServerLevel) world : null, 4,
			entity.getName().getString(), entity.getDisplayName(), server, entity), "cac_tp task");
		server.getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), world instanceof ServerLevel ? (ServerLevel) world : null, 4,
			entity.getName().getString(), entity.getDisplayName(), server, entity), "worldborder set 8");
		MeowViewOnProcedure.execute();
		MeowMoveOnProcedure.execute();
		CacModVariables.Dat_trial_spawnpoint_opponent = 2;
		CacModVariables.Dat_trial_type = 1;
		CacModVariables.Dat_difficulty = 0.8;
		TaskSpawnOpponentProcedure.execute(world);
		EffApplyMorphPreyProcedure.execute(entity);
		CacModVariables.Msg_actionbar_text = "\uC950\uB97C \uC870\uC885\uD558\uC5EC \uACE0\uC591\uC774\uC758 \uC704\uCE58\uB97C \uBBF8\uB9AC \uD655\uC778\uD558\uACE0 \uC790\uB9AC\uB97C \uC7A1\uC73C\uC138\uC694.\\n\uC900\uBE44\uB418\uBA74 \u00A7e\uACB0\uC815 \uBC84\uD2BC\u00A7r\uC744 \uB20C\uB7EC \uC2DC\uC791\uD558\uAE30.";
		CacModVariables.Msg_actionbar_switch = true;
	}
	public static void chasedGameTutorial(Entity entity) {
		if (entity == null) return;
		LevelAccessor world = entity.level();
		if (world == null || world.isClientSide()) return;
		MinecraftServer server = entity.getServer();
		if (server == null) return;
		
		tuto_id = 21;
		server.getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), world instanceof ServerLevel ? (ServerLevel) world : null, 4,
			entity.getName().getString(), entity.getDisplayName(), server, entity), "worldborder set 10000000");
		CacModVariables.Msg_actionbar_text = "\uACE0\uC591\uC774\uAC00 \uCAD3\uC544\uC635\uB2C8\uB2E4! \uC81C\uC2DC\uAC04 \uB3D9\uC548 \uB3C4\uB9DD\uCE58\uC138\uC694.";
		CstAgent.setDuration(600);
		CacModVariables.Switch_AI = true;
	}
	public static void chasedEndTutorial(Entity entity) {
		CacModVariables.Msg_actionbar_switch = false;
		tuto_id = 22;
		EffRemoveMorphProcedure.execute(entity);
	}
}
