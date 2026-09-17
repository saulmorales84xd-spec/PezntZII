package net.saullmc.pezntz.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.jar.Attributes;

public class ArmaMelee extends SwordItem {

    private static final UUID UUID_ALCANCE = UUID.fromString("9a1f4b2c-6d3e-4a58-9c71-2e8b0d5f37a4");

    @Nullable
    private final RegistryObject<SoundEvent> sonidoGolpe;

    private final double alcanceExtra;

    public ArmaMelee (Tier tier, int danio, float velocidad, Properties properties) {
        this(tier, danio, velocidad, properties, null, 0.00);
    }

    public ArmaMelee (Tier tier, int danio, float velocidad, Properties properties,
                      @Nullable RegistryObject<SoundEvent> sonidoGolpe) {
        this(tier, danio, velocidad, properties, sonidoGolpe, 0.00);
    }

    public ArmaMelee (Tier tier, int danio, float velocidad, Properties properties,
                      @Nullable RegistryObject<SoundEvent> sonidoGolpe, double alcanceExtra) {
        super(tier, danio, velocidad, properties);
        this.sonidoGolpe = sonidoGolpe;
        this.alcanceExtra = alcanceExtra;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (this.sonidoGolpe != null && !pAttacker.level().isClientSide()) {
            pAttacker.level().playSound(null, pTarget.getX(), pTarget.getY(), pTarget.getZ(),
                    this.sonidoGolpe.get(), SoundSource.PLAYERS, 1.0F, 0.9F + pAttacker.getRandom().nextFloat() * 0.2F);
        }

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot){
        if (slot != EquipmentSlot.MAINHAND || this.alcanceExtra <= 0.0D){
            return super.getDefaultAttributeModifiers(slot);
        }

        return ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .putAll(super.getDefaultAttributeModifiers(slot))
                .put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(
                        UUID_ALCANCE, "Alcance del arma", this.alcanceExtra,
                        AttributeModifier.Operation.ADDITION)).build();
    }
}
