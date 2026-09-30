package net.saullmc.pezntz.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.network.DamageNumberPacket;
import net.saullmc.pezntz.network.NetworkHandler;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class DamageNumberEvents {

    private static final float MINIMO = 0.05F;

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        enviar(event.getEntity(), event.getAmount(), false);
    }

    @SubscribeEvent
    public static void onHeal(LivingHealEvent event) {
        enviar(event.getEntity(), event.getAmount(), true);
    }

    private static void enviar(LivingEntity entidad, float cantidad, boolean curacion) {
        if (entidad.level().isClientSide()) return;
        if (cantidad < MINIMO) return;
        if (!(entidad.level() instanceof ServerLevel)) return;

        NetworkHandler.sendToTracking(new DamageNumberPacket(entidad.getId(), cantidad, curacion), entidad);
    }
}