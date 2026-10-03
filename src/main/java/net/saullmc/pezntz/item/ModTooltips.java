package net.saullmc.pezntz.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
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

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT)
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

    public static final String KEYS = hex(0xf38c1e);
    public static final String BYI = hex(0xFB9F5E);
    public static final String MOBS = hex(0x8be761);
    public static final String UBICACIONES = hex(0xf5ec4b);
    public static final String NUM = hex(0x5790ef);
    public static final String ZOM = hex(0x962424);

    public static final String BR = "{br}";

    public static final String RESET = "{/}";

    public static final int COLOR_BASE = 0xAAAAAA;

    public static final int MAX_ANCHO = 350;

    public static String hex(int rgb) {
        return String.format("{#%06X}", rgb & 0xFFFFFF);
    }

    private static final Map<Item, List<List<Segmento>>> ESCRITO = new HashMap<>();

    private static final Map<Item, List<Component>> LISTO = new HashMap<>();

    private static final Map<Item, Integer> COLORES_NOMBRE = new HashMap<>();

    public static void colorNombre(Item item, int rgb) {
        COLORES_NOMBRE.put(item, rgb);
    }

    public static void registrar(Item item, String... lineas) {
        List<List<Segmento>> escrito = new ArrayList<>();

        for (String linea : lineas) {
            for (String trozo : linea.split("\\{br}|\\R")) {
                escrito.add(segmentar(limpiar(trozo)));
            }
        }

        ESCRITO.put(item, escrito);
        LISTO.remove(item);
    }

    private static String limpiar(String texto) {
        return texto.replaceAll("[ \\t]+", " ").trim();
    }

    public static void registrarTodos() {

        registrar(ModItems.TELA.get(),
                BR + "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "cajas," + BR + BYI + "cuerpos " + GRIS + "y " + BYI + "contenedores.");

        registrar(ModItems.BACKPACK.get(),
                "Utiliza este objeto para guardar ", "tus recursos mas útiles.", "Presiona " + KEYS + "[B] " + GRIS +
                        "para abrir la mochila ", "directamente.");

        registrar(ModItems.CHATARRA.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "barriles," + BR + RESET + "y " + BYI + "contenedores.");

        registrar(ModItems.CANDADO.get(),
                "Presiona " + KEYS + "[Click Derecho] " + GRIS + "sobre un " + BR + BYI + "cofre "+ GRIS + "para protegerlo.",
                "Una vez puesto el candado puedes ", "colocar un código de " + NUM + "4 dígitos", "para poder abrirlo.");

        registrar(ModItems.LLAVE_CANDADO.get(),
                "Presiona " + KEYS + "[Click Derecho] " + GRIS + "sobre un " + BR + BYI + "cofre protegido " + GRIS +
                        "para quitarle la", "protección.", "Solo puedes quitarle la proteccion", "a un " + BYI + "cofre protegido "
                        + GRIS + "que sea tuyo.");

        registrar(ModItems.LLAVE_VEHICULO.get(),
                "Presiona " + KEYS + "[Click Derecho] " + GRIS + "sobre un" + BR + MOBS + "vehículo " + GRIS + "para encerderlo. ",
                        "Esta llave se vinculará al vehiculo", "por lo tanto no podrás encender ", "otro " + MOBS + "vehículo " + GRIS + "con la misma llave.");

        registrar(ModItems.PEZNSINITA_BRUTE.get(),
                "Cocina este material cósmico para", "conseguir nuevos objetos que", "te serviran mas adelante.");

        registrar(ModItems.PEZNSINITA_INGOT.get(),
                "Material muy resistente que", "puedes conseguirlo cocinando " + BR + BYI + "Meteorito de Peznsinita." + BR + GRIS +
                        "Ideal para fabricar curas,", "herramientas y armaduras.");

        registrar(ModItems.TITANIUM_BRUTE.get(),
                "Cocina este material para conseguir", "nuevos objetos que te serviran ", "mas adelante.");

        registrar(ModItems.TITANIUM_INGOT.get(),
                "Material radiactivo dificil de", "manipular, puedes consegirlo", "cocinando un " + BYI + "Bloque de" + BR + BYI +
                "Titanio.", "Ideal para fabricar curas,", "suministros y armaduras.");

        registrar(ModItems.CLAVOS.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "barriles," + BR + RESET + "y " + BYI + "contenedores.");

        registrar(ModItems.PLACA_ACERO.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes conseguirlo cocinando " + BR + BYI + "lingotes de hierro " + GRIS + "en un " + BR + BYI +
                "alto horno.");

        registrar(ModItems.AGUJA_HILO.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Colócalo en un " + BYI + "Yunque " + GRIS + "con una ", "pieza de armadura para reparla", "por completo.");

        registrar(ModItems.AZUFRE.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                        "Puedes conseguirlo en las " + UBICACIONES + "minas " + BR + UBICACIONES + "de azufre.");

        registrar(ModItems.CARBON_NITRIDO.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                        "Puedes conseguirlo cocinando" + BR + BYI + "carbón vegetal " + GRIS + "en un ahumador.");

        registrar(ModItems.LATA_VACIA.get(),
                "Cocina este objeto para conseguir", "nuevos recursos.");

        registrar(ModItems.BOTELLA_ALCOHOL.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "cajas," + BR + BYI + "cuerpos " + GRIS + "y " + BYI + "contenedores.");

        registrar(ModItems.RESORTE.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "barriles," + BR + RESET + "y " + BYI + "contenedores.");

        registrar(ModItems.TUERCA.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "barriles," + BR + RESET + "y " + BYI + "contenedores.");

        registrar(ModItems.CHATARRA_REFORZADA.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                        "Se obtiene al procesar " + NUM + "9 " + "partes", "de chatarra en un " + BYI + "Depósito de" + BR + BYI +
                "Chatarra.");

        registrar(ModItems.CHATARRA_ELECTRONICA.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                        "Este objeto solo podras conseguirlo ", "en los " + BYI + "Drops.");

        registrar(ModItems.CABLES.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "barriles," + BR + RESET + "y " + BYI + "contenedores.");

        registrar(ModItems.CINTA.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "cajas," + BR + BYI + "cuerpos " + GRIS + "y " + BYI + "contenedores.");

        registrar(ModItems.BATERIAS.get(),
                "Coloca este objeto junto a una " + BR + BYI + "linterna" + GRIS + "para recargarla. ", "Esta la recargara un " + NUM + "20 %.");

        registrar(ModItems.GLANDULA_APESTOSA.get(),
                "Combina este material con otros ", "recueros para obtener nuevos ", "objetos.",
                         "Pudes conseguir este material ", "al matar a un " + ZOM + "Infectado " + GRIS + "o a un" + BR + ZOM + "Fundidor.",
                        "Ideal para fabricar curas, ", "herramientas y armaduras.");

        registrar(ModItems.ESPORAS.get(),
                "Combina este material con otros ", "recueros para obtener nuevos ", "objetos.",
                "Pudes conseguir este material ", "al matar a un " + ZOM + "Hinchado " + GRIS + "o a un" + BR + ZOM + "Parasitador.",
                "Ideal para fabricar curas, ", "herramientas y armaduras.");

        registrar(ModItems.FISHY.get(),
                "¿Será esto lo que necesitamos?. Fishy es ", "el pescado del que hablaban en las noticias,", "el causante de todo este desastre.",
                        "Lleva este objeto con bien al " + UBICACIONES + "muelle " + GRIS + "para", "poder fabricar una cura.");

        registrar(ModItems.MUESTRA_VIRUS.get(),
                "Despues de todo esto, los mejores médicos", "intentaron fabricar una cura pero fallaron,", "al final solo quedo una unica muestra",
                        "del virus.", "Lleva este objeto con bien al " + UBICACIONES + "muelle " + GRIS + "para", "poder fabricar una cura.");

        registrar(ModItems.DOCUMENTOS_PEZNT.get(),
                "Dentro de un laboratorio reforzado por las", "mejores fuerzas del país, quedo resguardado", "el unico documento con la formula capaz",
                        "de resolver todo esto.", "Lleva este objeto con bien al " + UBICACIONES + "muelle " + GRIS + "para", "poder fabricar una cura." );

        registrar(ModItems.JERINGA_VACIA.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                        "Ideal para fabricar jeringas de", "gran apoyo.");

        registrar(ModItems.BIDON_GASOLINA.get(),
                "Este contenedor te servira para ", "almacenar combustible.", "Presiona " + KEYS + "[Click Derecho] " + "sobre",
                        "una " + BYI + "bomba de gasolina " + GRIS + "para llenar ", "el contener y rellenar los " + MOBS + "vehÍculos" + BR + GRIS + "de gasolina.");

        registrar(ModItems.LATA_ATUN_CERRADA.get(),
                "Coloca este objeto junto a un " + BR + BYI + "abrelatas " + GRIS + "para abrir esta lata. ", "Al abrirla podras consumir este", "objeto");

        registrar(ModItems.LATA_POLLO_CERRADA.get(),
                "Coloca este objeto junto a un " + BR + BYI + "abrelatas " + GRIS + "para abrir esta lata. ", "Al abrirla podras consumir este", "objeto");

        registrar(ModItems.LATA_CARNE_CERRADA.get(),
                "Coloca este objeto junto a un " + BR + BYI + "abrelatas " + GRIS + "para abrir esta lata. ", "Al abrirla podras consumir este", "objeto");

        for (Item consumible : List.of(
                ModItems.LATA_ATUN.get(), ModItems.LATA_POLLO.get(), ModItems.LATA_CARNE.get(),
                ModItems.PAQUETE_MRE.get(), ModItems.NACHOS.get(), ModItems.CEREALES.get(),
                ModItems.BARRA_CHOCOLATE.get(), ModItems.BARRA_GRANOLA.get(),
                ModItems.FRUTO_ESTIMULANTE.get())) {
            registrar(consumible, GRIS + "Objeto consumible");
        }

        registrar(ModItems.MUESTRA_VIRUS.get(),
                "Despues de todo esto, los mejores médicos", "intentaron fabricar una cura pero fallaron,", "al final solo quedo una unica muestra",
                "del virus.", "Lleva este objeto con bien al " + UBICACIONES + "muelle " + GRIS + "para", "poder fabricar una cura.");

        registrar(ModItems.VENDAS.get(),
                "Cura " + ROJO + "6 de vida principal " + GRIS + "y" + NUM +
                        "1 Punto aleatorio en una zona del cuerpo si",
                        "se aplica con [Click Derecho]",
                        "Si ingresas en el menú de Salud con la tecla",
                        "[H] podras elegir una parte del cuerpo que",
                        "quieras curar, recibiendo 3 puntos ");

        colorNombre(ModItems.VENDAS.get(), 0xAA0000);
        colorNombre(ModItems.BOTIQUIN.get(), 0xAA0000);
        colorNombre(ModItems.BOTIQUIN.get(), 0xAA0000);
        colorNombre(ModItems.BOTIQUIN.get(), 0xAA0000);

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

    private static final Pattern PALABRAS = Pattern.compile("(?<= )");

    private static List<Component> repartir(List<Segmento> segmentos, Font font) {
        List<Component> renglones = new ArrayList<>();

        MutableComponent renglon = Component.empty();
        int ancho = 0;

        for (Segmento segmento : segmentos) {
            for (String palabra : PALABRAS.split(segmento.texto())) {
                int anchoPalabra = font.width(palabra);

                if (ancho > 0 && ancho + anchoPalabra > MAX_ANCHO) {
                    renglones.add(renglon);
                    renglon = Component.empty();
                    ancho = 0;

                    palabra = palabra.stripLeading();
                    if (palabra.isEmpty()) continue;
                    anchoPalabra = font.width(palabra);
                }

                renglon.append(Component.literal(palabra).withStyle(segmento.estilo()));
                ancho += anchoPalabra;
            }
        }

        if (ancho > 0) {
            renglones.add(renglon);
        }

        return renglones;
    }

    private static List<Component> obtener(Item item) {
        List<Component> cacheado = LISTO.get(item);
        if (cacheado != null) return cacheado;

        List<List<Segmento>> escrito = ESCRITO.get(item);
        if (escrito == null) return null;

        Font font = Minecraft.getInstance().font;

        List<Component> salida = new ArrayList<>();
        for (List<Segmento> linea : escrito) {
            salida.addAll(repartir(linea, font));
        }

        LISTO.put(item, salida);
        return salida;
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

        List<Component> lineas = obtener(item);
        if (lineas == null) return;

        tooltip.addAll(Math.min(1, tooltip.size()), lineas);
    }
}