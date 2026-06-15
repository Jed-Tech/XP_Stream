# Changelog

All notable changes to the **XP_Stream repository** (workspace, tooling, shared docs — not individual mod releases) are documented in this file. Per-mod release notes live under `mods/<mod_id>/CHANGELOG.md`.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Changed
- Began monorepo Minecraft `26.2` release-readiness work for `xp_stream` and `saturation_regen`.
- Updated the Fabric compile pins for a release-today target: Minecraft `26.2-rc-2`, Fabric Loader `0.19.3`, Fabric Loom `1.16-SNAPSHOT`, and Fabric API `0.152.0+26.2`.
- Set the Fabric compatibility floor for both mods to `>=26.2-pre-1`, which includes `26.2 Pre-release 1` and later `26.2` builds while excluding earlier `26.2` snapshots.
- Documented that NeoForge `26.2` support remains pending official upstream `neoforge` `26.2` artifacts; no NeoForge `26.2` compile-pin change has been made yet.

### Compatibility (release target)
- **XP Stream / Fabric:** release build now targets Minecraft `26.2-rc-2` and accepts `26.2-pre-1` and later `26.2` builds
- **Saturation Regen / Fabric:** release build now targets Minecraft `26.2-rc-2` and accepts `26.2-pre-1` and later `26.2` builds
- **NeoForge:** `26.2` support intentionally deferred until official `neoforge` `26.2` artifacts exist and are smoke-tested


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
