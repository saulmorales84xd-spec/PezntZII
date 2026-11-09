package net.saullmc.pezntz.entity.custom;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.network.NetworkHooks;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.loot.TablasLoot;
import net.saullmc.pezntz.network.DropWaypointPacket;
import net.saullmc.pezntz.network.NetworkHandler;

import java.util.ArrayList;
import java.util.List;

public class DropCaja extends Entity {

    public static final int HUECOS = 54;

    public static final int MINUTOS_ESPERA = 3;

    public static final int TICKS_ESPERA = MINUTOS_ESPERA * 60 * 20;

    public static final String TABLA = "caja_militar";

    public static final String NOMBRE_INVENTARIO = "Airdrop";

    public static final int SEGUNDOS_PARA_DESAPARECER = 60;

    public static final int TICKS_ENTRE_AVISOS = 40;

    public static final boolean QUITAR_WAYPOINT_AL_ABRIR = true;

    public static final boolean FORZAR_CHUNK = true;

    public static final boolean SE_PUEDE_EMPUJAR = true;

    public static final boolean EMPUJAN_LOS_MOBS = false;

    public static final double FUERZA_EMPUJE = 0.06D;

    public static final double MARGEN_EMPUJE = 0.08D;

    public static final double GRAVEDAD = 0.04D;

    public static final double DISTANCIA_PARA_USAR = 8.0D;

    public static final String SONIDO_ABRIR = "minecraft:block.barrel.open";
    public static final String SONIDO_CERRAR = "minecraft:block.barrel.close";
    public static final String SONIDO_CERRADO = "minecraft:block.iron_door.close";

    public static final float VOLUMEN = 0.7F;
    public static final float TONO = 1.0F;

    private static final long SIN_CHUNK = Long.MIN_VALUE;

    private final Contenido contenido = new Contenido();

    private boolean generado = false;

    private boolean abierto = false;

    private long tiempoApertura = 0L;

    private long vacioDesde = -1L;

    private int mirando = 0;

    private long chunkForzado = SIN_CHUNK;

    public DropCaja(EntityType<? extends DropCaja> tipo, Level nivel) {
        super(tipo, nivel);
        this.blocksBuilding = true;
    }

    public void preparar() {
        if (this.level().isClientSide) return;

        this.tiempoApertura = this.level().getGameTime() + TICKS_ESPERA;
        this.abierto = false;

        generar();
        avisar(false);
        forzarChunk();
    }

    private void generar() {
        if (this.generado) return;

        this.generado = true;
        TablasLoot.rellenar(this.contenido, this.level().getRandom(), TABLA);
    }

    public boolean puedeAbrirse() {
        return this.tiempoApertura <= 0L || this.level().getGameTime() >= this.tiempoApertura;
    }

    public long getTiempoApertura() {
        return this.tiempoApertura;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 movimiento = this.getDeltaMovement();

        if (!this.isNoGravity()) movimiento = movimiento.add(0.0D, -GRAVEDAD, 0.0D);
        if (!this.level().isClientSide) movimiento = movimiento.add(empuje());

        this.setDeltaMovement(movimiento);
        this.move(MoverType.SELF, movimiento);

        float friccion = 0.98F;
        if (this.onGround()) {
            friccion = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement())
                    .getBlock().getFriction() * 0.98F;
        }

        Vec3 frenado = this.getDeltaMovement().multiply(friccion, 0.98D, friccion);
        if (Math.abs(frenado.x) < 1.0E-4D) frenado = new Vec3(0.0D, frenado.y, frenado.z);
        if (Math.abs(frenado.z) < 1.0E-4D) frenado = new Vec3(frenado.x, frenado.y, 0.0D);
        this.setDeltaMovement(frenado);

        if (this.level().isClientSide) return;

        long ahora = this.level().getGameTime();

        if (!this.abierto || !QUITAR_WAYPOINT_AL_ABRIR) {
            if (ahora % TICKS_ENTRE_AVISOS == 0L) avisar(false);
            forzarChunk();
        }

        if (!this.abierto) return;

