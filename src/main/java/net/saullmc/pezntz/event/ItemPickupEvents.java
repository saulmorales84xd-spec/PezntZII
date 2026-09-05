package net.saullmc.pezntz.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.network.ItemPickupPacket;
import net.saullmc.pezntz.network.NetworkHandler;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class ItemPickupEvents {

    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ItemStack recogido = event.getStack();
        if (recogido == null || recogido.isEmpty()) return;

        NetworkHandler.sendToPlayer(new ItemPickupPacket(recogido.copy()), player);
    }
}