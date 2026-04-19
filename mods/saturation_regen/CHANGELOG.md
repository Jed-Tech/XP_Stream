# Changelog

All notable changes to **Saturation Regen** are documented in this file. Entries are listed in reverse chronological order (newest releases at the top).

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.2.2] - 2026-04-19

### Released

- **Minecraft 1.21.1 support** - Saturation Regen is now available on **Fabric** and **NeoForge** for the `1.21.1` line.
- **Saturation-driven healing on 1.21.1** - Natural regeneration can start sooner when saturation is available, without changing vanilla cadence or exhaustion behavior.

### Compatibility (validated)

- **Fabric:** Minecraft 1.21.1
- **NeoForge:** Minecraft 1.21.1, NeoForge 21.1.226

---

## [0.2.1] - 2026-04-12

### Fixed
- **NeoForge installs** on older **26.1.x** loader builds (not only the exact version we compile against) are accepted again - metadata uses a broad minimum NeoForge line, same idea as the Minecraft range.

---

## [0.2.0] - 2026-04-12

### Added
- **NeoForge** - play with Fabric or NeoForge; same optional JSON config as before.

### Changed
- **Consistent healing speed** when you're almost full: hunger 18-19 no longer drops to a slower vanilla-only step; it matches the same "saturation-driven" pace as the rest of the widened range (default settings).

### Compatibility (tested)
- **Fabric:** Minecraft 26.1.2
- **NeoForge:** Minecraft 26.1.2

---

## [0.1.1] - 2026-04-11

### Changed
- Saturation Regen works on Minecraft `26.1.2` for Fabric

### Compatibility (tested)
- **Fabric:** Minecraft 26.1.2

---

## [0.1.0] - 2026-04-04

### Added
- **Natural regen when saturation matters** - heal from food-driven regeneration when you have **saturation** and enough hunger to play, without needing to top off the whole bar first (configurable threshold).
- **Vanilla cadence** - same healing timing and exhaustion costs as vanilla; only *when* regen can start is widened to feel fair.
- **Server-side Fabric** - dedicated or integrated server; vanilla clients work without installing the mod.
- **Optional JSON config** - tune the hunger threshold (`regenHungerPenaltyLevel`); restart to apply.

---
