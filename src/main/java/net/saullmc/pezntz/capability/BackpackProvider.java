package net.saullmc.pezntz.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackpackProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

    public static Capability<BackpackData> PLAYER_BACKPACK =
            CapabilityManager.get(new CapabilityToken<BackpackData>() { });

    private BackpackData backpack = null;
    private final LazyOptional<BackpackData> optional = LazyOptional.of(this::crearSiHaceFalta);

    private BackpackData crearSiHaceFalta() {
        if (this.backpack == null) {
            this.backpack = new BackpackData();
        }
        return this.backpack;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_BACKPACK) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        crearSiHaceFalta().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        crearSiHaceFalta().loadNBTData(nbt);
    }
}