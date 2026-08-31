package net.saullmc.pezntz.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class DiamondTradeData extends SavedData {
    private final MerchantOffers offers = new MerchantOffers();

    public MerchantOffers getOffers() {
        return offers;
    }

    public void addTrade(MerchantOffer offer) {
        this.offers.add(offer);
        this.setDirty();
    }

    public void clearTrades() {
        this.offers.clear();
        this.setDirty();
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (MerchantOffer offer : offers) {
            listTag.add(offer.createTag());
        }
        tag.put("Offers", listTag);
        return tag;
    }

    public static DiamondTradeData load(CompoundTag tag) {
        DiamondTradeData data = new DiamondTradeData();
        if (tag.contains("Offers", 9)) {
            ListTag listTag = tag.getList("Offers", 10);
            for (int i = 0; i < listTag.size(); i++) {
                data.offers.add(new MerchantOffer(listTag.getCompound(i)));
            }
        }
        return data;
    }

    public static DiamondTradeData get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(
                DiamondTradeData::load,
                DiamondTradeData::new,
                "diamond_block_trades"
        );
    }
}
