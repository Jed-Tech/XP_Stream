# Phase 1 Verified Versions for 26.1 Snapshot 2

**Date:** 2026-01-07  
**Status:** ✅ All versions verified and ready for Phase 2

## Verified Versions

### Minecraft
- **Version:** `26.1-snapshot-2`

### Fabric Toolchain
- **Fabric Loader:** `0.18.4` (for 26.1 snapshot-2)
- **Fabric Loom:** `1.15.0-alpha.25` (for 26.1 unobfuscated)
  - **Note:** Phase 0 used `1.14.0-alpha.32` for Yarn → Mojang migration
  - **Plugin ID:** Switch from `net.fabricmc.fabric-loom-remap` → `net.fabricmc.fabric-loom` in Phase 2
- **Fabric API:** `0.141.2+26.1`

### NeoForge Toolchain
- **NeoForge:** `26.1.0.0-alpha.5+snapshot-2`
- **ModDevGradle:** `2.0.134` (current, minimum for snapshots)
  - **Optional:** Bump to `2.0.135` (released Dec 31, 2025) - safe but not required

### Java
- **Version:** Java 25 (JDK 25.0.1)
- **Installation Path:** `C:\Program Files\Eclipse Adoptium\jdk-25.0.1.8-hotspot`
- **Status:** ✅ Installed, JAVA_HOME configured, Gradle verified

## Sources
- NeoForge: Official NeoForged project page + versions index (mods.upcraft.dev)
- Fabric: Versions index (mods.upcraft.dev) + official Maven repositories
- ModDevGradle: Gradle Plugin Portal (plugins.gradle.org)

## Phase 2 Action Items
These versions will be pinned in `gradle.properties` during Phase 2:
- `minecraft_version=26.1-snapshot-2`
- `fabric_loader_version=0.18.4`
- `fabric_loom_version=1.15.0-alpha.25`
- `neoforge_version=26.1.0.0-alpha.5+snapshot-2`
- `moddevgradle_version=2.0.134` (or `2.0.135` if bumping)
