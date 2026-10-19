package net.saullmc.pezntz.client.block;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.saullmc.pezntz.block.custom.Cadaver;
import net.saullmc.pezntz.block.entity.CadaverEntity;

import java.util.UUID;

public class CadaverRenderer implements BlockEntityRenderer<CadaverEntity> {

    public static final float[] CABEZA_POS      = { 0.0000F, 0.2500F,  0.2500F};
    public static final float[] CABEZA_ROT      = {90.0F,  0.0F,  22.5F};

    public static final float[] CUERPO_POS      = { 0.0000F, 0.1250F, -0.3750F};
    public static final float[] CUERPO_ROT      = {90.0F,  0.0F,   0.0F};

    public static final float[] BRAZO_IZQ_POS   = {-0.3750F, 0.1250F, -0.3750F};
    public static final float[] BRAZO_IZQ_ROT   = {90.0F, 22.5F,   0.0F};

    public static final float[] BRAZO_DER_POS   = { 0.5000F, 0.1250F, -0.3125F};
    public static final float[] BRAZO_DER_ROT   = {90.0F,-22.5F,   0.0F};

    public static final float[] PIERNA_IZQ_POS  = {-0.1250F, 0.1250F, -1.1250F};
    public static final float[] PIERNA_IZQ_ROT  = {90.0F,  0.0F,   0.0F};

    public static final float[] PIERNA_DER_POS  = { 0.1250F, 0.1250F, -1.1250F};
    public static final float[] PIERNA_DER_ROT  = {90.0F,-22.5F,   0.0F};

    public static final boolean MOSTRAR_CAPAS = true;

    public static final float GIRO_EXTRA = 0.0F;

    public static final int DISTANCIA_MAXIMA = 48;

    private static final float[] C_CABEZA = {0.0F, -4.0F, 0.0F};
    private static final float[] C_CUERPO = {0.0F,  6.0F, 0.0F};
    private static final float[] C_PIERNA = {0.0F,  6.0F, 0.0F};

    private static final float[] C_BRAZO_DER_ANCHO = {-1.0F, 4.0F, 0.0F};
    private static final float[] C_BRAZO_IZQ_ANCHO = { 1.0F, 4.0F, 0.0F};
    private static final float[] C_BRAZO_DER_FINO  = {-0.5F, 4.0F, 0.0F};
    private static final float[] C_BRAZO_IZQ_FINO  = { 0.5F, 4.0F, 0.0F};

    private final PlayerModel<AbstractClientPlayer> modeloNormal;
    private final PlayerModel<AbstractClientPlayer> modeloFino;

    public CadaverRenderer(BlockEntityRendererProvider.Context context) {
        this.modeloNormal = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false);
        this.modeloFino = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
    }

    @Override
    public void render(CadaverEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        boolean fino = esFino(be);
        PlayerModel<AbstractClientPlayer> m = fino ? this.modeloFino : this.modeloNormal;
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityTranslucent(resolverSkin(be)));

        poseStack.pushPose();

        poseStack.translate(0.5D, 0.0D, 0.5D);
        Direction cara = be.getBlockState().getValue(Cadaver.FACING);
        poseStack.mulPose(Axis.YP.rotationDegrees(-cara.toYRot() + GIRO_EXTRA));
        poseStack.translate(-0.5D, 0.0D, -0.5D);

        float[] cBrazoDer = fino ? C_BRAZO_DER_FINO : C_BRAZO_DER_ANCHO;
        float[] cBrazoIzq = fino ? C_BRAZO_IZQ_FINO : C_BRAZO_IZQ_ANCHO;

        pieza(poseStack, buffer, packedLight, m.head,     m.hat,         CABEZA_POS,     CABEZA_ROT,     C_CABEZA);
        pieza(poseStack, buffer, packedLight, m.body,     m.jacket,      CUERPO_POS,     CUERPO_ROT,     C_CUERPO);
        pieza(poseStack, buffer, packedLight, m.leftArm,  m.leftSleeve,  BRAZO_IZQ_POS,  BRAZO_IZQ_ROT,  cBrazoIzq);
        pieza(poseStack, buffer, packedLight, m.rightArm, m.rightSleeve, BRAZO_DER_POS,  BRAZO_DER_ROT,  cBrazoDer);
        pieza(poseStack, buffer, packedLight, m.leftLeg,  m.leftPants,   PIERNA_IZQ_POS, PIERNA_IZQ_ROT, C_PIERNA);
        pieza(poseStack, buffer, packedLight, m.rightLeg, m.rightPants,  PIERNA_DER_POS, PIERNA_DER_ROT, C_PIERNA);

        poseStack.popPose();
    }

    private static void pieza(PoseStack ps, VertexConsumer buffer, int light,
                              ModelPart parte, ModelPart capa,
                              float[] pos, float[] rot, float[] centro) {

        ps.pushPose();

        ps.translate(0.5F + pos[0], pos[1], 0.5F + pos[2]);

        ps.mulPose(Axis.YP.rotationDegrees(rot[1]));
        ps.mulPose(Axis.ZP.rotationDegrees(rot[2]));
        ps.mulPose(Axis.XP.rotationDegrees(rot[0]));

        ps.scale(-1.0F, -1.0F, 1.0F);

        ps.translate(-centro[0] / 16.0F, -centro[1] / 16.0F, -centro[2] / 16.0F);

        parte.setPos(0.0F, 0.0F, 0.0F);
        parte.setRotation(0.0F, 0.0F, 0.0F);
        parte.visible = true;
        parte.render(ps, buffer, light, OverlayTexture.NO_OVERLAY);

        if (MOSTRAR_CAPAS && capa != null) {
            capa.setPos(0.0F, 0.0F, 0.0F);
            capa.setRotation(0.0F, 0.0F, 0.0F);
            capa.visible = true;
            capa.render(ps, buffer, light, OverlayTexture.NO_OVERLAY);
        }

        ps.popPose();
    }

    private static ResourceLocation resolverSkin(CadaverEntity be) {
        Minecraft mc = Minecraft.getInstance();
        UUID uuid = be.getDuenioUuid();

        if (uuid != null && mc.level != null) {
            Player jugador = mc.level.getPlayerByUUID(uuid);
            if (jugador instanceof AbstractClientPlayer cliente) {
                return cliente.getSkinTextureLocation();
            }
        }

        GameProfile perfil = be.getPerfil();
        if (perfil != null) {
            return mc.getSkinManager().getInsecureSkinLocation(perfil);
        }

        return uuid != null ? DefaultPlayerSkin.getDefaultSkin(uuid) : DefaultPlayerSkin.getDefaultSkin();
    }

    /** Si la skin es de brazos finos, tipo Alex. */
    private static boolean esFino(CadaverEntity be) {
        Minecraft mc = Minecraft.getInstance();
        UUID uuid = be.getDuenioUuid();

        if (uuid != null && mc.level != null) {
            Player jugador = mc.level.getPlayerByUUID(uuid);
            if (jugador instanceof AbstractClientPlayer cliente) {
                return "slim".equals(cliente.getModelName());
            }
        }

        return uuid != null && "slim".equals(DefaultPlayerSkin.getSkinModelName(uuid));
    }

    @Override
    public boolean shouldRenderOffScreen(CadaverEntity be) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return DISTANCIA_MAXIMA;
    }
}