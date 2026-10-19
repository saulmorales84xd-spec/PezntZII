package net.saullmc.pezntz.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.saullmc.pezntz.block.entity.CofreCandadoEntity;
import net.saullmc.pezntz.network.CodigoCofrePacket;
import net.saullmc.pezntz.network.NetworkHandler;
import org.lwjgl.glfw.GLFW;

public class TecladoScreen extends Screen {

    public static final int TECLA = 34;          // lado de cada tecla
    public static final int HUECO = 4;           // separacion entre teclas
    public static final int BARRA_ALTO = 26;     // alto de la barra del codigo
    public static final int HUECO_BARRA = 8;     // separacion entre la barra y las teclas

    public static final int COLOR_FONDO       = 0xE0101014;
    public static final int COLOR_TECLA       = 0xFF2B2B33;
    public static final int COLOR_TECLA_SOBRE = 0xFF3D3D47;
    public static final int COLOR_BORDE       = 0xFF000000;
    public static final int COLOR_BORDE_SOBRE = 0xFFFFFFFF;
    public static final int COLOR_TEXTO       = 0xFFFFFFFF;
    public static final int COLOR_BORRAR      = 0xFFB33A3A;
    public static final int COLOR_ENVIAR      = 0xFF3AA355;
    public static final int COLOR_BARRA       = 0xFF17171C;

    public static final int GROSOR_BORDE = 2;

    private static final int COLUMNAS = 3;
    private static final int FILAS = 4;

    private final BlockPos pos;

    private final boolean yaTieneCodigo;

    private final StringBuilder codigo = new StringBuilder();
    private boolean visible = false;
    private String aviso = "";
    private int avisoTicks = 0;

    private int origenX;
    private int origenY;
    private int ancho;

    public TecladoScreen(BlockPos pos, boolean yaTieneCodigo) {
        super(Component.literal(yaTieneCodigo ? "Introduce el codigo" : "Elige un codigo"));
        this.pos = pos;
        this.yaTieneCodigo = yaTieneCodigo;
    }

    public static void abrir(BlockPos pos, boolean yaTieneCodigo) {
        Minecraft.getInstance().setScreen(new TecladoScreen(pos.immutable(), yaTieneCodigo));
    }

    @Override
    protected void init() {
        this.ancho = COLUMNAS * TECLA + (COLUMNAS - 1) * HUECO;
        int alto = BARRA_ALTO + HUECO_BARRA + FILAS * TECLA + (FILAS - 1) * HUECO;

        this.origenX = (this.width - this.ancho) / 2;
        this.origenY = (this.height - alto) / 2;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(g);

        int margen = 10;
        int alto = BARRA_ALTO + HUECO_BARRA + FILAS * TECLA + (FILAS - 1) * HUECO;
        g.fill(origenX - margen, origenY - margen - 14,
                origenX + ancho + margen, origenY + alto + margen, COLOR_FONDO);

        g.drawCenteredString(this.font, this.title, origenX + ancho / 2, origenY - margen - 10, COLOR_TEXTO);

        dibujarBarra(g, mouseX, mouseY);

        for (int i = 0; i < 12; i++) {
            dibujarTecla(g, i, mouseX, mouseY);
        }

        if (avisoTicks > 0) {
            g.drawCenteredString(this.font, aviso, origenX + ancho / 2, origenY + alto + 4, COLOR_BORRAR);
        }

        super.render(g, mouseX, mouseY, partialTick);
    }

    private void dibujarBarra(GuiGraphics g, int mouseX, int mouseY) {
        int x = origenX;
        int y = origenY;
        int anchoOjo = BARRA_ALTO;
        int anchoTexto = ancho - anchoOjo - HUECO;

        g.fill(x, y, x + anchoTexto, y + BARRA_ALTO, COLOR_BARRA);

        StringBuilder mostrado = new StringBuilder();
        for (int i = 0; i < CofreCandadoEntity.DIGITOS; i++) {
            if (i > 0) mostrado.append(' ');
            if (i < codigo.length()) mostrado.append(visible ? codigo.charAt(i) : 'x');
            else mostrado.append('_');
        }
        g.drawCenteredString(this.font, mostrado.toString(),
                x + anchoTexto / 2, y + (BARRA_ALTO - 8) / 2, COLOR_TEXTO);

        int ojoX = x + anchoTexto + HUECO;
        boolean sobre = dentro(mouseX, mouseY, ojoX, y, anchoOjo, BARRA_ALTO);

        g.fill(ojoX - GROSOR_BORDE, y - GROSOR_BORDE, ojoX + anchoOjo + GROSOR_BORDE, y + BARRA_ALTO + GROSOR_BORDE,
                sobre ? COLOR_BORDE_SOBRE : COLOR_BORDE);
        g.fill(ojoX, y, ojoX + anchoOjo, y + BARRA_ALTO, sobre ? COLOR_TECLA_SOBRE : COLOR_TECLA);
        g.drawCenteredString(this.font, visible ? "O" : "-",
                ojoX + anchoOjo / 2, y + (BARRA_ALTO - 8) / 2, COLOR_TEXTO);
    }

