package net.saullmc.pezntz.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;
import net.saullmc.pezntz.PezntZMod;

import java.util.List;

public class ModToolTiers {

    public static final Tier METAL = TierSortingRegistry.registerTier(
            new ForgeTier(2, 1500, 6f, 3f, 25,
                    Tags.Blocks.STORAGE_BLOCKS_IRON, () -> Ingredient.of(Items.IRON_INGOT)),
            new ResourceLocation(PezntZMod.MOD_ID, "metal"), List.of(Tiers.NETHERITE), List.of());

    public static final Tier ACERO = TierSortingRegistry.registerTier(
            new ForgeTier(3, 1500, 8f, 3f, 25,
                    Tags.Blocks.STORAGE_BLOCKS_IRON, () -> Ingredient.of(ModItems.TITANIUM_INGOT.get())),
            new ResourceLocation(PezntZMod.MOD_ID, "acero"), List.of(Tiers.NETHERITE), List.of());

    public static final Tier MADERA = TierSortingRegistry.registerTier(
            new ForgeTier(1, 1500, 4f, 2f, 25,
                    Tags.Blocks.STORAGE_BLOCKS_IRON, () -> Ingredient.of(Items.SPRUCE_LOG)),
            new ResourceLocation(PezntZMod.MOD_ID, "madera"), List.of(Tiers.NETHERITE), List.of());

    public static final Tier PEZNSINITA = TierSortingRegistry.registerTier(
            new ForgeTier(5, 1500, 5f, 4f, 25,
                    Tags.Blocks.STORAGE_BLOCKS_IRON, () -> Ingredient.of(ModItems.PEZNSINITA_INGOT.get())),
            new ResourceLocation(PezntZMod.MOD_ID, "peznsinita"), List.of(Tiers.NETHERITE), List.of());

}