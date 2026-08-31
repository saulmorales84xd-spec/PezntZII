package net.saullmc.pezntz.entity.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class ZombieTanqueAnimationDefinitions {
        public static final AnimationDefinition idle = AnimationDefinition.Builder.withLength(2.0F).looping()
                .addAnimation("head_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();

        public static final AnimationDefinition walk = AnimationDefinition.Builder.withLength(2.0F).looping()
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, -6.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, -6.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.4717F, 0.6518F, -4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(7.4717F, -0.6518F, 4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(7.4717F, 0.6518F, -4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(7.4717F, -0.6518F, 4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(7.4717F, 0.6518F, -4.9574F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -3.0F, -6.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -8.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(12.4717F, 0.6518F, -4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(12.4718F, -0.6518F, 4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(12.4718F, 0.6518F, -4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(12.4718F, -0.6518F, 4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(12.4718F, 0.6518F, -4.9574F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("bodu_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(4.9811F, 0.4352F, -4.9811F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(4.9811F, -0.4352F, 4.9811F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(4.9811F, 0.4352F, -4.9811F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(4.9811F, -0.4352F, 4.9811F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(4.9811F, 0.4352F, -4.9811F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("bodu_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();

        public static final AnimationDefinition attack = AnimationDefinition.Builder.withLength(2.0F)
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(-92.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.degreeVec(-92.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.75F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 6.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, -1.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 6.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, -1.0F, -6.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(-92.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.degreeVec(-92.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.75F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 6.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, -1.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 6.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, -1.0F, -6.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.4717F, 0.6518F, -4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(7.4717F, -0.6518F, 4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(7.4717F, 0.6518F, -4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(7.4717F, -0.6518F, 4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(7.4717F, 0.6518F, -4.9574F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -3.0F, -6.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -8.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(12.4717F, 0.6518F, -4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(12.4718F, -0.6518F, 4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(12.4718F, 0.6518F, -4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(12.4718F, -0.6518F, 4.9574F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(12.4718F, 0.6518F, -4.9574F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("bodu_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(4.9811F, 0.4352F, -4.9811F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(4.9811F, -0.4352F, 4.9811F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(4.9811F, 0.4352F, -4.9811F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(4.9811F, -0.4352F, 4.9811F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(4.9811F, 0.4352F, -4.9811F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("bodu_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
}