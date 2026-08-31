package net.saullmc.pezntz.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.saullmc.pezntz.item.custom.Adrenalina;
import net.saullmc.pezntz.item.custom.Vendas;
import org.joml.Matrix4f;

public class JeringaOverlay {

    public static final IGuiOverlay HUD_HEALING_TIMER = (ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) -> {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null) return;

        ItemStack useItem = player.getUseItem();

        if (useItem.getItem() instanceof Adrenalina || useItem.getItem() instanceof Vendas) {
            int ticksRemaining = player.getUseItemRemainingTicks();
            int maxDuration = useItem.getUseDuration();

            if (ticksRemaining > 0) {
                int secondsRemaining = (int) Math.ceil(ticksRemaining / 20.0f);

                int ticksUsed = maxDuration - ticksRemaining;
                float progress = (float) ticksUsed / maxDuration;

                int x = screenWidth / 2;
                int y = screenHeight - 65;
                int radius = 12;

                Matrix4f matrix = guiGraphics.pose().last().pose();
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder bufferbuilder = tesselator.getBuilder();

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                RenderSystem.disableCull();


                bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
                bufferbuilder.vertex(matrix, x, y, 0).color(0, 0, 0, 150).endVertex();
                for (int i = 0; i <= 360; i += 10) {
                    double angle = Math.toRadians(i - 90);
                    float dx = (float) Math.cos(angle) * radius;
                    float dy = (float) Math.sin(angle) * radius;
                    bufferbuilder.vertex(matrix, x + dx, y + dy, 0).color(0, 0, 0, 150).endVertex();
                }
                tesselator.end();

                int progressAngle = (int) (360 * progress);
                if (progressAngle > 0) {
                    bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
                    bufferbuilder.vertex(matrix, x, y, 0).color(255, 255, 255, 255).endVertex();
                    for (int i = 0; i <= progressAngle; i += 5) {
                        double angle = Math.toRadians(i - 90);
                        float dx = (float) Math.cos(angle) * radius;
                        float dy = (float) Math.sin(angle) * radius;
                        bufferbuilder.vertex(matrix, x + dx, y + dy, 0).color(255, 255, 255, 255).endVertex();
                    }
                    tesselator.end();

                    int innerRadius = radius - 2;
                    bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
                    bufferbuilder.vertex(matrix, x, y, 0).color(0, 0, 0, 150).endVertex();
                    for (int i = 0; i <= 360; i += 10) {
                        double angle = Math.toRadians(i - 90);
                        float dx = (float) Math.cos(angle) * innerRadius;
                        float dy = (float) Math.sin(angle) * innerRadius;
                        bufferbuilder.vertex(matrix, x + dx, y + dy, 0).color(0, 0, 0, 150).endVertex();
                    }
                    tesselator.end();

                    RenderSystem.lineWidth(1.0f);
                }

                RenderSystem.enableCull();
                RenderSystem.disableBlend();

                String text = String.valueOf(secondsRemaining);
                int textWidth = minecraft.font.width(text);
                guiGraphics.drawString(minecraft.font, text, x - (textWidth / 2), y - 4, 0xFFFFFF, true);
            }
        }
    };
}
