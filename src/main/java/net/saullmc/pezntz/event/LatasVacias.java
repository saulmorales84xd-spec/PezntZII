package net.saullmc.pezntz.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.ModItems;

import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID)
public class LatasVacias {

    public static final List<Supplier<Item>> LATAS = List.of(
            ModItems.LATA_POLLO::get,
            ModItems.LATA_ATUN::get,
            ModItems.LATA_CARNE::get
    );

    public static final Supplier<Item> ENVASE = ModItems.LATA_VACIA::get;

    @SubscribeEvent
    public static void alTerminarDeComer(LivingEntityUseItemEvent.Finish evento) {
        if (!esLata(evento.getItem())) return;

        LivingEntity quien = evento.getEntity();

        if (evento.getResultStack().isEmpty()) {
            evento.setResultStack(new ItemStack(ENVASE.get()));
            return;
        }

        if (quien.level().isClientSide()) return;
        if (quien instanceof Player jugador && jugador.getAbilities().instabuild) return;

        ItemStack lata = new ItemStack(ENVASE.get());

        if (quien instanceof Player jugador) {
            if (!jugador.getInventory().add(lata)) jugador.drop(lata, false);
        } else {
            quien.spawnAtLocation(lata);
        }
    }

    private static boolean esLata(ItemStack stack) {
        if (stack.isEmpty()) return false;
        for (Supplier<Item> lata : LATAS) {
            if (stack.is(lata.get())) return true;
        }
        return false;
    }
}