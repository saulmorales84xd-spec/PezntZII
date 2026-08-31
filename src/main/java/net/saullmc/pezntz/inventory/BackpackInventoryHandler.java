package net.saullmc.pezntz.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class BackpackInventoryHandler extends ItemStackHandler {
    private final ItemStack backpackStack;

    public BackpackInventoryHandler(int size, ItemStack backpackStack) {
        super(size);
        this.backpackStack = backpackStack;

        CompoundTag tag = backpackStack.getTag();
        if (tag != null && tag.contains("BackpackInventory")) {
            this.deserializeNBT(tag.getCompound("BackpackInventory"));
        }
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        CompoundTag tag = backpackStack.getOrCreateTag();
        tag.put("BackpackInventory", this.serializeNBT());
    }
}