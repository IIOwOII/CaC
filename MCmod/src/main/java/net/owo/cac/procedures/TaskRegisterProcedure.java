package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import java.util.Calendar;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class TaskRegisterProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		File file_timestamp = new File("");
		com.google.gson.JsonObject obj_main = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_sub = new com.google.gson.JsonObject();
		CacModVariables.MapVariables.get(world).Exp_subject = StringArgumentType.getString(arguments, "subject");
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Exp_path = FMLPaths.GAMEDIR.get().toString() + "/cacutil/behaviors/" + StringArgumentType.getString(arguments, "subject");
		CacModVariables.MapVariables.get(world).syncData(world);
		file_timestamp = new File(CacModVariables.MapVariables.get(world).Exp_path, File.separator + "log_timestamp.json");
		if (!file_timestamp.exists()) {
			try {
				file_timestamp.getParentFile().mkdirs();
				file_timestamp.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		obj_sub.addProperty("register", Calendar.getInstance().getTime().toString());
		obj_main.add("cac", obj_sub);
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(file_timestamp);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_main));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		if (CacModVariables.MapVariables.get(world).Switch_debug) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7esubject: \u00A7r" + CacModVariables.MapVariables.get(world).Exp_subject)), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7epath: \u00A7r" + CacModVariables.MapVariables.get(world).Exp_path)), false);
		}
		if (entity instanceof LivingEntity _entity)
			_entity.removeAllEffects();
		if (entity instanceof ServerPlayer _player)
			_player.setGameMode(GameType.ADVENTURE);
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, -1, 0, false, false));
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "title @p times 10 100 10");
			}
		}
	}
}
