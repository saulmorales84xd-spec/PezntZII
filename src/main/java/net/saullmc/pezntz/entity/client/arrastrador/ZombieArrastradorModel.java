package net.saullmc.pezntz.entity.client.arrastrador;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.saullmc.pezntz.entity.animations.ZombieArrastradorAnimationDefinitions;
import net.saullmc.pezntz.entity.custom.ZombieArrastrador;

public class ZombieArrastradorModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart bone;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart left_arm;
    private final ModelPart left_arm_1;
    private final ModelPart left_arm_2;
    private final ModelPart right_arm;
    private final ModelPart right_arm_1;
    private final ModelPart right_arm_2;
    private final ModelPart left_leg;
    private final ModelPart right_leg;

    public ZombieArrastradorModel(ModelPart root) {
        this.bone = root.getChild("bone");
        this.head = this.bone.getChild("head");
        this.body = this.bone.getChild("body");
        this.left_arm = this.bone.getChild("left_arm");
        this.left_arm_1 = this.left_arm.getChild("left_arm_1");
        this.left_arm_2 = this.left_arm.getChild("left_arm_2");
        this.right_arm = this.bone.getChild("right_arm");
        this.right_arm_1 = this.right_arm.getChild("right_arm_1");
        this.right_arm_2 = this.right_arm.getChild("right_arm_2");
        this.left_leg = this.bone.getChild("left_leg");
        this.right_leg = this.bone.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.3F));

        PartDefinition head = bone.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0436F, 0.0F, 0.0F));

        PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 1.9581F, 5.7912F));

        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -2.0F, -12.0F, 8.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0419F, 5.9089F, -0.1745F, 0.0F, 0.0F));

        PartDefinition left_arm = bone.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(8.0F, 4.0F, -1.3F));

        PartDefinition left_arm_1 = left_arm.addOrReplaceChild("left_arm_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = left_arm_1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(20, 32).addBox(-2.0F, -3.5F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -1.5F, 3.0F, -1.156F, -0.5392F, -0.3911F));

        PartDefinition left_arm_2 = left_arm.addOrReplaceChild("left_arm_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r4 = left_arm_2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(36, 36).addBox(-3.0F, -4.0F, -1.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition right_arm = bone.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-8.0F, 4.0F, -1.3F));

        PartDefinition right_arm_1 = right_arm.addOrReplaceChild("right_arm_1", CubeListBuilder.create(), PartPose.offset(2.0F, -1.5F, 3.0F));

        PartDefinition cube_r5 = right_arm_1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(32, 16).addBox(-2.0F, -3.5F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.156F, 0.5392F, 0.3911F));

        PartDefinition right_arm_2 = right_arm.addOrReplaceChild("right_arm_2", CubeListBuilder.create(), PartPose.offset(1.0F, -1.0F, -1.5F));

        PartDefinition cube_r6 = right_arm_2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(36, 27).addBox(-1.0F, -4.0F, -1.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, 1.5F, 1.5708F, 0.0F, 0.0F));

        PartDefinition left_leg = bone.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(-1.0F, 5.0F, 12.7F));

        PartDefinition cube_r7 = left_leg.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -2.0F, -1.0F, 0.0F, 0.2618F, 0.0F));

        PartDefinition right_leg = bone.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.0F, 5.0F, 12.7F));

        PartDefinition cube_r8 = right_leg.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(40, 0).addBox(-3.0F, -4.0F, -1.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0349F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(pNetHeadYaw, pHeadPitch, pAgeInTicks);

        ZombieArrastrador zombie = (ZombieArrastrador) pEntity;

        this.animate(zombie.idleAnimationState, ZombieArrastradorAnimationDefinitions.idle, pAgeInTicks, 1f);
        this.animate(zombie.walkAnimationState, ZombieArrastradorAnimationDefinitions.walk, pAgeInTicks, 1f);
        this.animate(zombie.attackAnimationState, ZombieArrastradorAnimationDefinitions.attack, pAgeInTicks, 1f);
    }

    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
        pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return bone;
    }
}