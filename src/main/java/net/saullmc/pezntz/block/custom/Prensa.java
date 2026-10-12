package net.saullmc.pezntz.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.saullmc.pezntz.block.entity.ModBlockEntities;
import net.saullmc.pezntz.block.entity.PrensaEntity;

import javax.annotation.Nullable;

public class Prensa extends BaseEntityBlock {

    public static final int MAX_CHATARRA = 9;

    public static final int CLICKS_NECESARIOS = 5;

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final double CAJA_X_MIN = 0.0D;
    public static final double CAJA_Y_MIN = 0.0D;
    public static final double CAJA_Z_MIN = 0.0D;
    public static final double CAJA_X_MAX = 16.0D;
    public static final double CAJA_Y_MAX = 16.0D;
    public static final double CAJA_Z_MAX = 16.0D;

    private static final VoxelShape FORMA =
            Block.box(CAJA_X_MIN, CAJA_Y_MIN, CAJA_Z_MIN, CAJA_X_MAX, CAJA_Y_MAX, CAJA_Z_MAX);

    public Prensa(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
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

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return FORMA;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PrensaEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.PRENSA.get(),
                (nivel, posicion, estado, be) -> be.tick());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof PrensaEntity proc)) return InteractionResult.PASS;

        if (level.isClientSide) {
            if (proc.estaLleno() && !proc.isProcesando()) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        net.saullmc.pezntz.client.hud.ProcesadorBarOverlay.mostrar(pos));
            }
            return InteractionResult.SUCCESS;
        }

        return proc.alHacerClick((ServerPlayer) player, player.getItemInHand(hand));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof PrensaEntity proc) {
                proc.soltarContenido();
            }
        }
        super.onRemove(state, level, pos, newState, moved);
    }
}
