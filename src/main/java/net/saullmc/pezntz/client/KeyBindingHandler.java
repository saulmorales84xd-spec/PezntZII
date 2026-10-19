package net.saullmc.pezntz.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.client.screen.HealingScreen;
import net.saullmc.pezntz.client.screen.menu.MenuPrincipalScreen;
import org.lwjgl.glfw.GLFW;
import net.saullmc.pezntz.network.NetworkHandler;
import net.saullmc.pezntz.network.CambiarAsientoPacket;
import net.saullmc.pezntz.network.OpenBackpackPacket;
import net.saullmc.pezntz.entity.custom.VehiculoBase;

@Mod.EventBusSubscriber(modid = "pezntz", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBindingHandler {

    public static final KeyMapping MEDICAL_GUI_KEY = new KeyMapping(
            "key.pezntz.medical_gui",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "category.pezntz.keys"
    );

    public static final KeyMapping BACKPACK_KEY = new KeyMapping(
            "key.pezntz.backpack",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "category.pezntz.keys"
    );

    public static final KeyMapping CAMBIAR_ASIENTO_KEY = new KeyMapping(
            "key.pezntz.cambiar_asiento",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_CONTROL,
            "category.pezntz.keys"
    );

    public static final KeyMapping MENU_KEY = new KeyMapping(
            "key.pezntz.menu",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "category.pezntz.keys"
    );

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(MEDICAL_GUI_KEY);
        event.register(BACKPACK_KEY);
        event.register(CAMBIAR_ASIENTO_KEY);
        event.register(MENU_KEY);
    }

    @Mod.EventBusSubscriber(modid = "pezntz", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            Minecraft mc = Minecraft.getInstance();

            if (MEDICAL_GUI_KEY.consumeClick()) {
                if (mc.player != null && mc.screen == null) {
                    mc.setScreen(new HealingScreen());
                }
            }

            if (MENU_KEY.consumeClick()) {
                if (mc.player != null && mc.screen == null) {
                    MenuPrincipalScreen.abrir();
                }
            }

            if (CAMBIAR_ASIENTO_KEY.consumeClick()) {
                if (mc.player != null && mc.player.getVehicle() instanceof VehiculoBase) {
                    NetworkHandler.sendToServer(new CambiarAsientoPacket());
                }
            }

            if (BACKPACK_KEY.consumeClick()) {
                if (mc.player != null && mc.screen == null
                        && OpenBackpackPacket.llevaMochila(mc.player)) {
                    NetworkHandler.sendToServer(new OpenBackpackPacket());
                }
            }
        }
    }
}