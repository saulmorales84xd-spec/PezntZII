package net.saullmc.pezntz.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.saullmc.pezntz.client.hud.PickupLogOverlay;

import java.util.function.Supplier;

public class ItemPickupPacket {

    private final ItemStack stack;

    public ItemPickupPacket(ItemStack stack) {
        this.stack = stack;
    }

    public ItemPickupPacket(FriendlyByteBuf buf) {
        this.stack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(this.stack);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> PickupLogOverlay.add(this.stack))
        );

        context.setPacketHandled(true);
        return true;
    }
}