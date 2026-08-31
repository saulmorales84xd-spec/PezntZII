package net.saullmc.pezntz.entity.client.arrastrador;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.client.ModModelLayers;
import net.saullmc.pezntz.entity.custom.ZombieArrastrador;

public class ZombieArrastradorRender extends MobRenderer<ZombieArrastrador, ZombieArrastradorModel<ZombieArrastrador>> {

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombiearrastrador/zombie_arrastrador_1.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombiearrastrador/zombie_arrastrador_2.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombiearrastrador/zombie_arrastrador_3.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombiearrastrador/zombie_arrastrador_4.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombiearrastrador/zombie_arrastrador_5.png")
    };

    public ZombieArrastradorRender(EntityRendererProvider.Context pContext) {
        super(pContext, new ZombieArrastradorModel<>(pContext.bakeLayer(ModModelLayers.ZOMBIE_ARRASTRADOR_LAYER)), 2f);
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieArrastrador pEntity) {
        int variant = pEntity.getVariant();
        if(variant < 0 || variant > 4) {
            variant = 0;
        }
        return TEXTURES[variant];
    }

    @Override
    public void render(ZombieArrastrador pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
