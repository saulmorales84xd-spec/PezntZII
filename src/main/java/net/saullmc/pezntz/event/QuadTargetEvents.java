package net.saullmc.pezntz.event;

import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.entity.custom.Quad;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class QuadTargetEvents {

    private static final int TARGET_PRIORITY = 3;

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;

        if (event.getEntity() instanceof Zombie zombie) {
            zombie.targetSelector.addGoal(TARGET_PRIORITY,
                    new NearestAttackableTargetGoal<>(zombie, Quad.class, true));
        }
    }
}