package net.saullmc.pezntz.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.saullmc.pezntz.menu.CofreCandadoMenu;

public class CofreCandadoScreen extends AbstractContainerScreen<CofreCandadoMenu> {

    private static final ResourceLocation TEXTURA =
            new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");

    private final int filas;

    public CofreCandadoScreen(CofreCandadoMenu menu, Inventory inventarioJugador, Component titulo) {
        super(menu, inventarioJugador, titulo);

        this.filas = menu.getFilas();
        this.imageWidth = CofreCandadoMenu.ANCHO_GUI;
        this.imageHeight = menu.getAltoGui();
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(g);
        super.render(g, mouseX, mouseY, partialTick);
        this.renderTooltip(g, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        g.blit(TEXTURA, x, y, 0, 0, this.imageWidth, this.filas * 18 + 17);
        g.blit(TEXTURA, x, y + this.filas * 18 + 17, 0, 126, this.imageWidth, 96);
    }
}