package net.saullmc.pezntz.client.map;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.server.packs.resources.ResourceManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.List;

public class TexturaMapa extends AbstractTexture {

    private NativeImage[] niveles;

    private boolean suave = true;
    private boolean pixelado = true;

    public TexturaMapa(List<NativeImage> imagenes) {
        this.niveles = imagenes.toArray(new NativeImage[0]);

        NativeImage base = this.niveles[0];
        TextureUtil.prepareImage(this.getId(), this.niveles.length - 1, base.getWidth(), base.getHeight());

        for (int n = 0; n < this.niveles.length; n++) {
            NativeImage img = this.niveles[n];
            img.upload(n, 0, 0, 0, 0, img.getWidth(), img.getHeight(), false, true, this.niveles.length > 1, false);
        }

        aplicarFiltro();
    }

    public int niveles() {
        return this.niveles.length;
    }

    public NativeImage nivel(int n) {
        return this.niveles[n];
    }

    public int ancho() {
        return this.niveles.length == 0 ? 0 : this.niveles[0].getWidth();
    }

    public void filtro(boolean suave, boolean pixelado) {
        if (this.suave == suave && this.pixelado == pixelado) return;

        this.suave = suave;
        this.pixelado = pixelado;
        aplicarFiltro();
    }

    private void aplicarFiltro() {
        this.bind();

        boolean conNiveles = this.niveles.length > 1;

        int alejar = this.suave
                ? (conNiveles ? GL11.GL_LINEAR_MIPMAP_LINEAR : GL11.GL_LINEAR)
                : (conNiveles ? GL11.GL_NEAREST_MIPMAP_NEAREST : GL11.GL_NEAREST);

        int acercar = this.pixelado ? GL11.GL_NEAREST : GL11.GL_LINEAR;

        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, alejar);
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, acercar);
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
    }

    public void subirRegion(int n, int minX, int minY, int maxX, int maxY) {
        if (n < 0 || n >= this.niveles.length) return;
        if (maxX < minX || maxY < minY) return;

        this.bind();
        this.niveles[n].upload(n, minX, minY, minX, minY, maxX - minX + 1, maxY - minY + 1,
                false, true, this.niveles.length > 1, false);

        aplicarFiltro();
    }

    @Override
    public void load(ResourceManager recursos) {
    }

    @Override
    public void close() {
        for (NativeImage img : this.niveles) img.close();
        this.niveles = new NativeImage[0];
        this.releaseId();
    }
}