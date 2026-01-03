# Changelog

All notable changes to XP_Stream will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.2.0] - 2024-12-28

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

## [0.1.0] - 2024-12-XX

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

### Planned for v0.3
- Multiplayer edge case testing
- Explicit Mending repair verification
- Performance testing under sustained load
- Additional configuration options (if needed)

---

[0.2.0]: https://github.com/Jed-Tech/XP_Stream/releases/tag/v0.2.0
[0.1.0]: https://github.com/Jed-Tech/XP_Stream/releases/tag/v0.1.0


