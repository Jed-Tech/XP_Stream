# XP_Stream

## XP_Stream Overview

**XP_Stream** is a lightweight, server-side Minecraft mod focused on improving the *experience* of collecting experience orbs while preserving vanilla gameplay.

Experience orbs still spawn, move, and behave as players expect. XP_Stream simply removes unnecessary friction in how XP is absorbed, so large amounts of XP feel satisfying instead of sluggish.

The intent is refinement, not reinvention.

---

## Goals

- Make experience absorb **faster and smoother**
- Preserve the visual and mechanical feel of vanilla XP orbs
- Eliminate the “ankle swarm” effect during large XP pickups
- Remain server-side only and easy to deploy
- Keep the implementation simple and maintainable across Minecraft updates

---

## Design Philosophy

XP_Stream follows a strict vanilla-first philosophy:

- XP rules and values are unchanged
- XP orbs remain physical entities
- Pickup behavior is improved without adding magnets, storage, or automation
- Mending and enchantment interactions behave exactly as in vanilla
- Changes are localized to XP pickup behavior only

If XP_Stream is doing its job, players should feel the improvement without feeling like a new system was introduced.

---

## Requirements

- Minecraft Java Edition
- Fabric Loader
- Neoforge (after beta 1)
- Server-side installation only (clients do not need the mod)

---

## License

XP_Stream is **source-available** for transparency.

Unmodified versions may be included in modpacks when downloaded from
an official source (such as the project's GitHub or Modrinth page).

Redistribution, modification, or derivative works are **not permitted** without explicit written permission from the author.  
See the `LICENSE` file for full terms.