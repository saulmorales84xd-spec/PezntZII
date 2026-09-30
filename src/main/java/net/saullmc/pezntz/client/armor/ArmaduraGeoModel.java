package net.saullmc.pezntz.client.armor;

import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.custom.ArmaduraModItem;
import software.bernie.geckolib.model.GeoModel;

public class ArmaduraGeoModel extends GeoModel<ArmaduraModItem> {

    private final ResourceLocation modelo;
    private final ResourceLocation textura;
    private final ResourceLocation animacion;

    public ArmaduraGeoModel(String modelo, String textura) {
        this.modelo = new ResourceLocation(PezntZMod.MOD_ID, "geo/" + modelo + ".geo.json");
        this.textura = new ResourceLocation(PezntZMod.MOD_ID, "textures/armor/" + textura + ".png");

        this.animacion = new ResourceLocation(PezntZMod.MOD_ID, "animations/armadura_tactica.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(ArmaduraModItem animatable) {
        return this.modelo;
    }

    @Override
    public ResourceLocation getTextureResource(ArmaduraModItem animatable) {
        return this.textura;
    }

    @Override
    public ResourceLocation getAnimationResource(ArmaduraModItem animatable) {
        return this.animacion;
    }
}