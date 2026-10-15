package net.saullmc.pezntz.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.block.custom.*;
import net.saullmc.pezntz.item.ModItems;
import net.saullmc.pezntz.item.custom.PrensaBlockItem;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, PezntZMod.MOD_ID);

    public static final RegistryObject<Block> BRICKS_1 = registerBlock("bricks_1",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> BRICKS_2 = registerBlock("bricks_2",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> BRICKS_3 = registerBlock("bricks_3",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> BRICKS_4 = registerBlock("bricks_4",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> PLANKS_1 = registerBlock("planks_1",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));

    public static final RegistryObject<Block> WALL_1 = registerBlock("wall_1",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> PRENSA = registerBlockConItemPropio("prensa",
            () -> new Prensa(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()),
            bloque -> () -> new PrensaBlockItem(bloque.get(), new Item.Properties()));

    public static final RegistryObject<Block> BARRIL_1 = registerBlock("barril_1",
            () -> new Barril(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> BARRIL_2 = registerBlock("barril_2",
            () -> new Barril(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> CAJA_1 = registerBlock("caja_1",
            () -> new Caja1(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).noOcclusion()));

    public static final RegistryObject<Block> CAJA_2 = registerBlock("caja_2",
            () -> new Caja2(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).noOcclusion()));

    public static final RegistryObject<Block> CAJA_3 = registerBlock("caja_3",
            () -> new Caja2(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).noOcclusion()));

    public static final RegistryObject<Block> CAJA_4 = registerBlock("caja_4",
            () -> new Caja2(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).noOcclusion()));

    public static final RegistryObject<Block> CAJA_5 = registerBlock("caja_5",
            () -> new Caja2(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).noOcclusion()));

    public static final RegistryObject<Block> CADAVER = BLOCKS.register("cadaver",
            () -> new Cadaver(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).strength(0.6F).noOcclusion().explosionResistance(1200.0F)));

    private static <T extends Block> RegistryObject<T> registerBlockConItemPropio(
            String name, Supplier<T> block, Function<RegistryObject<T>, Supplier<Item>> item) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, item.apply(toReturn));
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}