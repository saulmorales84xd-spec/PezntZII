package net.saullmc.pezntz.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.client.DropCliente;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;

public class DropWaypointOverlay {

    public static final ResourceLocation TEXTURA =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/item/marcador_drop.png");

    public static final int TEXTURA_ANCHO = 16;
    public static final int TEXTURA_ALTO = 16;

    public static final float ESCALA = 1.5F;

    public static final double ALTURA_MARCA = 3.0D;

    public static final int MARGEN = 12;

    public static final boolean PEGAR_AL_BORDE = true;

    public static final boolean MOSTRAR_CONTADOR = true;

    public static final int COLOR_CONTADOR = 0xFFFFFFFF;

    public static final boolean SOMBRA_CONTADOR = false;

    public static final int CONTADOR_OFFSET_Y = 3;

    public static final IGuiOverlay OVERLAY = (gui, g, partialTick, ancho, alto) -> {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null || mc.player == null) return;
        if (mc.options.hideGui) return;

        Map<BlockPos, DropCliente.Dato> drops = DropCliente.activos();
        if (drops.isEmpty()) return;

        Camera camara = mc.gameRenderer.getMainCamera();
        Vec3 ojo = camara.getPosition();

        Quaternionf inversa = camara.rotation().conjugate(new Quaternionf());

        double fov = mc.options.fov().get();
        float foco = (float) (alto / 2.0D / Math.tan(Math.toRadians(fov / 2.0D)));

        int centroX = ancho / 2;
        int centroY = alto / 2;

        int marcaAncho = Math.max(1, Math.round(TEXTURA_ANCHO * ESCALA));
        int marcaAlto = Math.max(1, Math.round(TEXTURA_ALTO * ESCALA));

        Font fuente = mc.font;

        for (Map.Entry<BlockPos, DropCliente.Dato> entrada : drops.entrySet()) {
            BlockPos pos = entrada.getKey();

            Vector3f v = new Vector3f(
                    (float) (pos.getX() + 0.5D - ojo.x),
                    (float) (pos.getY() + ALTURA_MARCA - ojo.y),
                    (float) (pos.getZ() + 0.5D - ojo.z));

            v.rotate(inversa);

            int x;
            int y;

            if (v.z > 0.05F) {
                x = centroX - Math.round(v.x / v.z * foco);
                y = centroY - Math.round(v.y / v.z * foco);

            } else {
                if (!PEGAR_AL_BORDE) continue;

                x = v.x > 0.0F ? MARGEN : ancho - MARGEN;
                y = alto - MARGEN;
            }

            x = Mth.clamp(x, MARGEN, ancho - MARGEN);
            y = Mth.clamp(y, MARGEN, alto - MARGEN);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            g.blit(TEXTURA,
                    x - marcaAncho / 2, y - marcaAlto / 2,
                    marcaAncho, marcaAlto,
                    0.0F, 0.0F,
                    TEXTURA_ANCHO, TEXTURA_ALTO,
                    TEXTURA_ANCHO, TEXTURA_ALTO);

            RenderSystem.disableBlend();

            if (!MOSTRAR_CONTADOR) continue;

            int ticks = (int) Math.max(0L, entrada.getValue().tiempoApertura() - mc.level.getGameTime());
            if (ticks <= 0) continue;

            String texto = formatear(ticks);
            int anchoTexto = fuente.width(texto);

            g.drawString(fuente, texto,
                    x - anchoTexto / 2,
                    y + marcaAlto / 2 + CONTADOR_OFFSET_Y,
                    COLOR_CONTADOR, SOMBRA_CONTADOR);
        }
    };

    private static String formatear(int ticks) {
        int segundos = (ticks + 19) / 20;
        return String.format("%d:%02d", segundos / 60, segundos % 60);
    }
}
