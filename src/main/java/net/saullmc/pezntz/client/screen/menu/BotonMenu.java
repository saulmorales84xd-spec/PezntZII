package net.saullmc.pezntz.client.screen.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class BotonMenu extends AbstractButton {

    public static final int GROSOR_BORDE = 2;

    public static final int COLOR_BORDE_SOBRE = 0xFFFFFFFF;

    private final ResourceLocation textura;
    private final SoundEvent sonido;
    private final float tono;
    private final Runnable alPulsar;

    public BotonMenu(int x, int y, int ancho, int alto, Component titulo,
                     ResourceLocation textura, SoundEvent sonido, float tono, Runnable alPulsar) {
        super(x, y, ancho, alto, titulo);
        this.textura = textura;
        this.sonido = sonido;
        this.tono = tono;
        this.alPulsar = alPulsar;
    }

    @Override
    public void onPress() {
        if (this.sonido != null) {
            Minecraft.getInstance().getSoundManager().play(
                    SimpleSoundInstance.forUI(this.sonido, this.tono));
        }
        this.alPulsar.run();
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        if (this.isHoveredOrFocused() && GROSOR_BORDE > 0) {
            g.fill(this.getX() - GROSOR_BORDE, this.getY() - GROSOR_BORDE,
                    this.getX() + this.width + GROSOR_BORDE, this.getY() + this.height + GROSOR_BORDE,
                    COLOR_BORDE_SOBRE);
        }

        g.blit(this.textura, this.getX(), this.getY(), 0, 0,
                this.width, this.height, this.width, this.height);
    }

    @Override
    public void playDownSound(net.minecraft.client.sounds.SoundManager manager) {
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput salida) {
        this.defaultButtonNarrationText(salida);
    }
}