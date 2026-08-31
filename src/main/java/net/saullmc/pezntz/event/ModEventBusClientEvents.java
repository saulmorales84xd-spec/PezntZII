package net.saullmc.pezntz.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.client.ModModelLayers;
import net.saullmc.pezntz.entity.client.arrastrador.ZombieArrastradorModel;
import net.saullmc.pezntz.entity.client.hinchado.ZombieHinchadoModel;
import net.saullmc.pezntz.entity.client.parasitador.ZombieParasitadorModel;
import net.saullmc.pezntz.entity.client.quad.QuadModel;
import net.saullmc.pezntz.entity.client.rata.RataCarroneraModel;
import net.saullmc.pezntz.entity.client.tanque.ZombieTanqueModel;
import net.saullmc.pezntz.entity.client.toxico.ZombieToxicoModel;
import net.saullmc.pezntz.entity.client.zumbador.MosquitoZumbadorModel;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.ZOMBIE_HINCHADO_LAYER, ZombieHinchadoModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ZOMBIE_TANQUE_LAYER, ZombieTanqueModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ZOMBIE_ARRASTRADOR_LAYER, ZombieArrastradorModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ZOMBIE_TOXICO_LAYER, ZombieToxicoModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ZOMBIE_PARASITADOR_LAYER, ZombieParasitadorModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.RATA_CARRONERA_LAYER, RataCarroneraModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.MOSQUITO_ZUMBADOR_LAYER, MosquitoZumbadorModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.QUAD_LAYER, QuadModel::createBodyLayer);

    }
}
