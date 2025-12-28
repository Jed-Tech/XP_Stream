package com.jedtech.xp_stream.api;

/**
 * Platform shim to keep common code free of loader dependencies.
 * Fabric implements this; NeoForge can later.
 */
public interface XpStreamPlatform {
    void logDebug(String message);
}














