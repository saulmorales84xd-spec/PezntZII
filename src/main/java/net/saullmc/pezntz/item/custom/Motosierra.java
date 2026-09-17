package net.saullmc.pezntz.item.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.saullmc.pezntz.sound.ModSounds;

import java.util.Optional;

public class Motosierra extends ArmaMelee {

    private static final int USO_MAXIMO = 72000;

    private static final int INTERVALO_DANIO = 5;

    private static final float DANIO_POR_MORDISCO = 2.0F;

    private static final int INTERVALO_SONIDO = 20;

    private static final int MORDISCOS_POR_USO = 2;

    public Motosierra(Tier tier, int danio, float velocidad, Properties properties) {
        super(tier, danio, velocidad, properties, ModSounds.GOLPE_MOTOSIERRA);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return USO_MAXIMO;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void onUseTick(Level level, LivingEntity usuario, ItemStack stack, int remaining) {
        if (level.isClientSide() || !(usuario instanceof Player jugador)) return;

        if (remaining % INTERVALO_SONIDO == 0) {
            level.playSound(null, jugador.getX(), jugador.getY(), jugador.getZ(),
                    ModSounds.MOTOSIERRA_ACTIVA.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        if (remaining % INTERVALO_DANIO != 0) return;

        LivingEntity objetivo = buscarObjetivo(jugador);
        if (objetivo == null) return;

        objetivo.invulnerableTime = 0;
        objetivo.hurt(jugador.damageSources().playerAttack(jugador), DANIO_POR_MORDISCO);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.CRIT,
                    objetivo.getX(), objetivo.getY(0.6D), objetivo.getZ(),
                    4, 0.2D, 0.2D, 0.2D, 0.05D);
        }

        if (remaining % (INTERVALO_DANIO * MORDISCOS_POR_USO) == 0) {
            stack.hurtAndBreak(1, jugador, p -> p.broadcastBreakEvent(jugador.getUsedItemHand()));
        }
    }

    private static LivingEntity buscarObjetivo(Player jugador) {
        double alcance = jugador.getAttributeValue(ForgeMod.ENTITY_REACH.get());

        Vec3 ojo = jugador.getEyePosition();
        Vec3 mira = jugador.getViewVector(1.0F);
        Vec3 fin = ojo.add(mira.scale(alcance));

        AABB zona = jugador.getBoundingBox().expandTowards(mira.scale(alcance)).inflate(1.0D);

        LivingEntity mejor = null;
        double mejorDistancia = Double.MAX_VALUE;

        for (Entity entidad : jugador.level().getEntities(jugador, zona,
                e -> e instanceof LivingEntity && e.isAlive() && e.isPickable())) {

            Optional<Vec3> golpe = entidad.getBoundingBox().inflate(0.3D).clip(ojo, fin);

            if (golpe.isPresent()) {
                double distancia = ojo.distanceToSqr(golpe.get());
                if (distancia < mejorDistancia) {
                    mejorDistancia = distancia;
                    mejor = (LivingEntity) entidad;
                }
            }
        }

        return mejor;
    }
}