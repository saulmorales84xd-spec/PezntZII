package net.saullmc.pezntz.client.screen.menu;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class ItemMenu {

    public static final int SIN_PONER = Integer.MIN_VALUE;

    public static final String TEXTURA_POR_DEFECTO = ImagenMenu.TEXTURA_POR_DEFECTO;

    public static final int TEXTURA_POR_DEFECTO_ANCHO = ImagenMenu.TEXTURA_POR_DEFECTO_ANCHO;
    public static final int TEXTURA_POR_DEFECTO_ALTO = ImagenMenu.TEXTURA_POR_DEFECTO_ALTO;

    public static final float TEXTURA_POR_DEFECTO_ESCALA = ImagenMenu.TEXTURA_POR_DEFECTO_ESCALA;

    public static final int TEXTURA_POR_DEFECTO_MARGEN_X = ImagenMenu.TEXTURA_POR_DEFECTO_MARGEN_X;
    public static final int TEXTURA_POR_DEFECTO_MARGEN_Y = ImagenMenu.TEXTURA_POR_DEFECTO_MARGEN_Y;

    public static final float ITEMS_IMAGEN_ESCALA = 0.28F;

    private final ItemStack stack;

    private final ImagenMenu img = new ImagenMenu();

    private int x = SIN_PONER;
    private int y = SIN_PONER;

    private float escala = -1.0F;

    public static final String ITEM_ARMA = "tacz:modern_kinetic_gun";
    public static final String ITEM_ACCESORIO = "tacz:attachment";
    public static final String ITEM_MUNICION = "tacz:ammo";
    public static final String ITEM_CAJAS = "tacz:ammo_box";

    public static final String MODO_DISPARO = "SEMI";

    public static final int CAJA_HIERRO = 0;
    public static final int CAJA_ORO = 1;
    public static final int CAJA_DIAMANTE = 2;

    public static ItemMenu arma(String id) {
        return new ItemMenu(stackArma(id));
    }

    public static ItemMenu accesorio(String id) {
        return new ItemMenu(stackAccesorio(id));
    }

    public static ItemMenu municion(String calibre, int cantidad) {
        return new ItemMenu(stackMunicion(calibre, cantidad));
    }

    public static ItemMenu caja(String calibre, int nivel, int balas) {
        return new ItemMenu(stackCaja(calibre, nivel, balas));
    }

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

    public static ItemStack stackCaja(String calibre, int nivel, int balas) {
        int n = Math.max(CAJA_HIERRO, Math.min(CAJA_DIAMANTE, nivel));

        return construir(ITEM_CAJAS,
                "{AmmoId:\"tacz:" + calibre + "\",AmmoCount:" + Math.max(1, balas) + ",Level:" + n + "}", 1);
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
        this.img.itemsEscala(ITEMS_IMAGEN_ESCALA);
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
        this.img.imagen(ruta, ancho, alto);
        return this;
    }

    public ItemMenu imagenPng(int texturaAncho, int texturaAlto) {
        this.img.png(texturaAncho, texturaAlto);
        return this;
    }

    public ItemMenu imagenEscala(float escala) {
        this.img.escala(escala);
        return this;
    }

    public ItemMenu imagenMueve(int x, int y) {
        this.img.mueve(x, y);
        return this;
    }

    public ItemMenu imagenMargen(int x, int y) {
        this.img.margen(x, y);
        return this;
    }

    public ItemMenu imagenItemEscala(float escala) {
        this.img.itemsEscala(escala);
        return this;
    }

    public ItemMenu imagenItem(Item item, float x, float y) {
        return this.imagenItem(new ItemStack(item), x, y, 1.0F);
    }

    public ItemMenu imagenItem(Item item, float x, float y, float escala) {
        return this.imagenItem(new ItemStack(item), x, y, escala);
    }

    public ItemMenu imagenItem(ItemStack stack, float x, float y) {
        return this.imagenItem(stack, x, y, 1.0F);
    }

    public ItemMenu imagenItem(ItemStack stack, float x, float y, float escala) {
        this.img.item(stack, x, y, escala);
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
        return this.img.construir();
    }
}