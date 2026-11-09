package net.saullmc.pezntz.client.screen.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.saullmc.pezntz.client.BestiarioCliente;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntidadMenu {

    public static final int PEZ_PAYASO = 65536;

    public static final String INTERROGANTE = "?";
    public static final int COLOR_INTERROGANTE = 0xFFFFFFFF;
    public static final float ESCALA_INTERROGANTE = 2.0F;

    public static final int INTERROGANTE_OFFSET_Y = 0;

    public static final float TAMANIO_POR_DEFECTO = 24.0F;

    public static final int COLOR_TITULO = 0xFFFFFF;
    public static final int COLOR_LINEA = 0xAAAAAA;

    public static final float ITEMS_IMAGEN_ESCALA = 0.3F;

    private final EntityType<?> tipo;

    private final ImagenMenu img = new ImagenMenu();

    private int x = 0;
    private int y = 0;
    private float tamanio = TAMANIO_POR_DEFECTO;

    private float giroX = 0.0F;
    private float giroY = 180.0F;

    private int zona = 16;

    private int variante = -1;
    private boolean secreto = false;

    private final List<Component> tooltip = new ArrayList<>();

    @Nullable
    private LivingEntity cache;

    private boolean intentado = false;

    public EntidadMenu(EntityType<?> tipo) {
        this.tipo = tipo;
        this.img.itemsEscala(ITEMS_IMAGEN_ESCALA);
    }

    public EntidadMenu en(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public EntidadMenu tamanio(float tamanio) {
        this.tamanio = tamanio;
        return this;
    }

    public EntidadMenu giro(float giroY, float giroX) {
        this.giroY = giroY;
        this.giroX = giroX;
        return this;
    }

    public EntidadMenu zona(int zona) {
        this.zona = zona;
        return this;
    }

    public EntidadMenu variante(int variante) {
        this.variante = variante;
        return this;
    }

    public EntidadMenu secreto() {
        this.secreto = true;
        return this;
    }

    public EntidadMenu titulo(String texto) {
        return this.titulo(texto, COLOR_TITULO);
    }

    public EntidadMenu titulo(String texto, int color) {
        this.tooltip.add(0, pintar(texto, color));
        return this;
    }

    public EntidadMenu titulo(String texto, ChatFormatting color) {
        this.tooltip.add(0, Component.literal(texto).withStyle(color));
        return this;
    }

    public EntidadMenu linea(String texto) {
        return this.linea(texto, COLOR_LINEA);
    }

    public EntidadMenu linea(String texto, int color) {
        this.tooltip.add(pintar(texto, color));
        return this;
    }

    public EntidadMenu linea(String texto, ChatFormatting color) {
        this.tooltip.add(Component.literal(texto).withStyle(color));
        return this;
    }

    public EntidadMenu lineaVacia() {
        this.tooltip.add(Component.empty());
        return this;
    }

    private static MutableComponent pintar(String texto, int color) {
        return Component.literal(texto)
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color & 0xFFFFFF)));
    }

    public EntidadMenu imagen(String ruta, int ancho, int alto) {
        this.img.imagen(ruta, ancho, alto);
        return this;
    }

    public EntidadMenu imagenPng(int texturaAncho, int texturaAlto) {
        this.img.png(texturaAncho, texturaAlto);
        return this;
    }

    public EntidadMenu imagenEscala(float escala) {
        this.img.escala(escala);
        return this;
    }

    public EntidadMenu imagenMueve(int x, int y) {
        this.img.mueve(x, y);
        return this;
    }

    public EntidadMenu imagenMargen(int x, int y) {
        this.img.margen(x, y);
        return this;
    }

    public EntidadMenu imagenItemEscala(float escala) {
        this.img.itemsEscala(escala);
        return this;
    }

    public EntidadMenu imagenItem(Item item, float x, float y) {
        return this.imagenItem(new ItemStack(item), x, y, 1.0F);
    }

    public EntidadMenu imagenItem(Item item, float x, float y, float escala) {
        return this.imagenItem(new ItemStack(item), x, y, escala);
    }

    public EntidadMenu imagenItem(ItemStack stack, float x, float y) {
        return this.imagenItem(stack, x, y, 1.0F);
    }

    public EntidadMenu imagenItem(ItemStack stack, float x, float y, float escala) {
        this.img.item(stack, x, y, escala);
        return this;
    }

    public Optional<TooltipComponent> getImagenTooltip() {
        return revelado() ? this.img.construir() : Optional.empty();
    }

    public boolean revelado() {
        if (!this.secreto) return true;

        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(this.tipo);
        return id != null && BestiarioCliente.conoce(id.toString());
    }

    public List<Component> getTooltip() {
        return revelado() ? this.tooltip : List.of();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZona() {
        return this.zona;
    }

    public float getTamanio() {
        return this.tamanio;
    }

    public int getAlto() {
        return Math.max(1, Math.round(this.tamanio));
    }

    public void dibujar(GuiGraphics g, int x, int y) {
        LivingEntity bicho = entidad();
        if (bicho == null) return;

        boolean visible = revelado();

        Quaternionf pose = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf camara = new Quaternionf().rotateX(this.giroX * ((float) Math.PI / 180.0F));
        pose.mul(camara);

        bicho.setYRot(this.giroY);
        bicho.setXRot(0.0F);
        bicho.yBodyRot = this.giroY;
        bicho.yHeadRot = this.giroY;
        bicho.yBodyRotO = this.giroY;
        bicho.yHeadRotO = this.giroY;

        if (!visible) {
            RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
        }

        int base = Math.max(1, Math.round(this.tamanio));
        float resto = this.tamanio / base;

        g.pose().pushPose();
        g.pose().translate(x, y, 0.0F);
        g.pose().scale(resto, resto, resto);

        InventoryScreen.renderEntityInInventory(g, 0, 0, base, pose, camara, bicho);

        g.pose().popPose();

        if (!visible) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            dibujarInterrogante(g, x, y, bicho.getBbHeight());
        }
    }

    private void dibujarInterrogante(GuiGraphics g, int x, int y, float alturaBicho) {
        Minecraft mc = Minecraft.getInstance();

        float medio = this.tamanio * Math.max(0.25F, alturaBicho) / 2.0F;

        g.pose().pushPose();

        g.pose().translate(x, y - medio + INTERROGANTE_OFFSET_Y, 300.0F);
        g.pose().scale(ESCALA_INTERROGANTE, ESCALA_INTERROGANTE, 1.0F);

        int ancho = mc.font.width(INTERROGANTE);
        g.drawString(mc.font, INTERROGANTE, -ancho / 2, -mc.font.lineHeight / 2,
                COLOR_INTERROGANTE, true);

        g.pose().popPose();
    }

    @Nullable
    private LivingEntity entidad() {
        if (this.cache != null || this.intentado) return this.cache;

        this.intentado = true;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return null;

        Entity creada = this.tipo.create(mc.level);
        if (!(creada instanceof LivingEntity viva)) return null;

        if (this.variante >= 0 && viva instanceof TropicalFish pez) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("Variant", this.variante);
            pez.readAdditionalSaveData(tag);
        }

        this.cache = viva;
        return viva;
    }
}