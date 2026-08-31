package net.saullmc.pezntz.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.saullmc.pezntz.capability.BodyHealthData;

import java.util.function.Supplier;

public class SyncBodyHealthPacket {
    private final int entityId;
    private final float head, body, arms, legs;
    private final boolean isDowned;
    private final int downedTimer, reviveProgress;

    public SyncBodyHealthPacket(int entityId, BodyHealthData data) {
        this.entityId = entityId;
        this.head = data.getHead(); this.body = data.getBody();
        this.arms = data.getArms(); this.legs = data.getLegs();
        this.isDowned = data.isDowned();
        this.downedTimer = data.getDownedTimer();
        this.reviveProgress = data.getReviveProgress();
    }

    public SyncBodyHealthPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.head = buf.readFloat(); this.body = buf.readFloat();
        this.arms = buf.readFloat(); this.legs = buf.readFloat();
        this.isDowned = buf.readBoolean();
        this.downedTimer = buf.readInt();
        this.reviveProgress = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeFloat(head); buf.writeFloat(body);
        buf.writeFloat(arms); buf.writeFloat(legs);
        buf.writeBoolean(isDowned);
        buf.writeInt(downedTimer);
        buf.writeInt(reviveProgress);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        ClientPacketHandler.handleSyncBodyHealth(
                                entityId, head, body, arms, legs,
                                isDowned, downedTimer, reviveProgress))
        );
        context.setPacketHandled(true);
        return true;
    }
}