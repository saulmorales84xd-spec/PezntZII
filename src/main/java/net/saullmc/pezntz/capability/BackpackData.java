package net.saullmc.pezntz.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;
import net.saullmc.pezntz.item.custom.Backpack;

public class BackpackData {

    private final ItemStackHandler inventario = new ItemStackHandler(Backpack.INVENTORY_SIZE);

    public ItemStackHandler getInventario() {
        return this.inventario;
    }

    public void copyFrom(BackpackData origen) {
        this.inventario.deserializeNBT(origen.inventario.serializeNBT());
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.put("BackpackInventory", this.inventario.serializeNBT());
    }

    public void loadNBTData(CompoundTag nbt) {
        if (nbt.contains("BackpackInventory")) {
            this.inventario.deserializeNBT(nbt.getCompound("BackpackInventory"));
        }
    }
}