package net.saullmc.pezntz.entity.client.quad;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.saullmc.pezntz.entity.animations.QuadAnimationDefinitions;
import net.saullmc.pezntz.entity.custom.Quad;

public class QuadModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart bone5;

    public QuadModel(ModelPart root) {
        this.bone5 = root.getChild("bone5");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone5 = partdefinition.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(0, 0).addBox(-22.0F, -11.0F, 1.0F, 18.0F, 5.0F, 34.0F, new CubeDeformation(0.0F))
                .texOffs(66, 81).addBox(-17.0F, -14.0F, 11.0F, 8.0F, 3.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(66, 58).addBox(-19.0F, -19.0F, 10.0F, 12.0F, 5.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, 26.0F, -17.0F));

        PartDefinition cube_r1 = bone5.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(20, 140).addBox(-4.5F, -3.0F, 0.5F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.0F, -11.5956F, 40.759F, 0.0873F, 0.0F, 0.0F));
        PartDefinition cube_r2 = bone5.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(104, 20).addBox(-9.0F, -1.0F, -9.0F, 18.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.0F, -22.0F, 3.0F, 0.2182F, 0.0F, 0.0F));
        PartDefinition cube_r3 = bone5.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(80, 39).addBox(-11.0F, -1.0F, -6.0F, 22.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.0F, -20.8695F, 34.0086F, 0.0436F, 0.0F, 0.0F));
        PartDefinition cube_r4 = bone5.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 111).addBox(-17.0F, -2.0F, -1.0F, 18.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -18.0F, 31.0F, 0.1309F, 0.0F, 0.0F));
        PartDefinition cube_r5 = bone5.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(60, 145).addBox(-5.5F, -1.0F, -0.5F, 9.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 140).addBox(-22.425F, -1.0F, -0.5F, 9.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.55F, -12.475F, 37.075F, 0.3491F, 0.0F, 0.0F));
        PartDefinition cube_r6 = bone5.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(104, 0).addBox(-8.0F, -1.5F, -8.0F, 16.0F, 9.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.0F, -19.5863F, 32.8545F, 0.1309F, 0.0F, 0.0F));
        PartDefinition cube_r7 = bone5.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(54, 89).addBox(-3.0F, 1.0F, 0.0F, 4.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.0F, -24.5F, 7.9F, 0.0894F, -0.2173F, -0.0193F));
        PartDefinition cube_r8 = bone5.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(54, 87).addBox(-1.0F, 1.0F, 0.0F, 4.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -24.5F, 7.9F, 0.0894F, 0.2173F, 0.0193F));
        PartDefinition cube_r9 = bone5.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(54, 94).addBox(-1.0F, -2.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(54, 91).addBox(9.0F, -2.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.0F, -25.5F, 11.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition cube_r10 = bone5.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(84, 139).addBox(-1.0F, -2.0F, -2.0F, 9.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(60, 139).addBox(18.0F, -2.0F, -2.0F, 9.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-26.0F, -15.5F, 40.5F, 0.0436F, 0.0F, 0.0F));
        PartDefinition cube_r11 = bone5.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(148, 45).addBox(-5.0F, -4.0F, -3.0F, 7.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(148, 39).addBox(12.0F, -4.0F, -3.0F, 7.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.0F, -8.0F, -3.5F, 0.0873F, 0.0F, 0.0F));
        PartDefinition cube_r12 = bone5.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 121).addBox(-5.0F, -3.0F, -3.0F, 10.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.0F, -10.0F, -4.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition cube_r13 = bone5.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(128, 33).addBox(-7.0F, -3.0F, -1.0F, 9.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(104, 33).addBox(12.0F, -3.0F, -1.0F, 9.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.0F, -13.5F, -5.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition cube_r14 = bone5.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(80, 53).addBox(-8.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.0F, -23.5F, 11.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition cube_r15 = bone5.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(122, 81).addBox(1.5F, -0.5F, -3.0F, 6.0F, 15.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.5F, -24.0F, 7.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition cube_r16 = bone5.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(128, 139).addBox(-3.5F, -1.0F, -0.5F, 9.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(108, 139).addBox(13.45F, -1.0F, -0.5F, 9.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-22.475F, -12.475F, 27.075F, -0.3491F, 0.0F, 0.0F));
        PartDefinition cube_r17 = bone5.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(96, 104).addBox(-3.5F, -2.5F, -6.0F, 6.0F, 4.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(54, 104).addBox(16.5F, -2.5F, -6.0F, 6.0F, 4.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-22.5F, -15.0F, 1.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition cube_r18 = bone5.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 39).addBox(-22.5F, -3.5F, -5.0F, 26.0F, 5.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -14.5F, 31.9F, 0.0436F, 0.0F, 0.0F));
        PartDefinition cube_r19 = bone5.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(148, 58).addBox(-3.5F, -1.0F, -0.5F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(148, 51).addBox(16.5F, -1.0F, -0.5F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-22.5F, -12.975F, 7.675F, 0.2182F, 0.0F, 0.0F));
        PartDefinition cube_r20 = bone5.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(0, 87).addBox(-2.5F, -1.5F, -7.0F, 14.0F, 11.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.5F, -20.0F, 4.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition cube_r21 = bone5.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 58).addBox(-8.0F, -6.5F, -7.0F, 16.0F, 12.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.0F, -12.0948F, 1.8658F, 0.0873F, 0.0F, 0.0F));

        PartDefinition bone = bone5.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(126, 53).addBox(-2.5006F, 3.0996F, -3.0052F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(80, 145).addBox(-2.5006F, -3.0004F, 3.1198F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(94, 145).addBox(-2.5006F, -3.0004F, -5.1052F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(126, 61).addBox(-2.5006F, -5.1254F, -3.0052F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(38, 123).addBox(-1.9996F, -3.9004F, -4.0052F, 4.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5004F, -7.0996F, 2.0052F));
        PartDefinition cube_r22 = bone.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(148, 139).addBox(-3.0F, 0.0F, 0.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5004F, -3.0254F, -5.1052F, 0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r23 = bone.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(148, 75).addBox(-3.0F, 0.0F, 0.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5004F, -5.1254F, 2.9948F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r24 = bone.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(148, 70).addBox(-4.0F, -2.0F, -3.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5004F, 5.0996F, -3.0052F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r25 = bone.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(148, 65).addBox(-4.0F, -2.0F, 0.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5004F, 5.0996F, 2.9948F, 0.7854F, 0.0F, 0.0F));

        PartDefinition bone2 = bone5.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(126, 69).addBox(-2.4994F, 3.0996F, -3.0052F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(20, 146).addBox(-2.4994F, -3.0004F, 3.1198F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(108, 146).addBox(-2.4994F, -3.0004F, -5.1052F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(134, 123).addBox(-2.4994F, -5.1254F, -3.0052F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(62, 123).addBox(-2.0004F, -3.9004F, -4.0052F, 4.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-24.4996F, -7.0996F, 2.0052F));
        PartDefinition cube_r26 = bone2.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(150, 95).addBox(-3.0F, 0.0F, 0.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4996F, -3.0254F, -5.1052F, 0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r27 = bone2.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(150, 90).addBox(-3.0F, 0.0F, 0.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4996F, -5.1254F, 2.9948F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r28 = bone2.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(150, 85).addBox(-4.0F, -2.0F, -3.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4996F, 5.0996F, -3.0052F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r29 = bone2.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(150, 80).addBox(-4.0F, -2.0F, 0.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4996F, 5.0996F, 2.9948F, 0.7854F, 0.0F, 0.0F));

        PartDefinition bone3 = bone5.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(134, 131).addBox(-2.501F, 3.0996F, -3.0052F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(122, 146).addBox(-2.501F, -3.0004F, 3.1198F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(136, 146).addBox(-2.501F, -3.0004F, -5.1052F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(138, 104).addBox(-2.501F, -5.1254F, -3.0052F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(86, 123).addBox(-2.0F, -3.9004F, -4.0052F, 4.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -7.0996F, 32.0052F));
        PartDefinition cube_r30 = bone3.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(48, 152).addBox(-3.0F, 0.0F, 0.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -3.0254F, -5.1052F, 0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r31 = bone3.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(152, 33).addBox(-3.0F, 0.0F, 0.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -5.1254F, 2.9948F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r32 = bone3.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(150, 149).addBox(-4.0F, -2.0F, -3.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0996F, -3.0052F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r33 = bone3.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(150, 144).addBox(-4.0F, -2.0F, 0.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0996F, 2.9948F, 0.7854F, 0.0F, 0.0F));

        PartDefinition bone4 = bone5.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(138, 112).addBox(-2.499F, 3.0996F, -3.0052F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 147).addBox(-2.499F, -3.0004F, 3.1198F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(34, 147).addBox(-2.499F, -3.0004F, -5.1052F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(38, 139).addBox(-2.499F, -5.1254F, -3.0052F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(110, 123).addBox(-2.0F, -3.9004F, -4.0052F, 4.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-24.5F, -7.0996F, 32.0052F));
        PartDefinition cube_r34 = bone4.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(96, 154).addBox(-3.0F, 0.0F, 0.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -3.0254F, -5.1052F, 0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r35 = bone4.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(14, 154).addBox(-3.0F, 0.0F, 0.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -5.1254F, 2.9948F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r36 = bone4.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(80, 153).addBox(-4.0F, -2.0F, -3.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0996F, -3.0052F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r37 = bone4.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(64, 152).addBox(-4.0F, -2.0F, 0.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0996F, 2.9948F, 0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(T pEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        if (pEntity instanceof Quad quad) {
            this.animateWalk(QuadAnimationDefinitions.drive, quad.getWheelRotation(), 1.0F, 1.5f, 1.5f);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone5.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return bone5;
    }
}