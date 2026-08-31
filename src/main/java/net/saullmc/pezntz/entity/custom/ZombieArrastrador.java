package net.saullmc.pezntz.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class ZombieArrastrador extends Zombie {

    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(ZombieArrastrador.class, EntityDataSerializers.INT);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();

    public int attackAnimationTimeout = 0;

    public ZombieArrastrador(EntityType<? extends Zombie> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
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
    public boolean doHurtTarget(Entity pEntity) {
        boolean success = super.doHurtTarget(pEntity);
        if (success) {
            this.level().broadcastEntityEvent(this, (byte) 4);
        }
        return success;
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 4) {
            if (this.attackAnimationTimeout <= 0) {
                this.attackAnimationState.start(this.tickCount);
                this.attackAnimationTimeout = 40;
            }
        } else {
            super.handleEntityEvent(pId);
        }
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public void setBaby(boolean childZombie) {
    }

    public int getVariant() {
        return this.entityData.get(DATA_VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(DATA_VARIANT, variant);
    }

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