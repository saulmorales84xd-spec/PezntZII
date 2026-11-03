package net.saullmc.pezntz.client.screen.menu;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
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

    public static final float ITEMS_IMAGEN_ESCALA = 0.3F;

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

    private float imagenItemsEscala = ITEMS_IMAGEN_ESCALA;

    private final List<ItemEnImagen> imagenItems = new ArrayList<>();

    public static final String ITEM_ARMA = "tacz:modern_kinetic_gun";
    public static final String ITEM_ACCESORIO = "tacz:attachment";
    public static final String ITEM_MUNICION = "tacz:ammo";
    public static final String ITEM_CAJAS = "tacz:ammo_box";

    public static final String MODO_DISPARO = "SEMI";

    /** Un arma del pack: "ak47", "m4a1", "deagle"... */
    public static ItemMenu arma(String id) {
        return new ItemMenu(construir(ITEM_ARMA,
                "{GunId:\"tacz:" + id + "\",GunFireMode:\"" + MODO_DISPARO + "\"}", 1));
    }

    /** Una mira, canion, culata, empuniadura o cargador: "scope_vudu", "grip_cobra"... */
    public static ItemMenu accesorio(String id) {
        return new ItemMenu(construir(ITEM_ACCESORIO, "{AttachmentId:\"tacz:" + id + "\"}", 1));
    }

    /** Un calibre: "9mm", "12g", "45acp"... */
    public static ItemMenu municion(String calibre, int cantidad) {
        return new ItemMenu(construir(ITEM_MUNICION, "{AmmoId:\"tacz:" + calibre + "\"}", cantidad));
    }

    public static final int CAJA_HIERRO = 0;
    public static final int CAJA_ORO = 1;
    public static final int CAJA_DIAMANTE = 2;

    public static ItemMenu caja(String calibre, int nivel, int balas) {
        return new ItemMenu(stackCaja(calibre, nivel, balas));
    }

    public static ItemStack stackCaja(String calibre, int nivel, int balas) {
        int n = Math.max(CAJA_HIERRO, Math.min(CAJA_DIAMANTE, nivel));

        return construir(ITEM_CAJAS,
                "{AmmoId:\"tacz:" + calibre + "\",AmmoCount:" + Math.max(1, balas) + ",Level:" + n + "}", 1);
    }

    /** Lo mismo pero para meterlo DENTRO del png de un tooltip con .imagenItem(...). */
    public static ItemStack stackArma(String id) {
        return construir(ITEM_ARMA,
                "{GunId:\"tacz:" + id + "\",GunFireMode:\"" + MODO_DISPARO + "\"}", 1);
    }

    public static ItemStack stackAccesorio(String id) {
        return construir(ITEM_ACCESORIO, "{AttachmentId:\"tacz:" + id + "\"}", 1);
    }

    public static ItemStack stackMunicion(String calibre, int cantidad) {
        return construir(ITEM_MUNICION, "{AmmoId:\"tacz:" + calibre + "\"}", cantidad);
    }

    private static ItemStack construir(String item, String nbt, int cantidad) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation(item));
        if (base == null) return ItemStack.EMPTY;

        ItemStack stack = new ItemStack(base, Math.max(1, cantidad));

        try {
            CompoundTag tag = TagParser.parseTag(nbt);
            stack.setTag(tag);
        } catch (CommandSyntaxException e) {
        }

        return stack;
    }

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

    public ItemMenu imagenItemEscala(float escala) {
        this.imagenItemsEscala = escala;
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
                this.imagenItemsEscala,
                this.imagenItems));
    }
}