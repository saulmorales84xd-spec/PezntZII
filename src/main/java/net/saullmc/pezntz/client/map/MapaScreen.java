package net.saullmc.pezntz.client.map;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.client.DropCliente;
import net.saullmc.pezntz.client.KeyBindingHandler;
import net.saullmc.pezntz.mapa.DatosMapa;
import net.saullmc.pezntz.mapa.MapaInfo;
import net.saullmc.pezntz.mapa.MarcadorMapa;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class MapaScreen extends Screen {

    public static final float TAMANIO_PANEL = 0.92F;

    public static final int PANEL_OFFSET_X = 0;
    public static final int PANEL_OFFSET_Y = 0;

    public static final int COLOR_FONDO = 0xF2101010;

    public static final int COLOR_BORDE = 0xFF000000;
    public static final int GROSOR_BORDE = 2;

    public static final float ZOOM_MAXIMO = 16.0F;

    public static final float ZOOM_PASO = 1.25F;

    public static final int BOTON_ARRASTRAR = 1;

    public static final boolean RECORDAR_VISTA = false;

    public static final boolean SUAVIZAR = true;

    public static final boolean PIXELADO_AL_ACERCAR = true;

    public static final boolean MOSTRAR_TEXTURAS = true;

    public static final float ZOOM_TEXTURAS = 6.0F;

    public static final String TEXTO_SIN_MAPA = "No hay mapa";
    public static final String TEXTO_CARGANDO = "Cargando mapa";
    public static final String TEXTO_PREPARANDO = "Preparando mapa";
    public static final int COLOR_TEXTO_AVISO = 0xFFAAAAAA;

    public static final ResourceLocation ICONO_DROP =
            new ResourceLocation(PezntZMod.MOD_ID, "textures/item/marcador_drop.png");
    public static final int ICONO_DROP_ANCHO = 16;
    public static final int ICONO_DROP_ALTO = 16;

    public static final String NOMBRE_DROP = "Airdrop";

    public static final boolean MOSTRAR_DROPS = true;
    public static final boolean MOSTRAR_MARCADORES = true;

    public static final float ICONO_TAMANIO = 16.0F;

    public static final float ESCALA_DROPS = 1.0F;
    public static final float ESCALA_MARCADORES = 1.0F;

    public static final float ICONO_CRECER_CON_ZOOM = 0.0F;

    public static final float ICONO_MINIMO = 6.0F;
    public static final float ICONO_MAXIMO = 64.0F;

    private static String vistaId = null;
    private static double vistaX;
    private static double vistaZ;
    private static double vistaZoom;

    private final Map<String, ItemStack> iconos = new HashMap<>();

    private int panelX;
    private int panelY;
    private int panelLado;

    private String idActual = null;

    private double centroX;
    private double centroZ;
    private double zoom = 1.0D;

    private boolean arrastrando = false;

    public MapaScreen() {
        super(Component.empty());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        this.panelLado = Math.max(16, Math.round(Math.min(this.width, this.height) * TAMANIO_PANEL));
        this.panelX = (this.width - this.panelLado) / 2 + PANEL_OFFSET_X;
        this.panelY = (this.height - this.panelLado) / 2 + PANEL_OFFSET_Y;
        this.idActual = null;
    }

    private double zoomMinimo(DatosMapa d) {
        return this.panelLado / (double) Math.max(1, d.bloquesLado());
    }

    private double zoomMaximo(DatosMapa d) {
        return Math.max(zoomMinimo(d), ZOOM_MAXIMO);
    }

    private void prepararVista(DatosMapa d) {
        this.idActual = d.id;

        if (RECORDAR_VISTA && d.id.equals(vistaId)) {
            this.centroX = vistaX;
            this.centroZ = vistaZ;
            this.zoom = vistaZoom;
        } else {
            this.centroX = d.x0 + d.bloquesLado() / 2.0D;
            this.centroZ = d.z0 + d.bloquesLado() / 2.0D;
            this.zoom = zoomMinimo(d);
        }

        limitar(d);
    }

    private void limitar(DatosMapa d) {
        this.zoom = Math.max(zoomMinimo(d), Math.min(zoomMaximo(d), this.zoom));

        double mitad = this.panelLado / 2.0D / this.zoom;
        double lado = d.bloquesLado();

        this.centroX = sujetar(this.centroX, d.x0 + mitad, d.x0 + lado - mitad, d.x0 + lado / 2.0D);
        this.centroZ = sujetar(this.centroZ, d.z0 + mitad, d.z0 + lado - mitad, d.z0 + lado / 2.0D);
    }

    private static double sujetar(double valor, double minimo, double maximo, double centro) {
        if (minimo > maximo) return centro;
        return Math.max(minimo, Math.min(maximo, valor));
    }

    private void guardarVista() {
        if (!RECORDAR_VISTA || this.idActual == null) return;

        vistaId = this.idActual;
        vistaX = this.centroX;
        vistaZ = this.centroZ;
        vistaZoom = this.zoom;
    }

    private float pantallaX(double mundoX) {
        return (float) (this.panelX + this.panelLado / 2.0D + (mundoX - this.centroX) * this.zoom);
    }

    private float pantallaY(double mundoZ) {
        return (float) (this.panelY + this.panelLado / 2.0D + (mundoZ - this.centroZ) * this.zoom);
    }

    private boolean dentroPanel(double x, double y) {
        return x >= this.panelX && x < this.panelX + this.panelLado
                && y >= this.panelY && y < this.panelY + this.panelLado;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.fill(0, 0, this.width, this.height, COLOR_FONDO);

        if (GROSOR_BORDE > 0 && (COLOR_BORDE >>> 24) != 0) {
            int b = GROSOR_BORDE;
            g.fill(this.panelX - b, this.panelY - b, this.panelX + this.panelLado + b, this.panelY, COLOR_BORDE);
            g.fill(this.panelX - b, this.panelY + this.panelLado, this.panelX + this.panelLado + b, this.panelY + this.panelLado + b, COLOR_BORDE);
            g.fill(this.panelX - b, this.panelY, this.panelX, this.panelY + this.panelLado, COLOR_BORDE);
            g.fill(this.panelX + this.panelLado, this.panelY, this.panelX + this.panelLado + b, this.panelY + this.panelLado, COLOR_BORDE);
        }

        MapaInfo info = MapaCliente.info();
        DatosMapa d = MapaCliente.datos();

        if (info == null || !MapaCliente.hayMapa()) {
            aviso(g, TEXTO_SIN_MAPA);
            return;
        }

        if (MapaCliente.descargando()) {
            aviso(g, TEXTO_CARGANDO.isEmpty() ? "" : TEXTO_CARGANDO + " " + Math.round(MapaCliente.progreso() * 100.0F) + "%");
            return;
        }

        if (!MapaCliente.listo() || d == null) {
            aviso(g, TEXTO_PREPARANDO);
            return;
        }

        if (!d.id.equals(this.idActual)) prepararVista(d);

        g.enableScissor(this.panelX, this.panelY, this.panelX + this.panelLado, this.panelY + this.panelLado);

        dibujarImagen(g, d);

        if (MOSTRAR_TEXTURAS && this.zoom >= ZOOM_TEXTURAS) dibujarTexturas(g, d);

        String encima = dibujarMarcadores(g, d, mouseX, mouseY, partialTick);

        g.disableScissor();

        if (encima != null && !encima.isEmpty()) {
            g.renderTooltip(this.font, Component.literal(encima), mouseX, mouseY);
        }
    }

    private void aviso(GuiGraphics g, String texto) {
        if (texto == null || texto.isEmpty()) return;

        int ancho = this.font.width(texto);
        g.drawString(this.font, texto, (this.width - ancho) / 2, (this.height - this.font.lineHeight) / 2,
                COLOR_TEXTO_AVISO, false);
    }

    private void dibujarImagen(GuiGraphics g, DatosMapa d) {
        float x0 = pantallaX(d.x0);
        float y0 = pantallaY(d.z0);
        float x1 = pantallaX(d.x0 + d.bloquesLado());
        float y1 = pantallaY(d.z0 + d.bloquesLado());

        TexturaMapa tex = MapaCliente.texturaMapa();
        if (tex == null) return;

        tex.filtro(SUAVIZAR, PIXELADO_AL_ACERCAR);

        ResourceLocation textura = MapaCliente.textura();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, textura);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();

        Matrix4f m = g.pose().last().pose();
        BufferBuilder bb = Tesselator.getInstance().getBuilder();

        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bb.vertex(m, x0, y0, 0.0F).uv(0.0F, 0.0F).endVertex();
        bb.vertex(m, x0, y1, 0.0F).uv(0.0F, 1.0F).endVertex();
        bb.vertex(m, x1, y1, 0.0F).uv(1.0F, 1.0F).endVertex();
        bb.vertex(m, x1, y0, 0.0F).uv(1.0F, 0.0F).endVertex();
        BufferUploader.drawWithShader(bb.end());
    }

    private void dibujarTexturas(GuiGraphics g, DatosMapa d) {
        double mitad = this.panelLado / 2.0D / this.zoom;
        int bpp = d.bpp;

        int iDesde = Math.max(0, (int) Math.floor((this.centroX - mitad - d.x0) / bpp));
        int iHasta = Math.min(d.lado - 1, (int) Math.floor((this.centroX + mitad - d.x0) / bpp));
        int jDesde = Math.max(0, (int) Math.floor((this.centroZ - mitad - d.z0) / bpp));
        int jHasta = Math.min(d.lado - 1, (int) Math.floor((this.centroZ + mitad - d.z0) / bpp));

        if (iDesde > iHasta || jDesde > jHasta) return;

        ColoresMapa colores = MapaCliente.colores();

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Matrix4f m = g.pose().last().pose();
        BufferBuilder bb = Tesselator.getInstance().getBuilder();
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        for (int j = jDesde; j <= jHasta; j++) {
            for (int i = iDesde; i <= iHasta; i++) {
                int c = j * d.lado + i;
                int e = d.estado[c];
                if (e < 0) continue;

                TextureAtlasSprite sprite = colores.sprite(d, e);
                if (sprite == null) continue;

                int tinte = colores.tinte(d, e, d.bioma[c]);
                float brillo = ColoresMapa.brillo(d, i, j);

                int rojo = Math.min(255, Math.round(((tinte >> 16) & 255) * brillo));
                int verde = Math.min(255, Math.round(((tinte >> 8) & 255) * brillo));
                int azul = Math.min(255, Math.round((tinte & 255) * brillo));

                float u0 = sprite.getU0();
                float u1 = sprite.getU1();
                float v0 = sprite.getV0();
                float v1 = sprite.getV1();

                for (int a = 0; a < bpp; a++) {
                    for (int b = 0; b < bpp; b++) {
                        double mundoX = d.x0 + (double) i * bpp + a;
                        double mundoZ = d.z0 + (double) j * bpp + b;

                        float sx0 = pantallaX(mundoX);
                        float sx1 = pantallaX(mundoX + 1.0D);
                        float sy0 = pantallaY(mundoZ);
                        float sy1 = pantallaY(mundoZ + 1.0D);

                        bb.vertex(m, sx0, sy0, 0.0F).uv(u0, v0).color(rojo, verde, azul, 255).endVertex();
                        bb.vertex(m, sx0, sy1, 0.0F).uv(u0, v1).color(rojo, verde, azul, 255).endVertex();
                        bb.vertex(m, sx1, sy1, 0.0F).uv(u1, v1).color(rojo, verde, azul, 255).endVertex();
                        bb.vertex(m, sx1, sy0, 0.0F).uv(u1, v0).color(rojo, verde, azul, 255).endVertex();
                    }
                }
            }
        }

        BufferUploader.drawWithShader(bb.end());
        RenderSystem.disableBlend();
    }

    @Nullable
    private String dibujarMarcadores(GuiGraphics g, DatosMapa d, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return null;
        if (!mc.level.dimension().location().toString().equals(d.dimension)) return null;

        float factor = (float) Math.pow(this.zoom / zoomMinimo(d), ICONO_CRECER_CON_ZOOM);
        boolean raton = dentroPanel(mouseX, mouseY);

        String encima = null;

        if (MOSTRAR_MARCADORES) {
            for (MarcadoresMapa.Marcador m : MarcadoresMapa.FIJOS) {
                float tamanio = tamanio(factor * ESCALA_MARCADORES * m.escala());

                if (textura(g, m.x() + 0.5D, m.z() + 0.5D, m.textura(), m.texturaAncho(), m.texturaAlto(), tamanio)
                        && raton && sobre(m.x() + 0.5D, m.z() + 0.5D, tamanio, mouseX, mouseY)) {
                    encima = m.nombre();
                }
            }

            for (MarcadorMapa m : MapaCliente.marcadores()) {
                float tamanio = tamanio(factor * ESCALA_MARCADORES);

                boolean dibujado;
                if (m.icono().endsWith(".png")) {
                    ResourceLocation tex = ResourceLocation.tryParse(m.icono());
                    dibujado = tex != null && textura(g, m.x() + 0.5D, m.z() + 0.5D, tex, 16, 16, tamanio);
                } else {
                    dibujado = item(g, m.x() + 0.5D, m.z() + 0.5D, icono(m.icono()), tamanio);
                }

                if (dibujado && raton && sobre(m.x() + 0.5D, m.z() + 0.5D, tamanio, mouseX, mouseY)) {
                    encima = m.nombre();
                }
            }
        }

        if (MOSTRAR_DROPS) {
            float tamanio = tamanio(factor * ESCALA_DROPS);

            for (Map.Entry<Integer, DropCliente.Dato> entrada : DropCliente.activos().entrySet()) {
                Vec3 pos = DropCliente.techo(entrada.getKey(), entrada.getValue(), partialTick);

                if (textura(g, pos.x, pos.z, ICONO_DROP, ICONO_DROP_ANCHO, ICONO_DROP_ALTO, tamanio)
                        && raton && sobre(pos.x, pos.z, tamanio, mouseX, mouseY)) {
                    encima = NOMBRE_DROP;
                }
            }
        }

        return encima;
    }

    private static float tamanio(float factor) {
        return Math.max(ICONO_MINIMO, Math.min(ICONO_MAXIMO, ICONO_TAMANIO * factor));
    }

    private boolean sobre(double mundoX, double mundoZ, float tamanio, int mouseX, int mouseY) {
        float x = pantallaX(mundoX);
        float y = pantallaY(mundoZ);
        float mitad = tamanio / 2.0F;

        return mouseX >= x - mitad && mouseX <= x + mitad && mouseY >= y - mitad && mouseY <= y + mitad;
    }

    private boolean visible(float x, float y, float tamanio) {
        float margen = tamanio;
        return x >= this.panelX - margen && x <= this.panelX + this.panelLado + margen
                && y >= this.panelY - margen && y <= this.panelY + this.panelLado + margen;
    }

    private boolean textura(GuiGraphics g, double mundoX, double mundoZ, ResourceLocation textura,
                            int texturaAncho, int texturaAlto, float tamanio) {

        float x = pantallaX(mundoX);
        float y = pantallaY(mundoZ);
        if (!visible(x, y, tamanio)) return false;

        float ancho = tamanio;
        float alto = tamanio * texturaAlto / (float) Math.max(1, texturaAncho);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        g.pose().pushPose();
        g.pose().translate(x - ancho / 2.0F, y - alto / 2.0F, 50.0F);
        g.pose().scale(ancho / texturaAncho, alto / texturaAlto, 1.0F);
        g.blit(textura, 0, 0, 0.0F, 0.0F, texturaAncho, texturaAlto, texturaAncho, texturaAlto);
        g.pose().popPose();

        RenderSystem.disableBlend();
        return true;
    }

    private boolean item(GuiGraphics g, double mundoX, double mundoZ, ItemStack stack, float tamanio) {
        if (stack.isEmpty()) return false;

        float x = pantallaX(mundoX);
        float y = pantallaY(mundoZ);
        if (!visible(x, y, tamanio)) return false;

        float escala = tamanio / 16.0F;

        g.pose().pushPose();
        g.pose().translate(x - tamanio / 2.0F, y - tamanio / 2.0F, 50.0F);
        g.pose().scale(escala, escala, 1.0F);
        g.renderItem(stack, 0, 0);
        g.pose().popPose();
        return true;
    }

    private ItemStack icono(String id) {
        return this.iconos.computeIfAbsent(id, clave -> {
            ResourceLocation rl = ResourceLocation.tryParse(clave);
            if (rl == null) return ItemStack.EMPTY;

            Item item = BuiltInRegistries.ITEM.get(rl);
            return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
        });
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int boton) {
        if (boton == BOTON_ARRASTRAR && dentroPanel(mouseX, mouseY)) {
            this.arrastrando = true;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, boton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int boton) {
        if (boton == BOTON_ARRASTRAR) this.arrastrando = false;
        return super.mouseReleased(mouseX, mouseY, boton);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int boton, double dx, double dy) {
        DatosMapa d = MapaCliente.datos();

        if (boton != BOTON_ARRASTRAR || !this.arrastrando || d == null || !MapaCliente.listo()) {
            return super.mouseDragged(mouseX, mouseY, boton, dx, dy);
        }

        this.centroX -= dx / this.zoom;
        this.centroZ -= dy / this.zoom;

        limitar(d);
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        DatosMapa d = MapaCliente.datos();
        if (d == null || !MapaCliente.listo() || delta == 0.0D) return false;
        if (!dentroPanel(mouseX, mouseY)) return false;

        double centroPanelX = this.panelX + this.panelLado / 2.0D;
        double centroPanelY = this.panelY + this.panelLado / 2.0D;

        double antesX = this.centroX + (mouseX - centroPanelX) / this.zoom;
        double antesZ = this.centroZ + (mouseY - centroPanelY) / this.zoom;

        this.zoom = Math.max(zoomMinimo(d), Math.min(zoomMaximo(d), this.zoom * Math.pow(ZOOM_PASO, delta)));

        this.centroX = antesX - (mouseX - centroPanelX) / this.zoom;
        this.centroZ = antesZ - (mouseY - centroPanelY) / this.zoom;

        limitar(d);
        return true;
    }

    @Override
    public boolean keyPressed(int tecla, int codigo, int modificadores) {
        if (KeyBindingHandler.MAPA_KEY.matches(tecla, codigo)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(tecla, codigo, modificadores);
    }

    @Override
    public void removed() {
        guardarVista();
        super.removed();
    }

    @Override
    public void resize(Minecraft mc, int ancho, int alto) {
        guardarVista();
        super.resize(mc, ancho, alto);
    }
}