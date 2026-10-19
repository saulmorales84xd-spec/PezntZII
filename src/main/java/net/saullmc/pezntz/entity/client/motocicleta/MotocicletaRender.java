package net.saullmc.pezntz.entity.client.motocicleta;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.client.ModModelLayers;
import net.saullmc.pezntz.entity.custom.Motocicleta;

public class MotocicletaRender extends MobRenderer<Motocicleta, MotocicletaModel<Motocicleta>> {

    private static final ResourceLocation TEXTURA =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/motocicleta.png");

    public MotocicletaRender(EntityRendererProvider.Context context) {
        super(context, new MotocicletaModel<>(context.bakeLayer(ModModelLayers.MOTOCICLETA_LAYER)), 1.0f);
        this.shadowRadius = 0.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(Motocicleta entidad) {
        return TEXTURA;
    }
}