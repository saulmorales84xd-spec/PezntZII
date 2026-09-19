package net.saullmc.pezntz.entity.ai;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class PerseguirDirectoGoal extends Goal {

    private static final double DISTANCIA_MAXIMA_DIRECTA = 24.0D;

    private static final int INTERVALO_RUTA = 10;

    private static final int COOLDOWN_ATAQUE = 20;

    private static final int TICKS_PARA_DARSE_POR_ATASCADO = 20;

    private final PathfinderMob mob;
    private final double velocidad;

    private LivingEntity objetivo;
    private int ticksHastaRecalcular;
    private int cooldownAtaque;

    private int ticksAtascado;
    private int forzarPathfinder;
    private double ultimaX;
    private double ultimaZ;

    public PerseguirDirectoGoal(PathfinderMob mob, double velocidad) {
        this.mob = mob;
        this.velocidad = velocidad;

        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity candidato = this.mob.getTarget();
        return candidato != null && candidato.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity actual = this.mob.getTarget();
        if (actual == null || !actual.isAlive()) return false;

        return !(actual instanceof net.minecraft.world.entity.player.Player jugador)
                || (!jugador.isSpectator() && !jugador.isCreative());
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.objetivo = this.mob.getTarget();
        this.mob.setAggressive(true);

        this.ticksHastaRecalcular = 0;
        this.cooldownAtaque = 0;
        this.ticksAtascado = 0;
        this.forzarPathfinder = 0;
        this.ultimaX = this.mob.getX();
        this.ultimaZ = this.mob.getZ();
    }

    @Override
    public void stop() {
        this.objetivo = null;
        this.mob.setAggressive(false);
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity objetivo = this.mob.getTarget();
        if (objetivo == null) return;
        this.objetivo = objetivo;

        this.mob.getLookControl().setLookAt(objetivo, 30.0F, 30.0F);

        if (this.cooldownAtaque > 0) this.cooldownAtaque--;
        if (this.forzarPathfinder > 0) this.forzarPathfinder--;

        atacarSiAlcanza(objetivo);

        double distancia = this.mob.distanceTo(objetivo);
        boolean directo = this.forzarPathfinder <= 0
                && distancia <= DISTANCIA_MAXIMA_DIRECTA
                && hayViaLibre(objetivo);

        if (directo) {
            moverEnLineaRecta(objetivo);
        } else {
            moverConPathfinder(objetivo);
        }
    }

    private void atacarSiAlcanza(LivingEntity objetivo) {
        if (this.cooldownAtaque > 0) return;
        if (!this.mob.isWithinMeleeAttackRange(objetivo)) return;
        if (!this.mob.getSensing().hasLineOfSight(objetivo)) return;

        this.cooldownAtaque = COOLDOWN_ATAQUE;
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(objetivo);
    }

    private void moverEnLineaRecta(LivingEntity objetivo) {
        this.mob.getNavigation().stop();
        this.mob.getMoveControl().setWantedPosition(
                objetivo.getX(), objetivo.getY(), objetivo.getZ(), this.velocidad);

        vigilarAtasco();
    }

    private void moverConPathfinder(LivingEntity objetivo) {
        this.ticksAtascado = 0;

        if (this.ticksHastaRecalcular > 0) {
            this.ticksHastaRecalcular--;
            return;
        }

        this.ticksHastaRecalcular = INTERVALO_RUTA;
        this.mob.getNavigation().moveTo(objetivo, this.velocidad);
    }

    private void vigilarAtasco() {
        double avance = Math.abs(this.mob.getX() - this.ultimaX) + Math.abs(this.mob.getZ() - this.ultimaZ);
        this.ultimaX = this.mob.getX();
        this.ultimaZ = this.mob.getZ();

        if (avance < 0.01D) {
            this.ticksAtascado++;

            if (this.ticksAtascado >= TICKS_PARA_DARSE_POR_ATASCADO) {
                this.ticksAtascado = 0;
                this.forzarPathfinder = 60;
                this.ticksHastaRecalcular = 0;
            }
        } else {
            this.ticksAtascado = 0;
        }
    }

    private boolean hayViaLibre(LivingEntity objetivo) {
        Vec3 desde = new Vec3(this.mob.getX(), this.mob.getY() + this.mob.getBbHeight() * 0.5D, this.mob.getZ());
        Vec3 hasta = new Vec3(objetivo.getX(), objetivo.getY() + objetivo.getBbHeight() * 0.5D, objetivo.getZ());

        HitResult choque = this.mob.level().clip(new ClipContext(
                desde, hasta, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob));

        return choque.getType() == HitResult.Type.MISS;
    }
}