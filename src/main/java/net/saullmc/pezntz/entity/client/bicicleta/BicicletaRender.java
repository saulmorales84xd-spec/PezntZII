package net.saullmc.pezntz.entity.client.bicicleta;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.client.ModModelLayers;
import net.saullmc.pezntz.entity.custom.Bicicleta;

public class BicicletaRender extends MobRenderer<Bicicleta, BicicletaModel<Bicicleta>> {

    private static final ResourceLocation TEXTURA =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/bicicleta.png");

    public BicicletaRender(EntityRendererProvider.Context context) {
        super(context, new BicicletaModel<>(context.bakeLayer(ModModelLayers.BICICLETA_LAYER)), 1.0f);
        this.shadowRadius = 0.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(Bicicleta entidad) {
        return TEXTURA;
    }
}