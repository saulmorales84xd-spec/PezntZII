package net.saullmc.pezntz.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.saullmc.pezntz.PezntZMod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.saullmc.pezntz.item.ModTooltips.*;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, value = Dist.CLIENT)
public class TooltipsTacz {

    public static final int CAJA_HIERRO = 0;
    public static final int CAJA_ORO = 1;
    public static final int CAJA_DIAMANTE = 2;

    public static final boolean NOMBRE_EN_LA_MANO = true;

    public static final boolean QUITAR_DIBUJO_TACZ = false;

    private static final ResourceLocation ID_CAJA = new ResourceLocation("tacz", "ammo_box");
    private static final ResourceLocation ID_BALA = new ResourceLocation("tacz", "ammo");

    private record Texto(String nombre, int color, String[] lineas) {
    }

    private static final Map<Integer, Texto> CAJAS = new HashMap<>();
    private static final Map<String, Texto> BALAS = new HashMap<>();

    private static final Map<Texto, Component> NOMBRES = new HashMap<>();
    private static final Map<Texto, List<Component>> LINEAS = new HashMap<>();

    private static boolean registrado = false;

    public static void caja(int nivel, String nombre, int color, String... lineas) {
        CAJAS.put(nivel, new Texto(nombre, color, lineas));
    }

    public static void bala(String calibre, String nombre, int color, String... lineas) {
        String id = calibre.contains(":") ? calibre : "tacz:" + calibre;
        BALAS.put(id, new Texto(nombre, color, lineas));
    }

    public static void registrarTodos() {

        caja(CAJA_HIERRO, "Caja de Munición Pequeña [☆]", 0x6b8178,
                KEYS + "[Click Izquierdo] " + GRIS + "para arrastrar la caja hasta",
                "tus balas en el inventario.",
                KEYS + "[Click Derecho] " + GRIS + "sobre las balas con la caja",
                "para guardarlas.",
                "",
                KEYS + "[Click Izquierdo] " + GRIS + "para arrastrar la caja con",
                "balas a un lugar vacio en tu inventario.",
                KEYS + "[Click Derecho] " + GRIS + "para retirarlas de la caja.",
                "",
                ROJO_OSCURO + "Importante: " + GRIS + "Solamente puedes guardar un",
                "tipo de balas.",
                "",
                "Capacidad: " + NUM + "180 Balas");

        caja(CAJA_ORO, "Caja de Munición Mediana [☆☆]", 0x6b8178,
                KEYS + "[Click Izquierdo] " + GRIS + "para arrastrar la caja hasta",
                "tus balas en el inventario.",
                KEYS + "[Click Derecho] " + GRIS + "sobre las balas con la caja",
                "para guardarlas.",
                "",
                KEYS + "[Click Izquierdo] " + GRIS + "para arrastrar la caja con",
                "balas a un lugar vacio en tu inventario.",
                KEYS + "[Click Derecho] " + GRIS + "para retirarlas de la caja.",
                "",
                ROJO_OSCURO + "Importante: " + GRIS + "Solamente puedes guardar un",
                "tipo de balas.",
                "",
                "Capacidad: " + NUM + "360 Balas");

        caja(CAJA_DIAMANTE, "Caja de Munición Grande [☆☆☆]", 0x6b8178,
                KEYS + "[Click Izquierdo] " + GRIS + "para arrastrar la caja hasta",
                "tus balas en el inventario.",
                KEYS + "[Click Derecho] " + GRIS + "sobre las balas con la caja",
                "para guardarlas.",
                "",
                KEYS + "[Click Izquierdo] " + GRIS + "para arrastrar la caja con",
                "balas a un lugar vacio en tu inventario.",
                KEYS + "[Click Derecho] " + GRIS + "para retirarlas de la caja.",
                "",
                ROJO_OSCURO + "Importante: " + GRIS + "Solamente puedes guardar un",
                "tipo de balas.",
                "",
                "Capacidad: " + NUM + "540 Balas");

    }

    private static void asegurar() {
        if (registrado) return;
        registrado = true;
        registrarTodos();
    }

