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

        PartDefinition bone3 = partdefinition.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 12).addBox(1.245F, -7.3646F, -3.15F, 8.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(30, 20).addBox(1.245F, 3.6354F, -2.15F, 11.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 17).addBox(1.245F, 3.6354F, 0.85F, 12.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(14, 51).addBox(-0.355F, 2.6354F, -3.15F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 51).addBox(-0.355F, 2.6354F, 1.85F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 28).addBox(-6.555F, -5.3646F, -1.15F, 13.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.145F, 14.3646F, 0.15F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r1 = bone3.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -1.0F, -5.0F, 8.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.355F, -9.3646F, -0.15F, 0.0F, 0.0F, 0.1745F));

        PartDefinition cube_r2 = bone3.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(46, 35).addBox(-5.0F, -7.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 39).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(32, 50).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.755F, -3.3646F, -0.15F, 0.0F, 0.0F, 0.1745F));

        PartDefinition cube_r3 = bone3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(28, 12).addBox(-11.0F, 0.0F, -1.0F, 11.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.245F, 3.6354F, -0.65F, 0.0F, 0.0F, 1.1345F));

        PartDefinition cube_r4 = bone3.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 24).addBox(-13.0F, -2.0F, -1.0F, 13.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.245F, 5.6354F, -0.15F, 0.0F, 0.0F, 1.9199F));

        PartDefinition cube_r5 = bone3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 20).addBox(-13.0F, -2.0F, -1.0F, 13.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.245F, 5.6354F, -0.15F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone6 = bone3.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(52, 4).addBox(-1.5F, -0.75F, -2.25F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(16, 36).addBox(-0.5F, -0.25F, -0.25F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.145F, 4.3854F, -4.9F));

        PartDefinition bone5 = bone3.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(52, 8).addBox(-1.0F, -5.0F, -3.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(22, 36).addBox(0.0F, -4.5F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.645F, 10.6354F, 6.85F));

        PartDefinition bone4 = partdefinition.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(30, 23).addBox(-14.0F, -1.3452F, -1.5F, 8.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(30, 27).addBox(6.0F, -1.3452F, -1.5F, 8.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, 14.3452F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r6 = bone4.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(52, 0).addBox(0.0F, 0.0F, -2.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, -1.3452F, 0.5F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r7 = bone4.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 48).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -1.3452F, 0.5F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r8 = bone4.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, -1.3452F, 0.5F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r9 = bone4.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 52).addBox(0.0F, 0.0F, -2.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -1.3452F, 0.5F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(-0.0021F, 19.0F, -10.0021F));

        PartDefinition cube_r10 = bone2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 32).addBox(-3.4947F, -3.5113F, -0.5F, 7.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0071F, 0.0021F, -1.5708F, 1.1345F, -1.5708F));

        PartDefinition cube_r11 = bone2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(30, 31).addBox(-3.5F, -3.5125F, -0.5F, 7.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(42, 43).addBox(-3.0F, 3.1125F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(42, 39).addBox(-3.0F, -5.1125F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0071F, 0.0021F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r12 = bone2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(54, 17).addBox(-1.4938F, 3.7364F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0071F, 0.0021F, -1.5708F, 0.7854F, -1.5708F));

        PartDefinition cube_r13 = bone2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(36, 8).addBox(-3.0125F, 3.1F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 47).addBox(-3.0125F, -5.1F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0071F, 0.0021F, 1.5708F, 0.0F, 1.5708F));

        PartDefinition cube_r14 = bone2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(56, 12).addBox(-5.7364F, -1.5239F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 51).addBox(-1.4938F, -5.7364F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 23).addBox(-1.5062F, 3.7364F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0071F, 0.0021F, 1.5708F, 0.7854F, 1.5708F));

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(-0.0021F, 19.0F, 9.9979F));

        PartDefinition cube_r15 = bone.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 32).addBox(-3.5F, -3.5125F, -0.5F, 7.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(46, 31).addBox(-3.0F, 3.1125F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 44).addBox(-3.0F, -5.1125F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0071F, 0.0021F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r16 = bone.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(30, 31).addBox(-3.5053F, -3.5113F, -0.5F, 7.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0071F, 0.0021F, 1.5708F, 1.1345F, 1.5708F));

        PartDefinition cube_r17 = bone.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 56).addBox(-1.4938F, 3.7364F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0071F, 0.0021F, -1.5708F, 0.7854F, -1.5708F));

        PartDefinition cube_r18 = bone.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0125F, 3.1F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(42, 47).addBox(-3.0125F, -5.1F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0071F, 0.0021F, 1.5708F, 0.0F, 1.5708F));

        PartDefinition cube_r19 = bone.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(10, 57).addBox(-5.7364F, -1.5239F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 55).addBox(-1.4938F, -5.7364F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 27).addBox(-1.5062F, 3.7364F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0071F, 0.0021F, 1.5708F, 0.7854F, 1.5708F));

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