package net.mcreator.cac;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CstKeybind {
    // Store the key mapping reference
    public static KeyMapping CAC_HOTBAR_KEY;
    public static KeyMapping CAC_CAMERA_KEY;

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        CAC_HOTBAR_KEY = new KeyMapping(
            "key.cac.key_hotbar", // translation key
            GLFW.GLFW_KEY_RIGHT_CONTROL, // default key
            "key.categories.cac"     // category for the controls menu
        );
        CAC_CAMERA_KEY = new KeyMapping(
            "key.cac.key_camera", // translation key
            GLFW.GLFW_KEY_F6, // default key
            "key.categories.cac"     // category for the controls menu
        );
        
        event.register(CAC_HOTBAR_KEY);
        event.register(CAC_CAMERA_KEY);
    }
}