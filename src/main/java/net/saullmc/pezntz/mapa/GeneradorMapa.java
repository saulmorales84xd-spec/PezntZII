package net.saullmc.pezntz.mapa;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class GeneradorMapa {

    public static final int MS_POR_TICK = 20;

    public static final int LADO_MAXIMO = 4096;

    public static final int LADO_AUTOMATICO = 3072;

    public static final int AVISAR_CADA_PORCIENTO = 5;

    private static final Logger LOGGER = LogUtils.getLogger();

    @Nullable
    private static Trabajo trabajo = null;

    public static boolean ocupado() {
        return trabajo != null;
    }

    public static int porcentaje() {
        return trabajo == null ? 0 : trabajo.porcentaje();
    }

    public static int chunksTotales() {
        return trabajo == null ? 0 : trabajo.totalChunks();
    }

    public static int bloquesPorPixelAutomatico(int radio) {
        return Math.max(1, (int) Math.ceil(2.0D * radio / LADO_AUTOMATICO));
    }

    @Nullable
    public static String empezar(ServerLevel level, int centroX, int centroZ, int radio,
                                 int bloquesPorPixel, @Nullable UUID quien) {

        if (ocupado()) return "Ya se esta generando un mapa";
        if (radio < 16) return "El radio tiene que ser de 16 bloques o mas";
        if (bloquesPorPixel < 1) return "Los bloques por pixel tienen que ser 1 o mas";

        int lado = (int) Math.ceil(2.0D * radio / bloquesPorPixel);

        if (lado > LADO_MAXIMO) {
            int minimo = (int) Math.ceil(2.0D * radio / LADO_MAXIMO);
            return "El mapa saldria de " + lado + " pixeles por lado (maximo " + LADO_MAXIMO
                    + "). Usa " + minimo + " bloques por pixel o mas";
        }

        String id = Long.toHexString(System.currentTimeMillis());

        DatosMapa datos = new DatosMapa(id, lado, centroX - radio, centroZ - radio, bloquesPorPixel,
                level.dimension().location().toString());

        trabajo = new Trabajo(level, datos, quien);

        LOGGER.info("[PezntZ] Generando mapa: centro {} {}, radio {}, {} bloques por pixel, {}x{} pixeles, {} chunks",
                centroX, centroZ, radio, bloquesPorPixel, lado, lado, trabajo.totalChunks());
        return null;
    }

    public static boolean cancelar() {
        if (trabajo == null) return false;
        trabajo = null;
        return true;
    }

    @SubscribeEvent
    public static void alTick(TickEvent.ServerTickEvent evento) {
        if (evento.phase != TickEvent.Phase.END) return;
        if (trabajo == null) return;

        Trabajo actual = trabajo;
        actual.avanzar();

        if (actual.terminado()) {
            trabajo = null;

            avisar(actual, "Mapa leido, guardando...");
            MapaServidor.nuevoMapa(actual.level.getServer(), actual.datos, actual.quien);
        }
    }

    @SubscribeEvent
    public static void alParar(ServerStoppingEvent evento) {
        trabajo = null;
    }

    static void avisar(Trabajo t, String texto) {
        LOGGER.info("[PezntZ] {}", texto);
        avisar(t.level, t.quien, texto);
    }

    static void avisar(ServerLevel level, @Nullable UUID quien, String texto) {
        if (quien == null) return;

        ServerPlayer jugador = level.getServer().getPlayerList().getPlayer(quien);
        if (jugador != null) jugador.sendSystemMessage(Component.literal(texto));
    }

    public static int primeraCelda(int inicio, int bpp, int lado, int bloque) {
        int medio = bpp / 2;
        int celda = Math.floorDiv(bloque - inicio - medio + bpp - 1, bpp);
        return Math.max(0, Math.min(lado, celda));
    }

    public static boolean muestrear(Level level, ChunkAccess chunk, DatosMapa datos, int i, int j,
                                    BlockPos.MutableBlockPos pos) {

        int medio = datos.bpp / 2;
        int x = datos.x0 + i * datos.bpp + medio;
        int z = datos.z0 + j * datos.bpp + medio;
        int indice = j * datos.lado + i;

        int minimo = level.getMinBuildHeight();
        int maximo = level.getMaxBuildHeight() - 1;

        int y = Math.min(chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x & 15, z & 15) + 1, maximo);

        BlockState encontrado = null;

        while (y >= minimo) {
            pos.set(x, y, z);
            BlockState aqui = chunk.getBlockState(pos);

            if (!aqui.isAir() && aqui.getMapColor(level, pos) != MapColor.NONE) {
                encontrado = aqui;
                break;
            }
            y--;
        }

        short nuevoEstado = DatosMapa.SIN_DATOS;
        short nuevoAlto = (short) minimo;
        byte nuevaAgua = 0;
        short nuevoBioma = 0;

        if (encontrado != null) {
            boolean esAgua = encontrado.getFluidState().is(FluidTags.WATER)
                    && (encontrado.getBlock() instanceof LiquidBlock || encontrado.getMapColor(level, pos) == MapColor.WATER);

            if (esAgua) {
                int profundidad = 1;
                int abajo = y - 1;

                while (abajo >= minimo && profundidad < 127) {
                    pos.set(x, abajo, z);
                    if (!chunk.getBlockState(pos).getFluidState().is(FluidTags.WATER)) break;

                    profundidad++;
                    abajo--;
                }

                nuevaAgua = (byte) profundidad;
                encontrado = Blocks.WATER.defaultBlockState();
            }

            nuevoEstado = (short) datos.indiceEstado(encontrado);
            nuevoAlto = (short) y;

            String nombreBioma = chunk.getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z))
                    .unwrapKey().map(k -> k.location().toString()).orElse("minecraft:plains");

            nuevoBioma = (short) datos.indiceBioma(nombreBioma);
        }

        boolean cambio = datos.estado[indice] != nuevoEstado || datos.alto[indice] != nuevoAlto
                || datos.agua[indice] != nuevaAgua || datos.bioma[indice] != nuevoBioma;

        datos.estado[indice] = nuevoEstado;
        datos.alto[indice] = nuevoAlto;
        datos.agua[indice] = nuevaAgua;
        datos.bioma[indice] = nuevoBioma;

        return cambio;
    }

    private static final class Columna {
        final int chunk;
        final int desde;
        final int hasta;

        Columna(int chunk, int desde, int hasta) {
            this.chunk = chunk;
            this.desde = desde;
            this.hasta = hasta;
        }
    }

    static final class Trabajo {
        final ServerLevel level;
        final DatosMapa datos;
        @Nullable
        final UUID quien;

        final List<Columna> columnasX;
        final List<Columna> columnasZ;

        int indiceX = 0;
        int indiceZ = 0;
        int procesados = 0;
        int ultimoAviso = 0;

        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        Trabajo(ServerLevel level, DatosMapa datos, @Nullable UUID quien) {
            this.level = level;
            this.datos = datos;
            this.quien = quien;

            this.columnasX = columnas(datos.x0, datos.lado, datos.bpp);
            this.columnasZ = columnas(datos.z0, datos.lado, datos.bpp);
        }

        static List<Columna> columnas(int inicio, int lado, int bpp) {
            List<Columna> lista = new ArrayList<>();

            int medio = bpp / 2;
            int actual = Integer.MIN_VALUE;
            int desde = 0;

            for (int i = 0; i < lado; i++) {
                int chunk = (inicio + i * bpp + medio) >> 4;

                if (chunk != actual) {
                    if (actual != Integer.MIN_VALUE) lista.add(new Columna(actual, desde, i - 1));
                    actual = chunk;
                    desde = i;
                }
            }

            if (actual != Integer.MIN_VALUE) lista.add(new Columna(actual, desde, lado - 1));
            return lista;
        }

        int totalChunks() {
            return this.columnasX.size() * this.columnasZ.size();
        }

        int porcentaje() {
            int total = Math.max(1, totalChunks());
            return (int) (this.procesados * 100L / total);
        }

        boolean terminado() {
            return this.indiceZ >= this.columnasZ.size();
        }

        void avanzar() {
            long limite = System.nanoTime() + MS_POR_TICK * 1_000_000L;

            while (!terminado() && System.nanoTime() < limite) {
                procesar(this.columnasX.get(this.indiceX), this.columnasZ.get(this.indiceZ));

                this.procesados++;
                this.indiceX++;

                if (this.indiceX >= this.columnasX.size()) {
                    this.indiceX = 0;
                    this.indiceZ++;
                }
            }

            int porcentaje = porcentaje();
            if (AVISAR_CADA_PORCIENTO > 0 && porcentaje >= this.ultimoAviso + AVISAR_CADA_PORCIENTO && porcentaje < 100) {
                this.ultimoAviso = porcentaje - porcentaje % AVISAR_CADA_PORCIENTO;
                avisar(this, "Generando mapa... " + this.ultimoAviso + "%");
            }
        }

        void procesar(Columna cx, Columna cz) {
            ChunkAccess chunk;

            try {
                chunk = this.level.getChunk(cx.chunk, cz.chunk, ChunkStatus.FULL, true);
            } catch (Exception e) {
                return;
            }

            if (chunk == null) return;

            for (int j = cz.desde; j <= cz.hasta; j++) {
                for (int i = cx.desde; i <= cx.hasta; i++) {
                    muestrear(this.level, chunk, this.datos, i, j, this.pos);
                }
            }
        }
    }
}