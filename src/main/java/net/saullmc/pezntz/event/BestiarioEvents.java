package net.saullmc.pezntz.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.capability.BestiarioProvider;
import net.saullmc.pezntz.network.NetworkHandler;
import net.saullmc.pezntz.network.SyncBestiarioPacket;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class BestiarioEvents {

    @SubscribeEvent
    public static void alMorirUnBicho(LivingDeathEvent event) {
        Entity muerto = event.getEntity();
        if (muerto.level().isClientSide) return;

        if (!(event.getSource().getEntity() instanceof ServerPlayer jugador)) return;

        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(muerto.getType());
        if (id == null) return;

        jugador.getCapability(BestiarioProvider.PLAYER_BESTIARIO).ifPresent(bestiario -> {
            if (bestiario.descubrir(id.toString())) {
                NetworkHandler.sendToPlayer(new SyncBestiarioPacket(bestiario), jugador);
            }
        });
    }

    @SubscribeEvent
    public static void alEntrar(PlayerEvent.PlayerLoggedInEvent event) {
        sincronizar(event.getEntity());
    }

    @SubscribeEvent
    public static void alCambiarDeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        sincronizar(event.getEntity());
    }

    @SubscribeEvent
    public static void alReaparecer(PlayerEvent.PlayerRespawnEvent event) {
        sincronizar(event.getEntity());
    }

    private static void sincronizar(Player jugador) {
        if (!(jugador instanceof ServerPlayer servidor)) return;

        servidor.getCapability(BestiarioProvider.PLAYER_BESTIARIO).ifPresent(bestiario ->
                NetworkHandler.sendToPlayer(new SyncBestiarioPacket(bestiario), servidor));
    }
}