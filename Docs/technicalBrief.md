# XP_Stream — Technical Brief

## Overview

XP_Stream is a server-side multi-loader mod for Minecraft 26.1 that accelerates XP absorption while preserving vanilla mechanics. It eliminates the "ankle swarm" effect when many XP orbs arrive at the player simultaneously. Supports both Fabric and NeoForge.

This repository is a **mono-repo**: several mods share one Gradle workspace. **Product-facing docs** live next to each mod; **workspace** docs live at the repo root.

| Location | Purpose |
|----------|---------|
| `README.md` (root) | Mono-repo overview, `just` commands, links to per-mod READMEs |
| `CHANGELOG.md` (root) | Repository/tooling changes only — **not** mod release notes |
| `mods/<mod_id>/README.md` | Player-facing description for that mod |
| `mods/<mod_id>/CHANGELOG.md` | That mod’s release history; **Modrinth**, **CurseForge**, and **`just github-release <mod>`** use the `## [<mod_version>]` section matching `mods/<mod_id>/gradle.properties` |

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
│       └── ExperienceOrbEntityMixin.java  # Fabric mixin (Mojmap)
└── neoforge/
    ├── XpStreamNeoForgeMod.java  # NeoForge entrypoint
    └── mixin/
        └── ExperienceOrbMixin.java  # NeoForge mixin (Mojmap mappings)
```

## Core Mechanism: Vanilla-Range Burst Pickup (On Foot)

When an XP orb is picked up by vanilla:

1. Vanilla pickup proceeds normally (triggering orb)
2. At `TAIL` of `playerTouch`, the mixin queries additional orbs in vanilla's on-foot pickup area: `player.getBoundingBox().inflate(1.0, 0.5, 1.0)`
3. Up to `maxBurstOrbs` additional orbs are collected via the vanilla pickup path
4. Each burst orb has `takeXpDelay` reset to bypass vanilla's 2-tick delay

Current scope is on-foot parity; mounted pickup-geometry parity is planned separately.

### Why Vanilla Pickup Range?

- **Source-aligned candidate set (on foot)** — Matches `Player.aiStep` pickup geometry for which orbs are in range
- **Preserves vanilla path** — Burst still calls `orb.playerTouch(player)` (Mending and XP handling stay vanilla)
- **Bounded local query** — Uses an expanded player AABB, not a world-wide scan

## Mixin Implementation

**Fabric (Mojmap):**
- Target: `net.minecraft.world.entity.ExperienceOrb`
- Method: `playerTouch(Player player)`
- Injection: `@At("TAIL")`

**NeoForge (Mojmap):**
- Target: `net.minecraft.world.entity.ExperienceOrb`
- Method: `playerTouch(Player player)`
- Injection: `@At("TAIL")`

Both mixins use Mojmap and implement the same vanilla-range burst pickup logic; only the mixin class names differ (ExperienceOrbEntityMixin vs ExperienceOrbMixin).

### Re-entrancy Guard

A static boolean flag prevents infinite loops when `playerTouch` is called on burst orbs:

```java
@Unique
private static boolean xp_stream$inBurst = false;
```

### Server-Side Enforcement

```java
if (!(player.level() instanceof ServerLevel serverLevel)) return;
```

### Pickup Delay Reset

Vanilla sets the pickup delay after each pickup. Without resetting it, burst orbs would be blocked:

```java
player.takeXpDelay = 0;
orb.playerTouch(player);  // Vanilla path — Mending works
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
orb.playerTouch(player);  // Calls repairPlayerGears() internally
```

**Never** use `player.addExperience()` directly — it bypasses Mending.

## Performance

- **No per-tick global scanning** — Logic runs only when `playerTouch` fires
- **Bounded query** — Uses vanilla on-foot pickup area (`inflate(1.0, 0.5, 1.0)`), not a world-wide search
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
| Fabric | ✅ Supported | Minecraft 26.1 (Fabric Loader ≥0.18.4; tested on Snapshot 9) |
| NeoForge | ✅ Supported | Minecraft 26.1 Snapshot 7 (NeoForge 26.1.0.0-alpha.12+snapshot-7) |

## Dependencies

- Minecraft 26.1
- Fabric Loader ≥0.18.4 (for Fabric version)
- NeoForge 26.1.0.0-alpha.12+snapshot-7 (for NeoForge version; or latest per [maven-metadata.xml](https://maven.neoforged.net/releases/net/neoforged/neoforge/maven-metadata.xml))
- Java 25

## Saturation Regen

Second mod in the repo (`mods/saturation_regen`): **server-side** adjustment to vanilla **natural regeneration** driven by food and saturation in **`FoodData#tick`**. Players can use the **fast** sat-based regen path **without** topping the hunger bar to 20, as long as **`getFoodLevel() > regenHungerPenaltyLevel`** (default **6** ⇒ **7+** food — same “above 6” idea as vanilla sprint). Healing **rate** and **exhaustion** stay vanilla; only **eligibility** for that branch is widened via a single **`@ModifyConstant`** on the **`>= 20`** check (with special handling so **18–19** food still follows the **slow** regen path like vanilla).

**Config (Fabric):** `config/saturation_regen.json`

```json
{
  "regenHungerPenaltyLevel": 6
}
```

Restart the game or server to apply changes. **No** custom packets; behavior is authoritative on the logical server (vanilla clients are fine).

**Loaders:** **Fabric** is implemented for Minecraft **26.1.x**. **NeoForge** subproject exists but is **not** implemented for this v1 (defer to a later change).

**Design record (archived):** `Docs/saturation_regen/Archive/saturation_regen_v1/spec.md`

## Release and Publishing Notes

- `xp_stream` is the current intended public release target.
- `saturation_regen`: **Fabric** gameplay is implemented; **NeoForge** is still scaffold-only. Publishing wiring exists under `mods/saturation_regen/publish*`, but public publishing is not the primary focus yet (see `Docs/publishing_tasks.md`).
- Publishing is currently manual/local.
- **Before publish or GitHub release:** add or update the `## [<mod_version>]` section in **`mods/<mod_id>/CHANGELOG.md`** (not the root changelog).
- Modrinth/CurseForge release notes come from that per-mod file via Gradle; **`just github-release <mod>`** uses the same file for GitHub release body text.
- Use `just xp-stream-publish` for the normal `xp_stream` publish path.
- Use `just github-release xp_stream` (or `just xp-stream-github-release`) for GitHub release metadata after publishing.
- See `Docs/publishing.md` for publishing details.
- See `Docs/release_checklist.md` for the step-by-step release flow.
- See `Docs/github_release.md` for GitHub automation behavior.
