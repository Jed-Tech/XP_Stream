# Phase 5 Overview: Branch Cleanup

**Status:** Complete

This phase makes the `monorepo/1.21.1` branch understandable and internally consistent after the mod-specific implementation phases are complete. It focuses on cleanup, alignment, and documentation polish needed before final runtime validation and release-readiness review in Phase 6.

Project overview: [overview.md](overview.md)

## Dependencies / Preconditions

- Phase 0 is complete.
- Phase 1 is complete.
- Mod-specific implementation phases should be complete or stable enough that cleanup work will not be immediately invalidated:
  - Phase 2: `xp_stream` Fabric
  - Phase 3: `xp_stream` NeoForge
  - Phase 4a: `saturation_regen` Fabric
  - Phase 4b: `saturation_regen` NeoForge
- The branch-level assumptions in [branch_assumptions.md](branch_assumptions.md) are accepted as the working baseline.
- Phase 5 should assume runtime validation is deferred to Phase 6.

## Agent Instructions

- Keep this phase focused on branch cleanup and alignment only.
- Do not reopen mod implementation work unless a cleanup task exposes a clear documentation or consistency bug that must be corrected immediately.
- Do not treat this phase as a release phase.
- Do not treat this phase as a substitute for runtime validation; Phase 6 owns runtime verification.
- Prefer cleanup that reduces ambiguity for later validation and release planning.
- If a branch-wide inconsistency is discovered, fix the smallest coherent set of files needed to make the branch tell one consistent story.

## Specs

- The branch should clearly present itself as the `1.21.1` support line for both mods.
- Repo-level and mod-level documentation should no longer send mixed signals about:
  - target Minecraft line
  - branch purpose
  - supported loaders
  - current state of each mod on this branch
- Phase handoff docs should accurately reflect completed and in-progress work.
- Deferred mod-level documentation updates from earlier phases may be completed here if they are now clear and stable.
- Cleanup should remain scoped to consistency, clarity, and handoff quality.
- Runtime validation steps, release execution, and version finalization remain outside this phase.

## Implementation Plan

1. Review branch-level and mod-level docs for stale `26.1.x` assumptions or mixed branch messaging.
2. Align planning docs so phase status and dependencies reflect the actual state of work.
3. Align mod-level docs and changelogs where implementation phases intentionally deferred those updates.
4. Clean up small consistency issues that would otherwise confuse Phase 6 validation or later release work.
5. Leave runtime verification, release numbering, and publishing tasks for Phase 6 or later release execution.

## Related Files

- [overview.md](overview.md)
- [branch_assumptions.md](branch_assumptions.md)
- [phase_0_branch_foundation.md](phase_0_branch_foundation.md)
- [phase_1_shared_workspace_retarget.md](phase_1_shared_workspace_retarget.md)
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
- [Docs/FabricReference.md](../../../Docs/FabricReference.md)
- [Docs/neoforgeSupport.md](../../../Docs/neoforgeSupport.md)
- [Docs/publishing.md](../../../Docs/publishing.md)
- [Docs/release_checklist.md](../../../Docs/release_checklist.md)

## Validation/Acceptance

- The branch documentation tells a consistent `1.21.1` story for both mods.
- Planning docs accurately reflect actual phase state and dependencies.
- Repo-level and mod-level documentation no longer contain avoidable mixed messaging about `main` vs `monorepo/1.21.1`.
- Deferred doc cleanup from implementation phases is resolved where the correct outcome is now known.
- No runtime validation is claimed as part of this phase.
- No publishing or release execution is performed as part of this phase.

## Completion notes

- Phase handoff docs now reflect the completed state of finished phases through Phase 4b.
- Branch-level and mod-level documentation consistently point at `monorepo/1.21.1` as the `1.21.1` support line.
- Remaining work is intentionally narrowed to runtime validation and release-readiness review in Phase 6.

## Out of Scope

- Runtime gameplay validation
- Loader-specific implementation work unless a small cleanup fix is unavoidable
- Release numbering finalization
- Publishing, release execution, or platform metadata submission
- Large refactors or broad cleanup unrelated to branch consistency

## Open Questions if needed

None at this time.
