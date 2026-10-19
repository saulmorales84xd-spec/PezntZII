package net.saullmc.pezntz.client.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.block.custom.CofreCandado;
import net.saullmc.pezntz.block.entity.CofreCandadoEntity;
import net.saullmc.pezntz.entity.client.ModModelLayers;

public class CofreCandadoRenderer implements BlockEntityRenderer<CofreCandadoEntity> {

    public static final ResourceLocation TEXTURA_IZQUIERDA =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/cofre_candado_izquierda.png");

    public static final ResourceLocation TEXTURA_DERECHA =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/cofre_candado_derecha.png");

    public static final ResourceLocation TEXTURA = TEXTURA_IZQUIERDA;

    public static final float APERTURA_TAPA = 0.0F;

    private final ModelPart tapa;
    private final ModelPart base;
    private final ModelPart cerradura;

    public CofreCandadoRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart raiz = context.bakeLayer(ModModelLayers.COFRE_CANDADO_LAYER);

        this.tapa = raiz.getChild(CofreCandadoModel.TAPA);
        this.base = raiz.getChild(CofreCandadoModel.BASE);
        this.cerradura = raiz.getChild(CofreCandadoModel.CERRADURA);
    }

    @Override
    public void render(CofreCandadoEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        poseStack.pushPose();
        Direction cara = be.getBlockState().getValue(CofreCandado.FACING);

        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-cara.toYRot()));
        poseStack.translate(-0.5F, -0.5F, -0.5F);

        float giro = -(APERTURA_TAPA * ((float) Math.PI / 2F));
        this.tapa.xRot = giro;
        this.cerradura.xRot = giro;

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURA));

        this.tapa.render(poseStack, buffer, packedLight, packedOverlay);
        this.cerradura.render(poseStack, buffer, packedLight, packedOverlay);
        this.base.render(poseStack, buffer, packedLight, packedOverlay);

        poseStack.popPose();
    }
}