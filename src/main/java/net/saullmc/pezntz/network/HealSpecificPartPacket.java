package net.saullmc.pezntz.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.saullmc.pezntz.capability.BodyHealthData;
import net.saullmc.pezntz.capability.BodyHealthData;
import net.saullmc.pezntz.capability.BodyHealthProvider;
import net.saullmc.pezntz.item.custom.Vendas;

import java.util.function.Supplier;

public class HealSpecificPartPacket {
    private final String part;

    public HealSpecificPartPacket(String part) { this.part = part; }
    public HealSpecificPartPacket(FriendlyByteBuf buf) { this.part = buf.readUtf(); }
    public void toBytes(FriendlyByteBuf buf) { buf.writeUtf(part); }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {

                int tempSlot = -1;
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    if (player.getInventory().getItem(i).getItem() instanceof Vendas) {
                        tempSlot = i;
                        break;
                    }
                }

                final int vendaSlot = tempSlot;

                if (vendaSlot != -1 || player.isCreative()) {
                    player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
                        float currentHealth = 0;
                        switch (part) {
                            case "head" -> currentHealth = cap.getHead();
                            case "body" -> currentHealth = cap.getBody();
                            case "arms" -> currentHealth = cap.getArms();
                            case "legs" -> currentHealth = cap.getLegs();
                        }

                        if (currentHealth < BodyHealthData.MAX_HEALTH) {
                            float newHealth = Math.min(BodyHealthData.MAX_HEALTH, currentHealth + BodyHealthData.HEAL_GUI);
                            switch (part) {
                                case "head" -> cap.setHead(newHealth);
                                case "body" -> cap.setBody(newHealth);
                                case "arms" -> cap.setArms(newHealth);
                                case "legs" -> cap.setLegs(newHealth);
                            }

                            if (!player.isCreative()) {
                                player.getInventory().getItem(vendaSlot).shrink(1);
                            }

                            NetworkHandler.sendToClients(new SyncBodyHealthPacket(player.getId(), cap), player);
                        }
                    });
                }
            }
        });
        context.setPacketHandled(true);
        return true;
    }
}