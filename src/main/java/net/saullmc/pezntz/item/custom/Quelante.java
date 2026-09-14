package net.saullmc.pezntz.item.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.saullmc.pezntz.effect.ModEffects;

public class Quelante extends JeringaItem {

    public Quelante(Properties properties) {
        super(properties, "quelante");
    }

    @Override
    protected void aplicar(ServerPlayer usuario, Player receptor) {
        receptor.removeEffect(ModEffects.RADIACION.get());
    }
}