package net.saullmc.pezntz.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.capability.BodyHealthProvider;
import net.saullmc.pezntz.network.NetworkHandler;
import net.saullmc.pezntz.network.SyncBodyHealthPacket;

@Mod.EventBusSubscriber(modid = "pezntz")
public class ModEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).isPresent()) {
                event.addCapability(new ResourceLocation("pezntz", "body_properties"), new BodyHealthProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            event.getOriginal().getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(oldStore -> {
                event.getEntity().getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
                NetworkHandler.sendToClients(new SyncBodyHealthPacket(player.getId(), cap), player);
            });
        }
    }
}