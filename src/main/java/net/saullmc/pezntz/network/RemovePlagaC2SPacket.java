package net.saullmc.pezntz.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.saullmc.pezntz.effect.ModEffects;
import java.util.function.Supplier;

public class RemovePlagaC2SPacket {
    public RemovePlagaC2SPacket() {}

    public RemovePlagaC2SPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.hasEffect(ModEffects.PLAGA.get())) {
                player.removeEffect(ModEffects.PLAGA.get());
            }
        });

        context.setPacketHandled(true);
        return true;
    }
}