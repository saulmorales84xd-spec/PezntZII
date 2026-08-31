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

    private static final float RANGE = 24.0f;
    private static final float HALF_ANGLE_DEG = 50.0f;
    private static final int MAX_VOLUMETRIC_LIGHTS = 4;
    private static final int VOLUMETRIC_STEPS = 24;
    private static final float VOLUMETRIC_INTENSITY = 0.5f;
    private static final float VOLUMETRIC_NOISE = 0.35f;

    private static final float BRIGHTNESS_MULTIPLIER = 12.0f;

    private static final float SELF_LIT_LOW = 0.4f;
    private static final float SELF_LIT_HIGH = 0.75f;

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

            boolean mainOn = main.getItem() instanceof FlashlightItem && main.getOrCreateTag().getBoolean("IsOn");
            boolean offOn = off.getItem() instanceof FlashlightItem && off.getOrCreateTag().getBoolean("IsOn");

            if (!mainOn && !offOn) continue;

            activeLights.add(new ActiveLight(
                    player.getEyePosition(partialTick),
                    player.getViewVector(partialTick)));
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