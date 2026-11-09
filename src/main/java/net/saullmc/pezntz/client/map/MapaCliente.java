package net.saullmc.pezntz.client.map;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.mapa.DatosMapa;
import net.saullmc.pezntz.mapa.MapaInfo;
import net.saullmc.pezntz.mapa.MarcadorMapa;
import net.saullmc.pezntz.network.MapaCambiosPacket;
import net.saullmc.pezntz.network.MapaPedirPacket;
import net.saullmc.pezntz.network.NetworkHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT)
public class MapaCliente {

    public static final boolean GUARDAR_EN_CACHE = true;

    public static final int LADO_MINIMO_NIVEL = 256;

    private static final Logger LOGGER = LogUtils.getLogger();

    @Nullable
    private static MapaInfo info = null;

    @Nullable
    private static byte[] descarga = null;
    private static int recibidos = 0;

    @Nullable
    private static DatosMapa datos = null;

    private static int revisionCache = -1;

    private static boolean cargando = false;
    private static boolean pintando = false;
    private static boolean fallo = false;
    private static boolean enVivo = false;

    private static int generacion = 0;

    private static final List<MapaCambiosPacket> PENDIENTES = new ArrayList<>();
    private static final Set<Integer> REPINTAR = new HashSet<>();

    private static final List<ResourceLocation> NIVELES = new ArrayList<>();
    private static final List<DynamicTexture> TEXTURAS = new ArrayList<>();
    private static final List<Integer> LADOS = new ArrayList<>();

    private static ColoresMapa colores = new ColoresMapa();

    private static List<MarcadorMapa> marcadores = new ArrayList<>();

    @Nullable
    public static MapaInfo info() {
        return info;
    }

    @Nullable
    public static DatosMapa datos() {
        return datos;
    }

    public static ColoresMapa colores() {
        return colores;
    }

    public static List<MarcadorMapa> marcadores() {
        return marcadores;
    }

    public static boolean listo() {
        return datos != null && !NIVELES.isEmpty();
    }

    public static boolean hayMapa() {
        return info != null && !fallo;
    }

    public static boolean descargando() {
        return descarga != null;
    }

    public static float progreso() {
        if (descarga == null || descarga.length == 0) return 0.0F;
        return recibidos / (float) descarga.length;
    }

    public static int niveles() {
        return NIVELES.size();
    }

    public static ResourceLocation textura(int nivel) {
        return NIVELES.get(Math.max(0, Math.min(nivel, NIVELES.size() - 1)));
    }

    public static int ladoNivel(int nivel) {
        return LADOS.get(Math.max(0, Math.min(nivel, LADOS.size() - 1)));
    }

    public static void recibirMarcadores(List<MarcadorMapa> nuevos) {
        marcadores = new ArrayList<>(nuevos);
    }

    public static void recibirInfo(@Nullable MapaInfo nueva) {
        if (nueva == null) {
            liberar();
            info = null;
            return;
        }

        if (datos != null && datos.id.equals(nueva.id()) && datos.revision >= nueva.revision()) {
            info = nueva;
            return;
        }

        if (info != null && info.id().equals(nueva.id()) && info.revision() == nueva.revision()
                && (descarga != null || cargando || pintando)) {
            return;
        }

        List<MapaCambiosPacket> guardados = new ArrayList<>();
        for (MapaCambiosPacket p : PENDIENTES) {
            if (p.id.equals(nueva.id())) guardados.add(p);
        }

        liberar();
        info = nueva;
        PENDIENTES.addAll(guardados);

        byte[] guardado = leerCache(nueva);
        if (guardado != null) {
            cargar(guardado, true);
            return;
        }

        pedir(nueva);
    }

    private static void pedir(MapaInfo i) {
        descarga = new byte[i.tamanio()];
        recibidos = 0;
        NetworkHandler.sendToServer(new MapaPedirPacket(i.id()));
    }

    public static void recibirTrozo(String id, int desde, byte[] trozo) {
        if (info == null || descarga == null) return;
        if (!info.id().equals(id)) return;
        if (desde < 0 || desde + trozo.length > descarga.length) return;

        System.arraycopy(trozo, 0, descarga, desde, trozo.length);
        recibidos = Math.max(recibidos, desde + trozo.length);

        if (recibidos < descarga.length) return;

        byte[] completo = descarga;
        descarga = null;

        if (DatosMapa.crc(completo) != info.crc()) {
            LOGGER.warn("[PezntZ] El mapa llego dañado, se vuelve a pedir");
            pedir(info);
            return;
        }

        escribirCache(info.id(), info.revision(), completo);
        revisionCache = info.revision();

        cargar(completo, false);
    }

