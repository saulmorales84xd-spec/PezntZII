package net.saullmc.pezntz.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.capability.BodyHealthProvider;
import net.saullmc.pezntz.network.NetworkHandler;
import net.saullmc.pezntz.network.RevivePlayerPacket;

@Mod.EventBusSubscriber(modid = "pezntz", value = Dist.CLIENT)
public class ClientInteractEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (event.phase == TickEvent.Phase.END && mc.player != null && mc.level != null) {
            mc.player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(myCap -> {
                if (!myCap.isDowned()) {
                    if (mc.options.keyUse.isDown()) {
                        if (mc.crosshairPickEntity instanceof Player targetPlayer) {
                            NetworkHandler.sendToServer(new RevivePlayerPacket(targetPlayer.getId()));
                        }
                    }
                }
            });
        }
    }
}