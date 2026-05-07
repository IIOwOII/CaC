package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class SimManageProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		String sim_type = "";
		CacModVariables.Exp_subject = "simulation";
		CacModVariables.Dir_behaviors = FMLPaths.GAMEDIR.get().toString() + "/cacutil/behaviors/" + CacModVariables.Exp_subject;
		IniInfoTimestampProcedure.execute(world);
		sim_type = StringArgumentType.getString(arguments, "type");
		CacModVariables.Exp_trial_total = DoubleArgumentType.getDouble(arguments, "trial");
		if ((sim_type).equals("chasing")) {
			CacModVariables.Exp_session = "simulation_chasing";
			CacModVariables.Dat_trial_type = 0;
		} else if ((sim_type).equals("chased")) {
			CacModVariables.Exp_session = "simulation_chased";
			CacModVariables.Dat_trial_type = 1;
		} else if ((sim_type).equals("fit_chasing") || (sim_type).equals("fit_chased")) {
			CacModVariables.Psy_method = "both";
			net.owo.cac.CstPsychometric.method_type = 0;
			if ((sim_type).equals("fit_chasing")) {
				CacModVariables.Exp_session = "simulation_fit_chasing";
				CacModVariables.Psy_task = "chasing";
				net.owo.cac.CstPsychometric.task_type = 0;
				net.owo.cac.CstPsychometric.initPsy();
				CacModVariables.Dat_trial_type = 0;
			} else if ((sim_type).equals("fit_chased")) {
				CacModVariables.Exp_session = "simulation_fit_chased";
				CacModVariables.Psy_task = "chased";
				net.owo.cac.CstPsychometric.task_type = 1;
				net.owo.cac.CstPsychometric.initPsy();
				CacModVariables.Dat_trial_type = 1;
			}
			net.owo.cac.CstPsychometric.TRIAL_MAX = CacModVariables.Exp_trial_total;
		}
		if (CacModVariables.Exp_session.startsWith("simulation")) {
			EvResetProcedure.execute();
			TimResetProcedure.execute();
			IniLogProcedure.execute();
			IniQueProcedure.execute();
			EvQueCallProcedure.execute(world, entity);
		} else {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Please check the task name!"), false);
		}
	}
}
