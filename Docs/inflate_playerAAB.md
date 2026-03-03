# Plan: Configurable Burst Pickup Margin (Inflate Player AABB)

## Goal

Slightly expand the volume used for burst-collecting XP orbs so orbs just outside the player hitbox (e.g. on a trapdoor above the player’s head) are included. Margin is configurable: **default 0.1 blocks**, **max 1.0 block**. Set to **0** to keep current behavior (strict collision only).

---

## Design

- **Option used:** Uniform AABB inflate (Option 1) + configurable margin (Option 3).
- **When:** The expansion applies only to the `getEntitiesOfClass(ExperienceOrb.class, playerBox, ...)` query in the burst pickup mixin. Vanilla pickup still requires actual collision; we only broaden which *other* orbs get collected in the same burst.
- **Config:** New field `burstPickupMargin` (double) in `xp_stream.json`. Default `0.1`, allowed range `0.0`–`1.0`. Values outside range are clamped in `validate()`.

---

## Implementation Steps

### 1. Constants (`common/.../XpStreamConstants.java`)

- Add:
  - `DEFAULT_BURST_PICKUP_MARGIN = 0.1`
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

- **Default:** New world/config uses `burstPickupMargin: 0.1`; orbs slightly above the player (e.g. on a trapdoor) burst-collect.
- **Zero:** Set `burstPickupMargin: 0` in config; behavior matches current (strict collision only).
- **Max:** Set `burstPickupMargin: 1.0`; burst range is player box + 1 block in all directions; no errors.
- **Clamp:** Set e.g. `1.5` or `-0.1` in JSON; after load, value is clamped to 1.0 or 0.0 and logged.
- **Both loaders:** Run Fabric and NeoForge and confirm same behavior for a given config.

### 6. Docs / release (optional)

- **README or config docs:** Mention `burstPickupMargin` (default 0.1, range 0–1, 0 = vanilla-style collision-only burst).
- **CHANGELOG:** Note “Configurable burst pickup margin (default 0.1, max 1 block); set to 0 to disable expansion.”

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
