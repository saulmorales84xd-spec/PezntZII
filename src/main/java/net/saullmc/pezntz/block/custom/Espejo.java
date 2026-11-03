package net.saullmc.pezntz.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Espejo extends BloqueContenedor {

    public static final double GROSOR = 1.0D;
    public static final double ALTO = 16.0D;

    public static final VoxelShape NORTE = Block.box(0, 0, 16 - GROSOR, 16, ALTO, 16);
    public static final VoxelShape SUR = Block.box(0, 0, 0, 16, ALTO, GROSOR);
    public static final VoxelShape OESTE = Block.box(16 - GROSOR, 0, 0, 16, ALTO, 16);
    public static final VoxelShape ESTE = Block.box(0, 0, 0, GROSOR, ALTO, 16);

    public Espejo(Properties properties) {
        super(properties, NORTE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SUR;
            case WEST -> OESTE;
            case EAST -> ESTE;
            default -> NORTE;
        };
    }
}