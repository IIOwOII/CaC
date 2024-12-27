package net.owo.cac;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstKeyHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        // Only run on the "end" phase to avoid doubling
        if (event.phase == TickEvent.Phase.END) {
            if (CstKeybind.CAC_HOTBAR_KEY.consumeClick()) {
                // Toggle the hotbar state
                CstState.hotbarVisible = !CstState.hotbarVisible;
            }
            if (CstKeybind.CAC_CAMERA_KEY.consumeClick()) {
                // Toggle the camera state
                CstMeowview.ToggleMeowview();
            }
        }
    }
}