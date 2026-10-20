package net.saullmc.pezntz.client.screen.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.ModItems;

public class MenuPrincipalScreen extends Screen {

    public static final int BOTON_ANCHO = 64;
    public static final int BOTON_ALTO = 64;

    public static final int HUECO = 16;

    public static final int OFFSET_X = 0;
    public static final int OFFSET_Y = 0;

    public static final boolean OSCURECER_FONDO = true;

    private static final ResourceLocation[] TEXTURAS = {
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/boton_1.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/boton_2.png"),
            new ResourceLocation(PezntZMod.MOD_ID, "textures/gui/boton_3.png"),
    };


    private static ItemStack[] tooltips() {
        return new ItemStack[]{
                new ItemStack(ModItems.BOTON_1.get()),
                new ItemStack(ModItems.BOTON_2.get()),
                new ItemStack(ModItems.BOTON_3.get()),
        };
    }

    private static final String[] SONIDOS = {
            "minecraft:ui.button.click",
            "minecraft:ui.button.click",
            "minecraft:ui.button.click",
    };

    private static final float[] TONOS = {0.8F, 1.1F, 1.4F};

    private static final String SONIDO_ABRIR = "minecraft:ui.toast.in";

    private BotonMenu[] botones = new BotonMenu[0];

    public MenuPrincipalScreen() {
        super(Component.literal("Menu"));
    }

    private static SoundEvent buscarSonido(String id) {
        return BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(id));
    }

    private static void reproducir(String id, float tono) {
        SoundEvent evento = buscarSonido(id);
        if (evento == null) return;

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(evento, tono));
    }

    public static void abrir() {
        Minecraft.getInstance().setScreen(new MenuPrincipalScreen());
        reproducir(SONIDO_ABRIR, 1.0F);
    }

    @Override
    protected void init() {
        ItemStack[] items = tooltips();
        int cuantos = TEXTURAS.length;

        int total = cuantos * BOTON_ANCHO + (cuantos - 1) * HUECO;
        int x = (this.width - total) / 2 + OFFSET_X;
        int y = (this.height - BOTON_ALTO) / 2 + OFFSET_Y;

        this.botones = new BotonMenu[cuantos];

        for (int i = 0; i < cuantos; i++) {
            final int indice = i;

            BotonMenu boton = new BotonMenu(
                    x + i * (BOTON_ANCHO + HUECO), y,
                    BOTON_ANCHO, BOTON_ALTO,
                    Component.empty(),
                    TEXTURAS[i],
                    i < items.length ? items[i] : ItemStack.EMPTY,
                    buscarSonido(SONIDOS[i]), TONOS[i],
                    () -> Minecraft.getInstance().setScreen(new MenuSeccionScreen(indice)));

            this.botones[i] = boton;
            this.addRenderableWidget(boton);
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

        for (BotonMenu boton : this.botones) {
            if (!boton.isHovered()) continue;

            ItemStack stack = boton.getItemTooltip();
            if (stack.isEmpty()) continue;

            g.renderTooltip(this.font, stack, mouseX, mouseY);
            break;
        }
    }
}