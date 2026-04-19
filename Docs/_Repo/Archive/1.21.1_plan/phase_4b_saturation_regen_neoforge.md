# Phase 4b Overview: Saturation Regen NeoForge

**Status:** Complete

**Note:** **Phase 3 is complete** (`xp_stream` NeoForge on `1.21.1`). **Phase 4a is complete** (`saturation_regen` Fabric on `1.21.1`). This phase is NeoForge parity for `saturation_regen`.

This phase brings `saturation_regen` to `1.21.1` on NeoForge and completes loader parity for the mod on this branch. It should mirror the intended `saturation_regen` behavior already established in Phase 4a while remaining specific to the NeoForge environment.

Project overview: [overview.md](overview.md)

## Completion notes

- **Parity:** `neoforge/.../FoodDataMixin` matches Phase 4a Fabric: same `@ModifyConstant` on `FoodData#tick`, same threshold rules, and **`player.level().isClientSide()` → return `original`** so the mixin only affects the logical server.
- **Build:** `:mods:saturation_regen:neoforge:build` succeeds on Phase 1 pins (NeoForge `21.1.226`, Java 21).
- **Docs:** `CHANGELOG.md` `[Unreleased]` and README branch line updated for full Fabric + NeoForge on `monorepo/1.21.1`.
- **Scope:** No `xp_stream` changes; branch-wide cleanup remains for later phases.

## Dependencies / Preconditions

- Phase 0 is complete.
- Phase 1 is complete.
- **Phase 3 is complete** (`xp_stream` NeoForge); both `xp_stream` loaders are done on this branch.
- **Phase 4a is complete** (`saturation_regen` Fabric on `1.21.1`) — use its mixin + config behavior as the parity target.
- The branch-level assumptions in [branch_assumptions.md](branch_assumptions.md) are accepted as the working baseline.
- Phase 4b should assume the shared branch-level toolchain established by Phase 1:
  - Minecraft `1.21.1`
  - Java 21
  - NeoForge `21.1.226`
  - `minecraft_version_range=[1.21.1,)`

## Agent Instructions

- Keep this phase focused on `saturation_regen` on NeoForge only.
- Treat Fabric behavior from Phase 4a as the parity target unless a branch-wide issue requires a new decision.
- Do not broaden this phase into `xp_stream` work or branch-wide cleanup.
- Port the intended behavior deliberately into the `1.21.1` NeoForge environment rather than assuming direct carryover from modern code.
- Preserve the mod's identity as a targeted vanilla-plus change to the existing natural regeneration path.
- Do not introduce a new healing system, separate healing loop, or custom cadence.
- Phase 4b should assume shared build retargeting is already solved and should not revisit Phase 1 decisions unless a branch-wide blocker is discovered.
- Evaluate whether NeoForge should mirror Fabric's explicit logical-server guard in `FoodDataMixin` so loader parity is clear and intentional.
- If this phase uncovers a branch-wide problem, document it in [branch_assumptions.md](branch_assumptions.md) and keep later docs aligned.
- If `saturation_regen` mod-level changelog or README updates were deferred in Phase 4a, they may be completed here once loader parity is established for the mod.

## Specs

- `saturation_regen` must work on `1.21.1` NeoForge on this branch.
- The NeoForge implementation must preserve the mod's core behavior and identity.
- The NeoForge implementation must match the intended Phase 4a behavior closely enough to establish loader parity for `saturation_regen`.
- The NeoForge implementation must keep the intended natural-regeneration design:
  - saturation-driven natural regen should become available sooner than vanilla
  - the configurable food gate remains part of the behavior contract
  - vanilla cadence, exhaustion, and drain order remain intact
  - the `naturalRegeneration` gamerule remains respected
- NeoForge parity review should include whether an explicit logical-server guard is needed to match the Fabric-side implementation intent.
- The result should be a deliberate `1.21.1` NeoForge port, not a rollback to an earlier or simplified behavior model.
- Phase 4b may include the `saturation_regen` documentation updates needed to keep the NeoForge-side branch story accurate, as long as they directly support this phase.
- Phase 4b should leave final branch cleanup and release-readiness work to later phases.
- Phase 4b should assume shared build retargeting is already solved and should not revisit Phase 1 decisions unless a branch-wide blocker is discovered.

