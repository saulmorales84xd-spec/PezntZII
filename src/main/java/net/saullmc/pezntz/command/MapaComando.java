package net.saullmc.pezntz.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.mapa.DatosMapa;
import net.saullmc.pezntz.mapa.GeneradorMapa;
import net.saullmc.pezntz.mapa.MapaServidor;
import net.saullmc.pezntz.mapa.MarcadorMapa;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class MapaComando {

    private static final int NIVEL_PERMISO = 2;

    public static final int RADIO_MAXIMO = 30000;

    public static final int BLOQUES_POR_PIXEL_MAXIMO = 64;

    @SubscribeEvent
    public static void alRegistrar(RegisterCommandsEvent evento) {
        LiteralArgumentBuilder<CommandSourceStack> raiz = Commands.literal("pezntz")
                .requires(source -> source.hasPermission(NIVEL_PERMISO));

        raiz.then(Commands.literal("mapa")
                .then(Commands.literal("generar")
                        .then(Commands.literal("aqui")
                                .then(Commands.argument("radio", IntegerArgumentType.integer(16, RADIO_MAXIMO))
                                        .executes(ctx -> generarAqui(ctx, -1))
                                        .then(Commands.argument("bloques_por_pixel",
                                                        IntegerArgumentType.integer(1, BLOQUES_POR_PIXEL_MAXIMO))
                                                .executes(ctx -> generarAqui(ctx,
                                                        IntegerArgumentType.getInteger(ctx, "bloques_por_pixel"))))))
                        .then(Commands.argument("x", IntegerArgumentType.integer())
                                .then(Commands.argument("z", IntegerArgumentType.integer())
                                        .then(Commands.argument("radio", IntegerArgumentType.integer(16, RADIO_MAXIMO))
                                                .executes(ctx -> generar(ctx,
                                                        IntegerArgumentType.getInteger(ctx, "x"),
                                                        IntegerArgumentType.getInteger(ctx, "z"), -1))
                                                .then(Commands.argument("bloques_por_pixel",
                                                                IntegerArgumentType.integer(1, BLOQUES_POR_PIXEL_MAXIMO))
                                                        .executes(ctx -> generar(ctx,
                                                                IntegerArgumentType.getInteger(ctx, "x"),
                                                                IntegerArgumentType.getInteger(ctx, "z"),
                                                                IntegerArgumentType.getInteger(ctx, "bloques_por_pixel"))))))))
                .then(Commands.literal("estado").executes(ctx -> estado(ctx.getSource())))
                .then(Commands.literal("cancelar").executes(ctx -> cancelar(ctx.getSource())))
                .then(Commands.literal("borrar").executes(ctx -> borrar(ctx.getSource())))
                .then(Commands.literal("reenviar").executes(ctx -> reenviar(ctx.getSource())))
                .then(Commands.literal("marcador")
                        .then(Commands.literal("agregar")
                                .then(Commands.argument("x", IntegerArgumentType.integer())
                                        .then(Commands.argument("z", IntegerArgumentType.integer())
                                                .then(Commands.argument("icono", ResourceLocationArgument.id())
                                                        .suggests((ctx, sugerencias) ->
                                                                SharedSuggestionProvider.suggestResource(BuiltInRegistries.ITEM.keySet(), sugerencias))
                                                        .then(Commands.argument("nombre", StringArgumentType.greedyString())
                                                                .executes(MapaComando::agregarMarcador))))))
                        .then(Commands.literal("quitar")
                                .then(Commands.argument("nombre", StringArgumentType.greedyString())
                                        .suggests((ctx, sugerencias) -> SharedSuggestionProvider.suggest(
                                                MapaServidor.marcadores().stream().map(MarcadorMapa::nombre), sugerencias))
                                        .executes(ctx -> quitarMarcador(ctx.getSource(),
                                                StringArgumentType.getString(ctx, "nombre")))))
                        .then(Commands.literal("lista").executes(ctx -> listaMarcadores(ctx.getSource())))));

        evento.getDispatcher().register(raiz);
    }

    private static int generarAqui(CommandContext<CommandSourceStack> ctx, int bloquesPorPixel) {
        BlockPos pos = BlockPos.containing(ctx.getSource().getPosition());
        return generar(ctx, pos.getX(), pos.getZ(), bloquesPorPixel);
    }

    private static int generar(CommandContext<CommandSourceStack> ctx, int x, int z, int bloquesPorPixel) {
        CommandSourceStack source = ctx.getSource();
        int radio = IntegerArgumentType.getInteger(ctx, "radio");

        int bpp = bloquesPorPixel > 0 ? bloquesPorPixel : GeneradorMapa.bloquesPorPixelAutomatico(radio);

        UUID quien = source.getEntity() instanceof ServerPlayer jugador ? jugador.getUUID() : null;

        String error = GeneradorMapa.empezar(source.getLevel(), x, z, radio, bpp, quien);

        if (error != null) {
            source.sendFailure(Component.literal(error));
            return 0;
        }

        int lado = (int) Math.ceil(2.0D * radio / bpp);
        int chunks = GeneradorMapa.chunksTotales();

        source.sendSuccess(() -> Component.literal("Generando mapa en " + x + " " + z + " con radio " + radio
                + " (" + bpp + " bloques por pixel, " + lado + "x" + lado + " pixeles, " + chunks + " chunks)"), true);
        return 1;
    }

    private static int estado(CommandSourceStack source) {
        if (GeneradorMapa.ocupado()) {
            source.sendSuccess(() -> Component.literal("Generando mapa... " + GeneradorMapa.porcentaje() + "%"), false);
            return 1;
        }

        DatosMapa d = MapaServidor.datos();

        if (d == null) {
            source.sendSuccess(() -> Component.literal("No hay mapa generado"), false);
            return 0;
        }

        int radio = d.bloquesLado() / 2;
        int centroX = d.x0 + radio;
        int centroZ = d.z0 + radio;

        source.sendSuccess(() -> Component.literal("Mapa en " + centroX + " " + centroZ + ", radio " + radio
                + ", " + d.bpp + " bloques por pixel, " + d.lado + "x" + d.lado
                + " pixeles, " + d.revision + " actualizaciones"), false);
        return 1;
    }

    private static int cancelar(CommandSourceStack source) {
        if (!GeneradorMapa.cancelar()) {
            source.sendFailure(Component.literal("No se esta generando ningun mapa"));
            return 0;
        }

        source.sendSuccess(() -> Component.literal("Generacion del mapa cancelada"), true);
        return 1;
    }

    private static int borrar(CommandSourceStack source) {
        boolean habia = MapaServidor.borrar(source.getServer());

        source.sendSuccess(() -> Component.literal(habia ? "Mapa borrado" : "No habia mapa"), true);
        return habia ? 1 : 0;
    }

    private static int reenviar(CommandSourceStack source) {
        MapaServidor.reenviar(source.getServer());
        source.sendSuccess(() -> Component.literal("Mapa reenviado a todos los jugadores"), false);
        return 1;
    }

    private static int agregarMarcador(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();

        int x = IntegerArgumentType.getInteger(ctx, "x");
        int z = IntegerArgumentType.getInteger(ctx, "z");
        ResourceLocation icono = ResourceLocationArgument.getId(ctx, "icono");
        String nombre = StringArgumentType.getString(ctx, "nombre").trim();

        if (nombre.isEmpty()) {
            source.sendFailure(Component.literal("Ponle un nombre al marcador"));
            return 0;
        }

        MapaServidor.ponerMarcador(source.getServer(), new MarcadorMapa(nombre, x, z, icono.toString()));

        source.sendSuccess(() -> Component.literal("Marcador \"" + nombre + "\" puesto en " + x + " " + z), true);
        return 1;
    }

    private static int quitarMarcador(CommandSourceStack source, String nombre) {
        if (!MapaServidor.quitarMarcador(source.getServer(), nombre.trim())) {
            source.sendFailure(Component.literal("No hay ningun marcador con ese nombre"));
            return 0;
        }

        source.sendSuccess(() -> Component.literal("Marcador \"" + nombre.trim() + "\" quitado"), true);
        return 1;
    }

    private static int listaMarcadores(CommandSourceStack source) {
        List<MarcadorMapa> lista = MapaServidor.marcadores();

        if (lista.isEmpty()) {
            source.sendSuccess(() -> Component.literal("No hay marcadores en el mapa"), false);
            return 0;
        }

        for (MarcadorMapa m : lista) {
            source.sendSuccess(() -> Component.literal(m.nombre() + ": " + m.x() + " " + m.z() + " (" + m.icono() + ")"), false);
        }
        return lista.size();
    }
}