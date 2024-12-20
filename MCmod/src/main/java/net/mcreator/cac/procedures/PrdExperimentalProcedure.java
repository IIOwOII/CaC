package net.mcreator.cac.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;

import net.mcreator.cac.network.CacModVariables;
import net.mcreator.cac.CacMod;

public class PrdExperimentalProcedure {
	public static void execute(LevelAccessor world) {
		Vec3 vec = Vec3.ZERO;
		CacMod.LOGGER.info(CacModVariables.MapVariables.get(world).List_wall);
	}
}
