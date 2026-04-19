# Phase 0 Overview: Branch Foundation

This phase establishes the dedicated `1.21.1` support branch as a clear working line for the multi-mod workspace. It defines the shared groundwork, confirms the scope of the branch, and prepares the repo for the mod-specific phases that follow.

Status: Complete

Project overview: [overview.md](overview.md)

## Dependencies / Preconditions

- The dedicated `1.21.1` branch has already been created from `main`.
- The overall project direction in [overview.md](overview.md) is accepted as the working plan.
- This phase does not depend on any mod-specific implementation work being complete.

## Agent Instructions

- Keep this phase focused on branch-level groundwork only.
- Do not start mod-specific feature porting in this phase.
- Prefer decisions and edits that make later phases narrower and easier to reason about.
- Update documentation when branch-level assumptions or scope boundaries become clearer.
- If a question only affects one later phase, record it there rather than expanding this phase.

## Specs

- The branch must be positioned as a dedicated `1.21.1` support line for the current monorepo workspace.
- The intended scope of the branch must be clear:
  - `xp_stream` support on `1.21.1`
  - `xp_stream` Vanilla Pickup support on `1.21.1`
  - `saturation_regen` support on `1.21.1`
  - Fabric and NeoForge support for both mods
- The branch-level sequencing strategy must be documented so later phases can follow a consistent order.
- Shared assumptions that affect all later phases must be identified early.
- The branch should be prepared for later implementation work without taking on unrelated cleanup or release activity.

## Implementation Plan

1. Confirm the branch purpose and scope in repo documentation.
2. Define the overall phase sequence for the `1.21.1` effort.
3. Identify the branch-level assumptions that later agents should treat as fixed unless a new decision is made.
4. Identify any branch-level questions that could affect multiple later phases.
5. Leave mod-specific implementation details to the dedicated phase documents.

## Related Files

- [overview.md](overview.md)
- [branch_assumptions.md](branch_assumptions.md)
- [README.md](../../../README.md)
- [justfile](../../../justfile)
- [build.gradle](../../../build.gradle)
- [settings.gradle](../../../settings.gradle)
- [gradle.properties](../../../gradle.properties)

## Validation/Acceptance

- The `1.21.1` branch plan is documented clearly enough that later phases can be executed independently.
- The branch scope is stated clearly and does not imply unrelated feature work.
- The phase sequence is documented and consistent with the project overview.
- Branch-level assumptions are captured without drifting into mod-specific implementation detail.
- No release or publishing work is introduced as part of this phase.

## Out of Scope

- Porting `xp_stream` implementation to `1.21.1`
- Porting `saturation_regen` implementation to `1.21.1`
- Loader-specific implementation details
- Detailed build retargeting tasks
- Publishing, version bumps, or release execution
- Broad repo cleanup not required for the `1.21.1` effort

## Open Questions if needed

None at this time.
