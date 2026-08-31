package net.saullmc.pezntz.entity.client.hinchado;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.client.ModModelLayers;
import net.saullmc.pezntz.entity.custom.ZombieHinchado;

import java.util.Map;

public class ZombieHinchadoRender extends MobRenderer<ZombieHinchado, ZombieHinchadoModel<ZombieHinchado>> {

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombiehinchado/zombie_hinchado_verde.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombiehinchado/zombie_hinchado_morado.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombiehinchado/zombie_hinchado_azul.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombiehinchado/zombie_hinchado_naranja.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/entity/zombiehinchado/zombie_hinchado_rosa.png")
    };

    public ZombieHinchadoRender(EntityRendererProvider.Context pContext) {
        super(pContext, new ZombieHinchadoModel<>(pContext.bakeLayer(ModModelLayers.ZOMBIE_HINCHADO_LAYER)), 2f);
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieHinchado pEntity) {
        int variant = pEntity.getVariant();
        if(variant < 0 || variant > 4) {
            variant = 0;
        }
        return TEXTURES[variant];
    }

    @Override
    public void render(ZombieHinchado pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}