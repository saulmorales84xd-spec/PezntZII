package net.saullmc.pezntz.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PickupLogOverlay {

    public static final int OFFSET_X = 8;

    public static final int OFFSET_Y = 8;

    public static final float SCALE = 1.0F;

    public static final int MAX_LINEAS = 10;

    public static final int DURACION_TICKS = 200;

    public static final int FADE_TICKS = 30;

    public static final int ESCALONADO_TICKS = 10;

    public static final int MAX_EN_COLA = 60;

    private static final int ALTO_LINEA = 18;

    private static final int HUECO_TEXTO = 4;

    private static final int COLOR_NOMBRE = 0xFFFFFF;
    private static final int COLOR_CANTIDAD = 0xFFD54A;

    private static final List<Entrada> VISIBLES = new ArrayList<>();

    private static final List<Entrada> COLA = new ArrayList<>();

    private static class Entrada {
        final ItemStack stack;
        int cantidad;
        int tickAlta;

        Entrada(ItemStack stack, int cantidad, int tickAlta) {
            this.stack = stack;
            this.cantidad = cantidad;
            this.tickAlta = tickAlta;
        }
    }

    public static void add(ItemStack recogido) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || recogido.isEmpty()) return;

        int ahora = mc.player.tickCount;

        for (Entrada e : VISIBLES) {
            if (ItemStack.isSameItemSameTags(e.stack, recogido)) {
                e.cantidad += recogido.getCount();
                e.tickAlta = ahora;
                return;
            }
        }

        for (Entrada e : COLA) {
            if (ItemStack.isSameItemSameTags(e.stack, recogido)) {
                e.cantidad += recogido.getCount();
                return;
            }
        }

        int nacimiento = ahora + VISIBLES.size() * ESCALONADO_TICKS;
        Entrada nueva = new Entrada(recogido.copy(), recogido.getCount(), nacimiento);

        if (VISIBLES.size() < MAX_LINEAS) {
            VISIBLES.add(nueva);
        } else if (COLA.size() < MAX_EN_COLA) {
            COLA.add(nueva);
        }
       }

    public static void clear() {
        VISIBLES.clear();
        COLA.clear();
    }

    public static final IGuiOverlay OVERLAY = (ForgeGui gui, GuiGraphics g, float partialTick,
                                               int screenWidth, int screenHeight) -> {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        int ahora = mc.player.tickCount;

        VISIBLES.removeIf(e -> ahora - e.tickAlta > DURACION_TICKS);

        if (VISIBLES.isEmpty() && !COLA.isEmpty()) {
            int cuantas = Math.min(MAX_LINEAS, COLA.size());
            for (int i = 0; i < cuantas; i++) {
                Entrada e = COLA.remove(0);
               e.tickAlta = ahora + i * ESCALONADO_TICKS;
                VISIBLES.add(e);
            }
        }

        if (VISIBLES.isEmpty()) return;

        g.pose().pushPose();
        g.pose().scale(SCALE, SCALE, 1.0F);

        int anchoUtil = (int) (screenWidth / SCALE);
        int altoUtil = (int) (screenHeight / SCALE);

        int derecha = anchoUtil - OFFSET_X;
        int abajo = altoUtil - OFFSET_Y;

        for (int i = 0; i < VISIBLES.size(); i++) {
            Entrada e = VISIBLES.get(i);

            float vida = (ahora - e.tickAlta) + partialTick;
            float alpha = vida > (DURACION_TICKS - FADE_TICKS)
                    ? (DURACION_TICKS - vida) / FADE_TICKS
                    : 1.0F;
            alpha = Mth.clamp(alpha, 0.0F, 1.0F);
            if (alpha <= 0.0F) continue;

            int y = abajo - (VISIBLES.size() - i) * ALTO_LINEA;

            Component nombre = e.stack.getHoverName();
            String cantidad = e.cantidad > 1 ? "x" + e.cantidad : "";

            int anchoNombre = mc.font.width(nombre);
            int anchoCantidad = cantidad.isEmpty() ? 0 : mc.font.width(cantidad) + HUECO_TEXTO;

            int xCantidad = derecha - (cantidad.isEmpty() ? 0 : mc.font.width(cantidad));
            int xNombre = derecha - anchoCantidad - anchoNombre;
            int xIcono = xNombre - HUECO_TEXTO - 16;

            g.flush();

            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
            g.renderItem(e.stack, xIcono, y);
            g.flush();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            int alphaTexto = (int) (alpha * 255.0F) << 24;
            int yTexto = y + (16 - mc.font.lineHeight) / 2;

            g.drawString(mc.font, nombre, xNombre, yTexto, alphaTexto | COLOR_NOMBRE, true);

            if (!cantidad.isEmpty()) {
                g.drawString(mc.font, cantidad, xCantidad, yTexto, alphaTexto | COLOR_CANTIDAD, true);
            }
        }

        g.pose().popPose();
    };
}