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
import net.saullmc.pezntz.capability.BodyHealthData;
import net.saullmc.pezntz.capability.BodyHealthProvider;
import net.saullmc.pezntz.network.NetworkHandler;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.saullmc.pezntz.client.item.VendasRenderer;
import net.saullmc.pezntz.network.SyncBodyHealthPacket;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class Vendas extends Item implements GeoItem {

    public static final String CONTROLLER = "vendas_controller";

    private static final RawAnimation USE = RawAnimation.begin().thenPlayAndHold("use");

    private static volatile boolean playingUseAnimation = false;

    public static void setPlayingUseAnimation(boolean playing) {
        playingUseAnimation = playing;
    }

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    public static final int USE_TICKS = 60;

    public static final int COOLDOWN_TICKS = 10;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Vendas(Properties properties) {
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
            private VendasRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new VendasRenderer();
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

            final boolean[] wasHealed = {false};

            serverPlayer.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {

                List<String> parts = new ArrayList<>(List.of("head", "body", "arms", "legs"));
                Collections.shuffle(parts);

                for (String part : parts) {
                    float currentHealth = 0;
                    switch (part) {
                        case "head" -> currentHealth = cap.getHead();
                        case "body" -> currentHealth = cap.getBody();
                        case "arms" -> currentHealth = cap.getArms();
                        case "legs" -> currentHealth = cap.getLegs();
                    }

                    if (currentHealth < BodyHealthData.MAX_HEALTH) {
                        float newHealth = Math.min(BodyHealthData.MAX_HEALTH, currentHealth + BodyHealthData.HEAL_QUICK);

                        switch (part) {
                            case "head" -> cap.setHead(newHealth);
                            case "body" -> cap.setBody(newHealth);
                            case "arms" -> cap.setArms(newHealth);
                            case "legs" -> cap.setLegs(newHealth);
                        }

                        wasHealed[0] = true;

                        NetworkHandler.sendToClients(new SyncBodyHealthPacket(serverPlayer.getId(), cap), serverPlayer);
                        break;
                    }
                }
            });

            if (wasHealed[0] && !serverPlayer.isCreative()) {
                pStack.shrink(1);
            }
        }

        if (pLivingEntity instanceof Player player) {
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        }

        return pStack;
    }
}