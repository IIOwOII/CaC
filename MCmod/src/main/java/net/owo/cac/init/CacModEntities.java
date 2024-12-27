
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.owo.cac.init;

import net.owo.cac.entity.EntPlayerMouseEntity;
import net.owo.cac.entity.EntPlayerCatEntity;
import net.owo.cac.entity.EntMouseEntity;
import net.owo.cac.entity.EntMeowCamEntity;
import net.owo.cac.entity.EntCatEntity;
import net.owo.cac.CacMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CacModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CacMod.MODID);
	public static final RegistryObject<EntityType<EntCatEntity>> ENT_CAT = register("ent_cat",
			EntityType.Builder.<EntCatEntity>of(EntCatEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(128).setUpdateInterval(3).setCustomClientFactory(EntCatEntity::new).fireImmune().sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<EntPlayerCatEntity>> ENT_PLAYER_CAT = register("ent_player_cat", EntityType.Builder.<EntPlayerCatEntity>of(EntPlayerCatEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true)
			.setTrackingRange(128).setUpdateInterval(3).setCustomClientFactory(EntPlayerCatEntity::new).fireImmune().sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<EntMouseEntity>> ENT_MOUSE = register("ent_mouse",
			EntityType.Builder.<EntMouseEntity>of(EntMouseEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(128).setUpdateInterval(3).setCustomClientFactory(EntMouseEntity::new).fireImmune().sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<EntPlayerMouseEntity>> ENT_PLAYER_MOUSE = register("ent_player_mouse", EntityType.Builder.<EntPlayerMouseEntity>of(EntPlayerMouseEntity::new, MobCategory.CREATURE)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(128).setUpdateInterval(3).setCustomClientFactory(EntPlayerMouseEntity::new).fireImmune().sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<EntMeowCamEntity>> ENT_MEOW_CAM = register("ent_meow_cam", EntityType.Builder.<EntMeowCamEntity>of(EntMeowCamEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true)
			.setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(EntMeowCamEntity::new).fireImmune().sized(0.5f, 0.5f));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			EntCatEntity.init();
			EntPlayerCatEntity.init();
			EntMouseEntity.init();
			EntPlayerMouseEntity.init();
			EntMeowCamEntity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(ENT_CAT.get(), EntCatEntity.createAttributes().build());
		event.put(ENT_PLAYER_CAT.get(), EntPlayerCatEntity.createAttributes().build());
		event.put(ENT_MOUSE.get(), EntMouseEntity.createAttributes().build());
		event.put(ENT_PLAYER_MOUSE.get(), EntPlayerMouseEntity.createAttributes().build());
		event.put(ENT_MEOW_CAM.get(), EntMeowCamEntity.createAttributes().build());
	}
}
