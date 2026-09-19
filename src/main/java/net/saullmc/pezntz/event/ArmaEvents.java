package net.saullmc.pezntz.event;

import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.custom.ArmaMelee;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class ArmaEvents {

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        var enMano = event.getEntity().getMainHandItem();

        if (enMano.getItem() instanceof ArmaMelee arma && arma.esSinGolpe()) {
            event.setCanceled(true);
        }
    }
}