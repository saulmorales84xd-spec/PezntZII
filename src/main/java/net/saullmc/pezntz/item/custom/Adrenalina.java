package net.saullmc.pezntz.item.custom;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.saullmc.pezntz.client.item.AdrenalinaRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class Adrenalina extends Item implements GeoItem {

    public static final String CONTROLLER = "adrenalina_controller";

    private static final RawAnimation USE = RawAnimation.begin().thenPlayAndHold("use");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    public static final int USE_TICKS = 40;

    public static final int COOLDOWN_TICKS = 10;

    public static final int FUERZA_TICKS = 600;
    public static final int FUERZA_NIVEL = 0;

    public static final int VELOCIDAD_TICKS = 200;
    public static final int VELOCIDAD_NIVEL = 0;

    private static final double ALCANCE = 3.5D;

    private static final String TAG_OBJETIVO = "AdrenalinaObjetivo";

    private static volatile boolean playingUseAnimation = false;

    public static void setPlayingUseAnimation(boolean playing) {
        playingUseAnimation = playing;
    }

    public static boolean isPlayingUseAnimation() {
        return playingUseAnimation;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Adrenalina(Properties properties) {
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
            private AdrenalinaRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new AdrenalinaRenderer();
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

        if (!pLevel.isClientSide) {
            Player objetivo = buscarObjetivo(pPlayer);

            if (objetivo != null) {
                stack.getOrCreateTag().putUUID(TAG_OBJETIVO, objetivo.getUUID());
            } else if (stack.getTag() != null) {
                stack.getTag().remove(TAG_OBJETIVO);
            }
        }

        pPlayer.startUsingItem(pUsedHand);

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide && pLivingEntity instanceof ServerPlayer usuario) {

            Player receptor = recuperarObjetivo(pStack, usuario);

            receptor.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, FUERZA_TICKS, FUERZA_NIVEL));
            receptor.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, VELOCIDAD_TICKS, VELOCIDAD_NIVEL));

            if (pStack.getTag() != null) {
                pStack.getTag().remove(TAG_OBJETIVO);
            }

            if (!usuario.isCreative()) {
                pStack.shrink(1);
            }
        }

        if (pLivingEntity instanceof Player player) {
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        }

        return pStack;
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeLeft) {
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeLeft);

        if (!pLevel.isClientSide && pStack.getTag() != null) {
            pStack.getTag().remove(TAG_OBJETIVO);
        }
    }

    private static Player buscarObjetivo(Player usuario) {
        Vec3 ojo = usuario.getEyePosition();
        Vec3 mira = usuario.getViewVector(1.0F);
        Vec3 fin = ojo.add(mira.scale(ALCANCE));

        AABB zona = usuario.getBoundingBox().expandTowards(mira.scale(ALCANCE)).inflate(1.0D);

        Player mejor = null;
        double mejorDistancia = Double.MAX_VALUE;

        for (Player otro : usuario.level().getEntitiesOfClass(Player.class, zona,
                p -> p != usuario && p.isAlive() && !p.isSpectator())) {

            Optional<Vec3> golpe = otro.getBoundingBox().inflate(0.3D).clip(ojo, fin);

            if (golpe.isPresent()) {
                double distancia = ojo.distanceToSqr(golpe.get());
                if (distancia < mejorDistancia) {
                    mejorDistancia = distancia;
                    mejor = otro;
                }
            }
        }

        return mejor;
    }

    private static Player recuperarObjetivo(ItemStack stack, ServerPlayer usuario) {
        if (stack.getTag() == null || !stack.getTag().hasUUID(TAG_OBJETIVO)) {
            return usuario;
        }

        UUID id = stack.getTag().getUUID(TAG_OBJETIVO);
        Player objetivo = usuario.level().getPlayerByUUID(id);

        if (objetivo == null || !objetivo.isAlive()) {
            return usuario;
        }

        if (usuario.distanceTo(objetivo) > ALCANCE + 1.5D) {
            return usuario;
        }

        return objetivo;
    }
}