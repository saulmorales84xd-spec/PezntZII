package net.saullmc.pezntz.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FlashlightItem extends Item {

    public FlashlightItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            CompoundTag tag = stack.getOrCreateTag();
            boolean isOn = tag.getBoolean("IsOn");
            tag.putBoolean("IsOn", !isOn);

            level.playSound(null, player.blockPosition(), SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.PLAYERS, 0.5f, isOn ? 0.8f : 1.2f);
        }

        player.getCooldowns().addCooldown(this, 10);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("IsOn");
    }
}