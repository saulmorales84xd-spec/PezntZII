package net.saullmc.pezntz.event;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.saullmc.pezntz.capability.BodyHealthData;
import net.saullmc.pezntz.effect.RadiacionEffect;
import net.saullmc.pezntz.entity.custom.MosquitoZumbador;
import net.saullmc.pezntz.entity.custom.RataCarronera;
import net.saullmc.pezntz.entity.custom.ZombieArrastrador;
import net.saullmc.pezntz.entity.custom.ZombieHinchado;
import net.saullmc.pezntz.entity.custom.ZombieParasitador;
import net.saullmc.pezntz.entity.custom.ZombieTanque;
import net.saullmc.pezntz.entity.custom.ZombieToxico;
import net.saullmc.pezntz.item.custom.ArmaduraModItem;

import java.util.ArrayList;
import java.util.List;

public class RepartoDanio {

    public static final boolean REPARTO_AUTOMATICO = true;

    public static final float FRACCION_A_PARTES = 0.55F;

    public static final float GASTO_VANILLA_FASE_1 = 0.60F;

    public static final double PROB_CUERPO  = 0.40D;
    public static final double PROB_BRAZOS  = 0.20D;
    public static final double PROB_PIERNAS = 0.20D;
    public static final double PROB_CABEZA  = 0.20D;

    public static final float MULTIPLICADOR_CABEZA = 1.5F;

    public static final float PROTECCION_POR_PUNTO = 0.035F;

    public static final float PROTECCION_MAXIMA = 0.60F;

    public static final float ESCALA_PROTECCION = 1.0F;

    public static final String CABEZA  = "head";
    public static final String CUERPO  = "body";
    public static final String BRAZOS  = "arms";
    public static final String PIERNAS = "legs";

    public static final String[] PRIORIDAD_ZOMBIE      = {CUERPO, BRAZOS};
    public static final String[] PRIORIDAD_TANQUE      = {CUERPO, BRAZOS};
    public static final String[] PRIORIDAD_HINCHADO    = {};
    public static final String[] PRIORIDAD_ARRASTRADOR = {PIERNAS};
    public static final String[] PRIORIDAD_FUNDIDOR    = {CABEZA};
    public static final String[] PRIORIDAD_PARASITADOR = {PIERNAS};
    public static final String[] PRIORIDAD_RATA        = {CUERPO, BRAZOS};
    public static final String[] PRIORIDAD_MOSQUITO    = {BRAZOS};

    public static final String[] SIN_PRIORIDAD = {};

    public static final String[] ORDEN_SOBRA = {CUERPO, BRAZOS, PIERNAS, CABEZA};

    public static final boolean ZOMBIES_VANILLA_CON_PRIORIDAD = true;

    public static final String TAG_VENENO_FUNDIDOR = "pezntzVenenoFundidor";

    public static final int MARGEN_VENENO_TICKS = 10;

    public static EquipmentSlot huecoDe(String parte) {
        return switch (parte) {
            case CABEZA  -> EquipmentSlot.HEAD;
            case CUERPO  -> EquipmentSlot.CHEST;
            case BRAZOS  -> EquipmentSlot.LEGS;
            case PIERNAS -> EquipmentSlot.FEET;
            default      -> EquipmentSlot.CHEST;
        };
    }

    public static String elegirParte(double tirada) {
        double acumulado = PROB_CUERPO;
        if (tirada < acumulado) return CUERPO;
        acumulado += PROB_BRAZOS;
        if (tirada < acumulado) return BRAZOS;
        acumulado += PROB_PIERNAS;
        if (tirada < acumulado) return PIERNAS;
        return CABEZA;
    }

    public static String[] prioridadDe(Player jugador, DamageSource fuente) {
        if (fuente.is(DamageTypeTags.IS_EXPLOSION)) return SIN_PRIORIDAD;

        if (fuente.is(DamageTypes.MAGIC) && !RadiacionEffect.estaHaciendoDanio()
                && jugador.hasEffect(MobEffects.POISON) && tieneVenenoDeFundidor(jugador)) {
            return PRIORIDAD_FUNDIDOR;
        }

        return prioridadDe(fuente.getEntity());
    }

    public static String[] prioridadDe(Entity atacante) {
        if (atacante == null) return SIN_PRIORIDAD;

        if (atacante instanceof ZombieTanque)      return PRIORIDAD_TANQUE;
        if (atacante instanceof ZombieHinchado)    return PRIORIDAD_HINCHADO;
        if (atacante instanceof ZombieArrastrador) return PRIORIDAD_ARRASTRADOR;
        if (atacante instanceof ZombieToxico)      return PRIORIDAD_FUNDIDOR;
        if (atacante instanceof ZombieParasitador) return PRIORIDAD_PARASITADOR;
        if (atacante instanceof RataCarronera)     return PRIORIDAD_RATA;
        if (atacante instanceof MosquitoZumbador)  return PRIORIDAD_MOSQUITO;

        if (ZOMBIES_VANILLA_CON_PRIORIDAD && atacante instanceof Zombie) return PRIORIDAD_ZOMBIE;

        return SIN_PRIORIDAD;
    }

