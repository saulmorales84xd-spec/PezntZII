package net.saullmc.pezntz.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import net.saullmc.pezntz.block.entity.ContenedorLootEntity;
import net.saullmc.pezntz.block.entity.ModBlockEntities;

import javax.annotation.Nullable;

public class BloqueContenedor extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final VoxelShape ALTO = Block.box(0, 0, 0, 16, 32, 16);

    public static final VoxelShape COMPLETO = Block.box(0, 0, 0, 16, 16, 16);

    public static final VoxelShape BAJO = Block.box(1, 0, 1, 15, 8, 15);

    public static final VoxelShape PEQUENO = Block.box(3, 0, 3, 13, 10, 13);

    private final VoxelShape forma;
    private final double altoEnBloques;

    public BloqueContenedor(Properties properties, VoxelShape forma) {
        super(properties);
        this.forma = forma;
        this.altoEnBloques = forma.max(Axis.Y);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public BloqueContenedor(Properties properties) {
        this(properties, COMPLETO);
    }

    public double getAltoEnBloques() {
        return this.altoEnBloques;
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
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.forma;
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
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ContenedorLootEntity(pos, state);
    }

    public static final String CARPETA_LOOT = "contenedores/";

    @Nullable
    public ResourceLocation getTablaLoot() {
        ResourceLocation id = ForgeRegistries.BLOCKS.getKey(this);
        if (id == null) return null;

        return new ResourceLocation(id.getNamespace(), CARPETA_LOOT + id.getPath());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            @Nullable LivingEntity colocador, ItemStack stack) {
        super.setPlacedBy(level, pos, state, colocador, stack);

        if (level.isClientSide) return;
        if (!(level.getBlockEntity(pos) instanceof ContenedorLootEntity contenedor)) return;

        ResourceLocation tabla = getTablaLoot();
        if (tabla == null) return;

        contenedor.setLootTable(tabla, level.getRandom().nextLong());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player jugador,
                                 InteractionHand mano, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ContenedorLootEntity contenedor)) return InteractionResult.PASS;

        contenedor.generarSiHaceFalta();

        jugador.openMenu(contenedor);
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);

            if (be instanceof ContenedorLootEntity contenedor) {
                Containers.dropContents(level, pos, contenedor);
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> tipo) {
        if (level.isClientSide) return null;

        return createTickerHelper(tipo, ModBlockEntities.CONTENEDOR_LOOT.get(),
                ContenedorLootEntity::tickServidor);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }
}