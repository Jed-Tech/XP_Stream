# saturation_regen — NeoForge parity plan (archived)

**Status:** Completed and archived.  
**Archived:** 2026-04-11  
**Target toolchain:** Minecraft `26.1.2`, NeoForge `26.1.2.x`, Java `25`.

---

## Original goal

Bring **`saturation_regen`** to **feature parity on NeoForge** with Fabric:

- Shared JSON config (`config/saturation_regen.json`) and plain-Java config in `mods/saturation_regen/common`
- Loader-local **`FoodDataMixin`** in both `fabric` and `neoforge` source sets
- Server-authoritative behavior: only vanilla natural regeneration paths, no client-only healing
- No publishing, version bumps, or publishing-config changes as part of this effort

---

## Delivered outcome

- **`SaturationRegenConfig`** lives in `common`; Fabric and NeoForge entrypoints load it from each loader’s config directory (NeoForge: `FMLPaths.CONFIGDIR`).
- **NeoForge:** `SaturationRegenNeoForgeMod` initializes config at startup; `saturation_regen.neoforge.mixins.json` registers `FoodDataMixin`; `neoforge.mods.toml` describes real behavior (not scaffold text).
- **Parity:** Same mixin logic and config semantics on Fabric and NeoForge; Gson available where needed for NeoForge.
- **Validation:** `just build-all`; dev clients for both loaders; gameplay sanity (including `xp_stream` and `saturation_regen` clients per project checks).

---

## Behavior note (after plan completion)

A follow-up design choice (**Approach 1**) unified cadence for **almost-full** hunger:

- The widened fast-branch band is **`food > regenHungerPenaltyLevel` and `food < 20`** (default penalty `6` → hunger **7–19**).
- Hunger **18–19** therefore use the same **fast** natural-regen branch as other widened levels when **`saturation > 0`**, instead of vanilla’s slower near-full band.

Original plan text that assumed **18–19** stayed on vanilla slow regen is **obsolete**; see **`mods/saturation_regen/README.md`** and **`mods/saturation_regen/CHANGELOG.md`** for user-facing wording.

---

## Acceptance criteria (final)

| # | Criterion | Result |
|---|-----------|--------|
| 1 | Builds on Fabric and NeoForge | Met |
| 2 | NeoForge not scaffold-only | Met |
| 3 | Same `saturation_regen.json` semantics on both loaders | Met |
| 4 | Same natural-regeneration **logic** on both loaders | Met (including post–Approach 1 18–19 behavior) |
| 5 | Server-authoritative; no client-only healing | Met |
| 6 | No required publish/version/publishing-config changes for the implementation | Met (per original non-goals) |

---

## Maintenance

- **Mixin anchor:** `@ModifyConstant(method = "tick", intValue = 20)` on `FoodData#tick(ServerPlayer)` — re-verify after major Minecraft updates if natural regen control flow changes.
- **Config load order:** Config must load before gameplay uses mixin read of `SaturationRegenConfig.get()` (constructor-time load on both loaders).

---

## Documentation follow-up (done)

- **`mods/saturation_regen/README.md`** — loaders and server-side wording updated for Fabric + NeoForge.
- **Root `README.md`** — mod list clarifies both mods ship on Fabric and NeoForge (see table).

---

## Historical detail

The long-form phased plan (Phases 1–3, risks, optional paths) lived at `Docs/Saturation_regen_Neoforge.md` before archive. For the pre-archive text, use **git history** on that path.

---

## Summary

NeoForge support was implemented via shared config in **`common`**, loader-local mixins, NeoForge bootstrap and resources, then validated with full-workspace builds and both loaders. Post-completion, **18–19** hunger was aligned with the widened **fast** branch for consistent pacing; documentation and changelog record that choice.
