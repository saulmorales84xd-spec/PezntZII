package net.saullmc.pezntz.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.saullmc.pezntz.item.ModItems;

public class Bicicleta extends VehiculoBase {

    private static final float[][] ASIENTOS = {
            {-0.1F, 0.55F},
    };

    public Bicicleta(EntityType<? extends PathfinderMob> tipo, Level nivel) {
        super(tipo, nivel);
    }

    @Override
    public float[][] getAsientos() {
        return ASIENTOS;
    }

    @Override
    public float getCombustibleMaximo() {
        return 0.0F;
    }

    @Override
    public Item getLlave() {
        return ModItems.LLAVE_VEHICULO.get();
    }

    @Override
    public float getVelocidadMaxima() {
        return 0.42F;
    }

    @Override
    public float getAceleracion() {
        return 0.015F;
    }

    @Override
    public float getGiro() {
        return 3.6F;
    }

    @Override
    public float getEscalaHud() {
        return 1.15F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.45D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }
}