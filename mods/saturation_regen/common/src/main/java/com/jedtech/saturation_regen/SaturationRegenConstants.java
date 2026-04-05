package com.jedtech.saturation_regen;

/** Defaults shared with Fabric config; must match spec defaults. */
public final class SaturationRegenConstants {
    /** See spec: `getFoodLevel() > regenHungerPenaltyLevel`; default aligns with vanilla sprint line. */
    public static final int DEFAULT_REGEN_HUNGER_PENALTY_LEVEL = 6;

    private SaturationRegenConstants() {}
}
