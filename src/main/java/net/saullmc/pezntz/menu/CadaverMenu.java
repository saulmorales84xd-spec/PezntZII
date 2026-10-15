package net.saullmc.pezntz.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.saullmc.pezntz.block.entity.CadaverEntity;
import net.saullmc.pezntz.init.ModMenuTypes;


public class CadaverMenu extends AbstractContainerMenu {

    public static final int ANCHO_GUI = 176;
    public static final int ALTO_GUI = 222;

    private final IItemHandler inventario;
    private final BlockPos pos;
    private final Level level;

    public CadaverMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(id, playerInventory, extraData.readBlockPos());
    }

    public CadaverMenu(int id, Inventory playerInventory, BlockPos pos) {
        super(ModMenuTypes.CADAVER_MENU.get(), id);

        this.pos = pos;
        this.level = playerInventory.player.level();

        BlockEntity be = this.level.getBlockEntity(pos);
        this.inventario = (be instanceof CadaverEntity cadaver)
                ? cadaver.getInventario()
                : new ItemStackHandler(CadaverEntity.SLOTS);

        for (int fila = 0; fila < CadaverEntity.FILAS; fila++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new SlotItemHandler(this.inventario, col + fila * 9,
                        8 + col * 18, 18 + fila * 18));
            }
        }

        int invY = 139;
        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + fila * 9 + 9,
                        8 + col * 18, invY + fila * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 197));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (!(this.level.getBlockEntity(this.pos) instanceof CadaverEntity)) return false;

        return player.distanceToSqr(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack resultado = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack enSlot = slot.getItem();
            resultado = enSlot.copy();

            if (index < CadaverEntity.SLOTS) {
                if (!this.moveItemStackTo(enSlot, CadaverEntity.SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(enSlot, 0, CadaverEntity.SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (enSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return resultado;
    }
}