package net.saullmc.pezntz.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class CompassOverlay {

    private static final Minecraft MC = Minecraft.getInstance();

    private static final String[] DIRECTIONS = {
            "N",
            "NE",
            "E",
            "SE",
            "S",
            "SW",
            "W",
            "NW"
    };

    private static final float[] ANGLES = {
            180F,
            -135F,
            -90F,
            -45F,
            0F,
            45F,
            90F,
            135F
    };

    private static final int SPACING = 60;

    public static void render(GuiGraphics guiGraphics, float partialTicks) {

        if (MC.player == null)
            return;

        guiGraphics.pose(). pushPose();
        float scale = 0.8F;
        guiGraphics.pose().scale(scale, scale, 1.0F);

        Font font = MC.font;

        int screenWidth = MC.getWindow().getGuiScaledWidth();

        int centerX = (int)((screenWidth / scale) / 2);

        int y = (int) (25 / scale);

        float yaw = MC.player.getViewYRot(partialTicks);

        yaw = normalizeYaw(yaw);

        for (int i = 0; i < DIRECTIONS.length; i++) {

            float diff = angleDifference(yaw, ANGLES[i]);

            float pixels = diff * (SPACING / 45F);

            int x = (int)(centerX + pixels);

            if (x < centerX - 180 || x > centerX + 180)
                continue;

            int color;

            if (DIRECTIONS[i].equals("N"))
                color = 0xFFFF5555;
            else
                color = 0xFFFFFFFF;

            String texto = DIRECTIONS[i];

            int drawX = x - font.width(texto) / 2;

            int drawY = y;

            if (texto.equals("N")) {

                guiGraphics.drawString(
                        font,
                        texto,
                        drawX,
                        drawY - 1,
                        0xFFFFFFFF,
                        true
                );

            } else {

                guiGraphics.drawString(
                        font,
                        texto,
                        drawX,
                        drawY,
                        color,
                        true
                );

            }
        }

        for (int degree = 0; degree < 360; degree += 5) {

            float diff = angleDifference(yaw, degree);

            float pixels = diff * (SPACING / 45F);

            int x = (int)(centerX + pixels);

            if (x < centerX - 180 || x > centerX + 180)
                continue;

            int altura;

            if (degree % 45 == 0) {
                altura = 8;
            } else if (degree % 15 == 0) {
                altura = 6;
            } else {
                altura = 4;
            }

            guiGraphics.fill(
                    x,
                    y + 14,
                    x + 1,
                    y + 14 + altura,
                    0xAAFFFFFF
            );
        }

        guiGraphics.drawString(
                font,
                "▲",
                centerX - font.width("▲") / 2,
                20,
                0xFFFFFFFF,
                true
        );

        guiGraphics.pose().popPose();
    }

    private static float normalizeYaw(float yaw) {

        yaw %= 360F;

        if (yaw < 0)
            yaw += 360F;

        return yaw;
    }

    private static float angleDifference(float playerYaw, float targetYaw) {

        float diff = targetYaw - playerYaw;

        while (diff < -180F)
            diff += 360F;

        while (diff > 180F)
            diff -= 360F;

        return diff;
    }

}