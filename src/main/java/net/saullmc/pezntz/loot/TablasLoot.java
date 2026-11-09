package net.saullmc.pezntz.loot;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TablasLoot {

    public static final int ITEMS_POR_CONTENEDOR = 5;

    public static final int PESO_NORMAL = 10;

    public static final int PESO_LIMITADO = 4;

    public static final int PESO_RARO = 3;

    public static final int PESO_MUY_RARO = 1;

    public static final boolean EVITAR_LINEAS = true;

    public static final boolean EVITAR_CUADROS = true;

    public static final boolean CONTAR_DIAGONALES = false;

    public static final int INTENTOS_REPARTO = 32;

    public static final String TELA = "pezntz:tela";
    public static final String CLAVOS = "pezntz:clavos";
    public static final String CHATARRA = "pezntz:chatarra";
    public static final String RESORTE = "pezntz:resorte";
    public static final String TUERCA = "pezntz:tuerca";
    public static final String CABLES = "pezntz:cables";
    public static final String CINTA = "pezntz:cinta";
    public static final String AZUFRE = "pezntz:azufre";
    public static final String BATERIAS = "pezntz:baterias";
    public static final String BOTELLA_ALCOHOL = "pezntz:botella_alcohol";
    public static final String COPPER_NUGGET = "pezntz:copper_nugget";

    public static final String LATA_POLLO = "pezntz:lata_pollo_cerrada";
    public static final String LATA_ATUN = "pezntz:lata_atun_cerrada";
    public static final String LATA_CARNE = "pezntz:lata_carne_cerrada";
    public static final String CEREALES = "pezntz:cereales";
    public static final String NACHOS = "pezntz:nachos";
    public static final String PAQUETE_MRE = "pezntz:paquete_mre";
    public static final String BARRA_CHOCOLATE = "pezntz:barra_chocolate";
    public static final String BARRA_GRANOLA = "pezntz:barra_granola";

    public static final String VENDAS = "pezntz:vendas";
    public static final String BOTIQUIN = "pezntz:botiquin";

    public static final String PAPEL = "minecraft:paper";
    public static final String HILO = "minecraft:string";
    public static final String CUERO = "minecraft:leather";
    public static final String POLVORA = "minecraft:gunpowder";
    public static final String CUARZO = "minecraft:quartz";
    public static final String PEPITAS_HIERRO = "minecraft:iron_nugget";
    public static final String PEPITAS_ORO = "minecraft:gold_nugget";
    public static final String LINGOTE_HIERRO = "minecraft:iron_ingot";
    public static final String LINGOTE_ORO = "minecraft:gold_ingot";
    public static final String LINGOTE_COBRE = "minecraft:copper_ingot";

    public static final String ITEM_MUNICION = "tacz:ammo";
    public static final String ITEM_ARMA = "tacz:modern_kinetic_gun";
    public static final String ITEM_ACCESORIO = "tacz:attachment";

    public static final String MODO_DISPARO = "SEMI";

    public static final String[] CALIBRES = {
            "9mm", "22wmr", "500mag", "357mag", "50bmg", "338", "792x57", "12g", "57x28",
            "556x45", "762x39"
    };

    public static final int PESO_MUNICION = 10;

    public static final boolean COHETES_RPG = true;
    public static final String COHETE_RPG = "rpg_rocket";
    public static final int CADA_CUANTO_COHETE = 200;
    public static final int COHETES_MIN = 1;
    public static final int COHETES_MAX = 2;

    public static final String[] ARMAS = {
            "glock_17", "taurus943", "taurus500", "deagle_golden", "m95", "ai_awp", "kar98",
            "db_short", "m870", "spas_12", "hk_mp5a5", "uzi", "p90", "m16a1", "scar_l", "ak47",
            "m4a1", "aug", "m249"
    };

    public static final boolean RPG = true;
    public static final String ARMA_RPG = "rpg7";
    public static final int CADA_CUANTO_RPG = 200;

    public static final String[] EMPUNADURAS = {
            "grip_magpul_afg_2", "grip_rk0", "grip_se_5", "grip_rk1_b25u"
    };

    public static final String[] CULATAS = {
            "stock_tactical_ar", "stock_m4ss", "stock_moe", "stock_carbon_bone_c5"
    };

    public static final String[] CANIONES = {
            "deagle_golden_long_barrel", "muzzle_silencer_vulture", "muzzle_silencer_ursus",
            "muzzle_brake_cyclone_d2"
    };

    public static final String[] MIRAS = {
            "scope_lpvo_1_6", "sight_coyote", "scope_qmk152", "sight_srs_02"
    };

    public static final String[] LASERES = {
            "laser_compact", "laser_nightstick", "laser_lopro"
    };

    public static final String[] CARGADORES = {
            "extended_mag_2", "extended_mag_3", "light_extended_mag_2", "light_extended_mag_3",
            "sniper_extended_mag_2", "sniper_extended_mag_3"
    };

    public static final int PESO_ARMA = 10;
    public static final int PESO_ACCESORIO = PESO_ARMA;

    public static final int BALAS_POR_CAJA = 30;

    public static final int MONTONES_BALAS_MIN = 6;
    public static final int MONTONES_BALAS_MAX = 8;

    public static final int PROBABILIDAD_ARMA_CAJA = 100;

    public static final int PROBABILIDAD_ARMA_CUERPO = 25;

    private static final Map<String, Tabla> TABLAS = new HashMap<>();

    static {

        Tabla muebles = new Tabla()
                .comun(TELA, 1, 2)
                .comun(CABLES, 1, 2)
                .comun(CINTA, 1, 1)
                .comun(COPPER_NUGGET, 1, 2)
                .comun(PEPITAS_HIERRO, 1, 3)
                .comun(HILO, 1, 2)
                .limitado(CLAVOS, 1, 2)
                .limitado(TUERCA, 1, 2)
                .limitado(BOTELLA_ALCOHOL, 1, 1)
                .raro(BATERIAS, 1, 1)
                .raro(CUERO, 1, 2)
                .conExplosivos();

        poner("armario_izq", muebles.copia());
        poner("armario_der", muebles.copia());
        poner("cajon_triple", muebles.copia());


        Tabla cocina = muebles.copia()
                .raro(LATA_ATUN, 1, 1)
                .raro(LATA_POLLO, 1, 1)
                .raro(LATA_CARNE, 1, 1)
                .raro(CEREALES, 1, 1)
                .raro(NACHOS, 1, 1)
                .raro(PAQUETE_MRE, 1, 1)
                .raro(BARRA_CHOCOLATE, 1, 2)
                .raro(BARRA_GRANOLA, 1, 2);

        poner("cajon_cocina_izq", cocina.copia());
        poner("cajon_cocina_der", cocina.copia());
        poner("encimera", cocina.copia());
        poner("cajon_doble", cocina.copia());


        poner("espejo", new Tabla()
                .fijo(VENDAS, 1)
                .comun(TELA, 1, 2)
                .comun(PAPEL, 1, 2)
                .comun(HILO, 1, 2)
                .muyRaro(BOTIQUIN, 1, 1)
                .conMinerales());


        Tabla lockers = new Tabla()
                .comun(TELA, 1, 2)
                .comun(CHATARRA, 1, 2)
                .comun(CABLES, 1, 2)
                .comun(CINTA, 1, 1)
                .comun(PEPITAS_HIERRO, 1, 3)
                .comun(PEPITAS_ORO, 1, 2)
                .comun(COPPER_NUGGET, 1, 2)
                .comun(HILO, 1, 2)
                .limitado(CLAVOS, 1, 2)
                .limitado(TUERCA, 1, 2)
                .limitado(RESORTE, 1, 2)
                .limitado(BOTELLA_ALCOHOL, 1, 1)
                .raro(BATERIAS, 1, 1)
                .raro(BARRA_CHOCOLATE, 1, 2)
                .raro(BARRA_GRANOLA, 1, 2)
                .raro(PAQUETE_MRE, 1, 1)
                .raro(LINGOTE_HIERRO, 1, 1)
                .raro(LINGOTE_ORO, 1, 1)
                .raro(LINGOTE_COBRE, 1, 1)
                .raro(CUERO, 1, 2)
                .conExplosivos();

        poner("locker", lockers.copia());
        poner("locker_abierto", lockers.copia());
        poner("locker_bajo", lockers.copia());
        poner("locker_bajo_abierto", lockers.copia());


        Tabla municion = new Tabla()
                .unSoloTipo(BALAS_POR_CAJA, MONTONES_BALAS_MIN, MONTONES_BALAS_MAX)
                .todaLaMunicion()
                .conArmeria(PROBABILIDAD_ARMA_CAJA);

        poner("cajas_militares", municion.copia());
        poner("caja_militar", municion.copia());
        poner("cuerpo_1", municion.copia());
        poner("cuerpo_3", municion.copia());
        poner("cuerpo_5", municion.copia());


        Tabla cuerpos = new Tabla()
                .comun(TELA, 1, 2)
                .comun(PEPITAS_HIERRO, 1, 3)
                .limitado(BOTELLA_ALCOHOL, 1, 1)
                .raro(BATERIAS, 1, 1)
                .raro(LATA_ATUN, 1, 1)
                .raro(LATA_POLLO, 1, 1)
                .raro(LATA_CARNE, 1, 1)
                .raro(BARRA_CHOCOLATE, 1, 2)
                .raro(BARRA_GRANOLA, 1, 2)
                .raro(PAQUETE_MRE, 1, 1)
                .raro(POLVORA, 1, 2)
                .conArmeria(PROBABILIDAD_ARMA_CUERPO);

        poner("cuerpo_2", cuerpos.copia());
        poner("cuerpo_4", cuerpos.copia());
        poner("cuerpo_6", cuerpos.copia());

    }

    private static void poner(String bloque, Tabla tabla) {
        TABLAS.put(bloque, tabla);
    }

    public static boolean tieneTabla(String bloque) {
        return TABLAS.containsKey(bloque);
    }

    public static void rellenar(Container contenedor, RandomSource azar, String bloque) {
        contenedor.clearContent();

        Tabla tabla = TABLAS.get(bloque);
        if (tabla == null) return;

        List<ItemStack> montones = tabla.unSoloTipo
                ? montonesDeUnSoloTipo(tabla, azar)
                : montonesSorteados(tabla, azar);

        aniadirArmeria(tabla, montones, azar);

        repartir(contenedor, montones, azar, !tabla.unSoloTipo);
    }

    private static List<ItemStack> montonesSorteados(Tabla tabla, RandomSource azar) {
        List<ItemStack> montones = new ArrayList<>();

        Map<String, ItemStack> porItem = new HashMap<>();

        int restante = tabla.items;

        for (Entrada fija : tabla.fijos) {
            if (restante <= 0) break;

            int cuantos = Math.min(fija.min, restante);
            if (!aniadir(montones, porItem, fija, cuantos, azar)) continue;

            restante -= cuantos;
        }

        Set<String> fuera = new HashSet<>();

        while (restante > 0) {
            Entrada elegida = sortear(tabla.sorteo, fuera, azar);
            if (elegida == null) break;

            int cuantos = Math.min(cantidad(elegida, azar), restante);

            if (!aniadir(montones, porItem, elegida, cuantos, azar)) {
                fuera.add(elegida.clave());
                continue;
            }

            if (elegida.unico) fuera.add(elegida.clave());

            restante -= cuantos;
        }

        return montones;
    }

    private static boolean aniadir(List<ItemStack> montones, Map<String, ItemStack> porItem,
                                   Entrada entrada, int cuantos, RandomSource azar) {

        ItemStack previo = porItem.get(entrada.clave());

        if (previo != null) {
            previo.grow(cuantos);
            return true;
        }

        ItemStack stack = construir(entrada, cuantos, azar);
        if (stack.isEmpty()) return false;

        montones.add(stack);
        porItem.put(entrada.clave(), stack);
        return true;
    }

    private static List<ItemStack> montonesDeUnSoloTipo(Tabla tabla, RandomSource azar) {
        List<ItemStack> montones = new ArrayList<>();

        if (tabla.cohete != null && azar.nextInt(Math.max(1, CADA_CUANTO_COHETE)) == 0) {
            int cohetes = COHETES_MIN + azar.nextInt(Math.max(1, COHETES_MAX - COHETES_MIN + 1));
            ItemStack stack = construir(tabla.cohete, cohetes, azar);

            if (!stack.isEmpty()) {
                montones.add(stack);
                return montones;
            }
        }

        Entrada elegida = sortear(tabla.sorteo, new HashSet<>(), azar);
        if (elegida == null) return montones;

        int cuantos = tabla.montonesMin + azar.nextInt(tabla.montonesMax - tabla.montonesMin + 1);
        int restante = tabla.cantidadTotal;

        cuantos = Math.max(1, Math.min(cuantos, restante));

        for (int i = 0; i < cuantos && restante > 0; i++) {
            int faltan = cuantos - i;

            int trozo;
            if (faltan == 1) {
                trozo = restante;
            } else {
                int parte = Math.max(1, restante / faltan);
                int meneo = Math.max(1, parte / 3);
                trozo = parte - meneo + azar.nextInt(meneo * 2 + 1);
                trozo = Math.max(1, Math.min(trozo, restante - (faltan - 1)));
            }

            ItemStack stack = construir(elegida, trozo, azar);
            if (stack.isEmpty()) break;

            int puesto = Math.min(trozo, stack.getMaxStackSize());
            stack.setCount(puesto);

            montones.add(stack);
            restante -= puesto;
        }

        return montones;
    }

    private static void aniadirArmeria(Tabla tabla, List<ItemStack> montones, RandomSource azar) {
        if (tabla.armeria.isEmpty()) return;
        if (azar.nextInt(100) >= tabla.probabilidadArmeria) return;

        if (tabla.rpg != null && azar.nextInt(Math.max(1, CADA_CUANTO_RPG)) == 0) {
            ItemStack stack = construir(tabla.rpg, 1, azar);

            if (!stack.isEmpty()) {
                montones.add(stack);
                return;
            }
        }

        Entrada elegida = sortear(tabla.armeria, new HashSet<>(), azar);
        if (elegida == null) return;

        ItemStack stack = construir(elegida, 1, azar);
        if (!stack.isEmpty()) montones.add(stack);
    }

    private static Entrada sortear(List<Entrada> entradas, Set<String> fuera, RandomSource azar) {
        int total = 0;
        for (Entrada entrada : entradas) {
            if (fuera.contains(entrada.clave())) continue;
            total += entrada.peso;
        }
        if (total <= 0) return null;

        int tirada = azar.nextInt(total);

        for (Entrada entrada : entradas) {
            if (fuera.contains(entrada.clave())) continue;

            tirada -= entrada.peso;
            if (tirada < 0) return entrada;
        }

        return null;
    }

    private static int cantidad(Entrada entrada, RandomSource azar) {
        return entrada.min + azar.nextInt(Math.max(1, entrada.max - entrada.min + 1));
    }

    private static ItemStack construir(Entrada entrada, int cantidad, RandomSource azar) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(entrada.id));
        if (item == null) return ItemStack.EMPTY;

        ItemStack stack = new ItemStack(item, Math.max(1, cantidad));

        if (entrada.nbt != null) {
            CompoundTag tag = leerNbt(entrada.nbt);
            if (tag != null) stack.setTag(tag);
        }

        return stack;
    }

    private static CompoundTag leerNbt(String texto) {
        try {
            return TagParser.parseTag(texto);
        } catch (CommandSyntaxException e) {
            return null;
        }
    }

    private static final int[][] LINEAS = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}};

    private static final int[][] DIAGONALES = {{0, 4, 8}, {2, 4, 6}};

    private static final int[][] CUADROS = {
            {0, 1, 3, 4}, {1, 2, 4, 5},
            {3, 4, 6, 7}, {4, 5, 7, 8}};

    private static void repartir(Container contenedor, List<ItemStack> montones,
                                 RandomSource azar, boolean cuidarDibujo) {
        int huecos = contenedor.getContainerSize();

        List<Integer> libres = new ArrayList<>();
        for (int i = 0; i < huecos; i++) {
            if (contenedor.getItem(i).isEmpty()) libres.add(i);
        }

        int cuantos = Math.min(montones.size(), libres.size());
        if (cuantos <= 0) return;

        List<Integer> elegidos = null;

        if (cuidarDibujo) {
            for (int intento = 0; intento < INTENTOS_REPARTO; intento++) {
                barajar(libres, azar);
                List<Integer> prueba = new ArrayList<>(libres.subList(0, cuantos));

                if (repartoLimpio(prueba, huecos)) {
                    elegidos = prueba;
                    break;
                }
            }
        }

        if (elegidos == null) {
            barajar(libres, azar);
            elegidos = new ArrayList<>(libres.subList(0, cuantos));
        }

        for (int i = 0; i < cuantos; i++) {
            contenedor.setItem(elegidos.get(i), montones.get(i));
        }
    }

    private static boolean repartoLimpio(List<Integer> elegidos, int huecos) {
        if (huecos != 9) return true;

        Set<Integer> puestos = new HashSet<>(elegidos);

        if (EVITAR_LINEAS && contieneAlguno(LINEAS, puestos)) return false;
        if (EVITAR_LINEAS && CONTAR_DIAGONALES && contieneAlguno(DIAGONALES, puestos)) return false;
        if (EVITAR_CUADROS && contieneAlguno(CUADROS, puestos)) return false;

        return true;
    }

    private static boolean contieneAlguno(int[][] grupos, Set<Integer> puestos) {
        for (int[] grupo : grupos) {
            boolean completo = true;

            for (int hueco : grupo) {
                if (!puestos.contains(hueco)) {
                    completo = false;
                    break;
                }
            }

            if (completo) return true;
        }
        return false;
    }

    private static void barajar(List<Integer> lista, RandomSource azar) {
        for (int i = lista.size() - 1; i > 0; i--) {
            Collections.swap(lista, i, azar.nextInt(i + 1));
        }
    }

    private record Entrada(String id, String nbt, int peso, int min, int max, boolean unico) {
        String clave() {
            return this.nbt == null ? this.id : this.id + this.nbt;
        }
    }

    public static class Tabla {
        private final List<Entrada> fijos = new ArrayList<>();
        private final List<Entrada> sorteo = new ArrayList<>();

        private final List<Entrada> armeria = new ArrayList<>();
        private Entrada rpg = null;
        private Entrada cohete = null;
        private int probabilidadArmeria = 0;

        private int items = ITEMS_POR_CONTENEDOR;

        private boolean unSoloTipo = false;
        private int cantidadTotal = 0;
        private int montonesMin = 1;
        private int montonesMax = 1;

        public Tabla fijo(String id, int cantidad) {
            this.fijos.add(new Entrada(id, null, 0, cantidad, cantidad, false));
            return this;
        }

        public Tabla comun(String id, int min, int max) {
            this.sorteo.add(new Entrada(id, null, PESO_NORMAL, min, max, false));
            return this;
        }

        public Tabla limitado(String id, int min, int max) {
            this.sorteo.add(new Entrada(id, null, PESO_LIMITADO, min, max, true));
            return this;
        }

        public Tabla raro(String id, int min, int max) {
            this.sorteo.add(new Entrada(id, null, PESO_RARO, min, max, true));
            return this;
        }

        public Tabla muyRaro(String id, int min, int max) {
            this.sorteo.add(new Entrada(id, null, PESO_MUY_RARO, min, max, true));
            return this;
        }

        public Tabla conMinerales() {
            return this.raro(CUARZO, 1, 2).muyRaro(AZUFRE, 1, 1);
        }

        public Tabla conExplosivos() {
            return this.raro(POLVORA, 1, 2).conMinerales();
        }

        public Tabla items(int cuantos) {
            this.items = cuantos;
            return this;
        }

        public Tabla municion(String[] calibres) {
            return this.municion(calibres, PESO_MUNICION);
        }

        public Tabla municion(String[] calibres, int peso) {
            for (String calibre : calibres) {
                this.sorteo.add(entradaMunicion(calibre, peso));
            }
            return this;
        }

        public Tabla todaLaMunicion() {
            this.municion(CALIBRES, PESO_MUNICION);

            if (COHETES_RPG) {
                this.cohete = entradaMunicion(COHETE_RPG, 1);
            }
            return this;
        }

        private static Entrada entradaMunicion(String calibre, int peso) {
            return new Entrada(ITEM_MUNICION, "{AmmoId:\"tacz:" + calibre + "\"}", peso, 1, 1, false);
        }

        public Tabla conArmeria(int probabilidad) {
            this.probabilidadArmeria = probabilidad;

            accesorios(EMPUNADURAS);
            accesorios(CULATAS);
            accesorios(CANIONES);
            accesorios(MIRAS);
            accesorios(LASERES);
            accesorios(CARGADORES);

            armas(ARMAS, PESO_ARMA);

            if (RPG) {
                this.rpg = entradaArma(ARMA_RPG, 1);
            }

            return this;
        }

        private void accesorios(String[] ids) {
            for (String id : ids) {
                this.armeria.add(new Entrada(ITEM_ACCESORIO, "{AttachmentId:\"tacz:" + id + "\"}",
                        PESO_ACCESORIO, 1, 1, false));
            }
        }

        private void armas(String[] ids, int peso) {
            for (String id : ids) {
                this.armeria.add(entradaArma(id, peso));
            }
        }

        private static Entrada entradaArma(String id, int peso) {
            return new Entrada(ITEM_ARMA,
                    "{GunId:\"tacz:" + id + "\",GunFireMode:\"" + MODO_DISPARO + "\"}",
                    peso, 1, 1, false);
        }

        public Tabla unSoloTipo(int total, int montonesMin, int montonesMax) {
            this.unSoloTipo = true;
            this.cantidadTotal = total;
            this.montonesMin = montonesMin;
            this.montonesMax = montonesMax;
            return this;
        }

        public Tabla copia() {
            Tabla nueva = new Tabla();
            nueva.fijos.addAll(this.fijos);
            nueva.sorteo.addAll(this.sorteo);
            nueva.armeria.addAll(this.armeria);
            nueva.rpg = this.rpg;
            nueva.cohete = this.cohete;
            nueva.probabilidadArmeria = this.probabilidadArmeria;
            nueva.items = this.items;
            nueva.unSoloTipo = this.unSoloTipo;
            nueva.cantidadTotal = this.cantidadTotal;
            nueva.montonesMin = this.montonesMin;
            nueva.montonesMax = this.montonesMax;
            return nueva;
        }
    }
}