        if (this.contenido.isEmpty()) {
            if (this.vacioDesde < 0L) {
                this.vacioDesde = ahora;
            } else if (ahora - this.vacioDesde >= SEGUNDOS_PARA_DESAPARECER * 20L) {
                this.discard();
            }
        } else {
            this.vacioDesde = -1L;
        }
    }

    private Vec3 empuje() {
        if (!SE_PUEDE_EMPUJAR) return Vec3.ZERO;

        AABB zona = this.getBoundingBox().inflate(MARGEN_EMPUJE, -0.05D, MARGEN_EMPUJE);

        List<LivingEntity> cerca = new ArrayList<>();
        if (EMPUJAN_LOS_MOBS) {
            cerca.addAll(this.level().getEntitiesOfClass(LivingEntity.class, zona, e -> e.isAlive() && !e.isSpectator()));
        } else {
            cerca.addAll(this.level().getEntitiesOfClass(Player.class, zona, e -> e.isAlive() && !e.isSpectator()));
        }

        double x = 0.0D;
        double z = 0.0D;

        for (LivingEntity e : cerca) {
            double dx = this.getX() - e.getX();
            double dz = this.getZ() - e.getZ();
            double largo = Math.sqrt(dx * dx + dz * dz);
            if (largo < 1.0E-4D) continue;

            x += dx / largo;
            z += dz / largo;
        }

        double largo = Math.sqrt(x * x + z * z);
        if (largo < 1.0E-4D) return Vec3.ZERO;

        return new Vec3(x / largo * FUERZA_EMPUJE, 0.0D, z / largo * FUERZA_EMPUJE);
    }

    @Override
    public InteractionResult interact(Player jugador, InteractionHand mano) {
        if (this.level().isClientSide) return InteractionResult.SUCCESS;

        if (!puedeAbrirse()) {
            sonido(SONIDO_CERRADO, 0.6F, 1.4F);
            return InteractionResult.CONSUME;
        }

        generar();

        jugador.openMenu(new SimpleMenuProvider(
                (id, inventario, quien) -> ChestMenu.sixRows(id, inventario, this.contenido),
                Component.literal(NOMBRE_INVENTARIO)));

        return InteractionResult.CONSUME;
    }

    private void alAbrir(Player jugador) {
        if (jugador.isSpectator()) return;

        this.mirando++;
        if (this.mirando == 1) sonido(SONIDO_ABRIR, VOLUMEN, TONO);

        if (!this.abierto) {
            this.abierto = true;

            if (QUITAR_WAYPOINT_AL_ABRIR) {
                avisar(true);
                soltarChunk();
            }
        }
    }

    private void alCerrar(Player jugador) {
        if (jugador.isSpectator()) return;

        this.mirando = Math.max(0, this.mirando - 1);
        if (this.mirando == 0) sonido(SONIDO_CERRAR, VOLUMEN, TONO);
    }

    private void avisar(boolean quitar) {
        if (this.level().isClientSide) return;

        NetworkHandler.sendToDimension(new DropWaypointPacket(this.getId(),
                this.getX(), this.getY() + this.getBbHeight(), this.getZ(),
                this.tiempoApertura, quitar), this.level().dimension());
    }

    private void forzarChunk() {
        if (!FORZAR_CHUNK) return;
        if (!(this.level() instanceof ServerLevel servidor)) return;

        ChunkPos aqui = this.chunkPosition();
        long actual = aqui.toLong();
        if (actual == this.chunkForzado) return;

        soltarChunk();

        ForgeChunkManager.forceChunk(servidor, PezntZMod.MOD_ID, this, aqui.x, aqui.z, true, true);
        this.chunkForzado = actual;
    }

    private void soltarChunk() {
        if (this.chunkForzado == SIN_CHUNK) return;
        if (!(this.level() instanceof ServerLevel servidor)) return;

        ChunkPos viejo = new ChunkPos(this.chunkForzado);
        ForgeChunkManager.forceChunk(servidor, PezntZMod.MOD_ID, this, viejo.x, viejo.z, false, true);
        this.chunkForzado = SIN_CHUNK;
    }

    @Override
    public void remove(RemovalReason motivo) {
        if (!this.level().isClientSide && motivo.shouldDestroy()) {
            avisar(true);
            soltarChunk();

            if (motivo == RemovalReason.KILLED) {
                Containers.dropContents(this.level(), this, this.contenido);
            }
        }

        super.remove(motivo);
    }

    private void sonido(String id, float volumen, float tono) {
        SoundEvent evento = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(id));
        if (evento == null) return;

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), evento, SoundSource.BLOCKS, volumen, tono);
    }

    @Override
    public boolean hurt(DamageSource fuente, float cantidad) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource fuente) {
        return true;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean skipAttackInteraction(Entity atacante) {
        return true;
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.generado = tag.getBoolean("Generado");
        this.abierto = tag.getBoolean("Abierto");
        this.tiempoApertura = tag.getLong("TiempoApertura");
        this.vacioDesde = tag.contains("VacioDesde") ? tag.getLong("VacioDesde") : -1L;
        this.chunkForzado = tag.contains("ChunkForzado") ? tag.getLong("ChunkForzado") : SIN_CHUNK;

        this.contenido.clearContent();

        ListTag lista = tag.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < lista.size(); i++) {
            CompoundTag item = lista.getCompound(i);
            int hueco = item.getByte("Slot") & 255;

            if (hueco < HUECOS) this.contenido.setItem(hueco, ItemStack.of(item));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("Generado", this.generado);
        tag.putBoolean("Abierto", this.abierto);
        tag.putLong("TiempoApertura", this.tiempoApertura);
        tag.putLong("VacioDesde", this.vacioDesde);
        if (this.chunkForzado != SIN_CHUNK) tag.putLong("ChunkForzado", this.chunkForzado);

        ListTag lista = new ListTag();

        for (int i = 0; i < HUECOS; i++) {
            ItemStack stack = this.contenido.getItem(i);
            if (stack.isEmpty()) continue;

            CompoundTag item = new CompoundTag();
            item.putByte("Slot", (byte) i);
            stack.save(item);
            lista.add(item);
        }

        tag.put("Items", lista);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private final class Contenido extends SimpleContainer {

        Contenido() {
            super(HUECOS);
        }

        @Override
        public boolean stillValid(Player jugador) {
            return DropCaja.this.isAlive()
                    && jugador.distanceToSqr(DropCaja.this) <= DISTANCIA_PARA_USAR * DISTANCIA_PARA_USAR;
        }

        @Override
        public void startOpen(Player jugador) {
            DropCaja.this.alAbrir(jugador);
        }

        @Override
        public void stopOpen(Player jugador) {
            DropCaja.this.alCerrar(jugador);
        }
    }
}