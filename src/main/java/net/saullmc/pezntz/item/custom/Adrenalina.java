package net.saullmc.pezntz.item.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class Adrenalina extends JeringaItem {

    public static final int FUERZA_TICKS = 600;
    public static final int FUERZA_NIVEL = 0;

    public static final int VELOCIDAD_TICKS = 200;
    public static final int VELOCIDAD_NIVEL = 0;

    public Adrenalina(Properties properties) {
        super(properties, "adrenalina");
    }

    private static boolean playingUseAnimation = false;

    public static void setPlayingUseAnimation(boolean playing) {
        playingUseAnimation = playing;
    }

    public static boolean isPlayingUseAnimation() {
        return playingUseAnimation;
    }

    @Override
    protected void aplicar(ServerPlayer usuario, Player receptor) {
        receptor.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, FUERZA_TICKS, FUERZA_NIVEL));
        receptor.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, VELOCIDAD_TICKS, VELOCIDAD_NIVEL));
    }
}