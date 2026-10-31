package net.saullmc.pezntz.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
    public static final String ROJO = "§c";
    public static final String ROJO_OSCURO = "§4";
    public static final String AZUL = "§9";

    public static final String KEYS = hex(0xae68dc);
    public static final String BYI = hex(0xFB9F5E);
    public static final String MOBS = hex(0x8be761);
    public static final String UBICACIONES = hex(0xf5ec4b);
    public static final String NUM = hex(0x5790ef);
    public static final String ZOM = hex(0x962424);
    public static final String EFE = hex(0x568553);
    public static final String PEZ = hex(0xff650a);
    public static final String RAD = hex(0xedb71d);
    public static final String ITEMS = hex(0x7372ae);
    public static final String REC = hex(0x8f7053);
    public static final String CON = hex(0x6aabb7);
    public static final String BIO = hex(0xcaa226);
    public static final String PART = hex(0x1ceedd);

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
            for (String trozo : linea.split("\\{br}|\\R", -1)) {
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
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
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
                        "para quitarle la", "protección.", "Solo puedes quitarle la protección", "a un " + BYI + "cofre protegido "
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
                "Puedes conseguirlo cocinando " + BR + ITEMS + "lingotes de hierro " + GRIS + "en un " + BR + BYI + "alto horno.");

        registrar(ModItems.AGUJA_HILO.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Colócalo en un " + BYI + "Yunque " + GRIS + "con una ", "pieza de armadura para reparla", "por completo.");

        registrar(ModItems.AZUFRE.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes conseguirlo en las " + UBICACIONES + "minas " + BR + UBICACIONES + "de azufre.");

        registrar(ModItems.CARBON_NITRIDO.get(),
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes conseguirlo cocinando" + BR + ITEMS + "carbón vegetal " + GRIS + "en un ahumador.");

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
                "Se obtiene al procesar " + NUM + "9 " + "partes", "de chatarra en una " + BYI + "Prensa");

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
                "Coloca este objeto junto a una " + BR + ITEMS + "linterna " + GRIS + "para recargarla. ", "Esta la recargara un " + NUM + "20 %.");

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
                "Este contenedor te servira para ", "almacenar combustible.", "Presiona " + KEYS + "[Click Derecho] " + GRIS +"sobre",
                "una " + BYI + "bomba de gasolina " + GRIS + "para llenar ", "el contener y rellenar los " + MOBS + "vehÍculos" + BR + GRIS + "de gasolina.");

        registrar(ModItems.LATA_ATUN_CERRADA.get(),
                "Coloca este objeto junto a un " + BR + ITEMS + "abrelatas " + GRIS + "para abrir esta lata. ", "Al abrirla podras consumir este", "objeto.");

        registrar(ModItems.LATA_POLLO_CERRADA.get(),
                "Coloca este objeto junto a un " + BR + ITEMS + "abrelatas " + GRIS + "para abrir esta lata. ", "Al abrirla podras consumir este", "objeto.");

        registrar(ModItems.LATA_CARNE_CERRADA.get(),
                "Coloca este objeto junto a un " + BR + ITEMS + "abrelatas " + GRIS + "para abrir esta lata. ", "Al abrirla podras consumir este", "objeto.");

        for (Item consumible : List.of(
                ModItems.LATA_ATUN.get(), ModItems.LATA_POLLO.get(), ModItems.LATA_CARNE.get(),
                ModItems.PAQUETE_MRE.get(), ModItems.NACHOS.get(), ModItems.CEREALES.get(),
                ModItems.BARRA_CHOCOLATE.get(), ModItems.BARRA_GRANOLA.get(),
                ModItems.FRUTO_ESTIMULANTE.get())) {
            registrar(consumible, GRIS + "Objeto consumible.");
        }

        registrar(ModItems.VENDAS.get(),
                "Cura " + ROJO + "6 de Vida Principal " + GRIS + "y " + NUM + "2 Puntos " + GRIS + "aleatorios", "en una zona del cuerpo si se aplica",
                "con " + KEYS + "[Click Derecho].",
                "Si ingresas en el menú de Salud con la tecla" + BR + KEYS + "[H] " + GRIS + "podras elegir una parte del cuerpo que",
                "quieras curar, recibiendo " + NUM + "4 Puntos " + GRIS + "en la zona ", "seleccionada.");

        registrar(ModItems.BOTIQUIN.get(),
                "Cura " + ROJO + "toda la Vida Principal " + GRIS + "mas aparte ", " todas las partes del cuerpo si se", "aplica con "
                        + KEYS + "[Click Derecho].");

        registrar(ModItems.ADRENALINA.get(),
                "Esta jeringa otorga " + AZUL + "Fuerza I " + GRIS + "y " + BR + AZUL + "Velocidad I." + BR + BR + ROJO_OSCURO + "Importante: " + GRIS +
                "Puedes aplicarsela a " + GRIS + "otro " + BR + MOBS + "jugador.");

        registrar(ModItems.MORFINA.get(),
                "Al aplicarte esta jeringa te curaras", "un " + ROJO + "20% de Vida Principal " + GRIS + "y recuperaras " + BR + NUM +
                "2 Puntos " + GRIS + "en todas las partes del cuerpo. " + BR + BR + ROJO_OSCURO + "Importante: " + GRIS + "Puedes aplicarsela a otro " + BR + MOBS + "jugador.");

        registrar(ModItems.COAGULANTE.get(),
                "Al aplicarte esta jeringa te quitaras ", "todo los " + EFE + "efectos negativos " + GRIS + "que tengas,",
                "menos la " + RAD + "Radiación." + BR + BR + ROJO_OSCURO + "Importante: " + GRIS + "Puedes aplicarsela a otro " + BR + MOBS + "jugador.");

        registrar(ModItems.QUELANTE.get(),
                "Al aplicarte esta jeringa te quitaras ", "la " + RAD + "radiación " + GRIS + "por completo."
                        + BR + BR + ROJO_OSCURO + "Importante: " + GRIS + "Puedes aplicarsela a otro " + BR + MOBS + "jugador.");

        registrar(ModItems.CURA.get(),
                "Al aplicarte esta jeringa te curara ", "del " + ZOM + "Virus Peznt." + BR + GRIS + "Sal del pueblo sano y salgo para poder ",
                        "recrear la cura. " + BR + BR + ROJO_OSCURO + "Importante: " + GRIS + "Puedes aplicarsela a otro " + BR + MOBS + "jugador.");

        registrar(ModItems.LANTERN.get(),
                "Este objeto te permite iluminar zonas ", "oscuras presionando " + KEYS + "[Click Derecho]." + BR + BR + GRIS +
                        "Utiliza " + ITEMS + "baterías " + GRIS + "para recargar la pila ", "de este objeto." + BR);

        registrar(ModItems.ABRELATAS.get(),
                "Este objeto te permite abrir alimentos ", "enlatados combinalo con " + ITEMS + "latas de comida " + BR + GRIS + "para abrirlas.");

        registrar(ModItems.CUCHILLO.get(),
                "Arma letal cuerpo a cuerpo contra " + BR + ZOM + "Infectados " + GRIS + "de todo tipo.");

        registrar(ModItems.BATE.get(),
                "Arma letal cuerpo a cuerpo contra " + BR + ZOM + "Infectados " + GRIS + "de todo tipo.");

        registrar(ModItems.LUCILLE.get(),
                "Arma letal cuerpo a cuerpo contra " + BR + ZOM + "Infectados " + GRIS + "de todo tipo.");

        registrar(ModItems.PALANCA.get(),
                "Arma letal cuerpo a cuerpo contra " + BR + ZOM + "Infectados " + GRIS + "de todo tipo.",
                        "Permite romper " + BYI + "barriles y chatarra " + BR + GRIS + "para obtener su contenido.");

        registrar(ModItems.GUITARRA.get(),
                "Arma letal cuerpo a cuerpo contra " + BR + ZOM + "Infectados " + GRIS + "de todo tipo.");

        registrar(ModItems.KATANA.get(),
                "Arma letal cuerpo a cuerpo contra " + BR + ZOM + "Infectados " + GRIS + "de todo tipo.");

        registrar(ModItems.MOTOSIERRA.get(),
                "Arma letal a distancia contra " + BR + ZOM + "Infectados " + GRIS + "de todo tipo.");

        registrar(ModItems.LANZALLAMAS.get(),
                "Arma letal a distancia contra " + BR + ZOM + "Infectados " + GRIS + "de todo tipo.");

        registrar(ModItems.PEZNSINITA_PICKAXE.get(),
                "Herramienta reforzada de " + PEZ + "peznsinita " + BR + GRIS + "con mayor eficiencia y durabilidad.", "Ideal para recoletar recursos y ",
                        "suministros básicos.");

        registrar(ModItems.PEZNSINITA_AXE.get(),
                "Herramienta reforzada de " + PEZ + "peznsinita " + BR + GRIS + "con mayor eficiencia y durabilidad.", "Ideal para recoletar recursos y ",
                "suministros básicos.");

        registrar(ModItems.PEZNSINITA_SHOVEL.get(),
                "Herramienta reforzada de " + PEZ + "peznsinita " + BR + GRIS + "con mayor eficiencia y durabilidad.", "Ideal para recoletar recursos y ",
                "suministros básicos.");

        registrar(ModItems.PEZNSINITA_HOE.get(),
                "Herramienta reforzada de " + PEZ + "peznsinita " + BR + GRIS + "con mayor eficiencia y durabilidad.", "Ideal para recoletar recursos y ",
                "suministros básicos.");

        registrar(ModItems.PEZNSINITA_HOE.get(),
                "Herramienta reforzada de " + PEZ + "peznsinita " + BR + GRIS + "con mayor eficiencia y durabilidad.", "Ideal para recoletar recursos y ",
                "suministros básicos.");

        registrar(ModItems.HELMET_ARMADURA_TACTICA.get(),
                "Esta armadura te aumenta " + NUM + "1 Punto " + GRIS + "en las partes ", "del cuerpo y " + NUM + "2.5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.CHESTPLATE_ARMADURA_TACTICA.get(),
                "Esta armadura te aumenta " + NUM + "1 Punto " + GRIS + "en las partes ", "del cuerpo y " + NUM + "2.5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.LEGGINS_ARMADURA_TACTICA.get(),
                "Esta armadura te aumenta " + NUM + "1 Punto " + GRIS + "en las partes ", "del cuerpo y " + NUM + "2.5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.BOOTS_ARMADURA_TACTICA.get(),
                "Esta armadura te aumenta " + NUM + "1 Punto " + GRIS + "en las partes ", "del cuerpo y " + NUM + "2.5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.HELMET_ARMADURA_CHATARRA_REFORZADA.get(),
                "Esta armadura te aumenta " + NUM + "2 Puntos " + GRIS + "en las partes ", "del cuerpo y " + NUM + "5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo reduce el daño recibido por ",
                "explosiones."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.CHESTPLATE_ARMADURA_CHATARRA_REFORZADA.get(),
                "Esta armadura te aumenta " + NUM + "2 Puntos " + GRIS + "en las partes ", "del cuerpo y " + NUM + "5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo reduce el daño recibido por ",
                "explosiones."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.LEGGINS_ARMADURA_CHATARRA_REFORZADA.get(),
                "Esta armadura te aumenta " + NUM + "2 Puntos " + GRIS + "en las partes ", "del cuerpo y " + NUM + "5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo reduce el daño recibido por ",
                "explosiones."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.BOOTS_ARMADURA_CHATARRA_REFORZADA.get(),
                "Esta armadura te aumenta " + NUM + "2 Puntos " + GRIS + "en las partes ", "del cuerpo y " + NUM + "5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo reduce el daño recibido por ",
                "explosiones."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.HELMET_ARMADURA_CONTENCION.get(),
                "Esta armadura te aumenta " + NUM + "3 Puntos " + GRIS + "en las partes ", "del cuerpo y " + NUM + "7.5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo reduce el daño recibido por ",
                "veneno."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.CHESTPLATE_ARMADURA_CONTENCION.get(),
                "Esta armadura te aumenta " + NUM + "3 Puntos " + GRIS + "en las partes ", "del cuerpo y " + NUM + "7.5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo reduce el daño recibido por ",
                "veneno."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.LEGGINS_ARMADURA_CONTENCION.get(),
                "Esta armadura te aumenta " + NUM + "3 Puntos " + GRIS + "en las partes ", "del cuerpo y " + NUM + "7.5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo reduce el daño recibido por ",
                "veneno."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.BOOTS_ARMADURA_CONTENCION.get(),
                "Esta armadura te aumenta " + NUM + "3 Puntos " + GRIS + "en las partes ", "del cuerpo y " + NUM + "7.5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo reduce el daño recibido por ",
                "veneno."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.HELMET_ARMADURA_BLINDADA.get(),
                "Esta armadura te aumenta " + NUM + "4 Puntos " + GRIS + "en las partes ", "del cuerpo y " + NUM + "10 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo brinda defensa superior y ",
                "maxima durabilidad."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.CHESTPLATE_ARMADURA_BLINDADA.get(),
                "Esta armadura te aumenta " + NUM + "4 Puntos " + GRIS + "en las partes ", "del cuerpo y " + NUM + "10 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo brinda defensa superior y ",
                "maxima durabilidad."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.LEGGINS_ARMADURA_BLINDADA.get(),
                "Esta armadura te aumenta " + NUM + "4 Puntos " + GRIS + "en las partes ", "del cuerpo y " + NUM + "10 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo brinda defensa superior y ",
                "maxima durabilidad."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.BOOTS_ARMADURA_BLINDADA.get(),
                "Esta armadura te aumenta " + NUM + "4 Puntos " + GRIS + "en las partes ", "del cuerpo y " + NUM + "10 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo brinda defensa superior y ",
                "maxima durabilidad."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.HELMET_TRAJE_BIOSEGURIDAD.get(),
                "Esta armadura te aumenta " + NUM + "1 Punto " + GRIS + "en las partes ", "del cuerpo y " + NUM + "2.5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo elimina por completo la " + BR + RAD +
                        "radiación."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.CHESTPLATE_TRAJE_BIOSEGURIDAD.get(),
                "Esta armadura te aumenta " + NUM + "1 Punto " + GRIS + "en las partes ", "del cuerpo y " + NUM + "2.5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo elimina por completo la " + BR + RAD +
                        "radiación."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.LEGGINS_TRAJE_BIOSEGURIDAD.get(),
                "Esta armadura te aumenta " + NUM + "1 Punto " + GRIS + "en las partes ", "del cuerpo y " + NUM + "2.5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo elimina por completo la " + BR + RAD +
                        "radiación."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(ModItems.BOOTS_TRAJE_BIOSEGURIDAD.get(),
                "Esta armadura te aumenta " + NUM + "1 Punto " + GRIS + "en las partes ", "del cuerpo y " + NUM + "2.5 Puntos "  + GRIS + "en la " + ROJO +
                        "Vida Principal. " + BR + GRIS +"El set completo elimina por completo la " + BR + RAD +
                "radiación."+ BR + BR + GRIS + "Puedes repararla con un " + BYI + "Kit de Costura.");

        registrar(Items.STRING,
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "cajas," + BR + BYI + "cuerpos " + GRIS + "y " + BYI + "contenedores.");

        registrar(Items.LEATHER,
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "cajas," + BR + BYI + "cuerpos " + GRIS + "y " + BYI + "contenedores.");

        registrar(Items.GUNPOWDER,
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "cajas," + BR + BYI + "cuerpos " + GRIS + "y " + BYI + "contenedores.");

        registrar(Items.PAPER,
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "cajas," + BR + BYI + "cuerpos " + GRIS + "y " + BYI + "contenedores.");

        registrar(Items.CHARCOAL,
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.");

        registrar(Items.IRON_NUGGET,
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "barriles," + BR + RESET + "y " + BYI + "contenedores.");

        registrar(Items.GOLD_NUGGET,
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "barriles," + BR + RESET + "y " + BYI + "contenedores.");

        registrar(Items.IRON_INGOT,
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "barriles," + BR + RESET + "y " + BYI + "contenedores.");

        registrar(Items.GOLD_INGOT,
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "barriles," + BR + RESET + "y " + BYI + "contenedores.");

        registrar(Items.COPPER_INGOT,
                "Combina este material con otros ", "recursos para obtener nuevos ", "objetos.",
                "Puedes encontrarlo en " + BYI + "barriles," + BR + RESET + "y " + BYI + "contenedores.");

        registrar(ModItems.INFO_REGLAS.get(),
                ROJO_OSCURO + "✞ Avancen juntos: " + GRIS + "En estas calles, un jugador aislado", "es un jugador muerto. Mantengan la formación y no ",
                        "se separen del grupo." + BR + BR + ROJO_OSCURO + "✞ Cooperación absoluta: " + GRIS + "Compartan recursos, munición", "y suministros médicos. " +
                        "La supervivencia depende del", "grupo, no de individualismos." + BR + BR + ROJO_OSCURO + "✞ Cero sabotaje: " + GRIS + "Está estrictamente prohibido provocar",
                        "la muerte de un compañero, de forma directa o indirecta.", "Cuídense la espalda entre todos." + BR + BR + ROJO_OSCURO + "✞ Entorno Modificado: " + GRIS +
                        "Esto no es un servidor Vanilla", "tradicional. Aquí todo se rige por mecánicas técnicas,", "médicas y de supervivencia urbana pesada. Adapta tu",
                        "estrategia desde el primer minuto.");

        registrar(ModItems.INFO_SERVER.get(),
                CON + "⟡ Despawn de Ítems: " + GRIS + "El ambiente corroerá cualquier", "recurso abandonado, todo ítem en el suelo desaparecerá", "en solo 60 segundos. Recoge tu botín de inmediato.",
                        BR + CON +"⟡ Oscuridad Absoluta: " + GRIS + "La noche en Huatenolgo es total.", "La visibilidad será de cero sin fuentes de luz, prepárate ", "antes de que caiga el sol.",
                        BR + CON +"⟡ Eventos Diarios: " + GRIS + "La ciudad cambia constantemente.", "Cada día se activarán nuevos eventos y peligros en", "el mapa.",
                        BR + CON +"⟡ Soporte Administrativo: " + GRIS + "Si experimentas un bug crítico", "o necesitas asistencia técnica del Staff, ejecuta", "el comando /help en el chat para enviar una", "alerta a los administradores.");

        registrar(ModItems.MARCADOR_MINAS_AZUFRE.get(),
                "Zona inestable con depósitos de Azufre.", "El ambiente es tóxico, po lo que requiere", "el " + BIO + "Traje de Bioseguridad " + GRIS + "completo para", "poder entrar de manera segura.");

        registrar(ModItems.MARCADOR_MINAS_TITANIO.get(),
                "Zona inestable con depósitos de Titanio.", "El ambiente es tóxico, po lo que requiere", "el " + BIO + "Traje de Bioseguridad " + GRIS + "completo para", "poder entrar de manera segura.");

        registrar(ModItems.MARCADOR_DROP.get(),
                "Cargamento Militar lanzado por vía", "aerea. Puede contener " + REC + "Munición," + BR + REC + "Armamento, Comida y Medicamentos");

        registrar(ModItems.INFO_CABEZA.get(),
                "Si recibes daño de Fuego, Lava, Caída, del Fundidor", "o Explosiones, tendrás diferentes efectos según", "cúantos puntos de vida tengas en cada parte del", "cuerpo."
                        + BR + BR + PART +"ᗢ Efectos que puedes recibir si te hacen dalo en la" + BR + PART + "cabeza." + BR + BR + PART + "➤ Ceguera." + BR + PART + "➤ Nauseas");

        registrar(ModItems.INFO_CUERPO.get(),
                "Si recibes daño de Fuego, Lava, Caída, del Infectado", "o Explosiones, tendrás diferentes efectos según", "cúantos puntos de vida tengas en cada parte del", "cuerpo."
                        + BR + BR + PART +"ᗢ Efectos que puedes recibir si te hacen dalo en el" + BR + PART + "cuerpo." + BR + BR + PART + "➤ Debilidad.");

        registrar(ModItems.INFO_BRAZOS.get(),
                "Si recibes daño de Fuego, Lava, Caída, del Infectado", "o Explosiones, tendrás diferentes efectos según", "cúantos puntos de vida tengas en cada parte del", "cuerpo."
                        + BR + BR + PART +"ᗢ Efectos que puedes recibir si te hacen dalo en los" + BR + PART + "brazos." + BR + BR + PART + "➤ Debilidad.");

        registrar(ModItems.INFO_PIERNAS.get(),
                "Si recibes daño de Fuego, Lava, Caída, del Arrastrador", "o Explosiones, tendrás diferentes efectos según", "cúantos puntos de vida tengas en cada parte del", "cuerpo."
                        + BR + BR + PART +"ᗢ Efectos que puedes recibir si te hacen dalo en las" + BR + PART + "piernas." + BR + BR + PART + "➤ Lentitud.");



        colorNombre(ModItems.BACKPACK.get(), 0xffff55);
        colorNombre(ModItems.PEZNSINITA_BRUTE.get(), 0xff7625);
        colorNombre(ModItems.PEZNSINITA_INGOT.get(), 0xff7625);
        colorNombre(ModItems.TITANIUM_BRUTE.get(), 0x7372ae);
        colorNombre(ModItems.TITANIUM_INGOT.get(), 0x7372ae);
        colorNombre(ModItems.AGUJA_HILO.get(), 0xffff55);
        colorNombre(ModItems.AZUFRE.get(), 0xefd63f);
        colorNombre(ModItems.GLANDULA_APESTOSA.get(), 0x568553);
        colorNombre(ModItems.ESPORAS.get(), 0x568553);
        colorNombre(ModItems.FISHY.get(), 0xee57c33);
        colorNombre(ModItems.MUESTRA_VIRUS.get(), 0x962424);
        colorNombre(ModItems.DOCUMENTOS_PEZNT.get(), 0xe57c33);
        colorNombre(ModItems.VENDAS.get(), 0xAA0000);
        colorNombre(ModItems.BOTIQUIN.get(), 0xAA0000);
        colorNombre(ModItems.ADRENALINA.get(), 0x8a203d);
        colorNombre(ModItems.MORFINA.get(), 0x26528d);
        colorNombre(ModItems.COAGULANTE .get(), 0x59a353);
        colorNombre(ModItems.QUELANTE .get(), 0xc4c864);
        colorNombre(ModItems.CURA .get(), 0xeda173);
        colorNombre(ModItems.PEZNSINITA_PICKAXE.get(), 0xff7625);
        colorNombre(ModItems.PEZNSINITA_AXE.get(), 0xff7625);
        colorNombre(ModItems.PEZNSINITA_SHOVEL.get(), 0xff7625);
        colorNombre(ModItems.PEZNSINITA_HOE.get(), 0xff7625);
        colorNombre(ModItems.HELMET_ARMADURA_TACTICA .get(), 0xac9d91);
        colorNombre(ModItems.CHESTPLATE_ARMADURA_TACTICA .get(), 0xac9d91);
        colorNombre(ModItems.LEGGINS_ARMADURA_TACTICA .get(), 0xac9d91);
        colorNombre(ModItems.BOOTS_ARMADURA_TACTICA .get(), 0xac9d91);
        colorNombre(ModItems.HELMET_ARMADURA_CHATARRA_REFORZADA .get(), 0x747c7f);
        colorNombre(ModItems.CHESTPLATE_ARMADURA_CHATARRA_REFORZADA .get(), 0x747c7f);
        colorNombre(ModItems.LEGGINS_ARMADURA_CHATARRA_REFORZADA .get(), 0x747c7f);
        colorNombre(ModItems.BOOTS_ARMADURA_CHATARRA_REFORZADA .get(), 0x747c7f);
        colorNombre(ModItems.HELMET_ARMADURA_CONTENCION .get(), 0x59709a);
        colorNombre(ModItems.CHESTPLATE_ARMADURA_CONTENCION .get(), 0x59709a);
        colorNombre(ModItems.LEGGINS_ARMADURA_CONTENCION .get(), 0x59709a);
        colorNombre(ModItems.BOOTS_ARMADURA_CONTENCION .get(), 0x59709a);
        colorNombre(ModItems.HELMET_ARMADURA_BLINDADA .get(), 0xff7625);
        colorNombre(ModItems.CHESTPLATE_ARMADURA_BLINDADA .get(), 0xff7625);
        colorNombre(ModItems.LEGGINS_ARMADURA_BLINDADA .get(), 0xff7625);
        colorNombre(ModItems.BOOTS_ARMADURA_BLINDADA .get(), 0xff7625);
        colorNombre(ModItems.HELMET_TRAJE_BIOSEGURIDAD .get(), 0xcaa226);
        colorNombre(ModItems.CHESTPLATE_TRAJE_BIOSEGURIDAD .get(), 0xcaa226);
        colorNombre(ModItems.LEGGINS_TRAJE_BIOSEGURIDAD .get(), 0xcaa226);
        colorNombre(ModItems.BOOTS_TRAJE_BIOSEGURIDAD .get(), 0xcaa226);

        colorNombre(ModItems.BOTON_1 .get(), 0xc4a333);
        colorNombre(ModItems.BOTON_2 .get(), 0x4768a1);
        colorNombre(ModItems.BOTON_3 .get(), 0x962424);


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
            if (linea.isEmpty()) {

                salida.add(Component.empty());
                continue;
            }

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