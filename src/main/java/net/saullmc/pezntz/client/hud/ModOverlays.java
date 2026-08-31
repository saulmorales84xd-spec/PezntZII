package net.saullmc.pezntz.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.effect.ModEffects;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModOverlays {

    private static final ResourceLocation MANCHA_TEXTURE = new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/mancha_zombie.png");

    public static final IGuiOverlay MANCHA_OVERLAY = (ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) -> {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player != null && minecraft.player.hasEffect(ModEffects.MANCHA_ZOMBIE.get())) {

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            guiGraphics.blit(MANCHA_TEXTURE, 0, 0, -90, 0.0F, 0.0F, screenWidth, screenHeight, screenWidth, screenHeight);

            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }
    };

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("mancha_zombie_overlay", MANCHA_OVERLAY);
    }
}