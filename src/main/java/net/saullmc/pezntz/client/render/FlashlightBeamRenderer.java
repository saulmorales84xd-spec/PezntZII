package net.saullmc.pezntz.client.render;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.custom.FlashlightItem;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT)
public class FlashlightBeamRenderer {

    /** Alcance del haz en bloques. */
    private static final float RANGE = 22.0f;

    /**
     * Medio angulo del cono. Estaba en 50, o sea 100 grados de apertura: eso es un foco de
     * obra, no una linterna. 24 da un haz reconocible con su nucleo y su halo.
     */
    private static final float HALF_ANGLE_DEG = 50.0f;

    private static final int MAX_VOLUMETRIC_LIGHTS = 4;
    private static final int VOLUMETRIC_STEPS = 24;

    /** Cuanta niebla se ve en el haz. Bajado de 0.5: antes tapaba lo que iluminaba. */
    private static final float VOLUMETRIC_INTENSITY = 0.26f;

    private static final float VOLUMETRIC_NOISE = 0.30f;

    /**
     * LA CAUSA DE QUE SE PERDIERAN LOS COLORES.
     *
     * Este numero multiplica el color de la escena. Estaba en 12: cualquier textura con algo
     * de claridad se iba directa a blanco y perdia su tono, y por eso bajar el brillo del
     * juego parecia arreglarlo a medias. Con 3.0 la luz se nota y la textura conserva su
     * color; el shader ademas reparte parte de la ganancia como suma en vez de producto.
     */
    private static final float BRIGHTNESS_MULTIPLIER = 9.0f;

    /**
     * Rango donde el shader considera que un pixel "ya tiene luz propia" y deja de
     * aplicarle la linterna. Bajado de 0.4/0.75 para que respete antes las zonas claras.
     */
    private static final float SELF_LIT_LOW = 0.22f;
    private static final float SELF_LIT_HIGH = 0.58f;

    /**
     * Cuanto le hace caso a la luz del entorno. 0.85 = de dia a cielo abierto la linterna
     * queda al 15% de fuerza; en una cueva, al 100%.
     */
    private static final float AMBIENT_INFLUENCE = 0.85f;

    /**
     * Bloques desde el foco en los que no se acumula niebla. Es lo que quita el punto
     * blanco que salia en la cara al verse en tercera persona.
     */
    private static final float NEAR_FADE = 1.6f;

    /**
     * Cuanto se adelanta el foco respecto al ojo. Una linterna se lleva en la mano, no
     * entre los ojos, y de paso ayuda a que el arranque del haz no toque la cabeza.
     */
    private static final float OFFSET_ADELANTE = 0.45f;

    private record ActiveLight(Vec3 origin, Vec3 dir) {}

    private static TextureTarget sceneTarget;

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (level == null) return;

        if (FlashlightShaders.volumetricShader == null || FlashlightShaders.copyShader == null) return;

        List<? extends Player> players = level.players();
        float partialTick = event.getPartialTick();
        List<ActiveLight> activeLights = new ArrayList<>();

        for (Player player : players) {
            if (activeLights.size() >= MAX_VOLUMETRIC_LIGHTS) break;

            ItemStack main = player.getMainHandItem();
            ItemStack off = player.getOffhandItem();

            boolean mainOn = main.getItem() instanceof FlashlightItem && FlashlightItem.estaEncendida(main);
            boolean offOn = off.getItem() instanceof FlashlightItem && FlashlightItem.estaEncendida(off);

            if (!mainOn && !offOn) continue;

            Vec3 mira = player.getViewVector(partialTick).normalize();

            activeLights.add(new ActiveLight(
                    player.getEyePosition(partialTick).add(mira.scale(OFFSET_ADELANTE)),
                    mira));
        }

