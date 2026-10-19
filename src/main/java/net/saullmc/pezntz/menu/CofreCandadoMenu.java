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
import net.saullmc.pezntz.block.entity.CofreCandadoEntity;
import net.saullmc.pezntz.init.ModMenuTypes;

public class CofreCandadoMenu extends AbstractContainerMenu {

    public static final int ANCHO_GUI = 176;

    private final IItemHandler contenedor;
    private final BlockPos pos;
    private final Level level;

    public CofreCandadoMenu(int id, Inventory inventarioJugador, FriendlyByteBuf extraData) {
        this(id, inventarioJugador, extraData.readBlockPos());
    }

    public CofreCandadoMenu(int id, Inventory inventarioJugador, BlockPos pos) {
        super(ModMenuTypes.COFRE_CANDADO_MENU.get(), id);

        this.pos = pos;
        this.level = inventarioJugador.player.level();

        BlockEntity be = this.level.getBlockEntity(pos);
        this.contenedor = (be instanceof CofreCandadoEntity cofre)
                ? cofre.getInventario()
                : new ItemStackHandler(CofreCandadoEntity.SLOTS);

        for (int fila = 0; fila < CofreCandadoEntity.FILAS; fila++) {
            for (int col = 0; col < 9; col++) {
                int indice = col + fila * 9;
                if (indice >= this.contenedor.getSlots()) break;

                this.addSlot(new SlotItemHandler(this.contenedor, indice, 8 + col * 18, 18 + fila * 18));
            }
        }

        int desplazamiento = (CofreCandadoEntity.FILAS - 4) * 18;

        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inventarioJugador, col + fila * 9 + 9,
                        8 + col * 18, 103 + fila * 18 + desplazamiento));
            }
        }

        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inventarioJugador, col, 8 + col * 18, 161 + desplazamiento));
        }
    }

    public int getAltoGui() {
        return 114 + CofreCandadoEntity.FILAS * 18;
    }

    public int getFilas() {
        return CofreCandadoEntity.FILAS;
    }

    @Override
    public boolean stillValid(Player jugador) {
        if (!(this.level.getBlockEntity(this.pos) instanceof CofreCandadoEntity)) return false;

        return jugador.distanceToSqr(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public ItemStack quickMoveStack(Player jugador, int index) {
        ItemStack resultado = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack enSlot = slot.getItem();
            resultado = enSlot.copy();

            if (index < CofreCandadoEntity.SLOTS) {
                if (!this.moveItemStackTo(enSlot, CofreCandadoEntity.SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(enSlot, 0, CofreCandadoEntity.SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (enSlot.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return resultado;
    }
}