## Implementation Plan

1. Review the intended `saturation_regen` behavior as established by the modern branch and the completed Phase 4a Fabric work.
2. Review the NeoForge implementation shape for the mod and translate the needed behavior into the `1.21.1` NeoForge environment.
3. Port `saturation_regen` NeoForge behavior to `1.21.1`, preserving the intended natural-regeneration contract.
4. Evaluate and resolve the logical-server guard question so NeoForge parity with Fabric is intentional rather than accidental.
5. Confirm the NeoForge implementation matches the intended Fabric behavior closely enough for loader parity.
6. Update `saturation_regen` mod-level documentation if needed once loader parity is established for the mod.
7. Leave branch cleanup, broader documentation alignment, and final release-readiness work to later phases.

## Related Files

- [overview.md](overview.md)
- [branch_assumptions.md](branch_assumptions.md)
- [phase_4a_saturation_regen_fabric.md](phase_4a_saturation_regen_fabric.md)
- [Docs/saturation_regen/Archive/saturation_regen_v1/spec.md](../../../Docs/saturation_regen/Archive/saturation_regen_v1/spec.md)
- [Docs/saturation_regen/Archive/saturation_regen_v1/tasks.md](../../../Docs/saturation_regen/Archive/saturation_regen_v1/tasks.md)
- [Docs/saturation_regen/Archive/Saturation_regen_Neoforge.md](../../../Docs/saturation_regen/Archive/Saturation_regen_Neoforge.md)
- [mods/saturation_regen/README.md](../../../mods/saturation_regen/README.md)
- [mods/saturation_regen/CHANGELOG.md](../../../mods/saturation_regen/CHANGELOG.md)
- [mods/saturation_regen/neoforge/build.gradle](../../../mods/saturation_regen/neoforge/build.gradle)
- [mods/saturation_regen/neoforge/src/main/java/com/jedtech/saturation_regen/neoforge/SaturationRegenNeoForgeMod.java](../../../mods/saturation_regen/neoforge/src/main/java/com/jedtech/saturation_regen/neoforge/SaturationRegenNeoForgeMod.java)
- [mods/saturation_regen/neoforge/src/main/java/com/jedtech/saturation_regen/mixin/FoodDataMixin.java](../../../mods/saturation_regen/neoforge/src/main/java/com/jedtech/saturation_regen/mixin/FoodDataMixin.java)
- [mods/saturation_regen/neoforge/src/main/resources/META-INF/neoforge.mods.toml](../../../mods/saturation_regen/neoforge/src/main/resources/META-INF/neoforge.mods.toml)
- [mods/saturation_regen/neoforge/src/main/resources/saturation_regen.neoforge.mixins.json](../../../mods/saturation_regen/neoforge/src/main/resources/saturation_regen.neoforge.mixins.json)
- [mods/saturation_regen/common/src/main/java/com/jedtech/saturation_regen/SaturationRegenConfig.java](../../../mods/saturation_regen/common/src/main/java/com/jedtech/saturation_regen/SaturationRegenConfig.java)
- [mods/saturation_regen/common/src/main/java/com/jedtech/saturation_regen/SaturationRegenConstants.java](../../../mods/saturation_regen/common/src/main/java/com/jedtech/saturation_regen/SaturationRegenConstants.java)

## Validation/Acceptance

- `saturation_regen` builds on the `1.21.1` NeoForge branch target.
- The NeoForge implementation reflects the intended modern `saturation_regen` behavior rather than an earlier or simplified branch.
- The natural regeneration behavior contract is present in the NeoForge implementation.
- NeoForge behavior is aligned with the intended Fabric behavior closely enough to establish loader parity.
- Config behavior remains coherent for the NeoForge implementation.
- The `naturalRegeneration` gamerule is still respected.
- The logical-server guard question has been addressed explicitly rather than left ambiguous.
- The phase does not absorb `xp_stream` work or final branch cleanup.

## Out of Scope

- `saturation_regen` Fabric implementation except as a parity reference
- `xp_stream` implementation on either loader
- Final cross-branch cleanup beyond direct `saturation_regen` NeoForge needs
- Publishing, release execution, or release metadata work
- Broader repo cleanup not required for `saturation_regen` NeoForge on `1.21.1`

## Open Questions if needed

None at this time.
