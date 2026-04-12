package com.jedtech.saturation_regen.neoforge;

import com.jedtech.saturation_regen.SaturationRegenConfig;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

@Mod("saturation_regen")
public final class SaturationRegenNeoForgeMod {
    public SaturationRegenNeoForgeMod() {
        SaturationRegenConfig.load(FMLPaths.CONFIGDIR.get());
    }
}
