package net.saullmc.pezntz.entity.custom;

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

            double currentSpeed = this.getDeltaMovement().length();
            if (currentSpeed < 0.1D) currentSpeed = 1.6D;

            this.setDeltaMovement(direction.scale(currentSpeed));
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (pResult.getEntity() instanceof LivingEntity targetHit) {
            targetHit.hurt(this.damageSources().thrown(this, this.getOwner()), 4.0F);
            targetHit.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 1));
            targetHit.addEffect(new MobEffectInstance(ModEffects.MANCHA_ZOMBIE.get(), 60, 0, false, false, false));
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.discard();
    }
}