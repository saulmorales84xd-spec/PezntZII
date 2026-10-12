package net.saullmc.pezntz.client.block;

import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.block.entity.PrensaEntity;
import software.bernie.geckolib.model.GeoModel;

public class PrensaModel extends GeoModel<PrensaEntity> {

    private static final ResourceLocation MODELO =
            new ResourceLocation(PezntZMod.MOD_ID, "geo/prensa.geo.json");

    private static final ResourceLocation TEXTURA =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/block/prensa.png");

    private static final ResourceLocation ANIMACION =
            new ResourceLocation(PezntZMod.MOD_ID, "animations/prensa.animation.json");

    @Override
    public ResourceLocation getModelResource(PrensaEntity animatable) {
        return MODELO;
    }

    @Override
    public ResourceLocation getTextureResource(PrensaEntity animatable) {
        return TEXTURA;
    }

    @Override
    public ResourceLocation getAnimationResource(PrensaEntity animatable) {
        return ANIMACION;
    }
}