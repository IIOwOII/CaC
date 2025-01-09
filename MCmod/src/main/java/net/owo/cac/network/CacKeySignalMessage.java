
package net.owo.cac.network;

import net.owo.cac.procedures.PrdKeySignalProcedure;
import net.owo.cac.procedures.PrdKeySignalOutProcedure;
import net.owo.cac.CacMod;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CacKeySignalMessage {
	int type, pressedms;

	public CacKeySignalMessage(int type, int pressedms) {
		this.type = type;
		this.pressedms = pressedms;
	}

	public CacKeySignalMessage(FriendlyByteBuf buffer) {
		this.type = buffer.readInt();
		this.pressedms = buffer.readInt();
	}

	public static void buffer(CacKeySignalMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.type);
		buffer.writeInt(message.pressedms);
	}

	public static void handler(CacKeySignalMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			pressAction(context.getSender(), message.type, message.pressedms);
		});
		context.setPacketHandled(true);
	}

	public static void pressAction(Player entity, int type, int pressedms) {
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(entity.blockPosition()))
			return;
		if (type == 0) {

			PrdKeySignalProcedure.execute(world);
		}
		if (type == 1) {

			PrdKeySignalOutProcedure.execute(world);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		CacMod.addNetworkMessage(CacKeySignalMessage.class, CacKeySignalMessage::buffer, CacKeySignalMessage::new, CacKeySignalMessage::handler);
	}
}
