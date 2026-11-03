package net.saullmc.pezntz.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BestiarioProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final Capability<BestiarioData> PLAYER_BESTIARIO =
            CapabilityManager.get(new CapabilityToken<>() { });

    private BestiarioData bestiario = null;
    private final LazyOptional<BestiarioData> optional = LazyOptional.of(this::crear);

    private BestiarioData crear() {
        if (this.bestiario == null) {
            this.bestiario = new BestiarioData();
        }
        return this.bestiario;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_BESTIARIO) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        crear().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        crear().loadNBTData(nbt);
    }
}