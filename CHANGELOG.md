# Changelog

All notable changes to XP_Stream will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.3.0] - 2026-01-07

### Added
- **Minecraft 26.1 Snapshot 2 support** — XP_Stream now works on Minecraft 26.1 Snapshot 2 for both Fabric and NeoForge
- Java 25 support — Updated toolchain to Java 25 (required for 26.1+)

### Changed
- **Fabric:** Migrated from Yarn to Mojang mappings (Phase 0)
- **Fabric:** Switched to unobfuscated Loom plugin (`net.fabricmc.fabric-loom`)
- **Fabric:** Updated dependency configuration from `modImplementation` to `implementation` (26.1 unobfuscated requirement)
- **Fabric:** Updated Fabric Loader to 0.18.4
- **Fabric:** Updated Fabric Loom to 1.15.0-alpha.25
- **NeoForge:** Updated to NeoForge 26.1.0.0-alpha.5+snapshot-2
- **NeoForge:** Updated ModDevGradle to 2.0.135
- **Performance:** 200 XP orbs now absorb in 3.8 seconds or less on Java 25 (verified on 26.1 Snapshot 2)

### Technical
- Updated Java toolchain from 21 → 25 in `build.gradle`
- Removed mappings configuration for Fabric (not needed for unobfuscated 26.1+ builds)
- Updated `fabric.mod.json` to use version range `">=26.1-alpha.2"` for compatibility
- Verified mixin compatibility — all targets remain accessible on 26.1
- Both Fabric and NeoForge modules verified working with identical functionality

### Fixed
- Resolved version mismatch between Gradle identifier (`26.1-snapshot-2`) and runtime version (`26.1-alpha.2`)

---

## [0.2.0] - 2025-12-28

### Added
- **NeoForge support** — XP_Stream now works on NeoForge 21.11.0-beta for Minecraft 1.21.11
- Multi-loader architecture — Separate JARs for Fabric and NeoForge with shared core logic
- Loader-specific mixins — Optimized implementation for each loader's mapping system

### Changed
- Default `maxBurstOrbs` increased from 4 to 6 for faster absorption
- Improved performance — 200 XP orbs now absorb in 2.5s (previously 4.2s with maxBurstOrbs=4)

### Technical
- Added `neoforge/` module with ModDevGradle build system
- NeoForge mixin uses Mojmap names (`ExperienceOrb`, `playerTouch`) vs Fabric's Yarn names
- Both loaders verified working with identical gameplay behavior

---

## [0.2.1] - 2026-01-03

### Changed
- **Improved JAR naming** — JARs now include Minecraft version in filename (e.g., `xp_stream-fabric-1.21.11-0.2.1.jar`)
- Better version organization in `gradle.properties`

### Technical
- Updated JAR naming to use `${project.minecraft_version}` variable instead of hardcoded version
- Cleaned up obsolete configuration files (log4j2 configs, run argument files)
- Removed unnecessary files in neoforge build directory

---

## [0.1.0] - 2025-12-XX

### Added
- **Initial release** — Fabric-only support for Minecraft 1.21.11
- Collision-based burst pickup system
- Configurable `maxBurstOrbs` setting (default: 4)
- Debug logging option
- JSON configuration file (`config/xp_stream.json`)

### Features
- Accelerates XP absorption while preserving vanilla mechanics
- No XP loss — every orb awards full value
- Mending compatibility — uses vanilla pickup path
- Server-side only — clients don't need the mod

### Performance
- 200 XP orbs: 4.2s absorption (vs 11.06s vanilla)
- No per-tick scanning — only runs on collision events
- Minimal performance impact

### Tested
- Single-player gameplay
- XP integrity verified (no loss)
- Mending compatibility (via vanilla path)

---

## [Unreleased]

### Planned
- Multiplayer edge case testing
- Performance testing under sustained load
- Additional configuration options (if needed)

---

[0.3.0]: https://github.com/Jed-Tech/XP_Stream/releases/tag/v0.3.0-26.1-snapshot-2
[0.2.1]: https://github.com/Jed-Tech/XP_Stream/releases/tag/v0.2.1-1.21.11
[0.2.0]: https://github.com/Jed-Tech/XP_Stream/releases/tag/v0.2.0
[0.1.0]: https://github.com/Jed-Tech/XP_Stream/releases/tag/v0.1.0
