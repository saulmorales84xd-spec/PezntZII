package net.saullmc.pezntz.item.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Coagulante extends JeringaItem {

    public Coagulante(Properties properties) {
        super(properties, "coagulante");
    }

    @Override
    protected void aplicar(ServerPlayer usuario, Player receptor) {

        List<MobEffect> aQuitar = new ArrayList<>();

        for (MobEffectInstance instancia : receptor.getActiveEffects()) {
            if (instancia.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                aQuitar.add(instancia.getEffect());
            }
        }

        for (MobEffect efecto : aQuitar) {
            receptor.removeEffect(efecto);
        }
    }
}