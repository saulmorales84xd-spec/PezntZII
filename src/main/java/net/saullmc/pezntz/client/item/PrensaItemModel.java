package net.saullmc.pezntz.client.item;

import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.custom.PrensaBlockItem;
import software.bernie.geckolib.model.GeoModel;

public class PrensaItemModel extends GeoModel<PrensaBlockItem> {

    private static final ResourceLocation MODEL =
            new ResourceLocation(PezntZMod.MOD_ID, "geo/prensa.geo.json");

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/block/prensa.png");

    private static final ResourceLocation ANIMATION =
            new ResourceLocation(PezntZMod.MOD_ID, "animations/prensa.animation.json");

    @Override
    public ResourceLocation getModelResource(PrensaBlockItem animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(PrensaBlockItem animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(PrensaBlockItem animatable) {
        return ANIMATION;
    }
}