package net.saullmc.pezntz.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.saullmc.pezntz.block.custom.Cadaver;
import net.saullmc.pezntz.block.entity.CadaverEntity;

import java.util.function.Supplier;

public class RecuperarCadaverPacket {

    public static final double DISTANCIA_MAXIMA = 8.0D;

    public static final boolean SOLO_EL_DUENIO = true;

    private final BlockPos pos;

    public RecuperarCadaverPacket(BlockPos pos) {
        this.pos = pos;
    }

    public RecuperarCadaverPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context contexto = supplier.get();

        contexto.enqueueWork(() -> {
            ServerPlayer jugador = contexto.getSender();
            if (jugador == null) return;

            Level nivel = jugador.level();

            if (jugador.distanceToSqr(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D)
                    > DISTANCIA_MAXIMA * DISTANCIA_MAXIMA) return;

            BlockEntity be = nivel.getBlockEntity(this.pos);
            if (!(be instanceof CadaverEntity cadaver)) return;

            if (SOLO_EL_DUENIO && !cadaver.esDuenio(jugador)) return;

            cadaver.devolverA(jugador);

            jugador.containerMenu.broadcastChanges();
            jugador.getInventory().setChanged();

            if (Cadaver.DESAPARECE_AL_VACIARSE && cadaver.estaVacio()) {
                jugador.closeContainer();
                nivel.removeBlock(this.pos, false);
            }
        });

        contexto.setPacketHandled(true);
        return true;
    }
}
