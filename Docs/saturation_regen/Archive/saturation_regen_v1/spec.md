# saturation_regen_v1

## Summary

Ship the first playable **saturation_regen** release on **Fabric**: adjust **vanilla natural regeneration** (the hunger/saturation-driven healing inside `FoodData#tick`) so players can heal when **saturation > 0** without needing **full** hunger, subject to a **hunger-penalty threshold for natural regen** (**strict** `getFoodLevel() >` — see **Config**). Preserve vanilla timing, exhaustion, saturation-before-hunger drain order, and the `naturalRegeneration` gamerule. Expose **`regenHungerPenaltyLevel`** via **Fabric server config** (no in-mod tick logging; use vanilla tooling / profilers if needed). **No player-health floor** for this feature. **NeoForge** is **out of scope** for this change (defer to a later release).

## Problem

Vanilla ties natural regeneration to hunger/saturation in a way that makes **saturation weak for healing** until the hunger bar is in a “good” state. The mod’s purpose is to make **saturation consistently valuable for healing** without asking players to **top off hunger first**.

## Goal

- Allow **natural-style** healing when **saturation > 0** **without requiring full hunger**, while still requiring **`getFoodLevel() > regenHungerPenaltyLevel`** (default **6** → **7+** food points — see **Config**).
- **Preserve vanilla regeneration behavior** everywhere else: same cadence, same exhaustion cost, same drain order (`FoodData` tick behavior).
- Feel like a **small, faithful extension** of vanilla—not a new healing pipeline.
- **Server-authoritative**: works with **vanilla clients** (no custom packets, no client-only code).

## Scope

- **Mod:** `saturation_regen` only.
- **Loader (implementation):** **Fabric** (`mods/saturation_regen/fabric` + shared `common` as needed).
- **Code path:** **Vanilla natural regeneration** driven by **food/saturation** in **`FoodData#tick(Player)`** (confirm exact control flow in **Minecraft 26.1.1** Mojang-mapped sources under Loom).
- **Configuration (Fabric, server):** **`regenHungerPenaltyLevel`** (integer). Load from a **server-side** config file under the usual Fabric `config/` convention; **no GUI** (file edit only). Defaults in code / `common` as needed.

## Config

| Key | Type | Default | Meaning |
|-----|------|---------|---------|
| **`regenHungerPenaltyLevel`** | int (typ. **0–19**) | **6** | Food-driven natural regen applies only when **`getFoodLevel() >`** this value (**not** `>=`). Default **6** ⇒ need **7+** food points — same strict **“above 6”** rule as vanilla **sprint**. |

### Why this name and default **6**

**`regenHungerPenaltyLevel`** ties **natural regen** to the familiar **hunger penalty** band (players often anchor on **6**). It does not spell out the comparator in the name; the rule in code is always **`getFoodLevel() > regenHungerPenaltyLevel`** — **strictly above** this level, not equal.

**Default `6`** matches how players talk about the **hunger penalty at 6**: in vanilla Java, **sprint** is allowed only when **`foodLevel > 6`**. So **“above 6 only”** means **7+** food points. Setting **`regenHungerPenaltyLevel = 6`** implements exactly that: **`getFoodLevel() > 6`**.

**Tuning:** Lower the value (e.g. **5**) to allow regen starting at **6+** food (`> 5`). Raise it (e.g. **7**) to require **8+** food — stricter.

### Naming note

Config JSON key is **`regenHungerPenaltyLevel`** (camelCase). Values are **Minecraft `FoodData` food points** (clamped **0–19** in code), not HP.

## Non-Goals

- A **second** regeneration loop or timer running alongside vanilla.
- Replacing or reimplementing **large chunks** of `FoodData#tick` (avoid copy-paste forks of the whole method).
- Changing **status-effect-driven** healing (`Regeneration`, `Instant Health`, poison/wither, resistance, absorption, etc.).
- **Client** mixins, **GUI** changes, or **custom networking**.
- **NeoForge** implementation (scaffold may stay empty until a **later** change).
- Any **minimum player health** / **HP floor** for natural regen (explicitly **out of scope**).

## Constraints

