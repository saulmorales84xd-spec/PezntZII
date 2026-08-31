package net.saullmc.pezntz.entity.client.toxico;

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
import net.saullmc.pezntz.entity.animations.ZombieToxicoAnimationDefinitions;
import net.saullmc.pezntz.entity.custom.ZombieToxico;

public class ZombieToxicoModel<T extends Entity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "zombie_toxico"), "main");
    private final ModelPart bone;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart body_1;
    private final ModelPart body_2;
    private final ModelPart left_arm;
    private final ModelPart left_arm_1;
    private final ModelPart left_arm_2;
    private final ModelPart right_arm;
    private final ModelPart right_arm_1;
    private final ModelPart right_arm_2;
    private final ModelPart left_leg;
    private final ModelPart left_leg_1;
    private final ModelPart left_leg_2;
    private final ModelPart right_leg;
    private final ModelPart right_leg_1;
    private final ModelPart right_leg_2;

    public ZombieToxicoModel(ModelPart root) {
        this.bone = root.getChild("bone");
        this.head = this.bone.getChild("head");
        this.body = this.bone.getChild("body");
        this.body_1 = this.body.getChild("body_1");
        this.body_2 = this.body.getChild("body_2");
        this.left_arm = this.bone.getChild("left_arm");
        this.left_arm_1 = this.left_arm.getChild("left_arm_1");
        this.left_arm_2 = this.left_arm.getChild("left_arm_2");
        this.right_arm = this.bone.getChild("right_arm");
        this.right_arm_1 = this.right_arm.getChild("right_arm_1");
        this.right_arm_2 = this.right_arm.getChild("right_arm_2");
        this.left_leg = this.bone.getChild("left_leg");
        this.left_leg_1 = this.left_leg.getChild("left_leg_1");
        this.left_leg_2 = this.left_leg.getChild("left_leg_2");
        this.right_leg = this.bone.getChild("right_leg");
        this.right_leg_1 = this.right_leg.getChild("right_leg_1");
        this.right_leg_2 = this.right_leg.getChild("right_leg_2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(-1.0F, 24.0F, -1.0F));
        PartDefinition head = bone.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.8525F, -8.2852F, -0.2704F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9475F, -23.488F, -2.5479F));
        PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(4.0F, -12.0F, 0.0F));
        PartDefinition body_1 = body.addOrReplaceChild("body_1", CubeListBuilder.create().texOffs(0, 16).addBox(-3.9089F, -10.3755F, -1.3402F, 8.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.2815F, -2.8923F, 1.1628F));
        PartDefinition body_2 = body.addOrReplaceChild("body_2", CubeListBuilder.create().texOffs(40, 16).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.6599F, -0.771F, 1.3862F));
        PartDefinition left_arm = bone.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.2043F, -22.2189F, -3.2705F));
        PartDefinition left_arm_1 = left_arm.addOrReplaceChild("left_arm_1", CubeListBuilder.create().texOffs(24, 27).addBox(-0.7142F, -1.1402F, 0.319F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.9957F, 0.2189F, 1.7705F));
        PartDefinition left_arm_2 = left_arm.addOrReplaceChild("left_arm_2", CubeListBuilder.create().texOffs(28, 37).addBox(-1.4187F, -1.4833F, 0.7523F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5043F, 5.4811F, 2.7295F));
        PartDefinition right_arm = bone.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-7.0156F, -18.1541F, -2.7471F));
        PartDefinition right_arm_1 = right_arm.addOrReplaceChild("right_arm_1", CubeListBuilder.create().texOffs(32, 8).addBox(-0.6403F, -3.4379F, -1.2685F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.9656F, -1.3466F, 2.4844F));
        PartDefinition right_arm_2 = right_arm.addOrReplaceChild("right_arm_2", CubeListBuilder.create().texOffs(0, 39).addBox(-1.0413F, -3.6407F, -0.4421F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.3345F, 1.8716F, 2.8906F));
        PartDefinition left_leg = bone.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(4.0F, 0.0F, 0.0F));
        PartDefinition left_leg_1 = left_leg.addOrReplaceChild("left_leg_1", CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F, -12.0F, -1.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition left_leg_2 = left_leg.addOrReplaceChild("left_leg_2", CubeListBuilder.create().texOffs(16, 37).addBox(-1.5F, -4.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -4.0F, 1.1F));
        PartDefinition right_leg = bone.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition right_leg_1 = right_leg.addOrReplaceChild("right_leg_1", CubeListBuilder.create().texOffs(24, 16).addBox(-2.0F, -0.6197F, -1.6476F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -11.4F, 0.9F));
        PartDefinition right_leg_2 = right_leg.addOrReplaceChild("right_leg_2", CubeListBuilder.create().texOffs(0, 30).addBox(-2.0F, -0.1365F, -2.4168F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -4.9F, 1.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(pNetHeadYaw, pHeadPitch, pAgeInTicks);

        ZombieToxico zombie = (ZombieToxico) pEntity;
        this.animate(zombie.idleAnimationState, ZombieToxicoAnimationDefinitions.idle, pAgeInTicks, 1f);
        this.animate(zombie.walkAnimationState, ZombieToxicoAnimationDefinitions.walk, pAgeInTicks, 1f);
        this.animate(zombie.attackAnimationState, ZombieToxicoAnimationDefinitions.attack, pAgeInTicks, 1f);
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
        return bone; // Corregido: ya no devuelve "null"
    }
}