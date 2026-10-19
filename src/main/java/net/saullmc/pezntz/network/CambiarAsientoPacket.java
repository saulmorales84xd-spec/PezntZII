package net.saullmc.pezntz.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.saullmc.pezntz.entity.custom.VehiculoBase;

import java.util.function.Supplier;

public class CambiarAsientoPacket {

    public CambiarAsientoPacket() {
    }

    public CambiarAsientoPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context contexto = supplier.get();

        contexto.enqueueWork(() -> {
            ServerPlayer jugador = contexto.getSender();
            if (jugador == null) return;

            if (jugador.getVehicle() instanceof VehiculoBase vehiculo) {
                vehiculo.cambiarDeAsiento(jugador);
            }
        });

        contexto.setPacketHandled(true);
        return true;
    }
}