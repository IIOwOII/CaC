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
	public static double Dat_difficulty_absolute = 0;
	public static double Dat_difficulty_relative = 0;
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
	public static ListTag Dat_psy_param = new ListTag();
	public static double Dat_time_gameplay = 0;
	public static double Dat_time_interval = 0;
	public static double Dat_time_preparation = 0;
	public static double Dat_trial_spawnpoint_opponent = 0;
	public static double Dat_trial_type = 0;
	public static double Dat_trial_winlose = 0;
	public static String Dir_behaviors = "\"\"";
	public static String Dir_behaviors_session = "\"\"";
	public static String Dir_components = "\"\"";
	public static com.google.gson.JsonArray Ev_que = new com.google.gson.JsonArray();
	public static double Ev_que_index = 0;
	public static boolean Ev_que_loop = false;
	public static double Exp_phase = 0;
	public static String Exp_session = "\"none\"";
	public static double Exp_session_reps = 0;
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
	public static String Log_type = "\"\"";
	public static File Pool_event = new File("");
	public static File Pool_point = new File("");
	public static File Pool_psychometric = new File("");
	public static File Pool_que = new File("");
	public static File Pool_random = new File("");
	public static File Pool_task = new File("");
	public static File Pool_tutorial = new File("");
	public static com.google.gson.JsonArray Tuto_que = new com.google.gson.JsonArray();

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
		public ListTag Dat_survey_answer = new ListTag();
		public ListTag Dat_survey_order = new ListTag();
		public double Dat_survey_surrender = 0;
		public double Dat_survey_surrender_type = 0;
		public ListTag Dat_survey_time = new ListTag();
		public String Ev_content = "\"\"";
		public boolean Ev_occuring = false;
		public String Ev_pulse_content = "\"\"";
		public ListTag List_obstacle = new ListTag();
		public ListTag List_random_preparation = new ListTag();
		public ListTag List_random_spawn = new ListTag();
		public ListTag List_spawnpoint_opponent = new ListTag();
		public ListTag List_survey_initial = new ListTag();
		public ListTag List_survey_label = new ListTag();
		public ListTag List_survey_name = new ListTag();
		public ListTag List_survey_range = new ListTag();
		public ListTag List_survey_type = new ListTag();
		public ListTag List_wall = new ListTag();
		public Vec3 Meow_destination = Vec3.ZERO;
		public double Meow_dx = 0;
		public double Meow_dz = 0;
		public boolean Msg_actionbar_switch = false;
		public String Msg_actionbar_text = "\"\"";
		public String Msg_subtitle_text = "\"\"";
		public String Msg_title_text = "\"\"";
		public boolean Msg_titles_switch = false;
		public Vec3 Pos_border_end = Vec3.ZERO;
		public Vec3 Pos_border_start = Vec3.ZERO;
		public Vec3 Pos_offset = Vec3.ZERO;
		public Vec3 Pos_opponent = Vec3.ZERO;
		public Vec3 Pos_opponent_destination = Vec3.ZERO;
		public Vec3 Pos_player = Vec3.ZERO;
		public Vec3 Pos_player_destination = Vec3.ZERO;
		public ListTag Psy_likelihood = new ListTag();
		public ListTag Psy_param_gamma = new ListTag();
		public ListTag Psy_param_lambda = new ListTag();
		public ListTag Psy_param_m = new ListTag();
		public ListTag Psy_param_w = new ListTag();
		public boolean Switch_AI = false;
		public boolean Switch_blank = false;
		public boolean Switch_debug = false;
		public boolean Switch_que = false;
		public boolean Switch_scanner = false;
		public boolean Switch_surrender = false;
		public boolean Switch_timer = false;
		public boolean Switch_trace = false;
		public double TimA_time = 0;
		public double TimA_time_currtick = 0;
		public double TimA_time_oldtick = 0;
		public boolean TimD_switch = false;
		public double TimD_time = 0;
		public double Time_AI = 0;
		public double TimR_que_time = 0;
		public double TimR_time = 0;
		public double TimS_time = 0;
		public Vec3 Tuto_checkpoint_center = Vec3.ZERO;
		public double Tuto_checkpoint_index = 0;
		public ListTag Tuto_checkpoint_pos = new ListTag();
		public ListTag Tuto_checkpoint_route = new ListTag();
		public double Tuto_hurdle_stack = 0;
		public double Tuto_hurdle_stack_old = 0;
		public double Tuto_score = 0;
		public boolean Tuto_score_running = false;

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
			this.Dat_survey_answer = nbt.get("Dat_survey_answer") instanceof ListTag Dat_survey_answer ? Dat_survey_answer : new ListTag();
			this.Dat_survey_order = nbt.get("Dat_survey_order") instanceof ListTag Dat_survey_order ? Dat_survey_order : new ListTag();
			Dat_survey_surrender = nbt.getDouble("Dat_survey_surrender");
			Dat_survey_surrender_type = nbt.getDouble("Dat_survey_surrender_type");
			this.Dat_survey_time = nbt.get("Dat_survey_time") instanceof ListTag Dat_survey_time ? Dat_survey_time : new ListTag();
			Ev_content = nbt.getString("Ev_content");
			Ev_occuring = nbt.getBoolean("Ev_occuring");
			Ev_pulse_content = nbt.getString("Ev_pulse_content");
			this.List_obstacle = nbt.get("List_obstacle") instanceof ListTag List_obstacle ? List_obstacle : new ListTag();
			this.List_random_preparation = nbt.get("List_random_preparation") instanceof ListTag List_random_preparation ? List_random_preparation : new ListTag();
			this.List_random_spawn = nbt.get("List_random_spawn") instanceof ListTag List_random_spawn ? List_random_spawn : new ListTag();
			this.List_spawnpoint_opponent = nbt.get("List_spawnpoint_opponent") instanceof ListTag List_spawnpoint_opponent ? List_spawnpoint_opponent : new ListTag();
			this.List_survey_initial = nbt.get("List_survey_initial") instanceof ListTag List_survey_initial ? List_survey_initial : new ListTag();
			this.List_survey_label = nbt.get("List_survey_label") instanceof ListTag List_survey_label ? List_survey_label : new ListTag();
			this.List_survey_name = nbt.get("List_survey_name") instanceof ListTag List_survey_name ? List_survey_name : new ListTag();
			this.List_survey_range = nbt.get("List_survey_range") instanceof ListTag List_survey_range ? List_survey_range : new ListTag();
			this.List_survey_type = nbt.get("List_survey_type") instanceof ListTag List_survey_type ? List_survey_type : new ListTag();
			this.List_wall = nbt.get("List_wall") instanceof ListTag List_wall ? List_wall : new ListTag();
			{
				ListTag listTag = nbt.getList("Meow_destination", 6);
				this.Meow_destination = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			Meow_dx = nbt.getDouble("Meow_dx");
			Meow_dz = nbt.getDouble("Meow_dz");
			Msg_actionbar_switch = nbt.getBoolean("Msg_actionbar_switch");
			Msg_actionbar_text = nbt.getString("Msg_actionbar_text");
			Msg_subtitle_text = nbt.getString("Msg_subtitle_text");
			Msg_title_text = nbt.getString("Msg_title_text");
			Msg_titles_switch = nbt.getBoolean("Msg_titles_switch");
			{
				ListTag listTag = nbt.getList("Pos_border_end", 6);
				this.Pos_border_end = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			{
				ListTag listTag = nbt.getList("Pos_border_start", 6);
				this.Pos_border_start = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			{
				ListTag listTag = nbt.getList("Pos_offset", 6);
				this.Pos_offset = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			{
				ListTag listTag = nbt.getList("Pos_opponent", 6);
				this.Pos_opponent = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			{
				ListTag listTag = nbt.getList("Pos_opponent_destination", 6);
				this.Pos_opponent_destination = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			{
				ListTag listTag = nbt.getList("Pos_player", 6);
				this.Pos_player = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			{
				ListTag listTag = nbt.getList("Pos_player_destination", 6);
				this.Pos_player_destination = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			this.Psy_likelihood = nbt.get("Psy_likelihood") instanceof ListTag Psy_likelihood ? Psy_likelihood : new ListTag();
			this.Psy_param_gamma = nbt.get("Psy_param_gamma") instanceof ListTag Psy_param_gamma ? Psy_param_gamma : new ListTag();
			this.Psy_param_lambda = nbt.get("Psy_param_lambda") instanceof ListTag Psy_param_lambda ? Psy_param_lambda : new ListTag();
			this.Psy_param_m = nbt.get("Psy_param_m") instanceof ListTag Psy_param_m ? Psy_param_m : new ListTag();
			this.Psy_param_w = nbt.get("Psy_param_w") instanceof ListTag Psy_param_w ? Psy_param_w : new ListTag();
			Switch_AI = nbt.getBoolean("Switch_AI");
			Switch_blank = nbt.getBoolean("Switch_blank");
			Switch_debug = nbt.getBoolean("Switch_debug");
			Switch_que = nbt.getBoolean("Switch_que");
			Switch_scanner = nbt.getBoolean("Switch_scanner");
			Switch_surrender = nbt.getBoolean("Switch_surrender");
			Switch_timer = nbt.getBoolean("Switch_timer");
			Switch_trace = nbt.getBoolean("Switch_trace");
			TimA_time = nbt.getDouble("TimA_time");
			TimA_time_currtick = nbt.getDouble("TimA_time_currtick");
			TimA_time_oldtick = nbt.getDouble("TimA_time_oldtick");
			TimD_switch = nbt.getBoolean("TimD_switch");
			TimD_time = nbt.getDouble("TimD_time");
			Time_AI = nbt.getDouble("Time_AI");
			TimR_que_time = nbt.getDouble("TimR_que_time");
			TimR_time = nbt.getDouble("TimR_time");
			TimS_time = nbt.getDouble("TimS_time");
			{
				ListTag listTag = nbt.getList("Tuto_checkpoint_center", 6);
				this.Tuto_checkpoint_center = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			Tuto_checkpoint_index = nbt.getDouble("Tuto_checkpoint_index");
			this.Tuto_checkpoint_pos = nbt.get("Tuto_checkpoint_pos") instanceof ListTag Tuto_checkpoint_pos ? Tuto_checkpoint_pos : new ListTag();
			this.Tuto_checkpoint_route = nbt.get("Tuto_checkpoint_route") instanceof ListTag Tuto_checkpoint_route ? Tuto_checkpoint_route : new ListTag();
			Tuto_hurdle_stack = nbt.getDouble("Tuto_hurdle_stack");
			Tuto_hurdle_stack_old = nbt.getDouble("Tuto_hurdle_stack_old");
			Tuto_score = nbt.getDouble("Tuto_score");
			Tuto_score_running = nbt.getBoolean("Tuto_score_running");
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
			nbt.put("Dat_survey_answer", this.Dat_survey_answer);
			nbt.put("Dat_survey_order", this.Dat_survey_order);
			nbt.putDouble("Dat_survey_surrender", Dat_survey_surrender);
			nbt.putDouble("Dat_survey_surrender_type", Dat_survey_surrender_type);
			nbt.put("Dat_survey_time", this.Dat_survey_time);
			nbt.putString("Ev_content", Ev_content);
			nbt.putBoolean("Ev_occuring", Ev_occuring);
			nbt.putString("Ev_pulse_content", Ev_pulse_content);
			nbt.put("List_obstacle", this.List_obstacle);
			nbt.put("List_random_preparation", this.List_random_preparation);
			nbt.put("List_random_spawn", this.List_random_spawn);
			nbt.put("List_spawnpoint_opponent", this.List_spawnpoint_opponent);
			nbt.put("List_survey_initial", this.List_survey_initial);
			nbt.put("List_survey_label", this.List_survey_label);
			nbt.put("List_survey_name", this.List_survey_name);
			nbt.put("List_survey_range", this.List_survey_range);
			nbt.put("List_survey_type", this.List_survey_type);
			nbt.put("List_wall", this.List_wall);
			{
				this.Meow_destination = this.Meow_destination == null ? Vec3.ZERO : this.Meow_destination;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Meow_destination.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Meow_destination.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Meow_destination.z()));
				nbt.put("Meow_destination", listTag);
			}
			nbt.putDouble("Meow_dx", Meow_dx);
			nbt.putDouble("Meow_dz", Meow_dz);
			nbt.putBoolean("Msg_actionbar_switch", Msg_actionbar_switch);
			nbt.putString("Msg_actionbar_text", Msg_actionbar_text);
			nbt.putString("Msg_subtitle_text", Msg_subtitle_text);
			nbt.putString("Msg_title_text", Msg_title_text);
			nbt.putBoolean("Msg_titles_switch", Msg_titles_switch);
			{
				this.Pos_border_end = this.Pos_border_end == null ? Vec3.ZERO : this.Pos_border_end;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Pos_border_end.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Pos_border_end.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Pos_border_end.z()));
				nbt.put("Pos_border_end", listTag);
			}
			{
				this.Pos_border_start = this.Pos_border_start == null ? Vec3.ZERO : this.Pos_border_start;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Pos_border_start.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Pos_border_start.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Pos_border_start.z()));
				nbt.put("Pos_border_start", listTag);
			}
			{
				this.Pos_offset = this.Pos_offset == null ? Vec3.ZERO : this.Pos_offset;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Pos_offset.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Pos_offset.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Pos_offset.z()));
				nbt.put("Pos_offset", listTag);
			}
			{
				this.Pos_opponent = this.Pos_opponent == null ? Vec3.ZERO : this.Pos_opponent;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Pos_opponent.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Pos_opponent.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Pos_opponent.z()));
				nbt.put("Pos_opponent", listTag);
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
				this.Pos_player = this.Pos_player == null ? Vec3.ZERO : this.Pos_player;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Pos_player.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Pos_player.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Pos_player.z()));
				nbt.put("Pos_player", listTag);
			}
			{
				this.Pos_player_destination = this.Pos_player_destination == null ? Vec3.ZERO : this.Pos_player_destination;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Pos_player_destination.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Pos_player_destination.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Pos_player_destination.z()));
				nbt.put("Pos_player_destination", listTag);
			}
			nbt.put("Psy_likelihood", this.Psy_likelihood);
			nbt.put("Psy_param_gamma", this.Psy_param_gamma);
			nbt.put("Psy_param_lambda", this.Psy_param_lambda);
			nbt.put("Psy_param_m", this.Psy_param_m);
			nbt.put("Psy_param_w", this.Psy_param_w);
			nbt.putBoolean("Switch_AI", Switch_AI);
			nbt.putBoolean("Switch_blank", Switch_blank);
			nbt.putBoolean("Switch_debug", Switch_debug);
			nbt.putBoolean("Switch_que", Switch_que);
			nbt.putBoolean("Switch_scanner", Switch_scanner);
			nbt.putBoolean("Switch_surrender", Switch_surrender);
			nbt.putBoolean("Switch_timer", Switch_timer);
			nbt.putBoolean("Switch_trace", Switch_trace);
			nbt.putDouble("TimA_time", TimA_time);
			nbt.putDouble("TimA_time_currtick", TimA_time_currtick);
			nbt.putDouble("TimA_time_oldtick", TimA_time_oldtick);
			nbt.putBoolean("TimD_switch", TimD_switch);
			nbt.putDouble("TimD_time", TimD_time);
			nbt.putDouble("Time_AI", Time_AI);
			nbt.putDouble("TimR_que_time", TimR_que_time);
			nbt.putDouble("TimR_time", TimR_time);
			nbt.putDouble("TimS_time", TimS_time);
			{
				this.Tuto_checkpoint_center = this.Tuto_checkpoint_center == null ? Vec3.ZERO : this.Tuto_checkpoint_center;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Tuto_checkpoint_center.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Tuto_checkpoint_center.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Tuto_checkpoint_center.z()));
				nbt.put("Tuto_checkpoint_center", listTag);
			}
			nbt.putDouble("Tuto_checkpoint_index", Tuto_checkpoint_index);
			nbt.put("Tuto_checkpoint_pos", this.Tuto_checkpoint_pos);
			nbt.put("Tuto_checkpoint_route", this.Tuto_checkpoint_route);
			nbt.putDouble("Tuto_hurdle_stack", Tuto_hurdle_stack);
			nbt.putDouble("Tuto_hurdle_stack_old", Tuto_hurdle_stack_old);
			nbt.putDouble("Tuto_score", Tuto_score);
			nbt.putBoolean("Tuto_score_running", Tuto_score_running);
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
