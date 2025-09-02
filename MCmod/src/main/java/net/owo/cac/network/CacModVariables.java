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
	public static com.google.gson.JsonArray Dat_pos_opponent_x_prep = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_opponent_z_prep = new com.google.gson.JsonArray();
	public static com.google.gson.JsonArray Dat_pos_opponent_r_prep = new com.google.gson.JsonArray();
	public static File Pool_survey = new File("");
	public static File Log_survey = new File("");
	public static File Pool_psychometric = new File("");
	public static File Log_scanner = new File("");

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
		public double TimR_time = 0;
		public double TimA_time = 0;
		public double TimA_time_currtick = 0;
		public double TimA_time_oldtick = 0;
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
		public double Meow_dx = 0;
		public double Meow_dz = 0;
		public Vec3 Pos_player_destination = Vec3.ZERO;
		public Vec3 Pos_opponent_destination = Vec3.ZERO;
		public Vec3 Meow_destination = Vec3.ZERO;
		public double Dat_survey_surrender = 0;
		public ListTag List_survey_name = new ListTag();
		public ListTag List_survey_type = new ListTag();
		public ListTag List_survey_range = new ListTag();
		public ListTag List_survey_label = new ListTag();
		public ListTag List_survey_initial = new ListTag();
		public boolean Switch_survey = false;
		public ListTag Dat_survey_order = new ListTag();
		public ListTag Dat_survey_time = new ListTag();
		public ListTag Dat_survey_answer = new ListTag();
		public ListTag Suv_reference = new ListTag();
		public ListTag Suv_type = new ListTag();
		public ListTag Suv_range_lower = new ListTag();
		public ListTag Suv_range_upper = new ListTag();
		public ListTag Suv_label_low = new ListTag();
		public ListTag Suv_label_mid = new ListTag();
		public ListTag Suv_label_high = new ListTag();
		public ListTag Suv_initial = new ListTag();
		public double SuvT_index = 0;
		public ListTag SuvT_answer_pre = new ListTag();
		public String SuvT_name = "\"\"";
		public String SuvT_type = "\"\"";
		public double SuvT_range_lower = 0;
		public double SuvT_range_upper = 0;
		public String SuvT_label_low = "\"\"";
		public String SuvT_label_mid = "\"\"";
		public String SuvT_label_high = "\"\"";
		public double SuvT_value = 0;
		public double SuvT_value_pre = 0;
		public double SuvT_time = 0;
		public boolean Switch_surrender = false;
		public double Dat_survey_surrender_type = 0;
		public ListTag Psy_param_m = new ListTag();
		public ListTag Psy_param_w = new ListTag();
		public ListTag Psy_param_gamma = new ListTag();
		public ListTag Psy_param_lambda = new ListTag();
		public ListTag Psy_likelihood = new ListTag();
		public boolean Switch_countdown = false;
		public ListTag Dat_psy_param = new ListTag();
		public boolean Switch_scanner = false;
		public double TimS_time = 0;
		public double Tuto_checkpoint_index = 0;
		public Vec3 Tuto_checkpoint_center = Vec3.ZERO;
		public ListTag Tuto_checkpoint_route = new ListTag();
		public ListTag Tuto_checkpoint_pos = new ListTag();
		public String Dir_behaviors_session = "\"\"";
		public double Exp_session_reps = 0;
		public Vec3 Builder_pos1 = Vec3.ZERO;
		public Vec3 Builder_pos2 = Vec3.ZERO;
		public double Option_builder = 0;
		public String Option_builder_str = "\"\"";
		public double Tuto_hurdle_stack_old = 0;
		public double Tuto_hurdle_stack = 0;
		public double Tuto_score = 0;
		public boolean Tuto_score_running = false;
		public String Tuto_progress = "\"\"";
		public boolean Tuto_switch = false;

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
			TimR_time = nbt.getDouble("TimR_time");
			TimA_time = nbt.getDouble("TimA_time");
			TimA_time_currtick = nbt.getDouble("TimA_time_currtick");
			TimA_time_oldtick = nbt.getDouble("TimA_time_oldtick");
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
			Dat_survey_surrender = nbt.getDouble("Dat_survey_surrender");
			this.List_survey_name = nbt.get("List_survey_name") instanceof ListTag List_survey_name ? List_survey_name : new ListTag();
			this.List_survey_type = nbt.get("List_survey_type") instanceof ListTag List_survey_type ? List_survey_type : new ListTag();
			this.List_survey_range = nbt.get("List_survey_range") instanceof ListTag List_survey_range ? List_survey_range : new ListTag();
			this.List_survey_label = nbt.get("List_survey_label") instanceof ListTag List_survey_label ? List_survey_label : new ListTag();
			this.List_survey_initial = nbt.get("List_survey_initial") instanceof ListTag List_survey_initial ? List_survey_initial : new ListTag();
			Switch_survey = nbt.getBoolean("Switch_survey");
			this.Dat_survey_order = nbt.get("Dat_survey_order") instanceof ListTag Dat_survey_order ? Dat_survey_order : new ListTag();
			this.Dat_survey_time = nbt.get("Dat_survey_time") instanceof ListTag Dat_survey_time ? Dat_survey_time : new ListTag();
			this.Dat_survey_answer = nbt.get("Dat_survey_answer") instanceof ListTag Dat_survey_answer ? Dat_survey_answer : new ListTag();
			this.Suv_reference = nbt.get("Suv_reference") instanceof ListTag Suv_reference ? Suv_reference : new ListTag();
			this.Suv_type = nbt.get("Suv_type") instanceof ListTag Suv_type ? Suv_type : new ListTag();
			this.Suv_range_lower = nbt.get("Suv_range_lower") instanceof ListTag Suv_range_lower ? Suv_range_lower : new ListTag();
			this.Suv_range_upper = nbt.get("Suv_range_upper") instanceof ListTag Suv_range_upper ? Suv_range_upper : new ListTag();
			this.Suv_label_low = nbt.get("Suv_label_low") instanceof ListTag Suv_label_low ? Suv_label_low : new ListTag();
			this.Suv_label_mid = nbt.get("Suv_label_mid") instanceof ListTag Suv_label_mid ? Suv_label_mid : new ListTag();
			this.Suv_label_high = nbt.get("Suv_label_high") instanceof ListTag Suv_label_high ? Suv_label_high : new ListTag();
			this.Suv_initial = nbt.get("Suv_initial") instanceof ListTag Suv_initial ? Suv_initial : new ListTag();
			SuvT_index = nbt.getDouble("SuvT_index");
			this.SuvT_answer_pre = nbt.get("SuvT_answer_pre") instanceof ListTag SuvT_answer_pre ? SuvT_answer_pre : new ListTag();
			SuvT_name = nbt.getString("SuvT_name");
			SuvT_type = nbt.getString("SuvT_type");
			SuvT_range_lower = nbt.getDouble("SuvT_range_lower");
			SuvT_range_upper = nbt.getDouble("SuvT_range_upper");
			SuvT_label_low = nbt.getString("SuvT_label_low");
			SuvT_label_mid = nbt.getString("SuvT_label_mid");
			SuvT_label_high = nbt.getString("SuvT_label_high");
			SuvT_value = nbt.getDouble("SuvT_value");
			SuvT_value_pre = nbt.getDouble("SuvT_value_pre");
			SuvT_time = nbt.getDouble("SuvT_time");
			Switch_surrender = nbt.getBoolean("Switch_surrender");
			Dat_survey_surrender_type = nbt.getDouble("Dat_survey_surrender_type");
			this.Psy_param_m = nbt.get("Psy_param_m") instanceof ListTag Psy_param_m ? Psy_param_m : new ListTag();
			this.Psy_param_w = nbt.get("Psy_param_w") instanceof ListTag Psy_param_w ? Psy_param_w : new ListTag();
			this.Psy_param_gamma = nbt.get("Psy_param_gamma") instanceof ListTag Psy_param_gamma ? Psy_param_gamma : new ListTag();
			this.Psy_param_lambda = nbt.get("Psy_param_lambda") instanceof ListTag Psy_param_lambda ? Psy_param_lambda : new ListTag();
			this.Psy_likelihood = nbt.get("Psy_likelihood") instanceof ListTag Psy_likelihood ? Psy_likelihood : new ListTag();
			Switch_countdown = nbt.getBoolean("Switch_countdown");
			this.Dat_psy_param = nbt.get("Dat_psy_param") instanceof ListTag Dat_psy_param ? Dat_psy_param : new ListTag();
			Switch_scanner = nbt.getBoolean("Switch_scanner");
			TimS_time = nbt.getDouble("TimS_time");
			Tuto_checkpoint_index = nbt.getDouble("Tuto_checkpoint_index");
			{
				ListTag listTag = nbt.getList("Tuto_checkpoint_center", 6);
				this.Tuto_checkpoint_center = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			this.Tuto_checkpoint_route = nbt.get("Tuto_checkpoint_route") instanceof ListTag Tuto_checkpoint_route ? Tuto_checkpoint_route : new ListTag();
			this.Tuto_checkpoint_pos = nbt.get("Tuto_checkpoint_pos") instanceof ListTag Tuto_checkpoint_pos ? Tuto_checkpoint_pos : new ListTag();
			Dir_behaviors_session = nbt.getString("Dir_behaviors_session");
			Exp_session_reps = nbt.getDouble("Exp_session_reps");
			{
				ListTag listTag = nbt.getList("Builder_pos1", 6);
				this.Builder_pos1 = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			{
				ListTag listTag = nbt.getList("Builder_pos2", 6);
				this.Builder_pos2 = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
			}
			Option_builder = nbt.getDouble("Option_builder");
			Option_builder_str = nbt.getString("Option_builder_str");
			Tuto_hurdle_stack_old = nbt.getDouble("Tuto_hurdle_stack_old");
			Tuto_hurdle_stack = nbt.getDouble("Tuto_hurdle_stack");
			Tuto_score = nbt.getDouble("Tuto_score");
			Tuto_score_running = nbt.getBoolean("Tuto_score_running");
			Tuto_progress = nbt.getString("Tuto_progress");
			Tuto_switch = nbt.getBoolean("Tuto_switch");
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
			nbt.putDouble("TimR_time", TimR_time);
			nbt.putDouble("TimA_time", TimA_time);
			nbt.putDouble("TimA_time_currtick", TimA_time_currtick);
			nbt.putDouble("TimA_time_oldtick", TimA_time_oldtick);
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
			nbt.putDouble("Dat_survey_surrender", Dat_survey_surrender);
			nbt.put("List_survey_name", this.List_survey_name);
			nbt.put("List_survey_type", this.List_survey_type);
			nbt.put("List_survey_range", this.List_survey_range);
			nbt.put("List_survey_label", this.List_survey_label);
			nbt.put("List_survey_initial", this.List_survey_initial);
			nbt.putBoolean("Switch_survey", Switch_survey);
			nbt.put("Dat_survey_order", this.Dat_survey_order);
			nbt.put("Dat_survey_time", this.Dat_survey_time);
			nbt.put("Dat_survey_answer", this.Dat_survey_answer);
			nbt.put("Suv_reference", this.Suv_reference);
			nbt.put("Suv_type", this.Suv_type);
			nbt.put("Suv_range_lower", this.Suv_range_lower);
			nbt.put("Suv_range_upper", this.Suv_range_upper);
			nbt.put("Suv_label_low", this.Suv_label_low);
			nbt.put("Suv_label_mid", this.Suv_label_mid);
			nbt.put("Suv_label_high", this.Suv_label_high);
			nbt.put("Suv_initial", this.Suv_initial);
			nbt.putDouble("SuvT_index", SuvT_index);
			nbt.put("SuvT_answer_pre", this.SuvT_answer_pre);
			nbt.putString("SuvT_name", SuvT_name);
			nbt.putString("SuvT_type", SuvT_type);
			nbt.putDouble("SuvT_range_lower", SuvT_range_lower);
			nbt.putDouble("SuvT_range_upper", SuvT_range_upper);
			nbt.putString("SuvT_label_low", SuvT_label_low);
			nbt.putString("SuvT_label_mid", SuvT_label_mid);
			nbt.putString("SuvT_label_high", SuvT_label_high);
			nbt.putDouble("SuvT_value", SuvT_value);
			nbt.putDouble("SuvT_value_pre", SuvT_value_pre);
			nbt.putDouble("SuvT_time", SuvT_time);
			nbt.putBoolean("Switch_surrender", Switch_surrender);
			nbt.putDouble("Dat_survey_surrender_type", Dat_survey_surrender_type);
			nbt.put("Psy_param_m", this.Psy_param_m);
			nbt.put("Psy_param_w", this.Psy_param_w);
			nbt.put("Psy_param_gamma", this.Psy_param_gamma);
			nbt.put("Psy_param_lambda", this.Psy_param_lambda);
			nbt.put("Psy_likelihood", this.Psy_likelihood);
			nbt.putBoolean("Switch_countdown", Switch_countdown);
			nbt.put("Dat_psy_param", this.Dat_psy_param);
			nbt.putBoolean("Switch_scanner", Switch_scanner);
			nbt.putDouble("TimS_time", TimS_time);
			nbt.putDouble("Tuto_checkpoint_index", Tuto_checkpoint_index);
			{
				this.Tuto_checkpoint_center = this.Tuto_checkpoint_center == null ? Vec3.ZERO : this.Tuto_checkpoint_center;
				ListTag listTag = new ListTag();
				listTag.addTag(0, DoubleTag.valueOf(this.Tuto_checkpoint_center.x()));
				listTag.addTag(1, DoubleTag.valueOf(this.Tuto_checkpoint_center.y()));
				listTag.addTag(2, DoubleTag.valueOf(this.Tuto_checkpoint_center.z()));
				nbt.put("Tuto_checkpoint_center", listTag);
			}
			nbt.put("Tuto_checkpoint_route", this.Tuto_checkpoint_route);
			nbt.put("Tuto_checkpoint_pos", this.Tuto_checkpoint_pos);
			nbt.putString("Dir_behaviors_session", Dir_behaviors_session);
			nbt.putDouble("Exp_session_reps", Exp_session_reps);
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
			nbt.putDouble("Option_builder", Option_builder);
			nbt.putString("Option_builder_str", Option_builder_str);
			nbt.putDouble("Tuto_hurdle_stack_old", Tuto_hurdle_stack_old);
			nbt.putDouble("Tuto_hurdle_stack", Tuto_hurdle_stack);
			nbt.putDouble("Tuto_score", Tuto_score);
			nbt.putBoolean("Tuto_score_running", Tuto_score_running);
			nbt.putString("Tuto_progress", Tuto_progress);
			nbt.putBoolean("Tuto_switch", Tuto_switch);
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
