package net.saullmc.pezntz.event;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.custom.MosquitoZumbador;
import net.saullmc.pezntz.entity.custom.RataCarronera;
import net.saullmc.pezntz.entity.custom.ZombieArrastrador;
import net.saullmc.pezntz.entity.custom.ZombieHinchado;
import net.saullmc.pezntz.entity.custom.ZombieParasitador;
import net.saullmc.pezntz.entity.custom.ZombieTanque;
import net.saullmc.pezntz.entity.custom.ZombieToxico;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class ResistenciaZombies {

    public static final float RESISTENCIA_PROYECTILES = 0.30F;

    public static final boolean SOLO_BALAS_TACZ = false;

    public static final boolean INCLUIR_ZOMBIES_VANILLA = true;

    public static final TagKey<DamageType> BALAS_TACZ =
            TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("tacz", "bullets"));

    @SubscribeEvent
    public static void alRecibirDanio(LivingHurtEvent evento) {
        if (RESISTENCIA_PROYECTILES <= 0.0F) return;

        LivingEntity victima = evento.getEntity();
        if (victima.level().isClientSide()) return;
        if (!esZombie(victima)) return;
        if (!esProyectil(evento.getSource())) return;

        evento.setAmount(evento.getAmount() * (1.0F - Math.min(1.0F, RESISTENCIA_PROYECTILES)));
    }

    public static boolean esZombie(LivingEntity entidad) {
        if (entidad instanceof ZombieTanque
                || entidad instanceof ZombieHinchado
                || entidad instanceof ZombieArrastrador
                || entidad instanceof ZombieToxico
                || entidad instanceof ZombieParasitador
                || entidad instanceof RataCarronera
                || entidad instanceof MosquitoZumbador) {
            return true;
        }

        return INCLUIR_ZOMBIES_VANILLA && entidad instanceof Zombie;
    }

    public static boolean esProyectil(DamageSource fuente) {
        if (esBalaTacz(fuente)) return true;
        return !SOLO_BALAS_TACZ && fuente.is(DamageTypeTags.IS_PROJECTILE);
    }

    public static boolean esBalaTacz(DamageSource fuente) {
        if (fuente.is(BALAS_TACZ)) return true;

        return fuente.typeHolder().unwrapKey()
                .map(llave -> llave.location().getNamespace().equals("tacz")
                        && llave.location().getPath().contains("bullet"))
                .orElse(false);
    }
}