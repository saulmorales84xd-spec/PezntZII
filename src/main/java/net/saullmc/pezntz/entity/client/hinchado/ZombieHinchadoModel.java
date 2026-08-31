package net.saullmc.pezntz.entity.client.hinchado;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.saullmc.pezntz.entity.animations.ModAnimationDefinitions;
import net.saullmc.pezntz.entity.custom.ZombieHinchado;

public class ZombieHinchadoModel<T extends Entity> extends HierarchicalModel<T> {

	private final ModelPart bone;
	private final ModelPart head;
	private final ModelPart head_1;
	private final ModelPart head_2;
	private final ModelPart body;
	private final ModelPart body_1;
	private final ModelPart body_2;
	private final ModelPart body_3;
	private final ModelPart body_4;
	private final ModelPart body_5;
	private final ModelPart body_6;
	private final ModelPart right_arm;
	private final ModelPart right_arm_1;
	private final ModelPart right_arm_2;
	private final ModelPart left_arm;
	private final ModelPart left_arm_2;
	private final ModelPart left_arm_1;
	private final ModelPart left_leg;
	private final ModelPart right_leg;

	public ZombieHinchadoModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.head = this.bone.getChild("head");
		this.head_1 = this.head.getChild("head_1");
		this.head_2 = this.head.getChild("head_2");
		this.body = this.bone.getChild("body");
		this.body_1 = this.body.getChild("body_1");
		this.body_2 = this.body.getChild("body_2");
		this.body_3 = this.body.getChild("body_3");
		this.body_4 = this.body.getChild("body_4");
		this.body_5 = this.body.getChild("body_5");
		this.body_6 = this.body.getChild("body_6");
		this.right_arm = this.bone.getChild("right_arm");
		this.right_arm_1 = this.right_arm.getChild("right_arm_1");
		this.right_arm_2 = this.right_arm.getChild("right_arm_2");
		this.left_arm = this.bone.getChild("left_arm");
		this.left_arm_2 = this.left_arm.getChild("left_arm_2");
		this.left_arm_1 = this.left_arm.getChild("left_arm_1");
		this.left_leg = this.bone.getChild("left_leg");
		this.right_leg = this.bone.getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(-4.0F, 24.0F, 0.0F));

		PartDefinition head = bone.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(7.0F, -24.0F, -4.0F));

		PartDefinition head_1 = head.addOrReplaceChild("head_1", CubeListBuilder.create().texOffs(64, 26).addBox(-1.0F, -9.0F, -1.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 0.0F, 0.0F));

		PartDefinition head_2 = head.addOrReplaceChild("head_2", CubeListBuilder.create().texOffs(64, 42).addBox(-2.0F, -4.0F, -1.5F, 10.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 0.0F, 0.0F));

		PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(4.0F, -15.098F, -2.211F));

		PartDefinition body_1 = body.addOrReplaceChild("body_1", CubeListBuilder.create().texOffs(62, 53).addBox(-7.0F, -9.902F, -3.789F, 14.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body_2 = body.addOrReplaceChild("body_2", CubeListBuilder.create(), PartPose.offset(3.1585F, -4.7367F, -4.112F));

		PartDefinition cube_r1 = body_2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 76).addBox(-3.0F, -3.0F, -1.5F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3415F, -1.1654F, -0.177F, -0.1487F, -0.2564F, 0.1798F));

		PartDefinition body_3 = body.addOrReplaceChild("body_3", CubeListBuilder.create(), PartPose.offset(-3.1585F, -4.7367F, -4.112F));

		PartDefinition cube_r2 = body_3.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(42, 76).addBox(-3.0F, -3.0F, -1.5F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3415F, -1.1654F, -0.177F, -0.1487F, 0.2564F, -0.1798F));

		PartDefinition body_4 = body.addOrReplaceChild("body_4", CubeListBuilder.create(), PartPose.offset(0.0F, 1.3972F, 1.2364F));

		PartDefinition cube_r3 = body_4.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 26).addBox(-8.0F, -11.0F, -7.0F, 16.0F, 11.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.7008F, -0.5254F, 0.0873F, 0.0F, 0.0F));

		PartDefinition body_5 = body.addOrReplaceChild("body_5", CubeListBuilder.create(), PartPose.offset(0.0F, 6.8801F, 1.3658F));

		PartDefinition cube_r4 = body_5.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -10.0F, -7.0F, 18.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.2179F, -0.1548F, 0.2618F, 0.0F, 0.0F));

		PartDefinition body_6 = body.addOrReplaceChild("body_6", CubeListBuilder.create().texOffs(0, 53).addBox(-10.0F, -0.902F, -1.089F, 20.0F, 12.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm = bone.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-7.5F, -17.0F, -4.0F));

		PartDefinition right_arm_1 = right_arm.addOrReplaceChild("right_arm_1", CubeListBuilder.create(), PartPose.offset(1.5F, 1.0F, 0.0F));

		PartDefinition cube_r5 = right_arm_1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(68, 13).addBox(-3.0F, -7.0F, -1.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -4.0F, 1.0F, 0.0F, 0.0F, 1.0036F));

		PartDefinition right_arm_2 = right_arm.addOrReplaceChild("right_arm_2", CubeListBuilder.create(), PartPose.offset(1.5F, 1.0F, 0.0F));

		PartDefinition cube_r6 = right_arm_2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(76, 81).addBox(-3.0F, -4.0F, -1.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -1.0F, 2.0F, 0.0F, 0.0F, 0.7418F));

		PartDefinition left_arm = bone.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(15.5F, -17.0F, -4.0F));

		PartDefinition left_arm_2 = left_arm.addOrReplaceChild("left_arm_2", CubeListBuilder.create(), PartPose.offset(-1.5F, 1.0F, 0.0F));

		PartDefinition cube_r7 = left_arm_2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(60, 81).addBox(-1.0F, -4.0F, -1.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -1.0F, 2.0F, 0.0F, 0.0F, -0.7418F));

		PartDefinition left_arm_1 = left_arm.addOrReplaceChild("left_arm_1", CubeListBuilder.create(), PartPose.offset(-1.5F, 1.0F, 0.0F));

		PartDefinition cube_r8 = left_arm_1.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(68, 0).addBox(-3.0F, -7.0F, -1.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -4.0F, 1.0F, 0.0F, 0.0F, -1.0036F));

		PartDefinition left_leg = bone.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(62, 71).addBox(-3.0F, -5.0F, -2.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(40, 85).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -1.0F, 0.0F));

		PartDefinition right_leg = bone.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(24, 85).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 76).addBox(-3.0F, -5.0F, -2.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		ZombieHinchado zombie = (ZombieHinchado) entity;

		this.animate(zombie.idleAnimationState, ModAnimationDefinitions.idle, ageInTicks, 1f);
		this.animate(zombie.walkAnimationState, ModAnimationDefinitions.walk, ageInTicks, 1f);
		this.animate(zombie.runAnimationState, ModAnimationDefinitions.run, ageInTicks, 1f);
		this.animate(zombie.attackAnimationState, ModAnimationDefinitions.attack, ageInTicks, 1f);
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