package net.saullmc.pezntz.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.block.entity.CofreCandadoEntity;
import net.saullmc.pezntz.network.CodigoCofrePacket;
import net.saullmc.pezntz.network.NetworkHandler;
import org.lwjgl.glfw.GLFW;

public class TecladoScreen extends Screen {

    public static final ResourceLocation TEXTURA =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/teclado.png");

    /** Tamanio REAL del png, en pixeles. Minecraft lo necesita para recortar bien. */
    public static final int TEXTURA_ANCHO = 256;
    public static final int TEXTURA_ALTO = 256;

    /** Trozo del png que se dibuja, y por tanto tamanio de la ventana en pantalla. */
    public static final int GUI_ANCHO = 176;
    public static final int GUI_ALTO = 200;

    /** De que punto del png se empieza a recortar. */
    public static final int GUI_U = 0;
    public static final int GUI_V = 0;

    /** Desplazamiento de toda la ventana respecto al centro de la pantalla. */
    public static final int GUI_OFFSET_X = 0;
    public static final int GUI_OFFSET_Y = 0;

    /** Si es false no se pinta ningun fondo y solo quedan las teclas. */
    public static final boolean DIBUJAR_FONDO = true;

    /** Oscurecer el juego por detras. */
    public static final boolean OSCURECER_DETRAS = true;

    // LA REJILLA DE TECLAS
    // Las coordenadas van desde la esquina de la ventana.

    /** Esquina de la primera tecla, la del 1. */
    public static final int TECLAS_X = 28;
    public static final int TECLAS_Y = 60;

    /** Tamanio de cada tecla. Esto es lo que las escala. */
    public static final int TECLA_ANCHO = 34;
    public static final int TECLA_ALTO = 30;

    /** Separacion entre teclas. */
    public static final int TECLA_HUECO_X = 4;
    public static final int TECLA_HUECO_Y = 4;

    /**
     * Si tu textura YA trae los numeros dibujados, ponlo a false y aqui no se escribe
     * nada encima.
     */
    public static final boolean MOSTRAR_NUMEROS = true;
    public static final int NUMERO_COLOR = 0xFFFFFFFF;

    /** Lo que se pinta encima de la tecla que tiene el raton. Un velo claro. */
    public static final int COLOR_TECLA_SOBRE = 0x40FFFFFF;

    /** Borde de la tecla que tiene el raton. 0 lo quita. */
    public static final int GROSOR_BORDE = 1;
    public static final int COLOR_BORDE_SOBRE = 0xFFFFFFFF;

    // LA BARRA DEL CODIGO

    public static final int BARRA_X = 28;
    public static final int BARRA_Y = 26;
    public static final int BARRA_ANCHO = 90;
    public static final int BARRA_ALTO = 24;

    /** Si tu textura ya trae el hueco dibujado, ponlo a false. */
    public static final boolean DIBUJAR_FONDO_BARRA = false;
    public static final int COLOR_BARRA = 0xFF17171C;

    /** Tamanio del codigo escrito. 1.0 es el normal. */
    public static final float CODIGO_ESCALA = 1.5F;
    public static final int CODIGO_COLOR = 0xFFFFFFFF;

    /** Caracter que tapa cada digito mientras esta oculto. */
    public static final String CODIGO_OCULTO = "x";

    /** Caracter de los huecos que faltan. */
    public static final String CODIGO_VACIO = "_";

    // EL BOTON DEL OJO

    public static final int OJO_X = 124;
    public static final int OJO_Y = 26;
    public static final int OJO_ANCHO = 24;
    public static final int OJO_ALTO = 24;

    /** Si tu textura ya trae el ojo dibujado, ponlo a false. */
    public static final boolean DIBUJAR_TEXTO_OJO = true;
    public static final String TEXTO_OJO_VISIBLE = "O";
    public static final String TEXTO_OJO_OCULTO = "-";

    public static final String SONIDO_TECLA = "minecraft:ui.button.click";

    private static final int COLUMNAS = 3;
    private static final int FILAS = 4;

    private final BlockPos pos;

    private final boolean yaTieneCodigo;

    private final StringBuilder codigo = new StringBuilder();
    private boolean visible = false;
    private String aviso = "";
    private int avisoTicks = 0;

    private int guiX;
    private int guiY;

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
        this.guiX = (this.width - GUI_ANCHO) / 2 + GUI_OFFSET_X;
        this.guiY = (this.height - GUI_ALTO) / 2 + GUI_OFFSET_Y;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        if (OSCURECER_DETRAS) this.renderBackground(g);

        if (DIBUJAR_FONDO) {
            g.blit(TEXTURA, guiX, guiY, GUI_U, GUI_V, GUI_ANCHO, GUI_ALTO, TEXTURA_ANCHO, TEXTURA_ALTO);
        }

