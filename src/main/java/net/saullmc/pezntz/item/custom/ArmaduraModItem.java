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
import net.saullmc.pezntz.client.armor.ArmaduraRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Consumer;

public class ArmaduraModItem extends ArmorItem implements GeoItem {

    private static final UUID[] UUID_VIDA = {
            UUID.fromString("5f0b3c1e-7a2d-4e8f-9b61-0c4d2a7e1f01"),
            UUID.fromString("5f0b3c1e-7a2d-4e8f-9b61-0c4d2a7e1f02"),
            UUID.fromString("5f0b3c1e-7a2d-4e8f-9b61-0c4d2a7e1f03"),
            UUID.fromString("5f0b3c1e-7a2d-4e8f-9b61-0c4d2a7e1f04"),
    };

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final String modelo;
    private final String textura;
    private final double vidaVanilla;
    private final float bonusParte;

    private final boolean protegeDeRadiacion;

    public ArmaduraModItem(ArmorMaterial material, Type tipo, Properties properties,
                           String modelo, String textura, double vidaVanilla, float bonusParte) {
        this(material, tipo, properties, modelo, textura, vidaVanilla, bonusParte, false);
    }

    public ArmaduraModItem(ArmorMaterial material, Type tipo, Properties properties,
                           String modelo, String textura, double vidaVanilla, float bonusParte,
                           boolean protegeDeRadiacion) {
        super(material, tipo, properties);
        this.modelo = modelo;
        this.textura = textura;
        this.vidaVanilla = vidaVanilla;
        this.bonusParte = bonusParte;
        this.protegeDeRadiacion = protegeDeRadiacion;
    }

    public boolean protegeDeRadiacion() {
        return this.protegeDeRadiacion;
    }

    public float getBonusParte() {
        return this.bonusParte;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot != this.getEquipmentSlot() || this.vidaVanilla == 0.0D) {
            return super.getDefaultAttributeModifiers(slot);
        }

        return ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .putAll(super.getDefaultAttributeModifiers(slot))
                .put(Attributes.MAX_HEALTH, new AttributeModifier(
                        UUID_VIDA[slot.getIndex()], "Vida de armadura",
                        this.vidaVanilla, AttributeModifier.Operation.ADDITION))
                .build();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private ArmaduraRenderer renderer;

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entidad, ItemStack stack,
                                                          EquipmentSlot hueco, HumanoidModel<?> original) {
                if (this.renderer == null) {
                    this.renderer = new ArmaduraRenderer(
                            ArmaduraModItem.this.modelo, ArmaduraModItem.this.textura);
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