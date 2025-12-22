package com.jedtech.xp_stream.fabric;

import com.jedtech.xp_stream.XpStreamConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class XpStreamFabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Load configuration from config/xp_stream.json
        XpStreamConfig.load(FabricLoader.getInstance().getConfigDir());
    }
}
