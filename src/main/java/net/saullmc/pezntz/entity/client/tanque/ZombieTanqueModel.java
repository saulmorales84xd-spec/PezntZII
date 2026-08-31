package net.saullmc.pezntz.entity.client.tanque;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.saullmc.pezntz.entity.animations.ZombieTanqueAnimationDefinitions;
import net.saullmc.pezntz.entity.custom.ZombieTanque;

public class ZombieTanqueModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart Zombie_tanque;
    private final ModelPart head;
    private final ModelPart head_1;
    private final ModelPart head_2;
    private final ModelPart body;
    private final ModelPart body_1;
    private final ModelPart bodu_2;
    private final ModelPart right_arm;
    private final ModelPart right_arm_1;
    private final ModelPart right_arm_2;
    private final ModelPart left_arm;
    private final ModelPart left_arm_1;
    private final ModelPart left_arm_2;
    private final ModelPart right_leg;
    private final ModelPart right_leg_1;
    private final ModelPart right_leg_2;
    private final ModelPart left_leg;
    private final ModelPart left_leg_1;
    private final ModelPart left_leg_2;

    public ZombieTanqueModel(ModelPart root) {
        this.Zombie_tanque = root.getChild("Zombie_tanque");
        this.head = this.Zombie_tanque.getChild("head");
        this.head_1 = this.head.getChild("head_1");
        this.head_2 = this.head.getChild("head_2");
        this.body = this.Zombie_tanque.getChild("body");
        this.body_1 = this.body.getChild("body_1");
        this.bodu_2 = this.body.getChild("bodu_2");
        this.right_arm = this.Zombie_tanque.getChild("right_arm");
        this.right_arm_1 = this.right_arm.getChild("right_arm_1");
        this.right_arm_2 = this.right_arm.getChild("right_arm_2");
        this.left_arm = this.Zombie_tanque.getChild("left_arm");
        this.left_arm_1 = this.left_arm.getChild("left_arm_1");
        this.left_arm_2 = this.left_arm.getChild("left_arm_2");
        this.right_leg = this.Zombie_tanque.getChild("right_leg");
        this.right_leg_1 = this.right_leg.getChild("right_leg_1");
        this.right_leg_2 = this.right_leg.getChild("right_leg_2");
        this.left_leg = this.Zombie_tanque.getChild("left_leg");
        this.left_leg_1 = this.left_leg.getChild("left_leg_1");
        this.left_leg_2 = this.left_leg.getChild("left_leg_2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Zombie_tanque = partdefinition.addOrReplaceChild("Zombie_tanque", CubeListBuilder.create(), PartPose.offset(0.0F, -19.5F, -4.5F));

        PartDefinition head = Zombie_tanque.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head_1 = head.addOrReplaceChild("head_1", CubeListBuilder.create().texOffs(80, 76).addBox(-8.0F, -20.25F, -12.9433F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head_2 = head.addOrReplaceChild("head_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = head_2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 76).addBox(-14.0F, -9.302F, 3.9748F, 28.0F, 11.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.829F, 0.0F, 0.0F));

        PartDefinition body = Zombie_tanque.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body_1 = body.addOrReplaceChild("body_1", CubeListBuilder.create(), PartPose.offset(0.0F, 3.4171F, 1.9823F));

        PartDefinition cube_r2 = body_1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-18.0F, -10.1617F, -6.3345F, 36.0F, 28.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0255F, -2.008F, 0.0873F, 0.0F, 0.0F));

        PartDefinition bodu_2 = body.addOrReplaceChild("bodu_2", CubeListBuilder.create(), PartPose.offset(0.0F, 20.5F, 6.5F));

        PartDefinition cube_r3 = bodu_2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 50).addBox(-14.0F, -6.0F, -7.0F, 28.0F, 12.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0076F, 0.0868F, 0.9962F, 0.2618F, 0.0F, 0.0F));

        PartDefinition right_arm = Zombie_tanque.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm_1 = right_arm.addOrReplaceChild("right_arm_1", CubeListBuilder.create(), PartPose.offset(0.0F, -2.22F, -0.8394F));

        PartDefinition cube_r4 = right_arm_1.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(48, 108).addBox(-30.0F, -9.1016F, 0.5792F, 12.0F, 16.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -1.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition right_arm_2 = right_arm.addOrReplaceChild("right_arm_2", CubeListBuilder.create(), PartPose.offset(0.0F, -2.22F, -0.8394F));

        PartDefinition cube_r5 = right_arm_2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(116, 0).addBox(-28.5F, 1.7886F, 1.7196F, 9.0F, 18.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -1.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition left_arm = Zombie_tanque.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm_1 = left_arm.addOrReplaceChild("left_arm_1", CubeListBuilder.create(), PartPose.offset(0.0F, -2.22F, -0.8394F));

        PartDefinition cube_r6 = left_arm_1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 99).addBox(18.0F, -9.1016F, 0.5792F, 12.0F, 16.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -1.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition left_arm_2 = left_arm.addOrReplaceChild("left_arm_2", CubeListBuilder.create(), PartPose.offset(0.0F, -2.22F, -0.8394F));

        PartDefinition cube_r7 = left_arm_2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(96, 108).addBox(19.5F, 1.7886F, 1.7196F, 9.0F, 18.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -1.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition right_leg = Zombie_tanque.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-5.0F, 35.5F, 3.5F));

        PartDefinition right_leg_1 = right_leg.addOrReplaceChild("right_leg_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r8 = right_leg_1.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(84, 50).addBox(-7.0F, -12.0F, -1.0F, 10.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition right_leg_2 = right_leg.addOrReplaceChild("right_leg_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r9 = right_leg_2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 127).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 3.0F, 4.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition left_leg = Zombie_tanque.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(7.0F, 38.5F, 7.5F));

        PartDefinition left_leg_1 = left_leg.addOrReplaceChild("left_leg_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r10 = left_leg_1.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(116, 27).addBox(-3.0F, -12.0F, -1.0F, 10.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -3.0F, -4.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition left_leg_2 = left_leg.addOrReplaceChild("left_leg_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r11 = left_leg_2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(124, 49).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

        ZombieTanque zombie = (ZombieTanque) entity;

        this.animate(zombie.idleAnimationState, ZombieTanqueAnimationDefinitions.idle, ageInTicks, 1f);
        this.animate(zombie.walkAnimationState, ZombieTanqueAnimationDefinitions.walk, ageInTicks, 1f);
        this.animate(zombie.attackAnimationState, ZombieTanqueAnimationDefinitions.attack, ageInTicks, 1f);
    }

    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
        pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Zombie_tanque.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Zombie_tanque;
    }
}