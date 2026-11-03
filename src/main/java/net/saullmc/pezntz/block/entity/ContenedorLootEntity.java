package net.saullmc.pezntz.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.saullmc.pezntz.loot.TablasLoot;

import javax.annotation.Nullable;

public class ContenedorLootEntity extends RandomizableContainerBlockEntity {

    public static final int HUECOS = 9;

    public static final int MINUTOS_REGENERACION = 30;

    public static final int TICKS_ENTRE_COMPROBACIONES = 20;

    public static final boolean VACIAR_AL_REGENERAR = true;

    public static final boolean OCULTAR_AL_ABRIR = false;

    public static final String SONIDO_ABRIR = "minecraft:block.barrel.open";
    public static final String SONIDO_CERRAR = "minecraft:block.barrel.close";

    public static final float VOLUMEN = 0.5F;
    public static final float TONO = 1.0F;

    public static final int TICKS_REGENERACION = MINUTOS_REGENERACION * 60 * 20;

    private NonNullList<ItemStack> items = NonNullList.withSize(HUECOS, ItemStack.EMPTY);

    private int abiertos = 0;

    private boolean generado = false;

    private boolean saqueado = false;

    private long tiempoRegeneracion = 0L;

    public ContenedorLootEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONTENEDOR_LOOT.get(), pos, state);
    }

    @Override
    public int getContainerSize() {
        return HUECOS;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.pezntz.contenedor");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventarioJugador) {
        return new DispenserMenu(id, inventarioJugador, this);
    }

    private String nombreBloque() {
        ResourceLocation id = ForgeRegistries.BLOCKS.getKey(getBlockState().getBlock());
        return id == null ? "" : id.getPath();
    }

    public void generarSiHaceFalta() {
        if (this.generado || this.level == null || this.level.isClientSide) return;

        this.generado = true;
        TablasLoot.rellenar(this, this.level.getRandom(), nombreBloque());
        setChanged();
    }

    public void regenerar() {
        if (this.level == null || this.level.isClientSide) return;

        if (VACIAR_AL_REGENERAR) {
            this.clearContent();
        }

        TablasLoot.rellenar(this, this.level.getRandom(), nombreBloque());

        this.generado = true;
        this.saqueado = false;
        this.tiempoRegeneracion = 0L;

        setChanged();
        sincronizar();
    }

    public static void tickServidor(Level level, BlockPos pos, BlockState state, ContenedorLootEntity contenedor) {
        if (!contenedor.saqueado || contenedor.tiempoRegeneracion <= 0L) return;
        if (level.getGameTime() % TICKS_ENTRE_COMPROBACIONES != 0L) return;

        if (level.getGameTime() >= contenedor.tiempoRegeneracion) {
            contenedor.regenerar();
        }
    }

    @Override
    public void startOpen(Player jugador) {
        if (this.level == null || this.remove || jugador.isSpectator()) return;

        if (this.abiertos < 0) this.abiertos = 0;
        this.abiertos++;

        if (this.abiertos == 1) sonido(SONIDO_ABRIR);
        if (OCULTAR_AL_ABRIR) marcarSaqueado();
    }

    @Override
    public void stopOpen(Player jugador) {
        if (this.level == null || this.remove || jugador.isSpectator()) return;

        this.abiertos--;

        if (this.abiertos <= 0) {
            this.abiertos = 0;
            sonido(SONIDO_CERRAR);
        }

        if (!OCULTAR_AL_ABRIR) marcarSaqueado();
    }

    private void sonido(String id) {
        if (this.level == null) return;

        SoundEvent evento = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(id));
        if (evento == null) return;

        this.level.playSound(null, this.worldPosition, evento, SoundSource.BLOCKS, VOLUMEN, TONO);
    }

    public boolean estaSaqueado() {
        return this.saqueado;
    }

    public int ticksParaRegenerar() {
        if (this.level == null || this.tiempoRegeneracion <= 0L) return 0;

        long faltan = this.tiempoRegeneracion - this.level.getGameTime();
        return faltan <= 0L ? 0 : (int) faltan;
    }

    private void marcarSaqueado() {
        if (this.saqueado) return;

        this.saqueado = true;
        this.tiempoRegeneracion = this.level == null ? 0L : this.level.getGameTime() + TICKS_REGENERACION;

        setChanged();
        sincronizar();
    }

    private void sincronizar() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items);
        }

        tag.putBoolean("Generado", this.generado);
        tag.putBoolean("Saqueado", this.saqueado);
        tag.putLong("TiempoRegeneracion", this.tiempoRegeneracion);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.items = NonNullList.withSize(HUECOS, ItemStack.EMPTY);

        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }

        this.generado = tag.getBoolean("Generado");
        this.saqueado = tag.getBoolean("Saqueado");
        this.tiempoRegeneracion = tag.getLong("TiempoRegeneracion");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Saqueado", this.saqueado);
        tag.putLong("TiempoRegeneracion", this.tiempoRegeneracion);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.saqueado = tag.getBoolean("Saqueado");
        this.tiempoRegeneracion = tag.getLong("TiempoRegeneracion");
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}