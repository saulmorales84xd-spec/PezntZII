package net.saullmc.pezntz.block.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
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
import net.saullmc.pezntz.capability.BackpackProvider;
import net.saullmc.pezntz.item.custom.Backpack;
import net.saullmc.pezntz.menu.CadaverMenu;

import javax.annotation.Nullable;
import java.util.UUID;

public class CadaverEntity extends BlockEntity implements MenuProvider {

    public static final int FILAS = 6;
    public static final int SLOTS = FILAS * 9;

    public static final int FILAS_MOCHILA = 3;

    public static final boolean SOLTAR_SOBRANTE = true;

    private final ItemStackHandler inventario = new ItemStackHandler(SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final ItemStackHandler mochila = new ItemStackHandler(Backpack.INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final int[] origen = new int[SLOTS];

    private boolean teniaMochila = false;

    private String duenioNombre = "";

    @Nullable
    private UUID duenioUuid = null;

    @Nullable
    private GameProfile perfil = null;

    public CadaverEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CADAVER.get(), pos, state);
    }

    public ItemStackHandler getInventario() {
        return this.inventario;
    }

    public ItemStackHandler getMochila() {
        return this.mochila;
    }

    public boolean teniaMochila() {
        return this.teniaMochila;
    }

    public String getDuenioNombre() {
        return this.duenioNombre;
    }

    @Nullable
    public UUID getDuenioUuid() {
        return this.duenioUuid;
    }

    @Nullable
    public GameProfile getPerfil() {
        return this.perfil;
    }

    public boolean esDuenio(Player player) {
        return this.duenioUuid != null && this.duenioUuid.equals(player.getUUID());
    }

    public boolean estaVacio() {
        return vacio(this.inventario) && vacio(this.mochila);
    }

