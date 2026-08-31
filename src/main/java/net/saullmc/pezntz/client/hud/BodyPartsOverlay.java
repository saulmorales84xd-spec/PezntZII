package net.saullmc.pezntz.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.saullmc.pezntz.capability.BodyHealthProvider;

public class BodyPartsOverlay {

    private static final ResourceLocation CUSTOM_ICON = new ResourceLocation("pezntz", "textures/gui/cuerpo.png");

    public static final IGuiOverlay HUD_BODY_PARTS = (ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) -> {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui || mc.player.isSpectator()) return;

        mc.player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(myCap -> {

            Font font = mc.font;

            if (myCap.isDowned()) {
                if (myCap.getReviveProgress() > 0) {
                    drawReviveBar(guiGraphics, screenWidth, screenHeight, myCap.getReviveProgress());
                }

                int seconds = myCap.getDownedTimer() / 20;
                String timeText = String.format("%02d:%02d", seconds / 60, seconds % 60);
                int timeWidth = font.width(timeText);

                guiGraphics.drawString(font, timeText, (screenWidth - timeWidth) / 2, screenHeight - 70, 0xFFFFFF, true);
            }
            else {
                int barX = 10;
                int startY = 10;
                int spacingY = 15;

                int iconOffsetX = 0;
                float iconOffsetY = -1.5f;
                float iconScale = 1.8f;

                RenderSystem.enableBlend();
                guiGraphics.pose().pushPose();
                guiGraphics.pose().scale(iconScale, iconScale, 1.0f);

                int drawX = (int) ((barX + iconOffsetX) / iconScale);
                int drawY = (int) ((startY + iconOffsetY) / iconScale);

                guiGraphics.blit(CUSTOM_ICON, drawX, drawY, 0, 0, 16, 32, 16, 32);

                guiGraphics.pose().popPose();
                RenderSystem.disableBlend();

                drawBar(guiGraphics, font, myCap.getHead(), barX, startY);
                drawBar(guiGraphics, font, myCap.getBody(), barX, startY + spacingY);
                drawBar(guiGraphics, font, myCap.getArms(), barX, startY + (spacingY * 2));
                drawBar(guiGraphics, font, myCap.getLegs(), barX, startY + (spacingY * 3));

                if (mc.crosshairPickEntity instanceof Player targetPlayer) {
                    targetPlayer.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(targetCap -> {
                        if (targetCap.isDowned()) {
                            if (targetCap.getReviveProgress() > 0) {
                                drawReviveBar(guiGraphics, screenWidth, screenHeight, targetCap.getReviveProgress());
                            }
                            else {

                                MutableComponent promptText = Component.literal("Pulsa ")
                                        .withStyle(style -> style.withColor(0xFFFFFF))

                                        .append(Component.literal("Click derecho ")
                                                .withStyle(style -> style.withColor(0xFFAA00)))

                                        .append(Component.literal("para revivir a ")
                                                .withStyle(style -> style.withColor(0xFFFFFF)))

                                        .append(Component.literal(targetPlayer.getDisplayName().getString())
                                                .withStyle(style -> style.withColor(0x00FF00)));

                                int promptWidth = font.width(promptText);

                                float textScale = 1.2f;

                                guiGraphics.pose().pushPose();
                                guiGraphics.pose().scale(textScale, textScale, 1.0f);

                                int promptDrawX = (int) (((screenWidth / textScale) - promptWidth) / 2);
                                int promptDrawY = (int) ((screenHeight / 2 + 110) / textScale);

                                guiGraphics.drawString(font, promptText, promptDrawX, promptDrawY, 0xFFFFFF, true);

                                guiGraphics.pose().popPose();
                            }
                        }
                    });
                }
            }
        });
    };

    private static void drawReviveBar(GuiGraphics guiGraphics, int screenWidth, int screenHeight, int progressTicks) {
        int barY = screenHeight - 90;
        int centerX = screenWidth / 2;

        int maxBarWidth = 140;
        int barHeight = 16;

        float progress = progressTicks / 200.0f;
        int currentFillWidth = (int) (progress * maxBarWidth);

        int bgLeft = centerX - (maxBarWidth / 2);
        int bgRight = centerX + (maxBarWidth / 2);
        int fillLeft = centerX - (currentFillWidth / 2);
        int fillRight = centerX + (currentFillWidth / 2);

        guiGraphics.fill(bgLeft, barY, bgRight, barY + barHeight, 0x88000000);
        guiGraphics.fill(fillLeft, barY, fillRight, barY + barHeight, 0xFFFFFFFF);
    }

    private static void drawBar(GuiGraphics guiGraphics, Font font, float currentHealth, int x, int y) {
        int maxHealth = 6;
        int maxBarWidth = 60;
        int barHeight = 7;
        int borderThickness = 1;

        int colorNormal = 0xFFb20f0f;
        int colorCritico = 0xFFb20f0f;
        int colorBorde = 0xFF000000;

        int currentBarWidth = (int) ((currentHealth / maxHealth) * maxBarWidth);

        int barOffsetX = 35;
        int barStartX = x + barOffsetX;

        guiGraphics.fill(barStartX - borderThickness, y - borderThickness, barStartX + maxBarWidth + borderThickness, y + barHeight + borderThickness, colorBorde);
        guiGraphics.fill(barStartX, y, barStartX + maxBarWidth, y + barHeight, 0xFF000000);

        int barColor = currentHealth <= 4.0f ? colorCritico : colorNormal;
        guiGraphics.fill(barStartX, y, barStartX + currentBarWidth, y + barHeight, barColor);

        String hpText = (int)currentHealth + "/" + maxHealth;
        guiGraphics.drawString(font, hpText, barStartX + maxBarWidth + 5, y, 0xFFFFFF, true);
    }
}