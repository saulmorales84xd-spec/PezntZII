package net.saullmc.pezntz.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.saullmc.pezntz.PezntZMod;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PezntZMod.MOD_ID);

    public static final RegistryObject<SoundEvent> GOLPE_BATE = registrar("golpe_bate");
    public static final RegistryObject<SoundEvent> GOLPE_LUCILLE = registrar("golpe_lucille");
    public static final RegistryObject<SoundEvent> GOLPE_PALANCA = registrar("golpe_palanca");
    public static final RegistryObject<SoundEvent> GOLPE_MOTOSIERRA = registrar("golpe_motosierra");
    public static final RegistryObject<SoundEvent> GOLPE_GUITARRA = registrar("golpe_guitarra");
    public static final RegistryObject<SoundEvent> GOLPE_LANZALLAMAS = registrar("golpe_lanzallamas");

    public static final RegistryObject<SoundEvent> MOTOSIERRA_ACTIVA = registrar("motosierra_activa");

    public static final RegistryObject<SoundEvent> LANZALLAMAS_ACTIVO = registrar("lanzallamas_activo");

    private static RegistryObject<SoundEvent> registrar(String nombre) {
        return SOUND_EVENTS.register(nombre,
                () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PezntZMod.MOD_ID, nombre)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}