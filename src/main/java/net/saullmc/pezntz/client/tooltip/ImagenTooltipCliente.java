package net.saullmc.pezntz.client.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public class ImagenTooltipCliente implements ClientTooltipComponent {

    private final ImagenTooltip datos;

    public ImagenTooltipCliente(ImagenTooltip datos) {
        this.datos = datos;
    }

    @Override
    public int getWidth(Font font) {
        return this.datos.anchoFinal() + this.datos.margenX() * 2;
    }

    @Override
    public int getHeight() {
        return this.datos.altoFinal() + this.datos.margenY() * 2;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics g) {
        int destinoX = x + this.datos.margenX() + this.datos.offsetX();
        int destinoY = y + this.datos.margenY() + this.datos.offsetY();

        g.blit(this.datos.textura(),
                destinoX, destinoY,
                this.datos.anchoFinal(), this.datos.altoFinal(),
                0.0F, 0.0F,
                this.datos.ancho(), this.datos.alto(),
                this.datos.texturaAncho(), this.datos.texturaAlto());
    }
}