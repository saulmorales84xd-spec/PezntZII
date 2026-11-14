package net.saullmc.pezntz.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.saullmc.pezntz.entity.custom.VehiculoBase;

import java.util.List;

public class KitVehiculos extends Item {

    public static final float CURACION = 1000.0F;

    public static final String SONIDO = "minecraft:block.anvil.use";

    public static final float VOLUMEN = 1.0F;
    public static final float TONO = 1.0F;

    public static final boolean GASTA_UN_USO = true;

    public static final boolean AVISAR = true;

    public KitVehiculos(Properties properties) {
        super(properties);
    }

    public static boolean reparar(VehiculoBase vehiculo, Player jugador, ItemStack kit) {

        if (vehiculo.getHealth() >= vehiculo.getMaxHealth()) {
            if (AVISAR) {
                jugador.displayClientMessage(
                        Component.literal("Este vehiculo no esta daniado").withStyle(ChatFormatting.GRAY), true);
            }
            return false;
        }

        float antes = vehiculo.getHealth();
        vehiculo.heal(CURACION);
        int curado = Math.round(vehiculo.getHealth() - antes);

        SoundEvent evento = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(SONIDO));
        if (evento != null) {
            vehiculo.level().playSound(null, vehiculo.blockPosition(),
                    evento, SoundSource.BLOCKS, VOLUMEN, TONO);
        }

        if (GASTA_UN_USO && !jugador.getAbilities().instabuild) {
            kit.shrink(1);
        }

        if (AVISAR) {
            jugador.displayClientMessage(
                    Component.literal("Vehiculo reparado  +" + curado + " de vida")
                            .withStyle(ChatFormatting.GREEN), true);
        }

        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
    }
}