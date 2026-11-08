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
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.loot.TablasLoot;
import net.saullmc.pezntz.network.DropWaypointPacket;
import net.saullmc.pezntz.network.NetworkHandler;

import javax.annotation.Nullable;

public class DropEntity extends RandomizableContainerBlockEntity {

    public static final int HUECOS = 54;

    public static final int MINUTOS_ESPERA = 3;

    public static final int TICKS_ESPERA = MINUTOS_ESPERA * 60 * 20;

    public static final String TABLA = "caja_militar";

    public static final boolean DESAPARECE_AL_VACIAR = true;

    public static final int TICKS_ENTRE_AVISOS = 40;

    public static final boolean FORZAR_CHUNK = true;

    public static final String SONIDO_ABRIR = "minecraft:block.barrel.open";
    public static final String SONIDO_CERRAR = "minecraft:block.barrel.close";
    public static final String SONIDO_CERRADO = "minecraft:block.iron_door.close";

    public static final float VOLUMEN = 0.7F;
    public static final float TONO = 1.0F;

    private NonNullList<ItemStack> items = NonNullList.withSize(HUECOS, ItemStack.EMPTY);

    private int abiertos = 0;

    private boolean generado = false;

    private boolean abierto = false;

    private long tiempoApertura = 0L;

    public DropEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DROP.get(), pos, state);
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
        return Component.translatable("container.pezntz.drop");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventarioJugador) {
        return ChestMenu.sixRows(id, inventarioJugador, this);
    }

    public void aterrizar() {
        if (this.level == null || this.level.isClientSide) return;

        this.tiempoApertura = this.level.getGameTime() + TICKS_ESPERA;
        this.abierto = false;

        generarSiHaceFalta();

        setChanged();
        sincronizar();
        avisar(false);
        forzarChunk(true);
    }

    public void generarSiHaceFalta() {
        if (this.generado || this.level == null || this.level.isClientSide) return;

        this.generado = true;
        TablasLoot.rellenar(this, this.level.getRandom(), TABLA);
        setChanged();
    }

    public boolean puedeAbrirse() {
        if (this.level == null) return true;
        if (this.tiempoApertura <= 0L) return true;

        return this.level.getGameTime() >= this.tiempoApertura;
    }

    public int ticksParaAbrir() {
        if (this.level == null || this.tiempoApertura <= 0L) return 0;

        long faltan = this.tiempoApertura - this.level.getGameTime();
        return faltan <= 0L ? 0 : (int) faltan;
    }

    public long getTiempoApertura() {
        return this.tiempoApertura;
    }

    public boolean estaAbierto() {
        return this.abierto;
    }

    public void sonarCerrado() {
        sonido(SONIDO_CERRADO, 0.6F, 1.4F);
    }

    public static void tickServidor(Level level, BlockPos pos, BlockState state, DropEntity drop) {
        if (level.getGameTime() % TICKS_ENTRE_AVISOS != 0L) return;

        if (!drop.abierto) {
            drop.avisar(false);
            return;
        }

        if (DESAPARECE_AL_VACIAR && drop.abiertos <= 0 && drop.isEmpty()) {
            level.removeBlock(pos, false);
        }
    }

    @Override
    public void startOpen(Player jugador) {
        if (this.level == null || this.remove || jugador.isSpectator()) return;

        if (this.abiertos < 0) this.abiertos = 0;
        this.abiertos++;

        if (this.abiertos == 1) sonido(SONIDO_ABRIR, VOLUMEN, TONO);

        if (!this.abierto) {
            this.abierto = true;

            setChanged();
            sincronizar();
            avisar(true);
            forzarChunk(false);
        }
    }

    @Override
    public void stopOpen(Player jugador) {
        if (this.level == null || this.remove || jugador.isSpectator()) return;

        this.abiertos--;

        if (this.abiertos <= 0) {
            this.abiertos = 0;
            sonido(SONIDO_CERRAR, VOLUMEN, TONO);
        }
    }

    public void avisar(boolean quitar) {
        if (this.level == null || this.level.isClientSide) return;

        NetworkHandler.sendToDimension(
                new DropWaypointPacket(this.worldPosition, this.tiempoApertura, quitar),
                this.level.dimension());
    }

    public void forzarChunk(boolean activar) {
        if (!FORZAR_CHUNK) return;
        if (!(this.level instanceof ServerLevel servidor)) return;

        ForgeChunkManager.forceChunk(servidor, PezntZMod.MOD_ID, this.worldPosition,
                this.worldPosition.getX() >> 4, this.worldPosition.getZ() >> 4,
                activar, true);
    }

    private void sonido(String id, float volumen, float tono) {
        if (this.level == null) return;

        SoundEvent evento = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(id));
        if (evento == null) return;

        this.level.playSound(null, this.worldPosition, evento, SoundSource.BLOCKS, volumen, tono);
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
        tag.putBoolean("Abierto", this.abierto);
        tag.putLong("TiempoApertura", this.tiempoApertura);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.items = NonNullList.withSize(HUECOS, ItemStack.EMPTY);

        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }

        this.generado = tag.getBoolean("Generado");
        this.abierto = tag.getBoolean("Abierto");
        this.tiempoApertura = tag.getLong("TiempoApertura");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Abierto", this.abierto);
        tag.putLong("TiempoApertura", this.tiempoApertura);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.abierto = tag.getBoolean("Abierto");
        this.tiempoApertura = tag.getLong("TiempoApertura");
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}