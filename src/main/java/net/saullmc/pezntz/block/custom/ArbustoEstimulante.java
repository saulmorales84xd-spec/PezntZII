package net.saullmc.pezntz.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.saullmc.pezntz.item.ModItems;

public class ArbustoEstimulante extends SweetBerryBushBlock {

    public static final int NIVEL_VELOCIDAD = 0;

    public static final int DURACION_VELOCIDAD = 60;

    public static final int EDAD_MINIMA_PARA_VELOCIDAD = 0;

    public static final boolean TAMBIEN_MOBS = false;

    public static final boolean FRENAR_AL_PASAR = false;

    public static final boolean MOSTRAR_PARTICULAS = true;

    public ArbustoEstimulante(Properties propiedades) {
        super(propiedades);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter nivel, BlockPos pos, BlockState estado) {
        return new ItemStack(ModItems.FRUTO_ESTIMULANTE.get());
    }

    @Override
    public void entityInside(BlockState estado, Level nivel, BlockPos pos, Entity entidad) {
        if (!(entidad instanceof LivingEntity vivo)) return;
        if (!TAMBIEN_MOBS && !(vivo instanceof Player)) return;

        if (FRENAR_AL_PASAR) {
            entidad.makeStuckInBlock(estado, new Vec3(0.8F, 0.75D, 0.8F));
        }

        if (nivel.isClientSide()) return;
        if (estado.getValue(AGE) < EDAD_MINIMA_PARA_VELOCIDAD) return;

        MobEffectInstance actual = vivo.getEffect(MobEffects.MOVEMENT_SPEED);
        if (actual != null) {
            if (actual.getAmplifier() > NIVEL_VELOCIDAD) return;
            if (actual.getAmplifier() == NIVEL_VELOCIDAD && actual.getDuration() > DURACION_VELOCIDAD - 10) return;
        }

        vivo.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, DURACION_VELOCIDAD, NIVEL_VELOCIDAD,
                false, MOSTRAR_PARTICULAS, true));
    }

    @Override
    public InteractionResult use(BlockState estado, Level nivel, BlockPos pos, Player jugador,
                                 InteractionHand mano, BlockHitResult golpe) {
        int edad = estado.getValue(AGE);
        boolean maduro = edad == MAX_AGE;

        if (!maduro && jugador.getItemInHand(mano).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        }

        if (edad > 1) {
            int cantidad = 1 + nivel.random.nextInt(2) + (maduro ? 1 : 0);
            popResource(nivel, pos, new ItemStack(ModItems.FRUTO_ESTIMULANTE.get(), cantidad));

            nivel.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS,
                    1.0F, 0.8F + nivel.random.nextFloat() * 0.4F);

            BlockState cosechado = estado.setValue(AGE, 1);
            nivel.setBlock(pos, cosechado, 2);
            nivel.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(jugador, cosechado));

            return InteractionResult.sidedSuccess(nivel.isClientSide());
        }

        return InteractionResult.PASS;
    }
}