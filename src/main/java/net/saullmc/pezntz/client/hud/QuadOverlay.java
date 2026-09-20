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
    public static final float ENTIDAD_YAW = 35.0F;

    /** Tamaño de las barras. Tu barra_vida.png mide 96x16, asi que sale a tamaño real. */
    public static final int BARRA_ANCHO = 96;
    public static final int BARRA_ALTO = 16;

    /** Hueco entre el marco y la primera barra, y entre las dos barras. */
    public static final int HUECO_TRAS_PANEL = 2;
    public static final int HUECO_ENTRE_BARRAS = 3;

    /** Margen del relleno respecto al borde del fondo, para que no lo tape. */
    public static final int BORDE_RELLENO = 2;

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

        int barraX = x + (PANEL - BARRA_ANCHO) / 2;
        int barraY = y + PANEL + HUECO_TRAS_PANEL;

        float vida = quad.getHealth() / quad.getMaxHealth();
        dibujarBarra(g, barraX, barraY, vida, COLOR_VIDA);

        float gasolina = quad.getFuel() / Quad.MAX_FUEL;
        dibujarBarra(g, barraX, barraY + BARRA_ALTO + HUECO_ENTRE_BARRAS, gasolina, COLOR_GASOLINA);

        g.pose().popPose();
    };

    private static void dibujarBarra(GuiGraphics g, int x, int y, float proporcion, int color) {
        g.blit(TEX_BARRA, x, y, 0, 0, BARRA_ANCHO, BARRA_ALTO, BARRA_ANCHO, BARRA_ALTO);

        proporcion = Math.max(0.0F, Math.min(1.0F, proporcion));

        int anchoUtil = BARRA_ANCHO - BORDE_RELLENO * 2;
        int relleno = Math.round(anchoUtil * proporcion);
        if (relleno <= 0) return;

        g.fill(x + BORDE_RELLENO, y + BORDE_RELLENO,
                x + BORDE_RELLENO + relleno, y + BARRA_ALTO - BORDE_RELLENO, color);
    }

    private static void dibujarEntidad(GuiGraphics g, Quad quad, int x, int y) {
        g.pose().pushPose();

        g.pose().translate(x, y, 50.0D);
        g.pose().scale(TAMANIO_ENTIDAD, -TAMANIO_ENTIDAD, TAMANIO_ENTIDAD);
        g.pose().mulPose(Axis.XP.rotationDegrees(ENTIDAD_PITCH));
        g.pose().mulPose(Axis.YP.rotationDegrees(ENTIDAD_YAW));

        Lighting.setupForEntityInInventory();

        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher despachador = mc.getEntityRenderDispatcher();

        despachador.setRenderShadow(false);

        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();

        despachador.render(quad, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, g.pose(), buffers, 15728880);

        buffers.endBatch();

        despachador.setRenderShadow(true);
        Lighting.setupFor3DItems();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        g.pose().popPose();
    }
}