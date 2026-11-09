package net.saullmc.pezntz.client.render;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT)
public class OscuridadLuz {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean ACTIVA = true;

    public static final float CURVA_CIELO = 3.0F;

    public static final int CIELO_MINIMO = 0;

    public static final float BRILLO_DIA = 1.0F;

    public static final float CURVA_DIA = 1.0F;

    public static final float CURVA_BLOQUE = 2.2F;

    public static final int BLOQUE_MINIMO = 1;

    public static final float BRILLO_BLOQUE = 1.0F;

    public static final float[] COLOR_BLOQUE = {1.0F, 0.95F, 0.88F};

    public static final float PARPADEO = 1.0F;

    public static final boolean VISION_NOCTURNA = true;

    public static final float NIVEL_VISION_NOCTURNA = 0.75F;

    public static final boolean RELAMPAGOS = true;

    public static final boolean OSCURIDAD_EN_NETHER = true;

    public static final boolean NIEBLA_NEGRA = true;

    public static final float SUAVIZADO_NIEBLA = 0.08F;

    private static final String NOMBRE_MAPA = "dynamic/light_map_";

    public static final int INTENTOS_NOMBRE = 3;

    private static final int LADO = 16;

    private static boolean roto = false;
    private static boolean avisado = false;

    private static DynamicTexture textura;

    private static final float[] CIELO = new float[LADO];
    private static final float[] BLOQUE = new float[LADO];

    private static final RandomSource AZAR = RandomSource.create();

    private static float parpadeo = 0.0F;

    private static float nieblaSuave = 1.0F;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void alColorNiebla(ViewportEvent.ComputeFogColor evento) {
        if (!ACTIVA || roto) return;

        Minecraft mc = Minecraft.getInstance();
        ClientLevel nivel = mc.level;
        if (nivel == null) return;
        if (!seAplica(nivel)) return;

        float partialTick = (float) evento.getPartialTick();
        float dia = dia(nivel, partialTick);

        escribirMapa(mc, partialTick, dia);

        if (NIEBLA_NEGRA) {
            oscurecerNiebla(evento, nivel, dia);
        }
    }

    @SubscribeEvent
    public static void alTick(TickEvent.ClientTickEvent evento) {
        if (evento.phase != TickEvent.Phase.END) return;

        parpadeo += (float) ((AZAR.nextDouble() - AZAR.nextDouble())
                * AZAR.nextDouble() * AZAR.nextDouble() * 0.1D);
        parpadeo *= 0.9F;
    }

    private static boolean seAplica(ClientLevel nivel) {
        if (nivel.effects().forceBrightLightmap()) return false;
        if (!OSCURIDAD_EN_NETHER && nivel.dimension() == Level.NETHER) return false;
        return true;
    }

    private static float dia(ClientLevel nivel, float partialTick) {
        if (RELAMPAGOS && nivel.getSkyFlashTime() > 0) return 1.0F;

        float cielo = nivel.getSkyDarken(partialTick);
        float d = Mth.clamp((cielo - 0.2F) / 0.8F, 0.0F, 1.0F);

        return (float) Math.pow(d, CURVA_DIA);
    }

