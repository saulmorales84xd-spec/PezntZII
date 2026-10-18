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

    public static final float OFFSET_X = 0.5F;
    public static final float OFFSET_Z = 0.5F;

    public static final float OFFSET_Y = 0.25F;

    public static final float DESPLAZAMIENTO_LARGO = -1.0F;

    public static final float ESCALA = 1.0F;

    public static final float GIRO_EXTRA = 0.0F;

    public static final float CABEZA_Z = 22.5F;
    public static final float CABEZA_Y = 0.0F;

    public static final float BRAZO_IZQ_Z = -22.5F;
    public static final float BRAZO_DER_Z = 22.5F;

    public static final float PIERNA_IZQ_Z = 0.0F;
    public static final float PIERNA_DER_Z = -22.5F;

    public static final boolean MOSTRAR_CAPAS = true;

    public static final int DISTANCIA_MAXIMA = 48;

    private static final float A_RADIANES = (float) (Math.PI / 180.0);

    private final PlayerModel<AbstractClientPlayer> modeloNormal;
    private final PlayerModel<AbstractClientPlayer> modeloFino;

    public CadaverRenderer(BlockEntityRendererProvider.Context context) {
        this.modeloNormal = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false);
        this.modeloFino = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
    }

    @Override
    public void render(CadaverEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        PlayerModel<AbstractClientPlayer> modelo = esFino(be) ? this.modeloFino : this.modeloNormal;
        ResourceLocation skin = resolverSkin(be);

        prepararPostura(modelo);

        poseStack.pushPose();

        poseStack.translate(OFFSET_X, OFFSET_Y, OFFSET_Z);

        Direction cara = be.getBlockState().getValue(Cadaver.FACING);
        poseStack.mulPose(Axis.YP.rotationDegrees(-cara.toYRot() + GIRO_EXTRA));

        poseStack.translate(0.0F, 0.0F, DESPLAZAMIENTO_LARGO);

        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.scale(ESCALA, ESCALA, ESCALA);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityTranslucent(skin));
        modelo.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

    private void prepararPostura(PlayerModel<AbstractClientPlayer> m) {
        m.young = false;
        m.crouching = false;
        m.riding = false;
        m.attackTime = 0.0F;
        m.swimAmount = 0.0F;

        m.head.setPos(0.0F, 0.0F, 0.0F);
        m.head.setRotation(0.0F, CABEZA_Y * A_RADIANES, CABEZA_Z * A_RADIANES);

        m.body.setRotation(0.0F, 0.0F, 0.0F);

        m.rightArm.setRotation(0.0F, 0.0F, BRAZO_DER_Z * A_RADIANES);
        m.leftArm.setRotation(0.0F, 0.0F, BRAZO_IZQ_Z * A_RADIANES);

        m.rightLeg.setRotation(0.0F, 0.0F, PIERNA_DER_Z * A_RADIANES);
        m.leftLeg.setRotation(0.0F, 0.0F, PIERNA_IZQ_Z * A_RADIANES);

        m.hat.copyFrom(m.head);
        m.jacket.copyFrom(m.body);
        m.rightSleeve.copyFrom(m.rightArm);
        m.leftSleeve.copyFrom(m.leftArm);
        m.rightPants.copyFrom(m.rightLeg);
        m.leftPants.copyFrom(m.leftLeg);

        capa(m.hat);
        capa(m.jacket);
        capa(m.rightSleeve);
        capa(m.leftSleeve);
        capa(m.rightPants);
        capa(m.leftPants);
    }

    private static void capa(ModelPart parte) {
        parte.visible = MOSTRAR_CAPAS;
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