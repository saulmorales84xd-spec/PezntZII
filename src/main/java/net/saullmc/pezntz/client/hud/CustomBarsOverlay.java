package net.saullmc.pezntz.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.entity.custom.Quad;

@Mod.EventBusSubscriber(modid = "pezntz", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CustomBarsOverlay {

    private static final ResourceLocation HEALTH_BAR_BG = new ResourceLocation("pezntz", "textures/gui/barra_vida.png");
    private static final ResourceLocation HUNGER_BAR_BG = new ResourceLocation("pezntz", "textures/gui/barra_comida.png");
    private static final ResourceLocation AIR_BAR_BG = new ResourceLocation("pezntz", "textures/gui/barra_aire.png");

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || mc.options.hideGui || player.isSpectator()) return;

        if (mc.gameMode == null || !mc.gameMode.canHurtPlayer()) return;

        ResourceLocation overlayId = event.getOverlay().id();
        GuiGraphics graphics = event.getGuiGraphics();

        if (overlayId.equals(VanillaGuiOverlay.PLAYER_HEALTH.id())) {
            event.setCanceled(true);
            drawCustomHealthBar(graphics, mc, player);
        }
        else if (overlayId.equals(VanillaGuiOverlay.FOOD_LEVEL.id())) {
            event.setCanceled(true);
            drawCustomHungerBar(graphics, mc, player);
        }
        else if (overlayId.equals(VanillaGuiOverlay.ARMOR_LEVEL.id())) {
            event.setCanceled(true);
        }
        else if (overlayId.equals(VanillaGuiOverlay.AIR_LEVEL.id())) {
            event.setCanceled(true);
            drawCustomAirBar(graphics, mc, player);
        }
        else if (overlayId.equals(VanillaGuiOverlay.MOUNT_HEALTH.id())) {
            if (player.getVehicle() instanceof Quad) {
                event.setCanceled(true);
            }
        }
    }

    private static void drawCustomHealthBar(GuiGraphics graphics, Minecraft mc, Player player) {
        ForgeGui gui = (ForgeGui) mc.gui;
        Font font = mc.font;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();
        float absorption = player.getAbsorptionAmount();

        int barWidth = 70;
        int barHeight = 7;

        int x = (screenWidth / 2) - 81;
        int y = screenHeight - gui.leftHeight;

        float healthPercentage = Math.min(health / maxHealth, 1.0f);
        int healthWidth = (int) (healthPercentage * barWidth);

        int textureX = x - 24;
        int textureY = y - 4;

        graphics.blit(HEALTH_BAR_BG, textureX, textureY, 0, 0, 96, 16, 96, 16);

        graphics.fill(x, y, x + healthWidth, y + barHeight, 0xFF3FC430);

        String text;
        int currentHealth = (int) Math.ceil(health);
        int maxHealthInt = (int) maxHealth;

        if (absorption > 0) {
            int extraHealth = (int) Math.ceil(absorption);
            text = currentHealth + " / " + maxHealthInt + " + " + extraHealth;
        } else {
            text = currentHealth + " / " + maxHealthInt;
        }

        float scale = 0.75f;

        graphics.pose().pushPose();
        try {
            float centerX = x + (barWidth / 2.0f);
            float centerY = y + (barHeight / 2.0f);
            graphics.pose().translate(centerX, centerY, 0);
            graphics.pose().scale(scale, scale, 1.0f);

            int textWidth = font.width(text);
            int textHeight = font.lineHeight;

            graphics.drawString(font, text, -textWidth / 2, -textHeight / 2 + 1, 0xFFFFFF, true);
        } finally {
            graphics.pose().popPose();
        }

        gui.leftHeight += 10;
    }

    private static void drawCustomHungerBar(GuiGraphics graphics, Minecraft mc, Player player) {
        ForgeGui gui = (ForgeGui) mc.gui;
        Font font = mc.font;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int food = player.getFoodData().getFoodLevel();
        int maxFood = 20;

        int barWidth = 70;
        int barHeight = 7;

        int x = (screenWidth / 2) + 11;
        int y = screenHeight - gui.rightHeight;

        float foodPercentage = Math.min(food / (float) maxFood, 1.0f);
        int currentWidth = (int) (foodPercentage * barWidth);

        int textureX = x - 2;
        int textureY = y - 4;

        graphics.blit(HUNGER_BAR_BG, textureX, textureY, 0, 0, 96, 16, 96, 16);

        int fillStartX = x + (barWidth - currentWidth);
        graphics.fill(fillStartX, y, x + barWidth, y + barHeight, 0xFFC4A630);

        String text = food + " / " + maxFood;
        float scale = 0.75f;

        graphics.pose().pushPose();
        try {
            float centerX = x + (barWidth / 2.0f);
            float centerY = y + (barHeight / 2.0f);
            graphics.pose().translate(centerX, centerY, 0);
            graphics.pose().scale(scale, scale, 1.0f);

            int textWidth = font.width(text);
            int textHeight = font.lineHeight;

            graphics.drawString(font, text, -textWidth / 2, -textHeight / 2 + 1, 0xFFFFFF, true);
        } finally {
            graphics.pose().popPose();
        }

        gui.rightHeight += 10;
    }

    private static void drawCustomAirBar(GuiGraphics graphics, Minecraft mc, Player player) {
        ForgeGui gui = (ForgeGui) mc.gui;
        Font font = mc.font;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int air = player.getAirSupply();
        int maxAir = player.getMaxAirSupply();

        if (air >= maxAir) return;

        int barWidth = 70;
        int barHeight = 7;

        int x = (screenWidth / 2) + 11;
        int y = screenHeight - gui.rightHeight - 3;

        float airPercentage = Math.min((float) Math.max(0, air) / maxAir, 1.0f);
        int currentWidth = (int) (airPercentage * barWidth);

        int textureX = x - 2;
        int textureY = y - 4;

        graphics.blit(AIR_BAR_BG, textureX, textureY, 0, 0, 96, 16, 96, 16);

        int fillStartX = x + (barWidth - currentWidth);
        graphics.fill(fillStartX, y, x + barWidth, y + barHeight, 0xFF33CCFF); // Azul claro

        int currentBubbles = (int) Math.ceil(((double) Math.max(0, air) / maxAir) * 10);
        String text = currentBubbles + " / 10";

        float scale = 0.75f;

        graphics.pose().pushPose();
        try {
            float centerX = x + (barWidth / 2.0f);
            float centerY = y + (barHeight / 2.0f);
            graphics.pose().translate(centerX, centerY, 0);
            graphics.pose().scale(scale, scale, 1.0f);

            int textWidth = font.width(text);
            int textHeight = font.lineHeight;

            graphics.drawString(font, text, -textWidth / 2, -textHeight / 2 + 1, 0xFFFFFF, true);
        } finally {
            graphics.pose().popPose();
        }

        gui.rightHeight += 13;
    }
}