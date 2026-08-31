package net.saullmc.pezntz.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.custom.Vendas;


@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VendasHandRenderer {

    private static final float ANCHOR_X = 0.0F;
    private static final float ANCHOR_Y = 0.0F;
    private static final float ANCHOR_Z = 0.0F;
    private static final float ANCHOR_SCALE = 1.0F;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        LocalPlayer player = Minecraft.getInstance().player;

        boolean bandaging = player != null
                && player.isUsingItem()
                && player.getUseItem().getItem() instanceof Vendas;

        Vendas.setPlayingUseAnimation(bandaging);
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.level == null) return;

        ItemStack mainHand = player.getMainHandItem();
        boolean holding = mainHand.getItem() instanceof Vendas;

        boolean cachedIsVendas = event.getItemStack().getItem() instanceof Vendas;

        if (!holding && !cachedIsVendas) return;

        event.setCanceled(true);

        if (!holding) return;

        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();

        poseStack.translate(ANCHOR_X, ANCHOR_Y, ANCHOR_Z);
        poseStack.scale(ANCHOR_SCALE, ANCHOR_SCALE, ANCHOR_SCALE);

        mc.getItemRenderer().renderStatic(
                player,
                mainHand,
                ItemDisplayContext.FIRST_PERSON_RIGHT_HAND,
                false,
                poseStack,
                event.getMultiBufferSource(),
                mc.level,
                event.getPackedLight(),
                OverlayTexture.NO_OVERLAY,
                0
        );

        poseStack.popPose();
    }
}