package net.owo.cac;

import javax.annotation.Nullable;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;


public class CstItem {
	
	public static int[] item_option = {0, 0};
	private static final int[] ITEM_OPTION_MAX = {3, 3};
	private static final String[][] ITEM_OPTION_TEXT = {
		{"Increase Difficulty", "Decrease Difficulty", "AI Switch"},
		{"Change Blockstate", "Set 1", "Set 2"}};
	
	public static int getItemOption(int id) {
		return item_option[id];
	}

	public static String getItemOptionText(int id) {
		return ITEM_OPTION_TEXT[id][item_option[id]];
	}
	
	public static void modifyItemOption(int id, int addnum) {
		item_option[id] = Math.floorMod((item_option[id] + addnum), ITEM_OPTION_MAX[id]);
		printItemOption(id);
	}

	public static void printItemOption(int id) {
		@Nullable Player player = Minecraft.getInstance().player;
		if (player == null) return;
		player.displayClientMessage(Component.literal(ITEM_OPTION_TEXT[id][item_option[id]]), true);
	}
	
}