package net.saullmc.pezntz.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.ModEntities;
import net.saullmc.pezntz.entity.custom.*;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ZOMBIE_HINCHADO.get(), ZombieHinchado.createAttributes().build());
        event.put(ModEntities.ZOMBIE_TANQUE.get(), ZombieTanque.createAttributes().build());
        event.put(ModEntities.ZOMBIE_ARRASTRADOR.get(), ZombieArrastrador.createAttributes().build());
        event.put(ModEntities.ZOMBIE_TOXICO.get(), ZombieToxico.createAttributes().build());
        event.put(ModEntities.ZOMBIE_PARASITADOR.get(), ZombieParasitador.createAttributes().build());
        event.put(ModEntities.RATA_CARRONERA.get(), RataCarronera.createAttributes().build());
        event.put(ModEntities.MOSQUITO_ZUMBADOR.get(), MosquitoZumbador.createAttributes().build());
        event.put(ModEntities.QUAD.get(), Quad.createAttributes().build());

    }
}
