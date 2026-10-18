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

    public static final int SECCION_CUERPO = 0;
    public static final int SECCION_MOCHILA = 1;

    public static final int ANCHO_GUI = 176;

    private final IItemHandler contenedor;
    private final BlockPos pos;
    private final Level level;
    private final int seccion;
    private final int filas;
    private final int huecosContenedor;

    public CadaverMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(id, playerInventory, extraData.readBlockPos(), extraData.readInt());
    }

    public CadaverMenu(int id, Inventory playerInventory, BlockPos pos, int seccion) {
        super(ModMenuTypes.CADAVER_MENU.get(), id);

        this.pos = pos;
        this.seccion = seccion;
        this.level = playerInventory.player.level();
        this.filas = (seccion == SECCION_MOCHILA) ? CadaverEntity.FILAS_MOCHILA : CadaverEntity.FILAS;

        BlockEntity be = this.level.getBlockEntity(pos);
        if (be instanceof CadaverEntity cadaver) {
            this.contenedor = (seccion == SECCION_MOCHILA) ? cadaver.getMochila() : cadaver.getInventario();
        } else {
            this.contenedor = new ItemStackHandler(this.filas * 9);
        }

        this.huecosContenedor = Math.min(this.filas * 9, this.contenedor.getSlots());
        for (int fila = 0; fila < this.filas; fila++) {
            for (int col = 0; col < 9; col++) {
                int indice = col + fila * 9;
                if (indice >= this.contenedor.getSlots()) break;

                this.addSlot(new SlotItemHandler(this.contenedor, indice,
                        8 + col * 18, 18 + fila * 18));
            }
        }

        int desplazamiento = (this.filas - 4) * 18;

        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + fila * 9 + 9,
                        8 + col * 18, 103 + fila * 18 + desplazamiento));
            }
        }

        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 161 + desplazamiento));
        }

    }

    public int getFilas() {
        return this.filas;
    }

    public int getSeccion() {
        return this.seccion;
    }

    public int getAltoGui() {
        return 114 + this.filas * 18;
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

            if (index < this.huecosContenedor) {
                if (!this.moveItemStackTo(enSlot, this.huecosContenedor, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(enSlot, 0, this.huecosContenedor, false)) {
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