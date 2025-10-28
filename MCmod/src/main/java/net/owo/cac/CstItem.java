package net.owo.cac;

import javax.annotation.Nullable;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.owo.cac.CacMod;
import net.owo.cac.init.CacModItems;
import net.owo.cac.network.CacModVariables;
import net.owo.cac.network.CacModVariables.MapVariables;

public class CstItem {	
	static final int OPTION_TESTER_MAX = 3;
	static final int OPTION_BUILDER_MAX = 2;
	
	public static void modifyItemOption(int _cond, int _modnum) {
		Minecraft mc = Minecraft.getInstance();
		@Nullable Entity _ent = mc.player;
		LevelAccessor world = _ent.level();
		if (_ent == null)
			return;

		switch (_cond) {
			case 10:
				CacModVariables.MapVariables.get(world).Option_tester = Math.floorMod((int)CacModVariables.MapVariables.get(world).Option_tester + _modnum, OPTION_TESTER_MAX);
				CacModVariables.MapVariables.get(world).syncData(world);
				break;
			case 11:
				CacModVariables.MapVariables.get(world).Option_builder = Math.floorMod((int)CacModVariables.MapVariables.get(world).Option_builder + _modnum, OPTION_BUILDER_MAX);
				CacModVariables.MapVariables.get(world).syncData(world);
				break;
		}
		printItemOption(_cond);
	}

	public static void printItemOption(int _cond) {
		Minecraft mc = Minecraft.getInstance();
		@Nullable Entity _ent = mc.player;
		LevelAccessor world = _ent.level();
		if (_ent == null)
			return;
		
		int option_num = -1;
		String option_str = "";
		
		switch (_cond) {
			case 10:
				option_num = (int)CacModVariables.MapVariables.get(world).Option_tester;
				switch (option_num) {
					case 0:
						option_str = "Increase Difficulty";
						break;
					case 1:
						option_str = "Decrease Difficulty";
						break;
					case 2:
						option_str = "AI Switch";
						break;
				}
				CacModVariables.MapVariables.get(world).Option_tester_str = new String(option_str);
				CacModVariables.MapVariables.get(world).syncData(world);
				if (_ent instanceof Player _player && _player.level().isClientSide())
					_player.displayClientMessage(Component.literal(CacModVariables.MapVariables.get(world).Option_tester_str), true);
				break;
			case 11:
				option_num = (int)CacModVariables.MapVariables.get(world).Option_builder;
				switch (option_num) {
					case 0:
						option_str = "Set 1";
						break;
					case 1:
						option_str = "Set 2";
						break;
				}
				CacModVariables.MapVariables.get(world).Option_builder_str = new String(option_str);
				CacModVariables.MapVariables.get(world).syncData(world);
				if (_ent instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal(CacModVariables.MapVariables.get(world).Option_builder_str), true);
				break;
		}
	}
}
