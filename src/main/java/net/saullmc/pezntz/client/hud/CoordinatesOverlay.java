package net.saullmc.pezntz.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;

public class CoordinatesOverlay {

    private static final Minecraft MC = Minecraft.getInstance();

    private static final float SCALE = 0.8F;
    private static final int HUD_Y = 48;
    private static final int COLOR = 0xFFFFFFFF;

    public static void render(GuiGraphics guiGraphics) {

        if (MC.player == null)
            return;

        Font font = MC.font;

        int screenWidth = MC.getWindow().getGuiScaledWidth();

        int centerX = (int) ((screenWidth / SCALE) / 2);

        int y = (int) (HUD_Y / SCALE);

        BlockPos pos = MC.player.blockPosition();

        String coords = String.format(
                "X: %d   Y: %d   Z: %d",
                pos.getX(),
                pos.getY(),
                pos.getZ()
        );

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(SCALE, SCALE, 1.0F);

        guiGraphics.drawString(
                font,
                coords,
                centerX - font.width(coords) / 2,
                y,
                COLOR,
                true
        );

        guiGraphics.pose().popPose();
    }
}