package net.saullmc.pezntz.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DropWaypointPacket {

    private final BlockPos pos;
    private final long tiempoApertura;
    private final boolean quitar;

    public DropWaypointPacket(BlockPos pos, long tiempoApertura, boolean quitar) {
        this.pos = pos;
        this.tiempoApertura = tiempoApertura;
        this.quitar = quitar;
    }

    public DropWaypointPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.tiempoApertura = buf.readLong();
        this.quitar = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeLong(this.tiempoApertura);
        buf.writeBoolean(this.quitar);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context contexto = supplier.get();

        contexto.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    if (this.quitar) {
                        net.saullmc.pezntz.client.DropCliente.quitar(this.pos);
                    } else {
                        net.saullmc.pezntz.client.DropCliente.poner(this.pos, this.tiempoApertura);
                    }
                })
        );

        contexto.setPacketHandled(true);
        return true;
    }
}
