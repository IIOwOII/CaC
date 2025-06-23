package net.owo.cac.procedures;

import org.checkerframework.checker.units.qual.m;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.DoubleTag;

public class PsyInvpsiLogisticProcedure {
	public static double execute(LevelAccessor world) {
		double m = 0;
		double w = 0;
		double gamma = 0;
		double lambda = 0;
		double p = 0;
		double rho = 0;
		p = CacModVariables.MapVariables.get(world).Dat_difficulty_relative;
		m = (CacModVariables.MapVariables.get(world).Dat_psy_param.get(0)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		w = (CacModVariables.MapVariables.get(world).Dat_psy_param.get(1)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		gamma = (CacModVariables.MapVariables.get(world).Dat_psy_param.get(2)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		lambda = (CacModVariables.MapVariables.get(world).Dat_psy_param.get(3)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		rho = m + 0.1698 * w * Math.log(((1 - p) - lambda) / (p - gamma));
		return rho;
	}
}
