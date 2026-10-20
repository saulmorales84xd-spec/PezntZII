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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraftforge.items.ItemStackHandler;
import net.saullmc.pezntz.block.custom.CofreCandado;
import net.saullmc.pezntz.menu.CofreCandadoMenu;

import javax.annotation.Nullable;
import java.util.UUID;

public class CofreCandadoEntity extends BlockEntity implements MenuProvider {

    /** Huecos de CADA mitad. Entre las dos suman 54, como un cofre doble. */
    public static final int SLOTS = 27;

    /** Filas del menu entero, las dos mitades juntas. */
    public static final int FILAS = 6;

    public static final int DIGITOS = 4;

    private static final String RELLENO = "0000";

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

    @Nullable
    public BlockPos getPosPareja() {
        BlockState estado = getBlockState();
        if (!(estado.getBlock() instanceof CofreCandado)) return null;

        return this.worldPosition.relative(CofreCandado.direccionPareja(estado));
    }

    @Nullable
    public CofreCandadoEntity getPareja() {
        if (this.level == null) return null;

        BlockPos otra = getPosPareja();
        if (otra == null) return null;

        return this.level.getBlockEntity(otra) instanceof CofreCandadoEntity pareja ? pareja : null;
    }

    public boolean esIzquierda() {
        BlockState estado = getBlockState();
        return estado.hasProperty(CofreCandado.TYPE)
                && estado.getValue(CofreCandado.TYPE) == ChestType.LEFT;
    }

    @Nullable
    public CofreCandadoEntity getIzquierda() {
        if (this.esIzquierda()) return this;
        return this.getPareja();
    }

    @Nullable
    public CofreCandadoEntity getDerecha() {
        if (!this.esIzquierda()) return this;
        return this.getPareja();
    }

    public boolean ponerCodigo(String nuevo) {
        if (nuevo == null || nuevo.length() != DIGITOS) return false;

        for (int i = 0; i < nuevo.length(); i++) {
            if (!Character.isDigit(nuevo.charAt(i))) return false;
        }

        this.guardarCodigo(nuevo);

        CofreCandadoEntity pareja = this.getPareja();
        if (pareja != null) pareja.guardarCodigo(nuevo);

        return true;
    }

    public void quitarCodigo() {
        this.guardarCodigo("");

        CofreCandadoEntity pareja = this.getPareja();
        if (pareja != null) pareja.guardarCodigo("");
    }

    private void guardarCodigo(String valor) {
        this.codigo = valor;
        setChanged();
        sincronizar();
    }

    public boolean codigoCorrecto(String intento) {
        return this.tieneCodigo() && this.codigo.equals(intento);
    }

    public void copiarDesde(ItemStack[] origen) {
        for (int i = 0; i < origen.length && i < SLOTS; i++) {
            if (origen[i] != null && !origen[i].isEmpty()) {
                this.inventario.setStackInSlot(i, origen[i]);
            }
        }
        setChanged();
    }

    public ItemStack[] vaciarYDevolver() {
        ItemStack[] copia = new ItemStack[SLOTS];

        for (int i = 0; i < SLOTS; i++) {
            copia[i] = this.inventario.getStackInSlot(i).copy();
            this.inventario.setStackInSlot(i, ItemStack.EMPTY);
        }

        setChanged();
        return copia;
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

        if (tag.contains("Codigo")) {
            this.codigo = tag.getString("Codigo");
        } else if (tag.contains("TieneCodigo")) {
            this.codigo = tag.getBoolean("TieneCodigo") ? RELLENO : "";
        }

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
        this.codigo = tag.getBoolean("TieneCodigo") ? RELLENO : "";
        this.duenio = tag.hasUUID("Duenio") ? tag.getUUID("Duenio") : null;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}