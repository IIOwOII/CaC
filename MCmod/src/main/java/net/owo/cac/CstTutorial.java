package net.owo.cac;

import javax.annotation.Nullable;

import net.minecraft.world.phys.Vec3;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.registries.ForgeRegistries;

import net.owo.cac.CstState;
import net.owo.cac.CstAgent;
import net.owo.cac.CstSurvey;
import net.owo.cac.CstSurrender;
import net.owo.cac.CstRenderComponent;
import net.owo.cac.network.CacModVariables;

import net.owo.cac.procedures.TutoBeginnerReadyProcedure;
import net.owo.cac.procedures.TutoCheckpointReadyProcedure;
import net.owo.cac.procedures.TutoRacingReadyProcedure;
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
	10: moving (legacy)
	20: checkpoint (legacy)
	30: racing (legacy)
	40: chasing (legacy)
	50: chased (legacy)
	60: survey (legacy)
	70: surrender (legacy)
	110: practice
	120: main
	*/
	public static int tuto_id = 0;
	public static int book_id = 0;
	public static int prac_id = 0;
	
	public static int timer = 0;
	public static boolean timer_switch = false;
	
	public static int adv_id = 0;
	public static boolean adv_switch = false;
	public static boolean adv_certificate = false;

	static ResourceLocation[] BOOK_MOVING = {
		new ResourceLocation("cac:textures/screens/book_3x2_moving_0.png"),
		new ResourceLocation("cac:textures/screens/book_3x2_moving_1.png"),
		new ResourceLocation("cac:textures/screens/book_3x2_start.png")
	};
	static ResourceLocation[] BOOK_CHECKPOINT = {
		new ResourceLocation("cac:textures/screens/book_3x2_checkpoint_0.png"),
		new ResourceLocation("cac:textures/screens/book_3x2_checkpoint_1.png"),
		new ResourceLocation("cac:textures/screens/book_3x2_checkpoint_2.png"),
		new ResourceLocation("cac:textures/screens/book_3x2_start.png")
	};
	static ResourceLocation[] BOOK_RACING = {
		new ResourceLocation("cac:textures/screens/book_3x2_racing_0.png"),
		new ResourceLocation("cac:textures/screens/book_3x2_racing_1.png"),
		new ResourceLocation("cac:textures/screens/book_3x2_start.png")
	};

	// Moving Tutorial
	static int[] MOVING_ORD = {2,0,5,3,6,1,7,4};
	public static int moving_idx = 0;
	public static int[] moving_footprint = {0,0,0,0,0,0,0,0};
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
		if (tuto_id == 11) {
			renderArrow(gg, gw, gh, MOVING_ORD[moving_idx]);
		}
		if (tuto_id == 10) {
			CstRenderComponent.renderBlankLightgrey(gg, gw, gh);
			renderBook(gg, gw, gh, BOOK_MOVING[book_id]);
		} else if (tuto_id == 20) {
			CstRenderComponent.renderBlankLightgrey(gg, gw, gh);
			renderBook(gg, gw, gh, BOOK_CHECKPOINT[book_id]);
		} else if (tuto_id == 30) {
			CstRenderComponent.renderBlankLightgrey(gg, gw, gh);
			renderBook(gg, gw, gh, BOOK_RACING[book_id]);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		Entity _ent = event.player;
		LevelAccessor world = _ent.level();
		if (world.isClientSide() || _ent == null) return;
		if (event.phase == TickEvent.Phase.END) {
			if (tuto_id == 41 && CstState.getKeyChanged(5) == 0) {chasingGameTutorial(_ent);} // Chasing Prep
			if (tuto_id == 51 && CstState.getKeyChanged(5) == 0) {chasedGameTutorial(_ent);} // Chased Prep
			if (tuto_id > 0 && tuto_id % 10 == 0) { // Book
				if (CstState.getKeyChanged(0) == 0) {
					nextBookPage(_ent);
				} else if (CstState.getKeyChanged(1) == 0) {
					prevBookPage(_ent);
				} else if (CstState.getKeyChanged(5) == 0) {
					selectBookPage(_ent);
				}
			}
			if (tuto_id == 11) {
				int moving_diff = CstState.meowmove_tick[MOVING_ORD[moving_idx]] - moving_footprint[MOVING_ORD[moving_idx]];
				if (moving_diff >= 60) {
					if (moving_idx < MOVING_ORD.length - 1) {
						moving_idx += 1;
						moving_footprint[MOVING_ORD[moving_idx]] = CstState.meowmove_tick[MOVING_ORD[moving_idx]];
					} else {
						completeMission(1, true);
					}
				}
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

	// Book Handler
	public static void nextBookPage(Entity player) {
		if (player == null) return;
		LevelAccessor world = player.level();
		if (world.isClientSide()) return;
		if (tuto_id == 10 && book_id < BOOK_MOVING.length-1) {
			book_id += 1;
			// Sound and message
			if (world instanceof Level _level) {
				_level.playSound(null, BlockPos.containing(player.getX(), player.getY(), player.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cac:snd_bookpage")), SoundSource.NEUTRAL, 1, 1);
			}
		} else if (tuto_id == 20 && book_id < BOOK_CHECKPOINT.length-1) {
			book_id += 1;
			// Sound and message
			if (world instanceof Level _level) {
				_level.playSound(null, BlockPos.containing(player.getX(), player.getY(), player.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cac:snd_bookpage")), SoundSource.NEUTRAL, 1, 1);
			}
		} else if (tuto_id == 30 && book_id < BOOK_RACING.length-1) {
			book_id += 1;
			// Sound and message
			if (world instanceof Level _level) {
				_level.playSound(null, BlockPos.containing(player.getX(), player.getY(), player.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cac:snd_bookpage")), SoundSource.NEUTRAL, 1, 1);
			}
		}
	}
	public static void prevBookPage(Entity player) {
		if (player == null) return;
		LevelAccessor world = player.level();
		if (world.isClientSide()) return;
		if (book_id > 0) {
			if (tuto_id == 10) {
				book_id -= 1;
				// Sound and message
				if (world instanceof Level _level) {
					_level.playSound(null, BlockPos.containing(player.getX(), player.getY(), player.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cac:snd_bookpage")), SoundSource.NEUTRAL, 1, 1);
				}
			} else if (tuto_id == 20) {
				book_id -= 1;
				// Sound and message
				if (world instanceof Level _level) {
					_level.playSound(null, BlockPos.containing(player.getX(), player.getY(), player.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cac:snd_bookpage")), SoundSource.NEUTRAL, 1, 1);
				}
			} else if (tuto_id == 30) {
				book_id -= 1;
				// Sound and message
				if (world instanceof Level _level) {
					_level.playSound(null, BlockPos.containing(player.getX(), player.getY(), player.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cac:snd_bookpage")), SoundSource.NEUTRAL, 1, 1);
				}
			}
		}
	}
	public static void selectBookPage(Entity player) {
		if (player == null) return;
		LevelAccessor world = player.level();
		if (world.isClientSide()) return;
		if (tuto_id == 10 && book_id == BOOK_MOVING.length-1) {
			tuto_id = 0;
			book_id = 0;
			TutoBeginnerReadyProcedure.execute(player);
		} else if (tuto_id == 20 && book_id == BOOK_CHECKPOINT.length-1) {
			tuto_id = 0;
			book_id = 0;
			TutoCheckpointReadyProcedure.execute(player);
		} else if (tuto_id == 30 && book_id == BOOK_RACING.length-1) {
			tuto_id = 0;
			book_id = 0;
			TutoRacingReadyProcedure.execute(player);
		}
	}

	// Rendering
	public static void renderBook(GuiGraphics gg, int gw, int gh, ResourceLocation book) {
		gg.blit(book, gw/2-180, gh/2-120, 0, 0, 360, 240, 360, 240);
	}

	public static void renderArrow(GuiGraphics gg, int gw, int gh, int ID) {
		gg.blit(tutorial_arrow[ID], gw/2-60, gh/2-60, 0, 0, 120, 120, 120, 120);
	}

	// Advancement
	public static void completeMission(int id, boolean is_win) {
		tuto_id = 0;
		if (id == 1) {
			CstState.offMeowMove();
			CacModVariables.Msg_actionbar_switch = true;
			CacModVariables.Msg_actionbar_text = "\uC88B\uC2B5\uB2C8\uB2E4!";
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

	public static void practiceTutorial() {
		tuto_id = 110;
		prac_id = 0;
	}

	// (Legacy)
	public static void movingTutorial() {
		moving_footprint = CstState.meowmove_tick.clone();
		moving_idx = 0;
		CacModVariables.Msg_actionbar_text = "\uD654\uC0B4\uD45C \uCABD\uC73C\uB85C \uC6C0\uC9C1\uC5EC\uC8FC\uC138\uC694!";
		CacModVariables.Msg_actionbar_switch = true;
		tuto_id = 11;
	}

	public static void checkpointTutorial() {
		tuto_id = 21;
	}

	public static void racingTutorial() {
		tuto_id = 31;
	}

	public static void surveyTutorial() {
		tuto_id = 61;
		CstSurvey.initSurvey();
		CstSurvey.startSurvey();
	}

	public static void surrenderTutorial() {
		tuto_id = 71;
		CstSurrender.startSurrender();
	}

	public static void chasingPrepTutorial(Entity entity) {
		if (entity == null) return;
		LevelAccessor world = entity.level();
		if (world == null || world.isClientSide()) return;
		MinecraftServer server = entity.getServer();
		if (server == null) return;
		
		tuto_id = 41;
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
		
		tuto_id = 42;
		server.getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), world instanceof ServerLevel ? (ServerLevel) world : null, 4,
			entity.getName().getString(), entity.getDisplayName(), server, entity), "worldborder set 10000000");
		CacModVariables.Msg_actionbar_text = "\uC950\uAC00 \uB3C4\uB9DD\uCE69\uB2C8\uB2E4! \uC81C\uC2DC\uAC04 \uC548\uC5D0 \uC7A1\uC544\uC8FC\uC138\uC694.";
		CstAgent.setDuration(600);
		CacModVariables.Switch_AI = true;
	}
	public static void chasingEndTutorial(Entity entity) {
		CacModVariables.Msg_actionbar_switch = false;
		tuto_id = 43;
		EffRemoveMorphProcedure.execute(entity);
	}

	public static void chasedPrepTutorial(Entity entity) {
		if (entity == null) return;
		LevelAccessor world = entity.level();
		if (world == null || world.isClientSide()) return;
		MinecraftServer server = entity.getServer();
		if (server == null) return;
		
		tuto_id = 51;
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
		
		tuto_id = 52;
		server.getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), world instanceof ServerLevel ? (ServerLevel) world : null, 4,
			entity.getName().getString(), entity.getDisplayName(), server, entity), "worldborder set 10000000");
		CacModVariables.Msg_actionbar_text = "\uACE0\uC591\uC774\uAC00 \uCAD3\uC544\uC635\uB2C8\uB2E4! \uC81C\uC2DC\uAC04 \uB3D9\uC548 \uB3C4\uB9DD\uCE58\uC138\uC694.";
		CstAgent.setDuration(600);
		CacModVariables.Switch_AI = true;
	}
	public static void chasedEndTutorial(Entity entity) {
		CacModVariables.Msg_actionbar_switch = false;
		tuto_id = 53;
		EffRemoveMorphProcedure.execute(entity);
	}
}
