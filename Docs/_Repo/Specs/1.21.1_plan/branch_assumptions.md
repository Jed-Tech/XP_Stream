# 1.21.1 Branch Assumptions

This document captures the branch-level decisions and assumptions established during Phase 0. Later phase agents should treat these as fixed unless a new explicit decision replaces them.

Project overview: [overview.md](overview.md)
Phase 0 handoff: [phase_0_branch_foundation.md](phase_0_branch_foundation.md)

## Branch Identity

- This branch is a dedicated `1.21.1` support line.
- It is based on the current monorepo structure from `main`, not on the older `maintenance/1.21.1` branch layout.
- The branch is intended to support both current mods in one workspace:
  - `xp_stream`
  - `saturation_regen`

## Scope Boundaries

- The branch exists to bring both mods to `1.21.1`.
- `xp_stream` includes the Vanilla Pickup behavior as part of this support effort.
- Both Fabric and NeoForge are in scope for both mods.
- This branch effort is a focused support project, not a general repo refresh.

## Delivery Shape

- Work proceeds in phased handoffs rather than one large branch-wide change.
- Shared branch-level work comes before mod-specific implementation work.
- Mod-specific work proceeds one mod at a time, with loader parity completed before shifting focus to the next mod.

## Branch-Level Constraints

- Do not expand the scope to unrelated feature work.
- Do not treat this branch as a vehicle for broad cleanup unless the cleanup is needed for the `1.21.1` goal.
- Do not treat publishing or release execution as part of Phase 0.
- Do not force later phases to preserve the older single-mod `1.21.1` branch shape.

## Documentation Assumptions

- The planning source of truth for this branch lives under `Docs/_Repo/Specs/1.21.1_plan/`.
- Phase docs are handoff specs for other agents and should stay focused, not exhaustive.
- Repo-level docs may be updated when they help clarify the purpose of the branch, but detailed implementation planning should stay in the phase documents.

## Sequencing Reference

- Phase 0: Branch Foundation
- Phase 1: Shared Workspace Retarget
- Phase 2: XP Stream Fabric
- Phase 3: XP Stream NeoForge
- Phase 4a: Saturation Regen Fabric
- Phase 4b: Saturation Regen NeoForge
- Phase 5: Branch Cleanup
- Phase 6: Validation and Release Readiness

## Toolchain (Phase 1 - shared workspace retarget)

- **Minecraft:** `1.21.1` (root `minecraft_version`).
- **Java:** **21** for Gradle `subprojects` (not Java 25 used on `main` for 26.1.x).
- **Fabric:** `net.fabricmc.fabric-loom-remap` with **official Mojang mappings** (obfuscated game jars); not the unobfuscated `net.fabricmc.fabric-loom` path used on `main`.
- **NeoForge:** `21.1.226`; **`neoforge_dependency_minimum`** is a broad **21.1.0** floor for `neoforge.mods.toml`, not the compile pin.
- **`minecraft_version_range`** in `gradle.properties` drives the NeoForge **`minecraft`** dependency range in both mods' `neoforge.mods.toml` (currently `[1.21.1,)`).

## Notes for Later Phases

- If a question only affects one phase, record it in that phase doc instead of broadening branch-level assumptions.
- If a later phase uncovers a branch-wide issue, update this document and the overview together so future agents inherit the same decision.
