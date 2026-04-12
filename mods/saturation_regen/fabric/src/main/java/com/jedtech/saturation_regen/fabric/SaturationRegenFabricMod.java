package com.jedtech.saturation_regen.fabric;

import com.jedtech.saturation_regen.SaturationRegenConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class SaturationRegenFabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        SaturationRegenConfig.load(FabricLoader.getInstance().getConfigDir());
    }
}
