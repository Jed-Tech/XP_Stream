# Saturation Regen

**Let saturation trigger healing - full hunger bar not required.**

Vanilla often makes **saturation** feel underused for healing. You can eat great food, have saturation available, but you still won't heal because vanilla wants you to be "full" first.

Saturation Regen keeps **vanilla healing behavior and vanilla saturation drain**, but lets natural regen start sooner, so topping off every drumstick isn't required.

---

## What It Does

- Allows natural regeneration when:
  - **Saturation > 0**
  - and hunger is above 6 points (3 drumsticks) - *this is configurable*
- Uses **vanilla timing and exhaustion** - no extra healing loops
- Respects the **`naturalRegeneration`** gamerule
- **Server-side** on **Fabric** and **NeoForge** - vanilla clients do not need the mod

---

## Vanilla-Plus Design

This mod does **not**:
- Add new healing mechanics
- Override potion or effect-based healing

It widens **which hunger levels** can use vanilla's fast natural-regen branch when saturation allows.

---

## Installation

1. Install Fabric or NeoForge Loader
2. Drop the mod jar into your `mods` folder
3. Start the server

---

## Branch Compatibility

- **`monorepo/1.21.1`** - validated on **Minecraft 1.21.1** for **Fabric** and **NeoForge**
- **`main`** - current development branch for the newer Minecraft line supported there

On `monorepo/1.21.1`, Saturation Regen has full Fabric + NeoForge parity for this branch.

---

## Minecraft Versions (Repo Branches)

- **`main`** - targets the current Minecraft line in that branch (see root `gradle.properties` and changelogs).
- **`monorepo/1.21.1`** - dedicated **Minecraft 1.21.1** support for **Fabric** and **NeoForge** (same mod behavior on both loaders).
