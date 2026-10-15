package net.saullmc.pezntz.event;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.saullmc.pezntz.capability.BodyHealthData;
import net.saullmc.pezntz.item.custom.ArmaduraModItem;

public class RepartoDanio {

    public static final boolean REPARTO_AUTOMATICO = true;

    public static final float FRACCION_A_PARTES = 0.55F;

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

    public static float fraccionAPartes(Player player, BodyHealthData cap) {
        if (!REPARTO_AUTOMATICO) {
            return Math.min(1.0F, Math.max(0.0F, FRACCION_A_PARTES));
        }

        float reservaPartes = cap.getMaxHead() + cap.getMaxBody() + cap.getMaxArms() + cap.getMaxLegs();
        float vidaMaxima = player.getMaxHealth();
        float total = reservaPartes + vidaMaxima;

        if (total <= 0.0F) return 0.5F;
        return Math.min(1.0F, Math.max(0.0F, reservaPartes / total));
    }

    public static float proteccionDe(Player player, String parte) {
        if (ESCALA_PROTECCION <= 0.0F) return 0.0F;

        ItemStack pieza = player.getItemBySlot(huecoDe(parte));
        if (!(pieza.getItem() instanceof ArmaduraModItem armadura)) return 0.0F;

        float proteccion = armadura.getDefense() * PROTECCION_POR_PUNTO * ESCALA_PROTECCION;
        return Math.min(PROTECCION_MAXIMA, Math.max(0.0F, proteccion));
    }
}