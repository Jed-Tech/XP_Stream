package com.jedtech.xp_stream.neoforge;

import com.jedtech.xp_stream.XpStreamConfig;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

@Mod("xp_stream")
public final class XpStreamNeoForgeMod {
    public XpStreamNeoForgeMod() {
        // Load configuration from config/xp_stream.json
        XpStreamConfig.load(FMLPaths.CONFIGDIR.get());
    }
}














