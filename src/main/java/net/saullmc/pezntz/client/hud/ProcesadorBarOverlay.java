package net.saullmc.pezntz.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.block.custom.Prensa;
import net.saullmc.pezntz.block.entity.PrensaEntity;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ProcesadorBarOverlay {

    public static final int OFFSET_X = 0;
    public static final int OFFSET_Y = -46;

    /** Tamanio de la barra en pixeles de GUI. */
    public static final int ANCHO = 130;
    public static final int ALTO = 10;

    public static final int DURACION = 50;

    public static final int TICKS_FUNDIDO = 12;

    public static final double DISTANCIA_MAXIMA = 12.0D;

    public static final int COLOR_BORDE = 0xFF101010;
    public static final int COLOR_FONDO = 0xB0202020;
    public static final int COLOR_RELLENO = 0xFFF5A623;
    public static final int COLOR_RELLENO_LLENO = 0xFF6BFF5E;

    public static final int GROSOR_BORDE = 1;

    public static final boolean MARCA_CENTRO = true;
    public static final int COLOR_MARCA_CENTRO = 0x60FFFFFF;

    public static final boolean MOSTRAR_TEXTO = true;
    public static final int TEXTO_OFFSET_Y = -11;
    public static final int TEXTO_COLOR = 0xFFFFFFFF;

    private static BlockPos objetivo = null;
    private static int ticksRestantes = 0;

    public static void mostrar(BlockPos pos) {
        objetivo = pos.immutable();
        ticksRestantes = DURACION;
    }

    public static void ocultar() {
        objetivo = null;
        ticksRestantes = 0;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (ticksRestantes > 0) ticksRestantes--;
        if (ticksRestantes == 0) objetivo = null;
    }

    public static final IGuiOverlay OVERLAY = ProcesadorBarOverlay::render;

    private static void render(ForgeGui gui, GuiGraphics g, float partialTick, int anchoPantalla, int altoPantalla) {
        if (ticksRestantes <= 0 || objetivo == null) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        if (mc.options.hideGui) return;

        if (mc.player.blockPosition().distSqr(objetivo) > DISTANCIA_MAXIMA * DISTANCIA_MAXIMA) {
            ocultar();
            return;
        }

        BlockEntity be = mc.level.getBlockEntity(objetivo);
        if (!(be instanceof PrensaEntity proc)) {
            ocultar();
            return;
        }

        float fraccion = proc.getProgresoClicks();

        // Fundido de salida
        float alpha = 1.0F;
        if (ticksRestantes < TICKS_FUNDIDO) {
            alpha = ticksRestantes / (float) TICKS_FUNDIDO;
        }
        if (alpha <= 0.02F) return;

        int centroX = anchoPantalla / 2 + OFFSET_X;
        int y = altoPantalla / 2 + OFFSET_Y;

        int x0 = centroX - ANCHO / 2;
        int x1 = centroX + ANCHO / 2;

        if (GROSOR_BORDE > 0) {
            g.fill(x0 - GROSOR_BORDE, y - GROSOR_BORDE, x1 + GROSOR_BORDE, y + ALTO + GROSOR_BORDE,
                    conAlpha(COLOR_BORDE, alpha));
        }
        g.fill(x0, y, x1, y + ALTO, conAlpha(COLOR_FONDO, alpha));

        int mitad = Math.round((ANCHO / 2.0F) * fraccion);
        if (mitad > 0) {
            int color = fraccion >= 1.0F ? COLOR_RELLENO_LLENO : COLOR_RELLENO;
            g.fill(centroX - mitad, y, centroX + mitad, y + ALTO, conAlpha(color, alpha));
        }

        if (MARCA_CENTRO) {
            g.fill(centroX, y, centroX + 1, y + ALTO, conAlpha(COLOR_MARCA_CENTRO, alpha));
        }

        if (MOSTRAR_TEXTO) {
            int golpes = Math.min(proc.getClicks(), Prensa.CLICKS_NECESARIOS);
            String texto = golpes + "/" + Prensa.CLICKS_NECESARIOS;
            int anchoTexto = mc.font.width(texto);
            g.drawString(mc.font, texto,
                    centroX - anchoTexto / 2,
                    y + TEXTO_OFFSET_Y,
                    conAlpha(TEXTO_COLOR, alpha), true);
        }
    }

    private static int conAlpha(int argb, float alpha) {
        int a = (argb >>> 24) & 0xFF;
        a = Math.max(1, Math.round(a * alpha));
        return (a << 24) | (argb & 0x00FFFFFF);
    }
}