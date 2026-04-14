
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.owo.cac.init;

import net.owo.cac.CacMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

public class CacModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CacMod.MODID);
	public static final RegistryObject<SoundEvent> CAC_SND_SILENCE = REGISTRY.register("cac.snd_silence", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("cac", "cac.snd_silence")));
	public static final RegistryObject<SoundEvent> SND_ORB_C7 = REGISTRY.register("snd_orb_c7", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("cac", "snd_orb_c7")));
	public static final RegistryObject<SoundEvent> SND_ORB_A7 = REGISTRY.register("snd_orb_a7", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("cac", "snd_orb_a7")));
	public static final RegistryObject<SoundEvent> SND_BBYONG = REGISTRY.register("snd_bbyong", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("cac", "snd_bbyong")));
}
