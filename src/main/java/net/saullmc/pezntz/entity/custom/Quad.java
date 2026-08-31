package net.saullmc.pezntz.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class Quad extends PathfinderMob {

    private static final EntityDataAccessor<Integer> DRIVER_ID = SynchedEntityData.defineId(Quad.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PASSENGER_ID = SynchedEntityData.defineId(Quad.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> WHEEL_ROTATION = SynchedEntityData.defineId(Quad.class, EntityDataSerializers.FLOAT);

    private float currentSpeed;

    public Quad(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setMaxUpStep(1.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DRIVER_ID, -1);
        this.entityData.define(PASSENGER_ID, -1);
        this.entityData.define(WHEEL_ROTATION, 0.0F);
    }

    @Override
    protected void registerGoals() {
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.45D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    public float getWheelRotation() {
        return this.entityData.get(WHEEL_ROTATION);
    }

    @Override
    public boolean showVehicleHealth() {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (!this.level().isClientSide) {
            if (this.entityData.get(DRIVER_ID) == -1) {
                pPlayer.setYRot(this.getYRot());
                pPlayer.setXRot(0.0F);
                pPlayer.setYBodyRot(this.getYRot());
                pPlayer.setYHeadRot(this.getYRot());
            }
            pPlayer.startRiding(this);
        } else {
            if (this.entityData.get(DRIVER_ID) == -1) {
                pPlayer.setYRot(this.getYRot());
                pPlayer.setXRot(0.0F);
                pPlayer.setYBodyRot(this.getYRot());
                pPlayer.setYHeadRot(this.getYRot());
            }
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return this.getPassengers().size() < 2;
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (!this.level().isClientSide) {
            if (this.entityData.get(DRIVER_ID) == -1) {
                this.entityData.set(DRIVER_ID, passenger.getId());
            }
            else if (this.entityData.get(PASSENGER_ID) == -1) {
                this.entityData.set(PASSENGER_ID, passenger.getId());
            }
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (!this.level().isClientSide) {
            if (passenger.getId() == this.entityData.get(DRIVER_ID)) {
                this.entityData.set(DRIVER_ID, -1);
            }
            if (passenger.getId() == this.entityData.get(PASSENGER_ID)) {
                this.entityData.set(PASSENGER_ID, -1);
            }
        }
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        int driverId = this.entityData.get(DRIVER_ID);
        if (driverId != -1) {
            Entity driver = this.level().getEntity(driverId);
            if (driver instanceof LivingEntity living && this.hasPassenger(driver)) {
                return living;
            }
        }
        return null;
    }

    @Override
    protected void positionRider(Entity pPassenger, Entity.MoveFunction pCallback) {
        if (this.hasPassenger(pPassenger)) {

            float conductorAdelanteZ = -0.1F;
            float conductorAlturaY = 0.3F;

            float pasajeroAtrasZ = -0.9F;
            float pasajeroAlturaY = 0.6F;

            float rotacionCuerpo = this.yBodyRot * ((float)Math.PI / 180F);

            if (pPassenger.getId() == this.entityData.get(DRIVER_ID)) {
                double offsetX = Math.sin(-rotacionCuerpo) * conductorAdelanteZ;
                double offsetZ = Math.cos(rotacionCuerpo) * conductorAdelanteZ;
                pCallback.accept(pPassenger, this.getX() + offsetX, this.getY() + conductorAlturaY, this.getZ() + offsetZ);
            } else {
                double offsetX = Math.sin(-rotacionCuerpo) * pasajeroAtrasZ;
                double offsetZ = Math.cos(rotacionCuerpo) * pasajeroAtrasZ;
                pCallback.accept(pPassenger, this.getX() + offsetX, this.getY() + pasajeroAlturaY, this.getZ() + offsetZ);
            }
        }
    }

    @Override
    public void onPassengerTurned(Entity pEntityToUpdate) {
        if (pEntityToUpdate.getId() == this.entityData.get(DRIVER_ID)) {
            pEntityToUpdate.setYBodyRot(this.getYRot());
            float f = Mth.wrapDegrees(pEntityToUpdate.getYRot() - this.getYRot());
            float f1 = Mth.clamp(f, -105.0F, 105.0F);
            pEntityToUpdate.yRotO += f1 - f;
            pEntityToUpdate.setYRot(pEntityToUpdate.getYRot() + f1 - f);
            pEntityToUpdate.setYHeadRot(pEntityToUpdate.getYRot());
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        LivingEntity driver = this.getControllingPassenger();
        if (this.isVehicle() && driver != null) {

            float steer = driver.xxa;
            float throttle = driver.zza;

            float maxSpeed = 0.6F;
            if (throttle > 0) this.currentSpeed = Mth.clamp(this.currentSpeed + 0.02F, -maxSpeed * 0.5F, maxSpeed);
            else if (throttle < 0) this.currentSpeed = Mth.clamp(this.currentSpeed - 0.02F, -maxSpeed * 0.5F, maxSpeed);
            else this.currentSpeed *= 0.92F;

            if (Math.abs(this.currentSpeed) > 0.01F) {
                float turnRate = 3.0F * Math.signum(this.currentSpeed);
                this.setYRot(this.getYRot() - steer * turnRate);
            }

            this.yRotO = this.getYRot();
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();

            Vec3 forward = Vec3.directionFromRotation(0, this.getYRot()).scale(this.currentSpeed);

            this.entityData.set(WHEEL_ROTATION, this.entityData.get(WHEEL_ROTATION) + this.currentSpeed);

            double motionY = this.onGround() ? 0.0D : this.getDeltaMovement().y - 0.08D;

            this.setDeltaMovement(forward.x, motionY, forward.z);

            this.move(MoverType.SELF, this.getDeltaMovement());
            return;
        }
        super.travel(travelVector);
    }
}