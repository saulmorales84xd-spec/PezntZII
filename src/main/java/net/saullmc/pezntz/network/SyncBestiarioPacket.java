package net.saullmc.pezntz.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.saullmc.pezntz.capability.BestiarioData;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class SyncBestiarioPacket {

    private static final int MAX_LARGO = 128;

    private final Set<String> vistos;

    public SyncBestiarioPacket(BestiarioData datos) {
        this.vistos = new HashSet<>(datos.getVistos());
    }

    public SyncBestiarioPacket(FriendlyByteBuf buf) {
        int cuantos = buf.readVarInt();
        this.vistos = new HashSet<>();

        for (int i = 0; i < cuantos; i++) {
            this.vistos.add(buf.readUtf(MAX_LARGO));
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(this.vistos.size());
        for (String bicho : this.vistos) {
            buf.writeUtf(bicho, MAX_LARGO);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context contexto = supplier.get();

        contexto.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        net.saullmc.pezntz.client.BestiarioCliente.poner(this.vistos))
        );

        contexto.setPacketHandled(true);
        return true;
    }
}