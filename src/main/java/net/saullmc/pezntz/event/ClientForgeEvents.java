package net.saullmc.pezntz.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.capability.BodyHealthProvider;
import net.saullmc.pezntz.effect.ModEffects;
import net.saullmc.pezntz.network.NetworkHandler;
import net.saullmc.pezntz.network.RemovePlagaC2SPacket;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEvents {

    private static int spaceBarPresses = 0;
    private static boolean wasSpacePressed = false;

    @SubscribeEvent
    public static void onDownedPoseVisual(TickEvent.PlayerTickEvent event) {
        if (!event.side.isClient() || event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
            if (cap.isDowned()) {
                if (player.getForcedPose() != Pose.SWIMMING) {
                    player.setForcedPose(Pose.SWIMMING);
                }
                if (player.getPose() != Pose.SWIMMING) {
                    player.setPose(Pose.SWIMMING);
                }
            } else if (player.getForcedPose() == Pose.SWIMMING) {
                player.setForcedPose(null);
            }
        });
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player != null && player.hasEffect(ModEffects.PLAGA.get())) {
            boolean isSpacePressed = mc.options.keyJump.isDown();

            if (isSpacePressed && !wasSpacePressed) {
                spaceBarPresses++;

                if (spaceBarPresses >= 10) {
                    NetworkHandler.sendToServer(new RemovePlagaC2SPacket());
                    spaceBarPresses = 0;
                }
            }
            wasSpacePressed = isSpacePressed;
        } else {
            spaceBarPresses = 0;
            wasSpacePressed = false;
        }
    }

    @SubscribeEvent
    public static void onInputUpdate(MovementInputUpdateEvent event) {
        Player player = event.getEntity();
        if (player.hasEffect(ModEffects.PLAGA.get())) {

            event.getInput().up = false;
            event.getInput().down = false;
            event.getInput().left = false;
            event.getInput().right = false;

            event.getInput().forwardImpulse = 0.0F;
            event.getInput().leftImpulse = 0.0F;

            event.getInput().jumping = false;

            event.getInput().shiftKeyDown = false;
        }
    }
}