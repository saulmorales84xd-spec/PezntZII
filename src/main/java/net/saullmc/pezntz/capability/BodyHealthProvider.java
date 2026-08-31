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

public class BodyHealthProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final Capability<BodyHealthData> PLAYER_BODY_HEALTH = CapabilityManager.get(new CapabilityToken<>() { });

    private BodyHealthData bodyHealth = null;
    private final LazyOptional<BodyHealthData> optional = LazyOptional.of(this::createBodyHealth);

    private BodyHealthData createBodyHealth() {
        if (this.bodyHealth == null) {
            this.bodyHealth = new BodyHealthData();
        }
        return this.bodyHealth;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_BODY_HEALTH) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createBodyHealth().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createBodyHealth().loadNBTData(nbt);
    }
}
