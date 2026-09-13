package net.saullmc.pezntz.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.saullmc.pezntz.effect.ModEffects;
import net.saullmc.pezntz.entity.ModEntities;
import net.saullmc.pezntz.item.ModItems;

public class BolaAcido extends ThrowableItemProjectile {

    private static final double MAX_SPEED = 0.8D;

    private LivingEntity target;

    public BolaAcido(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public BolaAcido(Level pLevel, LivingEntity pShooter) {
        super(ModEntities.BOLA_ACIDO.get(), pShooter, pLevel);
    }

    public void setTarget(LivingEntity pTarget) {
        this.target = pTarget;
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.LANTERN.get();
    }

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide() && this.target != null && this.target.isAlive()) {

            Vec3 targetPos = new Vec3(this.target.getX(), this.target.getY() + (this.target.getBbHeight() / 2.0D), this.target.getZ());
            Vec3 currentPos = this.position();

            Vec3 direction = targetPos.subtract(currentPos).normalize();

            double currentSpeed = Math.min(this.getDeltaMovement().length(), MAX_SPEED);
            if (currentSpeed < 0.1D) currentSpeed = MAX_SPEED;

            this.setDeltaMovement(direction.scale(currentSpeed));
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (pResult.getEntity() instanceof LivingEntity targetHit) {

            targetHit.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
            targetHit.addEffect(new MobEffectInstance(ModEffects.MANCHA_ZOMBIE.get(), 60, 0, false, false, false));
        }
        this.discard();
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!this.level().isClientSide()) {
            this.playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.4F);

            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.ITEM_SLIME,
                        this.getX(), this.getY(), this.getZ(),
                        12, 0.2D, 0.2D, 0.2D, 0.0D);
            }

            this.discard();
        }

        return true;
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.discard();
    }
}