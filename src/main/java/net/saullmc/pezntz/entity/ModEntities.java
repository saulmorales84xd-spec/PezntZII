package net.saullmc.pezntz.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.custom.*;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PezntZMod.MOD_ID);

    public static final RegistryObject<EntityType<ZombieHinchado>> ZOMBIE_HINCHADO =
            ENTITY_TYPES.register("zombie_hinchado", () -> EntityType.Builder.of(ZombieHinchado::new, MobCategory.MONSTER)
                    .sized(1.25f, 2.0f).build("zombie_hinchado"));

    public static final RegistryObject<EntityType<ZombieTanque>> ZOMBIE_TANQUE =
            ENTITY_TYPES.register("zombie_tanque", () -> EntityType.Builder.of(ZombieTanque::new, MobCategory.MONSTER)
                    .sized(2.25f, 4.0f).build("zombie_tanque"));

    public static final RegistryObject<EntityType<ZombieArrastrador>> ZOMBIE_ARRASTRADOR =
            ENTITY_TYPES.register("zombie_arrastrador", () -> EntityType.Builder.of(ZombieArrastrador::new, MobCategory.MONSTER)
                    .sized(1f, 1.0f).build("zombie_arrastrador"));

    public static final RegistryObject<EntityType<ZombieToxico>> ZOMBIE_TOXICO =
            ENTITY_TYPES.register("zombie_toxico", () -> EntityType.Builder.of(ZombieToxico::new, MobCategory.MONSTER)
                    .sized(1f, 2.0f).build("zombie_toxico"));

    public static final RegistryObject<EntityType<ZombieParasitador>> ZOMBIE_PARASITADOR =
            ENTITY_TYPES.register("zombie_parasitador", () -> EntityType.Builder.of(ZombieParasitador::new, MobCategory.MONSTER)
                    .sized(1f, 2.0f).build("zombie_parasitador"));

    public static final RegistryObject<EntityType<RataCarronera>> RATA_CARRONERA =
            ENTITY_TYPES.register("rata_carronera", () -> EntityType.Builder.of(RataCarronera::new, MobCategory.MONSTER)
                    .sized(1f, 1.0f).build("rata_carronera"));

    public static final RegistryObject<EntityType<MosquitoZumbador>> MOSQUITO_ZUMBADOR =
            ENTITY_TYPES.register("mosquito_zumbador", () -> EntityType.Builder.of(MosquitoZumbador::new, MobCategory.MONSTER)
                    .sized(1f, 1.5f).build("mosquito_zumbador"));

    public static final RegistryObject<EntityType<BolaAcido>> BOLA_ACIDO =
            ENTITY_TYPES.register("bola_acido", () -> EntityType.Builder.<BolaAcido>of(BolaAcido::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).build("bola_acido"));

    public static final RegistryObject<EntityType<Quad>> QUAD =
            ENTITY_TYPES.register("quad", () -> EntityType.Builder.of(Quad::new, MobCategory.MISC)
                    .sized(1.5f, 1.2f).build("quad"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}