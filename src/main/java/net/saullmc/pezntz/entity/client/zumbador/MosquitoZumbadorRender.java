package net.saullmc.pezntz.entity.client.zumbador;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.client.ModModelLayers;
import net.saullmc.pezntz.entity.custom.MosquitoZumbador;

public class MosquitoZumbadorRender extends MobRenderer<MosquitoZumbador, MosquitoZumbadorModel<MosquitoZumbador>> {
    public MosquitoZumbadorRender(EntityRendererProvider.Context pContext) {
        super(pContext, new MosquitoZumbadorModel<>(pContext.bakeLayer(ModModelLayers.MOSQUITO_ZUMBADOR_LAYER)), 2f);
    }

    @Override
    public ResourceLocation getTextureLocation(MosquitoZumbador pEntity) {
        return new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/mosquito_zumbador.png");
    }

    @Override
    public void render(MosquitoZumbador pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
