# XP_Stream — Multi-Loader Layout (common/ + fabric/) Skeleton (Fabric 1.21.11 first)

This adapts the repo skeleton to your existing `common/` and `fabric/` folders, so you can add NeoForge later with minimal churn.

---

## Folder / File Tree

XP_Stream/
├─ settings.gradle
├─ build.gradle
├─ gradle.properties
├─ README.md
├─ LICENSE
├─ common/
│  ├─ build.gradle
│  └─ src/
│     └─ main/
│        ├─ java/
│        │  └─ com/
│        │     └─ tomorrow_skies/
│        │        └─ xp_stream/
│        │           ├─ XpStreamConstants.java
│        │           └─ api/
│        │              └─ XpStreamPlatform.java
│        └─ resources/
│           └─ (optional shared resources)
└─ fabric/
   ├─ build.gradle
   └─ src/
      └─ main/
         ├─ java/
         │  └─ com/
         │     └─ tomorrow_skies/
         │        └─ xp_stream/
         │           ├─ fabric/
         │           │  └─ XpStreamFabricMod.java
         │           └─ mixin/
         │              └─ ExperienceOrbEntityMixin.java
         └─ resources/
            ├─ fabric.mod.json
            ├─ xp_stream.fabric.mixins.json
            └─ assets/
               └─ xp_stream/
                  └─ icon.png

---

## settings.gradle

pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = "https://maven.fabricmc.net/"
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "XP_Stream"
include("common", "fabric")

---

## Root gradle.properties

# Versions (confirmed working for 1.21.11)
minecraft_version=1.21.11
yarn_mappings=1.21.11+build.1
fabric_loader_version=0.17.2
fabric_loom_version=1.14-SNAPSHOT

# Mod metadata
mod_id=xp_stream
mod_name=XP_Stream
mod_version=0.1.0
maven_group=com.tomorrow_skies
archives_base_name=xp_stream

org.gradle.jvmargs=-Xmx2G

# NOTE: Loom 1.14 requires Gradle 9.2.1+

---

## Root build.gradle

plugins {
    id 'maven-publish'
}

allprojects {
    group = project.maven_group
    version = project.mod_version

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply plugin: 'java'

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
        withSourcesJar()
    }

    tasks.withType(JavaCompile).configureEach {
        options.release = 21
    }
}

---

## common/build.gradle

plugins {
    id 'java'
}

dependencies {
    // Keep common loader-agnostic.
    // Do NOT depend on Fabric API or Fabric Loader here.
    // (Optionally, you may depend on Minecraft mappings interfaces later,
    //  but try to keep this module "pure logic + interfaces".)
}

---

## common/src/main/java/.../XpStreamConstants.java

package com.tomorrow_skies.xp_stream;

public final class XpStreamConstants {
    private XpStreamConstants() {}

    // v0.1: hardcoded, no config yet
    public static final double EXTRA_PICKUP_RADIUS_BLOCKS = 1.25;
    public static final int MAX_EXTRA_ORBS_PER_EVENT = 32;

    // Debug logging toggle (keep off by default)
    public static final boolean DEBUG = false;
}

---

## common/src/main/java/.../api/XpStreamPlatform.java

package com.tomorrow_skies.xp_stream.api;

/**
 * Platform shim to keep common code free of loader dependencies.
 * Fabric implements this; NeoForge can later.
 */
public interface XpStreamPlatform {
    void logDebug(String message);
}

---

## fabric/build.gradle (Fabric Loom module)

plugins {
    id 'fabric-loom' version "${fabric_loom_version}"
    id 'maven-publish'
}

base {
    archivesName = project.archives_base_name + "-fabric"
}

dependencies {
    minecraft "com.mojang:minecraft:${project.minecraft_version}"
    mappings "net.fabricmc:yarn:${project.yarn_mappings}:v2"
    modImplementation "net.fabricmc:fabric-loader:${project.fabric_loader_version}"

    implementation project(":common")

    // Fabric API optional:
    // modImplementation "net.fabricmc.fabric-api:fabric-api:<MATCHING_VERSION>"
}

loom {
    splitEnvironmentSourceSets()
}

processResources {
    inputs.property "version", project.version
    filesMatching("fabric.mod.json") {
        expand "version": project.version,
               "mod_id": project.mod_id,
               "mod_name": project.mod_name
    }
}

publishing {
    publications {
        mavenJava(MavenPublication) {
            from components.java
        }
    }
}

---

## fabric/src/main/resources/fabric.mod.json

