# Phase 4a Overview: Saturation Regen Fabric

**Status:** Complete

**Note:** **Phase 3 is complete** (`xp_stream` NeoForge on `1.21.1`); `xp_stream` has full Fabric + NeoForge parity on this branch before Saturation Regen work proceeds.

This phase brings `saturation_regen` to `1.21.1` on Fabric and establishes the Fabric-side implementation baseline for the mod on this branch. It should preserve the mod's core identity: a small vanilla-plus change to natural regeneration that makes saturation matter sooner without introducing a new healing system.

Project overview: [overview.md](overview.md)

## Completion notes

- **Behavior:** `FoodDataMixin` keeps the modern natural-regen contract (`@ModifyConstant` on the fast-branch `20` in `FoodData#tick(ServerPlayer)`), configurable `regenHungerPenaltyLevel`, unchanged vanilla cadence/exhaustion/gamerule semantics. **Logical-server guard** added so the constant is not modified on the client.
- **Build:** `:mods:saturation_regen:fabric:build` succeeds on the Phase 1 Fabric remap toolchain (Minecraft `1.21.1`, Loader `0.19.2`, Java 21).
- **Docs:** `mods/saturation_regen/README.md` branch pointer; `mods/saturation_regen/CHANGELOG.md` `[Unreleased]` entry (no version bump in phase).
- **Scope:** NeoForge left for Phase 4b; no `xp_stream` changes.

## Dependencies / Preconditions

- Phase 0 is complete.
- Phase 1 is complete.
- **Phase 2 and Phase 3 are complete** (`xp_stream` Fabric and NeoForge on `1.21.1`). Earlier `xp_stream` work should not need to be revisited unless a regression is found.
- **Phase 4a is complete** (Fabric `saturation_regen` on `1.21.1`; see **Completion notes** above).
- The branch-level assumptions in [branch_assumptions.md](branch_assumptions.md) are accepted as the working baseline.
- Phase 4a should assume the shared branch-level toolchain established by Phase 1:
  - Minecraft `1.21.1`
  - Java 21
  - Fabric remap path with official Mojang mappings
  - Fabric Loader `0.19.2`
  - `minecraft_version_range=[1.21.1,)`

## Agent Instructions

- Keep this phase focused on `saturation_regen` on Fabric only.
- Do not implement NeoForge parity in this phase.
- Use the modern `saturation_regen` behavior as the product target, but port it intentionally to the `1.21.1` Fabric environment rather than assuming direct code carryover.
- Preserve the mod's identity as a targeted vanilla-plus change to the existing natural regeneration path.
- Do not introduce a new healing system, separate healing loop, or custom cadence.
- Phase 4a should assume shared build retargeting is already solved and should not revisit Phase 1 decisions unless a branch-wide blocker is discovered.
- If a discovered issue is clearly shared with the later NeoForge phase, note it for Phase 4b rather than expanding this phase beyond Fabric.
- If mod-level `saturation_regen` changelog or README updates are deferred until loader parity is complete, Phase 4b may finish them.

## Specs

- `saturation_regen` must work on `1.21.1` Fabric on this branch.
- The Fabric implementation must preserve the mod's core behavior and identity.
- The Fabric implementation must keep the intended natural-regeneration design:
  - saturation-driven natural regen should become available sooner than vanilla
  - the configurable food gate remains part of the behavior contract
  - vanilla cadence, exhaustion, and drain order remain intact
  - the `naturalRegeneration` gamerule remains respected
- The result should be a deliberate `1.21.1` Fabric port, not a rollback to an earlier or simplified behavior model.
- Phase 4a may include the `saturation_regen` documentation updates needed to keep the Fabric-side branch story accurate, as long as they directly support this phase.
- Phase 4a should leave NeoForge parity, final branch cleanup, and release-readiness work to later phases.
- Fabric build and metadata retargeting alone are not sufficient; the Fabric gameplay behavior and configuration outcome must also be in place before this phase is complete.

## Implementation Plan

1. Review the current `saturation_regen` Fabric implementation and identify which parts represent the intended modern behavior.
2. Review the earlier Fabric-side design/spec material for the mod and translate the needed behavior into the `1.21.1` Fabric environment.
3. Port `saturation_regen` Fabric behavior to `1.21.1`, preserving the intended natural-regeneration contract.
4. Confirm the Fabric implementation still reflects the intended mod identity and config behavior.
5. Update mod-level documentation only where it directly supports the `1.21.1` Fabric phase outcome, or defer those updates to Phase 4b once loader parity is established.
6. Leave NeoForge parity and broader branch cleanup for later phases.

## Related Files

- [overview.md](overview.md)
- [branch_assumptions.md](branch_assumptions.md)
- [Docs/saturation_regen/Archive/saturation_regen_v1/spec.md](../../../Docs/saturation_regen/Archive/saturation_regen_v1/spec.md)
- [Docs/saturation_regen/Archive/saturation_regen_v1/tasks.md](../../../Docs/saturation_regen/Archive/saturation_regen_v1/tasks.md)
- [mods/saturation_regen/README.md](../../../mods/saturation_regen/README.md)
- [mods/saturation_regen/CHANGELOG.md](../../../mods/saturation_regen/CHANGELOG.md)
- [mods/saturation_regen/fabric/build.gradle](../../../mods/saturation_regen/fabric/build.gradle)
- [mods/saturation_regen/fabric/src/main/java/com/jedtech/saturation_regen/fabric/SaturationRegenFabricMod.java](../../../mods/saturation_regen/fabric/src/main/java/com/jedtech/saturation_regen/fabric/SaturationRegenFabricMod.java)
- [mods/saturation_regen/fabric/src/main/java/com/jedtech/saturation_regen/mixin/FoodDataMixin.java](../../../mods/saturation_regen/fabric/src/main/java/com/jedtech/saturation_regen/mixin/FoodDataMixin.java)
- [mods/saturation_regen/fabric/src/main/resources/fabric.mod.json](../../../mods/saturation_regen/fabric/src/main/resources/fabric.mod.json)
- [mods/saturation_regen/fabric/src/main/resources/saturation_regen.fabric.mixins.json](../../../mods/saturation_regen/fabric/src/main/resources/saturation_regen.fabric.mixins.json)
- [mods/saturation_regen/common/src/main/java/com/jedtech/saturation_regen/SaturationRegenConfig.java](../../../mods/saturation_regen/common/src/main/java/com/jedtech/saturation_regen/SaturationRegenConfig.java)
- [mods/saturation_regen/common/src/main/java/com/jedtech/saturation_regen/SaturationRegenConstants.java](../../../mods/saturation_regen/common/src/main/java/com/jedtech/saturation_regen/SaturationRegenConstants.java)

## Validation/Acceptance

- `saturation_regen` builds on the `1.21.1` Fabric branch target.
- The Fabric implementation reflects the intended modern `saturation_regen` behavior rather than an earlier or simplified branch.
- The natural regeneration behavior contract is present in the Fabric implementation.
- Config behavior remains coherent for the Fabric implementation.
- The `naturalRegeneration` gamerule is still respected.
- The phase does not absorb NeoForge work.

## Out of Scope

- `saturation_regen` NeoForge implementation
- `xp_stream` implementation on either loader
- Final cross-loader parity work
- Branch-wide documentation cleanup beyond direct `saturation_regen` Fabric needs
- Publishing, release execution, or release metadata work
- Broader repo cleanup not required for `saturation_regen` Fabric on `1.21.1`

## Open Questions if needed

None at this time.
