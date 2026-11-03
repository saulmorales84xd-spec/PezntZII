package net.saullmc.pezntz.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class FondoBorroso {

    public static final boolean ACTIVO = true;

    public static final int PASADAS = 2;

    public static final int COLOR_VELO = 0x66000000;

    private static final ResourceLocation EFECTO = new ResourceLocation("shaders/post/blur.json");

    @Nullable
    private PostChain cadena;

    private boolean fallo = false;

    private int ancho = -1;
    private int alto = -1;

    public void preparar() {
        if (!ACTIVO || this.fallo) return;

        Minecraft mc = Minecraft.getInstance();

        int ancho = mc.getWindow().getWidth();
        int alto = mc.getWindow().getHeight();

        if (this.cadena == null) {
            try {
                this.cadena = new PostChain(mc.getTextureManager(), mc.getResourceManager(),
                        mc.getMainRenderTarget(), EFECTO);
            } catch (Exception e) {
                this.fallo = true;
                return;
            }
        }

        if (this.ancho != ancho || this.alto != alto) {
            this.cadena.resize(ancho, alto);
            this.ancho = ancho;
            this.alto = alto;
        }
    }

    public boolean dibujar(float partialTick) {
        if (!ACTIVO || this.fallo || this.cadena == null) return false;

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.resetTextureMatrix();

        for (int i = 0; i < Math.max(1, PASADAS); i++) {
            this.cadena.process(partialTick);
        }

        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        return true;
    }

    public void cerrar() {
        if (this.cadena != null) {
            this.cadena.close();
            this.cadena = null;
        }
        this.ancho = -1;
        this.alto = -1;
    }
}