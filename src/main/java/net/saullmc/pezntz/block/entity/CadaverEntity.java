package net.saullmc.pezntz.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.saullmc.pezntz.menu.CadaverMenu;

import javax.annotation.Nullable;
import java.util.UUID;

public class CadaverEntity extends BlockEntity implements MenuProvider {

    public static final int FILAS = 6;
    public static final int SLOTS = FILAS * 9;

    private final ItemStackHandler inventario = new ItemStackHandler(SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private String duenioNombre = "";

    @Nullable
    private UUID duenioUuid = null;

    public CadaverEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CADAVER.get(), pos, state);
    }

    public ItemStackHandler getInventario() {
        return this.inventario;
    }

    public String getDuenioNombre() {
        return this.duenioNombre;
    }

    @Nullable
    public UUID getDuenioUuid() {
        return this.duenioUuid;
    }

    public boolean esDuenio(Player player) {
        return this.duenioUuid != null && this.duenioUuid.equals(player.getUUID());
    }

    public boolean estaVacio() {
        for (int i = 0; i < SLOTS; i++) {
            if (!this.inventario.getStackInSlot(i).isEmpty()) return false;
        }
        return true;
    }

    public void guardarInventarioDe(Player player) {
        Inventory inv = player.getInventory();
        int destino = 0;

        destino = volcar(inv.items, destino);
        destino = volcar(inv.armor, destino);
        destino = volcar(inv.offhand, destino);

        this.duenioNombre = player.getGameProfile().getName();
        this.duenioUuid = player.getUUID();

        setChanged();
        sincronizar();
    }

    private int volcar(java.util.List<ItemStack> origen, int destino) {
        for (int i = 0; i < origen.size(); i++) {
            ItemStack stack = origen.get(i);
            if (stack.isEmpty()) continue;
            if (destino >= SLOTS) break;

            this.inventario.setStackInSlot(destino++, stack.copy());
            origen.set(i, ItemStack.EMPTY);
        }
        return destino;
    }

    public void soltarTodo() {
        if (this.level == null || this.level.isClientSide) return;

        for (int i = 0; i < SLOTS; i++) {
            ItemStack stack = this.inventario.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            Block.popResource(this.level, this.worldPosition, stack);
            this.inventario.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public Component getDisplayName() {
        return this.duenioNombre.isEmpty()
                ? Component.literal("Cadaver")
                : Component.literal("Cadaver de " + this.duenioNombre);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new CadaverMenu(id, playerInventory, this.worldPosition);
    }

    private void sincronizar() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventario", this.inventario.serializeNBT());
        tag.putString("DuenioNombre", this.duenioNombre);
        if (this.duenioUuid != null) {
            tag.putUUID("DuenioUuid", this.duenioUuid);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Inventario")) {
            this.inventario.deserializeNBT(tag.getCompound("Inventario"));
        }
        this.duenioNombre = tag.getString("DuenioNombre");
        this.duenioUuid = tag.hasUUID("DuenioUuid") ? tag.getUUID("DuenioUuid") : null;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("DuenioNombre", this.duenioNombre);
        if (this.duenioUuid != null) {
            tag.putUUID("DuenioUuid", this.duenioUuid);
        }
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}