    private static boolean vacio(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) return false;
        }
        return true;
    }

    public void guardarInventarioDe(Player player) {
        Inventory inv = player.getInventory();

        java.util.Arrays.fill(this.origen, -1);

        int destino = 0;
        int huecos = inv.getContainerSize();

        for (int i = 0; i < huecos && destino < SLOTS; i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            this.inventario.setStackInSlot(destino, stack.copy());
            this.origen[destino] = i;
            destino++;

            inv.setItem(i, ItemStack.EMPTY);
        }

        this.duenioNombre = player.getGameProfile().getName();
        this.duenioUuid = player.getUUID();
        this.perfil = player.getGameProfile();

        setChanged();
        sincronizar();
    }

    public void guardarMochilaDe(Player player, boolean llevabaMochila) {
        this.teniaMochila = llevabaMochila;
        if (!llevabaMochila) return;

        player.getCapability(BackpackProvider.PLAYER_BACKPACK).ifPresent(datos -> {
            ItemStackHandler origen = datos.getInventario();
            int tope = Math.min(origen.getSlots(), this.mochila.getSlots());

            for (int i = 0; i < tope; i++) {
                ItemStack stack = origen.getStackInSlot(i);
                if (stack.isEmpty()) continue;

                this.mochila.setStackInSlot(i, stack.copy());
                origen.setStackInSlot(i, ItemStack.EMPTY);
            }
        });

        setChanged();
        sincronizar();
    }

    public int devolverA(Player player) {
        if (this.level == null || this.level.isClientSide) return 0;

        Inventory inv = player.getInventory();
        int movidos = 0;

        for (int i = 0; i < this.inventario.getSlots(); i++) {
            ItemStack stack = this.inventario.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            int sitio = (i < this.origen.length) ? this.origen[i] : -1;
            if (sitio < 0 || sitio >= inv.getContainerSize()) continue;
            if (!inv.getItem(sitio).isEmpty()) continue;

            inv.setItem(sitio, stack.copy());
            this.inventario.setStackInSlot(i, ItemStack.EMPTY);
            movidos++;
        }

        movidos += vaciarEn(player, this.inventario);
        movidos += devolverMochila(player);

        setChanged();
        sincronizar();

        return movidos;
    }

    private int devolverMochila(Player player) {
        if (vacio(this.mochila)) return 0;

        ItemStackHandler destino = mochilaDe(player);

        if (destino == null) return vaciarEn(player, this.mochila);

        int movidos = 0;
        int tope = Math.min(this.mochila.getSlots(), destino.getSlots());

        for (int i = 0; i < tope; i++) {
            ItemStack stack = this.mochila.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (!destino.getStackInSlot(i).isEmpty()) continue;

            destino.setStackInSlot(i, stack.copy());
            this.mochila.setStackInSlot(i, ItemStack.EMPTY);
            movidos++;
        }

        return movidos + vaciarEn(player, this.mochila);
    }

    @Nullable
    private static ItemStackHandler mochilaDe(Player player) {
        if (!llevaMochila(player)) return null;

        return player.getCapability(BackpackProvider.PLAYER_BACKPACK)
                .map(datos -> datos.getInventario())
                .orElse(null);
    }

    private static boolean llevaMochila(Player player) {
        Inventory inv = player.getInventory();

        for (int i = 0; i < inv.getContainerSize(); i++) {
            if (inv.getItem(i).getItem() instanceof Backpack) return true;
        }
        return false;
    }

    private int vaciarEn(Player player, ItemStackHandler handler) {
        int movidos = 0;

        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            ItemStack copia = stack.copy();
            player.getInventory().add(copia);

            if (copia.isEmpty()) {
                handler.setStackInSlot(i, ItemStack.EMPTY);
                movidos++;

            } else if (SOLTAR_SOBRANTE) {
                player.drop(copia, false);
                handler.setStackInSlot(i, ItemStack.EMPTY);
                movidos++;

            } else {
                handler.setStackInSlot(i, copia);
            }
        }

        return movidos;
    }

    public void soltarTodo() {
        if (this.level == null || this.level.isClientSide) return;

        soltar(this.inventario);
        soltar(this.mochila);
    }

    private void soltar(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            Block.popResource(this.level, this.worldPosition, stack);
            handler.setStackInSlot(i, ItemStack.EMPTY);
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
        return new CadaverMenu(id, playerInventory, this.worldPosition, CadaverMenu.SECCION_CUERPO);
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
        tag.put("Mochila", this.mochila.serializeNBT());
        tag.putBoolean("TeniaMochila", this.teniaMochila);
        tag.putIntArray("Origen", this.origen);
        guardarDuenio(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Inventario")) this.inventario.deserializeNBT(tag.getCompound("Inventario"));
        if (tag.contains("Mochila")) this.mochila.deserializeNBT(tag.getCompound("Mochila"));
        this.teniaMochila = tag.getBoolean("TeniaMochila");

        java.util.Arrays.fill(this.origen, -1);
        int[] guardado = tag.getIntArray("Origen");
        System.arraycopy(guardado, 0, this.origen, 0, Math.min(guardado.length, this.origen.length));

        cargarDuenio(tag);
    }

    private void guardarDuenio(CompoundTag tag) {
        tag.putString("DuenioNombre", this.duenioNombre);
        tag.putBoolean("TeniaMochila", this.teniaMochila);
        if (this.duenioUuid != null) tag.putUUID("DuenioUuid", this.duenioUuid);
        if (this.perfil != null) tag.put("Perfil", NbtUtils.writeGameProfile(new CompoundTag(), this.perfil));
    }

    private void cargarDuenio(CompoundTag tag) {
        this.duenioNombre = tag.getString("DuenioNombre");
        this.duenioUuid = tag.hasUUID("DuenioUuid") ? tag.getUUID("DuenioUuid") : null;
        this.perfil = tag.contains("Perfil") ? NbtUtils.readGameProfile(tag.getCompound("Perfil")) : null;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        guardarDuenio(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}