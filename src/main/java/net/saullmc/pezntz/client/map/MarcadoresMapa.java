package net.saullmc.pezntz.client.map;

import net.minecraft.resources.ResourceLocation;
import net.saullmc.pezntz.PezntZMod;

import java.util.List;

public class MarcadoresMapa {

    public static final ResourceLocation ICONO_AZUFRE =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/item/marcador_minas_azufre.png");

    public static final ResourceLocation ICONO_TITANIO =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/item/marcador_minas_titanio.png");

    public record Marcador(String nombre, int x, int z, ResourceLocation textura,
                           int texturaAncho, int texturaAlto, float escala, int colorNombre) {

        public Marcador(String nombre, int x, int z, ResourceLocation textura) {
            this(nombre, x, z, textura, 16, 16, 1.0F, 0xFFFFFFFF);
        }
    }

    public static final List<Marcador> FIJOS = List.of(
    );
}