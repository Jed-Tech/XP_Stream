# Plan: Configurable Burst Pickup Margin (Inflate Player AABB)

**Status:** Not implemented — `burstPickupMargin` design is documented here and needs to be re-implemented/refactored in code.

## Goal

After **normal** XP pickup runs for the orb that touched the player, **burst-collect** *other* XP orbs that lie within a **configurable margin** around the player hitbox—**min `0` = disabled**, **default `0.4` blocks** (uniform inflate on all axes), **max `1.0`**. Set to **`0`** to keep current behavior: burst only queries **strict** collision (no shell). This catches orbs just outside the hitbox (e.g. on a trapdoor above the player’s head) without replacing vanilla’s first orb.

For the "stuck above your head" scenario you’re testing, **`burstPickupMargin: 0.4` is the practical sweet spot** (smallest range that still reliably pulls the intended burst candidates).

---

## Design

- **Order of operations (non-negotiable for this feature):**
  1. **Vanilla / normal collection first.** The burst mixin injects at **`@At("TAIL")`** of `ExperienceOrb.playerTouch`, so the **triggering** orb’s vanilla `playerTouch` logic runs to completion before any burst code executes.
  2. **Burst second.** Only **other** orbs (`orb != self`, alive) are queried via `getEntitiesOfClass(ExperienceOrb.class, playerBox, …)`. The **`playerBox`** is **`player.getBoundingBox().inflate(margin, margin, margin)`** when **`margin = burstPickupMargin` > 0**; when **`margin == 0`**, **`playerBox`** is the **strict** bounding box (today’s behavior). Orbs in that volume are then burst-processed up to **`maxBurstOrbs`** via **`playerTouch`** (re-entrancy guarded).
- **“Distance”** here: **axis-aligned** expansion of the player AABB by **`burstPickupMargin`** (same value on **X/Y/Z**), not a separate spherical radius API. It is the planned implementation of “within a configurable distance” for burst candidates.
- **Option used:** Uniform AABB inflate (Option 1) + configurable margin (Option 3).
- **Config:** New field **`burstPickupMargin`** (double) in `xp_stream.json`. Min **`0`** disables expansion, max **`1.0`**; default **`0.4`**, allowed range **`0.0`–`1.0`**. Values outside range are clamped in **`validate()`**.
- **Validation:** Overlap plateau experiments (Tests 1–3: inflate ladder, distance buckets, optional cramming sweep) — [inflateAABBTestPlan.md](inflateAABBTestPlan.md).

---

## Implementation Steps

### 1. Constants (`common/.../XpStreamConstants.java`)

- Add:
  - `DEFAULT_BURST_PICKUP_MARGIN = 0.4`
  - `MAX_BURST_PICKUP_MARGIN = 1.0`
- Use these in config default and validation.

### 2. Config (`common/.../XpStreamConfig.java`)

- Add field: `private double burstPickupMargin = XpStreamConstants.DEFAULT_BURST_PICKUP_MARGIN;`
- In `validate()`:
  - If `burstPickupMargin < 0`, set to `0` and log.
  - If `burstPickupMargin > XpStreamConstants.MAX_BURST_PICKUP_MARGIN`, set to `MAX_BURST_PICKUP_MARGIN` and log.
- Add getter: `public double getBurstPickupMargin() { return burstPickupMargin; }`
- In the existing debug block that logs config values, include `burstPickupMargin`.

### 3. NeoForge mixin (`neoforge/.../ExperienceOrbMixin.java`)

- After resolving `config` and before building the query box:
  - Get margin: `double margin = config.getBurstPickupMargin();`
  - Build query AABB:  
    `AABB playerBox = margin > 0 ? player.getBoundingBox().inflate(margin, margin, margin) : player.getBoundingBox();`
- Optionally: update the comment above the query from “no radius expansion” to “optional margin from config (0 = strict collision)” so the design is clear in code.

### 4. Fabric mixin (`fabric/.../ExperienceOrbEntityMixin.java`)

- Apply the same logic as in the NeoForge mixin:
  - `double margin = config.getBurstPickupMargin();`
  - `AABB playerBox = margin > 0 ? player.getBoundingBox().inflate(margin, margin, margin) : player.getBoundingBox();`
- Update the related comment to match.

### 5. Verification

- **Default:** New world/config uses `burstPickupMargin: 0.4`; orbs slightly above the player (e.g. on a trapdoor) burst-collect.
- **Zero:** Set `burstPickupMargin: 0` in config; behavior matches current (strict collision only).
- **Max:** Set `burstPickupMargin: 1.0`; burst range is player box + 1 block in all directions; no errors.
- **Sweet spot (your test):** With **`"debug": true`**, you should see burst candidates with **`strictOthers=0 queryOthers=8 maxBurst=6 picking=6`** for the targeted “above head” pile (exact `gameTime`/AABB coords vary).
- **`maxBurstOrbs` recommendation for your tests:** In this geometry, `queryOthers` (candidates inside the burst query) has been observed to peak at **19**, so keep **`maxBurstOrbs <= 19`** to avoid requesting more than can exist per burst pass.
- **Clamp:** Set e.g. `1.5` or `-0.1` in JSON; after load, value is clamped to 1.0 or 0.0 and logged.
- **Both loaders:** Run Fabric and NeoForge and confirm same behavior for a given config.

### 6. Docs / release (optional)

- **README or config docs:** Mention `burstPickupMargin` (default 0.4, range 0–1, 0 = vanilla-style collision-only burst).
- **CHANGELOG:** Note “Configurable burst pickup margin (default 0.4, max 1 block); set to 0 to disable expansion.”

---

## Files to touch

| File | Change |
|------|--------|
| `common/.../XpStreamConstants.java` | Add default and max margin constants. |
| `common/.../XpStreamConfig.java` | Add field, validation, getter, debug log. |
| `neoforge/.../ExperienceOrbMixin.java` | Use margin to build query AABB (inflate when > 0). |
| `fabric/.../ExperienceOrbEntityMixin.java` | Same as NeoForge. |

---

## AABB API note

Use `AABB.inflate(double x, double y, double z)`. For uniform expansion use the same value for all three (e.g. `inflate(margin, margin, margin)`). If the project uses a different method name (e.g. `expand`), switch to the correct one for the target Minecraft/NeoForge version.
