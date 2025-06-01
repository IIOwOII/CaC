package net.owo.cac;

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
    public static KeyMapping CAC_CAMERA_KEY;
    public static KeyMapping CAC_DOWN_KEY;
    public static KeyMapping CAC_UP_KEY;
    public static KeyMapping CAC_LEFT_KEY;
    public static KeyMapping CAC_RIGHT_KEY;

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        CAC_CAMERA_KEY = new KeyMapping(
            "key.cac.cac_key_camera", // translation key
            GLFW.GLFW_KEY_RIGHT_CONTROL, // default key
            "key.categories.cac"     // category for the controls menu
        );
        event.register(CAC_CAMERA_KEY);

        CAC_DOWN_KEY = new KeyMapping("key.cac.cac_key_down", GLFW.GLFW_KEY_DOWN, "key.categories.cac");
        event.register(CAC_DOWN_KEY);

        CAC_UP_KEY = new KeyMapping("key.cac.cac_key_up", GLFW.GLFW_KEY_UP, "key.categories.cac");
        event.register(CAC_UP_KEY);

        CAC_RIGHT_KEY = new KeyMapping("key.cac.cac_key_right", GLFW.GLFW_KEY_RIGHT, "key.categories.cac");
        event.register(CAC_RIGHT_KEY);

        CAC_LEFT_KEY = new KeyMapping("key.cac.cac_key_left", GLFW.GLFW_KEY_LEFT, "key.categories.cac");
        event.register(CAC_LEFT_KEY);
    }
}