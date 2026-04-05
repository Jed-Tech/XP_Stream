package com.jedtech.saturation_regen.fabric;

import net.fabricmc.api.ModInitializer;

public final class SaturationRegenFabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        SaturationRegenConfig.loadFromDisk();
    }
}
