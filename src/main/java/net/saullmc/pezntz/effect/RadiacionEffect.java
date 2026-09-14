package net.saullmc.pezntz.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class RadiacionEffect extends MobEffect {

    private static final int INTERVALO_BASE = 40;

    private static final float DANIO = 1.0F;

    public RadiacionEffect() {
        super(MobEffectCategory.HARMFUL, 0x7ACB4A);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int intervalo = INTERVALO_BASE >> Math.min(amplifier, 5);
        return intervalo <= 0 || duration % intervalo == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) return;

        entity.hurt(entity.damageSources().magic(), DANIO);
    }
}