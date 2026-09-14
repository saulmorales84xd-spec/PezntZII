package net.saullmc.pezntz.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class InfeccionEffect extends MobEffect {

    public InfeccionEffect() {
        super(MobEffectCategory.HARMFUL, 0x6B8E23);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration <= 1;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) return;

        entity.kill();
    }
}