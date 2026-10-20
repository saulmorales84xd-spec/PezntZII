package net.saullmc.pezntz.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AbrirTecladoPacket {

    private final BlockPos pos;

    private final boolean estableciendo;

    public AbrirTecladoPacket(BlockPos pos, boolean estableciendo) {
        this.pos = pos;
        this.estableciendo = estableciendo;
    }

    public AbrirTecladoPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.estableciendo = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeBoolean(this.estableciendo);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context contexto = supplier.get();

        contexto.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        net.saullmc.pezntz.client.screen.TecladoScreen.abrir(this.pos, !this.estableciendo))
        );

        contexto.setPacketHandled(true);
        return true;
    }
}