# Changelog

All notable changes to the **XP_Stream repository** (workspace, tooling, shared docs — not individual mod releases) are documented in this file. Per-mod release notes live under `mods/<mod_id>/CHANGELOG.md`.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Changed
- Began monorepo Minecraft `26.2` release-readiness work for `xp_stream` and `saturation_regen`.
- Moved the monorepo from the `26.2` RC toolchain to the Minecraft `26.2` GA line.
- Updated the Fabric compile pins to Minecraft `26.2`, Fabric Loader `0.19.3`, Fabric Loom `1.16.3`, and Fabric API `0.152.1+26.2`.
- Updated the NeoForge compile pin to `26.2.0.1-beta` and raised the NeoForge runtime floor to the `26.2` line.
- Set the Fabric compatibility floor for both mods to `>=26.2`.
- Simplified Loom version tracking so `settings.gradle` is now the only real Loom pin.

### Compatibility (build target)
- **XP Stream / Fabric:** Minecraft `26.2`
- **XP Stream / NeoForge:** Minecraft `26.2`, NeoForge `26.2.0.1-beta`
- **Saturation Regen / Fabric:** Minecraft `26.2`
- **Saturation Regen / NeoForge:** Minecraft `26.2`, NeoForge `26.2.0.1-beta`


## [2026-04-11]

### Changed
- Updated repo-wide development pins from the `26.1.1` line to `26.1.2`.
- Updated **NeoForge** target to `26.1.2.7-beta`.
- Updated Fabric API pin to `0.145.4+26.1.2`.

### Compatibility (tested)
- **XP Stream / Fabric:** Minecraft 26.1.2
- **XP Stream / NeoForge:** Minecraft 26.1.2, NeoForge 26.1.2.7-beta
- **Saturation Regen / Fabric:** Minecraft 26.1.2
- **Saturation Regen / NeoForge:** remains out of scope for this cycle

---

## [2026-04-04]

### Changed
- Split user-facing docs: each mod has its own `README.md` and `CHANGELOG.md` under `mods/<mod_id>/`. Root `README.md` describes the mono-repo; this file tracks repo-level changes only. **XP Stream** release history moved to `mods/xp_stream/CHANGELOG.md`.

---
