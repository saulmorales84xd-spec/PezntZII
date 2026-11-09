package net.saullmc.pezntz.mapa;

import net.minecraft.network.FriendlyByteBuf;

public record MapaInfo(String id, int revision, int lado, int x0, int z0, int bloquesPorPixel,
                       String dimension, int tamanio, long crc) {

    public int bloquesLado() {
        return this.lado * this.bloquesPorPixel;
    }

    public void escribir(FriendlyByteBuf buf) {
        buf.writeUtf(this.id);
        buf.writeVarInt(this.revision);
        buf.writeVarInt(this.lado);
        buf.writeInt(this.x0);
        buf.writeInt(this.z0);
        buf.writeVarInt(this.bloquesPorPixel);
        buf.writeUtf(this.dimension);
        buf.writeVarInt(this.tamanio);
        buf.writeLong(this.crc);
    }

    public static MapaInfo leer(FriendlyByteBuf buf) {
        return new MapaInfo(
                buf.readUtf(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readInt(),
                buf.readInt(),
                buf.readVarInt(),
                buf.readUtf(),
                buf.readVarInt(),
                buf.readLong());
    }
}