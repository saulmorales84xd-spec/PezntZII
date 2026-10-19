package net.saullmc.pezntz.entity.custom;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.saullmc.pezntz.item.ModItems;

public class Lancha extends VehiculoBase {

    private static final float[][] ASIENTOS = {
            { 1.80F, 0.45F,  0.00F},   // conductor, al timon
            { 0.60F, 0.45F, -0.60F},
            { 0.60F, 0.45F,  0.60F},
            {-0.60F, 0.45F, -0.60F},
            {-0.60F, 0.45F,  0.60F},
            {-1.80F, 0.45F, -0.60F},
            {-1.80F, 0.45F,  0.60F},
    };

    private static final double EMPUJE = 0.08D;

    private static final double ASENTAMIENTO = -0.012D;

    public Lancha(EntityType<? extends PathfinderMob> tipo, Level nivel) {
        super(tipo, nivel);
        this.setMaxUpStep(0.0F);
    }

    @Override
    public float[][] getAsientos() {
        return ASIENTOS;
    }

    @Override
    public float getCombustibleMaximo() {
        return 1000.0F;
    }

    @Override
    public boolean combustibleInfinito() {
        return true;
    }

    @Override
    public boolean esAcuatico() {
        return true;
    }

    @Override
    public Item getLlave() {
        return ModItems.LLAVE_VEHICULO.get();
    }

    @Override
    public float getVelocidadMaxima() {
        return 0.80F;
    }

    @Override
    public float getAceleracion() {
        return 0.018F;
    }

    @Override
    public float getGiro() {
        return 1.8F;
    }

    @Override
    protected boolean puedeMoverse() {
        return this.isInWater();
    }

    @Override
    protected double calcularMovimientoVertical() {
        if (!this.isInWater()) {
            return Math.max(this.getDeltaMovement().y - 0.08D, VELOCIDAD_CAIDA_MAXIMA);
        }
        return this.isUnderWater() ? EMPUJE : ASENTAMIENTO;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource fuente, float cantidad) {
        if (fuente.is(net.minecraft.world.damagesource.DamageTypes.DROWN)) return false;
        return super.hurt(fuente, cantidad);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10000.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.45D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }
}