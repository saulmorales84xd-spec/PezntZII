package net.saullmc.pezntz.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.network.NetworkEvent;
import net.saullmc.pezntz.capability.BodyHealthProvider;
import java.util.function.Supplier;

public class RevivePlayerPacket {
    private final int targetId;

    public RevivePlayerPacket(int targetId) { this.targetId = targetId; }
    public RevivePlayerPacket(FriendlyByteBuf buf) { this.targetId = buf.readInt(); }
    public void toBytes(FriendlyByteBuf buf) { buf.writeInt(targetId); }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer reviver = context.getSender();
            if (reviver != null && reviver.level().getEntity(targetId) instanceof ServerPlayer target) {

                reviver.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(reviverCap -> {
                    if (!reviverCap.isDowned() && reviver.distanceTo(target) < 4.0D) {

                        target.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
                            if (cap.isDowned()) {
                                cap.setReviveProgress(cap.getReviveProgress() + 1);

                                if (cap.getReviveProgress() >= 200) {
                                    cap.setDowned(false);
                                    cap.setReviveProgress(0);
                                    cap.setDownedTimer(0);

                                    cap.setHead(2.0f); cap.setBody(2.0f); cap.setArms(2.0f); cap.setLegs(2.0f);
                                    target.setHealth(6.0f);
                                    target.setPose(Pose.STANDING);
                                }
                                NetworkHandler.sendToClients(new SyncBodyHealthPacket(target.getId(), cap), target);
                            }
                        });
                    }
                });
            }
        });
        context.setPacketHandled(true);
        return true;
    }
}