package net.saullmc.pezntz.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof Container contenedor)) return;

        net.minecraft.world.item.ItemStack[] copia =
                new net.minecraft.world.item.ItemStack[contenedor.getContainerSize()];
        for (int i = 0; i < copia.length; i++) {
            copia[i] = contenedor.getItem(i).copy();
        }

        contenedor.clearContent();

        level.setBlockAndUpdate(pos, ModBlocks.COFRE_CANDADO.get().defaultBlockState()
                .setValue(CofreCandado.FACING, jugador.getDirection().getOpposite()));

        if (level.getBlockEntity(pos) instanceof CofreCandadoEntity nuevo) {
            for (int i = 0; i < copia.length && i < CofreCandadoEntity.SLOTS; i++) {
                if (!copia[i].isEmpty()) {
                    nuevo.getInventario().setStackInSlot(i, copia[i]);
                }
            }
            nuevo.setDuenio(jugador);
        }

        if (GASTA_CANDADO && !jugador.getAbilities().instabuild) {
            enMano.shrink(1);
        }

        jugador.displayClientMessage(
                net.minecraft.network.chat.Component.literal("Candado puesto. Vuelve a hacer click para elegir el codigo"),
                true);
    }
}