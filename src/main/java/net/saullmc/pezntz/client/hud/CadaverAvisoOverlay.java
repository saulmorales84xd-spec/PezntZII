package net.saullmc.pezntz.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.saullmc.pezntz.block.entity.CadaverEntity;

public class CadaverAvisoOverlay {

    public static final boolean ACTIVO = true;

    public static final String TEXTO = "Shift + Click derecho para abrir la mochila";

    public static final boolean SOLO_SI_HAY_MOCHILA = true;

    public static final int COLOR = 0xFFFFFFFF;

    public static final boolean SOMBRA = false;

    public static final float ESCALA = 1.0F;

    public static final int OFFSET_X = 0;

    public static final int OFFSET_Y = -62;

    public static final boolean MOSTRAR_DUENIO = true;

    public static final int COLOR_DUENIO = 0xFFAAAAAA;

    public static final int HUECO_ENTRE_LINEAS = 2;

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
            y = linea(g, fuente, "Cadaver de " + cadaver.getDuenioNombre(),
                    ancho, y, COLOR_DUENIO);
        }

        linea(g, fuente, TEXTO, ancho, y, COLOR);
    };

    private static int linea(GuiGraphics g, Font fuente, String texto, int ancho, int y, int color) {
        float anchoTexto = fuente.width(texto) * ESCALA;

        g.pose().pushPose();

        g.pose().translate((ancho - anchoTexto) / 2.0F + OFFSET_X, y, 0.0F);
        g.pose().scale(ESCALA, ESCALA, 1.0F);

        g.drawString(fuente, texto, 0, 0, color, SOMBRA);

        g.pose().popPose();

        return y + Math.round(fuente.lineHeight * ESCALA) + HUECO_ENTRE_LINEAS;
    }
}
