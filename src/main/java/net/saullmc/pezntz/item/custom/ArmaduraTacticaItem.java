package net.saullmc.pezntz.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.saullmc.pezntz.client.armor.ArmaduraTacticaRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Consumer;

public class ArmaduraTacticaItem extends ArmorItem implements GeoItem {

    public static final double VIDA_VANILLA = 5.0D;

    private static final UUID[] UUID_VIDA = {
            UUID.fromString("5f0b3c1e-7a2d-4e8f-9b61-0c4d2a7e1f01"),
            UUID.fromString("5f0b3c1e-7a2d-4e8f-9b61-0c4d2a7e1f02"),
            UUID.fromString("5f0b3c1e-7a2d-4e8f-9b61-0c4d2a7e1f03"),
            UUID.fromString("5f0b3c1e-7a2d-4e8f-9b61-0c4d2a7e1f04"),
    };

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ArmaduraTacticaItem(ArmorMaterial material, Type tipo, Properties properties) {
        super(material, tipo, properties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot != this.getEquipmentSlot()) {
            return super.getDefaultAttributeModifiers(slot);
        }

        UUID id = UUID_VIDA[slot.getIndex()];

        return ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .putAll(super.getDefaultAttributeModifiers(slot))
                .put(Attributes.MAX_HEALTH, new AttributeModifier(
                        id, "Vida de armadura tactica", VIDA_VANILLA, AttributeModifier.Operation.ADDITION))
                .build();
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private ArmaduraTacticaRenderer renderer;

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entidad, ItemStack stack,
                                                          EquipmentSlot hueco, HumanoidModel<?> original) {
                if (this.renderer == null) {
                    this.renderer = new ArmaduraTacticaRenderer();
                }

                this.renderer.prepForRender(entidad, stack, hueco, original);
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}