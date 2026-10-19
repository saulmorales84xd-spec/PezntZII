package net.saullmc.pezntz.client.screen.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MenuSeccionScreen extends Screen {

    public static final int BOTON_ANCHO = 100;
    public static final int BOTON_ALTO = 20;

    private final int seccion;

    public MenuSeccionScreen(Component titulo, int seccion) {
        super(titulo);
        this.seccion = seccion;
    }

    public int getSeccion() {
        return this.seccion;
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(
                        Component.literal("Volver"),
                        boton -> Minecraft.getInstance().setScreen(new MenuPrincipalScreen()))
                .bounds((this.width - BOTON_ANCHO) / 2, this.height / 2 + 40, BOTON_ANCHO, BOTON_ALTO)
                .build());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(g);

        g.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 40, 0xFFFFFFFF);
        g.drawCenteredString(this.font, "Vacio de momento",
                this.width / 2, this.height / 2 - 20, 0xFF808080);

        super.render(g, mouseX, mouseY, partialTick);
    }
}