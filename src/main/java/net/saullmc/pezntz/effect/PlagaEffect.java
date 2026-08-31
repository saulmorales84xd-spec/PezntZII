package net.saullmc.pezntz.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class PlagaEffect extends MobEffect {
    public PlagaEffect() {
        super(MobEffectCategory.HARMFUL, 0x1a4c19);

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890",
                -1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}