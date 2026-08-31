package net.saullmc.pezntz.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FlashlightShaders {

    public static ShaderInstance volumetricShader;
    public static ShaderInstance copyShader;

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(
                        event.getResourceProvider(),
                        new ResourceLocation(PezntZMod.MOD_ID, "flashlight_volumetric"),
                        DefaultVertexFormat.POSITION
                ),
                shader -> volumetricShader = shader
        );

        event.registerShader(
                new ShaderInstance(
                        event.getResourceProvider(),
                        new ResourceLocation(PezntZMod.MOD_ID, "flashlight_copy"),
                        DefaultVertexFormat.POSITION
                ),
                shader -> copyShader = shader
        );
    }
}