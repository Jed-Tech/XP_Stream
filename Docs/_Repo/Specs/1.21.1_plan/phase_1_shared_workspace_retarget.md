# Phase 1 Overview: Shared Workspace Retarget

This phase prepares the shared monorepo workspace for the dedicated `1.21.1` branch. It establishes the common branch-level build, version, and workspace assumptions needed before mod-specific Fabric or NeoForge porting begins.

Status: Complete

Project overview: [overview.md](overview.md)

## Dependencies / Preconditions

- Phase 0 is complete.
- The branch-level assumptions in [branch_assumptions.md](branch_assumptions.md) are accepted as the working baseline.
- No mod-specific implementation work from later phases should be required to complete this phase.

## Agent Instructions

- Keep this phase branch-level and shared-workspace focused.
- Do not implement `xp_stream` or `saturation_regen` feature behavior in this phase.
- Make the minimum shared changes needed so later mod phases can work against a real `1.21.1` branch target.
- Prefer decisions that reduce later duplication across mods and loaders.
- If a required change only affects one later mod/loader phase, leave it for that phase.
- If this phase uncovers a branch-wide decision, document it in [branch_assumptions.md](branch_assumptions.md) and keep [overview.md](overview.md) aligned.

## Specs

- The branch workspace must be clearly retargeted toward `1.21.1` at the shared repo level.
- Shared versioning, build, and workspace configuration must reflect the `1.21.1` line rather than the current `26.1.x` line.
- The workspace should be left in a state where later mod-specific phases can focus on behavior and loader parity rather than branch identity.
- The monorepo structure must remain intact.
- Phase 1 should handle shared branch-level retargeting only:
  - shared version pins
  - shared build assumptions
  - shared workspace-level documentation
  - shared loader/toolchain direction where it affects the branch as a whole
- Phase 1 should not absorb mod-specific code translation or gameplay behavior work.
- Differences between the modern unobfuscated branch model and the older `1.21.1` line should be recognized early so later phases are not built on the wrong assumptions.

## Implementation Plan

1. Review the current shared repo configuration and identify which branch-level files still point at the `26.1.x` line.
2. Retarget shared workspace configuration so the branch clearly represents the `1.21.1` line.
3. Update shared documentation that would otherwise mislead later agents about the branch target.
4. Confirm which shared build assumptions are now fixed for later phases.
5. Leave loader-specific implementation and mod behavior work to the later phase docs.

## Related Files

- [overview.md](overview.md)
- [branch_assumptions.md](branch_assumptions.md)
- [README.md](../../../README.md)
- [build.gradle](../../../build.gradle)
- [settings.gradle](../../../settings.gradle)
- [gradle.properties](../../../gradle.properties)
- [justfile](../../../justfile)
- [Docs/justfile.md](../../../Docs/justfile.md)
- [Docs/FabricReference.md](../../../Docs/FabricReference.md)
- [Docs/neoforgeSupport.md](../../../Docs/neoforgeSupport.md)

## Validation/Acceptance

- The shared branch-level workspace is clearly aimed at `1.21.1`.
- Later agents can begin mod-specific work without first redefining the branch target.
- Shared repo documentation no longer implies that this branch is still primarily a `26.1.x` line.
- The monorepo structure is preserved.
- No mod-specific feature work is performed as part of this phase.
- Any new branch-wide assumptions discovered during the retarget are documented.

## Phase 1 Result

- Shared branch target is now the `1.21.1` line.
- Shared root pins were moved to `1.21.1`-appropriate values, including:
  - `minecraft_version=1.21.1`
  - `minecraft_version_range=[1.21.1,)`
  - Java 21 for subprojects
  - Fabric remap path with official Mojang mappings
  - NeoForge `21.1.226`
  - `neoforge_dependency_minimum=21.1.0`
- Fabric-side shared build assumptions now follow the mapped/remap `1.21.x` path instead of the unobfuscated `26.1.x` path used on `main`.
- NeoForge metadata now derives its Minecraft range from shared branch properties rather than carrying `26.1`-specific literals.
- Shared documentation was updated so `main` vs `monorepo/1.21.1` is clear.
- `.\gradlew.bat build` completes successfully for the workspace on this branch.

## Out of Scope

- `xp_stream` Fabric implementation work
- `xp_stream` NeoForge implementation work
- `saturation_regen` Fabric implementation work
- `saturation_regen` NeoForge implementation work
- Mod-specific gameplay validation
- Publishing, release execution, or release metadata work
- Branch cleanup beyond what is needed to establish the shared `1.21.1` workspace target

## Open Questions if needed

None at this time.
