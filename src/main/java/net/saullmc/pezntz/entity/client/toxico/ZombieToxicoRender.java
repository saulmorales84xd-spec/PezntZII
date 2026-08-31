package net.saullmc.pezntz.entity.client.toxico;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.client.ModModelLayers;
import net.saullmc.pezntz.entity.custom.ZombieToxico;

public class ZombieToxicoRender extends MobRenderer<ZombieToxico, ZombieToxicoModel<ZombieToxico>> {

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombietoxico/zombie_toxico_1.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombietoxico/zombie_toxico_2.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombietoxico/zombie_toxico_3.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombietoxico/zombie_toxico_4.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombietoxico/zombie_toxico_5.png")
    };

    public ZombieToxicoRender(EntityRendererProvider.Context pContext) {
        super(pContext, new ZombieToxicoModel<>(pContext.bakeLayer(ModModelLayers.ZOMBIE_TOXICO_LAYER)), 2f);
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieToxico pEntity) {
        int variant = pEntity.getVariant();
        if(variant < 0 || variant > 4) {
            variant = 0;
        }
        return TEXTURES[variant];
    }

    @Override
    public void render(ZombieToxico pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}