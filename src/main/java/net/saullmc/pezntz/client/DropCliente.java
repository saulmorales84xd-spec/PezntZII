package net.saullmc.pezntz.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DropCliente {

    public static final int TICKS_CADUCIDAD = 200;

    private static final Map<BlockPos, Dato> DROPS = new HashMap<>();

    public record Dato(long tiempoApertura, long visto) {
    }

    public static void poner(BlockPos pos, long tiempoApertura) {
        DROPS.put(pos.immutable(), new Dato(tiempoApertura, ahora()));
    }

    public static void quitar(BlockPos pos) {
        DROPS.remove(pos.immutable());
    }

    public static void limpiar() {
        DROPS.clear();
    }

    public static Map<BlockPos, Dato> activos() {
        long ahora = ahora();

        Iterator<Map.Entry<BlockPos, Dato>> it = DROPS.entrySet().iterator();
        while (it.hasNext()) {
            Dato dato = it.next().getValue();

            if (ahora - dato.visto() > TICKS_CADUCIDAD || ahora < dato.visto()) {
                it.remove();
            }
        }

        return DROPS;
    }

    private static long ahora() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level == null ? 0L : mc.level.getGameTime();
    }
}
