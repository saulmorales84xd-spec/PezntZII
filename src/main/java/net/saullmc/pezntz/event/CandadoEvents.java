package net.saullmc.pezntz.event;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.block.ModBlocks;
import net.saullmc.pezntz.block.custom.CofreCandado;
import net.saullmc.pezntz.block.entity.CofreCandadoEntity;
import net.saullmc.pezntz.item.ModItems;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class CandadoEvents {

    public static final boolean GASTA_CANDADO = true;

    @SubscribeEvent
    public static void onClickDerecho(PlayerInteractEvent.RightClickBlock event) {
        Player jugador = event.getEntity();
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        ItemStack enMano = jugador.getItemInHand(InteractionHand.MAIN_HAND);
        if (!enMano.is(ModItems.CANDADO.get())) return;

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState estado = level.getBlockState(pos);

        if (!(estado.getBlock() instanceof ChestBlock)) return;

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);

        if (level.isClientSide) return;

        ChestType tipo = estado.getValue(ChestBlock.TYPE);

        if (tipo == ChestType.SINGLE) {
            jugador.displayClientMessage(
                    Component.literal("El candado solo va en cofres dobles").withStyle(ChatFormatting.RED), true);
            return;
        }

        Direction haciaPareja = ChestBlock.getConnectedDirection(estado);
        BlockPos posPareja = pos.relative(haciaPareja);
        BlockState estadoPareja = level.getBlockState(posPareja);

        if (!(estadoPareja.getBlock() instanceof ChestBlock)) {
            jugador.displayClientMessage(
                    Component.literal("No encuentro la otra mitad del cofre").withStyle(ChatFormatting.RED), true);
            return;
        }

        Direction mirando = estado.getValue(ChestBlock.FACING);

        ItemStack[] contenidoEste = vaciar(level, pos);
        ItemStack[] contenidoPareja = vaciar(level, posPareja);

        colocar(level, pos, mirando, tipo);
        colocar(level, posPareja, mirando, tipo == ChestType.LEFT ? ChestType.RIGHT : ChestType.LEFT);

        volcar(level, pos, contenidoEste, jugador);
        volcar(level, posPareja, contenidoPareja, jugador);

        if (GASTA_CANDADO && !jugador.getAbilities().instabuild) {
            enMano.shrink(1);
        }

        jugador.displayClientMessage(
                Component.literal("Candado puesto. Vuelve a hacer click para elegir el codigo")
                        .withStyle(ChatFormatting.GREEN), true);
    }

    private static ItemStack[] vaciar(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof Container contenedor)) return new ItemStack[0];

        ItemStack[] copia = new ItemStack[contenedor.getContainerSize()];
        for (int i = 0; i < copia.length; i++) {
            copia[i] = contenedor.getItem(i).copy();
        }

        contenedor.clearContent();
        return copia;
    }

    private static void colocar(Level level, BlockPos pos, Direction mirando, ChestType tipo) {
        level.setBlockAndUpdate(pos, ModBlocks.COFRE_CANDADO.get().defaultBlockState()
                .setValue(CofreCandado.FACING, mirando)
                .setValue(CofreCandado.TYPE, tipo));
    }

    private static void volcar(Level level, BlockPos pos, ItemStack[] contenido, Player duenio) {
        if (level.getBlockEntity(pos) instanceof CofreCandadoEntity nuevo) {
            nuevo.copiarDesde(contenido);
            nuevo.setDuenio(duenio);
        }
    }
}