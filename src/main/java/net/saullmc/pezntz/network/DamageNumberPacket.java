package net.saullmc.pezntz.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.saullmc.pezntz.client.hud.DamageNumbers;

import java.util.function.Supplier;

public class DamageNumberPacket {

    private final int entityId;
    private final float cantidad;
    private final boolean curacion;

    public DamageNumberPacket(int entityId, float cantidad, boolean curacion) {
        this.entityId = entityId;
        this.cantidad = cantidad;
        this.curacion = curacion;
    }

    public DamageNumberPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.cantidad = buf.readFloat();
        this.curacion = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeFloat(this.cantidad);
        buf.writeBoolean(this.curacion);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                        () -> () -> DamageNumbers.agregar(this.entityId, this.cantidad, this.curacion))
        );

        context.setPacketHandled(true);
        return true;
    }
}