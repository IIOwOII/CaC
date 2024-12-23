package net.mcreator.cac.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.ListTag;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.cac.network.CacModVariables;

public class PrdInitializeProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.MapVariables.get(world).List_obstacle = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).List_wall = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Pos_offset = new Vec3(0, 64, (-50));
		CacModVariables.MapVariables.get(world).syncData(world);
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Offset Position Setting"), false);
		CacModVariables.MapVariables.get(world).Radius_map = 16;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("Radius of map = " + new java.text.DecimalFormat("##").format(CacModVariables.MapVariables.get(world).Radius_map))), false);
		CacModVariables.MapVariables.get(world).List_obstacle = FncScanObstacleProcedure.execute(world);
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).List_wall = FncScanWallProcedure.execute(world);
		CacModVariables.MapVariables.get(world).syncData(world);
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Scan Obstacle & Wall"), false);
		CacModVariables.MapVariables.get(world).Option_hotbar = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Hotbar canceled"), false);
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("title " + "@a " + "times " + "10 100 10"));
			}
		}
		CacModVariables.MapVariables.get(world).Pmt_far = 15;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("Pmt_far = " + new java.text.DecimalFormat("##.##").format(CacModVariables.MapVariables.get(world).Pmt_far))), false);
		CacModVariables.MapVariables.get(world).Pmt_distance_scale = 1;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("Pmt_distance_scale = " + new java.text.DecimalFormat("##.##").format(CacModVariables.MapVariables.get(world).Pmt_distance_scale))), false);
		CacModVariables.MapVariables.get(world).Exp_Rest = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Exp_Rest = False"), false);
		CacModVariables.MapVariables.get(world).Timer_show = "none";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Timer_event = "none";
		CacModVariables.MapVariables.get(world).syncData(world);
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Timer Reset"), false);
	}
}
