package net.saullmc.pezntz.entity.client.lancha;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.client.ModModelLayers;
import net.saullmc.pezntz.entity.custom.Lancha;

public class LanchaRender extends MobRenderer<Lancha, LanchaModel<Lancha>> {

    private static final ResourceLocation TEXTURA =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/lancha.png");

    public LanchaRender(EntityRendererProvider.Context context) {
        super(context, new LanchaModel<>(context.bakeLayer(ModModelLayers.LANCHA_LAYER)), 3.0f);
        this.shadowRadius = 0.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(Lancha entidad) {
        return TEXTURA;
    }
}