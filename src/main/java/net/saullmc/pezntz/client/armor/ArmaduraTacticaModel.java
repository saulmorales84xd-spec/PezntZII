package net.saullmc.pezntz.client.armor;

import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.custom.ArmaduraTacticaItem;
import software.bernie.geckolib.model.GeoModel;

public class ArmaduraTacticaModel extends GeoModel<ArmaduraTacticaItem> {

    private static final ResourceLocation MODELO =
            new ResourceLocation(PezntZMod.MOD_ID, "geo/armadura_tactica.geo.json");

    private static final ResourceLocation TEXTURA =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/armor/armadura_tactica.png");

    private static final ResourceLocation ANIMACION =
            new ResourceLocation(PezntZMod.MOD_ID, "animations/armadura_tactica.animation.json");

    @Override
    public ResourceLocation getModelResource(ArmaduraTacticaItem animatable) {
        return MODELO;
    }

    @Override
    public ResourceLocation getTextureResource(ArmaduraTacticaItem animatable) {
        return TEXTURA;
    }

    @Override
    public ResourceLocation getAnimationResource(ArmaduraTacticaItem animatable) {
        return ANIMACION;
    }
}