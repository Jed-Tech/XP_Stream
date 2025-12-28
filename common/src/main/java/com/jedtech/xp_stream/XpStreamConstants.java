package com.jedtech.xp_stream;

/**
 * Default values for XP_Stream configuration.
 * These are used when creating a new config file or if config fails to load.
 * 
 * Runtime values are accessed via XpStreamConfig.get().
 */
public final class XpStreamConstants {
    private XpStreamConstants() {}

    /**
     * Default maximum orbs to burst-collect per pickup event.
     */
    public static final int DEFAULT_MAX_BURST_ORBS = 6;

    /**
     * Default debug logging state.
     */
    public static final boolean DEFAULT_DEBUG = false;
}
