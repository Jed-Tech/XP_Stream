# Phase 2 Overview: XP Stream Fabric

**Status:** Complete

This phase brings `xp_stream` to `1.21.1` on Fabric and establishes the Fabric-side implementation baseline for the mod on this branch. It includes the newer Vanilla Pickup behavior as part of the `1.21.1` support target.

Project overview: [overview.md](overview.md)

## Completion notes

- **Behavior:** `xp_stream` Fabric on `1.21.1` matches the intended modern branch behavior, including Vanilla Pickup range burst logic, current burst throughput/config intent, and the updated `"in range"` debug wording.
- **Build:** Fabric-side `xp_stream` builds successfully on the Phase 1 `1.21.1` toolchain.
- **Docs:** Mod-level documentation updates that depended on full loader parity were allowed to flow into Phase 3.
- **Scope:** NeoForge parity and later branch cleanup remained outside this phase.

## Dependencies / Preconditions

- Phase 0 is complete.
- Phase 1 is complete.
- The branch-level assumptions in [branch_assumptions.md](branch_assumptions.md) are accepted as the working baseline.
- Shared workspace retargeting for the `1.21.1` branch should already be in place before this phase begins.
- Phase 2 should assume the shared branch-level toolchain established by Phase 1:
  - Minecraft `1.21.1`
  - Java 21
  - Fabric remap path with official Mojang mappings
  - Fabric Loader `0.19.2`
  - `minecraft_version_range=[1.21.1,)`

## Agent Instructions

- Keep this phase focused on `xp_stream` on Fabric only.
- Do not implement NeoForge parity in this phase.
- Use the modern `xp_stream` behavior as the product target, but port it intentionally to the `1.21.1` Fabric environment rather than assuming direct code carryover.
- Treat Vanilla Pickup behavior as part of the phase contract, not as optional follow-up work.
- Preserve the established identity of the mod: faster pickup, vanilla feel, no XP loss, and use of vanilla pickup flow.
- If a discovered issue is clearly shared with the later NeoForge phase, note it for Phase 3 rather than expanding this phase beyond Fabric.
- Phase 2 should assume shared build retargeting is already solved and should not revisit Phase 1 decisions unless a branch-wide blocker is discovered.
- If mod-level `xp_stream` changelog or README updates are deferred until loader parity is complete, Phase 3 may finish them.

## Specs

- `xp_stream` must work on `1.21.1` Fabric on this branch.
- The Fabric implementation must preserve the mod's core behavior and identity.
- The Fabric implementation must include the newer Vanilla Pickup behavior:
  - use vanilla on-foot pickup range as the candidate area for burst pickup
  - keep burst throughput behavior
  - keep existing config intent for burst count
  - keep the debug output aligned with the newer "in range" wording
- The result should be a deliberate `1.21.1` Fabric port, not a partial rollback to older pre-Vanilla-Pickup behavior.
- Phase 2 may include the `xp_stream` documentation updates needed to keep the Fabric-side branch story accurate, as long as they directly support this phase.
- Phase 2 should leave NeoForge parity, final branch cleanup, and release-readiness work to later phases.
- Fabric build and metadata retargeting alone are not sufficient; the Fabric gameplay behavior and Vanilla Pickup outcome must also be in place before this phase is complete.

## Implementation Plan

1. Review the current `xp_stream` Fabric implementation and identify which parts represent the intended modern behavior.
2. Review the older `1.21.1` Fabric implementation shape and translate the needed behavior into that environment.
3. Port `xp_stream` Fabric behavior to `1.21.1`, including the Vanilla Pickup burst-range behavior.
4. Confirm the Fabric implementation still reflects the intended mod identity and config behavior.
5. Update mod-level documentation only where it directly supports the `1.21.1` Fabric phase outcome, or defer those updates to Phase 3 once loader parity is established.
6. Leave NeoForge parity and broader branch cleanup for later phases.

## Related Files

- [overview.md](overview.md)
- [branch_assumptions.md](branch_assumptions.md)
- [VanillaPickup_plan.md](../../../Docs/xp_stream/Archive/VanillaPickup_plan.md)
- [mods/xp_stream/README.md](../../../mods/xp_stream/README.md)
- [mods/xp_stream/CHANGELOG.md](../../../mods/xp_stream/CHANGELOG.md)
- [mods/xp_stream/fabric/build.gradle](../../../mods/xp_stream/fabric/build.gradle)
- [mods/xp_stream/fabric/src/main/java/com/jedtech/xp_stream/fabric/XpStreamFabricMod.java](../../../mods/xp_stream/fabric/src/main/java/com/jedtech/xp_stream/fabric/XpStreamFabricMod.java)
- [mods/xp_stream/fabric/src/main/java/com/jedtech/xp_stream/mixin/ExperienceOrbEntityMixin.java](../../../mods/xp_stream/fabric/src/main/java/com/jedtech/xp_stream/mixin/ExperienceOrbEntityMixin.java)
- [mods/xp_stream/fabric/src/main/resources/fabric.mod.json](../../../mods/xp_stream/fabric/src/main/resources/fabric.mod.json)
- [mods/xp_stream/fabric/src/main/resources/xp_stream.fabric.mixins.json](../../../mods/xp_stream/fabric/src/main/resources/xp_stream.fabric.mixins.json)
- [mods/xp_stream/common/src/main/java/com/jedtech/xp_stream/XpStreamConfig.java](../../../mods/xp_stream/common/src/main/java/com/jedtech/xp_stream/XpStreamConfig.java)
- [mods/xp_stream/common/src/main/java/com/jedtech/xp_stream/XpStreamConstants.java](../../../mods/xp_stream/common/src/main/java/com/jedtech/xp_stream/XpStreamConstants.java)

## Validation/Acceptance

- `xp_stream` builds on the `1.21.1` Fabric branch target.
- The Fabric implementation reflects the intended modern `xp_stream` behavior rather than the older collision-only burst behavior.
- Vanilla Pickup behavior is present in the Fabric implementation.
- Config behavior remains coherent for the Fabric implementation.
- Debug wording reflects the newer "in range" terminology where applicable.
- The phase does not absorb NeoForge work.

## Out of Scope

- `xp_stream` NeoForge implementation
- `saturation_regen` implementation on either loader
- Final cross-loader parity work
- Branch-wide documentation cleanup beyond direct `xp_stream` Fabric needs
- Publishing, release execution, or release metadata work
- Broader repo cleanup not required for `xp_stream` Fabric on `1.21.1`

## Open Questions if needed

None at this time.
