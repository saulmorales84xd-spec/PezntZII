package net.saullmc.pezntz.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT)
public class LinternaMundo {

    public static final boolean ACTIVA = true;

    public static final float[] COLOR = {1.0F, 0.97F, 0.92F};

    public static final float INTENSIDAD = 1.6F;

    public static final float CAIDA = 1.6F;

    public static final float CERCA = 0.6F;

    public static final float LAMBERT = 0.55F;

    public static final float LUZ_MANO = 0.30F;

    private static final int MAXIMO = 4;

    private static final String[] ORIGEN = {"PzOrigen0", "PzOrigen1", "PzOrigen2", "PzOrigen3"};
    private static final String[] DIRECCION = {"PzDir0", "PzDir1", "PzDir2", "PzDir3"};
    private static final String[] ALCANCE = {"PzAlcance0", "PzAlcance1", "PzAlcance2", "PzAlcance3"};

    private static final List<ShaderInstance> SHADERS = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void alColorNiebla(ViewportEvent.ComputeFogColor evento) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        Vec3 camara = evento.getCamera().getPosition();
        float partialTick = (float) evento.getPartialTick();

        List<LucesLinterna.Luz> luces = ACTIVA
                ? LucesLinterna.recoger(mc.level, partialTick, camara)
                : List.of();

        encender(luces, camara);
    }

    @SubscribeEvent
    public static void alRenderizar(RenderLevelStageEvent evento) {
        if (evento.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;

        float relleno = (ACTIVA && LucesLinterna.laMiaEncendida()) ? LUZ_MANO : 0.0F;

        for (ShaderInstance shader : shaders()) {
            shader.safeGetUniform("PzLuces").set(0);
            shader.safeGetUniform("PzRelleno").set(relleno);
        }
    }

    private static void encender(List<LucesLinterna.Luz> luces, Vec3 camara) {
        int cuantas = Math.min(luces.size(), MAXIMO);
        float cosCono = (float) Math.cos(Math.toRadians(LucesLinterna.MEDIO_ANGULO));

        for (ShaderInstance shader : shaders()) {
            shader.safeGetUniform("PzLuces").set(cuantas);

            for (int i = 0; i < cuantas; i++) {
                LucesLinterna.Luz luz = luces.get(i);
                Vec3 dir = luz.dir();

                shader.safeGetUniform(ORIGEN[i]).set(
                        (float) (luz.origen().x - camara.x),
                        (float) (luz.origen().y - camara.y),
                        (float) (luz.origen().z - camara.z));

                shader.safeGetUniform(DIRECCION[i]).set((float) dir.x, (float) dir.y, (float) dir.z);
                shader.safeGetUniform(ALCANCE[i]).set(luz.alcance());
            }

            shader.safeGetUniform("PzCosCono").set(cosCono);
            shader.safeGetUniform("PzColorLuz").set(COLOR[0], COLOR[1], COLOR[2]);
            shader.safeGetUniform("PzIntensidad").set(INTENSIDAD);
            shader.safeGetUniform("PzCaida").set(Math.max(0.1F, CAIDA));
            shader.safeGetUniform("PzCerca").set(CERCA);
            shader.safeGetUniform("PzLambert").set(LAMBERT);
            shader.safeGetUniform("PzRelleno").set(0.0F);
        }
    }

    private static List<ShaderInstance> shaders() {
        SHADERS.clear();

        agregar(GameRenderer.getRendertypeSolidShader());
        agregar(GameRenderer.getRendertypeCutoutMippedShader());
        agregar(GameRenderer.getRendertypeCutoutShader());
        agregar(GameRenderer.getRendertypeTranslucentShader());
        agregar(GameRenderer.getRendertypeTripwireShader());

        agregar(GameRenderer.getRendertypeEntitySolidShader());
        agregar(GameRenderer.getRendertypeEntityCutoutShader());
        agregar(GameRenderer.getRendertypeEntityCutoutNoCullShader());
        agregar(GameRenderer.getRendertypeEntityCutoutNoCullZOffsetShader());
        agregar(GameRenderer.getRendertypeEntityTranslucentShader());
        agregar(GameRenderer.getRendertypeEntitySmoothCutoutShader());

        agregar(GameRenderer.getRendertypeEntityNoOutlineShader());
        agregar(GameRenderer.getRendertypeEntityDecalShader());
        agregar(GameRenderer.getRendertypeEntityTranslucentCullShader());
        agregar(GameRenderer.getRendertypeItemEntityTranslucentCullShader());
        agregar(GameRenderer.getRendertypeArmorCutoutNoCullShader());

        agregar(GameRenderer.getParticleShader());

        return SHADERS;
    }

    private static void agregar(ShaderInstance shader) {
        if (shader != null) SHADERS.add(shader);
    }
}