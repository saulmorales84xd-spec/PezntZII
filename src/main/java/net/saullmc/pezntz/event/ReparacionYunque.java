package net.saullmc.pezntz.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.ModItems;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class ReparacionYunque {

    public static final float PORCENTAJE_POR_AGUJA = 0.20F;

    public static final int NIVELES_POR_AGUJA = 1;

    public static final boolean TODAS_LAS_ARMADURAS = false;

    public static final boolean SUMAR_PENALIZACION_DE_USO = false;

    public static final boolean AUMENTAR_PENALIZACION = false;

    public static final int NIVELES_POR_RENOMBRAR = 1;

    @SubscribeEvent
    public static void alCambiarYunque(AnvilUpdateEvent evento) {
        ItemStack armadura = evento.getLeft();
        ItemStack agujas = evento.getRight();

        if (armadura.isEmpty() || agujas.isEmpty()) return;
        if (!agujas.is(ModItems.AGUJA_HILO.get())) return;
        if (!(armadura.getItem() instanceof ArmorItem)) return;
        if (!TODAS_LAS_ARMADURAS && !armadura.getItem().isValidRepairItem(armadura, agujas)) return;

        if (!armadura.isDamageableItem() || !armadura.isDamaged()) return;

        int porAguja = Math.max(1, Math.round(armadura.getMaxDamage() * PORCENTAJE_POR_AGUJA));
        int danio = armadura.getDamageValue();

        int necesarias = (danio + porAguja - 1) / porAguja;
        int usadas = Math.min(necesarias, agujas.getCount());
        if (usadas <= 0) return;

        ItemStack reparada = armadura.copy();
        reparada.setDamageValue(Math.max(0, danio - usadas * porAguja));

        int costo = usadas * Math.max(0, NIVELES_POR_AGUJA);

        if (SUMAR_PENALIZACION_DE_USO) {
            costo += armadura.getBaseRepairCost();
        }

        String nombre = evento.getName();
        if (nombre != null) {
            if (nombre.isBlank()) {
                if (armadura.hasCustomHoverName()) {
                    reparada.resetHoverName();
                    costo += NIVELES_POR_RENOMBRAR;
                }
            } else if (!nombre.equals(armadura.getHoverName().getString())) {
                reparada.setHoverName(Component.literal(nombre));
                costo += NIVELES_POR_RENOMBRAR;
            }
        }

        if (AUMENTAR_PENALIZACION) {
            reparada.setRepairCost(armadura.getBaseRepairCost() * 2 + 1);
        }

        evento.setOutput(reparada);
        evento.setCost(Math.max(1, costo));
        evento.setMaterialCost(usadas);
    }
}