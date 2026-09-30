package net.saullmc.pezntz.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.saullmc.pezntz.PezntZMod;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {

    ARMADURA_TACTICA("armadura_tactica", 15, new int[]{ 2, 5, 6, 2 }, 9,
            SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0f,
            () -> Ingredient.of(ModItems.AGUJA_HILO.get())),

    CHATARRA_REFORZADA("chatarra_reforzada", 33, new int[]{ 3, 6, 8, 3 }, 10,
            SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0f, 0f,
            () -> Ingredient.of(ModItems.CHATARRA_REFORZADA.get())),

    CONTENCION("contencion", 37, new int[]{ 3, 6, 8, 3 }, 15,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0f, 0.1f,
            () -> Ingredient.of(ModItems.TITANIUM_INGOT.get())),

    BLINDADA("blindada", 37, new int[]{ 3, 6, 8, 3 }, 15,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0f, 0.1f,
            () -> Ingredient.of(ModItems.PEZNSINITA_INGOT.get()));

    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    private static final int[] BASE_DURABILITY = { 11, 16, 15, 13 };

    ModArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantmentValue, SoundEvent equipSound,
                      float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type pType) {
        return BASE_DURABILITY[pType.ordinal()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type pType) {
        return this.protectionAmounts[pType.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return PezntZMod.MOD_ID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}