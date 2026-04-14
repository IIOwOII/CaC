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
import net.owo.cac.network.CacModVariables;
import net.owo.cac.procedures.TutoComebackProcedure;
import net.owo.cac.procedures.AdpBeginnerProcedure;
import net.owo.cac.procedures.AdpCheckpointProcedure;
import net.owo.cac.procedures.AdpRacingProcedure;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstTutorial {
	/*
	1: moving
	2: checkpoint
	3: racing
	100: book_0
	101: book_1 ...
	*/
	static final int[] TUTO_QUE = {100,101,102,1,103};
	static int[] MOVING_ORD = {2,0,5,3,6,1,7,4};
	
	private static Vec3 TUTO_BEGINNER_OFFSET = new Vec3(-73.5, 63.0, 18.5);
	private static int TUTO_BEGINNER_RADIUS = 9;

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
			/*
			if (tuto_id == 1) {
				Vec3 pos = (_ent.position()).subtract(TUTO_BEGINNER_OFFSET);
				if (Math.abs(pos.x()) >= TUTO_BEGINNER_RADIUS || Math.abs(pos.z()) >= TUTO_BEGINNER_RADIUS) {
					
				}
			}
			*/
			if (!timer_switch) return;
			if (adv_switch) {
				if (adv_id == 1) {
					AdpBeginnerProcedure.execute(_ent);
				} else if ((adv_id == 2) && (adv_certificate)) {
					AdpCheckpointProcedure.execute(_ent);
				} else if ((adv_id == 3) && (adv_certificate)) {
					AdpRacingProcedure.execute(_ent);
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
}
