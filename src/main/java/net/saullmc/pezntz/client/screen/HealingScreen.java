package net.saullmc.pezntz.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.saullmc.pezntz.capability.BodyHealthProvider;
import net.saullmc.pezntz.network.HealSpecificPartPacket;
import net.saullmc.pezntz.network.NetworkHandler;

public class HealingScreen extends Screen {

    private static final ResourceLocation TEXTURE = new ResourceLocation("pezntz", "textures/gui/menu_vida.png");

    private final int guiWidth = 192;
    private final int guiHeight = 128;

    private final int buttonWidth = 42;
    private final int buttonHeight = 15;
    private final int spacingY = 24;

    private final int renderOffsetX = 149;
    private final int renderOffsetY = 99;
    private final int renderScale = 38;

    private String holdingPart = null;
    private int holdTicks = 0;
    private final int MAX_HOLD_TICKS = 60;

    private String lastHoveredPart = null;

    public HealingScreen() {
        super(Component.literal("Menu Medico"));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int leftPos = (this.width - guiWidth) / 2;
            int topPos = (this.height - guiHeight) / 2;

            int startX = leftPos + 33;
            int startY = topPos + 20;

            if (isHovering(mouseX, mouseY, startX, startY)) holdingPart = "head";
            else if (isHovering(mouseX, mouseY, startX, startY + spacingY)) holdingPart = "body";
            else if (isHovering(mouseX, mouseY, startX, startY + spacingY * 2)) holdingPart = "arms";
            else if (isHovering(mouseX, mouseY, startX, startY + spacingY * 3)) holdingPart = "legs";

            if (holdingPart != null) {
                holdTicks = 0;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            holdingPart = null;
            holdTicks = 0;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void tick() {
        super.tick();
        if (holdingPart != null) {
            holdTicks++;
            if (holdTicks >= MAX_HOLD_TICKS) {
                NetworkHandler.sendToServer(new HealSpecificPartPacket(holdingPart));
                holdingPart = null;
                holdTicks = 0;
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();

        this.renderBackground(guiGraphics);

        int leftPos = (this.width - guiWidth) / 2;
        int topPos = (this.height - guiHeight) / 2;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, guiWidth, guiHeight, guiWidth, guiHeight);
        RenderSystem.disableBlend();

        if (mc.player != null) {
            int renderX = leftPos + renderOffsetX;
            int renderY = topPos + renderOffsetY;

            float lookX = (float) renderX - mouseX;
            float lookY = (float) (renderY - (renderScale * 1.6f)) - mouseY;

            InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, renderX, renderY, renderScale, lookX, lookY, mc.player);
        }

        int startX = leftPos + 33;
        int startY = topPos + 20;

        String currentlyHovering = null;
        if (isHovering(mouseX, mouseY, startX, startY)) currentlyHovering = "head";
        else if (isHovering(mouseX, mouseY, startX, startY + spacingY)) currentlyHovering = "body";
        else if (isHovering(mouseX, mouseY, startX, startY + spacingY * 2)) currentlyHovering = "arms";
        else if (isHovering(mouseX, mouseY, startX, startY + spacingY * 3)) currentlyHovering = "legs";

        if (currentlyHovering != null && !currentlyHovering.equals(lastHoveredPart)) {
            mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.5F));
        }
        lastHoveredPart = currentlyHovering;

        // 5. DIBUJA LOS BOTONES Y TEXTOS
        drawCustomButton(guiGraphics, "Cabeza", startX, startY, mouseX, mouseY, "head");
        drawCustomButton(guiGraphics, "Cuerpo", startX, startY + spacingY, mouseX, mouseY, "body");
        drawCustomButton(guiGraphics, "Brazos", startX, startY + spacingY * 2, mouseX, mouseY, "arms");
        drawCustomButton(guiGraphics, "Piernas", startX, startY + spacingY * 3, mouseX, mouseY, "legs");

        if (mc.player != null) {
            mc.player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {

                int textOffsetX = buttonWidth + 8;
                int textOffsetY = 0;

                int textX = startX + textOffsetX;
                int baseTextY = startY + ((buttonHeight - mc.font.lineHeight) / 2) + textOffsetY;

                guiGraphics.drawString(mc.font, (int)cap.getHead() + "/6", textX, baseTextY, 0xFFFFFF, false);
                guiGraphics.drawString(mc.font, (int)cap.getBody() + "/6", textX, baseTextY + spacingY, 0xFFFFFF, false);
                guiGraphics.drawString(mc.font, (int)cap.getArms() + "/6", textX, baseTextY + spacingY * 2, 0xFFFFFF, false);
                guiGraphics.drawString(mc.font, (int)cap.getLegs() + "/6", textX, baseTextY + spacingY * 3, 0xFFFFFF, false);
            });
        }
    }

    private void drawCustomButton(GuiGraphics guiGraphics, String text, int x, int y, int mouseX, int mouseY, String part) {
        boolean hovered = isHovering(mouseX, mouseY, x, y);

        if (hovered) {
            guiGraphics.fill(x, y, x + buttonWidth, y + 1, 0xFFFFFFFF);
            guiGraphics.fill(x, y + buttonHeight - 1, x + buttonWidth, y + buttonHeight, 0xFFFFFFFF);
            guiGraphics.fill(x, y, x + 1, y + buttonHeight, 0xFFFFFFFF);
            guiGraphics.fill(x + buttonWidth - 1, y, x + buttonWidth, y + buttonHeight, 0xFFFFFFFF);
        }

        guiGraphics.pose().pushPose();
        float fontScale = 0.8f;
        guiGraphics.pose().scale(fontScale, fontScale, 1.0f);

        int textWidth = Minecraft.getInstance().font.width(text);
        int drawX = (int) ((x + (buttonWidth - textWidth * fontScale) / 2) / fontScale);
        int drawY = (int) ((y + (buttonHeight - Minecraft.getInstance().font.lineHeight * fontScale) / 2) / fontScale);

        guiGraphics.drawString(Minecraft.getInstance().font, text, drawX, drawY, 0xFFFFFF, false);
        guiGraphics.pose().popPose();

        if (holdingPart != null && holdingPart.equals(part)) {
            float progress = (float) holdTicks / MAX_HOLD_TICKS;
            int lineWidth = (int) (progress * buttonWidth);
            guiGraphics.fill(x, y + buttonHeight, x + lineWidth, y + buttonHeight + 1, 0xFFFFFFFF);
        }
    }

    private boolean isHovering(double mouseX, double mouseY, int x, int y) {
        return mouseX >= x && mouseX <= x + buttonWidth && mouseY >= y && mouseY <= y + buttonHeight;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}