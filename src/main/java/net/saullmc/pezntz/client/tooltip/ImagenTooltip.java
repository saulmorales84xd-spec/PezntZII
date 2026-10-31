package net.saullmc.pezntz.client.tooltip;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.List;

public record ImagenTooltip(ResourceLocation textura,
                            int ancho, int alto,
                            int texturaAncho, int texturaAlto,
                            float escala,
                            int offsetX, int offsetY,
                            int margenX, int margenY,
                            List<ItemEnImagen> items) implements TooltipComponent {

    public int anchoFinal() {
        return Math.max(1, Math.round(this.ancho * this.escala));
    }

    public int altoFinal() {
        return Math.max(1, Math.round(this.alto * this.escala));
    }
}