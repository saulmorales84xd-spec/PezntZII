package net.saullmc.pezntz.client.item;

import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.custom.Botiquin;
import software.bernie.geckolib.model.GeoModel;

public class BotiquinModel extends GeoModel<Botiquin> {

    private static final ResourceLocation MODEL =
            new ResourceLocation(PezntZMod.MOD_ID, "geo/medkit.geo.json");
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/item/medkit.png");
    private static final ResourceLocation ANIMATION =
            new ResourceLocation(PezntZMod.MOD_ID, "animations/medkit.animation.json");

    @Override
    public ResourceLocation getModelResource(Botiquin animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(Botiquin animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(Botiquin animatable) {
        return ANIMATION;
    }
}