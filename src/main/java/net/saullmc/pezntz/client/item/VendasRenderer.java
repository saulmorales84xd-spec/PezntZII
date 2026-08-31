package net.saullmc.pezntz.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.saullmc.pezntz.item.custom.Vendas;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.core.state.BoneSnapshot;
import software.bernie.geckolib.util.RenderUtils;

public class VendasRenderer extends GeoItemRenderer<Vendas> {

    public static final String RIGHT_ARM_BONE = "derecho";

    public static final String LEFT_ARM_BONE = "izquierdo";

    private ItemDisplayContext perspective = ItemDisplayContext.NONE;


    public VendasRenderer() {
        super(new VendasModel());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack,
                             MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        this.perspective = transformType;

        // OJO: aqui NO se decide que animacion suena.
        //
        // renderByItem corre varias veces por frame (mano en primera persona, miniatura de
        // la hotbar, etc.) y todas comparten el mismo AnimationController, porque comparten
        // el mismo stack. Si cada pase pedia una animacion distinta, el controlador hacia
        // setAnimation() dos veces por frame, y eso pone shouldResetTick = true: "use"
        // reiniciaba desde el frame 0 eternamente y parecia congelada.
        //
        // Ahora la bandera se escribe una sola vez por tick, en VendasHandRenderer.
        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }

    private boolean isStaticContext() {
        return this.perspective == ItemDisplayContext.GUI
                || this.perspective == ItemDisplayContext.GROUND
                || this.perspective == ItemDisplayContext.FIXED
                || this.perspective == ItemDisplayContext.HEAD;
    }

    private static void resetToRestPose(GeoBone bone) {
        BoneSnapshot initial = bone.getInitialSnapshot();

        if (initial != null) {
            bone.updatePosition(initial.getOffsetX(), initial.getOffsetY(), initial.getOffsetZ());
            bone.updateRotation(initial.getRotX(), initial.getRotY(), initial.getRotZ());
            bone.updateScale(initial.getScaleX(), initial.getScaleY(), initial.getScaleZ());
        } else {
            bone.updatePosition(0, 0, 0);
            bone.updateRotation(0, 0, 0);
            bone.updateScale(1, 1, 1);
        }
    }

    @Override
    public void renderRecursively(PoseStack poseStack, Vendas animatable, GeoBone bone, RenderType renderType,
                                  MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                                  float partialTick, int packedLight, int packedOverlay,
                                  float red, float green, float blue, float alpha) {

        if (isStaticContext()) {
            resetToRestPose(bone);
        }

        boolean isRightArm = RIGHT_ARM_BONE.equals(bone.getName());
        boolean isLeftArm = LEFT_ARM_BONE.equals(bone.getName());

        if (isRightArm || isLeftArm) {
            bone.setHidden(true);

            if (shouldRenderArms()) {
                renderPlayerArm(poseStack, bone, bufferSource, packedLight, isRightArm);
            }
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender,
                partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    private boolean shouldRenderArms() {
        boolean firstPerson = this.perspective == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || this.perspective == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        if (!firstPerson) return false;

        LocalPlayer player = Minecraft.getInstance().player;
        return player != null;
    }

    private void renderPlayerArm(PoseStack poseStack, GeoBone bone, MultiBufferSource bufferSource,
                                 int packedLight, boolean right) {
        Minecraft mc = Minecraft.getInstance();
        AbstractClientPlayer player = mc.player;
        if (player == null) return;

        EntityRenderer<?> entityRenderer = mc.getEntityRenderDispatcher().getRenderer(player);
        if (!(entityRenderer instanceof PlayerRenderer playerRenderer)) return;

        PlayerModel<AbstractClientPlayer> model = playerRenderer.getModel();

        model.attackTime = 0.0F;
        model.crouching = false;
        model.swimAmount = 0.0F;
        model.setupAnim(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

        ModelPart arm = right ? model.rightArm : model.leftArm;
        ModelPart sleeve = right ? model.rightSleeve : model.leftSleeve;

        arm.xRot = 0.0F; arm.yRot = 0.0F; arm.zRot = 0.0F;
        sleeve.xRot = 0.0F; sleeve.yRot = 0.0F; sleeve.zRot = 0.0F;
        arm.visible = true;
        sleeve.visible = true;

        GeoCube guide = bone.getCubes().isEmpty() ? null : bone.getCubes().get(0);

        poseStack.pushPose();

        RenderUtils.prepMatrixForBone(poseStack, bone);

        if (guide != null) {

            RenderUtils.translateToPivotPoint(poseStack, guide);
            RenderUtils.rotateMatrixAroundCube(poseStack, guide);
            RenderUtils.translateAwayFromPivotPoint(poseStack, guide);

            alignArmToGuide(poseStack, guide, arm);
        } else {
            poseStack.translate(bone.getPivotX() / 16.0F, bone.getPivotY() / 16.0F, bone.getPivotZ() / 16.0F);
        }

        poseStack.scale(-1.0F, -1.0F, 1.0F);

        poseStack.translate(-arm.x / 16.0F, -arm.y / 16.0F, -arm.z / 16.0F);

        ResourceLocation skin = player.getSkinTextureLocation();
        arm.render(poseStack, bufferSource.getBuffer(RenderType.entitySolid(skin)),
                packedLight, OverlayTexture.NO_OVERLAY);
        sleeve.render(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(skin)),
                packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }

    private static void alignArmToGuide(PoseStack poseStack, GeoCube guide, ModelPart arm) {
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;

        for (GeoQuad quad : guide.quads()) {
            if (quad == null) continue;
            for (GeoVertex vertex : quad.vertices()) {
                Vector3f p = vertex.position();
                minX = Math.min(minX, p.x()); maxX = Math.max(maxX, p.x());
                minY = Math.min(minY, p.y()); maxY = Math.max(maxY, p.y());
                minZ = Math.min(minZ, p.z()); maxZ = Math.max(maxZ, p.z());
            }
        }

        if (minX > maxX) return;

        ModelPart.Cube armCube = arm.getRandomCube(RandomSource.create());

        float offsetX = (minX + maxX) * 0.5F + (armCube.minX + armCube.maxX) / 32.0F;
        float offsetY = maxY + armCube.minY / 16.0F;
        float offsetZ = (minZ + maxZ) * 0.5F - (armCube.minZ + armCube.maxZ) / 32.0F;

        poseStack.translate(offsetX, offsetY, offsetZ);
    }
}