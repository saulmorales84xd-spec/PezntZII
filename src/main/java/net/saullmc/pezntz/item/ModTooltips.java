package net.saullmc.pezntz.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ibm.icu.impl.Utility.hex;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class ModTooltips {

    public static final String GRIS = "§7";
    public static final String GRIS_OSCURO = "§8";
    public static final String BLANCO = "§f";
    public static final String VERDE = "§a";
    public static final String VERDE_OSCURO = "§2";
    public static final String ROJO = "§c";
    public static final String ROJO_OSCURO = "§4";
    public static final String AMARILLO = "§e";
    public static final String DORADO = "§6";
    public static final String CIAN = "§b";
    public static final String AZUL = "§9";
    public static final String AZUL_OSCURO = "§1";
    public static final String MORADO = "§d";
    public static final String MORADO_OSCURO = "§5";
    public static final String NEGRO = "§0";

    public static final String KEYS = hex(0xD2691E);

    public static final String CURSIVA = "§o";
    public static final String NEGRITA = "§l";
    public static final String SUBRAYADO = "§n";

    private static final Map<Item, List<String>> LINEAS = new HashMap<>();

    public static void registrar(Item item, String... lineas) {
        LINEAS.put(item, List.of(lineas));
    }

    public static void registrarTodos() {

        registrar(ModItems.VENDAS.get(),
                GRIS + "Cura" + ROJO + "6 de vida principal " + GRIS + " y " + CIAN + "1 Punto " + GRIS + "aletorio en una zona especifica del cuerpo si se aplica con"
                        + KEYS + "Click Derecho.");

        registrar(Items.ROTTEN_FLESH,
                GRIS + "Carne " + VERDE_OSCURO + "infectada" + GRIS + ". Comer bajo tu riesgo");
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        List<String> lineas = LINEAS.get(event.getItemStack().getItem());
        if (lineas == null) return;

        List<Component> tooltip = event.getToolTip();

        int posicion = Math.min(1, tooltip.size());

        List<Component> nuevas = new ArrayList<>();
        for (String linea : lineas) {
            nuevas.add(Component.literal(linea));
        }

        tooltip.addAll(posicion, nuevas);
    }
}