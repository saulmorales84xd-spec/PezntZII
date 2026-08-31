package net.saullmc.pezntz.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.custom.Backpack;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BackpackEvents {

    private static final String BACKPACK_TAG = "HasCraftedBackpack";

    @SubscribeEvent
    public static void onBackpackCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack craftedItem = event.getCrafting();

        if (craftedItem.getItem() instanceof Backpack) {
            if (event.getEntity().getPersistentData().getBoolean(BACKPACK_TAG)) {
                craftedItem.setCount(0);
            } else {
                event.getEntity().getPersistentData().putBoolean(BACKPACK_TAG, true);
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("resetmochila")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("jugador", EntityArgument.player())
                        .executes(context -> {
                            ServerPlayer player = EntityArgument.getPlayer(context, "jugador");

                            player.getPersistentData().putBoolean(BACKPACK_TAG, false);

                            return 1;
                        }))
        );
    }
}