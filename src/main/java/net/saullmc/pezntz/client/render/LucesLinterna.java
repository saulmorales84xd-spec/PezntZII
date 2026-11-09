package net.saullmc.pezntz.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.saullmc.pezntz.item.custom.FlashlightItem;

import java.util.ArrayList;
import java.util.List;

public final class LucesLinterna {

    public static final float ALCANCE = 22.0F;

    public static final float MEDIO_ANGULO = 50.0F;

    public static final float OFFSET_ADELANTE = 0.45F;

    public static final double DISTANCIA_MAXIMA = 48.0D;

    public static final int MAXIMO = 4;

    public static final boolean CORTAR_EN_PAREDES = true;

    public static final float MARGEN_PARED = 1.5F;

    public static final float ABERTURA_RAYOS = 0.6F;

    public record Luz(Vec3 origen, Vec3 dir, double distSq, boolean propia, float alcance) {
    }

    private static final List<Luz> LUCES = new ArrayList<>();

    private LucesLinterna() {
    }

    public static List<Luz> recoger(Level nivel, float partialTick, Vec3 camara) {
        LUCES.clear();

        Player yo = Minecraft.getInstance().player;
        double limite = DISTANCIA_MAXIMA * DISTANCIA_MAXIMA;

        for (Player jugador : nivel.players()) {
            if (!encendida(jugador)) continue;

            Vec3 mira = jugador.getViewVector(partialTick).normalize();
            Vec3 origen = jugador.getEyePosition(partialTick).add(mira.scale(OFFSET_ADELANTE));

            double distSq = origen.distanceToSqr(camara);
            if (distSq > limite) continue;

            boolean propia = (jugador == yo);
            float alcance = (propia || !CORTAR_EN_PAREDES)
                    ? ALCANCE
                    : alcanceLibre(nivel, jugador, origen, mira);

            LUCES.add(new Luz(origen, mira, distSq, propia, alcance));
        }

        if (LUCES.size() > MAXIMO) {
            LUCES.sort((a, b) -> Double.compare(a.distSq(), b.distSq()));
            LUCES.subList(MAXIMO, LUCES.size()).clear();
        }

        return LUCES;
    }

    private static float alcanceLibre(Level nivel, Player jugador, Vec3 origen, Vec3 dir) {
        Vec3 apoyo = Math.abs(dir.y) < 0.99D ? new Vec3(0.0D, 1.0D, 0.0D) : new Vec3(1.0D, 0.0D, 0.0D);
        Vec3 lado = dir.cross(apoyo).normalize();
        Vec3 arriba = lado.cross(dir).normalize();

        double abre = Math.tan(Math.toRadians(MEDIO_ANGULO * ABERTURA_RAYOS));

        Vec3[] rayos = {
                dir,
                dir.add(lado.scale(abre)),
                dir.subtract(lado.scale(abre)),
                dir.add(arriba.scale(abre)),
                dir.subtract(arriba.scale(abre))
        };

        double lejos = 0.0D;

        for (Vec3 rayo : rayos) {
            Vec3 fin = origen.add(rayo.normalize().scale(ALCANCE));

            BlockHitResult choque = nivel.clip(new ClipContext(origen, fin,
                    ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, jugador));

            double hasta = (choque.getType() == HitResult.Type.MISS)
                    ? ALCANCE
                    : choque.getLocation().distanceTo(origen);

            lejos = Math.max(lejos, hasta);
            if (lejos >= ALCANCE) break;
        }

        return (float) Math.min(ALCANCE, lejos + MARGEN_PARED);
    }

    public static boolean encendida(Player jugador) {
        return enMano(jugador.getMainHandItem()) || enMano(jugador.getOffhandItem());
    }

    public static boolean laMiaEncendida() {
        Player yo = Minecraft.getInstance().player;
        return yo != null && encendida(yo);
    }

    private static boolean enMano(ItemStack stack) {
        return stack.getItem() instanceof FlashlightItem && FlashlightItem.estaEncendida(stack);
    }
}