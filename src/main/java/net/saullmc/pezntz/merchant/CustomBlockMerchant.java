package net.saullmc.pezntz.merchant;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;
import net.saullmc.pezntz.data.DiamondTradeData;

public class CustomBlockMerchant implements Merchant {
    private final Player player;
    private MerchantOffers offers;

    public CustomBlockMerchant(Player player, MerchantOffers offers) {
        this.player = player;
        this.offers = offers;
    }

    @Override
    public void setTradingPlayer(@Nullable Player player) {}

    @Nullable
    @Override
    public Player getTradingPlayer() {
        return this.player;
    }

    @Override
    public MerchantOffers getOffers() {
        return this.offers;
    }

    @Override
    public void overrideOffers(MerchantOffers offers) {
        this.offers = offers;
    }

    @Override
    public void notifyTrade(MerchantOffer offer) {
        offer.increaseUses();


        if (!this.player.level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) this.player.level();

            DiamondTradeData.get(serverLevel).setDirty();

            serverLevel.playSound(null, this.player.blockPosition(), SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void notifyTradeUpdated(ItemStack stack) {}

    @Override
    public int getVillagerXp() {
        return 0;
    }

    @Override
    public void overrideXp(int xp) {}

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.UI_BUTTON_CLICK.get();
    }

    @Override
    public boolean isClientSide() {
        return true;
    }
}