package net.saullmc.pezntz.item.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.saullmc.pezntz.effect.ModEffects;

public class Cura extends JeringaItem {

    public Cura(Properties properties) {
        super(properties, "cura");
    }

    @Override
    protected void aplicar(ServerPlayer usuario, Player receptor) {
        receptor.removeEffect(ModEffects.Infeccion.get());
    }
}