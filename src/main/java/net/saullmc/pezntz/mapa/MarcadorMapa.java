package net.saullmc.pezntz.mapa;

import net.minecraft.network.FriendlyByteBuf;

public record MarcadorMapa(String nombre, int x, int z, String icono) {

    public void escribir(FriendlyByteBuf buf) {
        buf.writeUtf(this.nombre);
        buf.writeInt(this.x);
        buf.writeInt(this.z);
        buf.writeUtf(this.icono);
    }

    public static MarcadorMapa leer(FriendlyByteBuf buf) {
        return new MarcadorMapa(buf.readUtf(), buf.readInt(), buf.readInt(), buf.readUtf());
    }
}