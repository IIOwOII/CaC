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
	static int[] MOVING_ORD = {2,0,5,3,6,1,7,4};

	public static int tuto_id = 0;
	public static int tuto_timer = 0;
	public static int moving_idx = 0;
	public static int[] moving_footprint = {0,0,0,0,0,0,0,0};
	
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
	public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
		if (tuto_id == 0) return;
		GuiGraphics gg = event.getGuiGraphics();
		if (tuto_id == 1) {
			renderArrow(MOVING_ORD[moving_idx]);
		}
	}
	
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (tuto_id == 0) return;
		if (event.phase == TickEvent.Phase.END) {
			if (tuto_id == 1) {
				int moving_diff = CstState.meowmove_tick[MOVING_ORD[moving_idx]] - moving_footprint[MOVING_ORD[moving_idx]];
				if (moving_diff >= 60) {
					if (moving_idx < MOVING_ORD.length) {
						moving_idx += 1;
					} else {
						completeMission(tuto_id);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (tuto_timer <= 0) return;
		if (event.phase == TickEvent.Phase.END) {
			tuto_timer = tuto_timer - 1;
			if (tuto_timer == 0) {
				CacModVariables.Msg_actionbar_switch = false;
				Entity _ent = event.player;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "cac_tp tutorial");
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

	public static void completeMission(int id) {
		tuto_id = 0;
		CstState.CanMeowMove = false;
		CacModVariables.Msg_actionbar_switch = true;
		CacModVariables.Msg_actionbar_text = "\uC798\uD588\uC2B5\uB2C8\uB2E4!";
	}
	
	public static void movingTutorial() {
		moving_footprint = CstState.meowmove_tick.clone();
		moving_idx = 0;
		CacModVariables.Msg_actionbar_text = "\uD654\uBA74\uC5D0 \uC9C0\uC2DC\uB41C \uBC29\uD5A5\uB300\uB85C \uACC4\uC18D \uC6C0\uC9C1\uC5EC\uC8FC\uC138\uC694.";
		CacModVariables.Msg_actionbar_switch = true;
		tuto_id = 1;
		tuto_timer = 60;
	}
	
}
