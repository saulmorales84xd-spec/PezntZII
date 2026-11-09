package net.saullmc.pezntz.mapa;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class DatosMapa {

    private static final int MAGIA = 0x505A4D50;

    private static final int VERSION = 2;

    public static final int MAXIMO_PALETA = 32000;

    public static final short SIN_DATOS = -1;

    public final String id;
    public final int lado;
    public final int x0;
    public final int z0;
    public final int bpp;
    public final String dimension;

    public int revision = 0;

    public final List<BlockState> estados = new ArrayList<>();
    private final Map<BlockState, Integer> indiceEstados = new HashMap<>();

    public final List<String> biomas = new ArrayList<>();
    private final Map<String, Integer> indiceBiomas = new HashMap<>();

    public final short[] estado;
    public final short[] alto;
    public final byte[] agua;
    public final short[] bioma;

    public DatosMapa(String id, int lado, int x0, int z0, int bpp, String dimension) {
        this.id = id;
        this.lado = lado;
        this.x0 = x0;
        this.z0 = z0;
        this.bpp = bpp;
        this.dimension = dimension;

        int celdas = lado * lado;
        this.estado = new short[celdas];
        this.alto = new short[celdas];
        this.agua = new byte[celdas];
        this.bioma = new short[celdas];

        Arrays.fill(this.estado, SIN_DATOS);
    }

    public int bloquesLado() {
        return this.lado * this.bpp;
    }

    public int indiceEstado(BlockState s) {
        Integer i = this.indiceEstados.get(s);
        if (i != null) return i;

        if (this.estados.size() >= MAXIMO_PALETA) return 0;

        int nuevo = this.estados.size();
        this.estados.add(s);
        this.indiceEstados.put(s, nuevo);
        return nuevo;
    }

    public int indiceBioma(String b) {
        Integer i = this.indiceBiomas.get(b);
        if (i != null) return i;

        if (this.biomas.size() >= MAXIMO_PALETA) return 0;

        int nuevo = this.biomas.size();
        this.biomas.add(b);
        this.indiceBiomas.put(b, nuevo);
        return nuevo;
    }

    public void ponerEstado(int indice, BlockState s) {
        if (indice < 0 || indice >= MAXIMO_PALETA) return;

        while (this.estados.size() <= indice) this.estados.add(Blocks.AIR.defaultBlockState());

        this.estados.set(indice, s);
        this.indiceEstados.putIfAbsent(s, indice);
    }

    public void ponerBioma(int indice, String b) {
        if (indice < 0 || indice >= MAXIMO_PALETA) return;

        while (this.biomas.size() <= indice) this.biomas.add("minecraft:plains");

        this.biomas.set(indice, b);
        this.indiceBiomas.putIfAbsent(b, indice);
    }

    public DatosMapa copia() {
        DatosMapa c = new DatosMapa(this.id, this.lado, this.x0, this.z0, this.bpp, this.dimension);
        c.revision = this.revision;

        for (BlockState s : this.estados) c.ponerEstado(c.estados.size(), s);
        for (String b : this.biomas) c.ponerBioma(c.biomas.size(), b);

        System.arraycopy(this.estado, 0, c.estado, 0, this.estado.length);
        System.arraycopy(this.alto, 0, c.alto, 0, this.alto.length);
        System.arraycopy(this.agua, 0, c.agua, 0, this.agua.length);
        System.arraycopy(this.bioma, 0, c.bioma, 0, this.bioma.length);
        return c;
    }

    public byte[] aBytes() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(Math.max(1 << 16, this.lado * this.lado / 2));

        try (DataOutputStream d = new DataOutputStream(new BufferedOutputStream(new DeflaterOutputStream(bytes), 1 << 16))) {
            d.writeInt(MAGIA);
            d.writeInt(VERSION);

            d.writeUTF(this.id);
            d.writeInt(this.revision);
            d.writeInt(this.lado);
            d.writeInt(this.x0);
            d.writeInt(this.z0);
            d.writeInt(this.bpp);
            d.writeUTF(this.dimension);

            d.writeInt(this.estados.size());
            for (BlockState s : this.estados) NbtIo.write(NbtUtils.writeBlockState(s), d);

            d.writeInt(this.biomas.size());
            for (String b : this.biomas) d.writeUTF(b);

            for (int j = 0; j < this.lado; j++) {
                int fila = j * this.lado;
                int anterior = 0;

                for (int i = 0; i < this.lado; i++) d.writeShort(this.estado[fila + i]);

                for (int i = 0; i < this.lado; i++) {
                    int h = this.alto[fila + i];
                    d.writeShort(h - anterior);
                    anterior = h;
                }

                d.write(this.agua, fila, this.lado);

                for (int i = 0; i < this.lado; i++) d.writeShort(this.bioma[fila + i]);
            }
        }

        return bytes.toByteArray();
    }

    public static DatosMapa deBytes(byte[] datos) throws IOException {
        return deBytes(datos, BuiltInRegistries.BLOCK.asLookup());
    }

    public static DatosMapa deBytes(byte[] datos, HolderGetter<Block> bloques) throws IOException {
        try (DataInputStream d = new DataInputStream(new BufferedInputStream(
                new InflaterInputStream(new ByteArrayInputStream(datos)), 1 << 16))) {

            if (d.readInt() != MAGIA) throw new IOException("No es un mapa de PezntZ");
            if (d.readInt() != VERSION) throw new IOException("Version de mapa distinta");

            String id = d.readUTF();
            int revision = d.readInt();
            int lado = d.readInt();
            int x0 = d.readInt();
            int z0 = d.readInt();
            int bpp = d.readInt();
            String dimension = d.readUTF();

            if (lado <= 0 || lado > 8192 || bpp <= 0) throw new IOException("Mapa dañado");

            DatosMapa mapa = new DatosMapa(id, lado, x0, z0, bpp, dimension);
            mapa.revision = revision;

            int estados = d.readInt();
            for (int i = 0; i < estados; i++) {
                CompoundTag tag = NbtIo.read(d);
                mapa.ponerEstado(i, NbtUtils.readBlockState(bloques, tag));
            }

            int biomas = d.readInt();
            for (int i = 0; i < biomas; i++) mapa.ponerBioma(i, d.readUTF());

            for (int j = 0; j < lado; j++) {
                int fila = j * lado;
                int anterior = 0;

                for (int i = 0; i < lado; i++) mapa.estado[fila + i] = d.readShort();

                for (int i = 0; i < lado; i++) {
                    anterior += d.readShort();
                    mapa.alto[fila + i] = (short) anterior;
                }

                d.readFully(mapa.agua, fila, lado);

                for (int i = 0; i < lado; i++) mapa.bioma[fila + i] = d.readShort();
            }

            return mapa;
        }
    }

    public static long crc(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return crc.getValue();
    }
}