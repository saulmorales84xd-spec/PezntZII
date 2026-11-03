package net.saullmc.pezntz.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.HashSet;
import java.util.Set;

public class BestiarioData {

    private final Set<String> vistos = new HashSet<>();

    public boolean conoce(String bicho) {
        return this.vistos.contains(bicho);
    }

    public boolean descubrir(String bicho) {
        return this.vistos.add(bicho);
    }

    public Set<String> getVistos() {
        return this.vistos;
    }

    public void ponerTodos(Set<String> nuevos) {
        this.vistos.clear();
        this.vistos.addAll(nuevos);
    }

    public void copyFrom(BestiarioData otro) {
        ponerTodos(otro.vistos);
    }

    public void saveNBTData(CompoundTag tag) {
        ListTag lista = new ListTag();
        for (String bicho : this.vistos) {
            lista.add(StringTag.valueOf(bicho));
        }
        tag.put("Vistos", lista);
    }

    public void loadNBTData(CompoundTag tag) {
        this.vistos.clear();

        ListTag lista = tag.getList("Vistos", Tag.TAG_STRING);
        for (int i = 0; i < lista.size(); i++) {
            this.vistos.add(lista.getString(i));
        }
    }
}