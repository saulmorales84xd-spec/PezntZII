package net.saullmc.pezntz.item.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.saullmc.pezntz.capability.BodyHealthData;
import net.saullmc.pezntz.capability.BodyHealthProvider;
import net.saullmc.pezntz.network.NetworkHandler;
import net.saullmc.pezntz.network.SyncBodyHealthPacket;

public class Morfina extends JeringaItem {

    public static final float CURA_POR_PARTE = 2.0F;

    public static final float FRACCION_VIDA = 0.20F;

    public Morfina(Properties properties) {
        super(properties, "morfina");
    }

    @Override
    protected void aplicar(ServerPlayer usuario, Player receptor) {
        receptor.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
            cap.setHead(cap.getHead() + CURA_POR_PARTE);
            cap.setBody(cap.getBody() + CURA_POR_PARTE);
            cap.setArms(cap.getArms() + CURA_POR_PARTE);
            cap.setLegs(cap.getLegs() + CURA_POR_PARTE);

            if (receptor instanceof ServerPlayer serverReceptor) {
                NetworkHandler.sendToClients(new SyncBodyHealthPacket(receptor.getId(), cap), serverReceptor);
            }        });

        receptor.heal(receptor.getMaxHealth() * FRACCION_VIDA);
    }
}