# XP Stream

**Faster XP pickup. Same vanilla feel.**

Absorbs XP orbs faster without clumping, while preserving the magic feel and core mechanics of normal XP collection. All the orbs with no loss of XP.

---

## Why XP Stream

Large XP drops in vanilla pile up and take too long to absorb.
XP Stream removes the pickup delay so XP flows smoothly toward players, without changing how it looks or behaves.

It feels like vanilla Minecraft... just smoother.

---

## What It Does

- Faster XP absorption for large drops
- Preserves vanilla orb motion, sounds, and Mending
- No XP loss: every orb grants full value
- Cleaner visuals with fewer lingering orbs
- Server-side only: no client install needed
- Safe for single player or multiplayer

---

## Vanilla-Plus Design

XP Stream is **not** an orb-clumping mod.

Instead of changing XP behavior, it adjusts *how fast* orbs are collected, keeping the familiar vanilla feel while avoiding pools of XP that just won't absorb.

---

## Great for XP Farms

Ideal for mob farms, spawners, boss fights, and XP farms.
Fewer lingering orbs means a **performance improvement** over vanilla.

---

## Configuration (Optional)

XP Stream works out of the box, but there are options for advanced configuration:

Config file: `config/xp_stream.json`

```json
{
  "maxBurstOrbs": 6,
  "debug": false
}
```

- `maxBurstOrbs` (default: 6) higher = faster absorption of XP orbs.
- Setting `maxBurstOrbs` to `0` effectively disables the mod.

---

## Installation

1. Install Fabric or NeoForge Loader
2. Drop the mod jar into your `mods` folder
3. Start the server

---

## Minecraft Versions (Repo Branches)

- **`main`** - current development targets the latest supported Minecraft line in that branch (see root `gradle.properties` and mod changelogs).
- **`monorepo/1.21.1`** - dedicated **Minecraft 1.21.1** support for **Fabric** and **NeoForge** (see repository `CHANGELOG.md` for workspace notes).
