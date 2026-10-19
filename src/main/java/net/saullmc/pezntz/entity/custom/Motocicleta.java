package net.saullmc.pezntz.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.saullmc.pezntz.item.ModItems;

public class Motocicleta extends VehiculoBase {

    public static final float MAX_COMBUSTIBLE = 2000.0F;

    private static final float[][] ASIENTOS = {
            {-0.2F, 0.55F},
    };

    public Motocicleta(EntityType<? extends PathfinderMob> tipo, Level nivel) {
        super(tipo, nivel);
    }

    @Override
    public float[][] getAsientos() {
        return ASIENTOS;
    }

    @Override
    public float getCombustibleMaximo() {
        return MAX_COMBUSTIBLE;
    }

    @Override
    public Item getLlave() {
        return ModItems.LLAVE_VEHICULO.get();
    }

    @Override
    public float getVelocidadMaxima() {
        return 0.85F;
    }

    @Override
    public float getAceleracion() {
        return 0.028F;
    }

    @Override
    public float getGiro() {
        return 2.4F;
    }

    @Override
    public float getGastoPorBloque() {
        return 3.0F;
    }

    @Override
    public float getEscalaHud() {
        return 0.9F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.45D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }
}