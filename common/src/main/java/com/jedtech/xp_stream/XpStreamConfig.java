package com.jedtech.xp_stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Runtime configuration for XP_Stream.
 * Loads from config/xp_stream.json, creates default if missing.
 */
public class XpStreamConfig {
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static XpStreamConfig INSTANCE;
    
    // Config values with defaults
    private int maxBurstOrbs = XpStreamConstants.DEFAULT_MAX_BURST_ORBS;
    private boolean debug = XpStreamConstants.DEFAULT_DEBUG;
    
    // Private constructor - use load() to get instance
    private XpStreamConfig() {}
    
    /**
     * Get the loaded config instance.
     * Must call load() first during mod initialization.
     */
    public static XpStreamConfig get() {
        if (INSTANCE == null) {
            throw new IllegalStateException("XpStreamConfig not loaded! Call load() during mod init.");
        }
        return INSTANCE;
    }
    
    /**
     * Load config from file, or create default if missing.
     * @param configDir The config directory (e.g., .minecraft/config)
     */
    public static void load(Path configDir) {
        Path configFile = configDir.resolve("xp_stream.json");
        
        if (Files.exists(configFile)) {
            try {
                String json = Files.readString(configFile);
                INSTANCE = GSON.fromJson(json, XpStreamConfig.class);
                INSTANCE.validate();
                log("Config loaded from " + configFile);
            } catch (IOException | com.google.gson.JsonSyntaxException e) {
                System.err.println("[XP_Stream] Failed to load config, using defaults: " + e.getMessage());
                INSTANCE = new XpStreamConfig();
                saveDefault(configFile);
            }
        } else {
            INSTANCE = new XpStreamConfig();
            saveDefault(configFile);
            log("Created default config at " + configFile);
        }
        
        if (INSTANCE.debug) {
            log("Config values: maxBurstOrbs=" + INSTANCE.maxBurstOrbs + ", debug=" + INSTANCE.debug);
        }
    }
    
    /**
     * Save default config to file.
     */
    private static void saveDefault(Path configFile) {
        try {
            Files.createDirectories(configFile.getParent());
            Files.writeString(configFile, GSON.toJson(new XpStreamConfig()));
        } catch (IOException e) {
            System.err.println("[XP_Stream] Failed to save default config: " + e.getMessage());
        }
    }
    
    /**
     * Validate and clamp config values to sensible ranges.
     */
    private void validate() {
        if (maxBurstOrbs < 0) {
            log("maxBurstOrbs cannot be negative, setting to 0");
            maxBurstOrbs = 0;
        }
        if (maxBurstOrbs > 64) {
            log("maxBurstOrbs capped at 64");
            maxBurstOrbs = 64;
        }
    }
    
    private static void log(String message) {
        System.out.println("[XP_Stream] " + message);
    }
    
    // =========================================================================
    // GETTERS
    // =========================================================================
    
    /**
     * Maximum orbs to burst-collect per pickup event.
     * Set to 0 to disable burst pickup.
     */
    public int getMaxBurstOrbs() {
        return maxBurstOrbs;
    }
    
    /**
     * Whether debug logging is enabled.
     */
    public boolean isDebug() {
        return debug;
    }
}

















