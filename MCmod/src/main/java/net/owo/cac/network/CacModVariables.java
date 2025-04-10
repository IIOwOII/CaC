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
	public static com.google.gson.JsonArray Dat_trial_pos_player = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_trial_pos_opponent = new com.google.gson.JsonArray();
	public static File Log_timestamp = new File("");
	public static File Log_event = new File("");
	public static File Log_position = new File("");
	public static File Log_trialresult = new File("");
	public static File Pool_event = new File("");

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
		public double Timer_time = 0.0;
		public String Timer_event = "\"none\"";
		public String Timer_show = "\"none\"";
		public double Pmt_unittime = 30.0;
		public ListTag List_obstacle = new ListTag();
		public Vec3 Pos_offset = Vec3.ZERO;
		public double Radius_map = 16.0;
		public ListTag List_wall = new ListTag();
		public boolean Exp_signal = false;
		public double Exp_phase = 0;
		public String Log_error = "\"\"";
		public String Exp_session = "\"none\"";
		public double Exp_trial = 0;
		public ListTag Pool_spawn = new ListTag();
		public boolean Switch_AI = false;
		public boolean Tim_trial_switch = false;
		public double Tim_trial_time = 0;
		public ListTag Dat_win = new ListTag();
		public boolean Switch_debug = false;
		public boolean Tim_survey_switch = false;
		public double Tim_survey_time = 0;
		public ListTag Dat_type_survey = new ListTag();
		public String Exp_subject = "\"\"";
		public String Exp_path = "\"\"";
		public double Dat_trial_total = 0;
		public ListTag Dat_time_interval = new ListTag();
		public boolean Switch_blank = false;
		public double Exp_survey_idx = 0;
		public boolean TimR_switch = false;
		public double TimR_time = 0;
		public boolean TimA_switch = false;
		public double TimA_time = 0;
		public double TimA_time_currtick = 0;
		public double TimA_time_oldtick = 0;
		public ListTag Dat_winlose = new ListTag();
		public ListTag Dat_survey_order = new ListTag();
		public ListTag Dat_survey_RT = new ListTag();
		public ListTag Dat_survey_answer = new ListTag();
		public double Exp_difficulty_absolute = 0;
		public double Exp_difficulty_relative = 0;
		public double Exp_trial_total = 0;
		public boolean TimR_que_switch = false;
		public double Ev_que_waittime = 0;
		public String Ev_que_content = "\"\"";
		public String Ev_pulse_content = "\"\"";
		public double Dat_trial_type = 0;
		public double Dat_difficulty_absolute = 0;
		public double Dat_difficulty_relative = 0;
		public double Dat_trial_spawnpoint_opponent = 0;
		public double Dat_trial_winlose = 0;
		public double Dat_time_preparation = 0;
		public double Dat_time_gameplay = 0;
		public double Dat_time_survey = 0;
		public double TimR_que_start = 0;
		public double TimR_que_end = 0;
		public Vec3 Pos_player = Vec3.ZERO;
		public Vec3 Pos_opponent = Vec3.ZERO;
		public boolean TimD_switch = false;
		public double TimD_time = 0;
		public double Time_AI = 0;
		public boolean Switch_que = false;
		public double TimR_que_time = 0;
		public String Ev_content_curr = "\"\"";
		public String Ev_content_next = "\"\"";

		public static MapVariables load(CompoundTag tag) {
			MapVariables data = new MapVariables();
			data.read(tag);
			return data;
		}

		public void read(CompoundTag nbt) {
			Option_tester = nbt.getDouble("Option_tester");
			Option_tester_str = nbt.getString("Option_tester_str");
			Pmt_difficulty = nbt.getDouble("Pmt_difficulty");
			Timer_time = nbt.getDouble("Timer_time");
			Timer_event = nbt.getString("Timer_event");
			Timer_show = nbt.getString("Timer_show");
			Pmt_unittime = nbt.getDouble("Pmt_unittime");
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
			this.Pool_spawn = nbt.get("Pool_spawn") instanceof ListTag Pool_spawn ? Pool_spawn : new ListTag();
			Switch_AI = nbt.getBoolean("Switch_AI");
			Tim_trial_switch = nbt.getBoolean("Tim_trial_switch");
			Tim_trial_time = nbt.getDouble("Tim_trial_time");
			this.Dat_win = nbt.get("Dat_win") instanceof ListTag Dat_win ? Dat_win : new ListTag();
			Switch_debug = nbt.getBoolean("Switch_debug");
			Tim_survey_switch = nbt.getBoolean("Tim_survey_switch");
			Tim_survey_time = nbt.getDouble("Tim_survey_time");
			this.Dat_type_survey = nbt.get("Dat_type_survey") instanceof ListTag Dat_type_survey ? Dat_type_survey : new ListTag();
			Exp_subject = nbt.getString("Exp_subject");
			Exp_path = nbt.getString("Exp_path");
			Dat_trial_total = nbt.getDouble("Dat_trial_total");
			this.Dat_time_interval = nbt.get("Dat_time_interval") instanceof ListTag Dat_time_interval ? Dat_time_interval : new ListTag();
			Switch_blank = nbt.getBoolean("Switch_blank");
			Exp_survey_idx = nbt.getDouble("Exp_survey_idx");
			TimR_switch = nbt.getBoolean("TimR_switch");
			TimR_time = nbt.getDouble("TimR_time");
			TimA_switch = nbt.getBoolean("TimA_switch");
			TimA_time = nbt.getDouble("TimA_time");
			TimA_time_currtick = nbt.getDouble("TimA_time_currtick");
			TimA_time_oldtick = nbt.getDouble("TimA_time_oldtick");
			this.Dat_winlose = nbt.get("Dat_winlose") instanceof ListTag Dat_winlose ? Dat_winlose : new ListTag();
			this.Dat_survey_order = nbt.get("Dat_survey_order") instanceof ListTag Dat_survey_order ? Dat_survey_order : new ListTag();
			this.Dat_survey_RT = nbt.get("Dat_survey_RT") instanceof ListTag Dat_survey_RT ? Dat_survey_RT : new ListTag();
			this.Dat_survey_answer = nbt.get("Dat_survey_answer") instanceof ListTag Dat_survey_answer ? Dat_survey_answer : new ListTag();
			Exp_difficulty_absolute = nbt.getDouble("Exp_difficulty_absolute");
			Exp_difficulty_relative = nbt.getDouble("Exp_difficulty_relative");
			Exp_trial_total = nbt.getDouble("Exp_trial_total");
			TimR_que_switch = nbt.getBoolean("TimR_que_switch");
			Ev_que_waittime = nbt.getDouble("Ev_que_waittime");
			Ev_que_content = nbt.getString("Ev_que_content");
			Ev_pulse_content = nbt.getString("Ev_pulse_content");
			Dat_trial_type = nbt.getDouble("Dat_trial_type");
			Dat_difficulty_absolute = nbt.getDouble("Dat_difficulty_absolute");
			Dat_difficulty_relative = nbt.getDouble("Dat_difficulty_relative");
			Dat_trial_spawnpoint_opponent = nbt.getDouble("Dat_trial_spawnpoint_opponent");
			Dat_trial_winlose = nbt.getDouble("Dat_trial_winlose");
			Dat_time_preparation = nbt.getDouble("Dat_time_preparation");
			Dat_time_gameplay = nbt.getDouble("Dat_time_gameplay");
			Dat_time_survey = nbt.getDouble("Dat_time_survey");
			TimR_que_start = nbt.getDouble("TimR_que_start");
			TimR_que_end = nbt.getDouble("TimR_que_end");
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
			Ev_content_curr = nbt.getString("Ev_content_curr");
			Ev_content_next = nbt.getString("Ev_content_next");
		}

		@Override
		public CompoundTag save(CompoundTag nbt) {
			nbt.putDouble("Option_tester", Option_tester);
			nbt.putString("Option_tester_str", Option_tester_str);
			nbt.putDouble("Pmt_difficulty", Pmt_difficulty);
			nbt.putDouble("Timer_time", Timer_time);
			nbt.putString("Timer_event", Timer_event);
			nbt.putString("Timer_show", Timer_show);
			nbt.putDouble("Pmt_unittime", Pmt_unittime);
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
			nbt.put("Pool_spawn", this.Pool_spawn);
			nbt.putBoolean("Switch_AI", Switch_AI);
			nbt.putBoolean("Tim_trial_switch", Tim_trial_switch);
			nbt.putDouble("Tim_trial_time", Tim_trial_time);
			nbt.put("Dat_win", this.Dat_win);
			nbt.putBoolean("Switch_debug", Switch_debug);
			nbt.putBoolean("Tim_survey_switch", Tim_survey_switch);
			nbt.putDouble("Tim_survey_time", Tim_survey_time);
			nbt.put("Dat_type_survey", this.Dat_type_survey);
			nbt.putString("Exp_subject", Exp_subject);
			nbt.putString("Exp_path", Exp_path);
			nbt.putDouble("Dat_trial_total", Dat_trial_total);
			nbt.put("Dat_time_interval", this.Dat_time_interval);
			nbt.putBoolean("Switch_blank", Switch_blank);
			nbt.putDouble("Exp_survey_idx", Exp_survey_idx);
			nbt.putBoolean("TimR_switch", TimR_switch);
			nbt.putDouble("TimR_time", TimR_time);
			nbt.putBoolean("TimA_switch", TimA_switch);
			nbt.putDouble("TimA_time", TimA_time);
			nbt.putDouble("TimA_time_currtick", TimA_time_currtick);
			nbt.putDouble("TimA_time_oldtick", TimA_time_oldtick);
			nbt.put("Dat_winlose", this.Dat_winlose);
			nbt.put("Dat_survey_order", this.Dat_survey_order);
			nbt.put("Dat_survey_RT", this.Dat_survey_RT);
			nbt.put("Dat_survey_answer", this.Dat_survey_answer);
			nbt.putDouble("Exp_difficulty_absolute", Exp_difficulty_absolute);
			nbt.putDouble("Exp_difficulty_relative", Exp_difficulty_relative);
			nbt.putDouble("Exp_trial_total", Exp_trial_total);
			nbt.putBoolean("TimR_que_switch", TimR_que_switch);
			nbt.putDouble("Ev_que_waittime", Ev_que_waittime);
			nbt.putString("Ev_que_content", Ev_que_content);
			nbt.putString("Ev_pulse_content", Ev_pulse_content);
			nbt.putDouble("Dat_trial_type", Dat_trial_type);
			nbt.putDouble("Dat_difficulty_absolute", Dat_difficulty_absolute);
			nbt.putDouble("Dat_difficulty_relative", Dat_difficulty_relative);
			nbt.putDouble("Dat_trial_spawnpoint_opponent", Dat_trial_spawnpoint_opponent);
			nbt.putDouble("Dat_trial_winlose", Dat_trial_winlose);
			nbt.putDouble("Dat_time_preparation", Dat_time_preparation);
			nbt.putDouble("Dat_time_gameplay", Dat_time_gameplay);
			nbt.putDouble("Dat_time_survey", Dat_time_survey);
			nbt.putDouble("TimR_que_start", TimR_que_start);
			nbt.putDouble("TimR_que_end", TimR_que_end);
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
			nbt.putString("Ev_content_curr", Ev_content_curr);
			nbt.putString("Ev_content_next", Ev_content_next);
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
