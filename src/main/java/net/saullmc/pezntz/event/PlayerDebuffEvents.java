package net.saullmc.pezntz.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.capability.BodyHealthProvider;

@Mod.EventBusSubscriber(modid = "pezntz")
public class PlayerDebuffEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
            if (cap.getArms() <= 0) {
                event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
            if (cap.getArms() <= 0) {
                event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
            if (cap.getArms() <= 0) {
                event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player != null) {
            Player player = event.player;
            player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
                if (cap.getLegs() <= 0 && player.isSprinting()) {
                    player.setSprinting(false);
                }
            });
        }
    }
}
