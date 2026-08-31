package net.saullmc.pezntz.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.saullmc.pezntz.init.ModMenuTypes;
import net.saullmc.pezntz.inventory.BackpackInventoryHandler;
import net.saullmc.pezntz.item.custom.Backpack;

public class BackpackMenu extends AbstractContainerMenu {
    private final ItemStack backpackStack;

    public BackpackMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, extraData.readItem());
    }

    public BackpackMenu(int containerId, Inventory playerInventory, ItemStack backpackStack) {
        super(ModMenuTypes.BACKPACK_MENU.get(), containerId);
        this.backpackStack = backpackStack;

        IItemHandler backpackInventory = new BackpackInventoryHandler(Backpack.INVENTORY_SIZE, backpackStack);

        int xStart = 8;
        int yStart = 18;
        int slotIndex = 0;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new SlotItemHandler(backpackInventory, slotIndex, xStart + col * 18, yStart + row * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return !(stack.getItem() instanceof Backpack);
                    }
                });
                slotIndex++;
            }
        }

        int playerInvY = 84;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, xStart + col * 18, playerInvY + row * 18) {
                    @Override
                    public boolean mayPickup(Player playerIn) {
                        return !Backpack.isSameBackpack(getItem(), backpackStack);
                    }
                });
            }
        }

        int hotbarY = 142;
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, xStart + col * 18, hotbarY) {
                @Override
                public boolean mayPickup(Player playerIn) {
                    return !Backpack.isSameBackpack(getItem(), backpackStack);
                }
            });
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Backpack.isSameBackpack(player.getMainHandItem(), backpackStack)
                || Backpack.isSameBackpack(player.getOffhandItem(), backpackStack);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < Backpack.INVENTORY_SIZE) {
                if (!this.moveItemStackTo(itemstack1, Backpack.INVENTORY_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else {
                if (itemstack1.getItem() instanceof Backpack) {
                    return ItemStack.EMPTY;
                }

                if (!this.moveItemStackTo(itemstack1, 0, Backpack.INVENTORY_SIZE, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }
}