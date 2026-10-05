package net.saullmc.pezntz.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.saullmc.pezntz.item.custom.Backpack;
import net.saullmc.pezntz.menu.BackpackMenu;

import java.util.function.Supplier;

public class OpenBackpackPacket {

    public OpenBackpackPacket() {
    }

    public OpenBackpackPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            if (!llevaMochila(player)) return;

            Component titulo = Component.literal("Mochila de " + player.getName().getString());

            NetworkHooks.openScreen(player, new SimpleMenuProvider(
                    (containerId, playerInventory, playerEntity) -> new BackpackMenu(containerId, playerInventory),
                    titulo
            ));
        });

        context.setPacketHandled(true);
        return true;
    }

    public static boolean llevaMochila(net.minecraft.world.entity.player.Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof Backpack) return true;
        }
        for (ItemStack stack : player.getInventory().offhand) {
            if (stack.getItem() instanceof Backpack) return true;
        }
        return false;
    }
}