# XP_Stream — Technical Brief

## Overview

XP_Stream is a server-side multi-loader mod for Minecraft 1.21.11 that accelerates XP absorption while preserving vanilla mechanics. It eliminates the "ankle swarm" effect when many XP orbs arrive at the player simultaneously. Supports both Fabric and NeoForge.

## Architecture

```
com.jedtech.xp_stream
├── XpStreamConfig.java      # JSON config loader (common)
├── XpStreamConstants.java   # Default values (common)
├── api/
│   └── XpStreamPlatform.java    # Platform abstraction (minimal)
├── fabric/
│   ├── XpStreamFabricMod.java   # Fabric entrypoint
│   └── mixin/
│       └── ExperienceOrbEntityMixin.java  # Fabric mixin (Yarn mappings)
└── neoforge/
    ├── XpStreamNeoForgeMod.java  # NeoForge entrypoint
    └── mixin/
        └── ExperienceOrbMixin.java  # NeoForge mixin (Mojmap mappings)
```

## Core Mechanism: Collision-Based Burst Pickup

When a player collides with an XP orb:

1. Vanilla pickup proceeds normally (triggering orb)
2. At `TAIL` of `onPlayerCollision`, the mixin queries for additional orbs **already colliding** with the player's bounding box
3. Up to `maxBurstOrbs` additional orbs are collected via the vanilla pickup path
4. Each burst orb has `experiencePickUpDelay` reset to bypass vanilla's 2-tick delay

### Why Collision-Based?

- **Preserves vanilla feel** — Orbs still fly toward the player visually
- **No "magnet" effect** — Only orbs that have reached the player are collected
- **No radius expansion** — Uses `player.getBoundingBox()` directly

## Mixin Implementation

**Fabric (Yarn mappings):**
- Target: `net.minecraft.entity.ExperienceOrbEntity`
- Method: `onPlayerCollision(PlayerEntity player)`
- Injection: `@At("TAIL")`

**NeoForge (Mojmap mappings):**
- Target: `net.minecraft.world.entity.ExperienceOrb`
- Method: `playerTouch(Player player)`
- Injection: `@At("TAIL")`

Both mixins implement the same collision-based burst pickup logic, but use loader-specific mapping names.

### Re-entrancy Guard

A static boolean flag prevents infinite loops when `onPlayerCollision` is called on burst orbs:

```java
@Unique
private static boolean xp_stream$inBurst = false;
```

### Server-Side Enforcement

```java
if (!(player.getEntityWorld() instanceof ServerWorld serverWorld)) return;
```

### Pickup Delay Reset

Vanilla sets `experiencePickUpDelay` after each pickup. Without resetting it, burst orbs would be blocked:

```java
player.experiencePickUpDelay = 0;
orb.onPlayerCollision(player);  // Vanilla path — Mending works
```

## Configuration

**File:** `config/xp_stream.json`

```json
{
  "maxBurstOrbs": 6,
  "debug": false
}
```

| Setting | Default | Range | Description |
|---------|---------|-------|-------------|
| `maxBurstOrbs` | 6 | 0–64 | Extra orbs to collect per pickup event. Set to 0 to disable. |
| `debug` | false | — | Log burst pickup events to console |

Config is loaded during mod initialization:
- **Fabric:** `FabricLoader.getInstance().getConfigDir()`
- **NeoForge:** `FMLPaths.CONFIGDIR.get()`

## Mending Compatibility

XP_Stream preserves Mending by using the vanilla pickup path:

```java
orb.onPlayerCollision(player);  // Calls repairPlayerGears() internally
```

**Never** use `player.addExperience()` directly — it bypasses Mending.

## Performance

- **No per-tick scanning** — Logic only runs on collision events
- **Bounded query** — Uses player bounding box, not world-wide search
- **Capped iteration** — Limited by `maxBurstOrbs`

## Test Results

**Test:** 200 XP orbs spawned as a cluster

| Metric | Vanilla | XP_Stream (maxBurstOrbs=4) | XP_Stream (maxBurstOrbs=6) |
|--------|---------|----------------------------|----------------------------|
| Absorption time | 11.06s | 4.2s | 2.5s |
| XP received | 11 lvl + 12 pts | 11 lvl + 12 pts | 11 lvl + 12 pts |

**Verified on both loaders:**
- ✅ Fabric: Tested and working
- ✅ NeoForge: Tested and working (2.5s with maxBurstOrbs=6)

No XP loss. Mending compatibility preserved on both platforms.

## Loader Support

| Loader | Status | Version |
|--------|--------|---------|
| Fabric | ✅ Supported | 1.21.11 (Fabric Loader ≥0.16.0) |
| NeoForge | ✅ Supported | 1.21.11 (NeoForge 21.11.0-beta) |

## Dependencies

- Minecraft 1.21.11
- Fabric Loader ≥0.16.0 (for Fabric version)
- NeoForge 21.11.0-beta (for NeoForge version)
- Java 21
