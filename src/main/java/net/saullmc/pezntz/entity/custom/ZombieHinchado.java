package net.saullmc.pezntz.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.saullmc.pezntz.effect.ModEffects;
import java.util.List;
import java.util.EnumSet;
import java.util.UUID;

public class ZombieHinchado extends Zombie {

    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(ZombieHinchado.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ENRAGED = SynchedEntityData.defineId(ZombieHinchado.class, EntityDataSerializers.BOOLEAN);

    private static final UUID ENRAGED_SPEED_MOD_ID = UUID.fromString("6a350106-90f7-4144-8395-58e1c6628b03");
    private static final AttributeModifier ENRAGED_SPEED_MOD = new AttributeModifier(ENRAGED_SPEED_MOD_ID, "Enraged speed boost", 0.15D, AttributeModifier.Operation.ADDITION);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState runAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();

    public int attackAnimationTimeout = 0;

    public ZombieHinchado(EntityType<? extends Zombie> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT, 0);
        this.entityData.define(ENRAGED, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ExplodeWhenCloseGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
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
        } else {
            if (this.getHealth() <= this.getMaxHealth() * 0.3F && !this.isEnraged()) {
                this.setEnraged(true);
                AttributeInstance speedAttr = this.getAttribute(Attributes.MOVEMENT_SPEED);
                if (speedAttr != null && !speedAttr.hasModifier(ENRAGED_SPEED_MOD)) {
                    speedAttr.addTransientModifier(ENRAGED_SPEED_MOD);
                }
            }
        }
    }

    private void setupAnimationStates() {
        boolean isMoving = this.walkAnimation.speed() > 0.015F;
        boolean isEnraged = this.isEnraged();

        if (isEnraged) {
            this.idleAnimationState.stop();
            this.walkAnimationState.stop();
            this.runAnimationState.startIfStopped(this.tickCount);
        } else {
            this.runAnimationState.stop();

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
        return false;
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 4) {
            this.attackAnimationState.start(this.tickCount);
            this.attackAnimationTimeout = 20;
        } else {
            super.handleEntityEvent(pId);
        }
    }

    private boolean hasExploded = false;

    private void explode() {
        if (!this.level().isClientSide() && !this.hasExploded) {
            this.hasExploded = true;
            AABB boundingBox = this.getBoundingBox().inflate(6.0D);
            List<Player> nearbyPlayers = this.level().getEntitiesOfClass(Player.class, boundingBox);

            for (Player player : nearbyPlayers) {
                player.addEffect(new MobEffectInstance(ModEffects.MANCHA_ZOMBIE.get(), 60, 0, false, false, false));
            }

            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 4.0F, Level.ExplosionInteraction.MOB);
            this.discard();
        }
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        explode();
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

    public boolean isEnraged() {
        return this.entityData.get(ENRAGED);
    }

    public void setEnraged(boolean enraged) {
        this.entityData.set(ENRAGED, enraged);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Variant", this.getVariant());
        pCompound.putBoolean("Enraged", this.isEnraged());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariant(pCompound.getInt("Variant"));
        this.setEnraged(pCompound.getBoolean("Enraged"));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.setVariant(this.random.nextInt(5));
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    class ExplodeWhenCloseGoal extends Goal {
        private final ZombieHinchado zombie;

        public ExplodeWhenCloseGoal(ZombieHinchado zombie) {
            this.zombie = zombie;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.zombie.getTarget();
            return target != null && target.isAlive() && this.zombie.distanceToSqr(target) <= 9.0D;
        }

        @Override
        public void start() {
            this.zombie.getNavigation().stop();
        }

        @Override
        public void tick() {
            this.zombie.explode();
        }
    }
}