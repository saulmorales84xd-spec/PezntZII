package net.saullmc.pezntz.client.hud;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.custom.Quad;

@OnlyIn(Dist.CLIENT)
public class QuadOverlay {

    /** Separacion desde el borde DERECHO, en pixeles. Mas alto = mas a la izquierda. */
    public static final int OFFSET_X = 8;

    /** Separacion desde el borde SUPERIOR. Mas alto = mas abajo. */
    public static final int OFFSET_Y = 8;

    /** Escala del conjunto entero, marco y barras. 1.0 es el tamaño de la interfaz. */
    public static final float SCALE = 1.0F;

    /** Tamaño del marco en pixeles. Tiene que coincidir con tu png. */
    public static final int PANEL = 96;

    /** Tamaño del quad dentro del marco. Subelo si lo ves pequeño en su hueco. */
    public static final int TAMANIO_ENTIDAD = 20;

    /** Desplazamiento del quad dentro del marco, por si tu textura no esta centrada. */
    public static final int ENTIDAD_OFFSET_X = 0;
    public static final int ENTIDAD_OFFSET_Y = 12;

    /** Inclinacion y giro con que se ve el quad, en grados. */
    public static final float ENTIDAD_PITCH = 12.0F;
    public static final float ENTIDAD_YAW = -35.0F;

    /** Tamaño del FONDO de la barra, o sea del png. Tu barra_vida.png mide 96x16. */
    public static final int BARRA_ANCHO = 112;
    public static final int BARRA_ALTO = 20;

    /**
     * Tamaño del RELLENO de color, independiente del fondo.
     *
     * Van separados a proposito: asi puedes hacer la parte verde y la roja tan finas como
     * quieras sin encoger ni deformar tu textura.
     */
    public static final int RELLENO_ANCHO = 78;
    public static final int RELLENO_ALTO = 7;

    /**
     * Si el relleno se centra solo dentro del fondo.
     *
     * Con true se centra y los OFFSET de abajo lo mueven DESDE ese centro. Con false el
     * relleno arranca en la esquina superior izquierda del fondo y los OFFSET son su
     * posicion exacta.
     */
    public static final boolean RELLENO_CENTRAR = true;

    /**
     * Ajuste fino del relleno dentro del fondo. Se suma siempre, centrado o no.
     *
     * Negativo mueve a la IZQUIERDA y hacia ARRIBA; positivo a la derecha y hacia abajo.
     *
     * (Antes esto no admitia negativos: usaba -1 como señal de "centrar" y el chequeo era
     * >= 0, asi que cualquier numero negativo se leia como centrar y no movia nada.)
     */
    public static final int RELLENO_OFFSET_X = 13;
    public static final int RELLENO_OFFSET_Y = 0;

    /** Hueco entre el marco y la primera barra. */
    public static final int HUECO_TRAS_PANEL = 1;

    /** Hueco entre una barra y la otra. */
    public static final int HUECO_ENTRE_BARRAS = -7;

    /** Mueve las DOS barras en horizontal. Positivo = a la derecha, negativo = izquierda. */
    public static final float BARRAS_OFFSET_X = -7.2F;

    /** Mueve las DOS barras en vertical. Positivo = hacia abajo. */
    public static final int BARRAS_OFFSET_Y = -4;

    public static final float BARRAS_SCALE = 1F;

    public static final int COLOR_VIDA = 0xFF4CC24C;
    public static final int COLOR_GASOLINA = 0xFFC2402F;

    private static final ResourceLocation TEX_PANEL =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/menu_vehiculo.png");

    private static final ResourceLocation TEX_BARRA =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/barra_vida.png");