    public static String elegirParte(BodyHealthData cap, String[] prioridad, double tirada) {
        if (prioridad == null || prioridad.length == 0) return elegirParte(tirada);

        List<String> favoritas = partesConVida(cap, prioridad, true);
        if (!favoritas.isEmpty()) return sortear(favoritas, tirada);

        List<String> demas = partesConVida(cap, prioridad, false);
        if (!demas.isEmpty()) return sortear(demas, tirada);

        return prioridad[0];
    }

    public static List<String> ordenDeSobra(String[] prioridad, String parteGolpeada) {
        List<String> orden = new ArrayList<>();

        if (prioridad != null) {
            for (String parte : prioridad) {
                if (!parte.equals(parteGolpeada) && !orden.contains(parte)) orden.add(parte);
            }
        }

        for (String parte : ORDEN_SOBRA) {
            if (!parte.equals(parteGolpeada) && !orden.contains(parte)) orden.add(parte);
        }

        return orden;
    }

    public static void marcarVenenoDeFundidor(LivingEntity objetivo, int duracionTicks) {
        objetivo.getPersistentData().putLong(TAG_VENENO_FUNDIDOR,
                objetivo.level().getGameTime() + duracionTicks + MARGEN_VENENO_TICKS);
    }

    public static boolean tieneVenenoDeFundidor(LivingEntity objetivo) {
        return objetivo.getPersistentData().getLong(TAG_VENENO_FUNDIDOR) >= objetivo.level().getGameTime();
    }

    private static List<String> partesConVida(BodyHealthData cap, String[] prioridad, boolean favoritas) {
        List<String> partes = new ArrayList<>();

        for (String parte : ORDEN_SOBRA) {
            boolean esFavorita = false;
            for (String p : prioridad) {
                if (p.equals(parte)) {
                    esFavorita = true;
                    break;
                }
            }

            if (esFavorita == favoritas && vidaDe(cap, parte) > 0.0F) partes.add(parte);
        }

        return partes;
    }

    private static String sortear(List<String> partes, double tirada) {
        double total = 0.0D;
        for (String parte : partes) total += Math.max(0.0D, probabilidadDe(parte));

        if (total <= 0.0D) {
            int i = (int) Math.floor(tirada * partes.size());
            return partes.get(Math.max(0, Math.min(partes.size() - 1, i)));
        }

        double objetivo = tirada * total;
        double acumulado = 0.0D;
        for (String parte : partes) {
            acumulado += Math.max(0.0D, probabilidadDe(parte));
            if (objetivo < acumulado) return parte;
        }

        return partes.get(partes.size() - 1);
    }

    private static double probabilidadDe(String parte) {
        return switch (parte) {
            case CUERPO  -> PROB_CUERPO;
            case BRAZOS  -> PROB_BRAZOS;
            case PIERNAS -> PROB_PIERNAS;
            case CABEZA  -> PROB_CABEZA;
            default      -> 0.0D;
        };
    }

    private static float vidaDe(BodyHealthData cap, String parte) {
        return switch (parte) {
            case CABEZA  -> cap.getHead();
            case CUERPO  -> cap.getBody();
            case BRAZOS  -> cap.getArms();
            case PIERNAS -> cap.getLegs();
            default      -> 0.0F;
        };
    }

    public static float fraccionAPartes(Player player, BodyHealthData cap) {
        if (!REPARTO_AUTOMATICO) {
            return Math.min(1.0F, Math.max(0.0F, FRACCION_A_PARTES));
        }

        float reservaPartes = cap.getMaxHead() + cap.getMaxBody() + cap.getMaxArms() + cap.getMaxLegs();
        float vidaMaxima = player.getMaxHealth();
        float denominador = reservaPartes + GASTO_VANILLA_FASE_1 * vidaMaxima;

        if (denominador <= 0.0F) return 0.5F;
        return Math.min(1.0F, Math.max(0.0F, reservaPartes / denominador));
    }

    public static float proteccionDe(Player player, String parte) {
        if (ESCALA_PROTECCION <= 0.0F) return 0.0F;

        ItemStack pieza = player.getItemBySlot(huecoDe(parte));
        if (!(pieza.getItem() instanceof ArmaduraModItem armadura)) return 0.0F;

        float proteccion = armadura.getDefense() * PROTECCION_POR_PUNTO * ESCALA_PROTECCION;
        return Math.min(PROTECCION_MAXIMA, Math.max(0.0F, proteccion));
    }
}