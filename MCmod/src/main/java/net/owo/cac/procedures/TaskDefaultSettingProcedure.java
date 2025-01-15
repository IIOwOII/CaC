package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;

public class TaskDefaultSettingProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.MapVariables.get(world).Switch_Task = true;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (entity instanceof ServerPlayer _player)
			_player.setGameMode(GameType.ADVENTURE);
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, -1, 0, false, false));
		CacModVariables.MapVariables.get(world).Exp_trial = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Exp_phase = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
