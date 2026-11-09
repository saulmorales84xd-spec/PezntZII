package net.saullmc.pezntz.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MapaTrozoPacket {

    private static final int MAXIMO = 1 << 20;

    private final String id;
    private final int desde;
    private final byte[] datos;

    public MapaTrozoPacket(String id, int desde, byte[] datos) {
        this.id = id;
        this.desde = desde;
        this.datos = datos;
    }

    public MapaTrozoPacket(FriendlyByteBuf buf) {
        this.id = buf.readUtf();
        this.desde = buf.readVarInt();
        this.datos = buf.readByteArray(MAXIMO);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.id);
        buf.writeVarInt(this.desde);
        buf.writeByteArray(this.datos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context contexto = supplier.get();

        contexto.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        net.saullmc.pezntz.client.map.MapaCliente.recibirTrozo(this.id, this.desde, this.datos))
        );

        contexto.setPacketHandled(true);
        return true;
    }
}