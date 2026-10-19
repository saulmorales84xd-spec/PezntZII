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
import net.saullmc.pezntz.menu.CofreCandadoMenu;

import javax.annotation.Nullable;
import java.util.UUID;

public class CofreCandadoEntity extends BlockEntity implements MenuProvider {

    public static final int SLOTS = 27;
    public static final int FILAS = 3;

    public static final int DIGITOS = 4;

    private final ItemStackHandler inventario = new ItemStackHandler(SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private String codigo = "";

    @Nullable
    private UUID duenio = null;

    public CofreCandadoEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COFRE_CANDADO.get(), pos, state);
    }

    public ItemStackHandler getInventario() {
        return this.inventario;
    }

    public boolean tieneCodigo() {
        return this.codigo.length() == DIGITOS;
    }

    public boolean esDuenio(Player jugador) {
        return this.duenio == null || this.duenio.equals(jugador.getUUID());
    }

    public void setDuenio(Player jugador) {
        this.duenio = jugador.getUUID();
        setChanged();
    }

    public boolean ponerCodigo(String nuevo) {
        if (nuevo == null || nuevo.length() != DIGITOS) return false;

        for (int i = 0; i < nuevo.length(); i++) {
            if (!Character.isDigit(nuevo.charAt(i))) return false;
        }

        this.codigo = nuevo;
        setChanged();
        sincronizar();
        return true;
    }

    public boolean codigoCorrecto(String intento) {
        return this.tieneCodigo() && this.codigo.equals(intento);
    }

    public void copiarDesde(net.minecraft.world.Container origen) {
        int tope = Math.min(origen.getContainerSize(), SLOTS);

        for (int i = 0; i < tope; i++) {
            ItemStack stack = origen.getItem(i);
            if (stack.isEmpty()) continue;

            this.inventario.setStackInSlot(i, stack.copy());
        }
        setChanged();
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
        return Component.literal("Cofre con candado");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventarioJugador, Player jugador) {
        return new CofreCandadoMenu(id, inventarioJugador, this.worldPosition);
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
        tag.putString("Codigo", this.codigo);
        if (this.duenio != null) tag.putUUID("Duenio", this.duenio);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Inventario")) this.inventario.deserializeNBT(tag.getCompound("Inventario"));
        this.codigo = tag.getString("Codigo");
        this.duenio = tag.hasUUID("Duenio") ? tag.getUUID("Duenio") : null;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("TieneCodigo", this.tieneCodigo());
        if (this.duenio != null) tag.putUUID("Duenio", this.duenio);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.codigo = tag.getBoolean("TieneCodigo") ? "0000" : "";
        this.duenio = tag.hasUUID("Duenio") ? tag.getUUID("Duenio") : null;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}