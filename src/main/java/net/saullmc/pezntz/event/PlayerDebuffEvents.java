package net.saullmc.pezntz.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.capability.BodyHealthProvider;

@Mod.EventBusSubscriber(modid = "pezntz")
public class PlayerDebuffEvents {

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