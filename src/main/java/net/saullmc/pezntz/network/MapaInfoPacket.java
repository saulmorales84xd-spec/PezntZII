package net.saullmc.pezntz.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.saullmc.pezntz.mapa.MapaInfo;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class MapaInfoPacket {

    @Nullable
    private final MapaInfo info;

    public MapaInfoPacket(@Nullable MapaInfo info) {
        this.info = info;
    }

    public MapaInfoPacket(FriendlyByteBuf buf) {
        this.info = buf.readBoolean() ? MapaInfo.leer(buf) : null;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(this.info != null);
        if (this.info != null) this.info.escribir(buf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context contexto = supplier.get();

        contexto.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        net.saullmc.pezntz.client.map.MapaCliente.recibirInfo(this.info))
        );

        contexto.setPacketHandled(true);
        return true;
    }
}