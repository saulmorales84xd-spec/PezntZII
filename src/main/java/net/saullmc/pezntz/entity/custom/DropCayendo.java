package net.saullmc.pezntz.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import net.saullmc.pezntz.block.ModBlocks;
import net.saullmc.pezntz.block.entity.DropEntity;

public class DropCayendo extends Entity {

    public static final double VELOCIDAD = 0.10D;

    public static final int ALTURA_CAIDA = 40;

    public static final int TICKS_MAXIMOS = 20 * 60 * 10;

    public static final String SONIDO_ATERRIZAJE = "minecraft:block.anvil.land";

    public static final float VOLUMEN = 1.0F;
    public static final float TONO = 0.7F;

    private static final EntityDataAccessor<Integer> SUELO =
            SynchedEntityData.defineId(DropCayendo.class, EntityDataSerializers.INT);

    private int edad = 0;

    public DropCayendo(EntityType<?> tipo, Level nivel) {
        super(tipo, nivel);

        this.noPhysics = true;
        this.noCulling = true;
        this.setNoGravity(true);
    }

    public void prepararCaida(BlockPos destino, int altura) {
        this.entityData.set(SUELO, destino.getY());

        this.setPos(destino.getX() + 0.5D,
                destino.getY() + altura,
                destino.getZ() + 0.5D);

        guardarPosicionAnterior();
    }

    private void guardarPosicionAnterior() {
        this.xOld = this.getX();
        this.yOld = this.getY();
        this.zOld = this.getZ();
    }

    public int getSuelo() {
        return this.entityData.get(SUELO);
    }

    @Override
    public void tick() {
        this.edad++;

        if (this.edad > TICKS_MAXIMOS) {
            this.discard();
            return;
        }

        guardarPosicionAnterior();

        double suelo = this.getSuelo();
        double siguiente = this.getY() - VELOCIDAD;

        if (siguiente <= suelo) {
            this.setPos(this.getX(), suelo, this.getZ());

            if (!this.level().isClientSide) {
                aterrizar(BlockPos.containing(this.getX(), suelo, this.getZ()));
            }
            return;
        }

        this.setPos(this.getX(), siguiente, this.getZ());
    }

    private void aterrizar(BlockPos pos) {
        Level nivel = this.level();

        BlockState estado = ModBlocks.DROP.get().defaultBlockState();
        nivel.setBlock(pos, estado, Block.UPDATE_ALL);

        if (nivel.getBlockEntity(pos) instanceof DropEntity drop) {
            drop.aterrizar();
        }

        SoundEvent evento = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(SONIDO_ATERRIZAJE));
        if (evento != null) {
            nivel.playSound(null, pos, evento, SoundSource.BLOCKS, VOLUMEN, TONO);
        }

        this.discard();
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(SUELO, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(SUELO, tag.getInt("Suelo"));
        this.edad = tag.getInt("Edad");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Suelo", this.getSuelo());
        tag.putInt("Edad", this.edad);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distancia) {
        return true;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
