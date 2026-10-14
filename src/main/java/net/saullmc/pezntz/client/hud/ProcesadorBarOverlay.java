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

    public static final int Y_DESDE_ABAJO = 90;

    public static final int ANCHO = 140;
    public static final int ALTO = 16;

    public static final int COLOR_FONDO = 0x88000000;

    public static final int COLOR_RELLENO = 0xFFFFFFFF;

    public static final int OFFSET_X = 0;
    public static final int OFFSET_Y = 0;

    public static final int DURACION = 50;

    public static final int TICKS_FUNDIDO = 0;

    public static final double DISTANCIA_MAXIMA = 12.0D;

    public static final boolean MOSTRAR_TEXTO = false;
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

        float alpha = 1.0F;
        if (TICKS_FUNDIDO > 0 && ticksRestantes < TICKS_FUNDIDO) {
            alpha = ticksRestantes / (float) TICKS_FUNDIDO;
            if (alpha <= 0.02F) return;
        }

        int barY = altoPantalla - Y_DESDE_ABAJO + OFFSET_Y;
        int centerX = anchoPantalla / 2 + OFFSET_X;

        int anchoRelleno = (int) (fraccion * ANCHO);

        int fondoIzq = centerX - (ANCHO / 2);
        int fondoDer = centerX + (ANCHO / 2);
        int relIzq   = centerX - (anchoRelleno / 2);
        int relDer   = centerX + (anchoRelleno / 2);

        g.fill(fondoIzq, barY, fondoDer, barY + ALTO, conAlpha(COLOR_FONDO, alpha));
        g.fill(relIzq,   barY, relDer,   barY + ALTO, conAlpha(COLOR_RELLENO, alpha));

        if (MOSTRAR_TEXTO) {
            int golpes = Math.min(proc.getClicks(), Prensa.CLICKS_NECESARIOS);
            String texto = golpes + "/" + Prensa.CLICKS_NECESARIOS;
            int anchoTexto = mc.font.width(texto);
            g.drawString(mc.font, texto,
                    centerX - anchoTexto / 2,
                    barY + TEXTO_OFFSET_Y,
                    conAlpha(TEXTO_COLOR, alpha), true);
        }
    }

    private static int conAlpha(int argb, float alpha) {
        if (alpha >= 1.0F) return argb;
        int a = (argb >>> 24) & 0xFF;
        a = Math.max(1, Math.round(a * alpha));
        return (a << 24) | (argb & 0x00FFFFFF);
    }
}