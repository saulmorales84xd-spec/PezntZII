package net.saullmc.pezntz.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static final String NARANJA_OXIDO = hex(0xD2691E);
    public static final String VERDE_LIMA = hex(0x9ACD32);
    public static final String ROJO_SANGRE = hex(0x8B1A1A);
    public static final String AZUL_MEDICO = hex(0x4FA3D1);
    public static final String VERDE_TOXICO = hex(0x7ACB4A);
    public static final String MORADO_INFECCION = hex(0x8A5FB0);
    public static final String ARENA = hex(0xC2B280);
    public static final String ACERO = hex(0x8C9AA6);

    public static final String RESET = "{/}";

    public static final String CURSIVA = "{o}";
    public static final String NEGRITA = "{l}";
    public static final String SUBRAYADO = "{n}";

    public static final int COLOR_BASE = 0xAAAAAA;

    public static final int MAX_CARACTERES = 45;

    public static String hex(int rgb) {
        return String.format("{#%06X}", rgb & 0xFFFFFF);
    }

    private static final Map<Item, List<Component>> LINEAS = new HashMap<>();

    private static final Map<Item, Integer> COLORES_NOMBRE = new HashMap<>();

    public static void colorNombre(Item item, int rgb) {
        COLORES_NOMBRE.put(item, rgb);
    }

    public static void registrar(Item item, String... lineas) {
        List<Component> componentes = new ArrayList<>();
        for (String linea : lineas) {
            componentes.addAll(parsear(linea));
        }
        LINEAS.put(item, componentes);
    }

    public static void registrarTodos() {

        registrar(ModItems.TELA.get(),
                "Combina este material con otros recursos para obtener nuevos objetos");

        registrar(ModItems.LATA_ATUN.get(),
                "Objeto consumible");

        registrar(ModItems.LATA_POLLO.get(),
                "Objeto consumible");

        registrar(ModItems.LATA_CARNE.get(),
                "Objeto consumible");

        registrar(ModItems.VENDAS.get(),
                "Cura " + ROJO + "6 de Vida Principal" + GRIS + " y " + CIAN + "1 Punto " + GRIS + "aleatorio en una zona del cuerpo si se aplica con "
                        + NARANJA_OXIDO + "Click Derecho                                                " +
                        GRIS + "Si ingresas en el menú de de curacion con la tecla " + NARANJA_OXIDO + "H " + GRIS + "podras escoger una parte del cuerpo para curarla," +
                        "recibiendo " + CIAN + "3 Puntos " + GRIS + "en la zona seleccionada");

        colorNombre(ModItems.VENDAS.get(), 0xD2691E);
    }

    private static final Pattern MARCAS =
            Pattern.compile("§([0-9a-fk-or])|\\{#([0-9a-fA-F]{6})}|\\{([/oln])}");


    private record Segmento(String texto, Style estilo) { }

    private static List<Segmento> segmentar(String texto) {
        Style base = Style.EMPTY.withColor(TextColor.fromRgb(COLOR_BASE));
        Style actual = base;

        List<Segmento> segmentos = new ArrayList<>();

        Matcher matcher = MARCAS.matcher(texto);
        int desde = 0;

        while (matcher.find()) {
            if (matcher.start() > desde) {
                segmentos.add(new Segmento(texto.substring(desde, matcher.start()), actual));
            }

            if (matcher.group(1) != null) {
                ChatFormatting formato = ChatFormatting.getByCode(matcher.group(1).charAt(0));
                if (formato != null) {
                    actual = formato.isColor()
                            ? Style.EMPTY.withColor(formato)
                            : actual.applyFormat(formato);
                }
            } else if (matcher.group(2) != null) {
                actual = actual.withColor(TextColor.fromRgb(Integer.parseInt(matcher.group(2), 16)));
            } else {
                switch (matcher.group(3)) {
                    case "/" -> actual = base;
                    case "o" -> actual = actual.withItalic(true);
                    case "l" -> actual = actual.withBold(true);
                    case "n" -> actual = actual.withUnderlined(true);
                }
            }

            desde = matcher.end();
        }

        if (desde < texto.length()) {
            segmentos.add(new Segmento(texto.substring(desde), actual));
        }

        return segmentos;
    }

    private static List<Component> parsear(String texto) {
        List<Component> renglones = new ArrayList<>();

        MutableComponent renglon = Component.empty();
        int usados = 0;

        for (Segmento segmento : segmentar(texto)) {
            String resto = segmento.texto();

            while (!resto.isEmpty()) {
                int hueco = MAX_CARACTERES - usados;

                if (resto.length() <= hueco) {
                    renglon.append(Component.literal(resto).withStyle(segmento.estilo()));
                    usados += resto.length();
                    break;
                }

                int corte = hueco > 0 ? resto.lastIndexOf(' ', hueco) : -1;

                if (corte <= 0) {

                    if (usados > 0) {
                        renglones.add(renglon);
                        renglon = Component.empty();
                        usados = 0;
                        continue;
                    }
                    corte = Math.max(1, hueco);
                }

                renglon.append(Component.literal(resto.substring(0, corte)).withStyle(segmento.estilo()));
                renglones.add(renglon);

                renglon = Component.empty();
                usados = 0;
                resto = resto.substring(corte).stripLeading();
            }
        }

        if (usados > 0) {
            renglones.add(renglon);
        }

        return renglones;
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        List<Component> tooltip = event.getToolTip();

        Integer color = COLORES_NOMBRE.get(item);
        if (color != null && !tooltip.isEmpty()) {
            tooltip.set(0, event.getItemStack().getHoverName().copy()
                    .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
        }

        List<Component> lineas = LINEAS.get(item);
        if (lineas == null) return;

        tooltip.addAll(Math.min(1, tooltip.size()), lineas);
    }
}