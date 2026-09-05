package net.saullmc.pezntz.item.custom;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.saullmc.pezntz.capability.BodyHealthData;
import net.saullmc.pezntz.capability.BodyHealthProvider;
import net.saullmc.pezntz.client.item.BotiquinRenderer;
import net.saullmc.pezntz.network.NetworkHandler;
import net.saullmc.pezntz.network.SyncBodyHealthPacket;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class Botiquin extends Item implements GeoItem {

    public static final String CONTROLLER = "medkit_controller";

    private static final RawAnimation USE = RawAnimation.begin().thenPlayAndHold("use");

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    public static final int USE_TICKS = 200;

    public static final int COOLDOWN_TICKS = 10;

    private static volatile boolean playingUseAnimation = false;

    public static void setPlayingUseAnimation(boolean playing) {
        playingUseAnimation = playing;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Botiquin(Properties properties) {
        super(properties);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, CONTROLLER, 4,
                state -> state.setAndContinue(playingUseAnimation ? USE : IDLE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BotiquinRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new BotiquinRenderer();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return USE_TICKS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
      return UseAnim.NONE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        pPlayer.startUsingItem(pUsedHand);

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide && pLivingEntity instanceof ServerPlayer serverPlayer) {

            serverPlayer.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
                cap.setHead(BodyHealthData.MAX_HEALTH);
                cap.setBody(BodyHealthData.MAX_HEALTH);
                cap.setArms(BodyHealthData.MAX_HEALTH);
                cap.setLegs(BodyHealthData.MAX_HEALTH);

                NetworkHandler.sendToClients(new SyncBodyHealthPacket(serverPlayer.getId(), cap), serverPlayer);
            });

            serverPlayer.setHealth(serverPlayer.getMaxHealth());

            if (!serverPlayer.isCreative()) {
                pStack.shrink(1);
            }
        }

        if (pLivingEntity instanceof Player player) {
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        }

        return pStack;
    }
}