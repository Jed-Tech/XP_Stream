# Broad Minecraft family compatibility — policy template

**How to use:** For each new Minecraft release line, substitute placeholders below (example values for **26.1** are in parentheses). Keep this doc as the standing policy; pair it with a **per-version** checklist from `Docs/NeoForge_UpdatePlan_Template.md` or a release-specific plan (e.g. `Docs/26.1_final.md`).

| Placeholder | Meaning | Example (26.1) |
|-------------|---------|----------------|
| **`[MC_FAMILY]`** | Game version prefix used in NeoForge’s broad Minecraft range | `26.1` → range lower bound `26.1-` |
| **`[FABRIC_MC_FLOOR]`** | Earliest Minecraft version string you accept on Fabric (semver string in `fabric.mod.json`) | `>=26.1-alpha.2` |
| **`[MOD]`** | Mod id under `mods/` | `xp_stream`, `saturation_regen` |

---

## Goal

The **whole mod** (common code + Fabric + NeoForge jars) should declare **broad compatibility** with the entire **`[MC_FAMILY]`** line—snapshots, pre-releases, RCs, GA, and typical hotfixes—without hard-coding a single snapshot-style lower bound in NeoForge metadata.

---

## Key policy

1. **NeoForge — `minecraft` dependency:** use an **open-ended family** lower bound, not a snapshot-only floor:  
   **`versionRange = "[[MC_FAMILY]-,)"`**  
   Example: `[MC_FAMILY]` = `26.1` → `"[26.1-,)"`.

2. **NeoForge — `neoforge` dependency:** use a **broad runtime floor** in `neoforge.mods.toml`, **not** the same string as the ModDevGradle compile pin. In this repo, **`versionRange = "[${neoforge_dependency_minimum},)"`** with **`neoforge_dependency_minimum`** in root **`gradle.properties`** (e.g. `26.1.0` for the 26.1 line). **`neoforge_version`** stays the **exact** NeoForge you compile against; it must not be reused for `[[dependencies]]` on `neoforge` or players on older 26.1.x NeoForge builds will be rejected.

3. **Fabric — `minecraft` dependency:** use a **low, stable floor** (`[FABRIC_MC_FLOOR]`) so pre/RC/GA builds stay accepted unless you deliberately tighten after testing. Prefer the simplest range Fabric accepts; avoid over-fitting one pre-release label.

4. **Common module:** loader-agnostic; **no** loader metadata. Version gating lives only in Fabric/NeoForge modules.

---

## Where to apply it (this repo)

For **each** mod under `mods/<mod_id>/`:

| Loader | File |
|--------|------|
| NeoForge | `mods/<mod_id>/neoforge/src/main/resources/META-INF/neoforge.mods.toml` |
| Fabric | `mods/<mod_id>/fabric/src/main/resources/fabric.mod.json` |
| Common | `mods/<mod_id>/common/` — logic only |

---

## Metadata checklist (per release line)

### NeoForge: `minecraft` range

- Set **`modId = "minecraft"`** to **`versionRange = "[[MC_FAMILY]-,)"`** (adjust `[MC_FAMILY]-` only if NeoForge’s version ordering for that cycle requires a different prefix—verify with a failing loader if needed).
- Keep **`neoforge`** on **`"[${neoforge_dependency_minimum},)"`** (broad floor; see key policy §2).

### Fabric: `depends.minecraft`

- Set **`[FABRIC_MC_FLOOR]`** (e.g. `>=26.1-alpha.2` for 26.1). Change only if real installs reject valid game builds.

### Common

- No metadata changes for this policy.

---

## Development pins vs player-facing ranges

- **`gradle.properties`** (`minecraft_version`, `neoforge_version`, Fabric Loom, loader, etc.): pin to the **newest resolvable** game + loader pair you need for **compile and CI** (often a specific pre, RC, or GA id). This is **not** the same as the broad `[MC_FAMILY]-` range in `mods.toml`.
- **Loader metadata** (above): stays **broad** so players on nearby builds of the same family are not rejected unnecessarily.

When the game GA’s, update pins toward GA and re-smoke-test; **do not** narrow `minecraft` in `neoforge.mods.toml` unless you have a concrete incompatibility.

---

## Verification matrix (run for each new `[MC_FAMILY]` cycle)

Run the **real game** (client or server as appropriate) so dependency resolution and mixins match production.

1. **Early cycle:** NeoForge + Fabric on an **early** snapshot/pre for `[MC_FAMILY]` — mod loads, core behavior OK.
2. **Mid cycle:** Repeat on a **newer** pre or RC — still loads (confirms broad range behavior).
3. **GA (or late RC):** NeoForge + Fabric on **GA** (or final RC) — loads, core behavior OK.
4. **Regression:** After any metadata or mixin change, spot-check prior step (2) or (3) again.

Replace “core behavior” with your mod’s smoke test (e.g. XP_Stream burst pickup).

---

## Build steps (this monorepo)

From repo root (adjust mod id):

- `.\gradlew :mods:[MOD]:neoforge:build` / `runClient` as needed  
- `.\gradlew :mods:[MOD]:fabric:build` / `runClient` as needed  

Use `Docs/NeoForge_UpdatePlan_Template.md` for NeoForge version discovery and toolchain notes per release.

---

## Risks and fallback

- **Risk:** NeoForge/Fabric may not order snapshot/pre/RC/GA strings the way you expect in all tools.
- **Fallback:** If a legitimate game build is rejected, narrow or shift the **`minecraft`** range using the **exact** version id from that NeoForge/Fabric run, then keep the upper end open; document the exception in your per-release plan.

---

## Acceptance criteria (per `[MC_FAMILY]` adoption)

Policy is satisfied for that line when:

- NeoForge: mod loads on **at least one** early and **at least one** late/GA-class build of `[MC_FAMILY]` without Minecraft dependency errors.
- Fabric: same, using your chosen `[FABRIC_MC_FLOOR]`.
- Core mod behavior passes smoke tests on those targets.
