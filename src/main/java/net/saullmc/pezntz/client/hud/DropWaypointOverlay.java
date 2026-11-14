package net.saullmc.pezntz.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.client.DropCliente;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DropWaypointOverlay {

    public static final ResourceLocation TEXTURA =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/item/marcador_drop.png");

    public static final int TEXTURA_ANCHO = 16;
    public static final int TEXTURA_ALTO = 16;

    public static final boolean TAMANIO_FIJO_EN_EL_MUNDO = true;

    public static final float BLOQUES_POR_PIXEL = 1.0F / 16.0F;

    public static final boolean NO_ACHICAR_DE_LEJOS = true;

    public static final boolean NO_CRECER_DE_CERCA = false;

    public static final double DISTANCIA_TAMANIO_NORMAL = 12.0D;

    public static final float ESCALA_ICONO = 0.6F;

    public static final float ESCALA_TEXTO = 0.6F;

    public static final float ESCALA_NOMBRE = 1.0F;
    public static final float ESCALA_DISTANCIA = 1.0F;
    public static final float ESCALA_CONTADOR = 1.0F;

    public static final double SEPARACION_HITBOX = 0.3D;

    public static final float ICONO_OFFSET_X = 0.0F;
    public static final float ICONO_OFFSET_Y = 0.0F;

    public static final float TEXTO_OFFSET_X = 0.0F;
    public static final float TEXTO_OFFSET_Y = 0.0F;

    public static final float HUECO_TRAS_MARCA = 3.0F;
    public static final float HUECO_ENTRE_LINEAS = 1.0F;

    public static final boolean TEXTO_DEBAJO = true;

    public static final boolean A_TRAVES_DE_PAREDES = true;
    public static final boolean OCULTAR_CON_F1 = true;

    public static final float OPACIDAD_ICONO = 0.6F;

    public static final float OPACIDAD_TEXTO = 0.6F;

    public static final boolean OPACO_AL_MIRARLO = true;

    public static final float OPACIDAD_ICONO_MIRANDO = 1.0F;

    public static final float OPACIDAD_TEXTO_MIRANDO = 1.0F;

    public static final float MARGEN_MIRA = 2.0F;

    public static final float TRANSICION_SEGUNDOS = 0.15F;

    public static final String NOMBRE = "Airdrop";

    public static final boolean MOSTRAR_NOMBRE = true;
    public static final boolean MOSTRAR_DISTANCIA = true;
    public static final boolean MOSTRAR_CONTADOR = true;

    public static final int DECIMALES_DISTANCIA = 1;

    public static final String SUFIJO_DISTANCIA = " m";

    public static final int COLOR_NOMBRE = 0xFFFFFFFF;
    public static final int COLOR_DISTANCIA = 0xFFFFFFFF;
    public static final int COLOR_CONTADOR = 0xFFFFFFFF;

    public static final boolean SOMBRA_TEXTO = false;

    private static final double DISTANCIA_DIBUJO = 48.0D;

    private static final int LUZ_TOTAL = 15728880;

    private record Linea(String texto, int color, float escala) {
    }

    private static final Map<Integer, Float> ENFOQUE = new HashMap<>();

    private static long ultimoCuadro = 0L;

    @SubscribeEvent
    public static void alRenderizar(RenderLevelStageEvent evento) {
        if (evento.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        if (OCULTAR_CON_F1 && mc.options.hideGui) return;

        Map<Integer, DropCliente.Dato> drops = DropCliente.activos();
        if (drops.isEmpty()) return;

        Camera camara = evento.getCamera();
        Vec3 ojo = camara.getPosition();
        Vector3f mira = camara.getLookVector();

        float m11 = evento.getProjectionMatrix().m11();
        if (!(m11 > 0.0F)) m11 = (float) (1.0D / Math.tan(Math.toRadians(35.0D)));

        Vector3f arriba = camara.getUpVector();
        Vector3f izquierda = camara.getLeftVector();

        int altoGui = Math.max(1, mc.getWindow().getGuiScaledHeight());

        float partialTick = evento.getPartialTick();

        PoseStack pose = evento.getPoseStack();
        MultiBufferSource.BufferSource buffers = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        Font fuente = mc.font;

        long ahora = System.nanoTime();
        float pasado = ultimoCuadro == 0L ? 0.0F : Math.min(0.1F, (ahora - ultimoCuadro) / 1.0E9F);
        ultimoCuadro = ahora;

        ENFOQUE.keySet().retainAll(drops.keySet());

        PoseStack vista = RenderSystem.getModelViewStack();
        vista.pushPose();
        vista.setIdentity();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        boolean habiaProfundidad = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        if (A_TRAVES_DE_PAREDES) RenderSystem.disableDepthTest();

        for (Map.Entry<Integer, DropCliente.Dato> entrada : drops.entrySet()) {
            Vec3 ancla = DropCliente.techo(entrada.getKey(), entrada.getValue(), partialTick)
                    .add(0.0D, SEPARACION_HITBOX, 0.0D);

            dibujar(mc, pose, buffers, fuente, camara, ojo, mira, arriba, izquierda, m11, altoGui,
                    ancla, entrada.getKey(), entrada.getValue(), pasado);
        }

        buffers.endBatch();

        if (habiaProfundidad) {
            RenderSystem.enableDepthTest();
        } else {
            RenderSystem.disableDepthTest();
        }

        vista.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    private static void dibujar(Minecraft mc, PoseStack pose, MultiBufferSource buffers, Font fuente,
                                Camera camara, Vec3 ojo, Vector3f mira, Vector3f arriba, Vector3f izquierda,
                                float m11, int altoGui, Vec3 ancla, int id, DropCliente.Dato dato, float pasado) {

        Vec3 rel = ancla.subtract(ojo);
        double distancia = rel.length();
        if (distancia < 1.0E-3D) return;

        double profundidad = rel.x * mira.x() + rel.y * mira.y() + rel.z * mira.z();
        if (profundidad <= 0.05D) return;

        double factor = Math.min(distancia, DISTANCIA_DIBUJO) / distancia;
        Vec3 dibujo = rel.scale(factor);

        float pixel = TAMANIO_FIJO_EN_EL_MUNDO
                ? (float) (BLOQUES_POR_PIXEL * factor * ajusteDeTamanio(profundidad))
                : (float) (2.0D * profundidad * factor / (m11 * altoGui));

        List<Linea> lineas = new ArrayList<>();

        if (MOSTRAR_NOMBRE && !NOMBRE.isEmpty()) {
            lineas.add(new Linea(NOMBRE, COLOR_NOMBRE, ESCALA_NOMBRE));
        }

        if (MOSTRAR_DISTANCIA) {
            double metros = mc.player.position().distanceTo(ancla);
            String texto = String.format(Locale.ROOT, "%." + Math.max(0, DECIMALES_DISTANCIA) + "f", metros);
            lineas.add(new Linea(texto + SUFIJO_DISTANCIA, COLOR_DISTANCIA, ESCALA_DISTANCIA));
        }

        if (MOSTRAR_CONTADOR) {
            int ticks = (int) Math.max(0L, dato.tiempoApertura() - mc.level.getGameTime());
            if (ticks > 0) lineas.add(new Linea(formatear(ticks), COLOR_CONTADOR, ESCALA_CONTADOR));
        }

        float anchoIcono = TEXTURA_ANCHO * ESCALA_ICONO;
        float altoIcono = TEXTURA_ALTO * ESCALA_ICONO;

        float altoTexto = 0.0F;
        for (int i = 0; i < lineas.size(); i++) {
            altoTexto += fuente.lineHeight * ESCALA_TEXTO * lineas.get(i).escala();
            if (i > 0) altoTexto += HUECO_ENTRE_LINEAS;
        }

        float hueco = lineas.isEmpty() ? 0.0F : HUECO_TRAS_MARCA;
        float total = altoIcono + hueco + altoTexto;

        float mirando = 0.0F;
        if (OPACO_AL_MIRARLO) {
            boolean loMiras = loEstasMirando(dibujo, arriba, izquierda, pixel, fuente, lineas,
                    anchoIcono, altoIcono, altoTexto, hueco, total);
            mirando = suavizar(id, loMiras ? 1.0F : 0.0F, pasado);
        }

        float alfaIcono = OPACIDAD_ICONO + (OPACIDAD_ICONO_MIRANDO - OPACIDAD_ICONO) * mirando;
        float alfaTexto = OPACIDAD_TEXTO + (OPACIDAD_TEXTO_MIRANDO - OPACIDAD_TEXTO) * mirando;

        pose.pushPose();
        pose.translate(dibujo.x, dibujo.y, dibujo.z);
        pose.mulPose(camara.rotation());
        pose.scale(-pixel, -pixel, pixel);

        float y = -total;

        if (TEXTO_DEBAJO) {
            icono(pose, buffers, anchoIcono, altoIcono, y, alfaIcono);
            y += altoIcono + hueco;
            textos(pose, buffers, fuente, lineas, y, alfaTexto);
        } else {
            textos(pose, buffers, fuente, lineas, y, alfaTexto);
            y += altoTexto + hueco;
            icono(pose, buffers, anchoIcono, altoIcono, y, alfaIcono);
        }

        pose.popPose();
    }

    private static double ajusteDeTamanio(double profundidad) {
        if (DISTANCIA_TAMANIO_NORMAL <= 0.0D) return 1.0D;

        double proporcion = profundidad / DISTANCIA_TAMANIO_NORMAL;

        if (NO_ACHICAR_DE_LEJOS && proporcion > 1.0D) return proporcion;
        if (NO_CRECER_DE_CERCA && proporcion < 1.0D) return proporcion;

        return 1.0D;
    }

    private static boolean loEstasMirando(Vec3 dibujo, Vector3f arriba, Vector3f izquierda, float pixel,
                                          Font fuente, List<Linea> lineas,
                                          float anchoIcono, float altoIcono, float altoTexto,
                                          float hueco, float total) {
        if (pixel <= 0.0F) return false;

        double derecha = -(dibujo.x * izquierda.x() + dibujo.y * izquierda.y() + dibujo.z * izquierda.z());
        double alto = dibujo.x * arriba.x() + dibujo.y * arriba.y() + dibujo.z * arriba.z();

        double miraX = -derecha / pixel;
        double miraY = alto / pixel;

        float iconoY = TEXTO_DEBAJO ? -total : -total + altoTexto + hueco;
        float textoY = TEXTO_DEBAJO ? -total + altoIcono + hueco : -total;

        float izq = -anchoIcono / 2.0F + ICONO_OFFSET_X;
        float der = izq + anchoIcono;
        float sup = iconoY + ICONO_OFFSET_Y;
        float inf = sup + altoIcono;

        if (!lineas.isEmpty()) {
            float anchoTexto = 0.0F;
            for (Linea linea : lineas) {
                anchoTexto = Math.max(anchoTexto, fuente.width(linea.texto()) * ESCALA_TEXTO * linea.escala());
            }

            izq = Math.min(izq, -anchoTexto / 2.0F + TEXTO_OFFSET_X);
            der = Math.max(der, anchoTexto / 2.0F + TEXTO_OFFSET_X);
            sup = Math.min(sup, textoY + TEXTO_OFFSET_Y);
            inf = Math.max(inf, textoY + TEXTO_OFFSET_Y + altoTexto);
        }

        return miraX >= izq - MARGEN_MIRA && miraX <= der + MARGEN_MIRA
                && miraY >= sup - MARGEN_MIRA && miraY <= inf + MARGEN_MIRA;
    }

    private static float suavizar(int id, float objetivo, float pasado) {
        if (TRANSICION_SEGUNDOS <= 0.0F) {
            ENFOQUE.put(id, objetivo);
            return objetivo;
        }

        float actual = ENFOQUE.getOrDefault(id, objetivo);
        float paso = pasado / TRANSICION_SEGUNDOS;

        if (actual < objetivo) actual = Math.min(objetivo, actual + paso);
        else if (actual > objetivo) actual = Math.max(objetivo, actual - paso);

        ENFOQUE.put(id, actual);
        return actual;
    }

    private static void icono(PoseStack pose, MultiBufferSource buffers, float ancho, float alto, float y,
                              float opacidad) {
        RenderType tipo = A_TRAVES_DE_PAREDES ? RenderType.textSeeThrough(TEXTURA) : RenderType.text(TEXTURA);
        VertexConsumer vc = buffers.getBuffer(tipo);

        Matrix4f m = pose.last().pose();

        float x0 = -ancho / 2.0F + ICONO_OFFSET_X;
        float x1 = x0 + ancho;
        float y0 = y + ICONO_OFFSET_Y;
        float y1 = y0 + alto;

        int a = Math.max(0, Math.min(255, Math.round(opacidad * 255.0F)));

        vc.vertex(m, x0, y0, 0.0F).color(255, 255, 255, a).uv(0.0F, 0.0F).uv2(LUZ_TOTAL).endVertex();
        vc.vertex(m, x0, y1, 0.0F).color(255, 255, 255, a).uv(0.0F, 1.0F).uv2(LUZ_TOTAL).endVertex();
        vc.vertex(m, x1, y1, 0.0F).color(255, 255, 255, a).uv(1.0F, 1.0F).uv2(LUZ_TOTAL).endVertex();
        vc.vertex(m, x1, y0, 0.0F).color(255, 255, 255, a).uv(1.0F, 0.0F).uv2(LUZ_TOTAL).endVertex();
    }

    private static void textos(PoseStack pose, MultiBufferSource buffers, Font fuente, List<Linea> lineas, float y,
                               float opacidad) {
        Font.DisplayMode modo = A_TRAVES_DE_PAREDES ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL;

        for (Linea linea : lineas) {
            float escala = ESCALA_TEXTO * linea.escala();
            float ancho = fuente.width(linea.texto()) * escala;

            pose.pushPose();
            pose.translate(-ancho / 2.0F + TEXTO_OFFSET_X, y + TEXTO_OFFSET_Y, 0.0F);
            pose.scale(escala, escala, escala);

            fuente.drawInBatch(linea.texto(), 0.0F, 0.0F, conAlfa(linea.color(), opacidad), SOMBRA_TEXTO,
                    pose.last().pose(), buffers, modo, 0, LUZ_TOTAL);

            pose.popPose();

            y += fuente.lineHeight * escala + HUECO_ENTRE_LINEAS;
        }
    }

    private static int conAlfa(int color, float opacidad) {
        int a = Math.max(26, Math.min(255, Math.round(opacidad * 255.0F)));
        return (a << 24) | (color & 0xFFFFFF);
    }

    private static String formatear(int ticks) {
        int segundos = (ticks + 19) / 20;
        return String.format("%d:%02d", segundos / 60, segundos % 60);
    }
}