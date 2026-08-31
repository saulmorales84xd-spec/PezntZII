package net.saullmc.pezntz.entity.custom;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;

public class MosquitoZumbador extends Monster {

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState flyAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();

    public int attackAnimationTimeout = 0;

    public MosquitoZumbador(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

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
        boolean isFlying = !this.onGround() || this.walkAnimation.speed() > 0.015F;

        // LA SOLUCIÓN: Eliminamos la condición que frenaba el vuelo.
        // Ahora el mosquito NUNCA dejará de aletear, incluso cuando la animación
        // de ataque se dispare por separado.
        if (isFlying) {
            this.idleAnimationState.stop();
            this.flyAnimationState.startIfStopped(this.tickCount);
        } else {
            this.flyAnimationState.stop();
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
                this.attackAnimationTimeout = 40;
            }
        } else {
            super.handleEntityEvent(pId);
        }
    }
}