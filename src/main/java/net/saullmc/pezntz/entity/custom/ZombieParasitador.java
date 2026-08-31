package net.saullmc.pezntz.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ServerLevelAccessor;
import net.saullmc.pezntz.effect.ModEffects;
import org.jetbrains.annotations.Nullable;

public class ZombieParasitador extends Zombie {
    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(ZombieParasitador.class, EntityDataSerializers.INT);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();

    public int attackAnimationTimeout = 0;

    public ZombieParasitador(EntityType<? extends Zombie> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ZombieAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    public boolean isBaby() { return false; }

    @Override
    public void setBaby(boolean childZombie) { }

    @Override
    protected boolean isSunSensitive() { return false; }

    @Override
    protected boolean convertsInWater() { return false; }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setupAnimationStates();

            if (this.attackAnimationTimeout > 0) {
                this.attackAnimationTimeout--;
            } else {
                this.attackAnimationState.stop();
            }
        }
    }

    private void setupAnimationStates() {
        boolean isMoving = this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
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
        boolean hurt = super.doHurtTarget(pEntity);
        if (hurt) {
            this.level().broadcastEntityEvent(this, (byte) 4);

            if (pEntity instanceof Player player) {
                player.addEffect(new MobEffectInstance(ModEffects.PLAGA.get(), 1200, 0, false, false, true));
            }
        }
        return hurt;
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