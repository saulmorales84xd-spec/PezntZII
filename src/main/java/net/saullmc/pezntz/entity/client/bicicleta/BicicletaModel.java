package net.saullmc.pezntz.entity.client.bicicleta;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.saullmc.pezntz.entity.animations.BicicletaAnimationDefinitions;
import net.saullmc.pezntz.entity.custom.Bicicleta;

public class BicicletaModel<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart raiz;

    public BicicletaModel(ModelPart root) {
        this.raiz = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone3 = partdefinition.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 12).addBox(11.5F, -17.125F, -3.0F, 8.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(30, 20).addBox(11.5F, -6.125F, -2.0F, 11.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 17).addBox(11.5F, -6.125F, 1.0F, 12.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(14, 51).addBox(9.9F, -7.125F, -3.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 51).addBox(9.9F, -7.125F, 2.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 28).addBox(3.7F, -15.125F, -1.0F, 13.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-11.9F, 24.125F, 0.0F));

        PartDefinition cube_r1 = bone3.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -1.0F, -5.0F, 8.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1F, -19.125F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition cube_r2 = bone3.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(46, 35).addBox(-5.0F, -7.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 39).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(32, 50).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -13.125F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition cube_r3 = bone3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(28, 12).addBox(-11.0F, 0.0F, -1.0F, 11.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5F, -6.125F, -0.5F, 0.0F, 0.0F, 1.1345F));

        PartDefinition cube_r4 = bone3.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 24).addBox(-13.0F, -2.0F, -1.0F, 13.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.5F, -4.125F, 0.0F, 0.0F, 0.0F, 1.9199F));

        PartDefinition cube_r5 = bone3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 20).addBox(-13.0F, -2.0F, -1.0F, 13.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.5F, -4.125F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone6 = bone3.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(52, 4).addBox(-1.5F, -0.75F, -2.25F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(16, 36).addBox(-0.5F, -0.25F, -0.25F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(11.4F, -5.375F, -4.75F));

        PartDefinition bone5 = bone3.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(52, 8).addBox(-1.0F, -5.0F, -3.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(22, 36).addBox(0.0F, -4.5F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(12.9F, 0.875F, 7.0F));

        PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(42, 39).addBox(-3.0021F, -5.1054F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(42, 43).addBox(-3.0021F, 3.1196F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(30, 31).addBox(-3.5021F, -3.5054F, -0.5F, 7.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.2979F, 19.0054F, 0.0F));

        PartDefinition cube_r6 = bone2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 32).addBox(-3.5F, -3.5F, -0.5F, 7.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0021F, -0.0054F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition cube_r7 = bone2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(54, 17).addBox(0.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9979F, 5.1196F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r8 = bone2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(36, 8).addBox(-2.0F, -2.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1021F, -1.0054F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r9 = bone2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(56, 12).addBox(0.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1021F, -3.0054F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r10 = bone2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(16, 47).addBox(-2.0F, -2.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0979F, -1.0054F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r11 = bone2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(52, 51).addBox(0.0F, 0.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9979F, -5.1054F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r12 = bone2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(52, 23).addBox(-3.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0021F, 5.1196F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 44).addBox(-3.0021F, -5.1054F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(46, 31).addBox(-3.0021F, 3.1196F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-3.5021F, -3.5054F, -0.5F, 7.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(9.7021F, 19.0054F, 0.0F));

        PartDefinition cube_r13 = bone.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(30, 31).addBox(-3.5F, -3.5F, -0.5F, 7.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0021F, -0.0054F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition cube_r14 = bone.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 56).addBox(0.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9979F, 5.1196F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r15 = bone.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 40).addBox(-2.0F, -2.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1021F, -1.0054F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r16 = bone.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(10, 57).addBox(0.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1021F, -3.0054F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r17 = bone.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(42, 47).addBox(-2.0F, -2.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0979F, -1.0054F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r18 = bone.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(52, 55).addBox(0.0F, 0.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9979F, -5.1054F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r19 = bone.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(52, 27).addBox(-3.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0021F, 5.1196F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone4 = partdefinition.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(30, 23).addBox(-8.0F, 0.0F, -2.0F, 8.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(30, 27).addBox(12.0F, 0.0F, -2.0F, 8.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.4F, 13.0F, 0.5F));

        PartDefinition cube_r20 = bone4.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(52, 0).addBox(0.0F, 0.0F, -2.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(20.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r21 = bone4.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 48).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r22 = bone4.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r23 = bone4.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(0, 52).addBox(0.0F, 0.0F, -2.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.raiz;
    }

    @Override
    public void setupAnim(T entidad, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        if (entidad instanceof Bicicleta vehiculo) {
            this.animateWalk(BicicletaAnimationDefinitions.drive, vehiculo.getGiroRueda(), 1.0F, 1.5F, 1.5F);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        this.raiz.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}