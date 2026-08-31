package net.saullmc.pezntz.entity.client.parasitador;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.saullmc.pezntz.entity.animations.ZombiePlagaAnimationDefinitions;
import net.saullmc.pezntz.entity.custom.ZombieParasitador;

public class ZombieParasitadorModel<T extends Entity> extends HierarchicalModel<T> {
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
    private final ModelPart left_leg_1;
    private final ModelPart left_leg_2;
    private final ModelPart right_leg;
    private final ModelPart right_leg_1;
    private final ModelPart right_leg_2;

    public ZombieParasitadorModel(ModelPart root) {
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
        this.left_leg_1 = this.left_leg.getChild("left_leg_1");
        this.left_leg_2 = this.left_leg.getChild("left_leg_2");
        this.right_leg = this.bone.getChild("right_leg");
        this.right_leg_1 = this.right_leg.getChild("right_leg_1");
        this.right_leg_2 = this.right_leg.getChild("right_leg_2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(-1.0F, 24.0F, 0.0F));

        PartDefinition head = bone.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, -1.1667F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(24, 16).addBox(1.0F, -4.0F, -2.1667F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 26).addBox(-5.0F, -3.0F, -2.1667F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(2.0F, -2.0F, -3.1667F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(52, 36).addBox(-4.0F, -4.0F, -3.1667F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(48, 47).addBox(-6.0F, 0.0F, -3.1667F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7F, -28.0F, -3.8333F, 0.1745F, 0.0F, 0.1309F));

        PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(4.0F, -12.0F, 0.0F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -12.0F, 0.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, -1.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition left_arm = bone.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(8.0F, -11.0F, 0.0F));

        PartDefinition left_arm_1 = left_arm.addOrReplaceChild("left_arm_1", CubeListBuilder.create().texOffs(44, 11).addBox(-2.25F, -2.25F, -2.5F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-2.25F, -3.25F, -1.5F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, -8.75F, 0.5F, -0.2618F, 0.0F, 0.0F));

        PartDefinition left_arm_2 = left_arm.addOrReplaceChild("left_arm_2", CubeListBuilder.create(), PartPose.offset(1.0F, -2.0F, -2.0F));

        PartDefinition cube_r2 = left_arm_2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -0.5F, 1.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition right_arm = bone.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -11.0F, 0.0F));

        PartDefinition right_arm_1 = right_arm.addOrReplaceChild("right_arm_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = right_arm_1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(20, 36).addBox(-2.0F, -3.5F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -8.5F, -1.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition right_arm_2 = right_arm.addOrReplaceChild("right_arm_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r4 = right_arm_2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(44, 27).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -3.0F, -2.6F, -0.3491F, 0.0F, 0.0F));

        PartDefinition left_leg = bone.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(4.0F, 0.0F, 0.0F));

        PartDefinition left_leg_1 = left_leg.addOrReplaceChild("left_leg_1", CubeListBuilder.create().texOffs(0, 42).addBox(-2.0F, -0.5F, 0.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -11.5F, -1.0F));

        PartDefinition left_leg_2 = left_leg.addOrReplaceChild("left_leg_2", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, -1.0F));

        PartDefinition cube_r5 = left_leg_2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(32, 47).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 2.5F, 2.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition right_leg = bone.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg_1 = right_leg.addOrReplaceChild("right_leg_1", CubeListBuilder.create().texOffs(36, 36).addBox(-1.75F, -0.75F, -0.5F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(44, 19).addBox(-2.75F, 1.25F, 0.5F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.25F, -11.25F, -0.5F));

        PartDefinition right_leg_2 = right_leg.addOrReplaceChild("right_leg_2", CubeListBuilder.create(), PartPose.offset(-1.0F, -4.5F, -1.0F));

        PartDefinition cube_r6 = right_leg_2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(16, 47).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 2.0F, 0.1745F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }


    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);

        if (entity instanceof ZombieParasitador zombie) {
            this.animate(zombie.idleAnimationState, ZombiePlagaAnimationDefinitions.idle, ageInTicks, 1f);
            this.animate(zombie.walkAnimationState, ZombiePlagaAnimationDefinitions.walk, ageInTicks, 1f);
            this.animate(zombie.attackAnimationState, ZombiePlagaAnimationDefinitions.attack, ageInTicks, 1f);
        }
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
