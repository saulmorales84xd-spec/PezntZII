package net.saullmc.pezntz.client.screen.menu;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.saullmc.pezntz.client.screen.FondoBorroso;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class MenuCrafteoScreen extends Screen {

    public static final float ESCALA = 9.0F;

    public static final boolean AJUSTAR_A_PANTALLA = true;

    public static final float MAXIMO_DE_PANTALLA = 0.9F;

    public static final boolean ESCALA_ENTERA = true;

    public static final int OFFSET_X = 0;
    public static final int OFFSET_Y = 0;

    public static final boolean OSCURECER_FONDO = true;

    public static final boolean TOOLTIP_CON_RECETA = false;

    public static final boolean ANIMAR_AL_ABRIR = true;

    public static final float DURACION_ABRIR = 0.35F;

    public static final float REBOTE_AL_ABRIR = 0.0F;

    public static final boolean ANIMAR_AL_CERRAR = true;

    public static final float DURACION_CERRAR = 0.25F;

    public static final String SONIDO_ABRIR = "minecraft:ui.button.click";
    public static final float TONO_ABRIR = 1.0F;

    public static final boolean MOSTRAR_VOLVER = true;

    public static final float VOLVER_ESCALA = MenuSeccionScreen.VOLVER_ESCALA;

    public static final int VOLVER_SEPARACION = 8;

    public static final int VOLVER_OFFSET_X = 0;

    public static final boolean CLIC_DERECHO_VUELVE = true;

    private static final int RATON_FUERA = -10000;

    private final Screen padre;
    private final ItemMenu entrada;

    @Nullable
    private final float[] origen;

    private final FondoBorroso borroso = new FondoBorroso();

    private float escala = ESCALA;

    private int x;
    private int y;
    private int ancho;
    private int alto;

    private int volverX;
    private int volverY;
    private int volverAncho;
    private int volverAlto;

    private long inicioAbrir = -1L;
    private long inicioCerrar = -1L;
    private float progresoAlCerrar = 1.0F;

    public MenuCrafteoScreen(Screen padre, ItemMenu entrada) {
        this(padre, entrada, null);
    }

    public MenuCrafteoScreen(Screen padre, ItemMenu entrada, @Nullable float[] origen) {
        super(Component.empty());
        this.padre = padre;
        this.entrada = entrada;
        this.origen = origen;
    }

    public static void abrir(Screen padre, ItemMenu entrada) {
        abrir(padre, entrada, null);
    }

    public static void abrir(Screen padre, ItemMenu entrada, float x, float y, float ancho, float alto) {
        abrir(padre, entrada, new float[]{x, y, ancho, alto});
    }

    private static void abrir(Screen padre, ItemMenu entrada, @Nullable float[] origen) {
        SoundEvent evento = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(SONIDO_ABRIR));
        if (evento != null) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(evento, TONO_ABRIR));
        }

        Minecraft.getInstance().setScreen(new MenuCrafteoScreen(padre, entrada, origen));
    }

    @Override
    protected void init() {
        this.borroso.preparar();

        this.volverAncho = Math.max(1, Math.round(MenuSeccionScreen.VOLVER_ANCHO * VOLVER_ESCALA));
        this.volverAlto = Math.max(1, Math.round(MenuSeccionScreen.VOLVER_ALTO * VOLVER_ESCALA));

        int espacioVolver = MOSTRAR_VOLVER ? this.volverAlto + VOLVER_SEPARACION : 0;

        float propia = this.entrada.getMenu().getEscalaPropia();
        float esc = propia > 0.0F ? propia : ESCALA;

        if (AJUSTAR_A_PANTALLA) {
            float porAlto = (this.height * MAXIMO_DE_PANTALLA - espacioVolver) / MenuCrafteo.TEXTURA_ALTO;
            float porAncho = this.width * MAXIMO_DE_PANTALLA / MenuCrafteo.TEXTURA_ANCHO;
            esc = Math.min(esc, Math.min(porAlto, porAncho));
        }

        if (ESCALA_ENTERA && esc >= 1.0F) esc = (float) Math.floor(esc);

        this.escala = Math.max(0.25F, esc);

        this.ancho = Math.round(MenuCrafteo.TEXTURA_ANCHO * this.escala);
        this.alto = Math.round(MenuCrafteo.TEXTURA_ALTO * this.escala);

        this.x = (this.width - this.ancho) / 2 + OFFSET_X;
        this.y = (this.height - this.alto - espacioVolver) / 2 + OFFSET_Y;

        this.volverX = (this.width - this.volverAncho) / 2 + VOLVER_OFFSET_X;
        this.volverY = this.y + this.alto + VOLVER_SEPARACION;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private float progreso() {
        long ahora = Util.getMillis();

        if (this.inicioCerrar >= 0L) {
            float t = DURACION_CERRAR <= 0.0F ? 1.0F : (ahora - this.inicioCerrar) / (DURACION_CERRAR * 1000.0F);
            t = Math.max(0.0F, Math.min(1.0F, t));
            return this.progresoAlCerrar * (1.0F - t * t * t);
        }

        if (!ANIMAR_AL_ABRIR || this.origen == null || DURACION_ABRIR <= 0.0F) return 1.0F;

        if (this.inicioAbrir < 0L) this.inicioAbrir = ahora;

        float t = (ahora - this.inicioAbrir) / (DURACION_ABRIR * 1000.0F);
        if (t >= 1.0F) return 1.0F;

        return salida(Math.max(0.0F, t), REBOTE_AL_ABRIR);
    }

    private boolean animando() {
        if (this.inicioCerrar >= 0L) return true;
        if (!ANIMAR_AL_ABRIR || this.origen == null || DURACION_ABRIR <= 0.0F) return false;
        if (this.inicioAbrir < 0L) return true;
        return Util.getMillis() - this.inicioAbrir < DURACION_ABRIR * 1000.0F;
    }

    private static float salida(float t, float rebote) {
        if (rebote <= 0.0F) {
            float u = 1.0F - t;
            return 1.0F - u * u * u;
        }

        float c1 = 1.70158F * rebote;
        float c3 = c1 + 1.0F;
        float u = t - 1.0F;
        return 1.0F + c3 * u * u * u + c1 * u * u;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        boolean hayDesenfoque = this.borroso.dibujar(partialTick);

        if (hayDesenfoque) {
            if ((FondoBorroso.COLOR_VELO >>> 24) != 0) {
                g.fill(0, 0, this.width, this.height, FondoBorroso.COLOR_VELO);
            }
        } else if (OSCURECER_FONDO) {
            this.renderBackground(g);
        }

        float p = progreso();
        boolean animando = animando();

        MenuCrafteo.Encima encima = null;

        if (animando && this.origen != null) {
            float centroFinalX = this.x + this.ancho / 2.0F;
            float centroFinalY = this.y + this.alto / 2.0F;

            float centroOrigenX = this.origen[0] + this.origen[2] / 2.0F;
            float centroOrigenY = this.origen[1] + this.origen[3] / 2.0F;

            float escalaOrigen = Math.max(0.01F, this.origen[2] / (float) this.ancho);
            float k = escalaOrigen + (1.0F - escalaOrigen) * p;

            float cx = centroOrigenX + (centroFinalX - centroOrigenX) * p;
            float cy = centroOrigenY + (centroFinalY - centroOrigenY) * p;

            g.pose().pushPose();
            g.pose().translate(cx, cy, 0.0F);
            g.pose().scale(k, k, 1.0F);
            g.pose().translate(-centroFinalX, -centroFinalY, 0.0F);

            this.entrada.getMenu().dibujar(g, this.font, this.entrada, this.x, this.y, this.escala,
                    RATON_FUERA, RATON_FUERA);

            g.pose().popPose();
        } else {
            encima = this.entrada.getMenu().dibujar(g, this.font, this.entrada, this.x, this.y, this.escala,
                    mouseX, mouseY);
        }

        super.render(g, mouseX, mouseY, partialTick);

        if (!animando) dibujarVolver(g, mouseX, mouseY);

        if (encima != null) dibujarTooltip(g, encima, mouseX, mouseY);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.inicioCerrar >= 0L) {
            float pasado = (Util.getMillis() - this.inicioCerrar) / 1000.0F;
            if (pasado >= DURACION_CERRAR) {
                Minecraft.getInstance().setScreen(this.padre);
            }
        }
    }

    private void dibujarTooltip(GuiGraphics g, MenuCrafteo.Encima encima, int mouseX, int mouseY) {
        ItemStack stack = encima.stack();
        if (stack.isEmpty()) return;

        if (encima.principal()) {
            List<Component> lineas = this.entrada.getTooltip();
            if (!lineas.isEmpty()) {
                g.renderTooltip(this.font, lineas,
                        TOOLTIP_CON_RECETA ? this.entrada.getImagenTooltip() : Optional.empty(),
                        mouseX, mouseY);
            }
            return;
        }

        g.renderTooltip(this.font, Screen.getTooltipFromItem(Minecraft.getInstance(), stack),
                stack.getTooltipImage(), mouseX, mouseY);
    }

    private void dibujarVolver(GuiGraphics g, int mouseX, int mouseY) {
        if (!MOSTRAR_VOLVER) return;

        boolean sobre = sobreVolver(mouseX, mouseY);

        int v = (sobre && MenuSeccionScreen.VOLVER_V_SOBRE >= 0)
                ? MenuSeccionScreen.VOLVER_V_SOBRE
                : MenuSeccionScreen.VOLVER_V;

        g.blit(MenuSeccionScreen.TEXTURA_VOLVER,
                this.volverX, this.volverY,
                this.volverAncho, this.volverAlto,
                (float) MenuSeccionScreen.VOLVER_U, (float) v,
                MenuSeccionScreen.VOLVER_ANCHO, MenuSeccionScreen.VOLVER_ALTO,
                MenuSeccionScreen.VOLVER_TEXTURA_ANCHO, MenuSeccionScreen.VOLVER_TEXTURA_ALTO);

        if (sobre && MenuSeccionScreen.VOLVER_V_SOBRE < 0 && (MenuSeccionScreen.VOLVER_COLOR_SOBRE >>> 24) != 0) {
            g.fill(this.volverX, this.volverY,
                    this.volverX + this.volverAncho, this.volverY + this.volverAlto,
                    MenuSeccionScreen.VOLVER_COLOR_SOBRE);
        }
    }

    private boolean sobreVolver(double mouseX, double mouseY) {
        return mouseX >= this.volverX && mouseX < this.volverX + this.volverAncho
                && mouseY >= this.volverY && mouseY < this.volverY + this.volverAlto;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int boton) {
        if (this.inicioCerrar >= 0L) return true;

        if (boton == 1 && CLIC_DERECHO_VUELVE) {
            volver(true);
            return true;
        }

        if (animando()) return true;

        if (boton == 0 && MOSTRAR_VOLVER && sobreVolver(mouseX, mouseY)) {
            volver(true);
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, boton);
    }

    @Override
    public void onClose() {
        volver(false);
    }

    private void volver(boolean conSonido) {
        if (this.inicioCerrar >= 0L) return;

        if (conSonido) {
            SoundEvent evento = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(MenuSeccionScreen.SONIDO_VOLVER));
            if (evento != null) {
                Minecraft.getInstance().getSoundManager().play(
                        SimpleSoundInstance.forUI(evento, MenuSeccionScreen.TONO_VOLVER));
            }
        }

        if (ANIMAR_AL_CERRAR && this.origen != null && DURACION_CERRAR > 0.0F) {
            this.progresoAlCerrar = Math.max(0.0F, Math.min(1.0F, progreso()));
            this.inicioCerrar = Util.getMillis();
            return;
        }

        Minecraft.getInstance().setScreen(this.padre);
    }

    @Override
    public void removed() {
        this.borroso.cerrar();
        super.removed();
    }
}