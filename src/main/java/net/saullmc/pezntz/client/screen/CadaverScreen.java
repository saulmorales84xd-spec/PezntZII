package net.saullmc.pezntz.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Inventory;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.menu.CadaverMenu;
import net.saullmc.pezntz.network.NetworkHandler;
import net.saullmc.pezntz.network.RecuperarCadaverPacket;

public class CadaverScreen extends AbstractContainerScreen<CadaverMenu> {

    private static final ResourceLocation TEXTURA =
            new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");

    public static final boolean MOSTRAR_BOTON = true;

    public static final ResourceLocation TEXTURA_BOTON =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/boton_recuperar.png");

    public static final int BOTON_ANCHO = 64;
    public static final int BOTON_ALTO = 16;

    public static final float BOTON_ESCALA = 1.0F;

    public static final int BOTON_OFFSET_X = 0;
    public static final int BOTON_OFFSET_Y = 4;

    public static final boolean BOTON_PEGADO_ABAJO = true;

    public static final boolean BOTON_TEXTO = false;

    public static final String BOTON_ETIQUETA = "Recuperar todo";

    public static final int BOTON_COLOR_TEXTO = 0xFFFFFFFF;

    public static final boolean BOTON_SOMBRA_TEXTO = false;

    public static final float BOTON_TINTE_ENCIMA = 1.25F;

    public static final boolean BOTON_TOOLTIP = true;

    public static final String BOTON_TOOLTIP_TEXTO = "Devuelve todo a su sitio";

    public static final String BOTON_SONIDO = "minecraft:ui.button.click";

    private final int filas;

    private int botonX;
    private int botonY;
    private int botonAnchoFinal;
    private int botonAltoFinal;

    public CadaverScreen(CadaverMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.filas = menu.getFilas();
        this.imageWidth = CadaverMenu.ANCHO_GUI;
        this.imageHeight = menu.getAltoGui();
        this.inventoryLabelY = this.imageHeight - 94;

        this.botonAnchoFinal = Math.max(1, Math.round(BOTON_ANCHO * BOTON_ESCALA));
        this.botonAltoFinal = Math.max(1, Math.round(BOTON_ALTO * BOTON_ESCALA));
    }

    private boolean hayBoton() {
        return MOSTRAR_BOTON && this.menu.esDuenio();
    }

    @Override
    protected void init() {
        super.init();

        this.botonAnchoFinal = Math.max(1, Math.round(BOTON_ANCHO * BOTON_ESCALA));
        this.botonAltoFinal = Math.max(1, Math.round(BOTON_ALTO * BOTON_ESCALA));

        this.botonX = this.leftPos + (this.imageWidth - this.botonAnchoFinal) / 2 + BOTON_OFFSET_X;

        this.botonY = BOTON_PEGADO_ABAJO
                ? this.topPos + this.imageHeight + BOTON_OFFSET_Y
                : this.topPos + BOTON_OFFSET_Y;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (hayBoton()) {
            dibujarBoton(guiGraphics, mouseX, mouseY);
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);

        if (hayBoton() && BOTON_TOOLTIP && encimaDelBoton(mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font,
                    Component.literal(BOTON_TOOLTIP_TEXTO), mouseX, mouseY);
        }
    }

    private void dibujarBoton(GuiGraphics g, int mouseX, int mouseY) {
        boolean encima = encimaDelBoton(mouseX, mouseY);
        float tinte = encima ? BOTON_TINTE_ENCIMA : 1.0F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(tinte, tinte, tinte, 1.0F);

        g.blit(TEXTURA_BOTON,
                this.botonX, this.botonY,
                this.botonAnchoFinal, this.botonAltoFinal,
                0.0F, 0.0F,
                BOTON_ANCHO, BOTON_ALTO,
                BOTON_ANCHO, BOTON_ALTO);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();

        if (!BOTON_TEXTO) return;

        int anchoTexto = this.font.width(BOTON_ETIQUETA);

        g.drawString(this.font, BOTON_ETIQUETA,
                this.botonX + (this.botonAnchoFinal - anchoTexto) / 2,
                this.botonY + (this.botonAltoFinal - this.font.lineHeight) / 2 + 1,
                BOTON_COLOR_TEXTO, BOTON_SOMBRA_TEXTO);
    }

    private boolean encimaDelBoton(double mouseX, double mouseY) {
        return mouseX >= this.botonX && mouseX < this.botonX + this.botonAnchoFinal
                && mouseY >= this.botonY && mouseY < this.botonY + this.botonAltoFinal;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int boton) {
        if (boton == 0 && hayBoton() && encimaDelBoton(mouseX, mouseY)) {
            NetworkHandler.sendToServer(new RecuperarCadaverPacket(this.menu.getPos()));
            sonar();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, boton);
    }

    private static void sonar() {
        SoundEvent evento = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(BOTON_SONIDO));
        if (evento == null) return;

        Minecraft.getInstance().getSoundManager().play(
                net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(evento, 1.0F));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(TEXTURA, x, y, 0, 0, this.imageWidth, this.filas * 18 + 17);
        guiGraphics.blit(TEXTURA, x, y + this.filas * 18 + 17, 0, 126, this.imageWidth, 96);
    }
}