    private static void cargar(byte[] bytes, boolean desdeCache) {
        int gen = ++generacion;
        cargando = true;
        fallo = false;

        Minecraft mc = Minecraft.getInstance();

        CompletableFuture.supplyAsync(() -> {
            try {
                return DatosMapa.deBytes(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, Util.backgroundExecutor()).whenComplete((leido, error) -> mc.execute(() -> {
            if (gen != generacion) return;

            boolean valido = error == null && leido != null && info != null
                    && leido.id.equals(info.id()) && (!desdeCache || leido.revision == info.revision());

            if (!valido) {
                cargando = false;

                if (desdeCache && info != null) {
                    borrarCache(info.id());
                    pedir(info);
                } else {
                    fallo = true;
                    LOGGER.warn("[PezntZ] No se pudo abrir el mapa: {}", error == null ? "datos distintos" : error.toString());
                }
                return;
            }

            datos = leido;
            revisionCache = leido.revision;

            for (MapaCambiosPacket p : PENDIENTES) aplicar(p);
            PENDIENTES.clear();

            pintarTodo(gen);
        }));
    }

    private static void pintarTodo(int gen) {
        DatosMapa d = datos;
        if (d == null) return;

        pintando = true;
        REPINTAR.clear();

        int[][] tabla = colores.prepararTodo(d);
        int maximo = Math.max(LADO_MINIMO_NIVEL, RenderSystem.maxSupportedTextureSize());

        Minecraft mc = Minecraft.getInstance();

        CompletableFuture.supplyAsync(() -> crearNiveles(d, tabla, maximo), Util.backgroundExecutor())
                .whenComplete((imagenes, error) -> mc.execute(() -> {
                    if (gen != generacion) {
                        if (imagenes != null) for (NativeImage img : imagenes) img.close();
                        return;
                    }

                    pintando = false;
                    cargando = false;

                    if (error != null || imagenes == null || imagenes.isEmpty()) {
                        fallo = true;
                        LOGGER.warn("[PezntZ] No se pudo pintar el mapa: {}", String.valueOf(error));
                        return;
                    }

                    for (int n = 0; n < imagenes.size(); n++) {
                        NativeImage img = imagenes.get(n);
                        ResourceLocation id = new ResourceLocation(PezntZMod.MOD_ID, "mapa/nivel_" + n);

                        DynamicTexture textura = new DynamicTexture(img);
                        mc.getTextureManager().register(id, textura);

                        textura.bind();
                        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
                        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

                        NIVELES.add(id);
                        TEXTURAS.add(textura);
                        LADOS.add(img.getWidth());
                    }

                    enVivo = LADOS.get(0) == d.lado;

                    if (!REPINTAR.isEmpty()) {
                        repintar(new ArrayList<>(REPINTAR));
                        REPINTAR.clear();
                    }
                }));
    }

    private static List<NativeImage> crearNiveles(DatosMapa d, int[][] tabla, int maximo) {
        int[] pixeles = ColoresMapa.pintar(d, tabla);

        NativeImage base = new NativeImage(d.lado, d.lado, false);
        for (int j = 0; j < d.lado; j++) {
            for (int i = 0; i < d.lado; i++) {
                base.setPixelRGBA(i, j, pixeles[j * d.lado + i]);
            }
        }

        while (base.getWidth() > maximo) {
            NativeImage mitad = reducir(base);
            base.close();
            base = mitad;
        }

        List<NativeImage> lista = new ArrayList<>();
        lista.add(base);

        NativeImage actual = base;
        while (actual.getWidth() / 2 >= LADO_MINIMO_NIVEL) {
            actual = reducir(actual);
            lista.add(actual);
        }

        return lista;
    }

    private static NativeImage reducir(NativeImage origen) {
        int ancho = Math.max(1, origen.getWidth() / 2);
        int alto = Math.max(1, origen.getHeight() / 2);

        NativeImage destino = new NativeImage(ancho, alto, false);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                destino.setPixelRGBA(x, y, promedio4(origen, x, y));
            }
        }

        return destino;
    }

    private static int promedio4(NativeImage origen, int x, int y) {
        int x0 = Math.min(x * 2, origen.getWidth() - 1);
        int y0 = Math.min(y * 2, origen.getHeight() - 1);
        int x1 = Math.min(x0 + 1, origen.getWidth() - 1);
        int y1 = Math.min(y0 + 1, origen.getHeight() - 1);

        int a = origen.getPixelRGBA(x0, y0);
        int b = origen.getPixelRGBA(x1, y0);
        int c = origen.getPixelRGBA(x0, y1);
        int d = origen.getPixelRGBA(x1, y1);

        int salida = 0;
        for (int desplazamiento = 0; desplazamiento < 32; desplazamiento += 8) {
            int suma = ((a >>> desplazamiento) & 0xFF) + ((b >>> desplazamiento) & 0xFF)
                    + ((c >>> desplazamiento) & 0xFF) + ((d >>> desplazamiento) & 0xFF);
            salida |= ((suma + 2) / 4) << desplazamiento;
        }
        return salida;
    }

    public static void recibirCambios(MapaCambiosPacket p) {
        if (info == null || !info.id().equals(p.id)) return;

        if (datos == null) {
            PENDIENTES.add(p);
            return;
        }

        aplicar(p);
    }

    private static void aplicar(MapaCambiosPacket p) {
        DatosMapa d = datos;
        if (d == null || !d.id.equals(p.id)) return;

        for (int k = 0; k < p.indicesEstado.length; k++) {
            BlockState estado = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), p.estadosNuevos[k]);
            d.ponerEstado(p.indicesEstado[k], estado);
            colores.olvidarEstado(p.indicesEstado[k]);
        }

