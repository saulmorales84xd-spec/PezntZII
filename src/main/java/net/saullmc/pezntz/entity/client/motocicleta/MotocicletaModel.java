package net.saullmc.pezntz.entity.client.motocicleta;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.saullmc.pezntz.entity.animations.MotocicletaAnimationDefinitions;
import net.saullmc.pezntz.entity.custom.Motocicleta;

public class MotocicletaModel<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart raiz;

    public MotocicletaModel(ModelPart root) {
        this.raiz = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone3 = partdefinition.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(-13, -11).addBox(5.3F, -10.5F, 6.0F, 6.0F, 3.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(-7, -6).addBox(5.325F, -8.8F, -7.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 1).addBox(4.8F, -16.175F, 0.775F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 1).addBox(1.375F, -18.0F, 0.775F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(-3, 0).addBox(-2.625F, -18.5F, 0.275F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(-6, -6).addBox(5.8F, -6.0F, 5.0F, 5.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(-6, -6).addBox(10.7F, 0.0F, 5.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(-6, -6).addBox(4.9F, 0.0F, 5.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.8F, 19.0F, 0.0F));

        PartDefinition cube_r1 = bone3.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-6, -6).addBox(-1.0F, -1.0F, -7.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(-6, -6).addBox(4.0F, -1.0F, -7.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.8F, 0.0F, 19.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r2 = bone3.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-3, -3).addBox(-0.5F, -1.0F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(-3, -3).addBox(5.3F, -1.0F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.4F, 1.0F, 5.0F, -1.2217F, 0.0F, 0.0F));

        PartDefinition cube_r3 = bone3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-3, -3).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(-3, -3).addBox(5.3F, -1.0F, 0.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.4F, 1.0F, 13.0F, 1.4399F, 0.0F, 0.0F));

        PartDefinition cube_r4 = bone3.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(-3, 0).addBox(-5.2125F, -1.9125F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 1).addBox(-1.2125F, -1.4125F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 1).addBox(2.2125F, 0.4125F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.5875F, -16.5875F, 1.275F, -3.1416F, 0.0F, 3.1416F));

        PartDefinition cube_r5 = bone3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(-1, 1).addBox(-0.4367F, -1.5657F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.5875F, -16.5875F, 1.275F, 3.1416F, 0.0F, 2.3562F));

        PartDefinition cube_r6 = bone3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(-1, 1).addBox(-3.0F, -1.0F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.8F, -15.175F, 1.275F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r7 = bone3.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(-3, 0).addBox(-4.0F, -17.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(-6, 0).addBox(-7.0F, -15.0F, -1.0F, 9.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(1, 0).addBox(-7.0F, -4.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(1, 0).addBox(0.0F, -4.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.8F, 0.0F, -3.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r8 = bone3.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(-5, -4).addBox(-3.0F, 0.0F, -6.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.3F, -8.8F, -7.0F, 0.5672F, 0.0F, 0.0F));

        PartDefinition cube_r9 = bone3.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(2, 1).addBox(0.0F, -13.0F, -1.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(2, 1).addBox(7.0F, -13.0F, -1.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.3F, 0.0F, -2.5F, -0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r10 = bone3.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(-5, -4).addBox(-2.5F, -3.0F, -1.5F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.8F, -10.4F, -0.5F, -0.1745F, 0.0F, 0.0F));

        PartDefinition cube_r11 = bone3.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(-10, -8).addBox(-3.5F, -2.0F, -3.5F, 7.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.325F, -7.4F, 8.5F, -0.4363F, 0.0F, 0.0F));

        PartDefinition cube_r12 = bone3.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(-7, -5).addBox(-3.5F, -5.0F, -1.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.3F, -5.4F, 3.0F, 0.48F, 0.0F, 0.0F));

        PartDefinition cube_r13 = bone3.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(-9, -7).addBox(-3.5F, -0.0045F, -0.0392F, 7.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.3F, -10.4239F, 19.0923F, 0.1745F, 0.0F, 0.0F));

        PartDefinition cube_r14 = bone3.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(-9, -7).addBox(-3.5F, -2.0F, -4.5F, 7.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.3F, -7.0F, 15.5F, 0.3491F, 0.0F, 0.0F));

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(1, 1).addBox(-0.5052F, -3.9861F, -0.5016F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(-1, -1).addBox(0.4948F, -1.4861F, -1.5016F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(-1, -1).addBox(-1.5052F, -1.4861F, -1.5016F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(-1, -1).addBox(-0.5052F, -0.4861F, -3.5016F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(-1, -1).addBox(-0.5052F, -0.4861F, 0.4984F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5052F, 18.9861F, -2.9984F));

        PartDefinition cube_r15 = bone.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(1, 1).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0052F, 0.0139F, -0.0016F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r16 = bone.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(1, 1).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0052F, 0.0139F, -0.0016F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r17 = bone.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(41, 42).addBox(-3.0F, 3.1125F, -1.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(41, 38).addBox(-3.0F, -5.1125F, -1.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4989F, -0.0279F, -0.0016F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r18 = bone.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(53, 16).addBox(-1.4938F, 3.7364F, -1.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4989F, -0.0279F, -0.0016F, 1.5708F, -0.7854F, -1.5708F));

        PartDefinition cube_r19 = bone.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(35, 7).addBox(-3.0125F, 3.1F, -1.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4989F, -0.0279F, -0.0016F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition cube_r20 = bone.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(55, 11).addBox(-5.7364F, -1.5239F, -1.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(51, 50).addBox(-1.4938F, -5.7364F, -1.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(51, 22).addBox(-1.5062F, 3.7364F, -1.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4989F, -0.0279F, -0.0016F, -1.5708F, -0.7854F, 1.5708F));

        PartDefinition cube_r21 = bone.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(15, 46).addBox(-3.0125F, -5.1F, -1.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4989F, -0.0279F, -0.0016F, -1.5597F, 0.0422F, 1.5721F));

        PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(1, 1).addBox(-0.5022F, -3.9746F, -0.5014F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(-1, -1).addBox(-0.5022F, -0.4746F, -3.5014F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(-1, -1).addBox(-0.5022F, -0.4746F, 0.4986F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(-1, -1).addBox(0.4978F, -1.4746F, -1.5014F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(-1, -1).addBox(-1.5022F, -1.4746F, -1.5014F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5022F, 18.9746F, 19.0014F));

        PartDefinition cube_r22 = bone2.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(1, 1).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0022F, 0.0254F, -0.0014F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r23 = bone2.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(1, 1).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0022F, 0.0254F, -0.0014F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r24 = bone2.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(41, 42).addBox(-3.0F, 3.1125F, -1.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(41, 38).addBox(-3.0F, -5.1125F, -1.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5019F, -0.0164F, -0.0014F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r25 = bone2.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(53, 16).addBox(-1.4938F, 3.7364F, -1.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5019F, -0.0164F, -0.0014F, 1.5708F, -0.7854F, -1.5708F));

        PartDefinition cube_r26 = bone2.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(35, 7).addBox(-3.0125F, 3.1F, -1.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(15, 46).addBox(-3.0125F, -5.1F, -1.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5019F, -0.0164F, -0.0014F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition cube_r27 = bone2.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(55, 11).addBox(-5.7364F, -1.5239F, -1.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(51, 50).addBox(-1.4938F, -5.7364F, -1.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(51, 22).addBox(-1.5062F, 3.7364F, -1.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5019F, -0.0164F, -0.0014F, -1.5708F, -0.7854F, 1.5708F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.raiz;
    }

    @Override
    public void setupAnim(T entidad, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        if (entidad instanceof Motocicleta vehiculo) {
            this.animateWalk(MotocicletaAnimationDefinitions.drive, vehiculo.getGiroRueda(), 1.0F, 1.5F, 1.5F);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        this.raiz.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}