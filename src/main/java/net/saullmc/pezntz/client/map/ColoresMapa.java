package net.saullmc.pezntz.client.map;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.saullmc.pezntz.mapa.DatosMapa;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColoresMapa {

    public static final float FUERZA_RELIEVE = 1.6F;

    public static final float ALTURA_LUZ = 1.0F;

    public static final float BRILLO_MINIMO = 0.45F;
    public static final float BRILLO_MAXIMO = 1.35F;

    public static final int PROFUNDIDAD_AGUA = 12;
    public static final float OSCURECER_AGUA = 0.5F;

    public static final int COLOR_SIN_DATOS = 0x000000;

    public static final int COLOR_PASTO_POR_DEFECTO = 0x91BD59;
    public static final int COLOR_AGUA_POR_DEFECTO = 0x3F76E4;

    private static final float LUZ_X;
    private static final float LUZ_Y;
    private static final float LUZ_Z;

    static {
        float largo = (float) Math.sqrt(2.0D + ALTURA_LUZ * ALTURA_LUZ);
        LUZ_X = -1.0F / largo;
        LUZ_Y = ALTURA_LUZ / largo;
        LUZ_Z = -1.0F / largo;
    }

    private final Map<ResourceLocation, Integer> promedios = new HashMap<>();

    private final List<TextureAtlasSprite> sprites = new ArrayList<>();
    private final List<Integer> tintes = new ArrayList<>();
    private final List<Integer> promedioEstado = new ArrayList<>();
    private final List<Boolean> fluido = new ArrayList<>();
    private final List<Boolean> preparado = new ArrayList<>();

    private final List<int[]> tablaBase = new ArrayList<>();
    private final List<int[]> tablaTinte = new ArrayList<>();

    private final List<Biome> biomas = new ArrayList<>();

    private final Tinte getter = new Tinte();

    public void olvidarEstado(int e) {
        if (e < 0 || e >= this.preparado.size()) return;

        this.preparado.set(e, false);
        if (e < this.tablaBase.size()) this.tablaBase.set(e, null);
        if (e < this.tablaTinte.size()) this.tablaTinte.set(e, null);
    }

    public void olvidarBioma(int b) {
        if (b >= 0 && b < this.biomas.size()) this.biomas.set(b, null);

        for (int i = 0; i < this.tablaBase.size(); i++) {
            int[] fila = this.tablaBase.get(i);
            if (fila != null && b < fila.length) fila[b] = 0;
        }

        for (int i = 0; i < this.tablaTinte.size(); i++) {
            int[] fila = this.tablaTinte.get(i);
            if (fila != null && b < fila.length) fila[b] = 0;
        }
    }

    public int[][] prepararTodo(DatosMapa d) {
        int estados = d.estados.size();
        int biomas = Math.max(1, d.biomas.size());

        boolean[] usado = new boolean[estados * biomas];

        for (int c = 0; c < d.estado.length; c++) {
            int e = d.estado[c];
            if (e < 0 || e >= estados) continue;

            int b = d.bioma[c];
            if (b < 0 || b >= biomas) b = 0;

            usado[e * biomas + b] = true;
        }

        int[][] tabla = new int[estados][];

        for (int e = 0; e < estados; e++) {
            for (int b = 0; b < biomas; b++) {
                if (!usado[e * biomas + b]) continue;
                base(d, e, b);
            }

            int[] fila = e < this.tablaBase.size() ? this.tablaBase.get(e) : null;
            tabla[e] = fila == null ? new int[0] : fila.clone();
        }

        return tabla;
    }

    public int base(DatosMapa d, int e, int b) {
        if (e < 0 || e >= d.estados.size()) return COLOR_SIN_DATOS;
        if (b < 0 || b >= d.biomas.size()) b = 0;

        int[] fila = fila(this.tablaBase, e, d.biomas.size());
        if (fila[b] != 0) return fila[b] & 0xFFFFFF;

        preparar(d, e);

        int promedio = this.promedioEstado.get(e);
        int tinte = tinte(d, e, b);

        int r = ((promedio >> 16) & 255) * ((tinte >> 16) & 255) / 255;
        int g = ((promedio >> 8) & 255) * ((tinte >> 8) & 255) / 255;
        int bl = (promedio & 255) * (tinte & 255) / 255;

        int color = (r << 16) | (g << 8) | bl;
        fila[b] = color | 0xFF000000;
        return color;
    }

    public int tinte(DatosMapa d, int e, int b) {
        if (e < 0 || e >= d.estados.size()) return 0xFFFFFF;
        if (b < 0 || b >= d.biomas.size()) b = 0;

        int[] fila = fila(this.tablaTinte, e, d.biomas.size());
        if (fila[b] != 0) return fila[b] & 0xFFFFFF;

        preparar(d, e);

        BlockState estado = d.estados.get(e);
        Biome bioma = bioma(d, b);

        int color = 0xFFFFFF;

        if (this.fluido.get(e)) {
            if (bioma == null) {
                color = estado.getFluidState().is(net.minecraft.tags.FluidTags.WATER) ? COLOR_AGUA_POR_DEFECTO : 0xFFFFFF;
            } else {
                try {
                    this.getter.bioma = bioma;
                    color = IClientFluidTypeExtensions.of(estado.getFluidState())
                            .getTintColor(estado.getFluidState(), this.getter, BlockPos.ZERO) & 0xFFFFFF;
                } catch (Throwable error) {
                    color = COLOR_AGUA_POR_DEFECTO;
                }
            }
        } else if (this.tintes.get(e) >= 0) {
            if (bioma == null) {
                color = COLOR_PASTO_POR_DEFECTO;
            } else {
                try {
                    this.getter.bioma = bioma;
                    int c = Minecraft.getInstance().getBlockColors().getColor(estado, this.getter, BlockPos.ZERO, this.tintes.get(e));
                    color = c == -1 ? 0xFFFFFF : c & 0xFFFFFF;
                } catch (Throwable error) {
                    color = COLOR_PASTO_POR_DEFECTO;
                }
            }
        }

        fila[b] = color | 0xFF000000;
        return color;
    }

    @Nullable
    public TextureAtlasSprite sprite(DatosMapa d, int e) {
        if (e < 0 || e >= d.estados.size()) return null;

        preparar(d, e);
        return this.sprites.get(e);
    }

    private static int[] fila(List<int[]> tabla, int e, int biomas) {
        while (tabla.size() <= e) tabla.add(null);

        int[] fila = tabla.get(e);
        int largo = Math.max(1, biomas);

        if (fila == null || fila.length < largo) {
            int[] nueva = new int[largo];
            if (fila != null) System.arraycopy(fila, 0, nueva, 0, fila.length);
            tabla.set(e, nueva);
            fila = nueva;
        }

        return fila;
    }

    @Nullable
    private Biome bioma(DatosMapa d, int b) {
        if (b < 0 || b >= d.biomas.size()) return null;

        while (this.biomas.size() <= b) this.biomas.add(null);

        Biome guardado = this.biomas.get(b);
        if (guardado != null) return guardado;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return null;

        Registry<Biome> registro = mc.level.registryAccess().registryOrThrow(Registries.BIOME);

        Biome bioma = null;
        ResourceLocation id = ResourceLocation.tryParse(d.biomas.get(b));
        if (id != null) bioma = registro.get(id);
        if (bioma == null) bioma = registro.get(Biomes.PLAINS);

        this.biomas.set(b, bioma);
        return bioma;
    }

    private void preparar(DatosMapa d, int e) {
        while (this.preparado.size() <= e) {
            this.preparado.add(false);
            this.sprites.add(null);
            this.tintes.add(-1);
            this.promedioEstado.add(0x808080);
            this.fluido.add(false);
        }

        if (this.preparado.get(e)) return;
        this.preparado.set(e, true);

        Minecraft mc = Minecraft.getInstance();
        BlockState estado = d.estados.get(e);

        TextureAtlasSprite sprite = null;
        int tinte = -1;
        boolean esFluido = false;

        FluidState f = estado.getFluidState();

        try {
            if (!f.isEmpty() && estado.getBlock() instanceof LiquidBlock) {
                esFluido = true;

                ResourceLocation textura = IClientFluidTypeExtensions.of(f).getStillTexture();
                if (textura != null) sprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(textura);

            } else {
                BakedModel modelo = mc.getBlockRenderer().getBlockModel(estado);
                RandomSource azar = RandomSource.create(42L);

                List<BakedQuad> quads = modelo.getQuads(estado, Direction.UP, azar);
                if (quads.isEmpty()) quads = modelo.getQuads(estado, null, azar);

                if (!quads.isEmpty()) {
                    BakedQuad quad = quads.get(0);
                    sprite = quad.getSprite();
                    if (quad.isTinted()) tinte = quad.getTintIndex();
                } else {
                    sprite = modelo.getParticleIcon();
                }
            }
        } catch (Throwable error) {
            sprite = null;
            tinte = -1;
        }

        if (sprite != null && MissingTextureAtlasSprite.getLocation().equals(sprite.contents().name())) {
            sprite = null;
        }

        int promedio = sprite == null ? -1 : promedio(sprite);

        if (promedio < 0) {
            try {
                promedio = estado.getMapColor(this.getter, BlockPos.ZERO).col;
            } catch (Throwable error) {
                promedio = 0x808080;
            }
        }

        this.sprites.set(e, sprite);
        this.tintes.set(e, tinte);
        this.promedioEstado.set(e, promedio & 0xFFFFFF);
        this.fluido.set(e, esFluido);
    }

    private int promedio(TextureAtlasSprite sprite) {
        ResourceLocation nombre = sprite.contents().name();

        Integer guardado = this.promedios.get(nombre);
        if (guardado != null) return guardado;

        int color = -1;

        ResourceLocation archivo = new ResourceLocation(nombre.getNamespace(), "textures/" + nombre.getPath() + ".png");
        Optional<Resource> recurso = Minecraft.getInstance().getResourceManager().getResource(archivo);

        if (recurso.isPresent()) {
            try (InputStream entrada = recurso.get().open(); NativeImage imagen = NativeImage.read(entrada)) {
                long r = 0L;
                long g = 0L;
                long b = 0L;
                long peso = 0L;

                for (int y = 0; y < imagen.getHeight(); y++) {
                    for (int x = 0; x < imagen.getWidth(); x++) {
                        int abgr = imagen.getPixelRGBA(x, y);
                        int a = (abgr >>> 24) & 255;
                        if (a < 16) continue;

                        r += (long) (abgr & 255) * a;
                        g += (long) ((abgr >> 8) & 255) * a;
                        b += (long) ((abgr >> 16) & 255) * a;
                        peso += a;
                    }
                }

                if (peso > 0L) {
                    color = ((int) (r / peso) << 16) | ((int) (g / peso) << 8) | (int) (b / peso);
                }
            } catch (Exception ignorado) {
            }
        }

        this.promedios.put(nombre, color);
        return color;
    }

    public static float brillo(DatosMapa d, int i, int j) {
        int c = j * d.lado + i;

        int agua = d.agua[c];
        if (agua > 0) {
            float hondo = Math.min(agua, PROFUNDIDAD_AGUA) / (float) PROFUNDIDAD_AGUA;
            return 1.0F - hondo * OSCURECER_AGUA;
        }

        int h = d.alto[c];

        int oeste = vecina(d, i - 1, j, h);
        int este = vecina(d, i + 1, j, h);
        int norte = vecina(d, i, j - 1, h);
        int sur = vecina(d, i, j + 1, h);

        float dx = (este - oeste) / (2.0F * d.bpp);
        float dz = (sur - norte) / (2.0F * d.bpp);

        float nx = -dx;
        float nz = -dz;
        float largo = (float) Math.sqrt(nx * nx + 1.0F + nz * nz);

        float luz = (nx * LUZ_X + LUZ_Y + nz * LUZ_Z) / largo;
        float brillo = 1.0F + FUERZA_RELIEVE * (luz - LUZ_Y);

        return Math.max(BRILLO_MINIMO, Math.min(BRILLO_MAXIMO, brillo));
    }

    private static int vecina(DatosMapa d, int i, int j, int porDefecto) {
        if (i < 0 || j < 0 || i >= d.lado || j >= d.lado) return porDefecto;

        int c = j * d.lado + i;
        if (d.estado[c] < 0) return porDefecto;

        return d.alto[c];
    }

    public static int pixel(int rgb, float brillo) {
        int r = Math.min(255, Math.max(0, Math.round(((rgb >> 16) & 255) * brillo)));
        int g = Math.min(255, Math.max(0, Math.round(((rgb >> 8) & 255) * brillo)));
        int b = Math.min(255, Math.max(0, Math.round((rgb & 255) * brillo)));
        return 0xFF000000 | (b << 16) | (g << 8) | r;
    }

    public static int[] pintar(DatosMapa d, int[][] tabla) {
        int[] pixeles = new int[d.lado * d.lado];
        int sinDatos = pixel(COLOR_SIN_DATOS, 1.0F);

        for (int j = 0; j < d.lado; j++) {
            for (int i = 0; i < d.lado; i++) {
                int c = j * d.lado + i;
                int e = d.estado[c];

                if (e < 0 || e >= tabla.length) {
                    pixeles[c] = sinDatos;
                    continue;
                }

                int b = d.bioma[c];
                int[] fila = tabla[e];
                int base = (b >= 0 && b < fila.length && fila[b] != 0) ? fila[b] & 0xFFFFFF : 0x808080;

                pixeles[c] = pixel(base, brillo(d, i, j));
            }
        }

        return pixeles;
    }

    private static final class Tinte implements BlockAndTintGetter {
        Biome bioma;

        @Override
        public float getShade(Direction direccion, boolean sombra) {
            return 1.0F;
        }

        @Override
        public LevelLightEngine getLightEngine() {
            Minecraft mc = Minecraft.getInstance();
            return mc.level == null ? null : mc.level.getLightEngine();
        }

        @Override
        public int getBlockTint(BlockPos pos, ColorResolver resolver) {
            return resolver.getColor(this.bioma, pos.getX(), pos.getZ());
        }

        @Override
        public BlockEntity getBlockEntity(BlockPos pos) {
            return null;
        }

        @Override
        public BlockState getBlockState(BlockPos pos) {
            return Blocks.AIR.defaultBlockState();
        }

        @Override
        public FluidState getFluidState(BlockPos pos) {
            return Fluids.EMPTY.defaultFluidState();
        }

        @Override
        public int getHeight() {
            return 384;
        }

        @Override
        public int getMinBuildHeight() {
            return -64;
        }
    }
}