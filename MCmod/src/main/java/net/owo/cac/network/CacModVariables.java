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
	public static com.google.gson.JsonArray Dat_pos_opponent_r = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_opponent_r_prep = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_opponent_x = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_opponent_x_prep = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_opponent_z = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_opponent_z_prep = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_player_r = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_player_r_prep = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_player_x = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_player_x_prep = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_player_z = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_player_z_prep = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_time = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_time_prep = new com.google.gson.JsonArray();
	public static double Dat_time_gameplay = 0;
	public static double Dat_time_interval = 0;
	public static double Dat_time_preparation = 0;
	public static double Dat_trial_spawnpoint_opponent = 0;
	public static double Dat_trial_type = 0;
	public static double Dat_trial_winlose = 0;
	public static String Dir_behaviors = "\"\"";
	public static String Dir_behaviors_session = "\"\"";
	public static String Dir_components = "\"\"";
	public static String Ev_content = "\"\"";
	public static boolean Ev_occuring = false;
	public static String Ev_pulse_content = "\"\"";
	public static com.google.gson.JsonArray Ev_que = new com.google.gson.JsonArray();
	public static double Ev_que_index = 0;
	public static boolean Ev_que_loop = false;
	public static double Exp_phase = 0;
	public static String Exp_session = "\"none\"";
	public static boolean Exp_signal = false;
	public static String Exp_subject = "\"\"";
	public static double Exp_trial = 0;
	public static double Exp_trial_total = 0;
	public static File Info_timestamp = new File("");
	public static String Log_error = "\"\"";
	public static File Log_event = new File("");
	public static File Log_gameplay = new File("");
	public static File Log_position = new File("");
	public static File Log_scanner = new File("");
	public static File Log_surrender = new File("");
	public static File Log_survey = new File("");
	public static Vec3 Meow_destination = Vec3.ZERO;
	public static double Meow_dx = 0;
	public static double Meow_dz = 0;
	public static boolean Msg_actionbar_switch = false;
	public static String Msg_actionbar_text = "\"\"";
	public static String Msg_subtitle_text = "\"\"";
	public static String Msg_title_text = "\"\"";
	public static boolean Msg_titles_switch = false;
	public static File Pool_event = new File("");
	public static File Pool_point = new File("");
	public static File Pool_psychometric = new File("");
	public static File Pool_que = new File("");
	public static File Pool_random = new File("");
	public static File Pool_task = new File("");
	public static Vec3 Pos_border_end = Vec3.ZERO;
	public static Vec3 Pos_border_start = Vec3.ZERO;
	public static Vec3 Pos_offset = Vec3.ZERO;
	public static Vec3 Pos_opponent = Vec3.ZERO;
	public static Vec3 Pos_opponent_destination = Vec3.ZERO;
	public static Vec3 Pos_player = Vec3.ZERO;
	public static Vec3 Pos_player_destination = Vec3.ZERO;
	public static boolean Switch_AI = false;
	public static boolean Switch_blank = false;
	public static boolean Switch_debug = false;
	public static boolean Switch_que = false;
	public static boolean Switch_scanner = false;
	public static boolean Switch_timer = false;
	public static boolean Switch_trace = false;
	public static double TimA_time = 0;
	public static double TimA_time_currtick = 0;
	public static double TimA_time_oldtick = 0;
	public static double TimR_que_time = 0;
	public static double TimR_time = 0;
	public static double TimS_time = 0;
	public static Vec3 Tuto_checkpoint_center = Vec3.ZERO;
	public static double Tuto_checkpoint_index = 0;
	public static ListTag Tuto_checkpoint_pos = new ListTag();
	public static ListTag Tuto_checkpoint_route = new ListTag();
	public static double Tuto_hurdle_stack = 0;
	public static double Tuto_hurdle_stack_old = 0;
	public static double Tuto_score = 0;
	public static boolean Tuto_score_running = false;
	public static boolean TimC_switch = false;
	public static double TimC_time = 0;
	public static String TimC_que = "\"\"";
	public static String Exp_property = "\"\"";
	public static File Log_fitting = new File("");
	public static double Dat_difficulty = 0;
	public static com.google.gson.JsonArray Psy_bin_param_max = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Psy_bin_param_min = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Psy_bin_param_prior = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Psy_bin_param_shape = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Psy_bin_param_step = new com.google.gson.JsonArray();
	public static String Psy_method = "\"\"";
	public static String Psy_function = "\"\"";
	public static String Psy_task = "\"\"";
	public static com.google.gson.JsonArray Psy_bin_param_best = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Psy_con_param_min = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Psy_con_param_max = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Psy_con_param_step = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Psy_con_param_shape = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Psy_con_param_prior = new com.google.gson.JsonArray();

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
		public Vec3 Builder_pos1 = Vec3.ZERO;
		public Vec3 Builder_pos2 = Vec3.ZERO;
		public ListTag List_obstacle = new ListTag();
		public ListTag List_random_preparation = new ListTag();
		public ListTag List_random_spawn = new ListTag();
		public ListTag List_spawnpoint_opponent = new ListTag();
		public ListTag List_wall = new ListTag();

		public static MapVariables load(CompoundTag tag) {
			MapVariables data = new MapVariables();
			data.read(tag);
			return data;
		}

		public void read(CompoundTag nbt) {
			{
				ListTag listTag = nbt.getList("Builder_pos1", 6);
				this.Builder_pos1 = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			{
				ListTag listTag = nbt.getList("Builder_pos2", 6);
				this.Builder_pos2 = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			this.List_obstacle = nbt.get("List_obstacle") instanceof ListTag List_obstacle ? List_obstacle : new ListTag();
			this.List_random_preparation = nbt.get("List_random_preparation") instanceof ListTag List_random_preparation ? List_random_preparation : new ListTag();
			this.List_random_spawn = nbt.get("List_random_spawn") instanceof ListTag List_random_spawn ? List_random_spawn : new ListTag();
			this.List_spawnpoint_opponent = nbt.get("List_spawnpoint_opponent") instanceof ListTag List_spawnpoint_opponent ? List_spawnpoint_opponent : new ListTag();
			this.List_wall = nbt.get("List_wall") instanceof ListTag List_wall ? List_wall : new ListTag();
		}

		@Override
		public CompoundTag save(CompoundTag nbt) {
			{
				this.Builder_pos1 = this.Builder_pos1 == null ? Vec3.ZERO : this.Builder_pos1;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Builder_pos1.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Builder_pos1.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Builder_pos1.z()));
				nbt.put("Builder_pos1", listTag);
			}
			{
				this.Builder_pos2 = this.Builder_pos2 == null ? Vec3.ZERO : this.Builder_pos2;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Builder_pos2.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Builder_pos2.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Builder_pos2.z()));
				nbt.put("Builder_pos2", listTag);
			}
			nbt.put("List_obstacle", this.List_obstacle);
			nbt.put("List_random_preparation", this.List_random_preparation);
			nbt.put("List_random_spawn", this.List_random_spawn);
			nbt.put("List_spawnpoint_opponent", this.List_spawnpoint_opponent);
			nbt.put("List_wall", this.List_wall);
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
