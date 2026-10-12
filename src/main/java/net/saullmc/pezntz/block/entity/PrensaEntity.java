package net.saullmc.pezntz.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.block.custom.Prensa;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class PrensaEntity extends BlockEntity implements GeoBlockEntity {

    public static final int TICKS_ANIMACION = 60;

    public static final int TICKS_MARGEN = 2;

    public static final String ID_CHATARRA = "chatarra";
    public static final String ID_CHATARRA_REFORZADA = "chatarra_reforzada";

    public static final String ANIM_IDLE = "idle";
    public static final String ANIM_PROCESAR = "procesar";

    public static final int CANTIDAD_RESULTADO = 1;

    public static final boolean AVISAR_SI_FALTA_CHATARRA = true;

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop(ANIM_IDLE);
    private static final RawAnimation PROCESAR = RawAnimation.begin().thenPlay(ANIM_PROCESAR);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private int chatarra = 0;

    private int clicks = 0;

    private boolean procesando = false;

    private int ticksAnim = 0;

    public PrensaEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PRENSA.get(), pos, state);
    }

    public int getChatarra() {
        return this.chatarra;
    }

    public int getClicks() {
        return this.clicks;
    }

    public boolean isProcesando() {
        return this.procesando;
    }

    public boolean estaLleno() {
        return this.chatarra >= Prensa.MAX_CHATARRA;
    }

    public float getProgresoClicks() {
        if (this.procesando) return 1.0F;
        return Math.min(1.0F, this.clicks / (float) Prensa.CLICKS_NECESARIOS);
    }

    public InteractionResult alHacerClick(ServerPlayer jugador, ItemStack enMano) {
        if (this.level == null) return InteractionResult.PASS;

        if (this.procesando) {
            return InteractionResult.CONSUME;
        }

        if (this.chatarra < Prensa.MAX_CHATARRA) {
            Item chatarraItem = itemPorNombre(ID_CHATARRA);

            if (!enMano.isEmpty() && enMano.is(chatarraItem)) {
                if (!jugador.isCreative()) {
                    enMano.shrink(1);
                }
                this.chatarra++;
                sonido(SoundEvents.ITEM_FRAME_ADD_ITEM, 0.9F, 1.0F + (this.chatarra * 0.05F));
                sincronizar();
                return InteractionResult.CONSUME;
            }

            if (AVISAR_SI_FALTA_CHATARRA) {
                jugador.displayClientMessage(Component.literal(
                        "Faltan " + (Prensa.MAX_CHATARRA - this.chatarra) + " de chatarra"), true);
            }
            return InteractionResult.CONSUME;
        }

        this.clicks++;
        sonido(SoundEvents.ANVIL_USE, 0.6F, 0.9F + (this.clicks * 0.08F));

        if (this.clicks >= Prensa.CLICKS_NECESARIOS) {
            this.procesando = true;
            this.ticksAnim = TICKS_ANIMACION + TICKS_MARGEN;
        }

        sincronizar();
        return InteractionResult.CONSUME;
    }

    public void tick() {
        if (this.level == null || this.level.isClientSide) return;
        if (!this.procesando) return;

        this.ticksAnim--;
        if (this.ticksAnim > 0) return;

        this.procesando = false;
        this.chatarra = 0;
        this.clicks = 0;

        Item resultado = itemPorNombre(ID_CHATARRA_REFORZADA);
        if (resultado != Items.AIR) {
            Block.popResource(this.level, this.worldPosition.above(),
                    new ItemStack(resultado, CANTIDAD_RESULTADO));
        }

        sonido(SoundEvents.ANVIL_LAND, 0.7F, 1.2F);
        sincronizar();
    }

    public void soltarContenido() {
        if (this.level == null || this.level.isClientSide) return;
        if (this.chatarra <= 0) return;

        Item chatarraItem = itemPorNombre(ID_CHATARRA);
        if (chatarraItem == Items.AIR) return;

        int restante = this.chatarra;
        this.chatarra = 0;
        this.clicks = 0;
        this.procesando = false;

        for (int i = 0; i < restante; i++) {
            Block.popResource(this.level, this.worldPosition, new ItemStack(chatarraItem));
        }
    }

    private void sonido(SoundEvent evento, float volumen, float tono) {
        if (this.level == null) return;
        this.level.playSound(null, this.worldPosition, evento, SoundSource.BLOCKS, volumen, tono);
    }

    private static Item itemPorNombre(String nombre) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(PezntZMod.MOD_ID, nombre));
        return item == null ? Items.AIR : item;
    }

    public Item getItemChatarra() {
        return itemPorNombre(ID_CHATARRA);
    }

    private void sincronizar() {
        setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Chatarra", this.chatarra);
        tag.putInt("Clicks", this.clicks);
        tag.putBoolean("Procesando", this.procesando);
        tag.putInt("TicksAnim", this.ticksAnim);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.chatarra = tag.getInt("Chatarra");
        this.clicks = tag.getInt("Clicks");
        this.procesando = tag.getBoolean("Procesando");
        this.ticksAnim = tag.getInt("TicksAnim");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "principal", 0, state -> {
            if (this.procesando) {
                return state.setAndContinue(PROCESAR);
            }
            if (ANIM_IDLE.isEmpty()) {
                return PlayState.STOP;
            }
            return state.setAndContinue(IDLE);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}