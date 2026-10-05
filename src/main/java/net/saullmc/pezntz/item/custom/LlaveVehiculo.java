package net.saullmc.pezntz.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class LlaveVehiculo extends Item {

    private static final String TAG_VEHICULO = "VehiculoUUID";

    public LlaveVehiculo(Properties properties) {
        super(properties);
    }

    public static boolean estaEmparejada(ItemStack stack) {
        return stack.getTag() != null && stack.getTag().hasUUID(TAG_VEHICULO);
    }

    public static UUID getVehiculo(ItemStack stack) {
        return estaEmparejada(stack) ? stack.getTag().getUUID(TAG_VEHICULO) : null;
    }

    public static void emparejar(ItemStack stack, UUID vehiculo) {
        stack.getOrCreateTag().putUUID(TAG_VEHICULO, vehiculo);
    }

    public static boolean sirvePara(ItemStack stack, UUID vehiculo) {
        return estaEmparejada(stack) && stack.getTag().getUUID(TAG_VEHICULO).equals(vehiculo);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        if (estaEmparejada(stack)) {

            String id = getVehiculo(stack).toString().substring(0, 8);
            tooltip.add(Component.literal("Emparejada: " + id).withStyle(ChatFormatting.GOLD));
        } else {
            tooltip.add(Component.literal("Sin emparejar").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return estaEmparejada(stack);
    }
}