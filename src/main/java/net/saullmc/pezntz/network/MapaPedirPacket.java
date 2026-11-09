package net.saullmc.pezntz.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.saullmc.pezntz.mapa.MapaServidor;

import java.util.function.Supplier;

public class MapaPedirPacket {

    private final String id;

    public MapaPedirPacket(String id) {
        this.id = id;
    }

    public MapaPedirPacket(FriendlyByteBuf buf) {
        this.id = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.id);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context contexto = supplier.get();

        contexto.enqueueWork(() -> {
            ServerPlayer jugador = contexto.getSender();
            if (jugador != null) MapaServidor.pedir(jugador, this.id);
        });

        contexto.setPacketHandled(true);
        return true;
    }
}