package net.saullmc.pezntz.block.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.saullmc.pezntz.block.entity.CofreCandadoEntity;
import net.saullmc.pezntz.item.ModItems;
import net.saullmc.pezntz.network.AbrirTecladoPacket;
import net.saullmc.pezntz.network.NetworkHandler;

import javax.annotation.Nullable;

public class CofreCandado extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final EnumProperty<ChestType> TYPE = BlockStateProperties.CHEST_TYPE;

    private static final VoxelShape FORMA = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    public static final boolean SUELTA_AL_ROMPER = true;

    public static final boolean DUENIO_ABRE_SIN_CODIGO = false;

    public static final boolean GASTAR_LLAVE = false;

    public static final boolean DEVOLVER_CANDADO = true;

    public static final boolean SOLO_DUENIO_QUITA_CANDADO = false;


    public CofreCandado(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(TYPE, ChestType.LEFT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE);
    }

    public static Direction direccionPareja(BlockState estado) {
        Direction mirando = estado.getValue(FACING);
        return estado.getValue(TYPE) == ChestType.LEFT ? mirando.getClockWise() : mirando.getCounterClockWise();
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return FORMA;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CofreCandadoEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player jugador,
                                 InteractionHand mano, BlockHitResult hit) {
        if (mano != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof CofreCandadoEntity cofre)) return InteractionResult.PASS;
        if (!(jugador instanceof ServerPlayer servidor)) return InteractionResult.CONSUME;

        ItemStack enMano = jugador.getItemInHand(mano);

        if (enMano.is(ModItems.LLAVE_CANDADO.get())) {
            quitarCandado(level, cofre, servidor, enMano);
            return InteractionResult.CONSUME;
        }

        if (!cofre.tieneCodigo()) {
            NetworkHandler.sendToPlayer(new AbrirTecladoPacket(pos, true), servidor);
            return InteractionResult.CONSUME;
        }

        if (DUENIO_ABRE_SIN_CODIGO && cofre.esDuenio(jugador)) {
            abrirContenido(servidor, cofre);
            return InteractionResult.CONSUME;
        }

        NetworkHandler.sendToPlayer(new AbrirTecladoPacket(pos, false), servidor);
        return InteractionResult.CONSUME;
    }

    public static void abrirContenido(ServerPlayer jugador, CofreCandadoEntity cofre) {
        NetworkHooks.openScreen(jugador, cofre, cofre.getBlockPos());
    }

    private static void quitarCandado(Level level, CofreCandadoEntity cofre, ServerPlayer jugador, ItemStack llave) {
        if (SOLO_DUENIO_QUITA_CANDADO && !cofre.esDuenio(jugador)) {
            aviso(jugador, "Este cofre no es tuyo", ChatFormatting.RED);
            return;
        }

        CofreCandadoEntity izquierda = cofre.getIzquierda();
        CofreCandadoEntity derecha = cofre.getDerecha();

        if (izquierda == null || derecha == null) {
            aviso(jugador, "No encuentro la otra mitad del cofre", ChatFormatting.RED);
            return;
        }

        BlockPos posIzquierda = izquierda.getBlockPos();
        BlockPos posDerecha = derecha.getBlockPos();
        Direction mirando = izquierda.getBlockState().getValue(FACING);

        ItemStack[] contenidoIzquierda = izquierda.vaciarYDevolver();
        ItemStack[] contenidoDerecha = derecha.vaciarYDevolver();

        level.removeBlock(posIzquierda, false);
        if (level.getBlockState(posDerecha).getBlock() instanceof CofreCandado) {
            level.removeBlock(posDerecha, false);
        }

        int banderas = Block.UPDATE_ALL | Block.UPDATE_KNOWN_SHAPE;

        level.setBlock(posIzquierda, Blocks.CHEST.defaultBlockState()
                .setValue(ChestBlock.FACING, mirando)
                .setValue(ChestBlock.TYPE, ChestType.LEFT), banderas);

        level.setBlock(posDerecha, Blocks.CHEST.defaultBlockState()
                .setValue(ChestBlock.FACING, mirando)
                .setValue(ChestBlock.TYPE, ChestType.RIGHT), banderas);

        volcarEnCofre(level, posIzquierda, contenidoIzquierda);
        volcarEnCofre(level, posDerecha, contenidoDerecha);

        if (GASTAR_LLAVE && !jugador.getAbilities().instabuild) {
            llave.shrink(1);
        }

        if (DEVOLVER_CANDADO) {
            ItemStack candado = new ItemStack(ModItems.CANDADO.get());
            if (!jugador.getInventory().add(candado)) {
                Block.popResource(level, posIzquierda, candado);
            }
        }

        aviso(jugador, "Candado quitado", ChatFormatting.GREEN);
    }

    private static void volcarEnCofre(Level level, BlockPos pos, ItemStack[] contenido) {
        if (!(level.getBlockEntity(pos) instanceof ChestBlockEntity cofre)) return;

        for (int i = 0; i < contenido.length && i < cofre.getContainerSize(); i++) {
            if (contenido[i] != null && !contenido[i].isEmpty()) {
                cofre.setItem(i, contenido[i]);
            }
        }
        cofre.setChanged();
    }

    private static void aviso(ServerPlayer jugador, String texto, ChatFormatting color) {
        jugador.displayClientMessage(Component.literal(texto).withStyle(color), true);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);

            if (be instanceof CofreCandadoEntity cofre) {
                BlockPos otra = cofre.getPosPareja();

                if (SUELTA_AL_ROMPER) cofre.soltarTodo();

                if (otra != null && level.getBlockState(otra).getBlock() instanceof CofreCandado) {
                    level.removeBlock(otra, false);
                }
            }
        }
        super.onRemove(state, level, pos, newState, moved);
    }
}