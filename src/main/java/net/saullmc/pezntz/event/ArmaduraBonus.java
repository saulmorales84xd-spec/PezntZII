package net.saullmc.pezntz.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.capability.BodyHealthData;
import net.saullmc.pezntz.capability.BodyHealthProvider;
import net.saullmc.pezntz.effect.ModEffects;
import net.saullmc.pezntz.effect.RadiacionEffect;
import net.saullmc.pezntz.item.ModArmorMaterials;
import net.saullmc.pezntz.item.custom.ArmaduraModItem;
import net.saullmc.pezntz.network.NetworkHandler;
import net.saullmc.pezntz.network.SyncBodyHealthPacket;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class ArmaduraBonus {

    public static final float REDUCCION_EXPLOSION_CHATARRA = 0.25F;

    public static final float REDUCCION_VENENO_CONTENCION = 0.30F;

    public static final boolean BIOSEGURIDAD_BLOQUEA_RADIACION = true;

    public static final boolean BIOSEGURIDAD_QUITA_LA_QUE_YA_TENIAS = false;

    private static final EquipmentSlot[] PIEZAS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;

        player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
            boolean cambio = aplicar(player, cap);

            if (BIOSEGURIDAD_QUITA_LA_QUE_YA_TENIAS) limpiarRadiacion(player);

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

    @SubscribeEvent
    public static void alRecibirDanio(LivingHurtEvent evento) {
        if (!(evento.getEntity() instanceof Player jugador)) return;
        if (jugador.level().isClientSide()) return;

        DamageSource fuente = evento.getSource();

        if (REDUCCION_EXPLOSION_CHATARRA > 0.0F && fuente.is(DamageTypeTags.IS_EXPLOSION)
                && setCompleto(jugador, ModArmorMaterials.CHATARRA_REFORZADA)) {
            evento.setAmount(evento.getAmount() * (1.0F - Math.min(1.0F, REDUCCION_EXPLOSION_CHATARRA)));
        }

        if (REDUCCION_VENENO_CONTENCION > 0.0F && esVeneno(jugador, fuente)
                && setCompleto(jugador, ModArmorMaterials.CONTENCION)) {
            evento.setAmount(evento.getAmount() * (1.0F - Math.min(1.0F, REDUCCION_VENENO_CONTENCION)));
        }
    }

    @SubscribeEvent
    public static void alPonerEfecto(MobEffectEvent.Applicable evento) {
        if (!BIOSEGURIDAD_BLOQUEA_RADIACION) return;
        if (evento.getEffectInstance().getEffect() != ModEffects.RADIACION.get()) return;
        if (!(evento.getEntity() instanceof Player jugador)) return;

        if (protegeDeRadiacion(jugador)) {
            evento.setResult(Event.Result.DENY);
        }
    }

    public static boolean esVeneno(Player jugador, DamageSource fuente) {
        return fuente.is(DamageTypes.MAGIC)
                && jugador.hasEffect(MobEffects.POISON)
                && !RadiacionEffect.estaHaciendoDanio();
    }

    public static boolean setCompleto(Player jugador, ArmorMaterial material) {
        for (EquipmentSlot hueco : PIEZAS) {
            if (!(jugador.getItemBySlot(hueco).getItem() instanceof ArmorItem pieza)) return false;
            if (pieza.getMaterial() != material) return false;
        }
        return true;
    }

    public static boolean protegeDeRadiacion(Player jugador) {
        for (EquipmentSlot hueco : PIEZAS) {
            if (!(jugador.getItemBySlot(hueco).getItem() instanceof ArmaduraModItem armadura)) return false;
            if (!armadura.protegeDeRadiacion()) return false;
        }
        return true;
    }

    private static void limpiarRadiacion(Player player) {
        if (player.level().isClientSide()) return;
        if (!player.hasEffect(ModEffects.RADIACION.get())) return;

        if (protegeDeRadiacion(player)) {
            player.removeEffect(ModEffects.RADIACION.get());
        }
    }
}