    private static void escribirMapa(Minecraft mc, float partialTick, float dia) {
        NativeImage pixeles = pixeles(mc);
        if (pixeles == null) return;

        float vision = 0.0F;
        if (VISION_NOCTURNA && mc.player != null && mc.player.hasEffect(MobEffects.NIGHT_VISION)) {
            vision = GameRenderer.getNightVisionScale(mc.player, partialTick);
        }

        float tembleque = 1.0F + parpadeo * PARPADEO;

        for (int i = 0; i < LADO; i++) {
            CIELO[i] = (i < CIELO_MINIMO)
                    ? 0.0F
                    : (float) Math.pow(i / 15.0F, CURVA_CIELO) * BRILLO_DIA * dia;

            BLOQUE[i] = (i < BLOQUE_MINIMO)
                    ? 0.0F
                    : Mth.clamp((float) Math.pow(i / 15.0F, CURVA_BLOQUE) * BRILLO_BLOQUE * tembleque, 0.0F, 1.0F);
        }

        for (int cielo = 0; cielo < LADO; cielo++) {
            for (int bloque = 0; bloque < LADO; bloque++) {
                float c = Mth.clamp(CIELO[cielo], 0.0F, 1.0F);
                float b = BLOQUE[bloque];

                float r = 1.0F - (1.0F - c) * (1.0F - b * COLOR_BLOQUE[0]);
                float g = 1.0F - (1.0F - c) * (1.0F - b * COLOR_BLOQUE[1]);
                float a = 1.0F - (1.0F - c) * (1.0F - b * COLOR_BLOQUE[2]);

                if (vision > 0.0F) {
                    r = Mth.lerp(vision, r, Math.max(r, NIVEL_VISION_NOCTURNA));
                    g = Mth.lerp(vision, g, Math.max(g, NIVEL_VISION_NOCTURNA));
                    a = Mth.lerp(vision, a, Math.max(a, NIVEL_VISION_NOCTURNA));
                }

                if (cielo == LADO - 1 && bloque == LADO - 1) {
                    r = 1.0F;
                    g = 1.0F;
                    a = 1.0F;
                }

                pixeles.setPixelRGBA(bloque, cielo, empaquetar(r, g, a));
            }
        }

        textura.upload();
    }

    private static int empaquetar(float r, float g, float b) {
        int ri = Mth.clamp(Math.round(r * 255.0F), 0, 255);
        int gi = Mth.clamp(Math.round(g * 255.0F), 0, 255);
        int bi = Mth.clamp(Math.round(b * 255.0F), 0, 255);

        return 0xFF000000 | (bi << 16) | (gi << 8) | ri;
    }

    private static void oscurecerNiebla(ViewportEvent.ComputeFogColor evento, ClientLevel nivel, float dia) {
        if (evento.getCamera().getFluidInCamera() == FogType.LAVA) return;

        BlockPos pos = BlockPos.containing(evento.getCamera().getPosition());
        float cieloCamara = nivel.getBrightness(LightLayer.SKY, pos) / 15.0F;

        float objetivo = Mth.clamp(dia * cieloCamara, 0.0F, 1.0F);
        nieblaSuave += (objetivo - nieblaSuave) * Mth.clamp(SUAVIZADO_NIEBLA, 0.001F, 1.0F);

        evento.setRed(evento.getRed() * nieblaSuave);
        evento.setGreen(evento.getGreen() * nieblaSuave);
        evento.setBlue(evento.getBlue() * nieblaSuave);
    }

    private static NativeImage pixeles(Minecraft mc) {
        if (textura == null) {
            textura = buscar(mc);

            if (textura == null) {
                apagar("no se encontro el mapa de luz en " + NOMBRE_MAPA + "N");
                return null;
            }
        }

        NativeImage pixeles = textura.getPixels();

        if (pixeles == null || pixeles.getWidth() != LADO || pixeles.getHeight() != LADO) {
            apagar("el mapa de luz no mide 16x16");
            return null;
        }

        return pixeles;
    }

    private static DynamicTexture buscar(Minecraft mc) {
        for (int i = 1; i <= INTENTOS_NOMBRE; i++) {
            AbstractTexture candidata = mc.getTextureManager()
                    .getTexture(new ResourceLocation(NOMBRE_MAPA + i));

            if (candidata instanceof DynamicTexture dinamica
                    && dinamica.getPixels() != null
                    && dinamica.getPixels().getWidth() == LADO
                    && dinamica.getPixels().getHeight() == LADO) {
                return dinamica;
            }
        }

        return null;
    }

    private static void apagar(String motivo) {
        roto = true;
        textura = null;

        if (!avisado) {
            avisado = true;
            LOGGER.warn("[PezntZ] Oscuridad desactivada: {}", motivo);
        }
    }

    public static void reiniciar() {
        roto = false;
        avisado = false;
        textura = null;
    }
}