        for (int k = 0; k < p.indicesBioma.length; k++) {
            d.ponerBioma(p.indicesBioma[k], p.biomasNuevos[k]);
            colores.olvidarBioma(p.indicesBioma[k]);
        }

        if (p.revision <= d.revision) return;

        List<Integer> cambiadas = new ArrayList<>(p.celdas.length);

        for (int k = 0; k < p.celdas.length; k++) {
            int c = p.celdas[k];
            if (c < 0 || c >= d.estado.length) continue;

            d.estado[c] = p.estado[k];
            d.alto[c] = p.alto[k];
            d.agua[c] = p.agua[k];
            d.bioma[c] = p.bioma[k];
            cambiadas.add(c);
        }

        d.revision = p.revision;

        if (pintando) {
            REPINTAR.addAll(cambiadas);
        } else if (!TEXTURAS.isEmpty() && enVivo) {
            repintar(cambiadas);
        }
    }

    private static void repintar(Collection<Integer> cambiadas) {
        DatosMapa d = datos;
        if (d == null || TEXTURAS.isEmpty() || cambiadas.isEmpty()) return;

        NativeImage imagen = TEXTURAS.get(0).getPixels();
        if (imagen == null) return;

        int lado = d.lado;

        Set<Integer> tocadas = new HashSet<>();
        for (int c : cambiadas) {
            int i = c % lado;
            int j = c / lado;

            tocadas.add(c);
            if (i > 0) tocadas.add(c - 1);
            if (i < lado - 1) tocadas.add(c + 1);
            if (j > 0) tocadas.add(c - lado);
            if (j < lado - 1) tocadas.add(c + lado);
        }

        int minI = Integer.MAX_VALUE;
        int minJ = Integer.MAX_VALUE;
        int maxI = Integer.MIN_VALUE;
        int maxJ = Integer.MIN_VALUE;

        int sinDatos = ColoresMapa.pixel(ColoresMapa.COLOR_SIN_DATOS, 1.0F);

        for (int c : tocadas) {
            int i = c % lado;
            int j = c / lado;
            int e = d.estado[c];

            int pixel = e < 0
                    ? sinDatos
                    : ColoresMapa.pixel(colores.base(d, e, d.bioma[c]), ColoresMapa.brillo(d, i, j));

            imagen.setPixelRGBA(i, j, pixel);

            minI = Math.min(minI, i);
            minJ = Math.min(minJ, j);
            maxI = Math.max(maxI, i);
            maxJ = Math.max(maxJ, j);
        }

        subir(TEXTURAS.get(0), imagen, minI, minJ, maxI, maxJ);

        for (int n = 1; n < TEXTURAS.size(); n++) {
            NativeImage anterior = TEXTURAS.get(n - 1).getPixels();
            NativeImage actual = TEXTURAS.get(n).getPixels();
            if (anterior == null || actual == null) break;

            minI = Math.max(0, minI >> 1);
            minJ = Math.max(0, minJ >> 1);
            maxI = Math.min(actual.getWidth() - 1, maxI >> 1);
            maxJ = Math.min(actual.getHeight() - 1, maxJ >> 1);

            for (int y = minJ; y <= maxJ; y++) {
                for (int x = minI; x <= maxI; x++) {
                    actual.setPixelRGBA(x, y, promedio4(anterior, x, y));
                }
            }

            subir(TEXTURAS.get(n), actual, minI, minJ, maxI, maxJ);
        }
    }

    private static void subir(DynamicTexture textura, NativeImage imagen, int minI, int minJ, int maxI, int maxJ) {
        if (maxI < minI || maxJ < minJ) return;

        textura.bind();
        imagen.upload(0, minI, minJ, minI, minJ, maxI - minI + 1, maxJ - minJ + 1, false, true, false, false);
    }

    private static void liberarTexturas() {
        Minecraft mc = Minecraft.getInstance();

        for (ResourceLocation id : NIVELES) {
            mc.getTextureManager().release(id);
        }

        NIVELES.clear();
        TEXTURAS.clear();
        LADOS.clear();
    }

    public static void liberar() {
        generacion++;

        liberarTexturas();

        datos = null;
        descarga = null;
        recibidos = 0;
        revisionCache = -1;
        cargando = false;
        pintando = false;
        fallo = false;
        enVivo = false;

        PENDIENTES.clear();
        REPINTAR.clear();

        colores = new ColoresMapa();
    }

    @SubscribeEvent
    public static void alDesconectar(ClientPlayerNetworkEvent.LoggingOut evento) {
        guardarAlSalir();

        liberar();
        info = null;
        marcadores = new ArrayList<>();
    }

    private static void guardarAlSalir() {
        if (!GUARDAR_EN_CACHE) return;

        DatosMapa d = datos;
        if (d == null || d.revision == revisionCache) return;

        DatosMapa copia = d.copia();

        CompletableFuture.runAsync(() -> {
            try {
                escribirCache(copia.id, copia.revision, copia.aBytes());
            } catch (IOException e) {
                LOGGER.warn("[PezntZ] No se pudo guardar el mapa en cache: {}", e.toString());
            }
        }, Util.backgroundExecutor());
    }

    private static Path carpetaCache() {
        return Minecraft.getInstance().gameDirectory.toPath().resolve("pezntz").resolve("mapas");
    }

    private static String limpio(String id) {
        return id.replaceAll("[^a-zA-Z0-9-]", "_");
    }

    private static Path archivoCache(String id, int revision) {
        return carpetaCache().resolve(limpio(id) + "_" + revision + ".dat");
    }

    @Nullable
    private static byte[] leerCache(MapaInfo i) {
        if (!GUARDAR_EN_CACHE) return null;

        try {
            Path archivo = archivoCache(i.id(), i.revision());
            if (!Files.isRegularFile(archivo)) return null;
            return Files.readAllBytes(archivo);
        } catch (Exception e) {
            return null;
        }
    }

    private static void borrarCache(String id) {
        Path carpeta = carpetaCache();
        if (!Files.isDirectory(carpeta)) return;

        try (DirectoryStream<Path> archivos = Files.newDirectoryStream(carpeta, limpio(id) + "_*.dat")) {
            for (Path p : archivos) Files.deleteIfExists(p);
        } catch (IOException ignorado) {
        }
    }

    private static void escribirCache(String id, int revision, byte[] bytes) {
        if (!GUARDAR_EN_CACHE) return;

        try {
            borrarCache(id);

            Path archivo = archivoCache(id, revision);
            Files.createDirectories(archivo.getParent());
            Files.write(archivo, bytes);
        } catch (Exception e) {
            LOGGER.warn("[PezntZ] No se pudo guardar el mapa en cache: {}", e.toString());
        }
    }
}