# Phase 1 Backup: Current Versions (Pre-26.1 Snapshot 2 Update)

**Date:** 2026-01-07  
**Branch:** `update/26.1-snapshot-2` (created from `main`)  
**Last Commit:** `b08969e` - "Docs: Update Phase 1 research with verified Fabric Loom version 1.14.0-alpha.32"

## Current Configuration (Minecraft 1.21.11)

### Gradle Properties (`gradle.properties`)
```
minecraft_version=1.21.11
yarn_mappings=1.21.11+build.1
fabric_loader_version=0.17.2
fabric_loom_version=1.14-SNAPSHOT
neoforge_version=21.11.0-beta
moddevgradle_version=2.0.134
```

### Java Configuration (`build.gradle`)
- **Java Toolchain:** Java 21
- **Release Target:** 21

### Fabric Module (`fabric/build.gradle`)
- **Loom Plugin ID:** `net.fabricmc.fabric-loom-remap` (obfuscated)
- **Loom Version:** `1.14-SNAPSHOT` (from pluginManagement)
- **Mappings:** Mojang official (`loom.officialMojangMappings()`)
- **Fabric Loader:** `0.17.2`

### NeoForge Module (`neoforge/build.gradle`)
- **ModDevGradle:** `2.0.134`
- **NeoForge Version:** `21.11.0-beta`
- **Mappings:** Mojang (already using)

### Settings (`settings.gradle`)
- **Loom Plugin:** `net.fabricmc.fabric-loom-remap` version `1.14-SNAPSHOT`

## Phase 0 Status
✅ **Complete** - Migrated from Yarn to Mojang mappings on 1.21.11
- Commit: `6143a2e` - "Fabric: migrate from Yarn to Mojang mappings (1.21.11)"
- Mixin verified working with Mojang names

## Target Versions for 26.1 Snapshot 2

### Verified Versions
- **Fabric Loom:** `1.14.0-alpha.32` ✅
- **ModDevGradle:** `2.0.134` ✅ (already have)
- **Fabric API:** `0.141.2+26.1` ✅

### Pending Verification
- **Fabric Loader:** `0.18.1` (baseline, may need 0.19.x+)
- **NeoForge:** `26.1.0.0-alpha.X+snapshot-2` (exact alpha number TBD)
- **Minecraft Version:** `26.1-snapshot-2`

## Notes
- Java 25 will be required for 26.1 Snapshot 2
- Fabric Loom plugin ID will switch from `fabric-loom-remap` → `fabric-loom` for unobfuscated builds
- All mappings already migrated to Mojang (Phase 0 complete)

