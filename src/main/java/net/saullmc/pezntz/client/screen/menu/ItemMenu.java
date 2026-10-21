package net.saullmc.pezntz.client.screen.menu;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.saullmc.pezntz.client.tooltip.ImagenTooltip;

import java.util.Optional;

public class ItemMenu {

    public static final int SIN_PONER = Integer.MIN_VALUE;

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
    private int imagenMargenX = 0;
    private int imagenMargenY = 2;

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
        if (this.imagen == null) return Optional.empty();

        return Optional.<TooltipComponent>of(new ImagenTooltip(
                this.imagen,
                this.imagenAncho, this.imagenAlto,
                this.imagenTexturaAncho, this.imagenTexturaAlto,
                this.imagenEscala,
                this.imagenOffsetX, this.imagenOffsetY,
                this.imagenMargenX, this.imagenMargenY));
    }
}