package net.saullmc.pezntz.client.render;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import org.joml.Matrix4f;

import java.util.List;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT)
public class FlashlightBeamRenderer {

    public static final boolean NIEBLA_ACTIVA = false;

    private static final int VOLUMETRIC_STEPS = 24;
    private static final float VOLUMETRIC_INTENSITY = 0.008f;
    private static final float VOLUMETRIC_NOISE = 0.30f;
    private static final float SELF_LIT_LOW = 0.6f;
    private static final float SELF_LIT_HIGH = 0.9f;
    private static final float NEAR_FADE = 1.6f;
    private static final float SHADOW_BIAS = 0.0015f;

    private static final float SHADOW_STEPS_PER_BLOCK = 2.0f;

    private static final int SHADOW_STEPS_VOLUMETRIC = 6;

    private static final float VOLUMETRIC_STEPS_PER_BLOCK = 1.2f;

    private static final int NOISE_OCTAVES = 2;

    private static final boolean REPARTIR_PASOS_ENTRE_LUCES = true;

    private static final int VOLUMETRIC_STEPS_MINIMO = 10;

    private static TextureTarget sceneTarget;

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (!NIEBLA_ACTIVA) return;
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;

        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (level == null) return;

        if (FlashlightShaders.volumetricShader == null || FlashlightShaders.copyShader == null) return;

        Vec3 camPos = event.getCamera().getPosition();
        List<LucesLinterna.Luz> luces = LucesLinterna.recoger(level, event.getPartialTick(), camPos);

        if (luces.isEmpty()) return;

        renderNiebla(mc, level, luces, event.getProjectionMatrix(), event.getPoseStack(), event.getCamera());
    }

    private static void ensureSceneTarget(int width, int height) {
        if (sceneTarget == null || sceneTarget.width != width || sceneTarget.height != height) {
            if (sceneTarget != null) {
                sceneTarget.destroyBuffers();
            }
            sceneTarget = new TextureTarget(width, height, false, Minecraft.ON_OSX);
        }
    }

    private static void renderNiebla(Minecraft mc, Level level, List<LucesLinterna.Luz> lights,
                                     Matrix4f projectionMatrix, PoseStack poseStack, Camera camera) {

        var mainTarget = mc.getMainRenderTarget();
        int width = mainTarget.width;
        int height = mainTarget.height;

        ensureSceneTarget(width, height);

        Vec3 camPos = camera.getPosition();
        poseStack.pushPose();
        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);
        Matrix4f mvpDirecta = new Matrix4f(projectionMatrix);
        mvpDirecta.mul(poseStack.last().pose());

        Matrix4f mvp = new Matrix4f(mvpDirecta);
        mvp.invert();
        poseStack.popPose();

        float coneCos = (float) Math.cos(Math.toRadians(LucesLinterna.MEDIO_ANGULO));

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.disableBlend();

        ShaderInstance shader = FlashlightShaders.volumetricShader;

        sceneTarget.bindWrite(true);

        shader.setSampler("uColorSampler", mainTarget.getColorTextureId());
        shader.setSampler("uDepthSampler", mainTarget.getDepthTextureId());

        setMat4(shader, "uInvMVP", mvp);
        setMat4(shader, "uMVP", mvpDirecta);
        setFloat2(shader, "uScreenSize", (float) width, (float) height);
        setInt(shader, "uLightCount", lights.size());

        setFloat3(shader, "uLightColor", 1.0f, 0.95f, 0.85f);
        setFloat(shader, "uNearFade", NEAR_FADE);
        setFloat(shader, "uShadowBias", SHADOW_BIAS);
        setFloat(shader, "uShadowStepsPerBlock", SHADOW_STEPS_PER_BLOCK);
        setInt(shader, "uShadowStepsVolumetric", SHADOW_STEPS_VOLUMETRIC);
        setFloat(shader, "uVolumetricStepsPerBlock", VOLUMETRIC_STEPS_PER_BLOCK);
        setInt(shader, "uNoiseOctaves", NOISE_OCTAVES);

        setFloat(shader, "uSelfLitLow", SELF_LIT_LOW);
        setFloat(shader, "uSelfLitHigh", SELF_LIT_HIGH);

        int pasosNiebla = VOLUMETRIC_STEPS;
        if (REPARTIR_PASOS_ENTRE_LUCES && lights.size() > 1) {
            pasosNiebla = Math.max(VOLUMETRIC_STEPS_MINIMO, VOLUMETRIC_STEPS / lights.size());
        }
        setInt(shader, "uVolumetricSteps", pasosNiebla);
        setFloat(shader, "uVolumetricIntensity", VOLUMETRIC_INTENSITY);
        setFloat(shader, "uVolumetricNoise", VOLUMETRIC_NOISE);
        setFloat(shader, "uGameTime", (float) level.getGameTime());

        for (int i = 0; i < LucesLinterna.MAXIMO; i++) {
            String suffix = String.valueOf(i);
            if (i < lights.size()) {
                LucesLinterna.Luz l = lights.get(i);
                Vec3 dir = l.dir().normalize();
                setFloat3(shader, "uLightOrigin" + suffix, (float) l.origen().x, (float) l.origen().y, (float) l.origen().z);
                setFloat3(shader, "uLightDir" + suffix, (float) dir.x, (float) dir.y, (float) dir.z);
                setFloat(shader, "uConeAngleCos" + suffix, coneCos);
                setFloat(shader, "uRange" + suffix, l.alcance());
                setFloat(shader, "uIntensity" + suffix, 1.0f);
            } else {
                setFloat(shader, "uRange" + suffix, 0.0f);
            }
        }

        drawFullscreenQuad(shader);

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