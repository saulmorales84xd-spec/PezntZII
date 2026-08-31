package net.saullmc.pezntz.entity.client.rata;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.client.ModModelLayers;
import net.saullmc.pezntz.entity.custom.RataCarronera;

public class RataCarroneraRender extends MobRenderer<RataCarronera, RataCarroneraModel<RataCarronera>> {
    public RataCarroneraRender(EntityRendererProvider.Context pContext) {
        super(pContext, new RataCarroneraModel<>(pContext.bakeLayer(ModModelLayers.RATA_CARRONERA_LAYER)), 2f);
    }

    @Override
    public ResourceLocation getTextureLocation(RataCarronera pEntity) {
        return new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/rata_carronera.png");
    }

    @Override
    public void render(RataCarronera pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
