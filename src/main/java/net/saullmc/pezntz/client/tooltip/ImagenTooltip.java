package net.saullmc.pezntz.client.tooltip;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record ImagenTooltip(ResourceLocation textura,
                            int ancho, int alto,
                            int texturaAncho, int texturaAlto,
                            float escala,
                            int offsetX, int offsetY,
                            int margenX, int margenY) implements TooltipComponent {

    public int anchoFinal() {
        return Math.max(1, Math.round(this.ancho * this.escala));
    }

    public int altoFinal() {
        return Math.max(1, Math.round(this.alto * this.escala));
    }
}