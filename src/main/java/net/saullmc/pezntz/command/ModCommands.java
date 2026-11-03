package net.saullmc.pezntz.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.capability.BodyHealthData;
import net.saullmc.pezntz.capability.BodyHealthProvider;
import net.saullmc.pezntz.network.NetworkHandler;
import net.saullmc.pezntz.block.entity.ContenedorLootEntity;
import net.saullmc.pezntz.capability.BestiarioProvider;
import net.saullmc.pezntz.network.SyncBestiarioPacket;
import net.saullmc.pezntz.network.SyncBodyHealthPacket;

import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class ModCommands {

    private static final int NIVEL_PERMISO = 2;

    private static final double ALCANCE_MIRADA = 10.0D;

    private static final int RADIO_MAXIMO = 32;

    private static final int ALTURA_RADIO = 8;

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> raiz = Commands.literal("pezntz")
                .requires(source -> source.hasPermission(NIVEL_PERMISO));

        raiz.then(Commands.literal("heal")
                .executes(ctx -> curar(ctx.getSource(), List.of(ctx.getSource().getPlayerOrException())))
                .then(Commands.argument("objetivos", EntityArgument.players())
                        .executes(ctx -> curar(ctx.getSource(), EntityArgument.getPlayers(ctx, "objetivos")))));

        raiz.then(Commands.literal("loot")
                .executes(ctx -> regenerarMirado(ctx.getSource()))
                .then(Commands.argument("radio", IntegerArgumentType.integer(1, RADIO_MAXIMO))
                        .executes(ctx -> regenerarEnRadio(ctx.getSource(),
                                IntegerArgumentType.getInteger(ctx, "radio")))));

        raiz.then(Commands.literal("bestiario")
                .then(Commands.literal("borrar")
                        .executes(ctx -> borrarBestiario(ctx.getSource(),
                                List.of(ctx.getSource().getPlayerOrException())))
                        .then(Commands.argument("objetivos", EntityArgument.players())
                                .executes(ctx -> borrarBestiario(ctx.getSource(),
                                        EntityArgument.getPlayers(ctx, "objetivos"))))));

        event.getDispatcher().register(raiz);
    }

    private static int regenerarMirado(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer jugador = source.getPlayerOrException();
        ServerLevel level = source.getLevel();

        HitResult mirando = jugador.pick(ALCANCE_MIRADA, 0.0F, false);

        if (mirando.getType() != HitResult.Type.BLOCK) {
            source.sendFailure(Component.literal("No estas mirando ningun bloque"));
            return 0;
        }

        BlockPos pos = ((BlockHitResult) mirando).getBlockPos();
        BlockEntity be = level.getBlockEntity(pos);

        if (!(be instanceof ContenedorLootEntity contenedor)) {
            source.sendFailure(Component.literal("Eso no es un contenedor de loot"));
            return 0;
        }

        contenedor.regenerar();
        source.sendSuccess(() -> Component.literal("Loot regenerado"), false);
        return 1;
    }

    private static int regenerarEnRadio(CommandSourceStack source, int radio) throws CommandSyntaxException {
        ServerLevel level = source.getLevel();
        BlockPos centro = BlockPos.containing(source.getPosition());

        int cuantos = 0;

        for (BlockPos pos : BlockPos.betweenClosed(centro.offset(-radio, -ALTURA_RADIO, -radio),
                centro.offset(radio, ALTURA_RADIO, radio))) {

            if (!level.isLoaded(pos)) continue;
            if (!(level.getBlockEntity(pos) instanceof ContenedorLootEntity contenedor)) continue;

            contenedor.regenerar();
            cuantos++;
        }

        final int total = cuantos;
        source.sendSuccess(() -> Component.literal("Loot regenerado en " + total + " contenedores"), false);
        return cuantos;
    }

    private static int borrarBestiario(CommandSourceStack source, Collection<ServerPlayer> objetivos) {
        for (ServerPlayer jugador : objetivos) {
            jugador.getCapability(BestiarioProvider.PLAYER_BESTIARIO).ifPresent(bestiario -> {
                bestiario.getVistos().clear();

                NetworkHandler.sendToPlayer(new SyncBestiarioPacket(bestiario), jugador);
            });
        }

        int cuantos = objetivos.size();
        Component mensaje = cuantos == 1
                ? Component.literal("Bestiario borrado a " + objetivos.iterator().next().getName().getString())
                : Component.literal("Bestiario borrado a " + cuantos + " jugadores");

        source.sendSuccess(() -> mensaje, false);
        return cuantos;
    }

    private static int curar(CommandSourceStack source, Collection<ServerPlayer> objetivos)
            throws CommandSyntaxException {

        for (ServerPlayer player : objetivos) {

            player.getCapability(BodyHealthProvider.PLAYER_BODY_HEALTH).ifPresent(cap -> {
                cap.setHead(cap.getMaxHead());
                cap.setBody(cap.getMaxBody());
                cap.setArms(cap.getMaxArms());
                cap.setLegs(cap.getMaxLegs());

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