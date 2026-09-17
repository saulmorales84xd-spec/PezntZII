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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.saullmc.pezntz.sound.ModSounds;

public class Lanzallamas extends ArmaMelee {

    private static final int USO_MAXIMO = 72000;

    private static final double ALCANCE = 7.0D;

    private static final double RADIO_FINAL = 0.9D;

    private static final int INTERVALO_DANIO = 4;

    private static final float DANIO = 1.5F;

    private static final int SEGUNDOS_FUEGO = 5;

    private static final int INTERVALO_SONIDO = 20;

    private static final int TICKS_POR_USO = 10;

    public Lanzallamas(Tier tier, int danio, float velocidad, Properties properties) {
        super(tier, danio, velocidad, properties, ModSounds.GOLPE_LANZALLAMAS);
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
        if (!(usuario instanceof Player jugador)) return;

        Vec3 ojo = jugador.getEyePosition();
        Vec3 mira = jugador.getViewVector(1.0F);

        double largo = distanciaLibre(level, jugador, ojo, mira);

        if (level.isClientSide()) {
            dibujarLlama(level, ojo, mira, largo, jugador);
            return;
        }

        if (remaining % INTERVALO_SONIDO == 0) {
            level.playSound(null, jugador.getX(), jugador.getY(), jugador.getZ(),
                    ModSounds.LANZALLAMAS_ACTIVO.get(), SoundSource.PLAYERS, 1.2F, 1.0F);
        }

        if (level instanceof ServerLevel serverLevel) {
            enviarLlama(serverLevel, ojo, mira, largo);
        }

        if (remaining % INTERVALO_DANIO != 0) return;

        quemarEnCono(level, jugador, ojo, mira, largo);

        if (remaining % TICKS_POR_USO == 0) {
            stack.hurtAndBreak(1, jugador, p -> p.broadcastBreakEvent(jugador.getUsedItemHand()));
        }
    }

    private static double distanciaLibre(Level level, Player jugador, Vec3 ojo, Vec3 mira) {
        Vec3 fin = ojo.add(mira.scale(ALCANCE));

        BlockHitResult choque = level.clip(new ClipContext(
                ojo, fin, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, jugador));

        return choque.getType() == HitResult.Type.MISS ? ALCANCE : ojo.distanceTo(choque.getLocation());
    }

    private static void quemarEnCono(Level level, Player jugador, Vec3 ojo, Vec3 mira, double largo) {
        for (double d = 1.0D; d <= largo; d += 1.0D) {
            Vec3 centro = ojo.add(mira.scale(d));
            double radio = 0.35D + RADIO_FINAL * (d / ALCANCE);

            AABB zona = new AABB(centro, centro).inflate(radio);

            for (Entity entidad : level.getEntities(jugador, zona, e -> e instanceof LivingEntity && e.isAlive())) {
                LivingEntity victima = (LivingEntity) entidad;

                victima.invulnerableTime = 0;
                victima.hurt(jugador.damageSources().playerAttack(jugador), DANIO);
                victima.setSecondsOnFire(SEGUNDOS_FUEGO);
            }
        }
    }

    private static void enviarLlama(ServerLevel level, Vec3 ojo, Vec3 mira, double largo) {
        Vec3 salida = ojo.add(mira.scale(0.6D));

        for (double d = 0.0D; d < largo; d += 0.45D) {
            Vec3 p = salida.add(mira.scale(d));
            double dispersion = 0.04D + 0.10D * (d / ALCANCE);

            level.sendParticles(ParticleTypes.FLAME, p.x, p.y, p.z, 2,
                    dispersion, dispersion, dispersion, 0.02D);

            if (d > largo * 0.4D) {
                level.sendParticles(ParticleTypes.SMOKE, p.x, p.y, p.z, 1,
                        dispersion, dispersion, dispersion, 0.01D);
            }
        }
    }

    private static void dibujarLlama(Level level, Vec3 ojo, Vec3 mira, double largo, Player jugador) {
        Vec3 salida = ojo.add(mira.scale(0.6D));

        for (double d = 0.0D; d < largo; d += 0.6D) {
            Vec3 p = salida.add(mira.scale(d));
            double dispersion = 0.05D + 0.12D * (d / ALCANCE);

            level.addParticle(ParticleTypes.FLAME,
                    p.x + (jugador.getRandom().nextDouble() - 0.5D) * dispersion,
                    p.y + (jugador.getRandom().nextDouble() - 0.5D) * dispersion,
                    p.z + (jugador.getRandom().nextDouble() - 0.5D) * dispersion,
                    mira.x * 0.1D, mira.y * 0.1D, mira.z * 0.1D);
        }
    }
}