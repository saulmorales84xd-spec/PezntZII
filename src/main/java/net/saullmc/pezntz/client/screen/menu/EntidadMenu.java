package net.saullmc.pezntz.client.screen.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraftforge.registries.ForgeRegistries;
import net.saullmc.pezntz.client.BestiarioCliente;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EntidadMenu {

    public static final int PEZ_PAYASO = 65536;

    public static final String INTERROGANTE = "?";
    public static final int COLOR_INTERROGANTE = 0xFFFFFFFF;
    public static final float ESCALA_INTERROGANTE = 2.0F;

    public static final int INTERROGANTE_OFFSET_Y = 0;

    public static final int TAMANIO_POR_DEFECTO = 24;

    private final EntityType<?> tipo;

    private int x = 0;
    private int y = 0;
    private int tamanio = TAMANIO_POR_DEFECTO;

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
    }

    public EntidadMenu en(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public EntidadMenu tamanio(int tamanio) {
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
        this.tooltip.add(0, Component.literal(texto));
        return this;
    }

    public EntidadMenu linea(String texto) {
        this.tooltip.add(Component.literal(texto));
        return this;
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

    public int getTamanio() {
        return this.tamanio;
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

        InventoryScreen.renderEntityInInventory(g, x, y, this.tamanio, pose, camara, bicho);

        if (!visible) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            dibujarInterrogante(g, x, y);
        }
    }

    private void dibujarInterrogante(GuiGraphics g, int x, int y) {
        Minecraft mc = Minecraft.getInstance();

        g.pose().pushPose();

        g.pose().translate(x, y - this.tamanio / 2.0F + INTERROGANTE_OFFSET_Y, 300.0F);
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