package net.saullmc.pezntz.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.saullmc.pezntz.item.ModItems;

public class Quad extends VehiculoBase {

    public static final float MAX_FUEL = 2500.0F;

    private static final float[][] ASIENTOS = {
            {-0.1F, 0.3F},   // conductor
            {-0.9F, 0.6F},   // acompaniante
    };

    public Quad(EntityType<? extends PathfinderMob> tipo, Level nivel) {
        super(tipo, nivel);
    }

    @Override
    public float[][] getAsientos() {
        return ASIENTOS;
    }

    @Override
    public float getCombustibleMaximo() {
        return MAX_FUEL;
    }

    @Override
    public Item getLlave() {
        return ModItems.LLAVE_VEHICULO.get();
    }

    @Override
    public float getGastoPorBloque() {
        return 2.5F;
    }

    @Override
    public float getEscalaHud() {
        return 1.0F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.45D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    public float getFuel() {
        return this.getCombustible();
    }

    public void setFuel(float cantidad) {
        this.setCombustible(cantidad);
    }

    public float getWheelRotation() {
        return this.getGiroRueda();
    }
}