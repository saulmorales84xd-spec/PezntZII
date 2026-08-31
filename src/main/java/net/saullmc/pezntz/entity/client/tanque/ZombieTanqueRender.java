package net.saullmc.pezntz.entity.client.tanque;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.client.ModModelLayers;
import net.saullmc.pezntz.entity.client.hinchado.ZombieHinchadoModel;
import net.saullmc.pezntz.entity.custom.ZombieHinchado;
import net.saullmc.pezntz.entity.custom.ZombieTanque;

public class ZombieTanqueRender extends MobRenderer<ZombieTanque, ZombieTanqueModel<ZombieTanque>> {

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombietanque/zombie_tanque_blanco.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombietanque/zombie_tanque_azul.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombietanque/zombie_tanque_rosa.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombietanque/zombie_tanque_verde.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombietanque/zombie_tanque_amarillo.png")
    };

    public ZombieTanqueRender(EntityRendererProvider.Context pContext) {
        super(pContext, new ZombieTanqueModel<>(pContext.bakeLayer(ModModelLayers.ZOMBIE_TANQUE_LAYER)), 2f);
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieTanque pEntity) {
        int variant = pEntity.getVariant();
        if(variant < 0 || variant > 4) {
            variant = 0;
        }
        return TEXTURES[variant];
    }

    @Override
    public void render(ZombieTanque pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}