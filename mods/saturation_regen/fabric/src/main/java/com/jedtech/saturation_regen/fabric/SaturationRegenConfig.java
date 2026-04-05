package com.jedtech.saturation_regen.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jedtech.saturation_regen.SaturationRegenConstants;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Server-side JSON under {@code config/saturation_regen.json}.
 * Loaded once at mod init; restart to apply edits.
 */
public final class SaturationRegenConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("saturation_regen");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "saturation_regen.json";

    private static SaturationRegenConfig instance = defaults();

    public int regenHungerPenaltyLevel = SaturationRegenConstants.DEFAULT_REGEN_HUNGER_PENALTY_LEVEL;

    private static SaturationRegenConfig defaults() {
        SaturationRegenConfig c = new SaturationRegenConfig();
        c.regenHungerPenaltyLevel = SaturationRegenConstants.DEFAULT_REGEN_HUNGER_PENALTY_LEVEL;
        return c;
    }

    public static void loadFromDisk() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
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
                LOGGER.error("Failed to read {}, using defaults", path, e);
                instance = defaults();
            }
        } else {
            instance = defaults();
            saveToDisk();
        }
    }

    public static void saveToDisk() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(instance, writer);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to write {}", path, e);
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