    private void dibujarTecla(GuiGraphics g, int indice, int mouseX, int mouseY) {
        int col = indice % COLUMNAS;
        int fila = indice / COLUMNAS;

        int x = origenX + col * (TECLA + HUECO);
        int y = origenY + BARRA_ALTO + HUECO_BARRA + fila * (TECLA + HUECO);

        boolean sobre = dentro(mouseX, mouseY, x, y, TECLA, TECLA);

        int fondo = switch (indice) {
            case 9 -> COLOR_BORRAR;
            case 11 -> COLOR_ENVIAR;
            default -> sobre ? COLOR_TECLA_SOBRE : COLOR_TECLA;
        };

        g.fill(x - GROSOR_BORDE, y - GROSOR_BORDE, x + TECLA + GROSOR_BORDE, y + TECLA + GROSOR_BORDE,
                sobre ? COLOR_BORDE_SOBRE : COLOR_BORDE);
        g.fill(x, y, x + TECLA, y + TECLA, fondo);

        g.drawCenteredString(this.font, etiqueta(indice), x + TECLA / 2, y + (TECLA - 8) / 2, COLOR_TEXTO);
    }

    private static String etiqueta(int indice) {
        return switch (indice) {
            case 9 -> "X";
            case 10 -> "0";
            case 11 -> ">";
            default -> String.valueOf(indice + 1);
        };
    }

    private static boolean dentro(int mx, int my, int x, int y, int ancho, int alto) {
        return mx >= x && mx < x + ancho && my >= y && my < y + alto;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int boton) {
        if (boton != 0) return super.mouseClicked(mouseX, mouseY, boton);

        int mx = (int) mouseX;
        int my = (int) mouseY;

        int anchoOjo = BARRA_ALTO;
        int ojoX = origenX + (ancho - anchoOjo - HUECO) + HUECO;
        if (dentro(mx, my, ojoX, origenY, anchoOjo, BARRA_ALTO)) {
            this.visible = !this.visible;
            clic(1.4F);
            return true;
        }

        for (int i = 0; i < 12; i++) {
            int col = i % COLUMNAS;
            int fila = i / COLUMNAS;
            int x = origenX + col * (TECLA + HUECO);
            int y = origenY + BARRA_ALTO + HUECO_BARRA + fila * (TECLA + HUECO);

            if (!dentro(mx, my, x, y, TECLA, TECLA)) continue;

            switch (i) {
                case 9 -> borrar();
                case 10 -> meter('0');
                case 11 -> enviar();
                default -> meter((char) ('1' + i));
            }
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, boton);
    }

    @Override
    public boolean keyPressed(int tecla, int scancode, int modificadores) {
        if (tecla == GLFW.GLFW_KEY_BACKSPACE || tecla == GLFW.GLFW_KEY_DELETE) {
            borrar();
            return true;
        }
        if (tecla == GLFW.GLFW_KEY_ENTER || tecla == GLFW.GLFW_KEY_KP_ENTER) {
            enviar();
            return true;
        }

        if (tecla >= GLFW.GLFW_KEY_0 && tecla <= GLFW.GLFW_KEY_9) {
            meter((char) ('0' + (tecla - GLFW.GLFW_KEY_0)));
            return true;
        }
        if (tecla >= GLFW.GLFW_KEY_KP_0 && tecla <= GLFW.GLFW_KEY_KP_9) {
            meter((char) ('0' + (tecla - GLFW.GLFW_KEY_KP_0)));
            return true;
        }

        return super.keyPressed(tecla, scancode, modificadores);
    }

    private void meter(char digito) {
        if (codigo.length() >= CofreCandadoEntity.DIGITOS) return;

        codigo.append(digito);
        clic(1.0F + codigo.length() * 0.1F);
    }

    private void borrar() {
        if (codigo.length() == 0) return;

        codigo.deleteCharAt(codigo.length() - 1);
        clic(0.8F);
    }

    private void enviar() {
        if (codigo.length() != CofreCandadoEntity.DIGITOS) {
            avisar("Faltan digitos");
            return;
        }

        NetworkHandler.sendToServer(new CodigoCofrePacket(pos, codigo.toString(), !yaTieneCodigo));
        Minecraft.getInstance().setScreen(null);
    }

    private void avisar(String texto) {
        this.aviso = texto;
        this.avisoTicks = 60;
        clic(0.6F);
    }

    @Override
    public void tick() {
        if (avisoTicks > 0) avisoTicks--;
    }

    public static final String SONIDO_TECLA = "minecraft:ui.button.click";

    private void clic(float tono) {
        SoundEvent evento = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(SONIDO_TECLA));
        if (evento == null) return;

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(evento, tono));
    }
}