package net.saullmc.pezntz.client;

import java.util.HashSet;
import java.util.Set;

public class BestiarioCliente {

    private static Set<String> vistos = new HashSet<>();

    public static void poner(Set<String> nuevos) {
        vistos = new HashSet<>(nuevos);
    }

    public static boolean conoce(String bicho) {
        return vistos.contains(bicho);
    }

    public static void limpiar() {
        vistos = new HashSet<>();
    }
}