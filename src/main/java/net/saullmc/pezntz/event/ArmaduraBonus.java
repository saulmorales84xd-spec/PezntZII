package net.saullmc.pezntz.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.capability.BodyHealthData;
import net.saullmc.pezntz.capability.BodyHealthProvider;
import net.saullmc.pezntz.item.custom.ArmaduraModItem;
import net.saullmc.pezntz.network.NetworkHandler;
import net.saullmc.pezntz.network.SyncBodyHealthPacket;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class ArmaduraBonus {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;

        player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
            boolean cambio = aplicar(player, cap);

            if (cambio && player instanceof ServerPlayer serverPlayer) {
                NetworkHandler.sendToClients(new SyncBodyHealthPacket(serverPlayer.getId(), cap), serverPlayer);
            }
        });
    }

    public static boolean aplicar(Player player, BodyHealthData cap) {
        return cap.setBonuses(
                bonus(player, EquipmentSlot.HEAD),
                bonus(player, EquipmentSlot.CHEST),
                bonus(player, EquipmentSlot.LEGS),
                bonus(player, EquipmentSlot.FEET));
    }

    private static float bonus(Player player, EquipmentSlot hueco) {
        return player.getItemBySlot(hueco).getItem() instanceof ArmaduraModItem armadura
                ? armadura.getBonusParte()
                : 0.0F;
    }
}