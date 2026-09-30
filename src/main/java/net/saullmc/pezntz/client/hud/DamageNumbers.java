package net.saullmc.pezntz.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageNumbers {

    public static final int DURACION = 30;

    public static final float SUBIDA = 2.2F;

    public static final float ALTURA_EXTRA = 0.55F;

    public static final float DESPLAZAMIENTO_LATERAL = 0.35F;

    public static final float ESCALA = 0.05F;

    public static final double DISTANCIA_MAXIMA = 32.0D;

    public static final int MAXIMO = 60;

    public static final int COLOR_DANIO = 0xFF5555;
    public static final int COLOR_CURACION = 0x55FF55;

    private static final List<Numero> NUMEROS = new ArrayList<>();

    private static class Numero {
        final int entityId;
        final String texto;
        final int color;
        final float lado;

        final long nacimiento;

        Numero(int entityId, String texto, int color, float lado, long nacimiento) {
            this.entityId = entityId;
            this.texto = texto;
            this.color = color;
            this.lado = lado;
            this.nacimiento = nacimiento;
        }
    }

    public static void agregar(int entityId, float cantidad, boolean curacion) {
        if (Minecraft.getInstance().level == null) return;

        String texto = (curacion ? "+" : "-") + formatear(cantidad);

        float lado = (NUMEROS.size() % 2 == 0 ? 1.0F : -1.0F) * DESPLAZAMIENTO_LATERAL;

        NUMEROS.add(new Numero(entityId, texto, curacion ? COLOR_CURACION : COLOR_DANIO, lado,
                Minecraft.getInstance().level.getGameTime()));

        while (NUMEROS.size() > MAXIMO) {
            NUMEROS.remove(0);
        }
    }

    private static String formatear(float valor) {
        return valor == Math.floor(valor)
                ? Integer.toString((int) valor)
                : String.format("%.1f", valor);
    }

    public static void limpiar() {
        NUMEROS.clear();
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
        if (NUMEROS.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        Camera camara = event.getCamera();
        Vec3 origen = camara.getPosition();

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        Font font = mc.font;

        float parcial = event.getPartialTick();

        long ahora = mc.level.getGameTime();
        NUMEROS.removeIf(n -> ahora - n.nacimiento > DURACION);

        for (Numero numero : NUMEROS) {
            Entity entidad = mc.level.getEntity(numero.entityId);
            if (entidad == null) continue;

            double distancia = entidad.position().distanceTo(origen);
            if (distancia > DISTANCIA_MAXIMA) continue;

            float avance = ((ahora - numero.nacimiento) + parcial) / (float) DURACION;

            float alpha = avance < 0.66F ? 1.0F : 1.0F - (avance - 0.66F) / 0.34F;
            if (alpha <= 0.02F) continue;

            double x = entidad.getX() - origen.x;
            double y = entidad.getY() + entidad.getBbHeight() + ALTURA_EXTRA + SUBIDA * avance - origen.y;
            double z = entidad.getZ() - origen.z;

            poseStack.pushPose();
            poseStack.translate(x, y, z);

            poseStack.mulPose(camara.rotation());


            poseStack.scale(-ESCALA, -ESCALA, ESCALA);

            poseStack.translate(-numero.lado / ESCALA * 0.5F, 0.0F, 0.0F);

            int ancho = font.width(numero.texto);
            int color = (Math.max(4, (int) (alpha * 255)) << 24) | numero.color;

            Matrix4f matriz = poseStack.last().pose();

            font.drawInBatch(numero.texto, -ancho / 2.0F, 0.0F, color, true,
                    matriz, buffers, Font.DisplayMode.NORMAL, 0, 15728880);

            poseStack.popPose();
        }

        buffers.endBatch();
    }
}