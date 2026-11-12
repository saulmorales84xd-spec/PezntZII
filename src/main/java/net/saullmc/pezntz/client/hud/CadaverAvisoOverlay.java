package net.saullmc.pezntz.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.saullmc.pezntz.block.entity.CadaverEntity;
import net.saullmc.pezntz.item.ModTooltips;

import java.util.List;

import static net.saullmc.pezntz.item.ModTooltips.*;

public class CadaverAvisoOverlay {

    public static final boolean ACTIVO = true;

    public static final String TEXTO =
            KEYS + "Shift " + hex(0xFFFFFF) + "+ " + NUM + "Click derecho " + GRIS + "para "
                    + MOBS + "abrir " + GRIS + "la " + BYI + "mochila";

    public static final String TEXTO_DUENIO = GRIS + "Cadaver de " + ZOM;

    public static final boolean SOLO_SI_HAY_MOCHILA = true;

    public static final int OPACIDAD = 255;

    public static final boolean SOMBRA = false;

    public static final float ESCALA = 1.0F;

    public static final int OFFSET_X = 0;

    public static final int OFFSET_Y = -62;

    public static final boolean MOSTRAR_DUENIO = true;

    public static final int HUECO_ENTRE_LINEAS = 2;

    private static List<Component> textoListo;

    private static String ultimoDuenio = "";

    private static List<Component> duenioListo = List.of();

    public static final IGuiOverlay OVERLAY = (gui, g, partialTick, ancho, alto) -> {
        if (!ACTIVO) return;

        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null || mc.player == null) return;
        if (mc.options.hideGui) return;
        if (mc.screen != null) return;

        if (!(mc.hitResult instanceof BlockHitResult golpe)) return;
        if (mc.hitResult.getType() != HitResult.Type.BLOCK) return;

        BlockPos pos = golpe.getBlockPos();
        BlockEntity be = mc.level.getBlockEntity(pos);

        if (!(be instanceof CadaverEntity cadaver)) return;
        if (SOLO_SI_HAY_MOCHILA && !cadaver.teniaMochila()) return;

        Font fuente = mc.font;

        int y = alto + OFFSET_Y;

        if (MOSTRAR_DUENIO && !cadaver.getDuenioNombre().isEmpty()) {
            if (!cadaver.getDuenioNombre().equals(ultimoDuenio) || duenioListo.isEmpty()) {
                ultimoDuenio = cadaver.getDuenioNombre();
                duenioListo = ModTooltips.formatear(TEXTO_DUENIO + ultimoDuenio);
            }
            y = lineas(g, fuente, duenioListo, ancho, y);
        }

        if (textoListo == null) textoListo = ModTooltips.formatear(TEXTO);

        lineas(g, fuente, textoListo, ancho, y);
    };

    private static int lineas(GuiGraphics g, Font fuente, List<Component> lineas, int ancho, int y) {
        int color = (Math.max(0, Math.min(255, OPACIDAD)) << 24) | 0xFFFFFF;

        for (Component linea : lineas) {
            float anchoTexto = fuente.width(linea) * ESCALA;

            g.pose().pushPose();

            g.pose().translate((ancho - anchoTexto) / 2.0F + OFFSET_X, y, 0.0F);
            g.pose().scale(ESCALA, ESCALA, 1.0F);

            g.drawString(fuente, linea, 0, 0, color, SOMBRA);

            g.pose().popPose();

            y += Math.round(fuente.lineHeight * ESCALA) + HUECO_ENTRE_LINEAS;
        }

        return y;
    }
}