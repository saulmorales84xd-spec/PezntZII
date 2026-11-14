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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageNumbers {

    public static final int DURACION = 30;

    public static final float SUBIDA = 2.2F;

    public static final float ALTURA_EXTRA = 0.55F;

    public static final float SEPARACION_LADOS = 0.6F;

    public static final float[] LADOS = {-1.0F, 0.0F, 1.0F};

    public static final boolean NO_REPETIR_LADO = true;

    public static final float VARIACION_LADO = 0.12F;

    public static final float VARIACION_ALTURA = 0.25F;

    public static final float ESCALA = 0.05F;

    public static final double DISTANCIA_MAXIMA = 32.0D;

    public static final int MAXIMO = 60;

    public static final int COLOR_DANIO = 0xFF5555;
    public static final int COLOR_CURACION = 0x55FF55;

    public static final int DECIMALES = 1;

    public static final boolean QUITAR_DECIMAL_CERO = true;

    public static final float ESPACIO_ANTES_DEL_PUNTO = 1.0F;

    public static final float ESPACIO_DESPUES_DEL_PUNTO = 1.0F;

    private static final List<Numero> NUMEROS = new ArrayList<>();

    private static final Map<Integer, Integer> ULTIMO_LADO = new HashMap<>();

    private static final Random AZAR = new Random();

    private static class Numero {

        final double x;
        final double y;
        final double z;
        final String texto;
        final String entero;
        final String decimales;
        final int color;
        final float lado;
        final float alturaExtra;

        final long nacimiento;

        Numero(double x, double y, double z, String texto, int color, float lado, float alturaExtra, long nacimiento) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.texto = texto;

            int punto = texto.indexOf('.');
            this.entero = punto < 0 ? texto : texto.substring(0, punto);
            this.decimales = punto < 0 ? "" : texto.substring(punto + 1);
            this.color = color;
            this.lado = lado;
            this.alturaExtra = alturaExtra;
            this.nacimiento = nacimiento;
        }
    }

    public static void agregar(int entityId, float cantidad, boolean curacion) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        Entity entidad = mc.level.getEntity(entityId);
        if (entidad == null) return;

        String texto = (curacion ? "+" : "-") + formatear(cantidad);

        float lado = elegirLado(entityId) * SEPARACION_LADOS
                + (AZAR.nextFloat() * 2.0F - 1.0F) * VARIACION_LADO;

        float alturaExtra = AZAR.nextFloat() * VARIACION_ALTURA;

        NUMEROS.add(new Numero(
                entidad.getX(),
                entidad.getY() + entidad.getBbHeight() + ALTURA_EXTRA,
                entidad.getZ(),
                texto, curacion ? COLOR_CURACION : COLOR_DANIO, lado, alturaExtra,
                mc.level.getGameTime()));

        while (NUMEROS.size() > MAXIMO) {
            NUMEROS.remove(0);
        }
    }


    private static float elegirLado(int entityId) {
        if (LADOS.length == 0) return 0.0F;
        if (LADOS.length == 1) return LADOS[0];

        Integer anterior = ULTIMO_LADO.get(entityId);

        int elegido;
        if (NO_REPETIR_LADO && anterior != null && anterior < LADOS.length) {
            elegido = AZAR.nextInt(LADOS.length - 1);
            if (elegido >= anterior) elegido++;
        } else {
            elegido = AZAR.nextInt(LADOS.length);
        }

        if (ULTIMO_LADO.size() > 512) ULTIMO_LADO.clear();
        ULTIMO_LADO.put(entityId, elegido);

        return LADOS[elegido];
    }

    private static String formatear(float valor) {
        int decimales = Math.max(0, DECIMALES);
        if (decimales == 0) return Integer.toString(Math.max(1, Math.round(valor)));

        String texto = new java.math.BigDecimal(Float.toString(valor))
                .setScale(decimales, java.math.RoundingMode.HALF_UP)
                .toPlainString();

        if (QUITAR_DECIMAL_CERO && texto.indexOf('.') >= 0) {
            texto = texto.replaceAll("0+$", "");
            if (texto.endsWith(".")) texto = texto.substring(0, texto.length() - 1);
        }

        return texto;
    }

    public static void limpiar() {
        NUMEROS.clear();
        ULTIMO_LADO.clear();
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
            double dx = numero.x - origen.x;
            double dy = numero.y - origen.y;
            double dz = numero.z - origen.z;
            if (Math.sqrt(dx * dx + dy * dy + dz * dz) > DISTANCIA_MAXIMA) continue;

            float avance = ((ahora - numero.nacimiento) + parcial) / (float) DURACION;

            float alpha = avance < 0.66F ? 1.0F : 1.0F - (avance - 0.66F) / 0.34F;
            if (alpha <= 0.02F) continue;

            double x = numero.x - origen.x;
            double y = numero.y + SUBIDA * avance - origen.y;
            double z = numero.z - origen.z;

            poseStack.pushPose();
            poseStack.translate(x, y, z);

            poseStack.mulPose(camara.rotation());

            poseStack.translate(-numero.lado, numero.alturaExtra, 0.0F);

            poseStack.scale(-ESCALA, -ESCALA, ESCALA);

            int color = (Math.max(4, (int) (alpha * 255)) << 24) | numero.color;

            Matrix4f matriz = poseStack.last().pose();

            if (numero.decimales.isEmpty()) {
                int ancho = font.width(numero.texto);
                font.drawInBatch(numero.texto, -ancho / 2.0F, 0.0F, color, false,
                        matriz, buffers, Font.DisplayMode.NORMAL, 0, 15728880);
            } else {
                float anchoEntero = font.width(numero.entero);
                float anchoPunto = font.width(".");
                float anchoDecimales = font.width(numero.decimales);

                float ancho = anchoEntero + ESPACIO_ANTES_DEL_PUNTO + anchoPunto
                        + ESPACIO_DESPUES_DEL_PUNTO + anchoDecimales;

                float xTexto = -ancho / 2.0F;

                font.drawInBatch(numero.entero, xTexto, 0.0F, color, false,
                        matriz, buffers, Font.DisplayMode.NORMAL, 0, 15728880);
                xTexto += anchoEntero + ESPACIO_ANTES_DEL_PUNTO;

                font.drawInBatch(".", xTexto, 0.0F, color, false,
                        matriz, buffers, Font.DisplayMode.NORMAL, 0, 15728880);
                xTexto += anchoPunto + ESPACIO_DESPUES_DEL_PUNTO;

                font.drawInBatch(numero.decimales, xTexto, 0.0F, color, false,
                        matriz, buffers, Font.DisplayMode.NORMAL, 0, 15728880);
            }

            poseStack.popPose();
        }

        buffers.endBatch();
    }
}