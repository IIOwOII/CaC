package net.mcreator.cac;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import net.mcreator.cac.entity.EntMeowCamEntity;

import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstMeowview {
	public static void ToggleMeowview() {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null) return;

		if (mc.getCameraEntity() == player) {
			Level level = player.level();
			if (level != null) {
				EntMeowCamEntity meowview = null;
				List<EntMeowCamEntity> cams = level.getEntitiesOfClass(EntMeowCamEntity.class, player.getBoundingBox().inflate(50));
				for (EntMeowCamEntity cam : cams) {
					TamableAnimal tameablecam = cam;
					if (tameablecam.isOwnedBy(player)) {
						meowview = cam;
					}
				}
				if (meowview != null) {
					mc.setCameraEntity(meowview);
				}
			}
		} else {
			mc.setCameraEntity(player);
		}
	}
}