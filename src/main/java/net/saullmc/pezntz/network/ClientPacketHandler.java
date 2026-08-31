package net.saullmc.pezntz.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.saullmc.pezntz.capability.BodyHealthProvider;

public class ClientPacketHandler {

    public static void handleSyncBodyHealth(int entityId, float head, float body, float arms, float legs,
                                            boolean isDowned, int downedTimer, int reviveProgress) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        Entity entity = mc.level.getEntity(entityId);
        if (entity instanceof Player player) {
            player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
                cap.setHead(head);
                cap.setBody(body);
                cap.setArms(arms);
                cap.setLegs(legs);
                cap.setDowned(isDowned);
                cap.setDownedTimer(downedTimer);
                cap.setReviveProgress(reviveProgress);
            });
        }
    }
}