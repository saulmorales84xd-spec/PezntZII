package net.saullmc.pezntz.client.screen.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.ModItems;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MenuSeccionScreen extends Screen {

    /** Cuantos items por fila. */
    public static final int COLUMNAS = 7;

    /** Cuantas filas se ven a la vez. Lo que sobre se mira con la ruedita. */
    public static final int FILAS_VISIBLES = 4;

    /** Hueco que ocupa cada item. Es tambien el area donde se detecta el raton. */
    public static final int CELDA = 24;

    /** Separacion entre celdas. */
    public static final int HUECO = 4;

    /** Tamanio del icono. 1.0 es el normal de 16x16. Cada item puede llevar el suyo. */
    public static final float ESCALA_ITEM = 1.0F;

    /** Desplazamiento de toda la rejilla respecto al centro de la pantalla. */
    public static final int OFFSET_X = 0;
    public static final int OFFSET_Y = 0;

    /** Recuadro sobre la celda que tiene el raton. 0 en el alfa lo quita. */
    public static final int COLOR_CELDA_SOBRE = 0x60FFFFFF;

    /** El oscurecimiento normal de cualquier menu. Debajo de los items ya no hay nada mas. */
    public static final boolean OSCURECER_FONDO = true;

    public static final boolean MOSTRAR_BARRA = true;
    public static final int BARRA_ANCHO = 4;
    public static final int BARRA_SEPARACION = 6;
    public static final int COLOR_BARRA_FONDO = 0x50FFFFFF;
    public static final int COLOR_BARRA = 0xFFFFFFFF;

    public static final int FILAS_POR_PASO = 1;

    public static final boolean MOSTRAR_VOLVER = true;

    public static final ResourceLocation TEXTURA_VOLVER =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/boton_volver.png");

    /** Tamanio REAL del archivo png, en pixeles. */
    public static final int VOLVER_TEXTURA_ANCHO = 32;
    public static final int VOLVER_TEXTURA_ALTO = 32;

    /** Trozo del png que se dibuja. Si usas el png entero, igual que los de arriba. */
    public static final int VOLVER_ANCHO = 32;
    public static final int VOLVER_ALTO = 32;

    /** De que punto del png se empieza a recortar. */
    public static final int VOLVER_U = 0;
    public static final int VOLVER_V = 0;

    /**
     * Si tu png lleva DOS dibujos, el normal arriba y el de raton encima debajo, pon aqui la
     * altura donde empieza el segundo. Con -1 se usa siempre el mismo.
     */
    public static final int VOLVER_V_SOBRE = -1;

    /** Tamanio en pantalla = el trozo de arriba por esto. 2.0 lo pone al doble. */
    public static final float VOLVER_ESCALA = 1.0F;

    /**
     * Donde se mide la altura del boton:
     *   false = justo debajo de la rejilla, asi que baja si agrandas la rejilla
     *   true  = pegado al borde de abajo de la pantalla, pase lo que pase con la rejilla
     */
    public static final boolean VOLVER_PEGADO_ABAJO = false;

    /** Ajuste fino. La X va desde el centro de la pantalla, la Y desde lo de arriba. */
    public static final int VOLVER_OFFSET_X = 0;
    public static final int VOLVER_OFFSET_Y = 14;

    public static final int VOLVER_COLOR_SOBRE = 0x40FFFFFF;

    public static final String SONIDO_VOLVER = "minecraft:ui.button.click";
    public static final float TONO_VOLVER = 0.9F;

    private static List<ItemMenu> contenido(int seccion) {
        return switch (seccion) {

            case 0 -> List.of(
                    new ItemMenu(ModItems.INFO_REGLAS.get()).en(-40, 0).escala(1.15F),
                    new ItemMenu(ModItems.INFO_SERVER.get()).en(-40, 0).escala(1.15F),
                    new ItemMenu(ModItems.MARCADOR_MINAS_AZUFRE.get()).en(-40, 0).escala(1.15F),
                    new ItemMenu(ModItems.MARCADOR_DROP.get()).en(-40, 0).escala(1.15F),
                    new ItemMenu(ModItems.MARCADOR_MINAS_TITANIO.get()).en(-40, 0).escala(1.15F));

            case 1 -> List.of(
                    new ItemMenu(ModItems.AZUFRE.get()));

            default -> List.of(
                    new ItemMenu(ModItems.AZUFRE.get()));
        };
    }

    private final int seccion;

    private final List<ItemMenu> rejilla = new ArrayList<>();

    private final List<ItemMenu> libres = new ArrayList<>();

    private int desplazamiento = 0;
    private int rejillaX;
    private int rejillaY;

    private int volverX;
    private int volverY;
    private int volverAncho;
    private int volverAlto;

    public MenuSeccionScreen(int seccion) {
        super(Component.empty());
        this.seccion = seccion;
    }

    public int getSeccion() {
        return this.seccion;
    }

    @Override
    protected void init() {
        this.rejilla.clear();
        this.libres.clear();

        for (ItemMenu entrada : contenido(this.seccion)) {
            if (entrada.getStack().isEmpty()) continue;

            if (entrada.tienePosicion()) this.libres.add(entrada);
            else this.rejilla.add(entrada);
        }

        this.rejillaX = (this.width - anchoRejilla()) / 2 + OFFSET_X;
        this.rejillaY = (this.height - altoRejilla()) / 2 + OFFSET_Y;

        this.volverAncho = Math.max(1, Math.round(VOLVER_ANCHO * VOLVER_ESCALA));
        this.volverAlto = Math.max(1, Math.round(VOLVER_ALTO * VOLVER_ESCALA));

        this.volverX = (this.width - this.volverAncho) / 2 + VOLVER_OFFSET_X;
        this.volverY = VOLVER_PEGADO_ABAJO
                ? this.height - this.volverAlto - VOLVER_OFFSET_Y
                : this.rejillaY + altoRejilla() + VOLVER_OFFSET_Y;

        this.desplazamiento = Mth.clamp(this.desplazamiento, 0, desplazamientoMaximo());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static int anchoRejilla() {
        return COLUMNAS * CELDA + (COLUMNAS - 1) * HUECO;
    }

    private static int altoRejilla() {
        return FILAS_VISIBLES * CELDA + (FILAS_VISIBLES - 1) * HUECO;
    }

    private int filasTotales() {
        return (this.rejilla.size() + COLUMNAS - 1) / COLUMNAS;
    }

    private int desplazamientoMaximo() {
        return Math.max(0, filasTotales() - FILAS_VISIBLES);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        if (OSCURECER_FONDO) this.renderBackground(g);

        ItemMenu encima = dibujarRejilla(g, mouseX, mouseY);

        ItemMenu encimaLibre = dibujarLibres(g, mouseX, mouseY);
        if (encimaLibre != null) encima = encimaLibre;

        dibujarBarra(g);

        super.render(g, mouseX, mouseY, partialTick);

        dibujarVolver(g, mouseX, mouseY);

        if (encima != null) {
            g.renderTooltip(this.font,
                    getTooltipFromItem(Minecraft.getInstance(), encima.getStack()),
                    encima.getImagenTooltip(),
                    mouseX, mouseY);
        }
    }

    @Nullable
    private ItemMenu dibujarRejilla(GuiGraphics g, int mouseX, int mouseY) {
        ItemMenu encima = null;

        for (int fila = 0; fila < FILAS_VISIBLES; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {

                int indice = (fila + this.desplazamiento) * COLUMNAS + col;
                if (indice >= this.rejilla.size()) continue;

                ItemMenu entrada = this.rejilla.get(indice);

                int x = this.rejillaX + col * (CELDA + HUECO);
                int y = this.rejillaY + fila * (CELDA + HUECO);

                if (dentro(mouseX, mouseY, x, y, CELDA, CELDA)) {
                    if ((COLOR_CELDA_SOBRE >>> 24) != 0) {
                        g.fill(x, y, x + CELDA, y + CELDA, COLOR_CELDA_SOBRE);
                    }
                    encima = entrada;
                }

                float escala = entrada.getEscala(ESCALA_ITEM);
                float tamanio = 16.0F * escala;

                dibujarItem(g, entrada.getStack(),
                        Math.round(x + (CELDA - tamanio) / 2.0F),
                        Math.round(y + (CELDA - tamanio) / 2.0F),
                        escala);
            }
        }

        return encima;
    }

    @Nullable
    private ItemMenu dibujarLibres(GuiGraphics g, int mouseX, int mouseY) {
        ItemMenu encima = null;

        for (ItemMenu entrada : this.libres) {
            float escala = entrada.getEscala(ESCALA_ITEM);
            int tamanio = Math.max(1, Math.round(16.0F * escala));

            int x = this.rejillaX + entrada.getX();
            int y = this.rejillaY + entrada.getY();

            if (dentro(mouseX, mouseY, x, y, tamanio, tamanio)) {
                if ((COLOR_CELDA_SOBRE >>> 24) != 0) {
                    g.fill(x, y, x + tamanio, y + tamanio, COLOR_CELDA_SOBRE);
                }
                encima = entrada;
            }

            dibujarItem(g, entrada.getStack(), x, y, escala);
        }

        return encima;
    }

    private void dibujarItem(GuiGraphics g, ItemStack stack, int x, int y, float escala) {
        g.pose().pushPose();

        g.pose().translate(x, y, 0.0F);
        g.pose().scale(escala, escala, 1.0F);

        g.renderItem(stack, 0, 0);
        g.renderItemDecorations(this.font, stack, 0, 0);

        g.pose().popPose();
    }

    private void dibujarBarra(GuiGraphics g) {
        if (!MOSTRAR_BARRA) return;

        int maximo = desplazamientoMaximo();
        if (maximo <= 0) return;

        int alto = altoRejilla();
        int x = this.rejillaX + anchoRejilla() + BARRA_SEPARACION;

        g.fill(x, this.rejillaY, x + BARRA_ANCHO, this.rejillaY + alto, COLOR_BARRA_FONDO);

        int altoPulgar = Math.max(8, alto * FILAS_VISIBLES / filasTotales());
        int recorrido = alto - altoPulgar;
        int y = this.rejillaY + recorrido * this.desplazamiento / maximo;

        g.fill(x, y, x + BARRA_ANCHO, y + altoPulgar, COLOR_BARRA);
    }

    private void dibujarVolver(GuiGraphics g, int mouseX, int mouseY) {
        if (!MOSTRAR_VOLVER) return;

        boolean sobre = dentro(mouseX, mouseY, this.volverX, this.volverY, this.volverAncho, this.volverAlto);

        int v = (sobre && VOLVER_V_SOBRE >= 0) ? VOLVER_V_SOBRE : VOLVER_V;

        g.blit(TEXTURA_VOLVER,
                this.volverX, this.volverY,
                this.volverAncho, this.volverAlto,
                (float) VOLVER_U, (float) v,
                VOLVER_ANCHO, VOLVER_ALTO,
                VOLVER_TEXTURA_ANCHO, VOLVER_TEXTURA_ALTO);

        if (sobre && VOLVER_V_SOBRE < 0 && (VOLVER_COLOR_SOBRE >>> 24) != 0) {
            g.fill(this.volverX, this.volverY,
                    this.volverX + this.volverAncho, this.volverY + this.volverAlto,
                    VOLVER_COLOR_SOBRE);
        }
    }

    private static boolean dentro(int mx, int my, int x, int y, int ancho, int alto) {
        return mx >= x && mx < x + ancho && my >= y && my < y + alto;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int maximo = desplazamientoMaximo();
        if (maximo <= 0) return super.mouseScrolled(mouseX, mouseY, delta);

        this.desplazamiento = Mth.clamp(
                this.desplazamiento - (int) Math.signum(delta) * FILAS_POR_PASO, 0, maximo);

        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int boton) {
        if (boton == 0 && MOSTRAR_VOLVER
                && dentro((int) mouseX, (int) mouseY, this.volverX, this.volverY, this.volverAncho, this.volverAlto)) {
            volver();
            return true;
        }

        if (boton == 1) {
            volver();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, boton);
    }

    private void volver() {
        SoundEvent evento = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(SONIDO_VOLVER));
        if (evento != null) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(evento, TONO_VOLVER));
        }

        Minecraft.getInstance().setScreen(new MenuPrincipalScreen());
    }
}