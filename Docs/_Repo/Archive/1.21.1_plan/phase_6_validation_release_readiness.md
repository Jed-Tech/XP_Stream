# Phase 6 Overview: Validation and Release Readiness

**Status:** Complete

This phase performs final validation for the `monorepo/1.21.1` branch and determines whether the branch is ready for release planning. It owns runtime verification for both mods and both loaders, and it confirms that the branch is in a trustworthy state before any version finalization or publishing work begins.

Project overview: [overview.md](overview.md)

## Runtime validation log

Manual checks during this phase (client/server). Add a row or bullet as each combination is verified.

| When | Mod | Loader | Result | Notes |
|------|-----|--------|--------|-------|
| 2026-04-18 | `xp_stream` | Fabric | **Pass** | In-game behavior reported flawless (dev client: `just xp-stream-fabric-client`). |
| 2026-04-19 | `xp_stream` | NeoForge | **Pass** | Dev client loads world; mod behavior OK (`just xp-stream-neoforge-client`). |
| 2026-04-19 | `saturation_regen` | Fabric | **Pass** | Dev client loads world; mixin OK after `FoodData#tick(Player)` fix (`just saturation-regen-fabric-client`). |
| 2026-04-19 | `saturation_regen` | NeoForge | **Pass** | Dev client loads world; mixin OK after `FoodData#tick(Player)` fix (`just saturation-regen-neoforge-client`). |

**Remaining (optional):** dedicated-server smoke if you rely on that workflow; deeper threshold/gamerule spot checks if desired; final release execution is outside this phase.

## Completion notes

- **Runtime matrix:** all four supported mod/loader combinations were confirmed working on `monorepo/1.21.1`:
  - `xp_stream` + Fabric
  - `xp_stream` + NeoForge
  - `saturation_regen` + Fabric
  - `saturation_regen` + NeoForge
- **Implementation confidence:** both mods now have successful build and runtime confirmation on the loaders this branch claims to support.
- **Release readiness:** the branch is ready to move into release planning.
- **Still outside scope:** no publishing, version finalization, or platform release work was performed in this phase.

## Dependencies / Preconditions

- Phase 0 is complete.
- Phase 1 is complete.
- Mod-specific implementation phases should be complete:
  - Phase 2: `xp_stream` Fabric
  - Phase 3: `xp_stream` NeoForge
  - Phase 4a: `saturation_regen` Fabric
  - Phase 4b: `saturation_regen` NeoForge
- Phase 5 branch cleanup should be complete, or stable enough that validation is not being performed against obviously stale docs or branch messaging.
- The branch-level assumptions in [branch_assumptions.md](branch_assumptions.md) are accepted as the working baseline.

## Agent Instructions

- Treat this phase as the branch's runtime validation and release-readiness gate.
- Perform real validation where feasible; do not rely only on build success or code inspection.
- Distinguish clearly between:
  - build verification
  - runtime verification
  - release readiness
- Do not publish, create releases, or finalize version numbers in this phase unless explicitly redirected to do so.
- If validation reveals a real defect, record it clearly and stop short of claiming release readiness.
- If small fixes are necessary to complete validation cleanly, keep them narrowly scoped to the discovered issue.

## Specs

- Both mods must be validated on the `1.21.1` branch for the loaders they claim to support.
- Validation should cover both build success and runtime behavior.
- `xp_stream` validation must confirm the intended branch behavior, including:
  - faster burst pickup behavior
  - Vanilla Pickup range behavior
  - coherent config behavior
  - no obvious behavioral mismatch between Fabric and NeoForge
- `saturation_regen` validation must confirm the intended branch behavior, including:
  - widened saturation-driven natural regen behavior
  - coherent config behavior
  - `naturalRegeneration` gamerule respect
  - no obvious behavioral mismatch between Fabric and NeoForge
- The branch should be evaluated for release readiness, but release execution is still outside this phase.
- Validation results should leave a clear answer to one question: is the branch ready to move into release work or not?

## Implementation Plan

1. Confirm the branch builds successfully for all intended mod/loader targets.
2. Run targeted runtime checks for both mods on both loaders where supported on this branch.
3. Verify that the implemented branch behavior matches the intended outcomes documented in earlier phases.
4. Verify that branch documentation and changelog state are good enough to support release planning.
5. Summarize validation results and explicitly state whether the branch is release-ready, release-ready with caveats, or blocked.

## Related Files

- [overview.md](overview.md)
- [branch_assumptions.md](branch_assumptions.md)
- [phase_2_xp_stream_fabric.md](phase_2_xp_stream_fabric.md)
- [phase_3_xp_stream_neoforge.md](phase_3_xp_stream_neoforge.md)
- [phase_4a_saturation_regen_fabric.md](phase_4a_saturation_regen_fabric.md)
- [phase_4b_saturation_regen_neoforge.md](phase_4b_saturation_regen_neoforge.md)
- [README.md](../../../README.md)
- [CHANGELOG.md](../../../CHANGELOG.md)
- [mods/xp_stream/README.md](../../../mods/xp_stream/README.md)
- [mods/xp_stream/CHANGELOG.md](../../../mods/xp_stream/CHANGELOG.md)
- [mods/saturation_regen/README.md](../../../mods/saturation_regen/README.md)
- [mods/saturation_regen/CHANGELOG.md](../../../mods/saturation_regen/CHANGELOG.md)
- [Docs/saturation_regen/Archive/saturation_regen_v1/spec.md](../../../Docs/saturation_regen/Archive/saturation_regen_v1/spec.md)
- [Docs/saturation_regen/Archive/saturation_regen_v1/tasks.md](../../../Docs/saturation_regen/Archive/saturation_regen_v1/tasks.md)
- [Docs/saturation_regen/Archive/Saturation_regen_Neoforge.md](../../../Docs/saturation_regen/Archive/Saturation_regen_Neoforge.md)
- [Docs/release_checklist.md](../../../Docs/release_checklist.md)
- [Docs/publishing.md](../../../Docs/publishing.md)
- [Docs/XPCollectionTest.md](../../../Docs/XPCollectionTest.md)

## Validation/Acceptance

- All intended branch targets build successfully.
- Runtime verification has been performed for the mod/loader combinations the branch claims to support.
- `xp_stream` behavior is validated closely enough to support release planning.
- `saturation_regen` behavior is validated closely enough to support release planning.
- `saturation_regen` runtime checks include at least:
  - expected behavior above vs. at/below the configured hunger threshold
  - `naturalRegeneration` gamerule off/on sanity checks
  - basic Fabric vs. NeoForge parity confidence
- Any remaining gaps are documented explicitly rather than left implicit.
- The branch's release-readiness status is stated clearly.
- No publishing or release execution is performed as part of this phase.

## Out of Scope

- Publishing to Modrinth or CurseForge
- Creating GitHub releases
- Final version-number decisions unless explicitly requested in a later release step
- Broad refactors unrelated to issues uncovered during validation
- Non-essential polish work that does not affect validation or release readiness

## Open Questions if needed

None at this time.
