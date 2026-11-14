package net.saullmc.pezntz.client.screen.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.client.tooltip.ItemEnImagen;
import net.saullmc.pezntz.item.ModTooltips;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MenuCrafteo {

    public static final boolean MENUS_AUTOMATICOS = true;

    public static final ResourceLocation TEXTURA =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/menu_crafteo.png");

    public static final int TEXTURA_ANCHO = 48;
    public static final int TEXTURA_ALTO = 38;

    public static final boolean MOSTRAR_NOMBRE = true;

    public static final float NOMBRE_X = 24.0F;
    public static final float NOMBRE_Y = 9.5F;

    public static final float NOMBRE_ANCHO_MAXIMO = 38.0F;
    public static final float NOMBRE_ALTO_MAXIMO = 7.0F;

    public static final float NOMBRE_ESCALA = 0.3F;

    public static final boolean NOMBRE_DOS_LINEAS = true;

    public static final int NOMBRE_HUECO_LINEAS = 1;

    public static final boolean NOMBRE_SOMBRA = false;

    public static final boolean MOSTRAR_ITEM_PRINCIPAL = true;

    public static final float ITEM_PRINCIPAL_X = 6.0F;
    public static final float ITEM_PRINCIPAL_Y = 20.0F;

    public static final float ITEM_PRINCIPAL_TAMANIO = 8.0F;

    public static final boolean RECETA_AUTOMATICA = true;

    public static final float RECETA_X = 29.0F;
    public static final float RECETA_Y = 17.0F;

    public static final float RECETA_PASO = 5.0F;

    public static final float ITEMS_TAMANIO = 4.0F;

    public static final float ESCALA_ITEMS = 1.0F;

    public static final float ITEMS_OFFSET_X = 1F;
    public static final float ITEMS_OFFSET_Y = 0.0F;

    public static final boolean ITEMS_CANTIDAD = true;

    public static final float TEXTO_ESCALA = 0.25F;

    public static final boolean TEXTO_SOMBRA = false;

    public static final int COLOR_ITEM_SOBRE = 0xFFFFFF;

    private static final float RECETA_ORIGEN = 0.7F;
    private static final float RECETA_PASO_ORIGINAL = 5.0F;

    public record ItemEnMenu(ItemStack stack, float x, float y, float escala) {
    }

    public record TextoEnMenu(String texto, float x, float y, float escala) {
    }

    public record Encima(ItemStack stack, boolean principal) {
    }

    private boolean forzado = false;
    private boolean quitado = false;

    private float escala = -1.0F;

    private String nombre = null;
    private int colorNombre = ItemMenu.COLOR_NOMBRE;

    private float nombreX = Float.NaN;
    private float nombreY = Float.NaN;
    private float nombreEscala = -1.0F;

    private boolean conItemPrincipal = MOSTRAR_ITEM_PRINCIPAL;
    private float principalX = ITEM_PRINCIPAL_X;
    private float principalY = ITEM_PRINCIPAL_Y;
    private float principalTamanio = ITEM_PRINCIPAL_TAMANIO;

    private boolean conReceta = RECETA_AUTOMATICA;
    private float recetaX = RECETA_X;
    private float recetaY = RECETA_Y;

    private final List<ItemEnMenu> items = new ArrayList<>();
    private final List<TextoEnMenu> textos = new ArrayList<>();

    private List<FormattedCharSequence> nombreListo = null;
    private float nombreEscalaLista = 1.0F;
    private float escalaDelNombreListo = -1.0F;

    private final List<List<Component>> textosListos = new ArrayList<>();

    void forzar() {
        this.forzado = true;
        this.quitado = false;
    }

    void quitar() {
        this.quitado = true;
        this.forzado = false;
    }

    void escala(float escala) {
        this.escala = escala;
    }

    void nombre(String texto, int color) {
        this.nombre = texto;
        this.colorNombre = color;
        this.nombreListo = null;
    }

    void nombreMueve(float x, float y) {
        this.nombreX = x;
        this.nombreY = y;
    }

    void nombreEscala(float escala) {
        this.nombreEscala = escala;
        this.nombreListo = null;
    }

    void itemPrincipal(float x, float y, float tamanio) {
        this.conItemPrincipal = true;
        this.principalX = x;
        this.principalY = y;
        this.principalTamanio = tamanio;
    }

    void sinItemPrincipal() {
        this.conItemPrincipal = false;
    }

    void sinReceta() {
        this.conReceta = false;
    }

    void recetaMueve(float x, float y) {
        this.recetaX = x;
        this.recetaY = y;
    }

    void item(ItemStack stack, float x, float y, float escala) {
        this.items.add(new ItemEnMenu(stack, x, y, escala));
    }

    void texto(String texto, float x, float y, float escala) {
        this.textos.add(new TextoEnMenu(texto, x, y, escala));
        this.textosListos.clear();
    }

    boolean activo(boolean tieneImagen) {
        if (this.quitado) return false;
        if (this.forzado) return true;
        return MENUS_AUTOMATICOS && tieneImagen;
    }

    public float getEscalaPropia() {
        return this.escala;
    }

    @Nullable
    public Encima dibujar(GuiGraphics g, Font fuente, ItemMenu entrada, int x, int y, float esc,
                          int mouseX, int mouseY) {

        g.blit(TEXTURA, x, y, Math.round(TEXTURA_ANCHO * esc), Math.round(TEXTURA_ALTO * esc),
                0.0F, 0.0F, TEXTURA_ANCHO, TEXTURA_ALTO, TEXTURA_ANCHO, TEXTURA_ALTO);

        if (MOSTRAR_NOMBRE) dibujarNombre(g, fuente, entrada, x, y, esc);

        ItemStack senalado = null;
        boolean senaladoPrincipal = false;
        float[] zona = null;

        if (this.conItemPrincipal) {
            float[] r = dibujarItem(g, fuente, entrada.getStack(), x, y, esc,
                    this.principalX, this.principalY, this.principalTamanio,
                    entrada.tieneBarra(), entrada.tieneCantidad());
            if (r != null && adentro(mouseX, mouseY, r)) {
                senalado = entrada.getStack();
                senaladoPrincipal = true;
                zona = r;
            }
        }

        if (this.conReceta && entrada.getImagen().usaTexturaPorDefecto()) {
            for (ItemEnImagen enReceta : entrada.getImagen().getItems()) {
                if (enReceta.stack().isEmpty()) continue;

                int col = celda(enReceta.x());
                int fila = celda(enReceta.y());

                float[] r = dibujarItem(g, fuente, enReceta.stack(), x, y, esc,
                        this.recetaX + col * RECETA_PASO, this.recetaY + fila * RECETA_PASO,
                        ITEMS_TAMANIO * enReceta.escala(), false, ITEMS_CANTIDAD);
                if (r != null && adentro(mouseX, mouseY, r)) {
                    senalado = enReceta.stack();
                    senaladoPrincipal = false;
                    zona = r;
                }
            }
        }

        for (ItemEnMenu extra : this.items) {
            if (extra.stack().isEmpty()) continue;

            float[] r = dibujarItem(g, fuente, extra.stack(), x, y, esc,
                    extra.x(), extra.y(), ITEMS_TAMANIO * extra.escala(), false, ITEMS_CANTIDAD);
            if (r != null && adentro(mouseX, mouseY, r)) {
                senalado = extra.stack();
                senaladoPrincipal = false;
                zona = r;
            }
        }

        dibujarTextos(g, fuente, x, y, esc);

        if (zona != null && (COLOR_ITEM_SOBRE >>> 24) != 0) {
            g.pose().pushPose();
            g.pose().translate(zona[0], zona[1], 300.0F);
            g.pose().scale(zona[2] / 16.0F, zona[2] / 16.0F, 1.0F);
            g.fillGradient(RenderType.guiOverlay(), 0, 0, 16, 16, COLOR_ITEM_SOBRE, COLOR_ITEM_SOBRE, 0);
            g.pose().popPose();
        }

        return senalado != null ? new Encima(senalado, senaladoPrincipal) : null;
    }

    private static int celda(float posicion) {
        int c = Math.round((posicion - RECETA_ORIGEN) / RECETA_PASO_ORIGINAL);
        return Math.max(0, Math.min(2, c));
    }

    private static boolean adentro(int mouseX, int mouseY, float[] r) {
        return mouseX >= r[0] && mouseX < r[0] + r[2] && mouseY >= r[1] && mouseY < r[1] + r[2];
    }

    @Nullable
    private static float[] dibujarItem(GuiGraphics g, Font fuente, ItemStack stack, int x, int y, float esc,
                                       float itemX, float itemY, float tamanio,
                                       boolean barra, boolean cantidad) {
        if (stack.isEmpty() || tamanio <= 0.0F) return null;

        float tamanioFinal = tamanio * Math.max(0.01F, ESCALA_ITEMS);
        float centrar = (tamanioFinal - tamanio) / 2.0F;

        float gx = x + (itemX + ITEMS_OFFSET_X - centrar) * esc;
        float gy = y + (itemY + ITEMS_OFFSET_Y - centrar) * esc;
        float lado = tamanioFinal * esc;
        float escalaItem = lado / 16.0F;

        g.pose().pushPose();
        g.pose().translate(gx, gy, 0.0F);
        g.pose().scale(escalaItem, escalaItem, 1.0F);

        g.renderItem(stack, 0, 0);
        MenuSeccionScreen.decorar(g, fuente, stack, barra, cantidad);

        g.pose().popPose();

        return new float[]{gx, gy, lado};
    }

    private void dibujarNombre(GuiGraphics g, Font fuente, ItemMenu entrada, int x, int y, float esc) {
        if (this.nombreListo == null || this.escalaDelNombreListo != esc) {
            prepararNombre(fuente, entrada, esc);
        }
        if (this.nombreListo.isEmpty()) return;

        float s = this.nombreEscalaLista;
        float centroX = x + (Float.isNaN(this.nombreX) ? NOMBRE_X : this.nombreX) * esc;
        float centroY = y + (Float.isNaN(this.nombreY) ? NOMBRE_Y : this.nombreY) * esc;

        int n = this.nombreListo.size();
        float altoTotal = (n * fuente.lineHeight - 1 + (n - 1) * NOMBRE_HUECO_LINEAS) * s;
        float arriba = centroY - altoTotal / 2.0F;

        for (int i = 0; i < n; i++) {
            FormattedCharSequence linea = this.nombreListo.get(i);
            float ancho = fuente.width(linea) * s;

            g.pose().pushPose();
            g.pose().translate(centroX - ancho / 2.0F, arriba + i * (fuente.lineHeight + NOMBRE_HUECO_LINEAS) * s, 0.0F);
            g.pose().scale(s, s, 1.0F);
            g.drawString(fuente, linea, 0, 0, 0xFFFFFF, NOMBRE_SOMBRA);
            g.pose().popPose();
        }
    }

    private void prepararNombre(Font fuente, ItemMenu entrada, float esc) {
        Component nombre = nombreDe(entrada);

        float maximo = (this.nombreEscala > 0.0F ? this.nombreEscala : NOMBRE_ESCALA) * esc;
        float anchoMax = NOMBRE_ANCHO_MAXIMO * esc;
        float altoMax = NOMBRE_ALTO_MAXIMO * esc;

        int ancho = Math.max(1, fuente.width(nombre));
        int altoUna = fuente.lineHeight - 1;

        float unaLinea = Math.min(maximo, Math.min(anchoMax / ancho, altoMax / altoUna));

        List<FormattedCharSequence> lineas = List.of(nombre.getVisualOrderText());
        float elegida = unaLinea;

        if (NOMBRE_DOS_LINEAS && unaLinea < maximo) {
            int objetivo = (int) Math.ceil(ancho / 2.0D) + 2;
            List<FormattedCharSequence> partidas = fuente.split(nombre, objetivo);

            while (partidas.size() > 2 && objetivo < ancho) {
                objetivo += 4;
                partidas = fuente.split(nombre, objetivo);
            }

            if (partidas.size() == 2) {
                int anchoPartido = 1;
                for (FormattedCharSequence p : partidas) anchoPartido = Math.max(anchoPartido, fuente.width(p));

                int altoDos = 2 * fuente.lineHeight - 1 + NOMBRE_HUECO_LINEAS;
                float dosLineas = Math.min(maximo, Math.min(anchoMax / anchoPartido, altoMax / altoDos));

                if (dosLineas > unaLinea * 1.1F) {
                    lineas = partidas;
                    elegida = dosLineas;
                }
            }
        }

        this.nombreListo = lineas;
        this.nombreEscalaLista = elegida;
        this.escalaDelNombreListo = esc;
    }

    private Component nombreDe(ItemMenu entrada) {
        if (this.nombre != null) return unir(ModTooltips.formatear(ModTooltips.hex(this.colorNombre) + this.nombre));

        Component propio = entrada.getNombrePropio();
        if (propio != null) return propio;

        ItemStack stack = entrada.getStack();
        List<Component> lineas = Screen.getTooltipFromItem(Minecraft.getInstance(), stack);
        return lineas.isEmpty() ? stack.getHoverName() : lineas.get(0);
    }

    private static Component unir(List<Component> partes) {
        if (partes.isEmpty()) return Component.empty();
        if (partes.size() == 1) return partes.get(0);

        MutableComponent junto = Component.empty();
        for (int i = 0; i < partes.size(); i++) {
            if (i > 0) junto.append(" ");
            junto.append(partes.get(i));
        }
        return junto;
    }

    private void dibujarTextos(GuiGraphics g, Font fuente, int x, int y, float esc) {
        if (this.textos.isEmpty()) return;

        if (this.textosListos.size() != this.textos.size()) {
            this.textosListos.clear();
            for (TextoEnMenu t : this.textos) this.textosListos.add(ModTooltips.formatear(t.texto()));
        }

        for (int i = 0; i < this.textos.size(); i++) {
            TextoEnMenu t = this.textos.get(i);
            float s = t.escala() * esc;

            float ty = y + t.y() * esc;
            for (Component linea : this.textosListos.get(i)) {
                g.pose().pushPose();
                g.pose().translate(x + t.x() * esc, ty, 0.0F);
                g.pose().scale(s, s, 1.0F);
                g.drawString(fuente, linea, 0, 0, 0xFFFFFF, TEXTO_SOMBRA);
                g.pose().popPose();

                ty += fuente.lineHeight * s;
            }
        }
    }
}