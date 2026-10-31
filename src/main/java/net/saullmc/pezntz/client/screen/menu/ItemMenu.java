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

public class ItemMenu {

    public static final int SIN_PONER = Integer.MIN_VALUE;

    public static final String TEXTURA_POR_DEFECTO = "pezntz:textures/gui/receta.png";

    public static final int TEXTURA_POR_DEFECTO_ANCHO = 16;
    public static final int TEXTURA_POR_DEFECTO_ALTO = 16;

    public static final float TEXTURA_POR_DEFECTO_ESCALA = 3.0F;

    public static final int TEXTURA_POR_DEFECTO_MARGEN_X = 0;
    public static final int TEXTURA_POR_DEFECTO_MARGEN_Y = 2;

    private final ItemStack stack;

    private int x = SIN_PONER;
    private int y = SIN_PONER;

    private float escala = -1.0F;

    private ResourceLocation imagen = null;
    private int imagenAncho = 0;
    private int imagenAlto = 0;
    private int imagenTexturaAncho = 0;
    private int imagenTexturaAlto = 0;
    private float imagenEscala = 1.0F;
    private int imagenOffsetX = 0;
    private int imagenOffsetY = 0;
    private int imagenMargenX = TEXTURA_POR_DEFECTO_MARGEN_X;
    private int imagenMargenY = TEXTURA_POR_DEFECTO_MARGEN_Y;

    private boolean imagenEscalaPuesta = false;

    private final List<ItemEnImagen> imagenItems = new ArrayList<>();

    public ItemMenu(Item item) {
        this(new ItemStack(item));
    }

    public ItemMenu(ItemStack stack) {
        this.stack = stack;
    }

    public ItemMenu en(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public ItemMenu escala(float escala) {
        this.escala = escala;
        return this;
    }

    public ItemMenu imagen(String ruta, int ancho, int alto) {
        this.imagen = new ResourceLocation(ruta);
        this.imagenAncho = ancho;
        this.imagenAlto = alto;
        this.imagenTexturaAncho = ancho;
        this.imagenTexturaAlto = alto;
        return this;
    }

    public ItemMenu imagenPng(int texturaAncho, int texturaAlto) {
        this.imagenTexturaAncho = texturaAncho;
        this.imagenTexturaAlto = texturaAlto;
        return this;
    }

    public ItemMenu imagenEscala(float escala) {
        this.imagenEscala = escala;
        this.imagenEscalaPuesta = true;
        return this;
    }

    public ItemMenu imagenMueve(int x, int y) {
        this.imagenOffsetX = x;
        this.imagenOffsetY = y;
        return this;
    }

    public ItemMenu imagenMargen(int x, int y) {
        this.imagenMargenX = x;
        this.imagenMargenY = y;
        return this;
    }

    public ItemMenu imagenItem(Item item, int x, int y) {
        return this.imagenItem(new ItemStack(item), x, y, 1.0F);
    }

    public ItemMenu imagenItem(Item item, int x, int y, float escala) {
        return this.imagenItem(new ItemStack(item), x, y, escala);
    }

    public ItemMenu imagenItem(ItemStack stack, int x, int y) {
        return this.imagenItem(stack, x, y, 1.0F);
    }

    public ItemMenu imagenItem(ItemStack stack, int x, int y, float escala) {
        this.imagenItems.add(new ItemEnImagen(stack, x, y, escala));
        return this;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public boolean tienePosicion() {
        return this.x != SIN_PONER && this.y != SIN_PONER;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public float getEscala(float porDefecto) {
        return this.escala < 0.0F ? porDefecto : this.escala;
    }

    public Optional<TooltipComponent> getImagenTooltip() {
        boolean porDefecto = (this.imagen == null);

        if (porDefecto && this.imagenItems.isEmpty()) return Optional.empty();

        ResourceLocation textura = porDefecto
                ? new ResourceLocation(TEXTURA_POR_DEFECTO)
                : this.imagen;

        int ancho = porDefecto ? TEXTURA_POR_DEFECTO_ANCHO : this.imagenAncho;
        int alto = porDefecto ? TEXTURA_POR_DEFECTO_ALTO : this.imagenAlto;
        int texturaAncho = porDefecto ? TEXTURA_POR_DEFECTO_ANCHO : this.imagenTexturaAncho;
        int texturaAlto = porDefecto ? TEXTURA_POR_DEFECTO_ALTO : this.imagenTexturaAlto;

        float escala = this.imagenEscalaPuesta
                ? this.imagenEscala
                : (porDefecto ? TEXTURA_POR_DEFECTO_ESCALA : 1.0F);

        return Optional.<TooltipComponent>of(new ImagenTooltip(
                textura,
                ancho, alto,
                texturaAncho, texturaAlto,
                escala,
                this.imagenOffsetX, this.imagenOffsetY,
                this.imagenMargenX, this.imagenMargenY,
                this.imagenItems));
    }
}