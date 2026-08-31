package net.saullmc.pezntz.entity.custom;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RataCarronera extends Zombie {

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();

    public int attackAnimationTimeout = 0;

    public RataCarronera(EntityType<? extends Zombie> pEntityType, Level pLevel) {
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
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 2.5D);
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
        boolean isMoving = this.walkAnimation.speed() > 0.02F;
        boolean isAttacking = this.attackAnimationTimeout > 0;

        if (isAttacking) {
            this.idleAnimationState.stop();
            this.walkAnimationState.stop();
        } else if (isMoving) {
            this.idleAnimationState.stop();
            this.walkAnimationState.startIfStopped(this.tickCount);
        } else {
            this.walkAnimationState.stop();
            this.idleAnimationState.startIfStopped(this.tickCount);
        }
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        boolean hurt = super.doHurtTarget(pEntity);
        if (hurt) {
            this.level().broadcastEntityEvent(this, (byte) 4);
        }
        return hurt;
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 4) {
            if (this.attackAnimationTimeout <= 0) {
                this.attackAnimationState.start(this.tickCount);
                this.attackAnimationTimeout = 80;
            }
        } else {
            super.handleEntityEvent(pId);
        }
    }
}