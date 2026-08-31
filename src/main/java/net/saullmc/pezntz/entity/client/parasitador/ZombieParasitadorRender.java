package net.saullmc.pezntz.entity.client.parasitador;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.client.ModModelLayers;
import net.saullmc.pezntz.entity.custom.ZombieParasitador;

public class ZombieParasitadorRender extends MobRenderer<ZombieParasitador, ZombieParasitadorModel<ZombieParasitador>> {

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombieparasitador/zombie_parasitador_1.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombieparasitador/zombie_parasitador_2.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombieparasitador/zombie_parasitador_3.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombieparasitador/zombie_parasitador_4.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombieparasitador/zombie_parasitador_5.png")
    };

    public ZombieParasitadorRender(EntityRendererProvider.Context pContext) {
        super(pContext, new ZombieParasitadorModel<>(pContext.bakeLayer(ModModelLayers.ZOMBIE_PARASITADOR_LAYER)), 2f);
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieParasitador pEntity) {
        int variant = pEntity.getVariant();
        if(variant < 0 || variant > 4) {
            variant = 0;
        }
        return TEXTURES[variant];
    }

    @Override
    public void render(ZombieParasitador pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}