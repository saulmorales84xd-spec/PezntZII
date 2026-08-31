package net.saullmc.pezntz.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.capability.BodyHealthData;
import net.saullmc.pezntz.capability.BodyHealthProvider;
import net.saullmc.pezntz.network.NetworkHandler;
import net.saullmc.pezntz.network.SyncBodyHealthPacket;

@Mod.EventBusSubscriber(modid = "pezntz")
public class PlayerDamageEvents {

    private static final int DOWNED_TICKS_LOST_PER_HIT = 100;

    private static final int DOWNED_TICKS_LOST_PER_DAMAGE = 40;

    @SubscribeEvent
    public static void onPlayerTakeDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {

            if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD) || event.getSource().is(DamageTypes.GENERIC_KILL)) {
                return;
            }

            player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(healthData -> {

                if (healthData.isDowned()) {

                    int tickReduction = DOWNED_TICKS_LOST_PER_HIT
                            + (int) (event.getAmount() * DOWNED_TICKS_LOST_PER_DAMAGE);
                    healthData.setDownedTimer(healthData.getDownedTimer() - tickReduction);

                    float healthAfter = player.getHealth() - event.getAmount();

                    if (healthAfter <= 0.0F) {
                        healthData.setDownedTimer(0);

                    } else {

                        int maxByHealth = (int) (BodyHealthData.DOWNED_DURATION_TICKS
                                * (healthAfter / player.getMaxHealth()));

                        if (healthData.getDownedTimer() > maxByHealth) {
                            healthData.setDownedTimer(maxByHealth);
                        }
                    }

                    NetworkHandler.sendToClients(new SyncBodyHealthPacket(player.getId(), healthData), player);
                    return;
                }

                float originalDamage = event.getAmount();
                float remainingDamage = originalDamage;
                boolean dataChanged = false;

                if (event.getSource().is(DamageTypes.FALL)) {
                    remainingDamage = healthData.damagePart("legs", remainingDamage);
                    dataChanged = true;
                }
                else {
                    double chance = Math.random();
                    if (chance < 0.40) {
                        remainingDamage = healthData.damagePart("body", remainingDamage);
                    } else if (chance < 0.60) {
                        remainingDamage = healthData.damagePart("arms", remainingDamage);
                    } else if (chance < 0.80) {
                        remainingDamage = healthData.damagePart("legs", remainingDamage);
                    } else {
                        remainingDamage = remainingDamage * 1.5f;
                        remainingDamage = healthData.damagePart("head", remainingDamage);
                    }
                    dataChanged = true;
                }

                if (dataChanged) {
                    if (remainingDamage > 0) remainingDamage = healthData.damagePart("body", remainingDamage);
                    if (remainingDamage > 0) remainingDamage = healthData.damagePart("arms", remainingDamage);
                    if (remainingDamage > 0) remainingDamage = healthData.damagePart("legs", remainingDamage);
                    if (remainingDamage > 0) remainingDamage = healthData.damagePart("head", remainingDamage);

                    if (remainingDamage <= 0) {
                        event.setCanceled(true);
                    } else {
                        event.setAmount(remainingDamage);
                    }
                    NetworkHandler.sendToClients(new SyncBodyHealthPacket(player.getId(), healthData), player);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD) || event.getSource().is(DamageTypes.GENERIC_KILL)) {
                player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
                    if (cap.isDowned()) {
                        cap.setDowned(false);
                        cap.setDownedTimer(0);
                        cap.setReviveProgress(0);
                        NetworkHandler.sendToClients(new SyncBodyHealthPacket(player.getId(), cap), player);
                    }
                });
                return;
            }

            player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
                if (!cap.isDowned()) {
                    event.setCanceled(true);
                    player.setHealth(player.getMaxHealth());
                    cap.setDowned(true);
                    cap.setDownedTimer(BodyHealthData.DOWNED_DURATION_TICKS);
                    cap.setReviveProgress(0);
                    cap.setHead(0); cap.setBody(0); cap.setArms(0); cap.setLegs(0);
                    NetworkHandler.sendToClients(new SyncBodyHealthPacket(player.getId(), cap), player);

                    String attackerName = event.getSource().getEntity() != null ? event.getSource().getEntity().getDisplayName().getString() : "el entorno";
                    Component deathMessage = Component.literal(player.getDisplayName().getString())
                            .withStyle(style -> style.withColor(0xFFFF55))
                            .append(Component.literal(" fue derribado ")
                                    .withStyle(style -> style.withColor(0xFFFFFF)));

                    player.getServer().getPlayerList().broadcastSystemMessage(deathMessage, false);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerTickRegenAndDebuffs(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
            ServerPlayer player = (ServerPlayer) event.player;
            player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
                boolean needsSync = false;

                if (cap.isDowned()) {
                    cap.setDownedTimer(cap.getDownedTimer() - 1);

                    player.setPose(Pose.SWIMMING);

                    if (cap.getDownedTimer() <= 0) {
                        cap.setDowned(false);
                        player.kill();
                    }

                    int currentProgress = cap.getReviveProgress();
                    int lastProgress = player.getPersistentData().getInt("lastReviveProgress");
                    int idleTicks = player.getPersistentData().getInt("reviveIdleTicks");

                    if (currentProgress > 0) {
                        if (currentProgress == lastProgress) {
                            idleTicks++;
                            if (idleTicks > 5) {
                                cap.setReviveProgress(0);
                                idleTicks = 0;
                            }
                        } else {
                            idleTicks = 0;
                        }
                    } else {
                        idleTicks = 0;
                    }

                    player.getPersistentData().putInt("lastReviveProgress", cap.getReviveProgress());
                    player.getPersistentData().putInt("reviveIdleTicks", idleTicks);

                    needsSync = true;
                }

                if (player.tickCount % 20 == 0) {
                    if (cap.getHead() <= 0) {
                        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false));
                        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0, false, false));
                    }
                    if (cap.getLegs() <= 0) player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2, false, false));
                }

                if (needsSync) NetworkHandler.sendToClients(new SyncBodyHealthPacket(player.getId(), cap), player);
            });
        }
    }

    @SubscribeEvent
    public static void onEntitySize(EntityEvent.Size event) {
        if (event.getEntity() instanceof Player player) {
            player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
                if (cap.isDowned()) {
                    event.setNewSize(EntityDimensions.fixed(0.6F, 0.6F));
                    event.setNewEyeHeight(0.4F);
                }
            });
        }
    }
}