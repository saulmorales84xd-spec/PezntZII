package net.saullmc.pezntz.event;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.data.DiamondTradeData;
import net.saullmc.pezntz.merchant.CustomBlockMerchant;

import java.util.OptionalInt;

@Mod.EventBusSubscriber(modid = "pezntz")
public class DiamondTradeEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() == InteractionHand.MAIN_HAND && !event.getLevel().isClientSide) {
            if (event.getLevel().getBlockState(event.getPos()).is(Blocks.DIAMOND_BLOCK)) {
                ServerPlayer player = (ServerPlayer) event.getEntity();
                ServerLevel level = (ServerLevel) event.getLevel();

                DiamondTradeData data = DiamondTradeData.get(level);

                CustomBlockMerchant merchant = new CustomBlockMerchant(player, data.getOffers());

                OptionalInt syncId = player.openMenu(new SimpleMenuProvider(
                        (id, inv, p) -> new MerchantMenu(id, inv, merchant),
                        Component.literal("Tienda de Diamante")
                ));

                if (syncId.isPresent()) {
                    player.sendMerchantOffers(syncId.getAsInt(), data.getOffers(), 1, 0, false, false);
                }

                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        CommandBuildContext buildContext = event.getBuildContext();

        dispatcher.register(Commands.literal("tiendadiamante")
                .requires(source -> source.hasPermission(2))

                .then(Commands.literal("add")
                        .then(Commands.argument("itemCompra", ItemArgument.item(buildContext))
                                .then(Commands.argument("cantCompra", IntegerArgumentType.integer(1, 64))
                                        .then(Commands.argument("itemVenta", ItemArgument.item(buildContext))
                                                .then(Commands.argument("cantVenta", IntegerArgumentType.integer(1, 64))
                                                        .executes(context -> {

                                                            ItemInput itemCompra = ItemArgument.getItem(context, "itemCompra");
                                                            int cantCompra = IntegerArgumentType.getInteger(context, "cantCompra");

                                                            ItemInput itemVenta = ItemArgument.getItem(context, "itemVenta");
                                                            int cantVenta = IntegerArgumentType.getInteger(context, "cantVenta");

                                                            ItemStack compraStack = itemCompra.createItemStack(cantCompra, false);
                                                            ItemStack ventaStack = itemVenta.createItemStack(cantVenta, false);

                                                            MerchantOffer offer = new MerchantOffer(compraStack, ventaStack, 99999, 0, 0);

                                                            ServerLevel level = context.getSource().getLevel();
                                                            DiamondTradeData data = DiamondTradeData.get(level);
                                                            data.addTrade(offer);

                                                            context.getSource().sendSuccess(() -> Component.literal("¡Tradeo añadido al bloque de diamante!"), true);
                                                            return 1;
                                                        }))))))

                .then(Commands.literal("clear")
                        .executes(context -> {
                            ServerLevel level = context.getSource().getLevel();
                            DiamondTradeData data = DiamondTradeData.get(level);
                            data.clearTrades();

                            context.getSource().sendSuccess(() -> Component.literal("¡Todos los tradeos han sido eliminados!"), true);
                            return 1;
                        }))
        );
    }
}