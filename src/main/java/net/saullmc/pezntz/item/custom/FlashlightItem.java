package net.saullmc.pezntz.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.saullmc.pezntz.item.ModItems;

import java.util.List;

public class FlashlightItem extends Item {

    public static final String TAG_ENCENDIDA = "IsOn";

    private static final int TICKS_POR_PUNTO = 20;

    public FlashlightItem(Properties properties) {
        super(properties);
    }

    public static boolean estaEncendida(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(TAG_ENCENDIDA);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            CompoundTag tag = stack.getOrCreateTag();
            boolean encendida = tag.getBoolean(TAG_ENCENDIDA);

            if (!encendida && sinBateria(stack)) {
                level.playSound(null, player.blockPosition(), SoundEvents.LEVER_CLICK,
                        SoundSource.PLAYERS, 0.4F, 0.6F);
                return InteractionResultHolder.fail(stack);
            }

            tag.putBoolean(TAG_ENCENDIDA, !encendida);

            level.playSound(null, player.blockPosition(), SoundEvents.UI_BUTTON_CLICK.get(),
                    SoundSource.PLAYERS, 0.5F, encendida ? 0.8F : 1.2F);
        }

        player.getCooldowns().addCooldown(this, 10);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide()) return;
        if (!estaEncendida(stack)) return;

        if (level.getGameTime() % TICKS_POR_PUNTO != 0) return;

        if (sinBateria(stack)) {
            apagar(stack);
            if (entity instanceof Player player) {
                level.playSound(null, player.blockPosition(), SoundEvents.LEVER_CLICK,
                        SoundSource.PLAYERS, 0.4F, 0.6F);
            }
            return;
        }

        stack.setDamageValue(stack.getDamageValue() + 1);

        if (sinBateria(stack)) {
            apagar(stack);
        }
    }

    private static boolean sinBateria(ItemStack stack) {
        return stack.getDamageValue() >= stack.getMaxDamage() - 1;
    }

    private static void apagar(ItemStack stack) {
        stack.getOrCreateTag().putBoolean(TAG_ENCENDIDA, false);
    }

    @Override
    public boolean isValidRepairItem(ItemStack linterna, ItemStack material) {
        return material.is(ModItems.CHATARRA_ELECTRONICA.get());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        int restante = stack.getMaxDamage() - 1 - stack.getDamageValue();
        int segundos = Math.max(0, restante) * TICKS_POR_PUNTO / 20;

        tooltip.add(Component.literal(estaEncendida(stack) ? "Encendida" : "Apagada")
                .withStyle(style -> style.withColor(estaEncendida(stack) ? 0xFFD54A : 0x808080)));

        tooltip.add(Component.literal("Bateria: " + (segundos / 60) + " min " + (segundos % 60) + " s")
                .withStyle(style -> style.withColor(0x9AA0A6)));
    }
}