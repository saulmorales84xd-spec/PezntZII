package net.saullmc.pezntz.client.screen.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.block.ModBlocks;
import net.minecraft.world.entity.EntityType;
import net.saullmc.pezntz.client.screen.FondoBorroso;
import net.saullmc.pezntz.entity.ModEntities;
import net.saullmc.pezntz.item.ModItems;
import net.saullmc.pezntz.item.ModTooltips;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MenuSeccionScreen extends Screen {

    public static final int COLUMNAS = 7;

    public static final int FILAS_VISIBLES = 4;

    public static final int CELDA = 24;

    public static final int HUECO = 4;

    public static final float ESCALA_ITEM = 1.0F;

    public static final int OFFSET_X = 0;
    public static final int OFFSET_Y = 0;

    public static final int COLOR_CELDA_SOBRE = 0;

    public static final boolean OSCURECER_FONDO = true;

    public static final boolean MOSTRAR_BARRA = true;
    public static final int BARRA_ANCHO = 4;
    public static final int BARRA_SEPARACION = 150;
    public static final int COLOR_BARRA_FONDO = 0x50FFFFFF;
    public static final int COLOR_BARRA = 0xFFFFFFFF;

    public static final int BARRA_ALTO = 0;

    public static final int BARRA_OFFSET_Y = 0;

    public static final int FILAS_POR_PASO = 1;

    public static final boolean MOSTRAR_VOLVER = true;

    public static final ResourceLocation TEXTURA_VOLVER =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/boton_volver.png");

    public static final int VOLVER_TEXTURA_ANCHO = 32;
    public static final int VOLVER_TEXTURA_ALTO = 32;

    public static final int VOLVER_ANCHO = 32;
    public static final int VOLVER_ALTO = 32;

    public static final int VOLVER_U = 0;
    public static final int VOLVER_V = 0;

    public static final int VOLVER_V_SOBRE = -1;

    public static final float VOLVER_ESCALA = 1.0F;

    public static final boolean VOLVER_PEGADO_ABAJO = false;

    public static final int VOLVER_OFFSET_X = 0;
    public static final int VOLVER_OFFSET_Y = 60;

    public static final int VOLVER_COLOR_SOBRE = 0x40FFFFFF;

    public static final String SONIDO_VOLVER = "minecraft:ui.button.click";
    public static final float TONO_VOLVER = 0.9F;

    public static final int MARGEN_VOLVER = 6;

    public static final boolean RECORTAR_CONTENIDO = true;

    public static final int RECORTE_ARRIBA = 0;

    public static final int RECORTE_ABAJO = 0;

    private static final int RATON_FUERA = -10000;

    private static List<EntidadMenu> entidades(int seccion) {
        return switch (seccion) {

            case 0 -> List.of(
                    new EntidadMenu(EntityType.TROPICAL_FISH).en(90, -70).tamanio(50).variante(EntidadMenu.PEZ_PAYASO).giro(220, 0F)
                            .titulo("Pez payaso"));

            case 2 -> List.of(

                    new EntidadMenu(EntityType.ZOMBIE).en(90, 0).tamanio(30).giro(220,0).secreto()
                            .titulo("Infectado",0x962424)
                            .linea("Humano transformado por el Virus. Ya no razona")
                            .linea("ni recuerda nada, ataca a cualquier cosa que")
                            .linea("se mueva. Aunque son lentos y predecibles,")
                            .linea("pueden ser muy peligrosos en grupo.")
                            .lineaVacia()
                            .linea(ModTooltips.ROJ + "❤ Vida: " + ModTooltips.GRIS + "20")
                            .linea(ModTooltips.NUM + "\uD83D\uDC80 Daño Base: " + ModTooltips.GRIS + "3 " + ModTooltips.CON + "➤ Cuerpo y Brazos")
                            .linea(ModTooltips.ITEMS + "☣ Habilidad: " + ModTooltips.GRIS + "No tiene. "),

                    new EntidadMenu(ModEntities.ZOMBIE_HINCHADO.get()).en(-40, 70).tamanio(30).giro(220,0).secreto()
                            .titulo("Hinchado",0x962424)
                            .linea("Los Hinchados se inflan hasta")
                            .linea("explotar, destruyendo todo a su")
                            .linea("alrededor.")
                            .lineaVacia()
                            .linea(ModTooltips.ROJ + "❤ Vida: " + ModTooltips.GRIS + "50")
                            .linea(ModTooltips.NUM + "\uD83D\uDC80 Daño Base: " + ModTooltips.GRIS + "Según la distancia " + ModTooltips.CON + "➤ Cuerpo y Brazos")
                            .linea(ModTooltips.ITEMS + "☣ Habilidad: " + ModTooltips.GRIS + "Explota y destruye todo a su alrededor. "),

            new EntidadMenu(ModEntities.ZOMBIE_TANQUE.get()).en(90, 160).tamanio(20).giro(220,0).secreto()
                            .titulo("Tanque",0x962424)
                            .linea("Humano masivamente mutado por la infección.")
                            .linea("Su densidad muscular y ósea aumentó a niveles")
                            .linea("monstruosos, volviéndolo prácticamente inmune")
                            .linea("convencional. Es lento, pero un solo impacto")
                            .linea("puede ser fatal.")
                            .lineaVacia()
                            .linea(ModTooltips.ROJ + "❤ Vida: " + ModTooltips.GRIS + "50")
                            .linea(ModTooltips.NUM + "\uD83D\uDC80 Daño Base: " + ModTooltips.GRIS + "4 " + ModTooltips.CON + "➤ Cuerpo y Brazos")
                            .linea(ModTooltips.ITEMS + "☣ Habilidad: " + ModTooltips.GRIS + "Lento y muy fuerte. Imposible")
                            .linea("de empujar."),

            new EntidadMenu(ModEntities.ZOMBIE_ARRASTRADOR.get()).en(50, 50).tamanio(30).giro(220,0).secreto()
                            .titulo("Arrastrador",0x962424)
                            .linea("Se arrastra por el suelo y ataca a corta")
                            .linea("distancia. Inflige daño en los pies, pudiendo")
                            .linea("provocarte problemas de movilidad.")
                            .lineaVacia()
                            .linea(ModTooltips.ROJ + "❤ Vida: " + ModTooltips.GRIS + "50")
                            .linea(ModTooltips.NUM + "\uD83D\uDC80 Daño Base: " + ModTooltips.GRIS + "4 " + ModTooltips.CON + "➤ Piernas")
                            .linea(ModTooltips.ITEMS + "☣ Habilidad: " + ModTooltips.GRIS + "No tiene. "),

            new EntidadMenu(ModEntities.ZOMBIE_TOXICO.get()).en(130, 70).tamanio(30).giro(220,0).secreto()
                            .titulo("Fundidor",0x962424)
                            .linea("Infectado con una alteración en su sistema")
                            .linea("digestivo que genera fluidos corrosivos.")
                            .linea("Incapaz de atacar de cerca, lanza proyectiles")
                            .linea("de ácido a distancia que derriban la armadura")
                            .linea("y queman la piel.")
                            .lineaVacia()
                            .linea(ModTooltips.ROJ + "❤ Vida: " + ModTooltips.GRIS + "50")
                            .linea(ModTooltips.NUM + "\uD83D\uDC80 Daño Base: " + ModTooltips.GRIS + "4 " + ModTooltips.CON + "➤ Cabeza")
                            .linea(ModTooltips.ITEMS + "☣ Habilidad: " + ModTooltips.GRIS + "Lanza bolas de acidos que puedes ")
                            .linea("destruir si las golpeas"),

            new EntidadMenu(ModEntities.ZOMBIE_PARASITADOR.get()).en(220, 70).tamanio(30).giro(220,0).secreto()
                            .titulo("Parasitador",0x962424)
                            .linea("Criatura alterada que segrega una toxina paralizante")
                            .linea("a través de sus garras. Un solo impacto directo")
                            .linea("paraliza el sistema nervioso del sobreviviente")
                            .linea("dejándolo indefenso e incapaz de moverse por varios")
                            .linea("segundos.")
                            .lineaVacia()
                            .linea(ModTooltips.ROJ + "❤ Vida: " + ModTooltips.GRIS + "50")
                            .linea(ModTooltips.NUM + "\uD83D\uDC80 Daño Base: " + ModTooltips.GRIS + "4 " + ModTooltips.CON + "➤ Piernas")
                            .linea(ModTooltips.ITEMS + "☣ Habilidad: " + ModTooltips.GRIS + "Te paraliza durante unos segundos, ")
                            .linea("pulsa repetidamente [espacio] para eliminar la")
                            .linea("paralisis"),

            new EntidadMenu(ModEntities.RATA_CARRONERA.get()).en(0, 140).tamanio(30).giro(220,0).secreto()
                            .titulo("Rata carronera",0x962424)
                            .linea("Animal de gran tamaño y mucha resistencia.")
                            .linea("Ataca con golpes contundentes. Imposible de")
                            .linea("desplazarlo con un ataque cuerpo a cuerpo.")
                            .lineaVacia()
                            .linea(ModTooltips.ROJ + "❤ Vida: " + ModTooltips.GRIS + "50")
                            .linea(ModTooltips.NUM + "\uD83D\uDC80 Daño Base: " + ModTooltips.GRIS + "4 " + ModTooltips.CON + "➤ Cuerpo y Brazos")
                            .linea(ModTooltips.ITEMS + "☣ Habilidad: " + ModTooltips.GRIS + "Lento y muy fuerte. Imposible")
                            .linea("de empujar."),

                    new EntidadMenu(ModEntities.MOSQUITO_ZUMBADOR.get()).en(180, 140).tamanio(30).giro(220,0).secreto()
                            .titulo("Mosquito zumbador",0x962424)
                            .linea("Insecto mutado por la contaminación del entorno.")
                            .linea("Su picadura transmite toxinas mientras se alimenta")
                            .linea("de los sobrevivientes. Su velocidad y capacidad")
                            .linea("para volar en enjambre lo convierten en un objetivo")
                            .linea("de los sobrevivientes. Su velocidad y capacidad")
                            .linea("difícil de golpear.")
                            .lineaVacia()
                            .linea(ModTooltips.ROJ + "❤ Vida: " + ModTooltips.GRIS + "50")
                            .linea(ModTooltips.NUM + "\uD83D\uDC80 Daño Base: " + ModTooltips.GRIS + "4 " + ModTooltips.CON + "➤ Brazos")
                            .linea(ModTooltips.ITEMS + "☣ Habilidad: " + ModTooltips.GRIS + "Capacidad de volaar y esquivar. ")
                            .linea("proyectiles")
            );

            default -> List.of();
        };
    }

    private static List<ItemMenu> contenido(int seccion) {
        return switch (seccion) {

            case 0 -> List.of(
                    new ItemMenu(ModItems.INFO_REGLAS.get()).en(40, -40).escala(1.35F),
                    new ItemMenu(ModItems.INFO_SERVER.get()).en(140, -37).escala(1F),
                    new ItemMenu(ModItems.MARCADOR_MINAS_AZUFRE.get()).en(40, 0).escala(1.25F),
                    new ItemMenu(ModItems.MARCADOR_DROP.get()).en(90, 0).escala(1.25F).imagenItem(ModItems.CHATARRA.get(), 0, 0),
                    new ItemMenu(ModItems.MARCADOR_MINAS_TITANIO.get()).en(140, 0).escala(1.25F),
                    new ItemMenu(ModItems.INFO_CABEZA.get()).en(90, 40).escala(1.25F),
                    new ItemMenu(ModItems.INFO_CUERPO.get()).en(90, 70).escala(1.25F),
                    new ItemMenu(ModItems.INFO_BRAZOS.get()).en(110, 70).escala(1.25F),
                    new ItemMenu(ModItems.INFO_BRAZOS.get()).en(70, 70).escala(1.25F),
                    new ItemMenu(ModItems.INFO_VIDA.get()).en(40, 70).escala(1.35F),
                    new ItemMenu(ModItems.INFO_SUPERVIVENCIA.get()).en(140, 70).escala(1.3F),
                    new ItemMenu(ModItems.INFO_PIERNAS.get()).en(91, 100).escala(1.25F));


            case 1 -> List.of(

                    new ItemMenu(ModItems.MUESTRA_VIRUS.get()).en(60, -60).escala(1.5F),
                    new ItemMenu(ModItems.FISHY.get()).en(90, -60).escala(1.5F),
                    new ItemMenu(ModItems.DOCUMENTOS_PEZNT.get()).en(120, -60).escala(1.5F),

                    new ItemMenu(ModItems.TELA.get()).en(-40, -10).escala(1.25F),
                    new ItemMenu(ModItems.CHATARRA.get()).en(-10, -10).escala(1.25F),
                    new ItemMenu(ModItems.CHATARRA_REFORZADA.get()).en(20, -10).escala(1.25F),
                    new ItemMenu(ModItems.AGUJA_HILO.get()).en(50, -10).escala(1.25F).imagenItem(Items.STRING, 0.7F, 0.7F).imagenItem(Items.IRON_NUGGET,5.7F,0.7F).imagenItem(Items.STRING,10.7F,0.7F).imagenItem(Items.IRON_NUGGET,0.7F,5.7F).imagenItem(Items.STRING,5.7F,5.7F).imagenItem(Items.IRON_NUGGET,10.7F,5.7F).imagenItem(Items.LEATHER,5.7F,10.7F),
                    new ItemMenu(ModItems.PEZNSINITA_BRUTE.get()).en(80, -10).escala(1.25F),
                    new ItemMenu(ModItems.PEZNSINITA_INGOT.get()).en(110, -10).escala(1.25F),
                    new ItemMenu(ModItems.TITANIUM_BRUTE.get()).en(140, -10).escala(1.25F),
                    new ItemMenu(ModItems.TITANIUM_INGOT.get()).en(170, -10).escala(1.25F),
                    new ItemMenu(ModItems.AZUFRE.get()).en(200, -10).escala(1.25F),
                    new ItemMenu(ModItems.BACKPACK.get()).en(230, -10).escala(1.25F).imagenItem(Items.LEATHER, 0.7F, 0.7F).imagenItem(ModItems.TELA.get(),5.7F,0.7F).imagenItem(Items.LEATHER,10.7F,0.7F).imagenItem(Items.LEATHER,0.7F,5.7F).imagenItem(ModItems.AGUJA_HILO.get(),5.7F,5.7F).imagenItem(Items.LEATHER,10.7F,5.7F).imagenItem(Items.LEATHER,0.7F,10.7F).imagenItem(Items.LEATHER,5.7F,10.7F).imagenItem(Items.LEATHER,10.7F,10.7F),
                    new ItemMenu(ModItems.CANDADO.get()).en(-40, 20).escala(1.25F).imagenItem(ModItems.PLACA_ACERO.get(),5.7F, 0.7F).imagenItem(Items.IRON_NUGGET, 0.7F, 5.7F).imagenItem(Items.QUARTZ,5.7F, 5.7F).imagenItem(Items.IRON_NUGGET, 10.7F, 5.7F),
                    new ItemMenu(ModItems.LLAVE_CANDADO.get()).en(-10, 20).escala(1.25F).imagenItem(Items.IRON_INGOT, 0.7F, 5.7F).imagenItem(Items.IRON_NUGGET,5.7F,5.7F).imagenItem(Items.IRON_NUGGET,10.7F, 5.7F),
                    new ItemMenu(ModItems.LLAVE_VEHICULO.get()).en(20, 20).escala(1.25F).imagenItem(Items.GOLD_INGOT, 0.7F, 5.7F).imagenItem(Items.GOLD_NUGGET,5.7F,5.7F).imagenItem(Items.GOLD_NUGGET,10.7F, 5.7F),
                    new ItemMenu(ModItems.VENDAS.get()).en(50, 20).escala(1.25F).imagenItem(ModItems.TELA.get(),0.7F, 0.7F).imagenItem(Items.STRING,5.7F, 0.7F).imagenItem(ModItems.TELA.get(),5.7F, 5.7F).imagenItem(Items.STRING,0.7F, 5.7F),
                    new ItemMenu(ModItems.BOTIQUIN.get()).en(80, 20).escala(1.25F),
                    new ItemMenu(ModItems.BATERIAS.get()).en(110, 20).escala(1.25F),
                    new ItemMenu(ModItems.LANTERN.get()).en(140, 20).escala(1.25F).imagenItem(ModItems.CHATARRA.get(),5.7F, 0.7F).imagenItem(Items.IRON_INGOT, 0.7F, 5.7F).imagenItem(ModItems.BATERIAS.get(),5.7F, 5.7F).imagenItem(Items.IRON_INGOT,10.7F, 5.7F).imagenItem(Items.IRON_INGOT,5.7F, 10.7F),
                    new ItemMenu(ModItems.ABRELATAS.get()).en(170, 20).escala(1.25F).imagenItem(ModItems.CHATARRA.get(),10.7F, 0.7F).imagenItem(Items.IRON_INGOT,5.7F, 5.7F).imagenItem(Items.IRON_INGOT, 0.7F, 10.7F),
                    new ItemMenu(ModItems.BIDON_GASOLINA.get()).en(200, 20).escala(1.25F).imagenItem(ModItems.BOTELLA_ALCOHOL.get(),0.7F, 0.7F).imagenItem(Items.IRON_INGOT,5.7F, 0.7F).imagenItem(ModItems.BOTELLA_ALCOHOL.get(), 10.7F, 0.7F).imagenItem(Items.LEATHER, 0.7F, 5.7F).imagenItem(ModItems.CINTA.get(), 5.7F, 5.7F).imagenItem(Items.LEATHER, 10.7F, 5.7F).imagenItem(Items.LEATHER, 0.7F, 10.7F).imagenItem(Items.LEATHER, 5.7F, 10.7F).imagenItem(Items.LEATHER, 10.7F, 10.7F),
                    new ItemMenu(ModItems.KIT_VEHICULOS.get()).en(230, 20).escala(1.25F).imagenItem(ModItems.CHATARRA_REFORZADA.get(), 0.7F, 0.7F).imagenItem(ModItems.CHATARRA_REFORZADA.get(), 10.7F, 0.7F).imagenItem(ModItems.TUERCA.get(), 0.7F, 5.7F).imagenItem(ModItems.CHATARRA_REFORZADA.get(), 5.7F, 5.7F).imagenItem(ModItems.RESORTE.get(), 10.7F, 5.7F).imagenItem(Items.IRON_INGOT, 0.7F, 10.7F).imagenItem(Items.GOLD_INGOT, 5.7F, 10.7F).imagenItem(Items.IRON_INGOT, 10.7F, 10.7F),

                    new ItemMenu(Item.byBlock(ModBlocks.PRENSA.get())).en(-40, 60).escala(1.25F).imagenItem(ModItems.TUERCA.get(),0.7F, 0.7F).imagenItem(ModItems.RESORTE.get(),5.7F, 0.7F).imagenItem(ModItems.CLAVOS.get(),10.7F, 0.7F).imagenItem(Items.IRON_INGOT,0.7F, 5.7F).imagenItem(Items.COPPER_INGOT,5.7F, 5.7F).imagenItem(Items.IRON_INGOT,10.7F, 5.7F).imagenItem(Blocks.COPPER_BLOCK.asItem(),0.7F, 10.7F).imagenItem(Items.IRON_INGOT,5.7F, 10.7F).imagenItem(Blocks.COPPER_BLOCK.asItem(),10.7F, 10.7F),
                    new ItemMenu(Item.byBlock(ModBlocks.BARRIL_1.get())).en(-10, 60).escala(1.25F),
                    new ItemMenu(Item.byBlock(ModBlocks.CAJA_2.get())).en(20, 60).escala(1.25F),
                    new ItemMenu(Item.byBlock(ModBlocks.CAJON_DOBLE.get())).en(50, 60).escala(1.25F),
                    new ItemMenu(Item.byBlock(ModBlocks.CAJA_MILITAR.get())).en(80, 60).escala(1.25F),
                    new ItemMenu(Item.byBlock(ModBlocks.CUERPO_3.get())).en(110, 60).escala(1.25F),
                    new ItemMenu(Item.byBlock(ModBlocks.STONE_QUARTZ.get())).en(140, 60).escala(1.25F),
                    new ItemMenu(Item.byBlock(ModBlocks.STONE_TITANIUM.get())).en(170, 60).escala(1.25F),
                    new ItemMenu(Item.byBlock(ModBlocks.METEORITO_PEZNSINITA.get())).en(200, 60).escala(1.25F),

                    new ItemMenu(ModItems.ADRENALINA.get()).en(35, 100).escala(1.25F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),0.7F, 5.7F).imagenItem(ModItems.JERINGA_VACIA.get(),5.7F, 5.7F).imagenItem(ModItems.FRUTO_ESTIMULANTE.get(),10.7F, 5.7F).imagenItem(ModItems.BATERIAS.get(),5.7F, 10.7F),
                    new ItemMenu(ModItems.MORFINA.get()).en(65, 100).escala(1.25F).imagenItem(ModItems.VENDAS.get(),0.7F, 5.7F).imagenItem(ModItems.JERINGA_VACIA.get(),5.7F, 5.7F).imagenItem(ModItems.FRUTO_ESTIMULANTE.get(),10.7F, 5.7F).imagenItem(ModItems.BOTELLA_ALCOHOL.get(),5.7F, 10.7F),
                    new ItemMenu(ModItems.COAGULANTE.get()).en(95, 100).escala(1.25F).imagenItem(Items.BONE_MEAL,0.7F, 5.7F).imagenItem(ModItems.JERINGA_VACIA.get(),5.7F, 5.7F).imagenItem(ModItems.ESPORAS.get(),10.7F, 5.7F).imagenItem(ModItems.GLANDULA_APESTOSA.get(),5.7F, 10.7F),
                    new ItemMenu(ModItems.QUELANTE.get()).en(125, 100).escala(1.25F).imagenItem(ModItems.AZUFRE.get(),0.7F, 5.7F).imagenItem(ModItems.JERINGA_VACIA.get(),5.7F, 5.7F).imagenItem(ModItems.AZUFRE.get(),10.7F, 5.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),5.7F, 10.7F),
                    new ItemMenu(ModItems.CURA.get()).en(155, 100).escala(1.25F),

                    new ItemMenu(ModItems.HELMET_TRAJE_BIOSEGURIDAD.get()).en(35, 140).escala(1.25F).imagenItem(Items.COPPER_INGOT,0.7F, 0.7F).imagenItem(Items.COPPER_INGOT,5.7F, 0.7F).imagenItem(Items.COPPER_INGOT,10.7F, 0.7F).imagenItem(ModItems.TELA.get(),0.7F, 5.7F).imagenItem(ModItems.TELA.get(),10.7F, 5.7F).imagenItem(ModItems.AGUJA_HILO.get(),5.7F, 5.7F),
                    new ItemMenu(ModItems.CHESTPLATE_TRAJE_BIOSEGURIDAD.get()).en(35, 160).escala(1.25F).imagenItem(Items.LEATHER,0.7F, 0.7F).imagenItem(Items.LEATHER,10.7F, 0.7F).imagenItem(Items.LEATHER,0.7F, 5.7F).imagenItem(ModItems.AGUJA_HILO.get(),5.7F, 5.7F).imagenItem(Items.LEATHER,10.7F, 5.7F).imagenItem(Items.GOLD_INGOT,0.7F, 10.7F).imagenItem(Items.GOLD_INGOT,5.7F, 10.7F).imagenItem(Items.GOLD_INGOT,10.7F, 10.7F),
                    new ItemMenu(ModItems.LEGGINS_TRAJE_BIOSEGURIDAD.get()).en(35, 180).escala(1.25F).imagenItem(Items.LEATHER,0.7F, 0.7F).imagenItem(Items.LEATHER,5.7F, 0.7F).imagenItem(Items.LEATHER,10.7F, 0.7F).imagenItem(Items.LEATHER,0.7F, 5.7F).imagenItem(ModItems.AGUJA_HILO.get(),5.7F, 5.7F).imagenItem(Items.LEATHER,10.7F, 5.7F).imagenItem(Items.GOLD_INGOT,0.7F, 10.7F).imagenItem(Items.GOLD_INGOT,10.7F, 10.7F),
                    new ItemMenu(ModItems.BOOTS_TRAJE_BIOSEGURIDAD.get()).en(35, 200).escala(1.25F).imagenItem(Items.COPPER_INGOT,0.7F, 0.7F).imagenItem(Items.COPPER_INGOT,10.7F, 0.7F).imagenItem(ModItems.TELA.get(),0.7F, 5.7F).imagenItem(ModItems.AGUJA_HILO.get(),5.7F, 5.7F).imagenItem(ModItems.TELA.get(),10.7F, 5.7F),
                    new ItemMenu(ModItems.HELMET_ARMADURA_TACTICA.get()).en(65, 140).escala(1.25F).imagenItem(Items.IRON_INGOT,0.7F, 0.7F).imagenItem(Items.IRON_INGOT,5.7F, 0.7F).imagenItem(Items.IRON_INGOT,10.7F, 0.7F).imagenItem(ModItems.TELA.get(),0.7F, 5.7F).imagenItem(ModItems.AGUJA_HILO.get(),5.7F, 5.7F).imagenItem(ModItems.TELA.get(),10.7F, 5.7F),
                    new ItemMenu(ModItems.CHESTPLATE_ARMADURA_TACTICA.get()).en(65, 160).escala(1.25F).imagenItem(Items.IRON_INGOT,0.7F, 0.7F).imagenItem(ModItems.AGUJA_HILO.get(),5.7F, 5.7F).imagenItem(Items.IRON_INGOT,10.7F, 0.7F).imagenItem(ModItems.TELA.get(),0.7F, 5.7F).imagenItem(Items.IRON_INGOT,10.7F, 10.7F).imagenItem(Items.IRON_INGOT,0.7F, 10.7F).imagenItem(Items.IRON_INGOT,5.7F, 10.7F).imagenItem(ModItems.TELA.get(),10.7F, 5.7F),
                    new ItemMenu(ModItems.LEGGINS_ARMADURA_TACTICA.get()).en(65, 180).escala(1.25F).imagenItem(Items.IRON_INGOT,0.7F, 0.7F).imagenItem(Items.IRON_INGOT,5.7F, 0.7F).imagenItem(Items.IRON_INGOT,10.7F, 0.7F).imagenItem(Items.IRON_INGOT,0.7F, 5.7F).imagenItem(ModItems.AGUJA_HILO.get(),5.7F, 5.7F).imagenItem(Items.IRON_INGOT,10.7F, 5.7F).imagenItem(ModItems.TELA.get(),0.7F, 10.7F).imagenItem(ModItems.TELA.get(),10.7F, 10.7F),
                    new ItemMenu(ModItems.BOOTS_ARMADURA_TACTICA.get()).en(65, 200).escala(1.25F).imagenItem(Items.IRON_INGOT,0.7F, 0.7F).imagenItem(Items.IRON_INGOT,10.7F, 0.7F).imagenItem(ModItems.TELA.get(),0.7F, 5.7F).imagenItem(ModItems.AGUJA_HILO.get(),5.7F, 5.7F).imagenItem(ModItems.TELA.get(),10.7F, 5.7F),
                    new ItemMenu(ModItems.HELMET_ARMADURA_CHATARRA_REFORZADA.get()).en(95, 140).escala(1.25F).imagenItem(Items.DIAMOND,0.7F, 0.7F).imagenItem(ModItems.CHATARRA_REFORZADA.get(),5.7F, 0.7F).imagenItem(Items.DIAMOND,10.7F, 0.7F).imagenItem(ModItems.CHATARRA_REFORZADA.get(),0.7F, 5.7F).imagenItem(ModItems.HELMET_ARMADURA_TACTICA.get(),5.7F, 5.7F).imagenItem(ModItems.CHATARRA_REFORZADA.get(),10.7F, 5.7F),
                    new ItemMenu(ModItems.CHESTPLATE_ARMADURA_CHATARRA_REFORZADA.get()).en(95, 160).escala(1.25F).imagenItem(ModItems.CHATARRA_REFORZADA.get(),0.7F, 0.7F).imagenItem(ModItems.CHESTPLATE_ARMADURA_TACTICA.get(),5.7F, 0.7F).imagenItem(ModItems.CHATARRA_REFORZADA.get(),10.7F, 0.7F).imagenItem(Items.DIAMOND,0.7F, 5.7F).imagenItem(ModItems.CHATARRA_REFORZADA.get(),5.7F, 5.7F).imagenItem(Items.DIAMOND,10.7F, 5.7F).imagenItem(ModItems.CHATARRA_REFORZADA.get(),0.7F, 10.7F).imagenItem(Items.DIAMOND,5.7F, 10.7F).imagenItem(ModItems.CHATARRA_REFORZADA.get(),10.7F, 10.7F),
                    new ItemMenu(ModItems.LEGGINS_ARMADURA_CHATARRA_REFORZADA.get()).en(95, 180).escala(1.25F).imagenItem(ModItems.CHATARRA_REFORZADA.get(),0.7F, 0.7F).imagenItem(ModItems.CHATARRA_REFORZADA.get(),5.7F, 0.7F).imagenItem(ModItems.CHATARRA_REFORZADA.get(),10.7F, 0.7F).imagenItem(ModItems.CHATARRA_REFORZADA.get(),0.7F, 5.7F).imagenItem(ModItems.LEGGINS_ARMADURA_TACTICA.get(),5.7F, 5.7F).imagenItem(ModItems.CHATARRA_REFORZADA.get(),10.7F, 5.7F).imagenItem(Items.DIAMOND,0.7F, 10.7F).imagenItem(Items.DIAMOND,10.7F, 10.7F),
                    new ItemMenu(ModItems.BOOTS_ARMADURA_CHATARRA_REFORZADA.get()).en(95, 200).escala(1.25F).imagenItem(ModItems.CHATARRA_REFORZADA.get(),0.7F, 0.7F).imagenItem(ModItems.CHATARRA_REFORZADA.get(),10.7F, 0.7F).imagenItem(Items.DIAMOND,0.7F, 5.7F).imagenItem(ModItems.BOOTS_ARMADURA_TACTICA.get(),5.7F, 5.7F).imagenItem(Items.DIAMOND,10.7F, 5.7F),
                    new ItemMenu(ModItems.HELMET_ARMADURA_CONTENCION.get()).en(125, 140).escala(1.25F).imagenItem(ModItems.TITANIUM_INGOT.get(),0.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_BRUTE.get(),5.7F, 0.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),10.7F, 0.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),0.7F, 5.7F).imagenItem(ModItems.HELMET_ARMADURA_CHATARRA_REFORZADA.get(),5.7F, 5.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),10.7F, 5.7F),
                    new ItemMenu(ModItems.CHESTPLATE_ARMADURA_CONTENCION.get()).en(125, 160).escala(1.25F).imagenItem(ModItems.TITANIUM_INGOT.get(),0.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_BRUTE.get(),5.7F, 0.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),10.7F, 0.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),0.7F, 5.7F).imagenItem(ModItems.CHESTPLATE_ARMADURA_CHATARRA_REFORZADA.get(),5.7F, 5.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),10.7F, 5.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),0.7F, 10.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),5.7F, 10.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),10.7F, 10.7F),
                    new ItemMenu(ModItems.LEGGINS_ARMADURA_CONTENCION.get()).en(125, 180).escala(1.25F).imagenItem(ModItems.TITANIUM_INGOT.get(),0.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_BRUTE.get(),5.7F, 0.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),10.7F, 0.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),0.7F, 5.7F).imagenItem(ModItems.LEGGINS_ARMADURA_CHATARRA_REFORZADA.get(),5.7F, 5.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),10.7F, 5.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),0.7F, 10.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),10.7F, 10.7F),
                    new ItemMenu(ModItems.BOOTS_ARMADURA_CONTENCION.get()).en(125, 200).escala(1.25F).imagenItem(ModItems.TITANIUM_INGOT.get(),0.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_BRUTE.get(),5.7F, 0.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),10.7F, 0.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),0.7F, 5.7F).imagenItem(ModItems.BOOTS_ARMADURA_CHATARRA_REFORZADA.get(),5.7F, 5.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),10.7F, 5.7F),
                    new ItemMenu(ModItems.HELMET_ARMADURA_BLINDADA.get()).en(155, 140).escala(1.25F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),0.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),5.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),10.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),0.7F, 5.7F).imagenItem(ModItems.HELMET_ARMADURA_CONTENCION.get(),5.7F, 5.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),10.7F, 5.7F),
                    new ItemMenu(ModItems.CHESTPLATE_ARMADURA_BLINDADA.get()).en(155, 160).escala(1.25F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),0.7F, 0.7F).imagenItem(ModItems.CHESTPLATE_ARMADURA_CONTENCION.get(),5.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),10.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),0.7F, 5.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),5.7F, 5.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),10.7F, 5.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),0.7F, 10.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),5.7F, 10.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),10.7F, 10.7F),
                    new ItemMenu(ModItems.LEGGINS_ARMADURA_BLINDADA.get()).en(155, 180).escala(1.25F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),0.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),5.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),10.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),0.7F, 5.7F).imagenItem(ModItems.LEGGINS_ARMADURA_CONTENCION.get(),5.7F, 5.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),10.7F, 5.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),0.7F, 10.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),10.7F, 10.7F),
                    new ItemMenu(ModItems.BOOTS_ARMADURA_BLINDADA.get()).en(155, 200).escala(1.25F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),0.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),10.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),0.7F, 5.7F).imagenItem(ModItems.BOOTS_ARMADURA_CONTENCION.get(),5.7F, 5.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),10.7F, 5.7F),

                    new ItemMenu(ModItems.CUCHILLO.get()).en(-10, 240).escala(1.25F).imagenItem(Items.STICK,5.7F, 10.7F).imagenItem(Items.IRON_INGOT,5.7F, 5.7F),
                    new ItemMenu(ModItems.BATE.get()).en(20, 240).escala(1.25F).imagenItem(Items.STICK,0.7F, 10.7F).imagenItem(Items.SPRUCE_LOG,5.7F, 5.7F).imagenItem(Items.SPRUCE_LOG,10.7F, 0.7F),
                    new ItemMenu(ModItems.LUCILLE.get()).en(50, 240).escala(1.25F).imagenItem(ModItems.CLAVOS.get(),5.7F, 0.7F).imagenItem(ModItems.CLAVOS.get(),0.7F, 5.7F).imagenItem(ModItems.BATE.get(),5.7F, 5.7F).imagenItem(ModItems.CLAVOS.get(),10.7F, 5.7F),
                    new ItemMenu(ModItems.PALANCA.get()).en(80, 240).escala(1.25F).imagenItem(Items.IRON_INGOT,0.7F, 10.7F).imagenItem(Items.IRON_INGOT,5.7F, 5.7F).imagenItem(Items.IRON_INGOT,10.7F, 0.7F).imagenItem(ModItems.CHATARRA.get(),10.7F, 5.7F),
                    new ItemMenu(ModItems.GUITARRA.get()).en(110, 240).escala(1.25F).imagenItem(Items.IRON_NUGGET,0.7F, 0.7F).imagenItem(Items.OAK_PLANKS,5.7F, 0.7F).imagenItem(Items.IRON_NUGGET,10.7F, 0.7F).imagenItem(Items.OAK_PLANKS,0.7F, 5.7F).imagenItem(ModItems.CABLES.get(),5.7F, 5.7F).imagenItem(Items.OAK_PLANKS,10.7F, 5.7F).imagenItem(Items.OAK_PLANKS,0.7F, 10.7F).imagenItem(Items.OAK_PLANKS,5.7F, 10.7F).imagenItem(Items.OAK_PLANKS,10.7F, 10.7F),
                    new ItemMenu(ModItems.KATANA.get()).en(140, 240).escala(1.25F).imagenItem(Items.STICK,0.7F, 10.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),5.7F, 5.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),10.7F, 0.7F),
                    new ItemMenu(ModItems.MOTOSIERRA.get()).en(170, 240).escala(1.25F).imagenItem(ModItems.CLAVOS.get(),0.7F, 0.7F).imagenItem(Items.IRON_INGOT,5.7F, 0.7F).imagenItem(ModItems.CLAVOS.get(),10.7F, 0.7F).imagenItem(Items.IRON_INGOT,0.7F, 5.7F).imagenItem(ModItems.BOTELLA_ALCOHOL.get(), 5.7F, 5.7F).imagenItem(Items.IRON_INGOT,10.7F, 5.7F).imagenItem(Items.IRON_INGOT,0.7F, 10.7F).imagenItem(Items.IRON_INGOT,5.7F, 10.7F).imagenItem(Items.IRON_INGOT,10.7F, 10.7F),
                    new ItemMenu(ModItems.LANZALLAMAS.get()).en(200, 240).escala(1.25F).imagenItem(ModItems.BOTELLA_ALCOHOL.get(),5.7F, 0.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),0.7F, 5.7F).imagenItem(ModItems.PEZNSINITA_BRUTE.get(),5.7F, 5.7F).imagenItem(ModItems.BOTELLA_ALCOHOL.get(),10.7F, 5.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),0.7F, 10.7F).imagenItem(ModItems.TITANIUM_INGOT.get(),5.7F, 10.7F),

                    new ItemMenu(ModItems.PEZNSINITA_PICKAXE.get()).en(50, 280).escala(1.25F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),0.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),5.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),10.7F, 0.7F).imagenItem(Items.STICK,5.7F, 5.7F).imagenItem(Items.STICK,5.7F, 10.7F),
                    new ItemMenu(ModItems.PEZNSINITA_AXE.get()).en(80, 280).escala(1.25F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),0.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),5.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),0.7F, 5.7F).imagenItem(Items.STICK,5.7F, 5.7F).imagenItem(Items.STICK,5.7F, 10.7F),
                    new ItemMenu(ModItems.PEZNSINITA_SHOVEL.get()).en(110, 280).escala(1.25F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),5.7F, 0.7F).imagenItem(Items.STICK,5.7F, 5.7F).imagenItem(Items.STICK,5.7F, 10.7F),
                    new ItemMenu(ModItems.PEZNSINITA_HOE.get()).en(140, 280).escala(1.25F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),0.7F, 0.7F).imagenItem(ModItems.PEZNSINITA_INGOT.get(),5.7F, 0.7F).imagenItem(Items.STICK,5.7F, 5.7F).imagenItem(Items.STICK,5.7F, 10.7F),

                    ItemMenu.caja("9mm", ItemMenu.CAJA_HIERRO, 0).en(65, 320).escala(1.5F).imagenItem(ModItems.TUERCA.get(),0.7F, 0.7F).imagenItem(ModItems.RESORTE.get(),5.7F, 0.7F).imagenItem(ModItems.CLAVOS.get(),10.7F, 0.7F).imagenItem(Items.IRON_INGOT,0.7F, 5.7F).imagenItem(ModItems.PEZNSINITA_BRUTE.get(),5.7F, 5.7F).imagenItem(Items.IRON_INGOT,10.7F, 5.7F).imagenItem(Blocks.IRON_BLOCK.asItem(),0.7F, 10.7F).imagenItem(Items.IRON_INGOT,5.7F, 10.7F).imagenItem(Blocks.IRON_BLOCK.asItem(), 10.7F, 10.7F)
                            .nombre("Caja de Munición Pequeña [☆]", 0x6b8178)
                            .tooltip(ModTooltips.KEYS + "[Click Izquierdo] " + ModTooltips.GRIS + "para arrastrar la caja hasta",
                                    "tus balas " + ModTooltips.GRIS + "en el inventario.",
                                    ModTooltips.KEYS + "[Click Derecho] " + ModTooltips.GRIS + "sobre las balas con la caja",
                                    "para guardarlas.",
                                    "",
                                    ModTooltips.KEYS + "[Click Izquierdo] " + ModTooltips.GRIS + "para arrastrar la caja con",
                                    "balas a un lugar vacio en tu inventario.",
                                    ModTooltips.KEYS + "[Click Derecho] " + ModTooltips.GRIS + "para retirarlas de la caja.",
                                    "",
                                    ModTooltips.ROJO_OSCURO + "Importante: " + ModTooltips.GRIS + "Solamente puedes guardar un",
                                    "tipo de balas.",
                                    "",
                                    "Capacidad: " + ModTooltips.NUM + "180 Balas"),

                    ItemMenu.caja("9mm", ItemMenu.CAJA_ORO, 0).en(95, 320).escala(1.5F).imagenItem(ModItems.TUERCA.get(),0.7F, 0.7F).imagenItem(ModItems.RESORTE.get(),5.7F, 0.7F).imagenItem(ModItems.CLAVOS.get(),10.7F, 0.7F).imagenItem(Items.GOLD_INGOT,0.7F, 5.7F).imagenItem(ModItems.PEZNSINITA_BRUTE.get(),5.7F, 5.7F).imagenItem(Items.GOLD_INGOT,10.7F, 5.7F).imagenItem(Blocks.GOLD_BLOCK.asItem(),0.7F, 10.7F).imagenItem(Items.GOLD_INGOT,5.7F, 10.7F).imagenItem(Blocks.GOLD_BLOCK.asItem(), 10.7F, 10.7F)
                            .nombre("Caja de Munición Mediana [☆☆]", 0x6b8178)
                            .tooltip(ModTooltips.KEYS + "[Click Izquierdo] " + ModTooltips.GRIS + "para arrastrar la caja hasta",
                                    "tus balas " + ModTooltips.GRIS + "en el inventario.",
                                    ModTooltips.KEYS + "[Click Derecho] " + ModTooltips.GRIS + "sobre las balas con la caja",
                                    "para guardarlas.",
                                    "",
                                    ModTooltips.KEYS + "[Click Izquierdo] " + ModTooltips.GRIS + "para arrastrar la caja con",
                                    "balas a un lugar vacio en tu inventario.",
                                    ModTooltips.KEYS + "[Click Derecho] " + ModTooltips.GRIS + "para retirarlas de la caja.",
                                    "",
                                    ModTooltips.ROJO_OSCURO + "Importante: " + ModTooltips.GRIS + "Solamente puedes guardar un",
                                    "tipo de balas.",
                                    "",
                                    "Capacidad: " + ModTooltips.NUM + "360 Balas"),

                    ItemMenu.caja("9mm", ItemMenu.CAJA_DIAMANTE, 0).en(125, 320).escala(1.5F).imagenItem(ModItems.TUERCA.get(),0.7F, 0.7F).imagenItem(ModItems.RESORTE.get(),5.7F, 0.7F).imagenItem(ModItems.CLAVOS.get(),10.7F, 0.7F).imagenItem(Items.DIAMOND,0.7F, 5.7F).imagenItem(ModItems.PEZNSINITA_BRUTE.get(),5.7F, 5.7F).imagenItem(Items.DIAMOND,10.7F, 5.7F).imagenItem(Blocks.DIAMOND_BLOCK.asItem(),0.7F, 10.7F).imagenItem(Items.DIAMOND,5.7F, 10.7F).imagenItem(Blocks.DIAMOND_BLOCK.asItem(), 10.7F, 10.7F)
                            .nombre("Caja de Munición Grande [☆☆☆]", 0x6b8178)
                            .tooltip(ModTooltips.KEYS + "[Click Izquierdo] " + ModTooltips.GRIS + "para arrastrar la caja hasta",
                                    "tus balas " + ModTooltips.GRIS + "en el inventario.",
                                    ModTooltips.KEYS + "[Click Derecho] " + ModTooltips.GRIS + "sobre las balas con la caja",
                                    "para guardarlas.",
                                    "",
                                    ModTooltips.KEYS + "[Click Izquierdo] " + ModTooltips.GRIS + "para arrastrar la caja con",
                                    "balas a un lugar vacio en tu inventario.",
                                    ModTooltips.KEYS + "[Click Derecho] " + ModTooltips.GRIS + "para retirarlas de la caja.",
                                    "",
                                    ModTooltips.ROJO_OSCURO + "Importante: " + ModTooltips.GRIS + "Solamente puedes guardar un",
                                    "tipo de balas.",
                                    "",
                                    "Capacidad: " + ModTooltips.NUM + "540 Balas"),


                    ItemMenu.arma("glock_17").en(-110, 360).escala(1.3F),
                    ItemMenu.arma("taurus943").en(-110, 400).escala(1.3F),
                    ItemMenu.arma("taurus500").en(-80, 360).escala(1.3F),
                    ItemMenu.arma("deagle_golden").en(-80, 400).escala(1.3F),
                    ItemMenu.arma("db_short").en(-40, 360).escala(1.3F),
                    ItemMenu.arma("m870").en(-40, 400).escala(1.3F),
                    ItemMenu.arma("spas_12").en(-10, 380).escala(1.3F),
                    ItemMenu.arma("hk_mp5a5").en(30, 360).escala(1.3F),
                    ItemMenu.arma("uzi").en(30, 400).escala(1.3F),
                    ItemMenu.arma("p90").en(60, 380).escala(1.3F),
                    ItemMenu.arma("m16a1").en(100, 360).escala(1.4F),
                    ItemMenu.arma("scar_l").en(100, 400).escala(1.4F),
                    ItemMenu.arma("ak47").en(130, 360).escala(1.4F),
                    ItemMenu.arma("m4a1").en(130, 400).escala(1.4F),
                    ItemMenu.arma("aug").en(160, 380).escala(1.4F),
                    ItemMenu.arma("m95").en(200, 360).escala(1.4F),
                    ItemMenu.arma("ai_awp").en(200, 400).escala(1.4F),
                    ItemMenu.arma("kar98").en(230, 380).escala(1.4F),
                    ItemMenu.arma("m249").en(270, 360).escala(1.4F),
                    ItemMenu.arma("rpg7").en(270, 400).escala(1.4F),


                    ItemMenu.municion("9mm", 1).en(-80, 440).escala(1.3F),
                    ItemMenu.municion("22wmr", 1).en(-50, 440).escala(1.3F),
                    ItemMenu.municion("500mag", 1).en(-20, 440).escala(1.3F),
                    ItemMenu.municion("357mag", 1).en(10, 440).escala(1.3F),
                    ItemMenu.municion("12g", 1).en(40, 440).escala(1.3F),
                    ItemMenu.municion("57x28", 1).en(70, 440).escala(1.3F),
                    ItemMenu.municion("556x45", 1).en(100, 440).escala(1.3F),
                    ItemMenu.municion("762x39", 1).en(130, 440).escala(1.3F),
                    ItemMenu.municion("50bmg", 1).en(160, 440).escala(1.3F),
                    ItemMenu.municion("338", 1).en(190, 440).escala(1.3F),
                    ItemMenu.municion("792x57", 1).en(220, 440).escala(1.3F),
                    ItemMenu.municion("rpg_rocket", 1).en(250, 440).escala(1.3F),

                    ItemMenu.accesorio("grip_magpul_afg_2").en(-120, 480).escala(1.2F),
                    ItemMenu.accesorio("grip_rk0").en(-120, 520).escala(1.2F),
                    ItemMenu.accesorio("grip_se_5").en(-90, 480).escala(1.2F),
                    ItemMenu.accesorio("grip_rk1_b25u").en(-90, 520).escala(1.2F),
                    ItemMenu.accesorio("extended_mag_2").en(-50, 480).escala(1.2F),
                    ItemMenu.accesorio("extended_mag_3").en(-20, 480).escala(1.2F),
                    ItemMenu.accesorio("light_extended_mag_2").en(-50, 520).escala(1.2F),
                    ItemMenu.accesorio("light_extended_mag_3").en(-20, 520).escala(1.2F),
                    ItemMenu.accesorio("sniper_extended_mag_2").en(-50, 560).escala(1.2F),
                    ItemMenu.accesorio("sniper_extended_mag_3").en(-20, 560).escala(1.2F),
                    ItemMenu.accesorio("stock_tactical_ar").en(20, 480).escala(1.2F),
                    ItemMenu.accesorio("stock_m4ss").en(20, 520).escala(1.2F),
                    ItemMenu.accesorio("stock_moe").en(50, 480).escala(1.2F),
                    ItemMenu.accesorio("stock_carbon_bone_c5").en(50, 520).escala(1.2F),
                    ItemMenu.accesorio("deagle_golden_long_barrel").en(90, 480).escala(1.2F),
                    ItemMenu.accesorio("muzzle_silencer_vulture").en(90, 520).escala(1.2F),
                    ItemMenu.accesorio("muzzle_silencer_ursus").en(120, 480).escala(1.2F),
                    ItemMenu.accesorio("muzzle_brake_cyclone_d2").en(120, 520).escala(1.2F),
                    ItemMenu.accesorio("scope_lpvo_1_6").en(160, 480).escala(1.2F),
                    ItemMenu.accesorio("sight_coyote").en(160, 520).escala(1.2F),
                    ItemMenu.accesorio("scope_qmk152").en(190, 480).escala(1.2F),
                    ItemMenu.accesorio("sight_srs_02").en(190, 520).escala(1.2F),
                    ItemMenu.accesorio("laser_compact").en(230, 480).escala(1.2F),
                    ItemMenu.accesorio("laser_nightstick").en(230, 520).escala(1.2F),
                    ItemMenu.accesorio("laser_lopro").en(260, 500).escala(1.2F)

                    );

            default -> List.of(
                    ItemMenu.arma("ak47").en(40, -20).escala(1.5F).imagen("pezntz:textures/gui/receta.png", 16, 16)
                            .imagenEscala(3.0F)
                            .imagenMargen(2, 3)
                            .imagenItem(ModItems.CHATARRA.get(), 0, 0));
        };
    }

    private final int seccion;

    private final List<ItemMenu> rejilla = new ArrayList<>();

    private final List<ItemMenu> libres = new ArrayList<>();

    private final List<EntidadMenu> bichos = new ArrayList<>();

    private final FondoBorroso borroso = new FondoBorroso();

    private int desplazamiento = 0;
    private int rejillaX;
    private int rejillaY;

    private int volverX;
    private int volverY;
    private int volverAncho;
    private int volverAlto;

    public MenuSeccionScreen(int seccion) {
        super(Component.empty());
        this.seccion = seccion;
    }

    public int getSeccion() {
        return this.seccion;
    }

    @Override
    protected void init() {
        this.rejilla.clear();
        this.libres.clear();

        for (ItemMenu entrada : contenido(this.seccion)) {
            if (entrada.getStack().isEmpty()) continue;

            if (entrada.tienePosicion()) this.libres.add(entrada);
            else this.rejilla.add(entrada);
        }

        this.bichos.clear();
        this.bichos.addAll(entidades(this.seccion));

        this.borroso.preparar();

        this.rejillaX = (this.width - anchoRejilla()) / 2 + OFFSET_X;
        this.rejillaY = (this.height - altoRejilla()) / 2 + OFFSET_Y;

        this.volverAncho = Math.max(1, Math.round(VOLVER_ANCHO * VOLVER_ESCALA));
        this.volverAlto = Math.max(1, Math.round(VOLVER_ALTO * VOLVER_ESCALA));

        this.volverX = (this.width - this.volverAncho) / 2 + VOLVER_OFFSET_X;
        this.volverY = VOLVER_PEGADO_ABAJO
                ? this.height - this.volverAlto - VOLVER_OFFSET_Y
                : this.rejillaY + altoRejilla() + VOLVER_OFFSET_Y;

        this.desplazamiento = Mth.clamp(this.desplazamiento, 0, desplazamientoMaximo());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static int anchoRejilla() {
        return COLUMNAS * CELDA + (COLUMNAS - 1) * HUECO;
    }

    private static int altoRejilla() {
        return FILAS_VISIBLES * CELDA + (FILAS_VISIBLES - 1) * HUECO;
    }

    private int filasTotales() {
        return (this.rejilla.size() + COLUMNAS - 1) / COLUMNAS;
    }

    private static int paso() {
        return CELDA + HUECO;
    }

    private int altoVisible() {
        if (!MOSTRAR_VOLVER) return altoRejilla();

        return Math.max(altoRejilla(), this.volverY - MARGEN_VOLVER - this.rejillaY);
    }

    private int recorteArriba() {
        return Math.max(0, RECORTE_ARRIBA);
    }

    private int recorteAbajo() {
        if (!MOSTRAR_VOLVER) return this.height;

        return Mth.clamp(this.volverY - MARGEN_VOLVER - RECORTE_ABAJO, 0, this.height);
    }

    private boolean hayRecorte() {
        if (!RECORTAR_CONTENIDO || !MOSTRAR_VOLVER) return false;

        return recorteAbajo() > recorteArriba() && recorteAbajo() < this.height;
    }

    private int fondoLibres() {
        int fondo = 0;

        for (ItemMenu entrada : this.libres) {
            int tamanio = Math.max(1, Math.round(16.0F * entrada.getEscala(ESCALA_ITEM)));
            fondo = Math.max(fondo, entrada.getY() + tamanio);
        }

        return fondo;
    }

    private int desplazamientoMaximo() {
        int porRejilla = Math.max(0, filasTotales() - FILAS_VISIBLES);

        int sobra = fondoLibres() - altoVisible();
        int porLibres = sobra <= 0 ? 0 : (sobra + paso() - 1) / paso();

        return Math.max(porRejilla, porLibres);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {

        boolean hayDesenfoque = this.borroso.dibujar(partialTick);

        if (hayDesenfoque) {
            if ((FondoBorroso.COLOR_VELO >>> 24) != 0) {
                g.fill(0, 0, this.width, this.height, FondoBorroso.COLOR_VELO);
            }
        } else if (OSCURECER_FONDO) {
            this.renderBackground(g);
        }

        boolean recorte = hayRecorte();
        int arriba = recorteArriba();
        int abajo = recorteAbajo();

        int ratonY = (recorte && (mouseY < arriba || mouseY >= abajo)) ? RATON_FUERA : mouseY;

        if (recorte) g.enableScissor(0, arriba, this.width, abajo);

        ItemMenu encima = dibujarRejilla(g, mouseX, ratonY);

        ItemMenu encimaLibre = dibujarLibres(g, mouseX, ratonY);
        if (encimaLibre != null) encima = encimaLibre;

        EntidadMenu encimaBicho = dibujarBichos(g, mouseX, ratonY);

        if (recorte) g.disableScissor();

        dibujarBarra(g);

        super.render(g, mouseX, mouseY, partialTick);

        dibujarVolver(g, mouseX, mouseY);

        if (encimaBicho != null) {
            List<Component> lineas = encimaBicho.getTooltip();
            if (!lineas.isEmpty()) {
                g.renderTooltip(this.font, lineas, encimaBicho.getImagenTooltip(), mouseX, mouseY);
            }

        } else if (encima != null) {
            List<Component> lineas = encima.getTooltip();
            if (!lineas.isEmpty()) {
                g.renderTooltip(this.font, lineas, encima.getImagenTooltip(), mouseX, mouseY);
            }
        }
    }

    @Nullable
    private EntidadMenu dibujarBichos(GuiGraphics g, int mouseX, int mouseY) {
        EntidadMenu encima = null;

        for (EntidadMenu bicho : this.bichos) {
            int x = this.rejillaX + bicho.getX();
            int y = this.rejillaY + bicho.getY() - this.desplazamiento * paso();

            int zona = bicho.getZona();
            int alto = bicho.getAlto();


            if (mouseX >= x - zona && mouseX <= x + zona
                    && mouseY >= y - alto - zona && mouseY <= y + zona) {
                encima = bicho;
            }

            bicho.dibujar(g, x, y);
        }

        return encima;
    }

    @Override
    public void removed() {

        this.borroso.cerrar();
        super.removed();
    }

    @Nullable
    private ItemMenu dibujarRejilla(GuiGraphics g, int mouseX, int mouseY) {
        ItemMenu encima = null;

        for (int fila = 0; fila < FILAS_VISIBLES; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {

                int indice = (fila + this.desplazamiento) * COLUMNAS + col;
                if (indice >= this.rejilla.size()) continue;

                ItemMenu entrada = this.rejilla.get(indice);

                int x = this.rejillaX + col * (CELDA + HUECO);
                int y = this.rejillaY + fila * (CELDA + HUECO);

                if (dentro(mouseX, mouseY, x, y, CELDA, CELDA)) {
                    if ((COLOR_CELDA_SOBRE >>> 24) != 0) {
                        g.fill(x, y, x + CELDA, y + CELDA, COLOR_CELDA_SOBRE);
                    }
                    encima = entrada;
                }

                float escala = entrada.getEscala(ESCALA_ITEM);
                float tamanio = 16.0F * escala;

                dibujarItem(g, entrada,
                        Math.round(x + (CELDA - tamanio) / 2.0F),
                        Math.round(y + (CELDA - tamanio) / 2.0F),
                        escala);
            }
        }

        return encima;
    }

    @Nullable
    private ItemMenu dibujarLibres(GuiGraphics g, int mouseX, int mouseY) {
        ItemMenu encima = null;

        for (ItemMenu entrada : this.libres) {
            float escala = entrada.getEscala(ESCALA_ITEM);
            int tamanio = Math.max(1, Math.round(16.0F * escala));

            int x = this.rejillaX + entrada.getX();
            int y = this.rejillaY + entrada.getY() - this.desplazamiento * paso();

            if (dentro(mouseX, mouseY, x, y, tamanio, tamanio)) {
                if ((COLOR_CELDA_SOBRE >>> 24) != 0) {
                    g.fill(x, y, x + tamanio, y + tamanio, COLOR_CELDA_SOBRE);
                }
                encima = entrada;
            }

            dibujarItem(g, entrada, x, y, escala);
        }

        return encima;
    }

    private void dibujarItem(GuiGraphics g, ItemMenu entrada, int x, int y, float escala) {
        ItemStack stack = entrada.getStack();

        g.pose().pushPose();

        g.pose().translate(x, y, 0.0F);
        g.pose().scale(escala, escala, 1.0F);

        g.renderItem(stack, 0, 0);
        decorar(g, this.font, stack, entrada.tieneBarra(), entrada.tieneCantidad());

        g.pose().popPose();
    }

    public static void decorar(GuiGraphics g, Font fuente, ItemStack stack,
                               boolean barra, boolean cantidad) {

        if (barra) {
            g.renderItemDecorations(fuente, stack, 0, 0);
            return;
        }

        if (!cantidad || stack.getCount() <= 1) return;

        String texto = String.valueOf(stack.getCount());

        g.drawString(fuente, texto,
                19 - 2 - fuente.width(texto), 6 + 3,
                0xFFFFFF, true);
    }

    private void dibujarBarra(GuiGraphics g) {
        if (!MOSTRAR_BARRA) return;

        int maximo = desplazamientoMaximo();
        if (maximo <= 0) return;

        int visible = altoVisible();
        int total = visible + maximo * paso();

        int altoBarra = BARRA_ALTO > 0 ? BARRA_ALTO : visible;

        int x = this.rejillaX + anchoRejilla() + BARRA_SEPARACION;
        int arriba = this.rejillaY + BARRA_OFFSET_Y;

        g.fill(x, arriba, x + BARRA_ANCHO, arriba + altoBarra, COLOR_BARRA_FONDO);

        int altoPulgar = Math.max(8, altoBarra * visible / total);
        int recorrido = altoBarra - altoPulgar;
        int y = arriba + recorrido * this.desplazamiento / maximo;

        g.fill(x, y, x + BARRA_ANCHO, y + altoPulgar, COLOR_BARRA);
    }

    private void dibujarVolver(GuiGraphics g, int mouseX, int mouseY) {
        if (!MOSTRAR_VOLVER) return;

        boolean sobre = dentro(mouseX, mouseY, this.volverX, this.volverY, this.volverAncho, this.volverAlto);

        int v = (sobre && VOLVER_V_SOBRE >= 0) ? VOLVER_V_SOBRE : VOLVER_V;

        g.blit(TEXTURA_VOLVER,
                this.volverX, this.volverY,
                this.volverAncho, this.volverAlto,
                (float) VOLVER_U, (float) v,
                VOLVER_ANCHO, VOLVER_ALTO,
                VOLVER_TEXTURA_ANCHO, VOLVER_TEXTURA_ALTO);

        if (sobre && VOLVER_V_SOBRE < 0 && (VOLVER_COLOR_SOBRE >>> 24) != 0) {
            g.fill(this.volverX, this.volverY,
                    this.volverX + this.volverAncho, this.volverY + this.volverAlto,
                    VOLVER_COLOR_SOBRE);
        }
    }

    private static boolean dentro(int mx, int my, int x, int y, int ancho, int alto) {
        return mx >= x && mx < x + ancho && my >= y && my < y + alto;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int maximo = desplazamientoMaximo();
        if (maximo <= 0) return super.mouseScrolled(mouseX, mouseY, delta);

        this.desplazamiento = Mth.clamp(
                this.desplazamiento - (int) Math.signum(delta) * FILAS_POR_PASO, 0, maximo);

        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int boton) {
        if (boton == 0 && MOSTRAR_VOLVER
                && dentro((int) mouseX, (int) mouseY, this.volverX, this.volverY, this.volverAncho, this.volverAlto)) {
            volver();
            return true;
        }

        if (boton == 1) {
            volver();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, boton);
    }

    private void volver() {
        SoundEvent evento = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(SONIDO_VOLVER));
        if (evento != null) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(evento, TONO_VOLVER));
        }

        Minecraft.getInstance().setScreen(new MenuPrincipalScreen());
    }
}