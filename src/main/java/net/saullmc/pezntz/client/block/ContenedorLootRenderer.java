package net.saullmc.pezntz.client.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import net.saullmc.pezntz.block.custom.BloqueContenedor;
import net.saullmc.pezntz.block.entity.ContenedorLootEntity;
import org.joml.Matrix4f;

public class ContenedorLootRenderer implements BlockEntityRenderer<ContenedorLootEntity> {

    public static final String FLECHA = "↓";

    public static final int COLOR_FLECHA = 0xFF00FFFF;

    public static final float ESCALA_FLECHA = 0.060F;

    public static final float FLOTE_ALTURA = 0.35F;

    public static final int FLOTE_TICKS = 40;

    public static final boolean FLOTE_SOLO_FLECHA = true;

    public static final float CENTRO_X = 0.5F;
    public static final float ALTURA = 1.35F;
    public static final float CENTRO_Z = 0.5F;

    public static final boolean SUBIR_EN_MUEBLES_ALTOS = true;

    public static final boolean MOSTRAR_RELOJ = true;

    public static final int COLOR_RELOJ = 0xFFFFFFFF;
    public static final float ESCALA_RELOJ = 0.022F;

    public static final String PREFIJO_RELOJ = "";

    public static final boolean SOMBRA_RELOJ = false;

    public static final boolean SOMBRA_FLECHA = false;

    public static final boolean ATRAVIESA_BLOQUES = false;

    public static final double DISTANCIA_MAXIMA = 16.0D;

    private final Font font;

    public ContenedorLootRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
    }

    @Override
    public void render(ContenedorLootEntity contenedor, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        if (contenedor.getLevel() == null) return;

        Minecraft mc = Minecraft.getInstance();
        Camera camara = mc.gameRenderer.getMainCamera();

        Vec3 centro = Vec3.atCenterOf(contenedor.getBlockPos());
        if (camara.getPosition().distanceToSqr(centro) > DISTANCIA_MAXIMA * DISTANCIA_MAXIMA) return;

        boolean saqueado = contenedor.estaSaqueado();

        String reloj = null;
        if (saqueado) {
            if (!MOSTRAR_RELOJ) return;

            int ticks = contenedor.ticksParaRegenerar();
            if (ticks <= 0) return;

            reloj = PREFIJO_RELOJ + formatear(ticks);
        }

        float flote = (saqueado && FLOTE_SOLO_FLECHA) ? 0.0F : flote(contenedor, partialTick);

        float extra = 0.0F;
        if (SUBIR_EN_MUEBLES_ALTOS
                && contenedor.getBlockState().getBlock() instanceof BloqueContenedor mueble) {
            extra = (float) Math.max(0.0D, mueble.getAltoEnBloques() - 1.0D);
        }

        float escala = saqueado ? ESCALA_RELOJ : ESCALA_FLECHA;

        poseStack.pushPose();
        poseStack.translate(CENTRO_X, ALTURA + extra + flote, CENTRO_Z);

        poseStack.mulPose(camara.rotation());

        poseStack.scale(-escala, -escala, escala);

        Matrix4f matriz = poseStack.last().pose();

        if (saqueado) {
            dibujarLinea(reloj, COLOR_RELOJ, SOMBRA_RELOJ, matriz, bufferSource);
        } else {
            dibujarLinea(FLECHA, COLOR_FLECHA, SOMBRA_FLECHA, matriz, bufferSource);
        }

        poseStack.popPose();
    }

    private static float flote(ContenedorLootEntity contenedor, float partialTick) {
        if (FLOTE_ALTURA == 0.0F) return 0.0F;

        long ciclo = Math.max(1L, FLOTE_TICKS);

        double t = (contenedor.getLevel().getGameTime() % ciclo) + partialTick;
        double angulo = (t / (double) ciclo) * (Math.PI * 2.0D);

        return (float) (Math.sin(angulo) * FLOTE_ALTURA);
    }

    private void dibujarLinea(String texto, int color, boolean sombra,
                              Matrix4f matriz, MultiBufferSource bufferSource) {

        float ancho = this.font.width(texto);

        this.font.drawInBatch(texto, -ancho / 2.0F, -this.font.lineHeight / 2.0F, color, sombra,
                matriz, bufferSource,
                ATRAVIESA_BLOQUES ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL,
                0, 15728880);
    }

    private static String formatear(int ticks) {
        int segundos = (ticks + 19) / 20;
        return String.format("%d:%02d", segundos / 60, segundos % 60);
    }

    @Override
    public boolean shouldRenderOffScreen(ContenedorLootEntity contenedor) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return (int) Math.max(64.0D, DISTANCIA_MAXIMA * 2.0D);
    }
}