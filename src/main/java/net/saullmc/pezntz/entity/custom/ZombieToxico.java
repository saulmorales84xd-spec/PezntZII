package net.saullmc.pezntz.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class ZombieToxico extends Zombie implements RangedAttackMob {

    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(ZombieToxico.class, EntityDataSerializers.INT);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();

    public int attackAnimationTimeout = 0;

    public ZombieToxico(EntityType<? extends Zombie> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.0D, 80, 15.0F));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            setupAnimationStates();
            if (this.attackAnimationTimeout > 0) {
                this.attackAnimationTimeout--;
            } else {
                this.attackAnimationState.stop();
            }
        }
    }

    private void setupAnimationStates() {
        boolean isMoving = this.walkAnimation.speed() > 0.015F;
        boolean isAttacking = this.attackAnimationTimeout > 0;

        if (isAttacking) {
            this.idleAnimationState.stop();
            this.walkAnimationState.stop();
        } else {
            this.attackAnimationState.stop();
            if (isMoving) {
                this.idleAnimationState.stop();
                this.walkAnimationState.startIfStopped(this.tickCount);
            } else {
                this.walkAnimationState.stop();
                this.idleAnimationState.startIfStopped(this.tickCount);
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        BolaAcido projectile = new BolaAcido(this.level(), this);

        projectile.setTarget(pTarget);

        double d0 = pTarget.getY() + (double)(pTarget.getBbHeight() / 2.0F);
        double d1 = pTarget.getX() - this.getX();
        double d2 = d0 - projectile.getY();
        double d3 = pTarget.getZ() - this.getZ();

        projectile.shoot(d1, d2, d3, 1.6F, 0.0F);

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.SPLASH_POTION_THROW, SoundSource.HOSTILE, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));

        this.level().addFreshEntity(projectile);

        this.level().broadcastEntityEvent(this, (byte) 4);
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 4) {
            if (this.attackAnimationTimeout <= 0) {
                this.attackAnimationState.start(this.tickCount);
                this.attackAnimationTimeout = 20;
            }
        } else {
            super.handleEntityEvent(pId);
        }
    }

    @Override
    protected boolean isSunSensitive() { return false; }
    @Override
    public boolean isBaby() { return false; }
    @Override
    public void setBaby(boolean childZombie) { }

    public int getVariant() { return this.entityData.get(DATA_VARIANT); }
    public void setVariant(int variant) { this.entityData.set(DATA_VARIANT, variant); }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariant(pCompound.getInt("Variant"));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.setVariant(this.random.nextInt(5));
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }
}