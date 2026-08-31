package net.saullmc.pezntz.capability;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCapabilities {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(BodyHealthData.class);
    }
}