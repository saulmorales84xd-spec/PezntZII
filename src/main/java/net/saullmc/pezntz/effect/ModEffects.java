package net.saullmc.pezntz.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.saullmc.pezntz.PezntZMod;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, PezntZMod.MOD_ID);

    public static final RegistryObject<MobEffect> MANCHA_ZOMBIE = MOB_EFFECTS.register("mancha_zombie",
            () -> new ManchaZombieEffect(MobEffectCategory.HARMFUL, 0x31522b));

    public static final RegistryObject<MobEffect> PLAGA = MOB_EFFECTS.register("plaga",
            PlagaEffect::new);

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}