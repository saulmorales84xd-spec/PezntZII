package net.saullmc.pezntz.client.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.saullmc.pezntz.block.custom.Prensa;
import net.saullmc.pezntz.block.entity.PrensaEntity;
import org.joml.Matrix4f;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class PrensaRenderer extends GeoBlockRenderer<PrensaEntity> {

    /** Posicion. 0.5 / 0.5 es el centro exacto del bloque. */
    public static final float ITEM_X = 0.5F;
    public static final float ITEM_Y = 0.60F;   // altura. Subelo si tu modelo es mas alto.
    public static final float ITEM_Z = 0.5F;

    /** Tamanio. 1.0 es el tamanio de un item en un marco. */
    public static final float ITEM_ESCALA = 0.55F;

    public static final float ITEM_ROT_X = 90.0F;
    public static final float ITEM_ROT_Y = 0.0F;
    public static final float ITEM_ROT_Z = 0.0F;

    public static final boolean ITEM_SIGUE_AL_BLOQUE = true;

    public static final boolean ITEM_GIRA_SOLO = false;
    public static final float ITEM_VELOCIDAD_GIRO = 2.0F;

    public static final boolean ITEM_APILAR = false;
    public static final float ITEM_APILAR_SEPARACION = 0.035F;

    public static final float CONTADOR_X = 0.5F;
    public static final float CONTADOR_Y = 1.35F;   // 1.0 = justo en la cara de arriba
    public static final float CONTADOR_Z = 0.5F;

    public static final float CONTADOR_ESCALA = 0.022F;

    public static final int CONTADOR_COLOR = 0xFFFFFF;
    public static final int CONTADOR_COLOR_LLENO = 0x55FF55;

    public static final boolean CONTADOR_SOMBRA = true;

    public static final boolean CONTADOR_ATRAVIESA_BLOQUES = false;

    public static final double CONTADOR_DISTANCIA_MAXIMA = 20.0D;

    public static final boolean CONTADOR_OCULTAR_SI_VACIO = true;

    public static final String CONTADOR_FORMATO = "(%d/%d)";


    public PrensaRenderer(BlockEntityRendererProvider.Context context) {
        super(new PrensaModel());
    }

    @Override
    public void render(PrensaEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {


        super.render(be, partialTick, poseStack, bufferSource, packedLight, packedOverlay);

        renderChatarraCentro(be, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        renderContador(be, poseStack, bufferSource);
    }

    private void renderChatarraCentro(PrensaEntity be, float partialTick, PoseStack poseStack,
                                      MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        int cantidad = be.getChatarra();
        if (cantidad <= 0) return;

        Item item = be.getItemChatarra();
        if (item == Items.AIR) return;

        ItemStack stack = new ItemStack(item);
        int copias = ITEM_APILAR ? cantidad : 1;

        float giroPropio = 0.0F;
        if (ITEM_GIRA_SOLO && be.getLevel() != null) {
            giroPropio = ((be.getLevel().getGameTime() + partialTick) * ITEM_VELOCIDAD_GIRO) % 360.0F;
        }

        for (int i = 0; i < copias; i++) {
            poseStack.pushPose();

            poseStack.translate(ITEM_X, ITEM_Y + i * ITEM_APILAR_SEPARACION, ITEM_Z);

            if (ITEM_SIGUE_AL_BLOQUE) {
                Direction cara = be.getBlockState().getValue(Prensa.FACING);
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - cara.toYRot()));
            }

            if (ITEM_GIRA_SOLO) {
                poseStack.mulPose(Axis.YP.rotationDegrees(giroPropio));
            }

            poseStack.mulPose(Axis.XP.rotationDegrees(ITEM_ROT_X));
            poseStack.mulPose(Axis.YP.rotationDegrees(ITEM_ROT_Y));
            poseStack.mulPose(Axis.ZP.rotationDegrees(ITEM_ROT_Z));

            poseStack.scale(ITEM_ESCALA, ITEM_ESCALA, ITEM_ESCALA);

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack, ItemDisplayContext.FIXED,
                    packedLight, packedOverlay,
                    poseStack, bufferSource, be.getLevel(),
                    (int) be.getBlockPos().asLong() + i);

            poseStack.popPose();
        }
    }

    private void renderContador(PrensaEntity be, PoseStack poseStack, MultiBufferSource bufferSource) {
        int cantidad = be.getChatarra();
        if (CONTADOR_OCULTAR_SI_VACIO && cantidad <= 0) return;

        Minecraft mc = Minecraft.getInstance();
        Camera camara = mc.gameRenderer.getMainCamera();

        Vec3 centro = Vec3.atCenterOf(be.getBlockPos());
        double limite = CONTADOR_DISTANCIA_MAXIMA * CONTADOR_DISTANCIA_MAXIMA;
        if (camara.getPosition().distanceToSqr(centro) > limite) return;

        String texto = String.format(CONTADOR_FORMATO, cantidad, Prensa.MAX_CHATARRA);

        poseStack.pushPose();
        poseStack.translate(CONTADOR_X, CONTADOR_Y, CONTADOR_Z);

        poseStack.mulPose(camara.rotation());

        poseStack.scale(-CONTADOR_ESCALA, -CONTADOR_ESCALA, CONTADOR_ESCALA);

        Font font = mc.font;
        int ancho = font.width(texto);
        int rgb = be.estaLleno() ? CONTADOR_COLOR_LLENO : CONTADOR_COLOR;
        int color = 0xFF000000 | rgb;

        Matrix4f matriz = poseStack.last().pose();

        font.drawInBatch(texto, -ancho / 2.0F, 0.0F, color, CONTADOR_SOMBRA,
                matriz, bufferSource,
                CONTADOR_ATRAVIESA_BLOQUES ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL,
                0, 15728880);

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(PrensaEntity be) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return (int) Math.max(64.0D, CONTADOR_DISTANCIA_MAXIMA * 2.0D);
    }
}