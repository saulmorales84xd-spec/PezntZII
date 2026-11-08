package net.saullmc.pezntz.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.block.ModBlocks;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PezntZMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<PrensaEntity>> PRENSA =
            BLOCK_ENTITIES.register("prensa",
                    () -> BlockEntityType.Builder.of(
                            PrensaEntity::new,
                            ModBlocks.PRENSA.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<CadaverEntity>> CADAVER =
            BLOCK_ENTITIES.register("cadaver",
                    () -> BlockEntityType.Builder.of(
                            CadaverEntity::new,
                            ModBlocks.CADAVER.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<CofreCandadoEntity>> COFRE_CANDADO =
            BLOCK_ENTITIES.register("cofre_candado",
                    () -> BlockEntityType.Builder.of(
                            CofreCandadoEntity::new,
                            ModBlocks.COFRE_CANDADO.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<ContenedorLootEntity>> CONTENEDOR_LOOT =
            BLOCK_ENTITIES.register("contenedor_loot",
                    () -> BlockEntityType.Builder.of(
                            ContenedorLootEntity::new,
                            ModBlocks.ARMARIO_IZQ.get(),
                            ModBlocks.ARMARIO_DER.get(),
                            ModBlocks.CAJON_COCINA_IZQ.get(),
                            ModBlocks.CAJON_COCINA_DER.get(),
                            ModBlocks.ENCIMERA.get(),
                            ModBlocks.CAJON_DOBLE.get(),
                            ModBlocks.CAJON_TRIPLE.get(),
                            ModBlocks.ESPEJO.get(),
                            ModBlocks.LOCKER.get(),
                            ModBlocks.LOCKER_ABIERTO.get(),
                            ModBlocks.LOCKER_BAJO.get(),
                            ModBlocks.LOCKER_BAJO_ABIERTO.get(),
                            ModBlocks.CAJAS_MILITARES.get(),
                            ModBlocks.CAJA_MILITAR.get(),
                            ModBlocks.CUERPO_1.get(),
                            ModBlocks.CUERPO_2.get(),
                            ModBlocks.CUERPO_3.get(),
                            ModBlocks.CUERPO_4.get(),
                            ModBlocks.CUERPO_5.get(),
                            ModBlocks.CUERPO_6.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<DropEntity>> DROP =
            BLOCK_ENTITIES.register("drop",
                    () -> BlockEntityType.Builder.of(
                            DropEntity::new,
                            ModBlocks.DROP.get()
                    ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}