package net.saullmc.pezntz.client;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;

import java.nio.file.Files;
import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RecursosTacz {

    public static final String CARPETA = "resourcepacks/pezntz_tacz";

    public static final String ID = "pezntz_tacz";

    @SubscribeEvent
    public static void alBuscarPaquetes(AddPackFindersEvent evento) {
        if (evento.getPackType() != PackType.CLIENT_RESOURCES) return;

        Path ruta = ModList.get().getModFileById(PezntZMod.MOD_ID).getFile().findResource(CARPETA);
        if (ruta == null || !Files.exists(ruta)) return;

        Pack paquete = Pack.readMetaAndCreate(ID, Component.literal("PezntZ - Armas"), true,
                id -> new PathPackResources(id, ruta, true),
                PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN);

        if (paquete != null) evento.addRepositorySource(consumidor -> consumidor.accept(paquete));
    }
}