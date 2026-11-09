package net.saullmc.pezntz.mapa;

import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.network.MapaCambiosPacket;
import net.saullmc.pezntz.network.MapaInfoPacket;
import net.saullmc.pezntz.network.MapaMarcadoresPacket;
import net.saullmc.pezntz.network.MapaTrozoPacket;
import net.saullmc.pezntz.network.NetworkHandler;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class MapaServidor {

    public static final String CARPETA = "pezntz";
    public static final String ARCHIVO_MAPA = "mapa.dat";
    public static final String ARCHIVO_MARCADORES = "marcadores.txt";

    public static final int TAMANIO_TROZO = 30000;

    public static final int TROZOS_POR_TICK = 4;

    public static final boolean ACTUALIZAR_EN_VIVO = true;

    public static final int TICKS_ACTUALIZAR = 40;

    public static final int CHUNKS_POR_ACTUALIZACION = 64;

    public static final int CELDAS_POR_PAQUETE = 4000;

    public static final int TICKS_GUARDADO = 6000;

    private static final Logger LOGGER = LogUtils.getLogger();

    @Nullable
    private static DatosMapa datos = null;

    @Nullable
    private static MapaInfo info = null;

    @Nullable
    private static byte[] blob = null;

    private static int blobRevision = -1;

    private static boolean serializando = false;
    private static boolean serializarOtraVez = false;

    @Nullable
    private static UUID quienGenero = null;

    private static int paletaEnviada = 0;
    private static int biomasEnviados = 0;

    private static long contador = 0L;

    private static final Map<UUID, Integer> ENVIOS = new HashMap<>();
    private static final Set<UUID> PENDIENTES = new HashSet<>();
    private static final Set<Long> SUCIOS = new LinkedHashSet<>();

    private static final List<MarcadorMapa> MARCADORES = new ArrayList<>();

    @Nullable
    public static MapaInfo info() {
        return info;
    }

    @Nullable
    public static DatosMapa datos() {
        return datos;
    }

    public static boolean guardando() {
        return serializando;
    }

    private static Path carpeta(MinecraftServer server) {
        return server.getWorldPath(LevelResource.ROOT).resolve(CARPETA);
    }

    private static void reiniciar() {
        datos = null;
        info = null;
        blob = null;
        blobRevision = -1;
        serializando = false;
        serializarOtraVez = false;
        quienGenero = null;
        paletaEnviada = 0;
        biomasEnviados = 0;
        ENVIOS.clear();
        PENDIENTES.clear();
        SUCIOS.clear();
        MARCADORES.clear();
    }

    @SubscribeEvent
    public static void alArrancar(ServerStartedEvent evento) {
        reiniciar();

        MinecraftServer server = evento.getServer();
        Path archivo = carpeta(server).resolve(ARCHIVO_MAPA);

        cargarMarcadores(server);

        if (!Files.isRegularFile(archivo)) return;

        try {
            byte[] bytes = Files.readAllBytes(archivo);
            DatosMapa leido = DatosMapa.deBytes(bytes);

            datos = leido;
            blob = bytes;
            blobRevision = leido.revision;
            info = crearInfo(leido, bytes);

            paletaEnviada = leido.estados.size();
            biomasEnviados = leido.biomas.size();

            LOGGER.info("[PezntZ] Mapa cargado: {}x{} pixeles, {} KB", leido.lado, leido.lado, bytes.length / 1024);

        } catch (Exception e) {
            reiniciar();
            cargarMarcadores(server);
            LOGGER.warn("[PezntZ] No se pudo leer el mapa guardado: {}", e.toString());
        }
    }

    @SubscribeEvent
    public static void alApagar(ServerStoppingEvent evento) {
        if (datos == null) return;
        if (blob != null && blobRevision == datos.revision) return;

        try {
            byte[] bytes = datos.aBytes();
            Path archivo = carpeta(evento.getServer()).resolve(ARCHIVO_MAPA);
            Files.createDirectories(archivo.getParent());
            Files.write(archivo, bytes);
        } catch (IOException e) {
            LOGGER.error("[PezntZ] No se pudo guardar el mapa al cerrar", e);
        }
    }

    @SubscribeEvent
    public static void alParar(ServerStoppedEvent evento) {
        reiniciar();
    }

    private static MapaInfo crearInfo(DatosMapa d, byte[] bytes) {
        return new MapaInfo(d.id, d.revision, d.lado, d.x0, d.z0, d.bpp, d.dimension, bytes.length, DatosMapa.crc(bytes));
    }

    private static boolean blobAlDia() {
        return datos != null && blob != null && info != null && blobRevision == datos.revision;
    }

    public static void nuevoMapa(MinecraftServer server, DatosMapa nuevos, @Nullable UUID quien) {
        datos = nuevos;
        info = null;
        blob = null;
        blobRevision = -1;
        quienGenero = quien;

        paletaEnviada = nuevos.estados.size();
        biomasEnviados = nuevos.biomas.size();

        ENVIOS.clear();
        PENDIENTES.clear();
        SUCIOS.clear();

        serializar(server);
    }

    private static void serializar(MinecraftServer server) {
        if (datos == null) return;

        if (serializando) {
            serializarOtraVez = true;
            return;
        }

        serializando = true;

        DatosMapa copia = datos.copia();
        Path archivo = carpeta(server).resolve(ARCHIVO_MAPA);

        CompletableFuture.supplyAsync(() -> {
            try {
                byte[] bytes = copia.aBytes();
                Files.createDirectories(archivo.getParent());
                Files.write(archivo, bytes);
                return bytes;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, Util.backgroundExecutor()).whenComplete((bytes, error) ->
                server.execute(() -> terminarSerializacion(server, copia, bytes, error)));
    }

    private static void terminarSerializacion(MinecraftServer server, DatosMapa copia,
                                              @Nullable byte[] bytes, @Nullable Throwable error) {
        serializando = false;

        if (datos != null && datos.id.equals(copia.id)) {
            if (error != null || bytes == null) {
                LOGGER.error("[PezntZ] No se pudo guardar el mapa", error);
                GeneradorMapa.avisar(server.overworld(), quienGenero, "No se pudo guardar el mapa");
            } else {
                boolean nuevo = info == null || !info.id().equals(copia.id);

                blob = bytes;
                blobRevision = copia.revision;
                info = crearInfo(copia, bytes);

                if (nuevo) {
                    ENVIOS.clear();
                    PENDIENTES.clear();
                    NetworkHandler.sendToAll(new MapaInfoPacket(info));

                    GeneradorMapa.avisar(server.overworld(), quienGenero, "Mapa listo (" + copia.lado + "x" + copia.lado
                            + " pixeles, " + (bytes.length / 1024) + " KB). Pulsa M para verlo");
                    quienGenero = null;
                } else {
                    Set<UUID> avisar = new HashSet<>(PENDIENTES);
                    avisar.addAll(ENVIOS.keySet());

                    PENDIENTES.clear();
                    ENVIOS.clear();

                    for (UUID uuid : avisar) {
                        ServerPlayer jugador = server.getPlayerList().getPlayer(uuid);
                        if (jugador != null) NetworkHandler.sendToPlayer(new MapaInfoPacket(info), jugador);
                    }
                }
            }
        }

        if (serializarOtraVez) {
            serializarOtraVez = false;
            serializar(server);
        }
    }

    public static boolean borrar(MinecraftServer server) {
        boolean habia = datos != null;

        datos = null;
        info = null;
        blob = null;
        blobRevision = -1;
        ENVIOS.clear();
        PENDIENTES.clear();
        SUCIOS.clear();

        try {
            Files.deleteIfExists(carpeta(server).resolve(ARCHIVO_MAPA));
        } catch (IOException e) {
            LOGGER.warn("[PezntZ] No se pudo borrar el mapa: {}", e.toString());
        }

        NetworkHandler.sendToAll(new MapaInfoPacket((MapaInfo) null));
        return habia;
    }

    public static void reenviar(MinecraftServer server) {
        if (datos == null) {
            NetworkHandler.sendToAll(new MapaInfoPacket((MapaInfo) null));
            return;
        }

        if (blobAlDia()) {
            NetworkHandler.sendToAll(new MapaInfoPacket(info));
            return;
        }

        for (ServerPlayer jugador : server.getPlayerList().getPlayers()) PENDIENTES.add(jugador.getUUID());
        serializar(server);
    }

    public static void pedir(ServerPlayer jugador, String id) {
        if (info == null || blob == null) return;
        if (!info.id().equals(id)) return;

        ENVIOS.put(jugador.getUUID(), 0);
    }

    @SubscribeEvent
    public static void alEntrar(PlayerEvent.PlayerLoggedInEvent evento) {
        if (!(evento.getEntity() instanceof ServerPlayer jugador)) return;

        NetworkHandler.sendToPlayer(new MapaMarcadoresPacket(MARCADORES), jugador);

        if (datos == null) {
            NetworkHandler.sendToPlayer(new MapaInfoPacket((MapaInfo) null), jugador);
            return;
        }

        if (blobAlDia()) {
            NetworkHandler.sendToPlayer(new MapaInfoPacket(info), jugador);
            return;
        }

        PENDIENTES.add(jugador.getUUID());

        MinecraftServer server = jugador.getServer();
        if (server != null) serializar(server);
    }

    @SubscribeEvent
    public static void alSalir(PlayerEvent.PlayerLoggedOutEvent evento) {
        UUID uuid = evento.getEntity().getUUID();
        ENVIOS.remove(uuid);
        PENDIENTES.remove(uuid);
    }

    @SubscribeEvent
    public static void alCambiarBloque(BlockEvent.NeighborNotifyEvent evento) {
        if (!ACTUALIZAR_EN_VIVO || datos == null) return;
        if (!(evento.getLevel() instanceof Level nivel) || nivel.isClientSide()) return;
        if (!nivel.dimension().location().toString().equals(datos.dimension)) return;

        BlockPos pos = evento.getPos();
        int x = pos.getX();
        int z = pos.getZ();

        if (x < datos.x0 || z < datos.z0) return;
        if (x >= datos.x0 + datos.bloquesLado() || z >= datos.z0 + datos.bloquesLado()) return;

        SUCIOS.add(ChunkPos.asLong(x >> 4, z >> 4));
    }

    @SubscribeEvent
    public static void alTick(TickEvent.ServerTickEvent evento) {
        if (evento.phase != TickEvent.Phase.END) return;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        contador++;

        if (contador % TICKS_ACTUALIZAR == 0L) procesarCambios(server);

        if (contador % TICKS_GUARDADO == 0L && datos != null && blobRevision != datos.revision) {
            serializar(server);
        }

        enviarTrozos(server);
    }

    private static void enviarTrozos(MinecraftServer server) {
        if (ENVIOS.isEmpty()) return;

        if (info == null || blob == null) {
            ENVIOS.clear();
            return;
        }

        Iterator<Map.Entry<UUID, Integer>> it = ENVIOS.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<UUID, Integer> envio = it.next();

            ServerPlayer jugador = server.getPlayerList().getPlayer(envio.getKey());
            if (jugador == null) {
                it.remove();
                continue;
            }

            int desde = envio.getValue();

            for (int n = 0; n < TROZOS_POR_TICK && desde < blob.length; n++) {
                int largo = Math.min(TAMANIO_TROZO, blob.length - desde);

                byte[] trozo = new byte[largo];
                System.arraycopy(blob, desde, trozo, 0, largo);

                NetworkHandler.sendToPlayer(new MapaTrozoPacket(info.id(), desde, trozo), jugador);
                desde += largo;
            }

            if (desde >= blob.length) {
                it.remove();
            } else {
                envio.setValue(desde);
            }
        }
    }

    private static void procesarCambios(MinecraftServer server) {
        if (datos == null || SUCIOS.isEmpty()) return;

        ServerLevel nivel = server.getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(datos.dimension)));
        if (nivel == null) {
            SUCIOS.clear();
            return;
        }

        List<Integer> cambiadas = new ArrayList<>();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        Iterator<Long> it = SUCIOS.iterator();
        int procesados = 0;

        while (it.hasNext() && procesados < CHUNKS_POR_ACTUALIZACION) {
            long clave = it.next();
            it.remove();
            procesados++;

            int cx = ChunkPos.getX(clave);
            int cz = ChunkPos.getZ(clave);

            int iDesde = GeneradorMapa.primeraCelda(datos.x0, datos.bpp, datos.lado, cx << 4);
            int iHasta = GeneradorMapa.primeraCelda(datos.x0, datos.bpp, datos.lado, (cx << 4) + 16) - 1;
            int jDesde = GeneradorMapa.primeraCelda(datos.z0, datos.bpp, datos.lado, cz << 4);
            int jHasta = GeneradorMapa.primeraCelda(datos.z0, datos.bpp, datos.lado, (cz << 4) + 16) - 1;

            if (iDesde > iHasta || jDesde > jHasta) continue;

            LevelChunk chunk = nivel.getChunkSource().getChunkNow(cx, cz);
            if (chunk == null) continue;

            for (int j = jDesde; j <= jHasta; j++) {
                for (int i = iDesde; i <= iHasta; i++) {
                    if (GeneradorMapa.muestrear(nivel, chunk, datos, i, j, pos)) {
                        cambiadas.add(j * datos.lado + i);
                    }
                }
            }
        }

        if (!cambiadas.isEmpty()) enviarCambios(cambiadas);
    }

    private static void enviarCambios(List<Integer> cambiadas) {
        if (datos == null) return;

        int nuevosEstados = Math.max(0, datos.estados.size() - paletaEnviada);
        int[] indicesEstado = new int[nuevosEstados];
        CompoundTag[] estadosNuevos = new CompoundTag[nuevosEstados];

        for (int k = 0; k < nuevosEstados; k++) {
            indicesEstado[k] = paletaEnviada + k;
            estadosNuevos[k] = NbtUtils.writeBlockState(datos.estados.get(paletaEnviada + k));
        }
        paletaEnviada = datos.estados.size();

        int nuevosBiomas = Math.max(0, datos.biomas.size() - biomasEnviados);
        int[] indicesBioma = new int[nuevosBiomas];
        String[] biomasNuevos = new String[nuevosBiomas];

        for (int k = 0; k < nuevosBiomas; k++) {
            indicesBioma[k] = biomasEnviados + k;
            biomasNuevos[k] = datos.biomas.get(biomasEnviados + k);
        }
        biomasEnviados = datos.biomas.size();

        boolean primero = true;

        for (int desde = 0; desde < cambiadas.size(); desde += CELDAS_POR_PAQUETE) {
            int n = Math.min(CELDAS_POR_PAQUETE, cambiadas.size() - desde);

            int[] celdas = new int[n];
            short[] estado = new short[n];
            short[] alto = new short[n];
            byte[] agua = new byte[n];
            short[] bioma = new short[n];

            for (int k = 0; k < n; k++) {
                int c = cambiadas.get(desde + k);
                celdas[k] = c;
                estado[k] = datos.estado[c];
                alto[k] = datos.alto[c];
                agua[k] = datos.agua[c];
                bioma[k] = datos.bioma[c];
            }

            datos.revision++;

            NetworkHandler.sendToAll(new MapaCambiosPacket(datos.id, datos.revision,
                    primero ? indicesEstado : new int[0], primero ? estadosNuevos : new CompoundTag[0],
                    primero ? indicesBioma : new int[0], primero ? biomasNuevos : new String[0],
                    celdas, estado, alto, agua, bioma));

            primero = false;
        }
    }

    public static List<MarcadorMapa> marcadores() {
        return MARCADORES;
    }

    public static void ponerMarcador(MinecraftServer server, MarcadorMapa marcador) {
        MARCADORES.removeIf(m -> m.nombre().equalsIgnoreCase(marcador.nombre()));
        MARCADORES.add(marcador);
        guardarMarcadores(server);
        NetworkHandler.sendToAll(new MapaMarcadoresPacket(MARCADORES));
    }

    public static boolean quitarMarcador(MinecraftServer server, String nombre) {
        boolean quitado = MARCADORES.removeIf(m -> m.nombre().equalsIgnoreCase(nombre));
        if (!quitado) return false;

        guardarMarcadores(server);
        NetworkHandler.sendToAll(new MapaMarcadoresPacket(MARCADORES));
        return true;
    }

    private static void cargarMarcadores(MinecraftServer server) {
        MARCADORES.clear();

        Path archivo = carpeta(server).resolve(ARCHIVO_MARCADORES);
        if (!Files.isRegularFile(archivo)) return;

        try {
            for (String linea : Files.readAllLines(archivo, StandardCharsets.UTF_8)) {
                String[] partes = linea.split("\t", 4);
                if (partes.length < 4) continue;

                try {
                    MARCADORES.add(new MarcadorMapa(partes[3], Integer.parseInt(partes[0]),
                            Integer.parseInt(partes[1]), partes[2]));
                } catch (NumberFormatException ignorado) {
                }
            }
        } catch (IOException e) {
            LOGGER.warn("[PezntZ] No se pudieron leer los marcadores del mapa: {}", e.toString());
        }
    }

    private static void guardarMarcadores(MinecraftServer server) {
        List<String> lineas = new ArrayList<>();
        for (MarcadorMapa m : MARCADORES) {
            lineas.add(m.x() + "\t" + m.z() + "\t" + m.icono() + "\t" + m.nombre());
        }

        try {
            Path archivo = carpeta(server).resolve(ARCHIVO_MARCADORES);
            Files.createDirectories(archivo.getParent());
            Files.write(archivo, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.warn("[PezntZ] No se pudieron guardar los marcadores del mapa: {}", e.toString());
        }
    }
}