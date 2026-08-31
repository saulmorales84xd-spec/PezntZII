package net.saullmc.pezntz.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() { return packetId++; }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation("pezntz", "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(SyncBodyHealthPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncBodyHealthPacket::new)
                .encoder(SyncBodyHealthPacket::toBytes)
                .consumerMainThread(SyncBodyHealthPacket::handle).add();

        net.messageBuilder(RevivePlayerPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RevivePlayerPacket::new)
                .encoder(RevivePlayerPacket::toBytes)
                .consumerMainThread(RevivePlayerPacket::handle).add();

        net.messageBuilder(RemovePlagaC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RemovePlagaC2SPacket::new)
                .encoder(RemovePlagaC2SPacket::toBytes)
                .consumerMainThread(RemovePlagaC2SPacket::handle)
                .add();

        net.messageBuilder(HealSpecificPartPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(HealSpecificPartPacket::new)
                .encoder(HealSpecificPartPacket::toBytes)
                .consumerMainThread(HealSpecificPartPacket::handle).add();
    }

    public static <MSG> void sendToClients(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), message);
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}