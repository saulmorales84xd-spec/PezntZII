package net.saullmc.pezntz.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CajaMilitar extends BloqueContenedor {

    public static final double EXTRA = 16.0D;

    public static final double ALTO = 16.0D;

    public static final boolean EXTENDER_A_LA_DERECHA = true;

    public static final VoxelShape NORTE_IZQUIERDA = Block.box(-EXTRA, 0, 0, 16, ALTO, 16);
    public static final VoxelShape SUR_IZQUIERDA = Block.box(0, 0, 0, 16 + EXTRA, ALTO, 16);
    public static final VoxelShape OESTE_IZQUIERDA = Block.box(0, 0, 0, 16, ALTO, 16 + EXTRA);
    public static final VoxelShape ESTE_IZQUIERDA = Block.box(0, 0, -EXTRA, 16, ALTO, 16);

    public static final VoxelShape NORTE_DERECHA = SUR_IZQUIERDA;
    public static final VoxelShape SUR_DERECHA = NORTE_IZQUIERDA;
    public static final VoxelShape OESTE_DERECHA = ESTE_IZQUIERDA;
    public static final VoxelShape ESTE_DERECHA = OESTE_IZQUIERDA;

    public CajaMilitar(Properties properties) {
        super(properties, NORTE_IZQUIERDA);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> EXTENDER_A_LA_DERECHA ? SUR_DERECHA : SUR_IZQUIERDA;
            case WEST -> EXTENDER_A_LA_DERECHA ? OESTE_DERECHA : OESTE_IZQUIERDA;
            case EAST -> EXTENDER_A_LA_DERECHA ? ESTE_DERECHA : ESTE_IZQUIERDA;
            default -> EXTENDER_A_LA_DERECHA ? NORTE_DERECHA : NORTE_IZQUIERDA;
        };
    }
}