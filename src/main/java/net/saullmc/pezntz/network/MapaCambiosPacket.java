package net.saullmc.pezntz.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MapaCambiosPacket {

    public final String id;
    public final int revision;

    public final int[] indicesEstado;
    public final CompoundTag[] estadosNuevos;

    public final int[] indicesBioma;
    public final String[] biomasNuevos;

    public final int[] celdas;
    public final short[] estado;
    public final short[] alto;
    public final byte[] agua;
    public final short[] bioma;

    public MapaCambiosPacket(String id, int revision,
                             int[] indicesEstado, CompoundTag[] estadosNuevos,
                             int[] indicesBioma, String[] biomasNuevos,
                             int[] celdas, short[] estado, short[] alto, byte[] agua, short[] bioma) {
        this.id = id;
        this.revision = revision;
        this.indicesEstado = indicesEstado;
        this.estadosNuevos = estadosNuevos;
        this.indicesBioma = indicesBioma;
        this.biomasNuevos = biomasNuevos;
        this.celdas = celdas;
        this.estado = estado;
        this.alto = alto;
        this.agua = agua;
        this.bioma = bioma;
    }

    public MapaCambiosPacket(FriendlyByteBuf buf) {
        this.id = buf.readUtf();
        this.revision = buf.readVarInt();

        int estados = buf.readVarInt();
        this.indicesEstado = new int[estados];
        this.estadosNuevos = new CompoundTag[estados];
        for (int i = 0; i < estados; i++) {
            this.indicesEstado[i] = buf.readVarInt();
            CompoundTag tag = buf.readNbt();
            this.estadosNuevos[i] = tag == null ? new CompoundTag() : tag;
        }

        int biomas = buf.readVarInt();
        this.indicesBioma = new int[biomas];
        this.biomasNuevos = new String[biomas];
        for (int i = 0; i < biomas; i++) {
            this.indicesBioma[i] = buf.readVarInt();
            this.biomasNuevos[i] = buf.readUtf();
        }

        int n = buf.readVarInt();
        this.celdas = new int[n];
        this.estado = new short[n];
        this.alto = new short[n];
        this.agua = new byte[n];
        this.bioma = new short[n];

        for (int i = 0; i < n; i++) {
            this.celdas[i] = buf.readVarInt();
            this.estado[i] = buf.readShort();
            this.alto[i] = buf.readShort();
            this.agua[i] = buf.readByte();
            this.bioma[i] = buf.readShort();
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.id);
        buf.writeVarInt(this.revision);

        buf.writeVarInt(this.indicesEstado.length);
        for (int i = 0; i < this.indicesEstado.length; i++) {
            buf.writeVarInt(this.indicesEstado[i]);
            buf.writeNbt(this.estadosNuevos[i]);
        }

        buf.writeVarInt(this.indicesBioma.length);
        for (int i = 0; i < this.indicesBioma.length; i++) {
            buf.writeVarInt(this.indicesBioma[i]);
            buf.writeUtf(this.biomasNuevos[i]);
        }

        buf.writeVarInt(this.celdas.length);
        for (int i = 0; i < this.celdas.length; i++) {
            buf.writeVarInt(this.celdas[i]);
            buf.writeShort(this.estado[i]);
            buf.writeShort(this.alto[i]);
            buf.writeByte(this.agua[i]);
            buf.writeShort(this.bioma[i]);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context contexto = supplier.get();

        contexto.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        net.saullmc.pezntz.client.map.MapaCliente.recibirCambios(this))
        );

        contexto.setPacketHandled(true);
        return true;
    }
}