package net.saullmc.pezntz.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.saullmc.pezntz.entity.ModEntities;
import net.saullmc.pezntz.entity.custom.DropCayendo;

import java.util.List;

public class Bengala extends Item {

    public static final int ALTURA_CAIDA = DropCayendo.ALTURA_CAIDA;

    public static final boolean GASTA_UN_USO = true;

    public static final String SONIDO = "minecraft:entity.firework_rocket.launch";

    public static final float VOLUMEN = 1.0F;
    public static final float TONO = 1.0F;

    public Bengala(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext contexto) {
        Level nivel = contexto.getLevel();

        if (nivel.isClientSide) return InteractionResult.SUCCESS;

        BlockPos destino = contexto.getClickedPos().relative(contexto.getClickedFace());

        DropCayendo caja = new DropCayendo(ModEntities.DROP_CAYENDO.get(), nivel);
        caja.prepararCaida(destino, ALTURA_CAIDA);

        nivel.addFreshEntity(caja);

        SoundEvent evento = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(SONIDO));
        if (evento != null) {
            nivel.playSound(null, destino, evento, SoundSource.PLAYERS, VOLUMEN, TONO);
        }

        Player jugador = contexto.getPlayer();

        if (GASTA_UN_USO && (jugador == null || !jugador.getAbilities().instabuild)) {
            contexto.getItemInHand().shrink(1);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Click derecho en el suelo").withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.literal("Llama a un airdrop").withStyle(ChatFormatting.AQUA));
    }
}
