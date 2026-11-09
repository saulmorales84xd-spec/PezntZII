package net.saullmc.pezntz.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT)
public class DropCliente {

    public static final int TICKS_CADUCIDAD = 200;

    private static final Map<Integer, Dato> DROPS = new HashMap<>();

    public record Dato(double x, double techo, double z, long tiempoApertura, long visto) {
    }

    public static void poner(int id, double x, double techo, double z, long tiempoApertura) {
        DROPS.put(id, new Dato(x, techo, z, tiempoApertura, ahora()));
    }

    public static void quitar(int id) {
        DROPS.remove(id);
    }

    public static void limpiar() {
        DROPS.clear();
    }

    public static Map<Integer, Dato> activos() {
        long ahora = ahora();

        Iterator<Map.Entry<Integer, Dato>> it = DROPS.entrySet().iterator();
        while (it.hasNext()) {
            Dato dato = it.next().getValue();

            if (ahora - dato.visto() > TICKS_CADUCIDAD || ahora < dato.visto()) {
                it.remove();
            }
        }

        return DROPS;
    }

    public static Vec3 techo(int id, Dato dato, float partialTick) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level != null && id >= 0) {
            Entity entidad = mc.level.getEntity(id);

            if (entidad != null && !entidad.isRemoved()) {
                Vec3 pos = entidad.getPosition(partialTick);
                return new Vec3(pos.x, pos.y + entidad.getBbHeight(), pos.z);
            }
        }

        return new Vec3(dato.x(), dato.techo(), dato.z());
    }

    @SubscribeEvent
    public static void alDesconectar(ClientPlayerNetworkEvent.LoggingOut evento) {
        limpiar();
    }

    private static long ahora() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level == null ? 0L : mc.level.getGameTime();
    }
}