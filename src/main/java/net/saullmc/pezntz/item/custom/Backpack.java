package net.saullmc.pezntz.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.saullmc.pezntz.menu.BackpackMenu;

import java.util.UUID;

public class Backpack extends Item {
    public static final int INVENTORY_SIZE = 27;

    public static final String OWNER_TAG = "BackpackOwner";
    public static final String ID_TAG = "BackpackId";

    public Backpack(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static UUID getOrCreateId(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        if (!nbt.hasUUID(ID_TAG)) {
            nbt.putUUID(ID_TAG, UUID.randomUUID());
        }
        return nbt.getUUID(ID_TAG);
    }

    public static boolean isSameBackpack(ItemStack a, ItemStack b) {
        if (a.isEmpty() || b.isEmpty()) return false;
        if (!(a.getItem() instanceof Backpack) || !(b.getItem() instanceof Backpack)) return false;

        CompoundTag tagA = a.getTag();
        CompoundTag tagB = b.getTag();
        if (tagA == null || tagB == null) return false;
        if (!tagA.hasUUID(ID_TAG) || !tagB.hasUUID(ID_TAG)) return false;

        return tagA.getUUID(ID_TAG).equals(tagB.getUUID(ID_TAG));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {

            CompoundTag nbt = stack.getOrCreateTag();

            if (!nbt.contains(OWNER_TAG)) {
                nbt.putString(OWNER_TAG, player.getName().getString());
            }

            getOrCreateId(stack);

            String ownerName = nbt.getString(OWNER_TAG);
            Component title = Component.literal("Mochila de " + ownerName);

            NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider(
                    (containerId, playerInventory, playerEntity) -> new BackpackMenu(containerId, playerInventory, stack),
                    title
            ), buf -> buf.writeItem(stack));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}