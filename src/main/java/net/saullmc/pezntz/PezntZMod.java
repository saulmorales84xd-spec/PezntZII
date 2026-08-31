package net.saullmc.pezntz;

import com.mojang.logging.LogUtils;
import com.tacz.guns.api.TimelessAPI;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.saullmc.pezntz.effect.ModEffects;
import net.saullmc.pezntz.entity.ModEntities;
import net.saullmc.pezntz.entity.client.arrastrador.ZombieArrastradorRender;
import net.saullmc.pezntz.entity.client.hinchado.ZombieHinchadoRender;
import net.saullmc.pezntz.entity.client.parasitador.ZombieParasitadorRender;
import net.saullmc.pezntz.entity.client.quad.QuadRender;
import net.saullmc.pezntz.entity.client.rata.RataCarroneraRender;
import net.saullmc.pezntz.entity.client.tanque.ZombieTanqueRender;
import net.saullmc.pezntz.entity.client.toxico.ZombieToxicoRender;
import net.saullmc.pezntz.entity.client.zumbador.MosquitoZumbadorRender;
import net.saullmc.pezntz.init.ModMenuTypes;
import net.saullmc.pezntz.item.ModCreativeModTabs;
import net.saullmc.pezntz.item.ModItems;
import net.saullmc.pezntz.network.NetworkHandler;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

@Mod(PezntZMod.MOD_ID)
public class PezntZMod {

    public static final String MOD_ID = "pezntz";
    private static final Logger LOGGER = LogUtils.getLogger();

    public PezntZMod() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        ModItems.register(modEventBus);

        ModCreativeModTabs.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModEntities.register(modEventBus);
        ModEffects.register(modEventBus);

        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

            NetworkHandler.register();

        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.LANTERN);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            EntityRenderers.register(ModEntities.ZOMBIE_HINCHADO.get(), ZombieHinchadoRender::new);
            EntityRenderers.register(ModEntities.ZOMBIE_TANQUE.get(), ZombieTanqueRender::new);
            EntityRenderers.register(ModEntities.ZOMBIE_ARRASTRADOR.get(), ZombieArrastradorRender::new);
            EntityRenderers.register(ModEntities.ZOMBIE_TOXICO.get(), ZombieToxicoRender::new);
            EntityRenderers.register(ModEntities.ZOMBIE_PARASITADOR.get(), ZombieParasitadorRender::new);
            EntityRenderers.register(ModEntities.RATA_CARRONERA.get(), RataCarroneraRender::new);
            EntityRenderers.register(ModEntities.MOSQUITO_ZUMBADOR.get(), MosquitoZumbadorRender::new);
            EntityRenderers.register(ModEntities.BOLA_ACIDO.get(), ThrownItemRenderer::new);
            EntityRenderers.register(ModEntities.QUAD.get(), QuadRender::new);

        }
    }
}