| Constraint | Detail |
|------------|--------|
| Implementation style | **Modify vanilla in-place** via **targeted mixin(s)** (redirect / modify variable / modify expression / slice—whatever fits the real control flow after inspection). |
| Healing system | **No new healing system**; only alter the **existing** natural regen branch in `FoodData`. |
| Preserve | **Vanilla regeneration timing** (`tickTimer` / cadence as in vanilla for this code path), **exhaustion** costs, **saturation → hunger** depletion order, **`naturalRegeneration`** gamerule. **Do not** change how much health is restored per proc, how the food tick timer advances, or **difficulty / Peaceful** behavior that vanilla already encodes in `FoodData#tick`—only widen **when** the existing natural-regen branch may run (see **Regen rate**, below). |
| Status effects | **Do not** touch effect-based healing paths; they must behave **exactly like vanilla**. Natural regen and effect healing may still **stack** when vanilla allows. |
| Min food rule | Food-driven natural regen (this mod’s path) applies only when **`getFoodLevel() > regenHungerPenaltyLevel`**. Default **6** ⇒ **7+** food (see **Config**). |
| Side | **Logical server only** (no client-side healing or prediction changes); gate mixin behavior so **integrated server / dedicated** behavior is correct. |
| Code quality | **Minimal**, **maintainable**, **diff-friendly**; prefer one well-placed injection over scattering logic. |

## Success Criteria

1. With **`naturalRegeneration` true**, a player with **saturation > 0**, **`getFoodLevel() > regenHungerPenaltyLevel`** (default **6** ⇒ **7+** food), and **hunger not full** receives **natural healing** on the **same schedule** as vanilla would when conditions would have allowed it (timing and exhaustion match vanilla’s `FoodData` behavior for that scenario).
2. **Exhaustion** and **saturation/hunger drain** match vanilla for equivalent states (no extra drains, no skipped vanilla drains).
3. **`naturalRegeneration` false** disables this healing like vanilla.
4. At **`getFoodLevel() <= regenHungerPenaltyLevel`** (default: **6** or below), **no** food-driven natural regeneration—verify border at **6 vs 7** food with saturation (strict **above 6**).
5. **Regeneration / Instant Health / Poison / Wither / Resistance / Absorption** behave **indistinguishably** from vanilla in manual spot checks (and do not require code changes in those systems).
6. **Dedicated server + vanilla client** shows correct behavior with **no client mod**.
7. **`just` build** (or mod’s Fabric `build`) succeeds; mixins apply without runtime errors on server.
8. Changing **`regenHungerPenaltyLevel`** in the config file behaves as expected after **restart** (reload not implemented).

## Regen rate (vanilla)

**Yes — this was considered.** The design **does not** introduce a custom heal-per-second or a second timer. It only changes **eligibility** (the hunger/saturation **gate**) for the **existing** natural-regeneration branch inside **`FoodData#tick`**. Everything that vanilla uses to determine **how often** that branch heals and **how much** per heal—including **food tick timer**, **per-tick increments**, and any **difficulty or Peaceful** logic already in that method—stays **Mojang’s logic**. If Mojang changes regen rate in a future version, this mod should pick that up automatically as long as the mixin still targets the same **condition**, not the heal/timer internals.

## Open Questions

- None critical. Optional: if **26.1.1** changes the **sprint vs food level** rule, update the **“Why default 6”** explanation text (defaults stay unless you decide otherwise).

## Decisions

| Topic | Decision |
|-------|----------|
| Primary injection site | **`FoodData#tick(ServerPlayer)`** — **`@ModifyConstant`** on the **`>= 20`** hunger check for the **fast** natural-regen branch: replace **`20`** with **`regenHungerPenaltyLevel + 1`** when **`food > penalty && food < 18`**, keep **`20`** at full hunger, use **`21`** for **18–19** food so the **slow** branch still applies (matches vanilla exhaustion for that band). No `@Inject` on `tick` in release (no tick logging). |
| Food gate | **`regenHungerPenaltyLevel`**, default **6** — predicate **`getFoodLevel() >` value** (default ⇒ **7+** food, matches vanilla sprint). |
| Player health | **No HP floor** — not in scope for v1. |
| Loader scope for v1 | **Fabric only** for implementation; **NeoForge later** (separate change). |
| Server gating | Mixin logic runs only for **server-side** `Player` (e.g. `ServerPlayer` / `!level().isClientSide()`), matching “server-side only” and avoiding client prediction issues. |

## Documentation Impact

- **`fabric.mod.json`** `description` reflects server-side behavior.
- Broader repo docs (**`Docs/technicalBrief.md`**, root README): propose under **document-change**; apply only with maintainer approval.