    @Nullable
    private static Texto buscar(ItemStack stack) {
        if (stack.isEmpty()) return null;

        asegurar();

        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (id == null) return null;

        CompoundTag tag = stack.getTag();

        if (id.equals(ID_CAJA)) {
            if (tag != null && (tag.getBoolean("Creative") || tag.getBoolean("AllTypeCreative"))) return null;

            int nivel = tag == null ? 0 : tag.getInt("Level");
            return CAJAS.get(nivel);
        }

        if (id.equals(ID_BALA)) {
            if (tag == null || !tag.contains("AmmoId")) return null;
            return BALAS.get(tag.getString("AmmoId"));
        }

        return null;
    }

    @Nullable
    public static Component nombre(ItemStack stack) {
        Texto texto = buscar(stack);
        if (texto == null) return null;

        return NOMBRES.computeIfAbsent(texto, t -> {
            List<Component> partes = formatear(hex(t.color()) + t.nombre());
            if (partes.isEmpty()) return Component.literal(t.nombre());

            MutableComponent junto = Component.empty();
            for (int i = 0; i < partes.size(); i++) {
                if (i > 0) junto.append(" ");
                junto.append(partes.get(i));
            }
            return junto;
        });
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void alTooltip(ItemTooltipEvent evento) {
        Texto texto = buscar(evento.getItemStack());
        if (texto == null) return;

        List<Component> tooltip = evento.getToolTip();
        tooltip.clear();

        tooltip.add(nombre(evento.getItemStack()));
        tooltip.addAll(LINEAS.computeIfAbsent(texto, t -> new ArrayList<>(formatear(t.lineas()))));
    }

    @SubscribeEvent
    public static void alJuntarTooltip(RenderTooltipEvent.GatherComponents evento) {
        if (!QUITAR_DIBUJO_TACZ) return;
        if (buscar(evento.getItemStack()) == null) return;

        evento.getTooltipElements().removeIf(e -> e.right().isPresent());
    }

    private static ItemStack ultimo = ItemStack.EMPTY;
    private static int tiempo = 0;

    @SubscribeEvent
    public static void alTick(TickEvent.ClientTickEvent evento) {
        if (evento.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.isPaused()) return;

        ItemStack enMano = mc.player.getInventory().getSelected();

        if (enMano.isEmpty()) {
            tiempo = 0;
        } else if (!ultimo.isEmpty() && enMano.is(ultimo.getItem())
                && enMano.getHoverName().equals(ultimo.getHoverName())
                && ItemStack.isSameItemSameTags(enMano, ultimo)) {
            if (tiempo > 0) tiempo--;
        } else {
            tiempo = (int) (40.0D * mc.options.notificationDisplayTime().get());
        }

        ultimo = enMano.copy();
    }

    @SubscribeEvent
    public static void alDibujarNombre(RenderGuiOverlayEvent.Pre evento) {
        if (!NOMBRE_EN_LA_MANO) return;
        if (!evento.getOverlay().id().equals(VanillaGuiOverlay.ITEM_NAME.id())) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.gameMode == null) return;
        if (mc.gameMode.getPlayerMode() == GameType.SPECTATOR) return;

        Component nombre = nombre(mc.player.getInventory().getSelected());
        if (nombre == null) return;

        evento.setCanceled(true);

        if (tiempo <= 0 || ultimo.isEmpty()) return;

        GuiGraphics g = evento.getGuiGraphics();
        Font fuente = mc.font;

        int anchoPantalla = evento.getWindow().getGuiScaledWidth();
        int altoPantalla = evento.getWindow().getGuiScaledHeight();

        int subir = 59;
        if (mc.gui instanceof ForgeGui forge) subir = Math.max(59, Math.max(forge.leftHeight, forge.rightHeight));

        int ancho = fuente.width(nombre);
        int x = (anchoPantalla - ancho) / 2;
        int y = altoPantalla - subir;
        if (!mc.gameMode.canHurtPlayer()) y += 14;

        int alfa = Math.min(255, (int) (tiempo * 256.0F / 10.0F));
        if (alfa <= 0) return;

        g.fill(x - 2, y - 2, x + ancho + 2, y + fuente.lineHeight + 2, mc.options.getBackgroundColor(0));
        g.drawString(fuente, nombre, x, y, 0xFFFFFF | (alfa << 24));
    }
}