{
  "schemaVersion": 1,
  "id": "${mod_id}",
  "version": "${version}",
  "name": "${mod_name}",
  "description": "Server-side XP pickup smoothing that keeps the vanilla orb feel.",
  "authors": ["Jeff E"],
  "license": "All-Rights-Reserved",
  "environment": "*",
  "entrypoints": {
    "main": [
      "com.tomorrow_skies.xp_stream.fabric.XpStreamFabricMod"
    ]
  },
  "mixins": [
    "xp_stream.fabric.mixins.json"
  ],
  "depends": {
    "fabricloader": ">=0.16.0",
    "minecraft": "1.21.11"
  }
}

---

## fabric/src/main/resources/xp_stream.fabric.mixins.json

{
  "required": true,
  "package": "com.tomorrow_skies.xp_stream.mixin",
  "compatibilityLevel": "JAVA_21",
  "mixins": [
    "ExperienceOrbEntityMixin"
  ],
  "injectors": {
    "defaultRequire": 1
  }
}

---

## fabric/src/main/java/.../fabric/XpStreamFabricMod.java

package com.tomorrow_skies.xp_stream.fabric;

import net.fabricmc.api.ModInitializer;

public final class XpStreamFabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Intentionally minimal for v0.1.
        // Behavior lives in mixins.
    }
}

---

## fabric/src/main/java/.../mixin/ExperienceOrbEntityMixin.java

package com.tomorrow_skies.xp_stream.mixin;

import com.tomorrow_skies.xp_stream.XpStreamConstants;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * v0.1 plan (Approach B):
 * - Hook orb pickup at the collision moment.
 * - After vanilla pickup runs for the triggering orb, also pick up nearby orbs in a tight radius, capped.
 *
 * CONFIRMED for Yarn 1.21.11+build.1:
 * - Method: onPlayerCollision(PlayerEntity) ✓
 * - World access: getEntityWorld() (not getWorld())
 */
@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin {

    // Simple re-entrancy guard
    private static boolean xp_stream$inBurst = false;

    @Inject(method = "onPlayerCollision", at = @At("TAIL"))
    private void xp_stream$burstPickup(PlayerEntity player, CallbackInfo ci) {
        if (xp_stream$inBurst) return;
        if (!(player.getEntityWorld() instanceof ServerWorld serverWorld)) return;

        try {
            xp_stream$inBurst = true;

            Box box = player.getBoundingBox().expand(XpStreamConstants.EXTRA_PICKUP_RADIUS_BLOCKS);

            // IMPLEMENTATION NOTE:
            // - Query nearby ExperienceOrbEntity in 'box'
            // - For up to MAX_EXTRA_ORBS_PER_EVENT:
            //   - invoke the vanilla orb pickup path for each orb (so Mending works)
            //   - discard the orb
            // - Avoid sound spam (ideally rely on the vanilla sound from the initial orb pickup)

        } finally {
            xp_stream$inBurst = false;
        }
    }
}

---

## Notes for future NeoForge support

When you add NeoForge, you’ll mirror `fabric/` with `neoforge/`:
- `neoforge/build.gradle`
- `neoforge/src/main/resources/META-INF/mods.toml`
- `neoforge` entrypoint
- Either:
  - NeoForge pickup event hook, OR
  - mixin to the same vanilla method (whichever is more stable for 1.21.x)

The key is: keep the algorithm and constants in `common/` so only the hook layer changes.

---


---

## Icon
Place a 128x128 PNG at:
src/main/resources/assets/xp_stream/icon.png

---

## Minimal additional files you’ll want soon (optional but recommended)
- CHANGELOG.md
- BETARELEASEPLAN.md (you already have content)
- ROADMAP.md (optional)

---

## Confirmed Yarn Mappings (1.21.11+build.1)

✅ **XP pickup method**: `ExperienceOrbEntity#onPlayerCollision(PlayerEntity player)`
✅ **World access**: Use `entity.getEntityWorld()` (not `getWorld()`)
✅ **Vanilla XP award**: Inside `onPlayerCollision`, the orb calls:
   - `player.addExperience(amount)` for leftover XP after Mending
   - Mending is handled via `repairPlayerGears(ServerPlayerEntity, int)` which uses `EnchantmentHelper.chooseEquipmentWith(EnchantmentEffectComponentTypes.REPAIR_WITH_XP, ...)`

## Next Implementation Steps
1) Query nearby `ExperienceOrbEntity` using `serverWorld.getEntitiesByType(TypeFilter.instanceOf(ExperienceOrbEntity.class), box, predicate)`
2) For each extra orb, call `orb.onPlayerCollision(player)` to trigger vanilla pickup (Mending + XP award)
3) Cap at `MAX_EXTRA_ORBS_PER_EVENT` to avoid lag
4) Sound spam mitigation: vanilla only plays one sound per pickup event, so calling `onPlayerCollision` should be fine
