package net.saullmc.pezntz.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.capability.BodyHealthData;
import net.saullmc.pezntz.capability.BodyHealthProvider;
import net.saullmc.pezntz.network.NetworkHandler;
import net.saullmc.pezntz.network.SyncBodyHealthPacket;

import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class ModCommands {

    private static final int NIVEL_PERMISO = 2;

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> raiz = Commands.literal("pezntz")
                .requires(source -> source.hasPermission(NIVEL_PERMISO));

        raiz.then(Commands.literal("heal")
                .executes(ctx -> curar(ctx.getSource(), List.of(ctx.getSource().getPlayerOrException())))
                .then(Commands.argument("objetivos", EntityArgument.players())
                        .executes(ctx -> curar(ctx.getSource(), EntityArgument.getPlayers(ctx, "objetivos")))));

        event.getDispatcher().register(raiz);
    }

    private static int curar(CommandSourceStack source, Collection<ServerPlayer> objetivos)
            throws CommandSyntaxException {

        for (ServerPlayer player : objetivos) {

            player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
                cap.setHead(BodyHealthData.MAX_HEALTH);
                cap.setBody(BodyHealthData.MAX_HEALTH);
                cap.setArms(BodyHealthData.MAX_HEALTH);
                cap.setLegs(BodyHealthData.MAX_HEALTH);

                NetworkHandler.sendToClients(new SyncBodyHealthPacket(player.getId(), cap), player);
            });

            player.setHealth(player.getMaxHealth());
        }

        int cuantos = objetivos.size();
        Component mensaje = cuantos == 1
                ? Component.literal("Curado " + objetivos.iterator().next().getName().getString())
                : Component.literal("Curados " + cuantos + " jugadores");

        source.sendSuccess(() -> mensaje, false);

        return cuantos;
    }
}