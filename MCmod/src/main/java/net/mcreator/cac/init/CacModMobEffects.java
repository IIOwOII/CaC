
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.cac.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffect;

import net.mcreator.cac.potion.EffStopMoveMobEffect;
import net.mcreator.cac.potion.EffMorphPreyMobEffect;
import net.mcreator.cac.potion.EffMorphPredatorMobEffect;
import net.mcreator.cac.CacMod;

public class CacModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CacMod.MODID);
	public static final RegistryObject<MobEffect> EFF_MORPH_PREY = REGISTRY.register("eff_morph_prey", () -> new EffMorphPreyMobEffect());
	public static final RegistryObject<MobEffect> EFF_MORPH_PREDATOR = REGISTRY.register("eff_morph_predator", () -> new EffMorphPredatorMobEffect());
	public static final RegistryObject<MobEffect> EFF_STOP_MOVE = REGISTRY.register("eff_stop_move", () -> new EffStopMoveMobEffect());
}
