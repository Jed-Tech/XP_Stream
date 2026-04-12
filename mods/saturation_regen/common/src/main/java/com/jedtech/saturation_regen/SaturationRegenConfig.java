package com.jedtech.saturation_regen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Runtime configuration for saturation_regen.
 * Stored as config/saturation_regen.json and loaded during loader startup.
 */
public final class SaturationRegenConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "saturation_regen.json";

    private static SaturationRegenConfig instance = defaults();

    public int regenHungerPenaltyLevel = SaturationRegenConstants.DEFAULT_REGEN_HUNGER_PENALTY_LEVEL;

    private static SaturationRegenConfig defaults() {
        SaturationRegenConfig config = new SaturationRegenConfig();
        config.regenHungerPenaltyLevel = SaturationRegenConstants.DEFAULT_REGEN_HUNGER_PENALTY_LEVEL;
        return config;
    }

    public static void load(Path configDir) {
        Path path = configDir.resolve(FILE_NAME);
        if (Files.isRegularFile(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                SaturationRegenConfig merged = defaults();
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                if (root.has("regenHungerPenaltyLevel")) {
                    merged.regenHungerPenaltyLevel = root.get("regenHungerPenaltyLevel").getAsInt();
                }
                merged.clamp();
                instance = merged;
            } catch (IOException | RuntimeException e) {
                System.err.println("[saturation_regen] Failed to read " + path + ", using defaults: " + e.getMessage());
                instance = defaults();
            }
        } else {
            instance = defaults();
            save(path);
        }
    }

    private static void save(Path path) {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(instance, writer);
            }
        } catch (IOException e) {
            System.err.println("[saturation_regen] Failed to write " + path + ": " + e.getMessage());
        }
    }

    private void clamp() {
        regenHungerPenaltyLevel = Math.max(0, Math.min(19, regenHungerPenaltyLevel));
    }

    public static SaturationRegenConfig get() {
        return instance;
    }

    public int getRegenHungerPenaltyLevel() {
        return regenHungerPenaltyLevel;
    }
}
