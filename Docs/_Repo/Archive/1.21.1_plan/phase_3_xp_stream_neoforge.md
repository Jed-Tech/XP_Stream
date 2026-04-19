# Phase 3 Overview: XP Stream NeoForge

**Status:** Complete

This phase brings `xp_stream` to `1.21.1` on NeoForge and completes loader parity for the mod on this branch. It should mirror the intended `xp_stream` behavior already established in Phase 2, including the Vanilla Pickup behavior, while remaining specific to the NeoForge environment.

Project overview: [overview.md](overview.md)

## Completion notes

- **`xp_stream` NeoForge on `1.21.1` matches the Phase 2 Fabric parity target:** vanilla on-foot pickup box (`inflate(1.0, 0.5, 1.0)`), burst throughput and config semantics, and debug strings including `Burst pickup: X orbs (of Y in range).` and the message when `maxBurstOrbs` is `0` with debug enabled.
- **Build:** `:mods:xp_stream:neoforge:build` succeeds on the shared Phase 1 toolchain (NeoForge `21.1.226`, Java 21).
- **Mod docs:** `mods/xp_stream/README.md` includes a short branch/version pointer; `mods/xp_stream/CHANGELOG.md` has an `[Unreleased]` entry for this branch (no version bump — release work stays out of phase scope).
- **Scope:** No `saturation_regen` changes; branch-wide cleanup and publishing remain for later phases.

## Dependencies / Preconditions

- Phase 0 is complete.
- Phase 1 is complete.
- Phase 2 is complete (Fabric parity on `1.21.1`). **Phase 3 is complete** (NeoForge parity with Phase 2; see **Completion notes** above).
- The branch-level assumptions in [branch_assumptions.md](branch_assumptions.md) are accepted as the working baseline.
- Phase 3 should assume the shared branch-level toolchain established by Phase 1:
  - Minecraft `1.21.1`
  - Java 21
  - NeoForge `21.1.226`
  - `minecraft_version_range=[1.21.1,)`

## Agent Instructions

- Keep this phase focused on `xp_stream` on NeoForge only.
- Treat Fabric behavior from Phase 2 as the parity target unless a branch-wide issue requires a new decision.
- Do not broaden this phase into `saturation_regen` work or branch-wide cleanup.
- Port the intended behavior deliberately into the `1.21.1` NeoForge environment rather than assuming direct carryover from modern code.
- Preserve the established identity of the mod: faster pickup, vanilla feel, no XP loss, and use of vanilla pickup flow.
- If this phase uncovers a branch-wide problem, document it in [branch_assumptions.md](branch_assumptions.md) and keep later docs aligned.
- If `xp_stream` mod-level changelog or README updates were deferred in Phase 2, they may be completed here once loader parity is established for the mod.

## Specs

- `xp_stream` must work on `1.21.1` NeoForge on this branch.
- The NeoForge implementation must preserve the mod's core behavior and identity.
- The NeoForge implementation must match the intended Phase 2 behavior closely enough to establish loader parity for `xp_stream`.
- The NeoForge implementation must include the newer Vanilla Pickup behavior:
  - use vanilla on-foot pickup range as the candidate area for burst pickup
  - keep burst throughput behavior
  - keep existing config intent for burst count
  - keep the debug output aligned with the newer "in range" wording
- The result should be a deliberate `1.21.1` NeoForge port, not a rollback to older collision-only burst behavior.
- Phase 3 may include the `xp_stream` documentation updates needed to keep the NeoForge-side branch story accurate, as long as they directly support this phase.
- Phase 3 should leave final branch cleanup and release-readiness work to later phases.
- Phase 3 should assume shared build retargeting is already solved and should not revisit Phase 1 decisions unless a branch-wide blocker is discovered.

## Implementation Plan

1. Review the intended `xp_stream` behavior as established by the modern branch and the completed Phase 2 Fabric work.
2. Review the older `1.21.1` NeoForge implementation shape and translate the needed behavior into that environment.
3. Port `xp_stream` NeoForge behavior to `1.21.1`, including the Vanilla Pickup burst-range behavior.
4. Confirm the NeoForge implementation matches the intended Fabric behavior closely enough for loader parity.
5. Update `xp_stream` mod-level documentation if needed once loader parity is established for the mod.
6. Leave branch cleanup, broader documentation alignment, and final release-readiness work to later phases.

## Related Files

- [overview.md](overview.md)
- [branch_assumptions.md](branch_assumptions.md)
- [phase_2_xp_stream_fabric.md](phase_2_xp_stream_fabric.md)
- [VanillaPickup_plan.md](../../../Docs/xp_stream/Archive/VanillaPickup_plan.md)
- [mods/xp_stream/README.md](../../../mods/xp_stream/README.md)
- [mods/xp_stream/CHANGELOG.md](../../../mods/xp_stream/CHANGELOG.md)
- [mods/xp_stream/neoforge/build.gradle](../../../mods/xp_stream/neoforge/build.gradle)
- [mods/xp_stream/neoforge/src/main/java/com/jedtech/xp_stream/neoforge/XpStreamNeoForgeMod.java](../../../mods/xp_stream/neoforge/src/main/java/com/jedtech/xp_stream/neoforge/XpStreamNeoForgeMod.java)
- [mods/xp_stream/neoforge/src/main/java/com/jedtech/xp_stream/mixin/ExperienceOrbMixin.java](../../../mods/xp_stream/neoforge/src/main/java/com/jedtech/xp_stream/mixin/ExperienceOrbMixin.java)
- [mods/xp_stream/neoforge/src/main/resources/META-INF/neoforge.mods.toml](../../../mods/xp_stream/neoforge/src/main/resources/META-INF/neoforge.mods.toml)
- [mods/xp_stream/neoforge/src/main/resources/xp_stream.neoforge.mixins.json](../../../mods/xp_stream/neoforge/src/main/resources/xp_stream.neoforge.mixins.json)
- [mods/xp_stream/common/src/main/java/com/jedtech/xp_stream/XpStreamConfig.java](../../../mods/xp_stream/common/src/main/java/com/jedtech/xp_stream/XpStreamConfig.java)
- [mods/xp_stream/common/src/main/java/com/jedtech/xp_stream/XpStreamConstants.java](../../../mods/xp_stream/common/src/main/java/com/jedtech/xp_stream/XpStreamConstants.java)

## Validation/Acceptance

- `xp_stream` builds on the `1.21.1` NeoForge branch target.
- The NeoForge implementation reflects the intended modern `xp_stream` behavior rather than the older collision-only burst behavior.
- Vanilla Pickup behavior is present in the NeoForge implementation.
- NeoForge behavior is aligned with the intended Fabric behavior closely enough to establish loader parity.
- Config behavior remains coherent for the NeoForge implementation.
- Debug wording reflects the newer "in range" terminology where applicable.
- The phase does not absorb `saturation_regen` work or final branch cleanup.

## Out of Scope

- `xp_stream` Fabric implementation except as a parity reference
- `saturation_regen` implementation on either loader
- Final cross-branch cleanup beyond direct `xp_stream` NeoForge needs
- Publishing, release execution, or release metadata work
- Broader repo cleanup not required for `xp_stream` NeoForge on `1.21.1`

## Open Questions if needed

None at this time.
