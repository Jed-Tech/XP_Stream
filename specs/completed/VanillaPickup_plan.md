# Plan: Vanilla Pickup Range Burst (On-Foot, Fixed)

**Status:** Not implemented.

**Minecraft target:** `26.1` (Mojmap names).

**Plan type:** Alternate plan. `Docs/inflate_playerAAB.md` is canceled for this branch direction.

---

## Goal

After normal vanilla pickup runs for the triggering orb (`ExperienceOrb.playerTouch`), burst-pick additional sibling orbs using the same on-foot pickup geometry that vanilla uses in `Player.aiStep`.

- Fixed range (non-configurable): `player.getBoundingBox().inflate(1.0, 0.5, 1.0)`
- Throughput remains modded: pick up to `maxBurstOrbs` siblings in one burst pass.
- Selection strategy remains current: first-K in returned list order (no random sampling).

This is "same candidate range as vanilla, faster throughput."

---

## Scope / Non-scope

### In scope (this plan)

- On-foot vanilla parity only for burst candidate range.
- Keep existing hook point at `ExperienceOrb.playerTouch` `@At("TAIL")`.
- Keep re-entrancy guard and existing burst flow.
- Keep `maxBurstOrbs` config behavior.
- Update debug line format to:
  - `Burst pickup: <picked> orbs (of <inRange> in range).`

### Out of scope (later)

- Mounted/player-vehicle parity branch (`minmax(vehicle).inflate(1.0, 0.0, 1.0)`).
- Any configurable `burstPickupMargin` feature.

---

## Design

1. Vanilla first, burst second (unchanged):
   - Injection stays at `ExperienceOrb.playerTouch` tail, so the triggering orb fully completes vanilla handling first.
2. Candidate query geometry changes:
   - Replace strict player collision AABB query with fixed vanilla on-foot pickup area:
   - `AABB playerBox = player.getBoundingBox().inflate(1.0, 0.5, 1.0);`
3. Candidate filter (unchanged intent):
   - `orb != self`
   - `orb.isAlive()`
4. Burst application (unchanged intent):
   - Iterate queried siblings in list order.
   - Reset `player.takeXpDelay = 0` before each sibling `playerTouch` call.
   - Stop at `maxBurstOrbs`.

---

## Pre-Implementation Findings (verified on this branch)

- Both loaders currently use strict player collision AABB for sibling query:
  - `AABB playerBox = player.getBoundingBox();`
- Both loaders already use first-K list-order burst selection with:
  - `if (picked >= config.getMaxBurstOrbs()) break;`
- Existing debug text still says `"colliding"` and must be updated to `"in range"` per this plan.
- No additional `ExperienceOrb` burst mixins were found in `mods/xp_stream` beyond the Fabric and NeoForge mixins listed below.
- Vanilla reference for this plan remains confirmed in `Docs/vanilla_experience_orb_pickup.md`:
  - On-foot pickup area: `player.getBoundingBox().inflate(1.0, 0.5, 1.0)`
  - Mounted branch deferred to later plan.
- Additional doc follow-up after implementation:
  - `Docs/technicalBrief.md` currently describes strict "colliding with player's bounding box" behavior and should be updated once code lands.

---

## Implementation Steps

### 1) Common behavior contract (docs/comments only, no new config)

- Record in code comments that burst candidate area is intentionally matched to vanilla on-foot pickup range from `Player.aiStep` (26.1).
- Do not add `burstPickupMargin` constants/config fields.

### 2) Fabric mixin

File: `mods/xp_stream/fabric/src/main/java/com/jedtech/xp_stream/mixin/ExperienceOrbEntityMixin.java`

- Change query AABB from strict `player.getBoundingBox()` to:
  - `player.getBoundingBox().inflate(1.0, 0.5, 1.0)`
- Keep existing sibling filtering and first-K loop behavior.
- Update debug print string to:
  - `Burst pickup: <picked> orbs (of <inRange> in range).`

### 3) NeoForge mixin

File: `mods/xp_stream/neoforge/src/main/java/com/jedtech/xp_stream/mixin/ExperienceOrbMixin.java`

- Apply the same AABB and debug updates as Fabric.
- Preserve behavior parity across loaders.

### 4) Validation pass

- Confirm no references to configurable margin remain in active burst logic for this plan.
- Confirm both loaders compile/run with the same behavior.

---

## Acceptance / Verification

### A. Throughput baseline stress

- 200-orb test (as previously used in this project):
  - Burst remains stable with no crashes or runaway recursion.
  - Debug output shows expected in-range counts and capped picks (`picked <= maxBurstOrbs`).

### B. Geometry behavior checks (source-aligned expectations)

1. Behind line of full blocks, ~1 block gap:
   - Expectation: vanilla does not pick up orbs there.
   - Burst should not pull siblings outside the fixed in-range query for this setup.

2. Behind line of wall blocks, ~0.5 block gap:
   - Expectation: vanilla does pick up orbs.
   - Burst should include siblings in that same practical range.

3. Below trapdoor test:
   - Expectation: vanilla does pick up orbs.
   - Burst should include siblings in that same practical range.

### C. Logging format check

- With `debug: true`, verify format is exactly:
  - `Burst pickup: <picked> orbs (of <inRange> in range).`

### D. Loader parity

- Confirm Fabric and NeoForge produce equivalent behavior under the same test setups.

---

## Risks / Notes

- This plan intentionally prioritizes parity with vanilla on-foot pickup geometry over user-tunable range.
- Because vanilla itself picks one random orb per tick while burst picks first-K in list order, total throughput remains intentionally non-vanilla.
- Mounted parity is deferred; if player-while-mounted behavior matters later, add a follow-up plan to mirror vanilla passenger handling.

---

## Files to Touch (implementation phase)

- `mods/xp_stream/fabric/src/main/java/com/jedtech/xp_stream/mixin/ExperienceOrbEntityMixin.java`
- `mods/xp_stream/neoforge/src/main/java/com/jedtech/xp_stream/mixin/ExperienceOrbMixin.java`
