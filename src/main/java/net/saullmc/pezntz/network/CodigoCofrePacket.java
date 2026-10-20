package net.saullmc.pezntz.network;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.saullmc.pezntz.block.custom.CofreCandado;
import net.saullmc.pezntz.block.entity.CofreCandadoEntity;

import java.util.function.Supplier;

public class CodigoCofrePacket {

    public static final boolean ABRIR_AL_PONER_CODIGO = true;

    private static final int MAX_LARGO = 16;

    private static final double DISTANCIA_MAXIMA = 8.0D;

    private final BlockPos pos;
    private final String codigo;

    public CodigoCofrePacket(BlockPos pos, String codigo) {
        this.pos = pos;
        this.codigo = codigo;
    }

    public CodigoCofrePacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.codigo = buf.readUtf(MAX_LARGO);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeUtf(this.codigo, MAX_LARGO);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context contexto = supplier.get();

        contexto.enqueueWork(() -> {
            ServerPlayer jugador = contexto.getSender();
            if (jugador == null) return;

            if (jugador.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D)
                    > DISTANCIA_MAXIMA * DISTANCIA_MAXIMA) {
                return;
            }

            BlockEntity be = jugador.level().getBlockEntity(pos);
            if (!(be instanceof CofreCandadoEntity cofre)) return;

            if (!cofre.tieneCodigo()) {
                if (!cofre.ponerCodigo(codigo)) {
                    mensaje(jugador, "El codigo deben ser 4 numeros", ChatFormatting.RED);
                    return;
                }

                cofre.setDuenio(jugador);
                mensaje(jugador, "Codigo guardado", ChatFormatting.GREEN);
                sonido(jugador, SoundEvents.IRON_DOOR_CLOSE, 1.2F);

                if (ABRIR_AL_PONER_CODIGO) {
                    CofreCandado.abrirContenido(jugador, cofre);
                }
                return;
            }

            if (cofre.codigoCorrecto(codigo)) {
                sonido(jugador, SoundEvents.IRON_DOOR_OPEN, 1.2F);
                CofreCandado.abrirContenido(jugador, cofre);
            } else {
                mensaje(jugador, "Codigo incorrecto", ChatFormatting.RED);
                sonido(jugador, SoundEvents.IRON_DOOR_CLOSE, 0.7F);
            }
        });

        contexto.setPacketHandled(true);
        return true;
    }

    private static void mensaje(ServerPlayer jugador, String texto, ChatFormatting color) {
        jugador.displayClientMessage(Component.literal(texto).withStyle(color), true);
    }

    private static void sonido(ServerPlayer jugador, net.minecraft.sounds.SoundEvent evento, float tono) {
        jugador.level().playSound(null, jugador.blockPosition(), evento, SoundSource.BLOCKS, 0.7F, tono);
    }
}