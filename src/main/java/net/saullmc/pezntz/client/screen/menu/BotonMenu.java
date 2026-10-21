package net.saullmc.pezntz.client.screen.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

public class BotonMenu extends AbstractButton {

    private final ResourceLocation textura;
    private final ItemStack tooltip;
    private final SoundEvent sonido;
    private final float tono;
    private final Runnable alPulsar;

    public BotonMenu(int x, int y, int ancho, int alto, Component titulo, ResourceLocation textura,
                     ItemStack tooltip, SoundEvent sonido, float tono, Runnable alPulsar) {
        super(x, y, ancho, alto, titulo);
        this.textura = textura;
        this.tooltip = tooltip;
        this.sonido = sonido;
        this.tono = tono;
        this.alPulsar = alPulsar;
    }

    public ItemStack getItemTooltip() {
        return this.tooltip;
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