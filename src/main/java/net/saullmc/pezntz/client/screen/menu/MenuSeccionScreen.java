package net.saullmc.pezntz.client.screen.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.saullmc.pezntz.item.ModItems;

import java.util.ArrayList;
import java.util.List;

public class MenuSeccionScreen extends Screen {

    /** Cuantos items por fila. */
    public static final int COLUMNAS = 7;

    /** Cuantas filas se ven a la vez. Lo que sobre se mira con la ruedita. */
    public static final int FILAS_VISIBLES = 4;

    /** Hueco que ocupa cada item, en pixeles. Es tambien el area donde se detecta el raton. */
    public static final int CELDA = 24;

    /** Separacion entre celdas. */
    public static final int HUECO = 4;

    /** Tamanio del icono. 1.0 es el normal de 16x16, 1.5 lo hace mitad mas grande. */
    public static final float ESCALA_ITEM = 1F;

    /** Desplazamiento de toda la rejilla respecto al centro de la pantalla. */
    public static final int OFFSET_X = 0;
    public static final int OFFSET_Y = 0;

    /** Fondo detras de la rejilla. 0 en el alfa lo quita. */
    public static final int COLOR_FONDO = 0xB0101014;
    public static final int MARGEN_FONDO = 0;

    /** Recuadro sobre la celda que tiene el raton. */
    public static final int COLOR_CELDA_SOBRE = 0x60FFFFFF;

    public static final boolean OSCURECER_FONDO = true;

    public static final boolean MOSTRAR_BARRA = true;
    public static final int BARRA_ANCHO = 4;
    public static final int BARRA_SEPARACION = 6;
    public static final int COLOR_BARRA_FONDO = 0x50FFFFFF;
    public static final int COLOR_BARRA = 0xFFFFFFFF;

    /** Cuantas filas sube o baja cada paso de la ruedita. */
    public static final int FILAS_POR_PASO = 1;

    private final int seccion;
    private final List<ItemStack> items = new ArrayList<>();

    private int desplazamiento = 0;
    private int rejillaX;
    private int rejillaY;

    public MenuSeccionScreen(int seccion) {
        super(Component.empty());
        this.seccion = seccion;
    }

    public int getSeccion() {
        return this.seccion;
    }

    private static List<ItemStack> contenido(int seccion) {
        return switch (seccion) {
            case 0 -> List.of(
                    new ItemStack(ModItems.AZUFRE.get()));

            case 1 -> List.of(
                    new ItemStack(ModItems.AZUFRE.get()));

            default -> List.of(
                    new ItemStack(ModItems.AZUFRE.get()));
        };
    }

    @Override
    protected void init() {
        this.items.clear();
        this.items.addAll(contenido(this.seccion));

        int ancho = COLUMNAS * CELDA + (COLUMNAS - 1) * HUECO;
        int alto = FILAS_VISIBLES * CELDA + (FILAS_VISIBLES - 1) * HUECO;

        this.rejillaX = (this.width - ancho) / 2 + OFFSET_X;
        this.rejillaY = (this.height - alto) / 2 + OFFSET_Y;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private int filasTotales() {
        return (this.items.size() + COLUMNAS - 1) / COLUMNAS;
    }

    private int desplazamientoMaximo() {
        return Math.max(0, filasTotales() - FILAS_VISIBLES);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        if (OSCURECER_FONDO) this.renderBackground(g);

        int ancho = COLUMNAS * CELDA + (COLUMNAS - 1) * HUECO;
        int alto = FILAS_VISIBLES * CELDA + (FILAS_VISIBLES - 1) * HUECO;

        if ((COLOR_FONDO >>> 24) != 0) {
            g.fill(rejillaX - MARGEN_FONDO, rejillaY - MARGEN_FONDO,
                    rejillaX + ancho + MARGEN_FONDO, rejillaY + alto + MARGEN_FONDO, COLOR_FONDO);
        }

        ItemStack encima = ItemStack.EMPTY;

        for (int fila = 0; fila < FILAS_VISIBLES; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {

                int indice = (fila + desplazamiento) * COLUMNAS + col;
                if (indice >= this.items.size()) continue;

                ItemStack stack = this.items.get(indice);
                if (stack.isEmpty()) continue;

                int x = rejillaX + col * (CELDA + HUECO);
                int y = rejillaY + fila * (CELDA + HUECO);

                boolean sobre = mouseX >= x && mouseX < x + CELDA && mouseY >= y && mouseY < y + CELDA;
                if (sobre) {
                    g.fill(x, y, x + CELDA, y + CELDA, COLOR_CELDA_SOBRE);
                    encima = stack;
                }

                dibujarItem(g, stack, x, y);
            }
        }

        dibujarBarra(g, ancho, alto);

        super.render(g, mouseX, mouseY, partialTick);

        if (!encima.isEmpty()) {
            g.renderTooltip(this.font, encima, mouseX, mouseY);
        }
    }

    private void dibujarItem(GuiGraphics g, ItemStack stack, int x, int y) {
        float tamanio = 16.0F * ESCALA_ITEM;

        g.pose().pushPose();

        g.pose().translate(x + (CELDA - tamanio) / 2.0F, y + (CELDA - tamanio) / 2.0F, 0.0F);
        g.pose().scale(ESCALA_ITEM, ESCALA_ITEM, 1.0F);

        g.renderItem(stack, 0, 0);
        g.renderItemDecorations(this.font, stack, 0, 0);

        g.pose().popPose();
    }

    private void dibujarBarra(GuiGraphics g, int ancho, int alto) {
        if (!MOSTRAR_BARRA) return;

        int maximo = desplazamientoMaximo();
        if (maximo <= 0) return;

        int x = rejillaX + ancho + BARRA_SEPARACION;

        g.fill(x, rejillaY, x + BARRA_ANCHO, rejillaY + alto, COLOR_BARRA_FONDO);

        int totales = filasTotales();
        int altoPulgar = Math.max(8, alto * FILAS_VISIBLES / totales);
        int recorrido = alto - altoPulgar;
        int y = rejillaY + (maximo == 0 ? 0 : recorrido * desplazamiento / maximo);

        g.fill(x, y, x + BARRA_ANCHO, y + altoPulgar, COLOR_BARRA);
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
        if (boton == 1) {
            Minecraft.getInstance().setScreen(new MenuPrincipalScreen());
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, boton);
    }
}