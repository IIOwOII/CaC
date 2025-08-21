package net.owo.cac;

import net.owo.cac.CacMod;
import net.owo.cac.CstState;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CstKeyMessage {
	int type, pressedms, keyindex;

	public CstKeyMessage(int type, int pressedms, int keyindex) {
		this.type = type;
		this.pressedms = pressedms;
		this.keyindex = keyindex;
	}

	public CstKeyMessage(FriendlyByteBuf buffer) {
		this.type = buffer.readInt();
		this.pressedms = buffer.readInt();
		this.keyindex = buffer.readInt();
	}

	public static void buffer(CstKeyMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.type);
		buffer.writeInt(message.pressedms);
		buffer.writeInt(message.keyindex);
	}

	public static void handler(CstKeyMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			pressAction(context.getSender(), message.type, message.pressedms, message.keyindex);
		});
		context.setPacketHandled(true);
	}

	public static void pressAction(Player entity, int type, int pressedms, int keyindex) {
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(entity.blockPosition()))
			return;
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		CacMod.addNetworkMessage(CstKeyMessage.class, CstKeyMessage::buffer, CstKeyMessage::new, CstKeyMessage::handler);
	}
	
}