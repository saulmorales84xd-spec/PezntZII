package net.saullmc.pezntz.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.client.hud.BodyPartsOverlay;
import net.saullmc.pezntz.client.hud.CompassOverlay;
import net.saullmc.pezntz.client.hud.CoordinatesOverlay;
import net.saullmc.pezntz.client.hud.JeringaOverlay;
import net.saullmc.pezntz.client.screen.BackpackScreen;
import net.saullmc.pezntz.effect.ModEffects;
import net.saullmc.pezntz.entity.custom.Quad;
import net.saullmc.pezntz.init.ModMenuTypes;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.BACKPACK_MENU.get(), BackpackScreen::new);
        });
    }
    public static final IGuiOverlay COMPASS = (gui, guiGraphics, partialTick, width, height) -> {
        CompassOverlay.render(guiGraphics, partialTick);
        CoordinatesOverlay.render(guiGraphics);
    };

    public static final IGuiOverlay PLAGA_OVERLAY = (gui, guiGraphics, partialTick, width, height) -> {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.hasEffect(ModEffects.PLAGA.get())) {

            RenderSystem.enableBlend();

            ResourceLocation pantallaCompleta = new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/plaga.png");
            guiGraphics.blit(pantallaCompleta, 0, 0, 0, 0, width, height, width, height);

            ResourceLocation frame1 = new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/space1.png");
            ResourceLocation frame2 = new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/space2.png");
            ResourceLocation currentFrame = (player.tickCount / 5) % 2 == 0 ? frame1 : frame2;

            float scale = 1.7F;
            int imgWidth = 96;
            int imgHeight = 32;

            int renderWidth = (int) (imgWidth * scale);
            int renderHeight = (int) (imgHeight * scale);

            int x = (width - renderWidth) / 2;
            int y = height - 75 - (renderHeight / 2);

            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, 1.0F);

            guiGraphics.blit(currentFrame, (int) (x / scale), (int) (y / scale), 0, 0, imgWidth, imgHeight, imgWidth, imgHeight);

            guiGraphics.pose().popPose();
            RenderSystem.disableBlend();
        }
    };

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("compass", COMPASS);
        event.registerAboveAll("healing_timer", JeringaOverlay.HUD_HEALING_TIMER);
        event.registerAboveAll("body_health_hud", BodyPartsOverlay.HUD_BODY_PARTS);
        event.registerAboveAll("plaga_hud", PLAGA_OVERLAY);
    }
}