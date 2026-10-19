package net.saullmc.pezntz.entity.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.saullmc.pezntz.item.ModItems;
import net.saullmc.pezntz.item.custom.LlaveVehiculo;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class VehiculoBase extends PathfinderMob {

    public static final int MAX_ASIENTOS = 8;

    private static final List<EntityDataAccessor<Integer>> ASIENTOS = new ArrayList<>();

    static {
        for (int i = 0; i < MAX_ASIENTOS; i++) {
            ASIENTOS.add(SynchedEntityData.defineId(VehiculoBase.class, EntityDataSerializers.INT));
        }
    }

    private static final EntityDataAccessor<Float> COMBUSTIBLE =
            SynchedEntityData.defineId(VehiculoBase.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> ENCENDIDO =
            SynchedEntityData.defineId(VehiculoBase.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> GIRO_RUEDA =
            SynchedEntityData.defineId(VehiculoBase.class, EntityDataSerializers.FLOAT);

    protected static final double VELOCIDAD_CAIDA_MAXIMA = -3.92D;

    protected float velocidadActual;

    private double ultimaX;
    private double ultimaZ;

    protected VehiculoBase(EntityType<? extends PathfinderMob> tipo, Level nivel) {
        super(tipo, nivel);
        this.setMaxUpStep(1.1F);
    }

    public abstract float[][] getAsientos();

    public abstract float getCombustibleMaximo();

    public abstract Item getLlave();

    public boolean combustibleInfinito() {
        return false;
    }

    public boolean esAcuatico() {
        return false;
    }

    public boolean usaCombustible() {
        return this.getCombustibleMaximo() > 0.0F;
    }

    public float getVelocidadMaxima() {
        return 0.6F;
    }

    public float getAceleracion() {
        return 0.02F;
    }

    public float getGiro() {
        return 3.0F;
    }

    /** Combustible gastado por bloque recorrido. */
    public float getGastoPorBloque() {
        return 2.5F;
    }

    /** Lo que repone un bidon. */
    public float getRecargaPorItem() {
        return 250.0F;
    }

    public int getNumeroDeAsientos() {
        return Math.min(this.getAsientos().length, MAX_ASIENTOS);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        for (EntityDataAccessor<Integer> asiento : ASIENTOS) {
            this.entityData.define(asiento, -1);
        }
        this.entityData.define(COMBUSTIBLE, this.getCombustibleMaximo());
        this.entityData.define(ENCENDIDO, false);
        this.entityData.define(GIRO_RUEDA, 0.0F);
    }

    public float getCombustible() {
        return this.entityData.get(COMBUSTIBLE);
    }

    public void setCombustible(float cantidad) {
        this.entityData.set(COMBUSTIBLE, Mth.clamp(cantidad, 0.0F, Math.max(1.0F, this.getCombustibleMaximo())));
    }

    public boolean tieneCombustible() {
        return !this.usaCombustible() || this.combustibleInfinito() || this.getCombustible() > 0.0F;
    }

    public boolean estaEncendido() {
        return this.entityData.get(ENCENDIDO);
    }

    public void setEncendido(boolean encendido) {
        this.entityData.set(ENCENDIDO, encendido);
    }

    public float getGiroRueda() {
        return this.entityData.get(GIRO_RUEDA);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Combustible", this.getCombustible());
        tag.putBoolean("Encendido", this.estaEncendido());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setCombustible(tag.contains("Combustible") ? tag.getFloat("Combustible") : this.getCombustibleMaximo());
        this.setEncendido(tag.getBoolean("Encendido"));
    }

    public int getAsientoDe(Entity entidad) {
        for (int i = 0; i < this.getNumeroDeAsientos(); i++) {
            if (this.entityData.get(ASIENTOS.get(i)) == entidad.getId()) return i;
        }
        return -1;
    }

    public boolean asientoLibre(int indice) {
        if (indice < 0 || indice >= this.getNumeroDeAsientos()) return false;
        return this.entityData.get(ASIENTOS.get(indice)) == -1;
    }

    private void ocupar(int indice, int idEntidad) {
        if (indice >= 0 && indice < MAX_ASIENTOS) {
            this.entityData.set(ASIENTOS.get(indice), idEntidad);
        }
    }

    public boolean cambiarDeAsiento(Entity entidad) {
        int actual = this.getAsientoDe(entidad);
        if (actual < 0) return false;

        int total = this.getNumeroDeAsientos();
        for (int paso = 1; paso < total; paso++) {
            int destino = (actual + paso) % total;
            if (this.asientoLibre(destino)) {
                this.ocupar(actual, -1);
                this.ocupar(destino, entidad.getId());
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean canAddPassenger(Entity pasajero) {
        return this.getPassengers().size() < this.getNumeroDeAsientos();
    }

    @Override
    protected void addPassenger(Entity pasajero) {
        super.addPassenger(pasajero);
        if (this.level().isClientSide) return;

        for (int i = 0; i < this.getNumeroDeAsientos(); i++) {
            if (this.asientoLibre(i)) {
                this.ocupar(i, pasajero.getId());
                return;
            }
        }
    }

    @Override
    protected void removePassenger(Entity pasajero) {
        super.removePassenger(pasajero);
        if (this.level().isClientSide) return;

        int asiento = this.getAsientoDe(pasajero);
        if (asiento >= 0) this.ocupar(asiento, -1);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        int idConductor = this.entityData.get(ASIENTOS.get(0));
        if (idConductor == -1) return null;

        Entity conductor = this.level().getEntity(idConductor);
        return (conductor instanceof LivingEntity vivo && this.hasPassenger(conductor)) ? vivo : null;
    }

    @Override
    protected void positionRider(Entity pasajero, Entity.MoveFunction callback) {
        if (!this.hasPassenger(pasajero)) return;

        int asiento = this.getAsientoDe(pasajero);
        if (asiento < 0) asiento = 0;

        float[][] sitios = this.getAsientos();
        if (asiento >= sitios.length) asiento = sitios.length - 1;

        float adelante = sitios[asiento][0];
        float altura = sitios[asiento][1];
        float lado = sitios[asiento].length > 2 ? sitios[asiento][2] : 0.0F;

        float rot = this.yBodyRot * ((float) Math.PI / 180F);

        double offsetX = Math.sin(-rot) * adelante + Math.cos(rot) * lado;
        double offsetZ = Math.cos(rot) * adelante + Math.sin(rot) * lado;

        callback.accept(pasajero, this.getX() + offsetX, this.getY() + altura, this.getZ() + offsetZ);
    }

    @Override
    public void onPassengerTurned(Entity pasajero) {
        if (this.getAsientoDe(pasajero) != 0) return;

        pasajero.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(pasajero.getYRot() - this.getYRot());
        float f1 = Mth.clamp(f, -105.0F, 105.0F);
        pasajero.yRotO += f1 - f;
        pasajero.setYRot(pasajero.getYRot() + f1 - f);
        pasajero.setYHeadRot(pasajero.getYRot());
    }

    protected void usarLlave(Player jugador, ItemStack llave) {
        UUID mio = this.getUUID();

        if (!LlaveVehiculo.estaEmparejada(llave)) {
            LlaveVehiculo.emparejar(llave, mio);
            this.setEncendido(true);

            jugador.displayClientMessage(
                    Component.literal("Llave emparejada con este vehiculo").withStyle(ChatFormatting.GOLD), true);
            this.level().playSound(null, this.blockPosition(),
                    SoundEvents.IRON_DOOR_OPEN, SoundSource.NEUTRAL, 0.7F, 1.4F);
            return;
        }

        if (!LlaveVehiculo.sirvePara(llave, mio)) {
            jugador.displayClientMessage(
                    Component.literal("Esta llave no es de este vehiculo").withStyle(ChatFormatting.RED), true);
            this.level().playSound(null, this.blockPosition(),
                    SoundEvents.IRON_DOOR_CLOSE, SoundSource.NEUTRAL, 0.5F, 0.8F);
            return;
        }

        boolean encender = !this.estaEncendido();
        this.setEncendido(encender);

        jugador.displayClientMessage(
                Component.literal(encender ? "Motor encendido" : "Motor apagado")
                        .withStyle(encender ? ChatFormatting.GREEN : ChatFormatting.GRAY), true);

        this.level().playSound(null, this.blockPosition(),
                encender ? SoundEvents.DISPENSER_LAUNCH : SoundEvents.DISPENSER_FAIL,
                SoundSource.NEUTRAL, 0.8F, encender ? 1.2F : 0.9F);
    }

    @Override
    public InteractionResult mobInteract(Player jugador, InteractionHand mano) {
        ItemStack enMano = jugador.getItemInHand(mano);

        if (enMano.getItem() instanceof LlaveVehiculo) {
            if (!this.level().isClientSide()) {
                if (enMano.is(this.getLlave())) {
                    usarLlave(jugador, enMano);
                } else {
                    jugador.displayClientMessage(
                            Component.literal("Esa no es la llave de este vehiculo").withStyle(ChatFormatting.RED), true);
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }

        if (this.usaCombustible() && !this.combustibleInfinito()
                && enMano.is(ModItems.BIDON_GASOLINA.get()) && this.getCombustible() < this.getCombustibleMaximo()) {

            if (!this.level().isClientSide()) {
                this.setCombustible(this.getCombustible() + this.getRecargaPorItem());

                if (!jugador.getAbilities().instabuild) {
                    enMano.shrink(1);
                }

                this.level().playSound(null, this.blockPosition(),
                        SoundEvents.BUCKET_FILL, SoundSource.NEUTRAL, 0.8F, 1.2F);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }

        if (this.asientoLibre(0)) {
            jugador.setYRot(this.getYRot());
            jugador.setXRot(0.0F);
            jugador.setYBodyRot(this.getYRot());
            jugador.setYHeadRot(this.getYRot());
        }

        if (!this.level().isClientSide) {
            jugador.startRiding(this);
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) return;

        if (this.usaCombustible() && !this.combustibleInfinito()
                && this.isVehicle() && this.getCombustible() > 0.0F) {

            double dx = this.getX() - this.ultimaX;
            double dz = this.getZ() - this.ultimaZ;
            double avance = Math.sqrt(dx * dx + dz * dz);

            if (avance > 0.02D) {
                this.setCombustible(this.getCombustible() - (float) (avance * this.getGastoPorBloque()));
            }
        }

        this.ultimaX = this.getX();
        this.ultimaZ = this.getZ();
    }

    protected double calcularMovimientoVertical() {
        return Math.max(this.getDeltaMovement().y - 0.08D, VELOCIDAD_CAIDA_MAXIMA);
    }

    protected boolean puedeMoverse() {
        return true;
    }

    @Override
    public void travel(Vec3 travelVector) {
        LivingEntity conductor = this.getControllingPassenger();

        if (!this.esAcuatico() && this.isUnderWater()) {
            this.velocidadActual = 0.0F;
            super.travel(travelVector);
            return;
        }

        if (this.isVehicle() && conductor != null) {

            float direccion = conductor.xxa;
            float acelerador = conductor.zza;

            if (!this.estaEncendido() || !this.tieneCombustible() || !this.puedeMoverse()) {
                acelerador = 0.0F;
                this.velocidadActual *= 0.90F;
            }

            float maxima = this.getVelocidadMaxima();
            float acel = this.getAceleracion();

            if (acelerador > 0) this.velocidadActual = Mth.clamp(this.velocidadActual + acel, -maxima * 0.5F, maxima);
            else if (acelerador < 0) this.velocidadActual = Mth.clamp(this.velocidadActual - acel, -maxima * 0.5F, maxima);
            else this.velocidadActual *= 0.92F;

            if (Math.abs(this.velocidadActual) > 0.01F) {
                float giro = this.getGiro() * Math.signum(this.velocidadActual);
                this.setYRot(this.getYRot() - direccion * giro);
            }

            this.yRotO = this.getYRot();
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();

            Vec3 adelante = Vec3.directionFromRotation(0, this.getYRot()).scale(this.velocidadActual);

            this.entityData.set(GIRO_RUEDA, this.entityData.get(GIRO_RUEDA) + this.velocidadActual);

            this.setDeltaMovement(adelante.x, this.calcularMovimientoVertical(), adelante.z);
            this.move(MoverType.SELF, this.getDeltaMovement());
            return;
        }

        super.travel(travelVector);
    }

    @Override
    protected void registerGoals() {
    }

    @Override
    public boolean showVehicleHealth() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState estado) {
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}