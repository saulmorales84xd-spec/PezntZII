package net.saullmc.pezntz.client.screen.menu;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.saullmc.pezntz.item.ModTooltips;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public static final boolean BARRA_POR_DEFECTO = true;
    public static final boolean CANTIDAD_POR_DEFECTO = true;

    public static final int COLOR_NOMBRE = 0xFFFFFF;
    public static final int COLOR_DESCRIPCION = 0xAAAAAA;

    private final ItemStack stack;

    private final ImagenMenu img = new ImagenMenu();

    private final MenuCrafteo menu = new MenuCrafteo();

    private int x = SIN_PONER;
    private int y = SIN_PONER;

    private float escala = -1.0F;

    private boolean barra = BARRA_POR_DEFECTO;
    private boolean cantidad = CANTIDAD_POR_DEFECTO;

    private String nombre = null;
    private int colorNombre = COLOR_NOMBRE;
    private boolean conNombre = true;

    private boolean tooltipPropio = false;
    private final List<String> lineas = new ArrayList<>();
    private List<Component> tooltipListo = null;

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
        return new ItemMenu(stackMunicion(calibre, cantidad)).sinBarra();
    }

    public static ItemMenu caja(String calibre, int nivel, int balas) {
        return new ItemMenu(stackCaja(calibre, nivel, balas)).sinBarra();
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

    public ItemMenu sinBarra() {
        this.barra = false;
        return this;
    }

    public ItemMenu barra(boolean mostrar) {
        this.barra = mostrar;
        return this;
    }

    public ItemMenu sinCantidad() {
        this.cantidad = false;
        return this;
    }

    public ItemMenu cantidad(boolean mostrar) {
        this.cantidad = mostrar;
        return this;
    }

    public ItemMenu nombre(String texto) {
        return this.nombre(texto, COLOR_NOMBRE);
    }

    public ItemMenu nombre(String texto, int color) {
        this.nombre = texto;
        this.colorNombre = color;
        this.tooltipListo = null;
        return this;
    }

    public ItemMenu tooltip(String... lineas) {
        this.tooltipPropio = true;
        this.lineas.addAll(Arrays.asList(lineas));
        this.tooltipListo = null;
        return this;
    }

    public ItemMenu descripcion(String texto) {
        return this.descripcion(texto, COLOR_DESCRIPCION);
    }

    public ItemMenu descripcion(String texto, int color) {
        this.tooltipPropio = true;
        this.lineas.add(texto.isEmpty() ? "" : ModTooltips.hex(color) + texto);
        this.tooltipListo = null;
        return this;
    }

    public ItemMenu sinTooltip() {
        this.tooltipPropio = true;
        this.tooltipListo = null;
        return this;
    }

    public ItemMenu sinNombre() {
        this.conNombre = false;
        this.tooltipListo = null;
        return this;
    }

    public ItemMenu conMenu() {
        this.menu.forzar();
        return this;
    }

    public ItemMenu sinMenu() {
        this.menu.quitar();
        return this;
    }

    public ItemMenu menuEscala(float escala) {
        this.menu.escala(escala);
        return this;
    }

    public ItemMenu menuNombre(String texto) {
        return this.menuNombre(texto, COLOR_NOMBRE);
    }

    public ItemMenu menuNombre(String texto, int color) {
        this.menu.nombre(texto, color);
        return this;
    }

    public ItemMenu menuNombreMueve(float x, float y) {
        this.menu.nombreMueve(x, y);
        return this;
    }

    public ItemMenu menuNombreEscala(float escala) {
        this.menu.nombreEscala(escala);
        return this;
    }

    public ItemMenu menuItemPrincipal(float x, float y, float tamanio) {
        this.menu.itemPrincipal(x, y, tamanio);
        return this;
    }

    public ItemMenu menuSinItemPrincipal() {
        this.menu.sinItemPrincipal();
        return this;
    }

    public ItemMenu menuSinReceta() {
        this.menu.sinReceta();
        return this;
    }

    public ItemMenu menuRecetaMueve(float x, float y) {
        this.menu.recetaMueve(x, y);
        return this;
    }

    public ItemMenu menuItem(Item item, float x, float y) {
        return this.menuItem(new ItemStack(item), x, y, 1.0F);
    }

    public ItemMenu menuItem(Item item, float x, float y, float escala) {
        return this.menuItem(new ItemStack(item), x, y, escala);
    }

    public ItemMenu menuItem(ItemStack stack, float x, float y) {
        return this.menuItem(stack, x, y, 1.0F);
    }

    public ItemMenu menuItem(ItemStack stack, float x, float y, float escala) {
        this.menu.item(stack, x, y, escala);
        return this;
    }

    public ItemMenu menuTexto(String texto, float x, float y) {
        return this.menuTexto(texto, x, y, MenuCrafteo.TEXTO_ESCALA);
    }

    public ItemMenu menuTexto(String texto, float x, float y, float escala) {
        this.menu.texto(texto, x, y, escala);
        return this;
    }

    public boolean tieneMenu() {
        return this.menu.activo(!this.img.vacia());
    }

    public MenuCrafteo getMenu() {
        return this.menu;
    }

    public ImagenMenu getImagen() {
        return this.img;
    }

    @javax.annotation.Nullable
    public Component getNombrePropio() {
        return this.nombre != null ? nombreFormateado() : null;
    }

    public List<Component> getTooltip() {
        if (!this.tooltipPropio) {
            List<Component> original = new ArrayList<>(Screen.getTooltipFromItem(Minecraft.getInstance(), this.stack));

            if (!this.conNombre && !original.isEmpty()) {
                original.remove(0);
            } else if (this.nombre != null && !original.isEmpty()) {
                original.set(0, nombreFormateado());
            }

            return original;
        }

        if (this.tooltipListo != null) return this.tooltipListo;

        List<Component> salida = new ArrayList<>();

        if (this.conNombre) {
            salida.add(this.nombre != null ? nombreFormateado() : this.stack.getHoverName());
        }

        if (!this.lineas.isEmpty()) {
            salida.addAll(ModTooltips.formatear(this.lineas.toArray(new String[0])));
        }

        this.tooltipListo = salida;
        return salida;
    }

    private Component nombreFormateado() {
        List<Component> partes = ModTooltips.formatear(ModTooltips.hex(this.colorNombre) + this.nombre);

        if (partes.isEmpty()) return Component.literal(this.nombre);
        if (partes.size() == 1) return partes.get(0);

        MutableComponent junto = Component.empty();
        for (int i = 0; i < partes.size(); i++) {
            if (i > 0) junto.append(" ");
            junto.append(partes.get(i));
        }
        return junto;
    }

    public boolean tieneBarra() {
        return this.barra;
    }

    public boolean tieneCantidad() {
        return this.cantidad;
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