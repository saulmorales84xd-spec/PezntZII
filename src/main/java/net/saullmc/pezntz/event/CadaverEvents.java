package net.saullmc.pezntz.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.block.ModBlocks;
import net.saullmc.pezntz.block.custom.Cadaver;
import net.saullmc.pezntz.block.entity.CadaverEntity;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class CadaverEvents {

    public static final int ALTURA_BUSQUEDA = 8;

    public static final int RADIO_BUSQUEDA = 3;

    public static final boolean AVISAR_COORDENADAS = true;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.isSpectator()) return;

        Level level = player.level();
        if (level.isClientSide) return;

        if (level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) return;

        if (inventarioVacio(player)) return;

        BlockPos sitio = buscarSitio(level, player.blockPosition());

        level.setBlockAndUpdate(sitio, ModBlocks.CADAVER.get().defaultBlockState()
                .setValue(Cadaver.FACING, player.getDirection().getOpposite()));

        BlockEntity be = level.getBlockEntity(sitio);
        if (!(be instanceof CadaverEntity cadaver)) {
            return;
        }

        cadaver.guardarInventarioDe(player);

        if (AVISAR_COORDENADAS) {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                    "Tu cadaver quedo en " + sitio.getX() + ", " + sitio.getY() + ", " + sitio.getZ()));
        }
    }

    private static boolean inventarioVacio(ServerPlayer player) {
        for (var lista : java.util.List.of(player.getInventory().items,
                player.getInventory().armor, player.getInventory().offhand)) {
            for (var stack : lista) {
                if (!stack.isEmpty()) return false;
            }
        }
        return true;
    }

    private static BlockPos buscarSitio(Level level, BlockPos origen) {
        int minY = level.getMinBuildHeight() + 1;
        int maxY = level.getMaxBuildHeight() - 1;

        BlockPos base = new BlockPos(origen.getX(), Mth.clamp(origen.getY(), minY, maxY), origen.getZ());

        for (int dy = 0; dy <= ALTURA_BUSQUEDA; dy++) {
            BlockPos p = base.above(dy);
            if (p.getY() > maxY) break;
            if (sePuedeReemplazar(level, p)) return p;
        }

        for (int r = 1; r <= RADIO_BUSQUEDA; r++) {
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (Math.abs(dx) != r && Math.abs(dz) != r) continue;  // solo el borde

                    for (int dy = 0; dy <= ALTURA_BUSQUEDA; dy++) {
                        BlockPos p = base.offset(dx, dy, dz);
                        if (p.getY() > maxY) break;
                        if (sePuedeReemplazar(level, p)) return p;
                    }
                }
            }
        }

        return base;
    }

    private static boolean sePuedeReemplazar(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir() || state.canBeReplaced();
    }
}