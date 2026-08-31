package net.saullmc.pezntz.entity.client.zumbador;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.saullmc.pezntz.entity.animations.MosquitoZumbadorAnimationDefinitions;
import net.saullmc.pezntz.entity.custom.MosquitoZumbador;

public class MosquitoZumbadorModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart bone;
    private final ModelPart head;
    private final ModelPart head_1;
    private final ModelPart head_2;
    private final ModelPart body;
    private final ModelPart body_1;
    private final ModelPart body_2;
    private final ModelPart body_3;
    private final ModelPart wings;
    private final ModelPart wings_1;
    private final ModelPart wings_2;
    private final ModelPart hwings;
    private final ModelPart hwings_1;
    private final ModelPart hwings_2;
    private final ModelPart legs;
    private final ModelPart legs_1;
    private final ModelPart legs_2;
    private final ModelPart legs_3;

    public MosquitoZumbadorModel(ModelPart root) {
        this.bone = root.getChild("bone");
        this.head = this.bone.getChild("head");
        this.head_1 = this.head.getChild("head_1");
        this.head_2 = this.head.getChild("head_2");
        this.body = this.bone.getChild("body");
        this.body_1 = this.body.getChild("body_1");
        this.body_2 = this.body.getChild("body_2");
        this.body_3 = this.body.getChild("body_3");
        this.wings = this.bone.getChild("wings");
        this.wings_1 = this.wings.getChild("wings_1");
        this.wings_2 = this.wings.getChild("wings_2");
        this.hwings = this.bone.getChild("hwings");
        this.hwings_1 = this.hwings.getChild("hwings_1");
        this.hwings_2 = this.hwings.getChild("hwings_2");
        this.legs = this.bone.getChild("legs");
        this.legs_1 = this.legs.getChild("legs_1");
        this.legs_2 = this.legs.getChild("legs_2");
        this.legs_3 = this.legs.getChild("legs_3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, -6.0F));

        PartDefinition head = bone.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-1.5F, 1.0F, 0.0F));

        PartDefinition head_1 = head.addOrReplaceChild("head_1", CubeListBuilder.create(), PartPose.offset(0.0F, 1.1119F, -2.5302F));

        PartDefinition cube_r1 = head_1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(36, 43).addBox(-1.5F, -1.0F, -6.0F, 3.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.1119F, 2.5302F, 0.5672F, 0.0F, 0.0F));

        PartDefinition head_2 = head.addOrReplaceChild("head_2", CubeListBuilder.create(), PartPose.offset(0.0F, -3.3852F, -3.161F));

        PartDefinition cube_r2 = head_2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 34).addBox(-1.5F, -1.5F, -6.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.8852F, 2.661F, -0.48F, 0.0F, 0.0F));

        PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body_1 = body.addOrReplaceChild("body_1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -2.0F, -1.0F, 5.0F, 5.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body_2 = body.addOrReplaceChild("body_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = body_2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(50, 8).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.5F, 11.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition body_3 = body.addOrReplaceChild("body_3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r4 = body_3.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -2.0F, -6.0F, 4.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 4.0F, 17.0F, -0.5672F, 0.0F, 0.0F));

        PartDefinition wings = bone.addOrReplaceChild("wings", CubeListBuilder.create(), PartPose.offset(-3.0F, -2.0F, 4.0F));

        PartDefinition wings_1 = wings.addOrReplaceChild("wings_1", CubeListBuilder.create(), PartPose.offset(3.0F, 0.0F, 0.0F));

        PartDefinition cube_r5 = wings_1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(32, 0).addBox(0.0F, 0.0F, -4.0F, 12.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition wings_2 = wings.addOrReplaceChild("wings_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r6 = wings_2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 32).addBox(-12.0F, 0.0F, -4.0F, 12.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition hwings = bone.addOrReplaceChild("hwings", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 12.0F));

        PartDefinition hwings_1 = hwings.addOrReplaceChild("hwings_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r7 = hwings_1.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(32, 22).addBox(0.0F, 0.0F, -3.0F, 6.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6545F));

        PartDefinition hwings_2 = hwings.addOrReplaceChild("hwings_2", CubeListBuilder.create(), PartPose.offset(-3.0F, 0.0F, 0.0F));

        PartDefinition cube_r8 = hwings_2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(40, 28).addBox(-6.0F, 0.0F, -3.0F, 6.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6545F));

        PartDefinition legs = bone.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 4.5F));

        PartDefinition legs_1 = legs.addOrReplaceChild("legs_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r9 = legs_1.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(32, 8).addBox(0.0F, 0.0F, -4.5F, 0.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition legs_2 = legs.addOrReplaceChild("legs_2", CubeListBuilder.create().texOffs(18, 40).addBox(-1.5F, 0.0F, -4.5F, 0.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition legs_3 = legs.addOrReplaceChild("legs_3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r10 = legs_3.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 40).addBox(0.0F, -0.5F, -4.5F, 0.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.2182F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T pEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

        MosquitoZumbador mosquito = (MosquitoZumbador) pEntity;

        this.animate(mosquito.idleAnimationState, MosquitoZumbadorAnimationDefinitions.idle, ageInTicks, 1f);
        this.animate(mosquito.flyAnimationState, MosquitoZumbadorAnimationDefinitions.fly, ageInTicks, 1f);
        this.animate(mosquito.attackAnimationState, MosquitoZumbadorAnimationDefinitions.attack, ageInTicks, 1f);
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