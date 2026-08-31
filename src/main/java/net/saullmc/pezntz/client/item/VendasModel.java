package net.saullmc.pezntz.client.item;

import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.custom.Vendas;
import software.bernie.geckolib.model.GeoModel;

public class VendasModel extends GeoModel<Vendas> {

    private static final ResourceLocation MODEL =
            new ResourceLocation(PezntZMod.MOD_ID, "geo/vendas.geo.json");
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/item/vendas.png");
    private static final ResourceLocation ANIMATION =
            new ResourceLocation(PezntZMod.MOD_ID, "animations/vendas.animation.json");

    @Override
    public ResourceLocation getModelResource(Vendas animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(Vendas animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(Vendas animatable) {
        return ANIMATION;
    }
}