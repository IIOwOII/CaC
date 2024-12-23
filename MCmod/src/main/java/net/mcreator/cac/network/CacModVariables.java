package net.mcreator.cac.network;

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

import net.mcreator.cac.CacMod;

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
		public String Exp_Directory = "\"\"";
		public String Exp_Subject = "\"sub01\"";
		public double Option_tester = 0.0;
		public String Option_tester_str = "\"Reset\"";
		public double Pmt_difficulty = 0.0;
		public double Pos_player_x = 0;
		public double Pos_player_z = 0;
		public boolean Switch_Task = false;
		public double Pmt_far = 15.0;
		public double Pmt_distance_scale = 1.0;
		public boolean Exp_Rest = false;
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
		public boolean Option_hotbar = false;

		public static MapVariables load(CompoundTag tag) {
			MapVariables data = new MapVariables();
			data.read(tag);
			return data;
		}

		public void read(CompoundTag nbt) {
			Exp_Directory = nbt.getString("Exp_Directory");
			Exp_Subject = nbt.getString("Exp_Subject");
			Option_tester = nbt.getDouble("Option_tester");
			Option_tester_str = nbt.getString("Option_tester_str");
			Pmt_difficulty = nbt.getDouble("Pmt_difficulty");
			Pos_player_x = nbt.getDouble("Pos_player_x");
			Pos_player_z = nbt.getDouble("Pos_player_z");
			Switch_Task = nbt.getBoolean("Switch_Task");
			Pmt_far = nbt.getDouble("Pmt_far");
			Pmt_distance_scale = nbt.getDouble("Pmt_distance_scale");
			Exp_Rest = nbt.getBoolean("Exp_Rest");
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
			Option_hotbar = nbt.getBoolean("Option_hotbar");
		}

		@Override
		public CompoundTag save(CompoundTag nbt) {
			nbt.putString("Exp_Directory", Exp_Directory);
			nbt.putString("Exp_Subject", Exp_Subject);
			nbt.putDouble("Option_tester", Option_tester);
			nbt.putString("Option_tester_str", Option_tester_str);
			nbt.putDouble("Pmt_difficulty", Pmt_difficulty);
			nbt.putDouble("Pos_player_x", Pos_player_x);
			nbt.putDouble("Pos_player_z", Pos_player_z);
			nbt.putBoolean("Switch_Task", Switch_Task);
			nbt.putDouble("Pmt_far", Pmt_far);
			nbt.putDouble("Pmt_distance_scale", Pmt_distance_scale);
			nbt.putBoolean("Exp_Rest", Exp_Rest);
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
			nbt.putBoolean("Option_hotbar", Option_hotbar);
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
