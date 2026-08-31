package net.saullmc.pezntz.entity.client.rata;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.saullmc.pezntz.entity.animations.RataCarroneraAnimationDefinitions;
import net.saullmc.pezntz.entity.custom.RataCarronera;

public class RataCarroneraModel<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart bone;
    private final ModelPart head;
    private final ModelPart head_2;
    private final ModelPart head_3;
    private final ModelPart body;
    private final ModelPart body_1;
    private final ModelPart body_2;
    private final ModelPart body_3;
    private final ModelPart leg_1;
    private final ModelPart leg_1_1;
    private final ModelPart leg_1_2;
    private final ModelPart leg_1_3;
    private final ModelPart leg_2;
    private final ModelPart leg_2_1;
    private final ModelPart leg_2_2;
    private final ModelPart leg_2_3;
    private final ModelPart leg_3;
    private final ModelPart leg_3_1;
    private final ModelPart leg_3_2;
    private final ModelPart leg_3_3;
    private final ModelPart leg_4;
    private final ModelPart leg_4_1;
    private final ModelPart leg_4_2;
    private final ModelPart leg_4_3;
    private final ModelPart cola;
    private final ModelPart cola_1;
    private final ModelPart cola_2;

    public RataCarroneraModel(ModelPart root) {
        this.bone = root.getChild("bone");
        this.head = this.bone.getChild("head");
        this.head_2 = this.head.getChild("head_2");
        this.head_3 = this.head.getChild("head_3");
        this.body = this.bone.getChild("body");
        this.body_1 = this.body.getChild("body_1");
        this.body_2 = this.body.getChild("body_2");
        this.body_3 = this.body.getChild("body_3");
        this.leg_1 = this.bone.getChild("leg_1");
        this.leg_1_1 = this.leg_1.getChild("leg_1_1");
        this.leg_1_2 = this.leg_1.getChild("leg_1_2");
        this.leg_1_3 = this.leg_1.getChild("leg_1_3");
        this.leg_2 = this.bone.getChild("leg_2");
        this.leg_2_1 = this.leg_2.getChild("leg_2_1");
        this.leg_2_2 = this.leg_2.getChild("leg_2_2");
        this.leg_2_3 = this.leg_2.getChild("leg_2_3");
        this.leg_3 = this.bone.getChild("leg_3");
        this.leg_3_1 = this.leg_3.getChild("leg_3_1");
        this.leg_3_2 = this.leg_3.getChild("leg_3_2");
        this.leg_3_3 = this.leg_3.getChild("leg_3_3");
        this.leg_4 = this.bone.getChild("leg_4");
        this.leg_4_1 = this.leg_4.getChild("leg_4_1");
        this.leg_4_2 = this.leg_4.getChild("leg_4_2");
        this.leg_4_3 = this.leg_4.getChild("leg_4_3");
        this.cola = this.bone.getChild("cola");
        this.cola_1 = this.cola.getChild("cola_1");
        this.cola_2 = this.cola.getChild("cola_2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(8.0F, 24.0F, -8.0F));

        PartDefinition head = bone.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-9.0F, -9.5F, -7.75F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(36, 10).addBox(-3.0F, -3.0F, -3.25F, 6.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(30, 18).addBox(-3.0F, -3.0F, -1.25F, 6.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0436F, 0.0F, 0.0F));

        PartDefinition head_2 = head.addOrReplaceChild("head_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.9F, -3.45F));

        PartDefinition cube_r2 = head_2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(20, 33).addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition head_3 = head.addOrReplaceChild("head_3", CubeListBuilder.create(), PartPose.offset(0.0F, 3.95F, -0.85F));

        PartDefinition cube_r3 = head_3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(20, 38).addBox(-2.0F, -2.75F, -3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(52, 48).addBox(-2.0F, -0.75F, -3.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6109F, 0.0F, 0.0F));

        PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(-8.0F, -10.0F, 12.0F));

        PartDefinition body_1 = body.addOrReplaceChild("body_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r4 = body_1.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 33).addBox(0.0F, -1.0F, -5.0F, 0.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -5.0F, 0.2F, -0.0436F, 0.0F, 0.0F));

        PartDefinition cube_r5 = body_1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -4.0F, -5.0F, 8.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0436F, -0.0023F, -0.0173F));

        PartDefinition body_2 = body.addOrReplaceChild("body_2", CubeListBuilder.create(), PartPose.offset(0.0F, -4.4F, -8.6F));

        PartDefinition cube_r6 = body_2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, -3.5F, -4.0F, 7.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 4.4F, 0.6F, 0.1309F, 0.0F, 0.0F));

        PartDefinition cube_r7 = body_2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(20, 43).addBox(0.0F, -1.0F, -4.0F, 0.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition body_3 = body.addOrReplaceChild("body_3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.6F, -13.0F));

        PartDefinition cube_r8 = body_3.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(36, 43).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition leg_1 = bone.addOrReplaceChild("leg_1", CubeListBuilder.create(), PartPose.offset(-2.2F, -3.0F, 2.0F));

        PartDefinition leg_1_1 = leg_1.addOrReplaceChild("leg_1_1", CubeListBuilder.create(), PartPose.offset(-1.6F, -4.0F, -0.5F));

        PartDefinition cube_r9 = leg_1_1.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(52, 10).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6496F, -0.0728F, -0.2173F));

        PartDefinition leg_1_2 = leg_1.addOrReplaceChild("leg_1_2", CubeListBuilder.create(), PartPose.offset(-1.1585F, -0.8181F, -0.4886F));

        PartDefinition cube_r10 = leg_1_2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(12, 45).addBox(-2.0F, -3.0F, -2.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1585F, 0.8181F, 0.4886F, -0.4863F, 0.1546F, -0.0812F));

        PartDefinition leg_1_3 = leg_1.addOrReplaceChild("leg_1_3", CubeListBuilder.create(), PartPose.offset(-0.3753F, 2.0F, -4.6623F));

        PartDefinition cube_r11 = leg_1_3.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(8, 54).addBox(1.2182F, -1.0F, -1.7712F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 57).addBox(0.2182F, -1.0F, -2.7712F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(56, 24).addBox(-0.7818F, -1.0F, -2.7712F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(48, 53).addBox(-1.7818F, -1.0F, -1.7712F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(24, 53).addBox(-1.7818F, -1.0F, 0.2288F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.2182F, 0.0F));

        PartDefinition leg_2 = bone.addOrReplaceChild("leg_2", CubeListBuilder.create(), PartPose.offset(-18.0F, 0.0F, 0.0F));

        PartDefinition leg_2_1 = leg_2.addOrReplaceChild("leg_2_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r12 = leg_2_1.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(52, 41).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8F, -7.0F, 1.5F, 0.6496F, 0.0728F, 0.2173F));

        PartDefinition leg_2_2 = leg_2.addOrReplaceChild("leg_2_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r13 = leg_2_2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 54).addBox(0.0F, -3.0F, -2.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.2F, -3.0F, 2.0F, -0.4863F, -0.1546F, 0.0812F));

        PartDefinition leg_2_3 = leg_2.addOrReplaceChild("leg_2_3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r14 = leg_2_3.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(42, 58).addBox(-1.2182F, -1.0F, -1.7712F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 58).addBox(-0.2182F, -1.0F, -2.7712F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(54, 57).addBox(0.7818F, -1.0F, -2.7712F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(38, 58).addBox(1.7818F, -1.0F, -1.7712F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(36, 53).addBox(-1.2182F, -1.0F, 0.2288F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5753F, -1.0F, -2.6623F, 0.0F, 0.2182F, 0.0F));

        PartDefinition leg_3 = bone.addOrReplaceChild("leg_3", CubeListBuilder.create(), PartPose.offset(-5.0F, -11.0F, 14.5F));

        PartDefinition leg_3_1 = leg_3.addOrReplaceChild("leg_3_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r15 = leg_3_1.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 45).addBox(0.0F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, -0.0873F));

        PartDefinition leg_3_2 = leg_3.addOrReplaceChild("leg_3_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r16 = leg_3_2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 4.2F, 0.5F, -0.3054F, 0.0F, 0.0F));

        PartDefinition leg_3_3 = leg_3.addOrReplaceChild("leg_3_3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r17 = leg_3_3.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 61).addBox(-1.5F, -1.0F, -2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(60, 57).addBox(1.5F, -1.0F, -2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(20, 58).addBox(0.5F, -1.0F, -3.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(14, 58).addBox(-0.5F, -1.0F, -3.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(52, 52).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 10.0F, -2.5F, 0.0F, -0.2182F, 0.0F));

        PartDefinition leg_4 = bone.addOrReplaceChild("leg_4", CubeListBuilder.create(), PartPose.offset(-13.0F, -11.0F, 14.5F));

        PartDefinition leg_4_1 = leg_4.addOrReplaceChild("leg_4_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r18 = leg_4_1.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(52, 32).addBox(-3.0F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0873F));

        PartDefinition leg_4_2 = leg_4.addOrReplaceChild("leg_4_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r19 = leg_4_2.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(56, 17).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 4.2F, 0.5F, -0.3054F, 0.0F, 0.0F));

        PartDefinition leg_4_3 = leg_4.addOrReplaceChild("leg_4_3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r20 = leg_4_3.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(60, 61).addBox(1.5F, -1.0F, -2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 58).addBox(-0.5F, -1.0F, -3.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(26, 58).addBox(0.5F, -1.0F, -3.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(12, 53).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(4, 61).addBox(-1.5F, -1.0F, -2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 10.0F, -2.5F, 0.0F, 0.2182F, 0.0F));

        PartDefinition cola = bone.addOrReplaceChild("cola", CubeListBuilder.create(), PartPose.offset(-7.0F, -9.0F, 17.0F));

        PartDefinition cola_1 = cola.addOrReplaceChild("cola_1", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -1.0F, 0.0F));

        PartDefinition cola_2 = cola.addOrReplaceChild("cola_2", CubeListBuilder.create().texOffs(30, 32).addBox(-1.5F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -1.0F, 8.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T pEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

        RataCarronera rata = (RataCarronera) pEntity;

        // 1. Idle: Velocidad normal (1f)
        this.animate(rata.idleAnimationState, RataCarroneraAnimationDefinitions.idle, ageInTicks, 1f);

        // 2. Walk: ¡VELOCIDAD x5!
        // Compensamos los 4.0F segundos de Blockbench haciéndolo girar 5 veces más rápido.
        this.animate(rata.walkAnimationState, RataCarroneraAnimationDefinitions.walk, ageInTicks, 5f);

        // 3. Attack: Velocidad normal (1f) porque tienes 80 ticks definidos en tu Entidad
        this.animate(rata.attackAnimationState, RataCarroneraAnimationDefinitions.attack, ageInTicks, 1f);
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