        dibujarBarra(g);
        dibujarOjo(g, mouseX, mouseY);

        for (int i = 0; i < 12; i++) {
            dibujarTecla(g, i, mouseX, mouseY);
        }

        if (avisoTicks > 0) {
            g.drawCenteredString(this.font, aviso, guiX + GUI_ANCHO / 2, guiY + GUI_ALTO + 4, 0xFFFF5555);
        }

        super.render(g, mouseX, mouseY, partialTick);
    }

    private void dibujarBarra(GuiGraphics g) {
        int x = guiX + BARRA_X;
        int y = guiY + BARRA_Y;

        if (DIBUJAR_FONDO_BARRA) {
            g.fill(x, y, x + BARRA_ANCHO, y + BARRA_ALTO, COLOR_BARRA);
        }

        StringBuilder mostrado = new StringBuilder();
        for (int i = 0; i < CofreCandadoEntity.DIGITOS; i++) {
            if (i > 0) mostrado.append(' ');

            if (i < codigo.length()) {
                mostrado.append(visible ? String.valueOf(codigo.charAt(i)) : CODIGO_OCULTO);
            } else {
                mostrado.append(CODIGO_VACIO);
            }
        }

        String texto = mostrado.toString();
        int ancho = this.font.width(texto);

        g.pose().pushPose();
        g.pose().translate(x + BARRA_ANCHO / 2.0F, y + BARRA_ALTO / 2.0F, 0.0F);
        g.pose().scale(CODIGO_ESCALA, CODIGO_ESCALA, 1.0F);

        g.drawString(this.font, texto, -ancho / 2, -4, CODIGO_COLOR, false);

        g.pose().popPose();
    }

    private void dibujarOjo(GuiGraphics g, int mouseX, int mouseY) {
        int x = guiX + OJO_X;
        int y = guiY + OJO_Y;
        boolean sobre = dentro(mouseX, mouseY, x, y, OJO_ANCHO, OJO_ALTO);

        if (sobre) resaltar(g, x, y, OJO_ANCHO, OJO_ALTO);

        if (DIBUJAR_TEXTO_OJO) {
            String texto = visible ? TEXTO_OJO_VISIBLE : TEXTO_OJO_OCULTO;
            g.drawCenteredString(this.font, texto, x + OJO_ANCHO / 2, y + (OJO_ALTO - 8) / 2, NUMERO_COLOR);
        }
    }

    private void dibujarTecla(GuiGraphics g, int indice, int mouseX, int mouseY) {
        int x = teclaX(indice);
        int y = teclaY(indice);

        if (dentro(mouseX, mouseY, x, y, TECLA_ANCHO, TECLA_ALTO)) {
            resaltar(g, x, y, TECLA_ANCHO, TECLA_ALTO);
        }

        if (MOSTRAR_NUMEROS) {
            g.drawCenteredString(this.font, etiqueta(indice),
                    x + TECLA_ANCHO / 2, y + (TECLA_ALTO - 8) / 2, NUMERO_COLOR);
        }
    }

    private static void resaltar(GuiGraphics g, int x, int y, int ancho, int alto) {
        if (GROSOR_BORDE > 0) {
            g.fill(x - GROSOR_BORDE, y - GROSOR_BORDE, x + ancho + GROSOR_BORDE, y + alto + GROSOR_BORDE,
                    COLOR_BORDE_SOBRE);
        }
        g.fill(x, y, x + ancho, y + alto, COLOR_TECLA_SOBRE);
    }

    private int teclaX(int indice) {
        return guiX + TECLAS_X + (indice % COLUMNAS) * (TECLA_ANCHO + TECLA_HUECO_X);
    }

    private int teclaY(int indice) {
        return guiY + TECLAS_Y + (indice / COLUMNAS) * (TECLA_ALTO + TECLA_HUECO_Y);
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

        if (dentro(mx, my, guiX + OJO_X, guiY + OJO_Y, OJO_ANCHO, OJO_ALTO)) {
            this.visible = !this.visible;
            clic(1.4F);
            return true;
        }

        for (int i = 0; i < 12; i++) {
            if (!dentro(mx, my, teclaX(i), teclaY(i), TECLA_ANCHO, TECLA_ALTO)) continue;

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

        NetworkHandler.sendToServer(new CodigoCofrePacket(pos, codigo.toString()));

        if (!yaTieneCodigo) {
            Minecraft.getInstance().setScreen(null);
            return;
        }

        codigo.setLength(0);
        this.visible = false;
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

    private void clic(float tono) {
        SoundEvent evento = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(SONIDO_TECLA));
        if (evento == null) return;

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(evento, tono));
    }
}