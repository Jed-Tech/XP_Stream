# Changelog

All notable changes to the **XP_Stream repository** (workspace, tooling, shared docs - not individual mod releases) are documented in this file. Per-mod release notes live under `mods/<mod_id>/CHANGELOG.md`.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [2026-04-19]

### Changed (1.21.1 branch - project completion)
- Completed the `monorepo/1.21.1` support project for both mods.
- `xp_stream` now has validated Fabric + NeoForge support on Minecraft `1.21.1`, including the Vanilla Pickup range behavior on this branch.
- `saturation_regen` now has validated Fabric + NeoForge support on Minecraft `1.21.1`.
- Phase planning, branch assumptions, and validation handoff docs were completed under `Docs/_Repo/Specs/1.21.1_plan/`.
- Runtime validation confirmed all four supported combinations work:
  - `xp_stream` + Fabric
  - `xp_stream` + NeoForge
  - `saturation_regen` + Fabric
  - `saturation_regen` + NeoForge
- Repo and mod docs were aligned so `main` vs `monorepo/1.21.1` branch expectations are clear.

---

## [2026-04-18]

### Changed (1.21.1 branch - Phase 1 shared workspace retarget)
- Retargeted shared pins to **Minecraft 1.21.1**: Fabric Loader **0.19.2**, `settings.gradle` uses **`fabric-loom-remap` 1.15.5** with **official Mojang mappings** in each Fabric `build.gradle`, Fabric API pin **0.116.11+1.21.1**, NeoForge **21.1.226** (ModDevGradle **2.0.141**), and **`neoforge_dependency_minimum=21.1.0`**.
- Root Java toolchain set to **21** (from 25 on `main`).
- Loader metadata and publish fallbacks updated so the branch identity is **1.21.1**, not **26.1.x**.
- Docs updated so `main` vs `monorepo/1.21.1` toolchain differences are clear.

---

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
