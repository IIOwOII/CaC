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

import java.io.File;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CacModVariables {
	public static File Log_event = new File("");
	public static File Log_position = new File("");
	public static File Pool_event = new File("");
	public static File Pool_task = new File("");
	public static File Pool_point = new File("");
	public static File Pool_que = new File("");
	public static com.google.gson.JsonArray Ev_que = new com.google.gson.JsonArray();
	public static boolean Ev_que_loop = false;
	public static double Ev_que_index = 0;
	public static com.google.gson.JsonArray Dat_pos_player_x = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_player_z = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_player_r = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_opponent_x = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_opponent_z = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_opponent_r = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_time = new com.google.gson.JsonArray();
	public static File Log_gameplay = new File("");
	public static com.google.gson.JsonArray Dat_pos_time_prep = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_player_x_prep = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_player_z_prep = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_player_r_prep = new com.google.gson.JsonArray();
	public static File Info_timestamp = new File("");
	public static File Info_PF = new File("");
	public static com.google.gson.JsonArray Dat_pos_opponent_x_prep = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_opponent_z_prep = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_opponent_r_prep = new com.google.gson.JsonArray();

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
		public ListTag List_obstacle = new ListTag();
		public Vec3 Pos_offset = Vec3.ZERO;
		public double Radius_map = 16.0;
		public ListTag List_wall = new ListTag();
		public boolean Exp_signal = false;
		public double Exp_phase = 0;
		public String Log_error = "\"\"";
		public String Exp_session = "\"none\"";
		public double Exp_trial = 0;
		public boolean Switch_AI = false;
		public boolean Switch_debug = false;
		public String Exp_subject = "\"\"";
		public boolean Switch_blank = false;
		public double Exp_survey_idx = 0;
		public double TimR_time = 0;
		public double TimA_time = 0;
		public double TimA_time_currtick = 0;
		public double TimA_time_oldtick = 0;
		public ListTag Dat_survey_order = new ListTag();
		public ListTag Dat_survey_RT = new ListTag();
		public ListTag Dat_survey_answer = new ListTag();
		public double Exp_trial_total = 0;
		public String Ev_pulse_content = "\"\"";
		public double Dat_trial_type = 0;
		public double Dat_difficulty_absolute = 0;
		public double Dat_difficulty_relative = 0;
		public double Dat_trial_spawnpoint_opponent = 0;
		public double Dat_trial_winlose = 0;
		public double Dat_time_preparation = 0;
		public double Dat_time_gameplay = 0;
		public Vec3 Pos_player = Vec3.ZERO;
		public Vec3 Pos_opponent = Vec3.ZERO;
		public boolean TimD_switch = false;
		public double TimD_time = 0;
		public double Time_AI = 0;
		public boolean Switch_que = false;
		public double TimR_que_time = 0;
		public String Dir_components = "\"\"";
		public String Dir_behaviors = "\"\"";
		public ListTag List_spawnpoint_opponent = new ListTag();
		public boolean Switch_timer = false;
		public String Ev_content = "\"\"";
		public boolean Ev_occuring = false;
		public double Dat_time_interval = 0;
		public boolean Switch_trace = false;
		public Vec3 Pos_border_start = Vec3.ZERO;
		public Vec3 Pos_border_end = Vec3.ZERO;
		public ListTag PF_likelihood = new ListTag();
		public ListTag PF_parameter = new ListTag();
		public ListTag PF_parameter_name = new ListTag();
		public double Meow_dx = 0;
		public double Meow_dz = 0;
		public Vec3 Pos_player_destination = Vec3.ZERO;
		public Vec3 Pos_opponent_destination = Vec3.ZERO;
		public Vec3 Meow_destination = Vec3.ZERO;

		public static MapVariables load(CompoundTag tag) {
			MapVariables data = new MapVariables();
			data.read(tag);
			return data;
		}

		public void read(CompoundTag nbt) {
			Option_tester = nbt.getDouble("Option_tester");
			Option_tester_str = nbt.getString("Option_tester_str");
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
			Switch_AI = nbt.getBoolean("Switch_AI");
			Switch_debug = nbt.getBoolean("Switch_debug");
			Exp_subject = nbt.getString("Exp_subject");
			Switch_blank = nbt.getBoolean("Switch_blank");
			Exp_survey_idx = nbt.getDouble("Exp_survey_idx");
			TimR_time = nbt.getDouble("TimR_time");
			TimA_time = nbt.getDouble("TimA_time");
			TimA_time_currtick = nbt.getDouble("TimA_time_currtick");
			TimA_time_oldtick = nbt.getDouble("TimA_time_oldtick");
			this.Dat_survey_order = nbt.get("Dat_survey_order") instanceof ListTag Dat_survey_order ? Dat_survey_order : new ListTag();
			this.Dat_survey_RT = nbt.get("Dat_survey_RT") instanceof ListTag Dat_survey_RT ? Dat_survey_RT : new ListTag();
			this.Dat_survey_answer = nbt.get("Dat_survey_answer") instanceof ListTag Dat_survey_answer ? Dat_survey_answer : new ListTag();
			Exp_trial_total = nbt.getDouble("Exp_trial_total");
			Ev_pulse_content = nbt.getString("Ev_pulse_content");
			Dat_trial_type = nbt.getDouble("Dat_trial_type");
			Dat_difficulty_absolute = nbt.getDouble("Dat_difficulty_absolute");
			Dat_difficulty_relative = nbt.getDouble("Dat_difficulty_relative");
			Dat_trial_spawnpoint_opponent = nbt.getDouble("Dat_trial_spawnpoint_opponent");
			Dat_trial_winlose = nbt.getDouble("Dat_trial_winlose");
			Dat_time_preparation = nbt.getDouble("Dat_time_preparation");
			Dat_time_gameplay = nbt.getDouble("Dat_time_gameplay");
			{
				ListTag listTag = nbt.getList("Pos_player", 6);
				this.Pos_player = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			{
				ListTag listTag = nbt.getList("Pos_opponent", 6);
				this.Pos_opponent = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			TimD_switch = nbt.getBoolean("TimD_switch");
			TimD_time = nbt.getDouble("TimD_time");
			Time_AI = nbt.getDouble("Time_AI");
			Switch_que = nbt.getBoolean("Switch_que");
			TimR_que_time = nbt.getDouble("TimR_que_time");
			Dir_components = nbt.getString("Dir_components");
			Dir_behaviors = nbt.getString("Dir_behaviors");
			this.List_spawnpoint_opponent = nbt.get("List_spawnpoint_opponent") instanceof ListTag List_spawnpoint_opponent ? List_spawnpoint_opponent : new ListTag();
			Switch_timer = nbt.getBoolean("Switch_timer");
			Ev_content = nbt.getString("Ev_content");
			Ev_occuring = nbt.getBoolean("Ev_occuring");
			Dat_time_interval = nbt.getDouble("Dat_time_interval");
			Switch_trace = nbt.getBoolean("Switch_trace");
			{
				ListTag listTag = nbt.getList("Pos_border_start", 6);
				this.Pos_border_start = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			{
				ListTag listTag = nbt.getList("Pos_border_end", 6);
				this.Pos_border_end = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			this.PF_likelihood = nbt.get("PF_likelihood") instanceof ListTag PF_likelihood ? PF_likelihood : new ListTag();
			this.PF_parameter = nbt.get("PF_parameter") instanceof ListTag PF_parameter ? PF_parameter : new ListTag();
			this.PF_parameter_name = nbt.get("PF_parameter_name") instanceof ListTag PF_parameter_name ? PF_parameter_name : new ListTag();
			Meow_dx = nbt.getDouble("Meow_dx");
			Meow_dz = nbt.getDouble("Meow_dz");
			{
				ListTag listTag = nbt.getList("Pos_player_destination", 6);
				this.Pos_player_destination = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			{
				ListTag listTag = nbt.getList("Pos_opponent_destination", 6);
				this.Pos_opponent_destination = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			{
				ListTag listTag = nbt.getList("Meow_destination", 6);
				this.Meow_destination = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
		}

		@Override
		public CompoundTag save(CompoundTag nbt) {
			nbt.putDouble("Option_tester", Option_tester);
			nbt.putString("Option_tester_str", Option_tester_str);
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
			nbt.putBoolean("Switch_AI", Switch_AI);
			nbt.putBoolean("Switch_debug", Switch_debug);
			nbt.putString("Exp_subject", Exp_subject);
			nbt.putBoolean("Switch_blank", Switch_blank);
			nbt.putDouble("Exp_survey_idx", Exp_survey_idx);
			nbt.putDouble("TimR_time", TimR_time);
			nbt.putDouble("TimA_time", TimA_time);
			nbt.putDouble("TimA_time_currtick", TimA_time_currtick);
			nbt.putDouble("TimA_time_oldtick", TimA_time_oldtick);
			nbt.put("Dat_survey_order", this.Dat_survey_order);
			nbt.put("Dat_survey_RT", this.Dat_survey_RT);
			nbt.put("Dat_survey_answer", this.Dat_survey_answer);
			nbt.putDouble("Exp_trial_total", Exp_trial_total);
			nbt.putString("Ev_pulse_content", Ev_pulse_content);
			nbt.putDouble("Dat_trial_type", Dat_trial_type);
			nbt.putDouble("Dat_difficulty_absolute", Dat_difficulty_absolute);
			nbt.putDouble("Dat_difficulty_relative", Dat_difficulty_relative);
			nbt.putDouble("Dat_trial_spawnpoint_opponent", Dat_trial_spawnpoint_opponent);
			nbt.putDouble("Dat_trial_winlose", Dat_trial_winlose);
			nbt.putDouble("Dat_time_preparation", Dat_time_preparation);
			nbt.putDouble("Dat_time_gameplay", Dat_time_gameplay);
			{
				this.Pos_player = this.Pos_player == null ? Vec3.ZERO : this.Pos_player;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Pos_player.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Pos_player.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Pos_player.z()));
				nbt.put("Pos_player", listTag);
			}
			{
				this.Pos_opponent = this.Pos_opponent == null ? Vec3.ZERO : this.Pos_opponent;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Pos_opponent.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Pos_opponent.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Pos_opponent.z()));
				nbt.put("Pos_opponent", listTag);
			}
			nbt.putBoolean("TimD_switch", TimD_switch);
			nbt.putDouble("TimD_time", TimD_time);
			nbt.putDouble("Time_AI", Time_AI);
			nbt.putBoolean("Switch_que", Switch_que);
			nbt.putDouble("TimR_que_time", TimR_que_time);
			nbt.putString("Dir_components", Dir_components);
			nbt.putString("Dir_behaviors", Dir_behaviors);
			nbt.put("List_spawnpoint_opponent", this.List_spawnpoint_opponent);
			nbt.putBoolean("Switch_timer", Switch_timer);
			nbt.putString("Ev_content", Ev_content);
			nbt.putBoolean("Ev_occuring", Ev_occuring);
			nbt.putDouble("Dat_time_interval", Dat_time_interval);
			nbt.putBoolean("Switch_trace", Switch_trace);
			{
				this.Pos_border_start = this.Pos_border_start == null ? Vec3.ZERO : this.Pos_border_start;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Pos_border_start.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Pos_border_start.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Pos_border_start.z()));
				nbt.put("Pos_border_start", listTag);
			}
			{
				this.Pos_border_end = this.Pos_border_end == null ? Vec3.ZERO : this.Pos_border_end;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Pos_border_end.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Pos_border_end.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Pos_border_end.z()));
				nbt.put("Pos_border_end", listTag);
			}
			nbt.put("PF_likelihood", this.PF_likelihood);
			nbt.put("PF_parameter", this.PF_parameter);
			nbt.put("PF_parameter_name", this.PF_parameter_name);
			nbt.putDouble("Meow_dx", Meow_dx);
			nbt.putDouble("Meow_dz", Meow_dz);
			{
				this.Pos_player_destination = this.Pos_player_destination == null ? Vec3.ZERO : this.Pos_player_destination;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Pos_player_destination.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Pos_player_destination.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Pos_player_destination.z()));
				nbt.put("Pos_player_destination", listTag);
			}
			{
				this.Pos_opponent_destination = this.Pos_opponent_destination == null ? Vec3.ZERO : this.Pos_opponent_destination;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Pos_opponent_destination.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Pos_opponent_destination.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Pos_opponent_destination.z()));
				nbt.put("Pos_opponent_destination", listTag);
			}
			{
				this.Meow_destination = this.Meow_destination == null ? Vec3.ZERO : this.Meow_destination;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Meow_destination.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Meow_destination.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Meow_destination.z()));
				nbt.put("Meow_destination", listTag);
			}
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
