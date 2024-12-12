package net.mcreator.cac.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.FloatTag;

public class PrdExperimentalProcedure {
	public static void execute(LevelAccessor world) {
		ListTag list;
		list = new ListTag();
		list.addTag(list.size(), FloatTag.valueOf((float) 0.1));
		list.addTag(list.size(), FloatTag.valueOf(2));
		list.addTag(list.size(), FloatTag.valueOf(3));
		for (Tag dataelementiterator : list) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal((new java.text.DecimalFormat("##.##").format(dataelementiterator instanceof FloatTag _floatTag ? _floatTag.getAsFloat() : 0.0F))), false);
		}
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal((new java.text.DecimalFormat("##.##").format(list.size()))), false);
	}
}
