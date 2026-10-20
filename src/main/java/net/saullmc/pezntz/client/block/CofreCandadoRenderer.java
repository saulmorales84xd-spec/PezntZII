package net.saullmc.pezntz.client.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.block.custom.CofreCandado;
import net.saullmc.pezntz.block.entity.CofreCandadoEntity;

public class CofreCandadoRenderer implements BlockEntityRenderer<CofreCandadoEntity> {

    public static final ResourceLocation TEXTURA_IZQUIERDA =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/cofre_protegido_left.png");

    public static final ResourceLocation TEXTURA_DERECHA =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/cofre_protegido_right.png");

    public static final float APERTURA_TAPA = 0.0F;

    private static final String TAPA = "lid";
    private static final String BASE = "bottom";
    private static final String CERRADURA = "lock";

    private final ModelPart tapaIzq, baseIzq, cerraduraIzq;
    private final ModelPart tapaDer, baseDer, cerraduraDer;

    public CofreCandadoRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart raizIzq = context.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT);
        this.tapaIzq = raizIzq.getChild(TAPA);
        this.baseIzq = raizIzq.getChild(BASE);
        this.cerraduraIzq = raizIzq.getChild(CERRADURA);

        ModelPart raizDer = context.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT);
        this.tapaDer = raizDer.getChild(TAPA);
        this.baseDer = raizDer.getChild(BASE);
        this.cerraduraDer = raizDer.getChild(CERRADURA);
    }

    @Override
    public void render(CofreCandadoEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        BlockState estado = be.getBlockState();
        if (!(estado.getBlock() instanceof CofreCandado)) return;

        boolean izquierda = estado.getValue(CofreCandado.TYPE) == ChestType.LEFT;

        ModelPart tapa = izquierda ? this.tapaIzq : this.tapaDer;
        ModelPart base = izquierda ? this.baseIzq : this.baseDer;
        ModelPart cerradura = izquierda ? this.cerraduraIzq : this.cerraduraDer;
        ResourceLocation textura = izquierda ? TEXTURA_IZQUIERDA : TEXTURA_DERECHA;

        poseStack.pushPose();

        Direction cara = estado.getValue(CofreCandado.FACING);

        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-cara.toYRot()));
        poseStack.translate(-0.5F, -0.5F, -0.5F);

        float giro = -(APERTURA_TAPA * ((float) Math.PI / 2F));
        tapa.xRot = giro;
        cerradura.xRot = giro;

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutout(textura));

        tapa.render(poseStack, buffer, packedLight, packedOverlay);
        cerradura.render(poseStack, buffer, packedLight, packedOverlay);
        base.render(poseStack, buffer, packedLight, packedOverlay);

        poseStack.popPose();
    }
}