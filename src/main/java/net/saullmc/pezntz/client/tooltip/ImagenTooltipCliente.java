package net.saullmc.pezntz.client.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import net.saullmc.pezntz.client.screen.menu.MenuSeccionScreen;

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

        dibujarItems(font, destinoX, destinoY, g);
    }

    private void dibujarItems(Font font, int destinoX, int destinoY, GuiGraphics g) {
        float escalaImagen = this.datos.escala();
        float escalaItems = this.datos.itemsEscala();

        for (ItemEnImagen item : this.datos.items()) {
            if (item.stack().isEmpty()) continue;

            float escala = item.escala() * escalaImagen * escalaItems;

            float itemX = destinoX + item.x() * escalaImagen;
            float itemY = destinoY + item.y() * escalaImagen;

            g.pose().pushPose();

            g.pose().translate(itemX, itemY, 0.0F);
            g.pose().scale(escala, escala, 1.0F);

            g.renderItem(item.stack(), 0, 0);

            MenuSeccionScreen.decorar(g, font, item.stack(), item.barra(), true);

            g.pose().popPose();
        }
    }
}