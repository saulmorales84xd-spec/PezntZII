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
import net.saullmc.pezntz.item.custom.ArmaduraTacticaItem;
import net.saullmc.pezntz.network.NetworkHandler;
import net.saullmc.pezntz.network.SyncBodyHealthPacket;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class ArmaduraTacticaBonus {

    public static final float BONUS_POR_PIEZA = 1.0F;

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
                lleva(player, EquipmentSlot.HEAD) ? BONUS_POR_PIEZA : 0.0F,
                lleva(player, EquipmentSlot.CHEST) ? BONUS_POR_PIEZA : 0.0F,
                lleva(player, EquipmentSlot.LEGS) ? BONUS_POR_PIEZA : 0.0F,
                lleva(player, EquipmentSlot.FEET) ? BONUS_POR_PIEZA : 0.0F);
    }

    private static boolean lleva(Player player, EquipmentSlot hueco) {
        return player.getItemBySlot(hueco).getItem() instanceof ArmaduraTacticaItem;
    }
}