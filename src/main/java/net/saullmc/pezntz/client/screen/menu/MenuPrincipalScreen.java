package net.saullmc.pezntz.client.screen.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.saullmc.pezntz.PezntZMod;

public class MenuPrincipalScreen extends Screen {

    /** Tamanio de cada boton. Si usas texturas, que sean de estas medidas. */
    public static final int BOTON_ANCHO = 64;
    public static final int BOTON_ALTO = 64;

    /** Separacion entre botones. */
    public static final int HUECO = 16;

    public static final int OFFSET_X = 0;
    public static final int OFFSET_Y = 0;

    /** Si sale el nombre debajo de cada boton. */
    public static final boolean MOSTRAR_NOMBRES = true;
    public static final int NOMBRE_OFFSET_Y = 6;

    public static final boolean OSCURECER_FONDO = true;

    private static final String[] NOMBRES = {"Seccion 1", "Seccion 2", "Seccion 3"};

    private static final ResourceLocation[] TEXTURAS = {
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/menu_boton_1.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/menu_boton_2.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/menu_boton_3.png"),
    };

    private static final String[] SONIDOS = {
            "minecraft:ui.button.click",
            "minecraft:ui.button.click",
            "minecraft:ui.button.click",
    };

    private static final float[] TONOS = {0.8F, 1.1F, 1.4F};

    private static final String SONIDO_ABRIR = "minecraft:ui.toast.in";

    private static SoundEvent buscarSonido(String id) {
        return BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(id));
    }

    private static void reproducir(String id, float tono) {
        SoundEvent evento = buscarSonido(id);
        if (evento == null) return;

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(evento, tono));
    }

    public MenuPrincipalScreen() {
        super(Component.literal("Menu"));
    }

    public static void abrir() {
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new MenuPrincipalScreen());
        reproducir(SONIDO_ABRIR, 1.0F);
    }

    @Override
    protected void init() {
        int total = NOMBRES.length * BOTON_ANCHO + (NOMBRES.length - 1) * HUECO;
        int x = (this.width - total) / 2 + OFFSET_X;
        int y = (this.height - BOTON_ALTO) / 2 + OFFSET_Y;

        for (int i = 0; i < NOMBRES.length; i++) {
            final int indice = i;

            this.addRenderableWidget(new BotonMenu(
                    x + i * (BOTON_ANCHO + HUECO), y,
                    BOTON_ANCHO, BOTON_ALTO,
                    Component.literal(NOMBRES[i]),
                    TEXTURAS[i], buscarSonido(SONIDOS[i]), TONOS[i],
                    () -> Minecraft.getInstance().setScreen(
                            new MenuSeccionScreen(Component.literal(NOMBRES[indice]), indice))));
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        if (OSCURECER_FONDO) this.renderBackground(g);

        super.render(g, mouseX, mouseY, partialTick);

        if (MOSTRAR_NOMBRES) {
            int total = NOMBRES.length * BOTON_ANCHO + (NOMBRES.length - 1) * HUECO;
            int x = (this.width - total) / 2 + OFFSET_X;
            int y = (this.height - BOTON_ALTO) / 2 + OFFSET_Y;

            for (int i = 0; i < NOMBRES.length; i++) {
                g.drawCenteredString(this.font, NOMBRES[i],
                        x + i * (BOTON_ANCHO + HUECO) + BOTON_ANCHO / 2,
                        y + BOTON_ALTO + NOMBRE_OFFSET_Y, 0xFFFFFFFF);
            }
        }
    }
}