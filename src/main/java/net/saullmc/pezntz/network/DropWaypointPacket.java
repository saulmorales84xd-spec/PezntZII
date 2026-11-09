package net.saullmc.pezntz.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DropWaypointPacket {

    private final int id;
    private final double x;
    private final double techo;
    private final double z;
    private final long tiempoApertura;
    private final boolean quitar;

    public DropWaypointPacket(int id, double x, double techo, double z, long tiempoApertura, boolean quitar) {
        this.id = id;
        this.x = x;
        this.techo = techo;
        this.z = z;
        this.tiempoApertura = tiempoApertura;
        this.quitar = quitar;
    }

    public DropWaypointPacket(BlockPos pos, long tiempoApertura, boolean quitar) {
        this(idDeBloque(pos), pos.getX() + 0.5D, pos.getY() + 2.0D, pos.getZ() + 0.5D, tiempoApertura, quitar);
    }

    public static int idDeBloque(BlockPos pos) {
        long valor = pos.asLong();
        return ((int) (valor ^ (valor >>> 32))) | Integer.MIN_VALUE;
    }

    public DropWaypointPacket(FriendlyByteBuf buf) {
        this.id = buf.readInt();
        this.x = buf.readDouble();
        this.techo = buf.readDouble();
        this.z = buf.readDouble();
        this.tiempoApertura = buf.readLong();
        this.quitar = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeDouble(this.x);
        buf.writeDouble(this.techo);
        buf.writeDouble(this.z);
        buf.writeLong(this.tiempoApertura);
        buf.writeBoolean(this.quitar);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context contexto = supplier.get();

        contexto.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    if (this.quitar) {
                        net.saullmc.pezntz.client.DropCliente.quitar(this.id);
                    } else {
                        net.saullmc.pezntz.client.DropCliente.poner(this.id, this.x, this.techo, this.z, this.tiempoApertura);
                    }
                })
        );

        contexto.setPacketHandled(true);
        return true;
    }
}