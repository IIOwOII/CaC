package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.ListTag;

public class PrdInitializeProcedure {
	public static void execute(LevelAccessor world) {
		IniReadConfigProcedure.execute(world);
		CacModVariables.MapVariables.get(world).List_obstacle = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).List_wall = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Pos_offset = new Vec3(0, 64, (-50));
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Radius_map = 16;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).List_obstacle = FncScanObstacleProcedure.execute(world);
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).List_wall = FncScanWallProcedure.execute(world);
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Timer_show = "none";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Timer_event = "none";
		CacModVariables.MapVariables.get(world).syncData(world);
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7eInitialized!"), false);
	}
}
