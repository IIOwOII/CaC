package net.owo.cac.network;

import net.owo.cac.CacMod;

import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.CompoundTag;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CacModVariables {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		CacMod.addNetworkMessage(SavedDataSyncMessage.class, SavedDataSyncMessage::buffer, SavedDataSyncMessage::new, SavedDataSyncMessage::handler);
	}

	@Mod.EventBusSubscriber
	public static class EventBusVariableHandlers {
		@SubscribeEvent
		public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
			if (!event.getEntity().level().isClientSide()) {
				SavedData mapdata = MapVariables.get(event.getEntity().level());
				SavedData worlddata = WorldVariables.get(event.getEntity().level());
				if (mapdata != null)
					CacMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SavedDataSyncMessage(0, mapdata));
				if (worlddata != null)
					CacMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SavedDataSyncMessage(1, worlddata));
			}
		}

		@SubscribeEvent
		public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
			if (!event.getEntity().level().isClientSide()) {
				SavedData worlddata = WorldVariables.get(event.getEntity().level());
				if (worlddata != null)
					CacMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SavedDataSyncMessage(1, worlddata));
			}
		}
	}

	public static class WorldVariables extends SavedData {
		public static final String DATA_NAME = "cac_worldvars";

		public static WorldVariables load(CompoundTag tag) {
			WorldVariables data = new WorldVariables();
			data.read(tag);
			return data;
		}

		public void read(CompoundTag nbt) {
		}

		@Override
		public CompoundTag save(CompoundTag nbt) {
			return nbt;
		}

		public void syncData(LevelAccessor world) {
			this.setDirty();
			if (world instanceof Level level && !level.isClientSide())
				CacMod.PACKET_HANDLER.send(PacketDistributor.DIMENSION.with(level::dimension), new SavedDataSyncMessage(1, this));
		}

		static WorldVariables clientSide = new WorldVariables();

		public static WorldVariables get(LevelAccessor world) {
			if (world instanceof ServerLevel level) {
				return level.getDataStorage().computeIfAbsent(e -> WorldVariables.load(e), WorldVariables::new, DATA_NAME);
			} else {
				return clientSide;
			}
		}
	}

	public static class MapVariables extends SavedData {
		public static final String DATA_NAME = "cac_mapvars";
		public double Option_tester = 0.0;
		public String Option_tester_str = "\"Reset\"";
		public double Pmt_difficulty = 0.5;
		public double Pos_player_x = 0;
		public double Pos_player_z = 0;
		public double Pmt_far = 15.0;
		public double Timer_time = 0.0;
		public String Timer_event = "\"none\"";
		public String Timer_show = "\"none\"";
		public double Pmt_unittime = 30.0;
		public ListTag Exp_prechasing_time = new ListTag();
		public ListTag Exp_prechasing_win = new ListTag();
		public ListTag Exp_prechasing_difficulty = new ListTag();
		public double Exp_prechasing_trial = 0;
		public ListTag Exp_prechased_time = new ListTag();
		public ListTag Exp_prechased_win = new ListTag();
		public ListTag Exp_prechased_difficulty = new ListTag();
		public double Exp_prechased_trial = 0;
		public ListTag Exp_pre_type = new ListTag();
		public ListTag List_obstacle = new ListTag();
		public Vec3 Pos_offset = Vec3.ZERO;
		public double Radius_map = 16.0;
		public ListTag List_wall = new ListTag();
		public boolean Exp_signal = false;
		public double Exp_phase = 0;
		public String Log_error = "\"\"";
		public String Exp_session = "\"none\"";
		public double Exp_trial = 0;
		public ListTag Dat_spawn_opponent = new ListTag();
		public ListTag Dat_time_preparation = new ListTag();
		public ListTag Dat_type_trial = new ListTag();
		public String UUID_opponent = "\"\"";
		public String UUID_player = "\"\"";
		public ListTag Pool_spawn = new ListTag();
		public boolean Switch_AI = false;
		public boolean Switch_exp = false;
		public boolean Tim_trial_switch = false;
		public double Tim_trial_time = 0;
		public ListTag Dat_time_gameplay = new ListTag();
		public ListTag Dat_win = new ListTag();
		public boolean Switch_debug = false;
		public boolean Tim_survey_switch = false;
		public double Tim_survey_time = 0;
		public ListTag Dat_type_survey = new ListTag();
		public ListTag Dat_time_survey = new ListTag();
		public String Exp_subject = "\"\"";
		public String Exp_path = "\"\"";
		public ListTag Dat_speed = new ListTag();
		public double Dat_trial_total = 0;
		public ListTag Dat_time_interval = new ListTag();
		public boolean Switch_blank = false;
		public ListTag Dat_answer_survey = new ListTag();
		public double Exp_survey_idx = 0;

		public static MapVariables load(CompoundTag tag) {
			MapVariables data = new MapVariables();
			data.read(tag);
			return data;
		}

		public void read(CompoundTag nbt) {
			Option_tester = nbt.getDouble("Option_tester");
			Option_tester_str = nbt.getString("Option_tester_str");
			Pmt_difficulty = nbt.getDouble("Pmt_difficulty");
			Pos_player_x = nbt.getDouble("Pos_player_x");
			Pos_player_z = nbt.getDouble("Pos_player_z");
			Pmt_far = nbt.getDouble("Pmt_far");
			Timer_time = nbt.getDouble("Timer_time");
			Timer_event = nbt.getString("Timer_event");
			Timer_show = nbt.getString("Timer_show");
			Pmt_unittime = nbt.getDouble("Pmt_unittime");
			this.Exp_prechasing_time = nbt.get("Exp_prechasing_time") instanceof ListTag Exp_prechasing_time ? Exp_prechasing_time : new ListTag();
			this.Exp_prechasing_win = nbt.get("Exp_prechasing_win") instanceof ListTag Exp_prechasing_win ? Exp_prechasing_win : new ListTag();
			this.Exp_prechasing_difficulty = nbt.get("Exp_prechasing_difficulty") instanceof ListTag Exp_prechasing_difficulty ? Exp_prechasing_difficulty : new ListTag();
			Exp_prechasing_trial = nbt.getDouble("Exp_prechasing_trial");
			this.Exp_prechased_time = nbt.get("Exp_prechased_time") instanceof ListTag Exp_prechased_time ? Exp_prechased_time : new ListTag();
			this.Exp_prechased_win = nbt.get("Exp_prechased_win") instanceof ListTag Exp_prechased_win ? Exp_prechased_win : new ListTag();
			this.Exp_prechased_difficulty = nbt.get("Exp_prechased_difficulty") instanceof ListTag Exp_prechased_difficulty ? Exp_prechased_difficulty : new ListTag();
			Exp_prechased_trial = nbt.getDouble("Exp_prechased_trial");
			this.Exp_pre_type = nbt.get("Exp_pre_type") instanceof ListTag Exp_pre_type ? Exp_pre_type : new ListTag();
			this.List_obstacle = nbt.get("List_obstacle") instanceof ListTag List_obstacle ? List_obstacle : new ListTag();
			{
				ListTag listTag = nbt.getList("Pos_offset", 6);
				this.Pos_offset = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			Radius_map = nbt.getDouble("Radius_map");
			this.List_wall = nbt.get("List_wall") instanceof ListTag List_wall ? List_wall : new ListTag();
			Exp_signal = nbt.getBoolean("Exp_signal");
			Exp_phase = nbt.getDouble("Exp_phase");
			Log_error = nbt.getString("Log_error");
			Exp_session = nbt.getString("Exp_session");
			Exp_trial = nbt.getDouble("Exp_trial");
			this.Dat_spawn_opponent = nbt.get("Dat_spawn_opponent") instanceof ListTag Dat_spawn_opponent ? Dat_spawn_opponent : new ListTag();
			this.Dat_time_preparation = nbt.get("Dat_time_preparation") instanceof ListTag Dat_time_preparation ? Dat_time_preparation : new ListTag();
			this.Dat_type_trial = nbt.get("Dat_type_trial") instanceof ListTag Dat_type_trial ? Dat_type_trial : new ListTag();
			UUID_opponent = nbt.getString("UUID_opponent");
			UUID_player = nbt.getString("UUID_player");
			this.Pool_spawn = nbt.get("Pool_spawn") instanceof ListTag Pool_spawn ? Pool_spawn : new ListTag();
			Switch_AI = nbt.getBoolean("Switch_AI");
			Switch_exp = nbt.getBoolean("Switch_exp");
			Tim_trial_switch = nbt.getBoolean("Tim_trial_switch");
			Tim_trial_time = nbt.getDouble("Tim_trial_time");
			this.Dat_time_gameplay = nbt.get("Dat_time_gameplay") instanceof ListTag Dat_time_gameplay ? Dat_time_gameplay : new ListTag();
			this.Dat_win = nbt.get("Dat_win") instanceof ListTag Dat_win ? Dat_win : new ListTag();
			Switch_debug = nbt.getBoolean("Switch_debug");
			Tim_survey_switch = nbt.getBoolean("Tim_survey_switch");
			Tim_survey_time = nbt.getDouble("Tim_survey_time");
			this.Dat_type_survey = nbt.get("Dat_type_survey") instanceof ListTag Dat_type_survey ? Dat_type_survey : new ListTag();
			this.Dat_time_survey = nbt.get("Dat_time_survey") instanceof ListTag Dat_time_survey ? Dat_time_survey : new ListTag();
			Exp_subject = nbt.getString("Exp_subject");
			Exp_path = nbt.getString("Exp_path");
			this.Dat_speed = nbt.get("Dat_speed") instanceof ListTag Dat_speed ? Dat_speed : new ListTag();
			Dat_trial_total = nbt.getDouble("Dat_trial_total");
			this.Dat_time_interval = nbt.get("Dat_time_interval") instanceof ListTag Dat_time_interval ? Dat_time_interval : new ListTag();
			Switch_blank = nbt.getBoolean("Switch_blank");
			this.Dat_answer_survey = nbt.get("Dat_answer_survey") instanceof ListTag Dat_answer_survey ? Dat_answer_survey : new ListTag();
			Exp_survey_idx = nbt.getDouble("Exp_survey_idx");
		}

		@Override
		public CompoundTag save(CompoundTag nbt) {
			nbt.putDouble("Option_tester", Option_tester);
			nbt.putString("Option_tester_str", Option_tester_str);
			nbt.putDouble("Pmt_difficulty", Pmt_difficulty);
			nbt.putDouble("Pos_player_x", Pos_player_x);
			nbt.putDouble("Pos_player_z", Pos_player_z);
			nbt.putDouble("Pmt_far", Pmt_far);
			nbt.putDouble("Timer_time", Timer_time);
			nbt.putString("Timer_event", Timer_event);
			nbt.putString("Timer_show", Timer_show);
			nbt.putDouble("Pmt_unittime", Pmt_unittime);
			nbt.put("Exp_prechasing_time", this.Exp_prechasing_time);
			nbt.put("Exp_prechasing_win", this.Exp_prechasing_win);
			nbt.put("Exp_prechasing_difficulty", this.Exp_prechasing_difficulty);
			nbt.putDouble("Exp_prechasing_trial", Exp_prechasing_trial);
			nbt.put("Exp_prechased_time", this.Exp_prechased_time);
			nbt.put("Exp_prechased_win", this.Exp_prechased_win);
			nbt.put("Exp_prechased_difficulty", this.Exp_prechased_difficulty);
			nbt.putDouble("Exp_prechased_trial", Exp_prechased_trial);
			nbt.put("Exp_pre_type", this.Exp_pre_type);
			nbt.put("List_obstacle", this.List_obstacle);
			{
				this.Pos_offset = this.Pos_offset == null ? Vec3.ZERO : this.Pos_offset;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Pos_offset.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Pos_offset.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Pos_offset.z()));
				nbt.put("Pos_offset", listTag);
			}
			nbt.putDouble("Radius_map", Radius_map);
			nbt.put("List_wall", this.List_wall);
			nbt.putBoolean("Exp_signal", Exp_signal);
			nbt.putDouble("Exp_phase", Exp_phase);
			nbt.putString("Log_error", Log_error);
			nbt.putString("Exp_session", Exp_session);
			nbt.putDouble("Exp_trial", Exp_trial);
			nbt.put("Dat_spawn_opponent", this.Dat_spawn_opponent);
			nbt.put("Dat_time_preparation", this.Dat_time_preparation);
			nbt.put("Dat_type_trial", this.Dat_type_trial);
			nbt.putString("UUID_opponent", UUID_opponent);
			nbt.putString("UUID_player", UUID_player);
			nbt.put("Pool_spawn", this.Pool_spawn);
			nbt.putBoolean("Switch_AI", Switch_AI);
			nbt.putBoolean("Switch_exp", Switch_exp);
			nbt.putBoolean("Tim_trial_switch", Tim_trial_switch);
			nbt.putDouble("Tim_trial_time", Tim_trial_time);
			nbt.put("Dat_time_gameplay", this.Dat_time_gameplay);
			nbt.put("Dat_win", this.Dat_win);
			nbt.putBoolean("Switch_debug", Switch_debug);
			nbt.putBoolean("Tim_survey_switch", Tim_survey_switch);
			nbt.putDouble("Tim_survey_time", Tim_survey_time);
			nbt.put("Dat_type_survey", this.Dat_type_survey);
			nbt.put("Dat_time_survey", this.Dat_time_survey);
			nbt.putString("Exp_subject", Exp_subject);
			nbt.putString("Exp_path", Exp_path);
			nbt.put("Dat_speed", this.Dat_speed);
			nbt.putDouble("Dat_trial_total", Dat_trial_total);
			nbt.put("Dat_time_interval", this.Dat_time_interval);
			nbt.putBoolean("Switch_blank", Switch_blank);
			nbt.put("Dat_answer_survey", this.Dat_answer_survey);
			nbt.putDouble("Exp_survey_idx", Exp_survey_idx);
			return nbt;
		}

		public void syncData(LevelAccessor world) {
			this.setDirty();
			if (world instanceof Level && !world.isClientSide())
				CacMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new SavedDataSyncMessage(0, this));
		}

		static MapVariables clientSide = new MapVariables();

		public static MapVariables get(LevelAccessor world) {
			if (world instanceof ServerLevelAccessor serverLevelAcc) {
				return serverLevelAcc.getLevel().getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(e -> MapVariables.load(e), MapVariables::new, DATA_NAME);
			} else {
				return clientSide;
			}
		}
	}

	public static class SavedDataSyncMessage {
		private final int type;
		private SavedData data;

		public SavedDataSyncMessage(FriendlyByteBuf buffer) {
			this.type = buffer.readInt();
			CompoundTag nbt = buffer.readNbt();
			if (nbt != null) {
				this.data = this.type == 0 ? new MapVariables() : new WorldVariables();
				if (this.data instanceof MapVariables mapVariables)
					mapVariables.read(nbt);
				else if (this.data instanceof WorldVariables worldVariables)
					worldVariables.read(nbt);
			}
		}

		public SavedDataSyncMessage(int type, SavedData data) {
			this.type = type;
			this.data = data;
		}

		public static void buffer(SavedDataSyncMessage message, FriendlyByteBuf buffer) {
			buffer.writeInt(message.type);
			if (message.data != null)
				buffer.writeNbt(message.data.save(new CompoundTag()));
		}

		public static void handler(SavedDataSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(() -> {
				if (!context.getDirection().getReceptionSide().isServer() && message.data != null) {
					if (message.type == 0)
						MapVariables.clientSide = (MapVariables) message.data;
					else
						WorldVariables.clientSide = (WorldVariables) message.data;
				}
			});
			context.setPacketHandled(true);
		}
	}
}
