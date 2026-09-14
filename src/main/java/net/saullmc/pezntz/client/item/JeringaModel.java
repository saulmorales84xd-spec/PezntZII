package net.saullmc.pezntz.client.item;

import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.custom.JeringaItem;
import software.bernie.geckolib.model.GeoModel;

public class JeringaModel extends GeoModel<JeringaItem> {

    private static final ResourceLocation MODEL =
            new ResourceLocation(PezntZMod.MOD_ID, "geo/adrenalina.geo.json");
    private static final ResourceLocation ANIMATION =
            new ResourceLocation(PezntZMod.MOD_ID, "animations/adrenalina.animation.json");

    private final ResourceLocation textura;

    public JeringaModel(String nombre) {
        this.textura = new ResourceLocation(PezntZMod.MOD_ID, "textures/item/" + nombre + ".png");
    }

    @Override
    public ResourceLocation getModelResource(JeringaItem animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(JeringaItem animatable) {
        return this.textura;
    }

    @Override
    public ResourceLocation getAnimationResource(JeringaItem animatable) {
        return ANIMATION;
    }
}