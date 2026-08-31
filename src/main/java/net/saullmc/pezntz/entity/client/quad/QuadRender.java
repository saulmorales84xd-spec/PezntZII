package net.saullmc.pezntz.entity.client.quad;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.client.ModModelLayers;
import net.saullmc.pezntz.entity.custom.Quad;

public class QuadRender extends MobRenderer<Quad, QuadModel<Quad>> {
    public QuadRender(EntityRendererProvider.Context pContext) {
        super(pContext, new QuadModel<>(pContext.bakeLayer(ModModelLayers.QUAD_LAYER)), 1.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Quad pEntity) {
        return new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/quad.png");
    }

    @Override
    public void render(Quad pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}