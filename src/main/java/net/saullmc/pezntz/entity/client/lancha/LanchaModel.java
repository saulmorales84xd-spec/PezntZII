package net.saullmc.pezntz.entity.client.lancha;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.saullmc.pezntz.entity.custom.Lancha;

public class LanchaModel<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart raiz;

    public LanchaModel(ModelPart root) {
        this.raiz = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-18.0F, 3.5791F, -22.5978F, 36.0F, 4.0F, 81.0F, new CubeDeformation(0.0F))
                .texOffs(0, 212).addBox(-16.0F, 1.5791F, 23.4022F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 226).addBox(4.0F, 1.5791F, -20.5978F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(150, 228).addBox(-16.0F, 1.5791F, -20.5978F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(234, 59).addBox(4.0F, -8.4209F, 23.4022F, 12.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(198, 228).addBox(-16.0F, 1.5791F, 1.4022F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(234, 0).addBox(4.0F, 1.5791F, 1.4022F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(234, 14).addBox(-6.0F, 1.5791F, 43.4022F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(234, 71).addBox(-16.0F, -8.4209F, 23.4022F, 12.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(234, 28).addBox(4.0F, 1.5791F, 23.4022F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 240).addBox(4.0F, -8.4209F, 1.4022F, 12.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 240).addBox(-16.0F, -8.4209F, 1.4022F, 12.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(56, 241).addBox(4.0F, -8.4209F, -20.5978F, 12.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(84, 241).addBox(-16.0F, -8.4209F, -20.5978F, 12.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 85).addBox(-20.0F, -4.4209F, -22.5978F, 2.0F, 12.0F, 81.0F, new CubeDeformation(0.0F))
                .texOffs(166, 85).addBox(18.0F, -4.4209F, -22.5978F, 2.0F, 12.0F, 81.0F, new CubeDeformation(0.0F))
                .texOffs(86, 178).addBox(-20.0F, -4.4209F, -24.5978F, 40.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(170, 178).addBox(-20.0F, -4.4209F, 58.4022F, 40.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 178).addBox(-18.0F, -2.4209F, 60.4022F, 36.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(86, 192).addBox(-16.0F, -2.4209F, 67.4022F, 32.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(234, 42).addBox(-5.0F, -2.4209F, 81.4022F, 10.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 195).addBox(-12.0F, -2.4209F, 74.4022F, 24.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(164, 242).addBox(-10.0F, -2.4209F, 81.4022F, 5.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(178, 242).addBox(5.0F, -2.4209F, 81.4022F, 5.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(112, 241).addBox(-6.0F, -8.4209F, 43.4022F, 12.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(76, 195).addBox(-8.0F, -2.4209F, 83.4022F, 3.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(192, 242).addBox(5.0F, -2.4209F, 83.4022F, 3.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(56, 212).addBox(5.0F, -2.4209F, 85.4022F, 2.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 223).addBox(-7.0F, -2.4209F, 85.4022F, 2.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(62, 195).addBox(-15.0F, -2.4209F, 74.4022F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(150, 209).addBox(12.0F, -2.4209F, 74.4022F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(218, 242).addBox(-14.0F, -2.4209F, 78.4022F, 2.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(224, 242).addBox(12.0F, -2.4209F, 78.4022F, 2.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(148, 242).addBox(-3.0F, -2.4209F, 88.4022F, 6.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.4209F, 19.5978F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(140, 241).addBox(18.0F, -25.1F, 1.3682F, 2.0F, 23.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 212).addBox(-20.0F, -25.1F, 1.3682F, 2.0F, 23.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5995F, 32.9022F, -1.0647F, 0.0F, 0.0F));

        PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(164, 192).addBox(-20.0F, -4.6977F, 24.5271F, 40.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(210, 242).addBox(18.0F, -2.71F, 24.5168F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(202, 242).addBox(-20.0F, -2.71F, 24.5168F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5995F, 32.9022F, 0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(106, 209).addBox(24.436F, 0.1785F, 21.3866F, 2.0F, 12.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5995F, 32.9022F, 0.0F, -0.2618F, 0.0F));

        PartDefinition cube_r4 = bone.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(62, 209).addBox(-44.5033F, 0.1785F, 20.7059F, 2.0F, 12.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5995F, 32.9022F, 0.0F, 0.829F, 0.0F));

        PartDefinition cube_r5 = bone.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(208, 196).addBox(42.5033F, 0.1785F, 20.7059F, 2.0F, 12.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5995F, 32.9022F, 0.0F, -0.829F, 0.0F));

        PartDefinition cube_r6 = bone.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(164, 196).addBox(-26.436F, 0.1785F, 21.3866F, 2.0F, 12.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5995F, 32.9022F, 0.0F, 0.2618F, 0.0F));

        return LayerDefinition.create(meshdefinition, 512, 512);
    }

    @Override
    public ModelPart root() {
        return this.raiz;
    }

    @Override
    public void setupAnim(T entidad, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        this.raiz.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}