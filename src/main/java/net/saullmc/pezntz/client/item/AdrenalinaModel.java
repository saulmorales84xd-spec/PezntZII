package net.saullmc.pezntz.client.item;

import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.custom.Adrenalina;
import software.bernie.geckolib.model.GeoModel;

public class AdrenalinaModel extends GeoModel<Adrenalina> {

    private static final ResourceLocation MODEL =
            new ResourceLocation(PezntZMod.MOD_ID, "geo/adrenalina.geo.json");
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/item/adrenalina.png");
    private static final ResourceLocation ANIMATION =
            new ResourceLocation(PezntZMod.MOD_ID, "animations/adrenalina.animation.json");

    @Override
    public ResourceLocation getModelResource(Adrenalina animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(Adrenalina animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(Adrenalina animatable) {
        return ANIMATION;
    }
}