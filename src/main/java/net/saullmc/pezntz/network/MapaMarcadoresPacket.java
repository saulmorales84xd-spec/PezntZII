package net.saullmc.pezntz.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.saullmc.pezntz.mapa.MarcadorMapa;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MapaMarcadoresPacket {

    private final List<MarcadorMapa> marcadores;

    public MapaMarcadoresPacket(List<MarcadorMapa> marcadores) {
        this.marcadores = new ArrayList<>(marcadores);
    }

    public MapaMarcadoresPacket(FriendlyByteBuf buf) {
        int n = buf.readVarInt();
        this.marcadores = new ArrayList<>(n);
        for (int i = 0; i < n; i++) this.marcadores.add(MarcadorMapa.leer(buf));
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(this.marcadores.size());
        for (MarcadorMapa m : this.marcadores) m.escribir(buf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context contexto = supplier.get();

        contexto.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        net.saullmc.pezntz.client.map.MapaCliente.recibirMarcadores(this.marcadores))
        );

        contexto.setPacketHandled(true);
        return true;
    }
}