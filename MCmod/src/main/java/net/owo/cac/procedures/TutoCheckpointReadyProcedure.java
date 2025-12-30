package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class TutoCheckpointReadyProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "cac_tp tutorial_checkpoint");
			}
		}
		MeowMoveOffProcedure.execute();
		MeowViewOnProcedure.execute();
		EffApplyMorphPredatorProcedure.execute(entity);
		CacModVariables.Tuto_score = 3000;
		CacModVariables.Tuto_checkpoint_index = 0;
		CacModVariables.Msg_actionbar_text = "Score : " + new java.text.DecimalFormat("#####").format(CacModVariables.Tuto_score);
		CacModVariables.Msg_actionbar_switch = true;
		CacModVariables.TimC_time = 100;
		CacModVariables.TimC_que = "tutorial_checkpoint";
		CacModVariables.TimC_switch = true;
	}
}
