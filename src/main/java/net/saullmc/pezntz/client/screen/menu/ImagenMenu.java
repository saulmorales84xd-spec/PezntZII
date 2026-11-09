package net.saullmc.pezntz.client.screen.menu;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.saullmc.pezntz.client.tooltip.ImagenTooltip;
import net.saullmc.pezntz.client.tooltip.ItemEnImagen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImagenMenu {

    public static final String TEXTURA_POR_DEFECTO = "pezntz:textures/gui/receta.png";

    public static final int TEXTURA_POR_DEFECTO_ANCHO = 16;
    public static final int TEXTURA_POR_DEFECTO_ALTO = 16;

    public static final float TEXTURA_POR_DEFECTO_ESCALA = 3.0F;

    public static final int TEXTURA_POR_DEFECTO_MARGEN_X = 0;
    public static final int TEXTURA_POR_DEFECTO_MARGEN_Y = 2;

    public static final float ITEMS_IMAGEN_ESCALA = 1.0F;

    private ResourceLocation imagen = null;

    private int ancho = 0;
    private int alto = 0;

    private int texturaAncho = 0;
    private int texturaAlto = 0;

    private float escala = 1.0F;
    private boolean escalaPuesta = false;

    private int offsetX = 0;
    private int offsetY = 0;

    private int margenX = TEXTURA_POR_DEFECTO_MARGEN_X;
    private int margenY = TEXTURA_POR_DEFECTO_MARGEN_Y;

    private float itemsEscala = ITEMS_IMAGEN_ESCALA;

    private final List<ItemEnImagen> items = new ArrayList<>();

    public void imagen(String ruta, int ancho, int alto) {
        this.imagen = new ResourceLocation(ruta);
        this.ancho = ancho;
        this.alto = alto;
        this.texturaAncho = ancho;
        this.texturaAlto = alto;
    }

    public void png(int texturaAncho, int texturaAlto) {
        this.texturaAncho = texturaAncho;
        this.texturaAlto = texturaAlto;
    }

    public void escala(float escala) {
        this.escala = escala;
        this.escalaPuesta = true;
    }

    public void mueve(int x, int y) {
        this.offsetX = x;
        this.offsetY = y;
    }

    public void margen(int x, int y) {
        this.margenX = x;
        this.margenY = y;
    }

    public void itemsEscala(float escala) {
        this.itemsEscala = escala;
    }

    public static final boolean BARRA_POR_DEFECTO = false;

    public void item(Item item, float x, float y, float escala) {
        this.item(new ItemStack(item), x, y, escala);
    }

    public void item(ItemStack stack, float x, float y, float escala) {
        this.items.add(new ItemEnImagen(stack, x, y, escala, BARRA_POR_DEFECTO));
    }

    public boolean vacia() {
        return this.imagen == null && this.items.isEmpty();
    }

    public Optional<TooltipComponent> construir() {
        boolean porDefecto = (this.imagen == null);

        if (porDefecto && this.items.isEmpty()) return Optional.empty();

        ResourceLocation textura = porDefecto
                ? new ResourceLocation(TEXTURA_POR_DEFECTO)
                : this.imagen;

        int ancho = porDefecto ? TEXTURA_POR_DEFECTO_ANCHO : this.ancho;
        int alto = porDefecto ? TEXTURA_POR_DEFECTO_ALTO : this.alto;

        int texturaAncho = porDefecto ? TEXTURA_POR_DEFECTO_ANCHO : this.texturaAncho;
        int texturaAlto = porDefecto ? TEXTURA_POR_DEFECTO_ALTO : this.texturaAlto;

        float escala = this.escalaPuesta
                ? this.escala
                : (porDefecto ? TEXTURA_POR_DEFECTO_ESCALA : 1.0F);

        return Optional.<TooltipComponent>of(new ImagenTooltip(
                textura,
                ancho, alto,
                texturaAncho, texturaAlto,
                escala,
                this.offsetX, this.offsetY,
                this.margenX, this.margenY,
                this.itemsEscala,
                this.items));
    }
}