    public static final IGuiOverlay OVERLAY = (ForgeGui gui, GuiGraphics g, float partialTick,
                                               int screenWidth, int screenHeight) -> {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        if (!(mc.player.getVehicle() instanceof Quad quad)) return;

        g.pose().pushPose();
        g.pose().scale(SCALE, SCALE, 1.0F);

        int anchoUtil = (int) (screenWidth / SCALE);

        int x = anchoUtil - OFFSET_X - PANEL;
        int y = OFFSET_Y;

        g.blit(TEX_PANEL, x, y, 0, 0, PANEL, PANEL, PANEL, PANEL);

        dibujarEntidad(g, quad,
                x + PANEL / 2 + ENTIDAD_OFFSET_X,
                y + PANEL / 2 + ENTIDAD_OFFSET_Y);

        float anchoEscalado = BARRA_ANCHO * BARRAS_SCALE;

        float anclaX = x + (PANEL - anchoEscalado) / 2.0F + BARRAS_OFFSET_X;
        float anclaY = y + PANEL + HUECO_TRAS_PANEL + BARRAS_OFFSET_Y;

        g.pose().pushPose();

        g.pose().translate(anclaX, anclaY, 0.0F);
        g.pose().scale(BARRAS_SCALE, BARRAS_SCALE, 1.0F);

        float vida = quad.getHealth() / quad.getMaxHealth();
        dibujarBarra(g, 0, 0, vida, COLOR_VIDA);

        float gasolina = quad.getFuel() / Quad.MAX_FUEL;
        dibujarBarra(g, 0, BARRA_ALTO + HUECO_ENTRE_BARRAS, gasolina, COLOR_GASOLINA);

        g.pose().popPose();

        g.pose().popPose();
    };

    private static void dibujarBarra(GuiGraphics g, int x, int y, float proporcion, int color) {
        g.blit(TEX_BARRA, x, y, 0, 0, BARRA_ANCHO, BARRA_ALTO, BARRA_ANCHO, BARRA_ALTO);

        proporcion = Math.max(0.0F, Math.min(1.0F, proporcion));


        int centradoX = RELLENO_CENTRAR ? (BARRA_ANCHO - RELLENO_ANCHO) / 2 : 0;
        int centradoY = RELLENO_CENTRAR ? (BARRA_ALTO - RELLENO_ALTO) / 2 : 0;

        int rellenoX = x + centradoX + RELLENO_OFFSET_X;
        int rellenoY = y + centradoY + RELLENO_OFFSET_Y;

        int ancho = Math.round(RELLENO_ANCHO * proporcion);
        if (ancho <= 0) return;

        g.fill(rellenoX, rellenoY, rellenoX + ancho, rellenoY + RELLENO_ALTO, color);
    }

    private static void dibujarEntidad(GuiGraphics g, Quad quad, int x, int y) {
        g.pose().pushPose();

        g.pose().translate(x, y, 50.0D);
        g.pose().scale(TAMANIO_ENTIDAD, -TAMANIO_ENTIDAD, TAMANIO_ENTIDAD);
        g.pose().mulPose(Axis.XP.rotationDegrees(ENTIDAD_PITCH));
        g.pose().mulPose(Axis.YP.rotationDegrees(ENTIDAD_YAW));

        float guardadoYRot = quad.getYRot();
        float guardadoXRot = quad.getXRot();
        float guardadoYBody = quad.yBodyRot;
        float guardadoYHead = quad.yHeadRot;
        float guardadoYRotO = quad.yRotO;
        float guardadoXRotO = quad.xRotO;
        float guardadoYBodyO = quad.yBodyRotO;
        float guardadoYHeadO = quad.yHeadRotO;

        quad.setYRot(0.0F);
        quad.setXRot(0.0F);
        quad.yBodyRot = 0.0F;
        quad.yHeadRot = 0.0F;
        quad.yRotO = 0.0F;
        quad.xRotO = 0.0F;
        quad.yBodyRotO = 0.0F;
        quad.yHeadRotO = 0.0F;

        Lighting.setupForEntityInInventory();

        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher despachador = mc.getEntityRenderDispatcher();

        despachador.setRenderShadow(false);

        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();

        despachador.render(quad, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, g.pose(), buffers, 15728880);

        buffers.endBatch();

        despachador.setRenderShadow(true);
        Lighting.setupFor3DItems();

        quad.setYRot(guardadoYRot);
        quad.setXRot(guardadoXRot);
        quad.yBodyRot = guardadoYBody;
        quad.yHeadRot = guardadoYHead;
        quad.yRotO = guardadoYRotO;
        quad.xRotO = guardadoXRotO;
        quad.yBodyRotO = guardadoYBodyO;
        quad.yHeadRotO = guardadoYHeadO;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        g.pose().popPose();
    }
}