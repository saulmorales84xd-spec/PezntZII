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

    public static final int OFFSET_X = 8;

    public static final int OFFSET_Y = 8;

    public static final float SCALE = 1.0F;

    public static final int PANEL = 96;

    public static final int TAMANIO_ENTIDAD = 20;

    public static final int ENTIDAD_OFFSET_X = 0;
    public static final int ENTIDAD_OFFSET_Y = 12;

    public static final float ENTIDAD_PITCH = 12.0F;
    public static final float ENTIDAD_YAW = -35.0F;

    public static final int BARRA_ANCHO = 112;
    public static final int BARRA_ALTO = 20;

    public static final int RELLENO_ANCHO = 78;
    public static final int RELLENO_ALTO = 7;

    public static final boolean RELLENO_CENTRAR = true;

    public static final int RELLENO_OFFSET_X = 13;
    public static final int RELLENO_OFFSET_Y = 0;

    public static final int HUECO_TRAS_PANEL = 1;

    public static final int HUECO_ENTRE_BARRAS = -7;

    public static final float BARRAS_OFFSET_X = -7.2F;

    public static final int BARRAS_OFFSET_Y = -4;

    public static final float BARRAS_SCALE = 1F;

    public static final int COLOR_VIDA = 0xFF4CC24C;
    public static final int COLOR_GASOLINA = 0xFFC2402F;

    public static final boolean MOSTRAR_TEXTO = true;

    public static final int TEXTO_OFFSET_X = 10;
    public static final int TEXTO_OFFSET_Y = 0;

    public static final float TEXTO_SCALE = 0.75F;

    public static final int TEXTO_COLOR = 0xFFFFFFFF;

    public static final boolean TEXTO_SOMBRA = true;

    public static final int TEXTO_COMBUSTIBLE_MAX = 100;

    private static final ResourceLocation TEX_PANEL =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/menu_vehiculo.png");

    private static final ResourceLocation TEX_BARRA_VIDA =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/barra_vida.png");

    private static final ResourceLocation TEX_BARRA_COMBUSTIBLE =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/barra_combustible.png");

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

        int vidaActual = (int) Math.ceil(quad.getHealth());
        int vidaMaxima = (int) quad.getMaxHealth();
        dibujarBarra(g, 0, 0,
                quad.getHealth() / quad.getMaxHealth(), COLOR_VIDA, TEX_BARRA_VIDA,
                vidaActual + "/" + vidaMaxima);

        float proporcionGasolina = quad.getFuel() / Quad.MAX_FUEL;
        int gasolinaActual = Math.round(proporcionGasolina * TEXTO_COMBUSTIBLE_MAX);
        dibujarBarra(g, 0, BARRA_ALTO + HUECO_ENTRE_BARRAS,
                proporcionGasolina, COLOR_GASOLINA, TEX_BARRA_COMBUSTIBLE,
                gasolinaActual + "/" + TEXTO_COMBUSTIBLE_MAX);

        g.pose().popPose();

        g.pose().popPose();
    };

    private static void dibujarBarra(GuiGraphics g, int x, int y, float proporcion, int color,
                                     ResourceLocation fondo, String texto) {
        g.blit(fondo, x, y, 0, 0, BARRA_ANCHO, BARRA_ALTO, BARRA_ANCHO, BARRA_ALTO);

        proporcion = Math.max(0.0F, Math.min(1.0F, proporcion));

        int centradoX = RELLENO_CENTRAR ? (BARRA_ANCHO - RELLENO_ANCHO) / 2 : 0;
        int centradoY = RELLENO_CENTRAR ? (BARRA_ALTO - RELLENO_ALTO) / 2 : 0;

        int rellenoX = x + centradoX + RELLENO_OFFSET_X;
        int rellenoY = y + centradoY + RELLENO_OFFSET_Y;

        int ancho = Math.round(RELLENO_ANCHO * proporcion);
        if (ancho > 0) {
            g.fill(rellenoX, rellenoY, rellenoX + ancho, rellenoY + RELLENO_ALTO, color);
        }

        if (MOSTRAR_TEXTO && texto != null) {
            dibujarTexto(g, x, y, texto);
        }
    }

    private static void dibujarTexto(GuiGraphics g, int x, int y, String texto) {
        Minecraft mc = Minecraft.getInstance();

        int anchoTexto = mc.font.width(texto);

        g.pose().pushPose();

        float centradoX = (BARRA_ANCHO - anchoTexto * TEXTO_SCALE) / 2.0F;
        float centradoY = (BARRA_ALTO - mc.font.lineHeight * TEXTO_SCALE) / 2.0F;

        g.pose().translate(x + centradoX + TEXTO_OFFSET_X, y + centradoY + TEXTO_OFFSET_Y, 0.0F);
        g.pose().scale(TEXTO_SCALE, TEXTO_SCALE, 1.0F);

        g.drawString(mc.font, texto, 0, 0, TEXTO_COLOR, TEXTO_SOMBRA);

        g.pose().popPose();
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