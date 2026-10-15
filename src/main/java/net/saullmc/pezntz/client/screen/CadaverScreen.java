package net.saullmc.pezntz.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.saullmc.pezntz.block.entity.CadaverEntity;
import net.saullmc.pezntz.menu.CadaverMenu;

public class CadaverScreen extends AbstractContainerScreen<CadaverMenu> {

    private static final int FILAS = CadaverEntity.FILAS;

    private static final ResourceLocation TEXTURA =
            new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");

    public CadaverScreen(CadaverMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = CadaverMenu.ANCHO_GUI;
        this.imageHeight = CadaverMenu.ALTO_GUI;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(TEXTURA, x, y, 0, 0, this.imageWidth, FILAS * 18 + 17);
        guiGraphics.blit(TEXTURA, x, y + FILAS * 18 + 17, 0, 126, this.imageWidth, 96);
    }

}