        renderDarknessAndLighting(mc, level, activeLights, event.getProjectionMatrix(),
                event.getPoseStack(), event.getCamera());
    }

    private static void ensureSceneTarget(int width, int height) {
        if (sceneTarget == null || sceneTarget.width != width || sceneTarget.height != height) {
            if (sceneTarget != null) {
                sceneTarget.destroyBuffers();
            }
            sceneTarget = new TextureTarget(width, height, false, Minecraft.ON_OSX);
        }
    }

    private static void renderDarknessAndLighting(Minecraft mc, Level level, List<ActiveLight> lights,
                                                  Matrix4f projectionMatrix, PoseStack poseStack, Camera camera) {

        var mainTarget = mc.getMainRenderTarget();
        int width = mainTarget.width;
        int height = mainTarget.height;

        ensureSceneTarget(width, height);

        Vec3 camPos = camera.getPosition();
        poseStack.pushPose();
        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);
        Matrix4f mvp = new Matrix4f(projectionMatrix);
        mvp.mul(poseStack.last().pose());
        mvp.invert();
        poseStack.popPose();

        float coneCos = (float) Math.cos(Math.toRadians(HALF_ANGLE_DEG));

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.disableBlend();

        ShaderInstance lightingShader = FlashlightShaders.volumetricShader;

        sceneTarget.bindWrite(true);

        lightingShader.setSampler("uColorSampler", mainTarget.getColorTextureId());
        lightingShader.setSampler("uDepthSampler", mainTarget.getDepthTextureId());

        setMat4(lightingShader, "uInvMVP", mvp);
        setFloat2(lightingShader, "uScreenSize", (float) width, (float) height);
        setInt(lightingShader, "uLightCount", lights.size());

        setFloat3(lightingShader, "uLightColor", 1.0f, 0.95f, 0.85f);
        setFloat(lightingShader, "uBrightnessMultiplier", BRIGHTNESS_MULTIPLIER);

        int luzAqui = level.getMaxLocalRawBrightness(BlockPos.containing(camPos));
        setFloat(lightingShader, "uAmbient", luzAqui / 15.0f);
        setFloat(lightingShader, "uAmbientInfluence", AMBIENT_INFLUENCE);
        setFloat(lightingShader, "uNearFade", NEAR_FADE);

        setFloat(lightingShader, "uSelfLitLow", SELF_LIT_LOW);
        setFloat(lightingShader, "uSelfLitHigh", SELF_LIT_HIGH);

        setInt(lightingShader, "uVolumetricSteps", VOLUMETRIC_STEPS);
        setFloat(lightingShader, "uVolumetricIntensity", VOLUMETRIC_INTENSITY);
        setFloat(lightingShader, "uVolumetricNoise", VOLUMETRIC_NOISE);
        setFloat(lightingShader, "uGameTime", (float) level.getGameTime());

        for (int i = 0; i < MAX_VOLUMETRIC_LIGHTS; i++) {
            String suffix = String.valueOf(i);
            if (i < lights.size()) {
                ActiveLight l = lights.get(i);
                Vec3 dir = l.dir().normalize();
                setFloat3(lightingShader, "uLightOrigin" + suffix, (float) l.origin().x, (float) l.origin().y, (float) l.origin().z);
                setFloat3(lightingShader, "uLightDir" + suffix, (float) dir.x, (float) dir.y, (float) dir.z);
                setFloat(lightingShader, "uConeAngleCos" + suffix, coneCos);
                setFloat(lightingShader, "uRange" + suffix, RANGE);
                setFloat(lightingShader, "uIntensity" + suffix, 1.0f);
            } else {
                setFloat(lightingShader, "uRange" + suffix, 0.0f);
            }
        }

        drawFullscreenQuad(lightingShader);

        sceneTarget.unbindWrite();

        mainTarget.bindWrite(true);

        ShaderInstance copyShader = FlashlightShaders.copyShader;
        copyShader.setSampler("uSourceSampler", sceneTarget.getColorTextureId());
        setFloat2(copyShader, "uScreenSize", (float) width, (float) height);

        drawFullscreenQuad(copyShader);

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private static void drawFullscreenQuad(ShaderInstance shader) {
        RenderSystem.setShader(() -> shader);

        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        buffer.vertex(-1.0, -1.0, 0.0).endVertex();
        buffer.vertex(1.0, -1.0, 0.0).endVertex();
        buffer.vertex(1.0, 1.0, 0.0).endVertex();
        buffer.vertex(-1.0, 1.0, 0.0).endVertex();

        shader.apply();
        BufferUploader.drawWithShader(buffer.end());
        shader.clear();
    }

    private static void setFloat(ShaderInstance shader, String name, float v) {
        var u = shader.getUniform(name);
        if (u != null) u.set(v);
    }

    private static void setFloat2(ShaderInstance shader, String name, float a, float b) {
        var u = shader.getUniform(name);
        if (u != null) u.set(a, b);
    }

    private static void setFloat3(ShaderInstance shader, String name, float a, float b, float c) {
        var u = shader.getUniform(name);
        if (u != null) u.set(a, b, c);
    }

    private static void setInt(ShaderInstance shader, String name, int v) {
        var u = shader.getUniform(name);
        if (u != null) u.set(v);
    }

    private static void setMat4(ShaderInstance shader, String name, Matrix4f m) {
        var u = shader.getUniform(name);
        if (u != null) u.set(m);
    }
}