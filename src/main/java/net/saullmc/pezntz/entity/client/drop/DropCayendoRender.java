package net.saullmc.pezntz.entity.client.drop;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.saullmc.pezntz.block.ModBlocks;
import net.saullmc.pezntz.entity.custom.DropCayendo;

public class DropCayendoRender extends EntityRenderer<DropCayendo> {

    public static final float OFFSET_X = -0.5F;
    public static final float OFFSET_Y = 0.0F;
    public static final float OFFSET_Z = -0.5F;

    private final BlockRenderDispatcher bloques;

    public DropCayendoRender(EntityRendererProvider.Context contexto) {
        super(contexto);

        this.bloques = contexto.getBlockRenderDispatcher();
        this.shadowRadius = 0.9F;
    }

    @Override
    public void render(DropCayendo entidad, float giro, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int luz) {

        BlockState estado = ModBlocks.DROP.get().defaultBlockState();

        poseStack.pushPose();
        poseStack.translate(OFFSET_X, OFFSET_Y, OFFSET_Z);

        this.bloques.renderSingleBlock(estado, poseStack, bufferSource, luz, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();

        super.render(entidad, giro, partialTick, poseStack, bufferSource, luz);
    }

    @Override
    public ResourceLocation getTextureLocation(DropCayendo entidad) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
