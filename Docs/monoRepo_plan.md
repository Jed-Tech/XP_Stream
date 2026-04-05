# Minecraft Multi-Mod Monorepo Plan (Fabric + NeoForge)

This document is the single plan we follow to migrate XP_Stream into a multi-mod monorepo and add a second mod. Execute phases in order; do not combine structural migration with multi-mod expansion.

---

## Goals

- Support **1.21.1** and **1.21.11** (critical fixes only) and **26.1 snapshots → latest stable**.
- Minimize upgrade effort per Minecraft version.
- Allow **independent hotfixes per mod** (bump and publish one mod only).
- Keep publishing consistent and safe (public repo; tokens only in env/CI).
- Keep loaders thin and logic centralized in `common/`.

---

## Branch Strategy (Critical)

**Branches = Minecraft lines, not mods.**

| Branch      | Purpose                          |
|------------|-----------------------------------|
| `main`     | Newest MC (currently 26.1 snapshots) |
| `mc/1.21.11` | Emergency-only maintenance       |
| `mc/1.21.1`  | Emergency-only maintenance       |

**Rules:**

- **New features and refactors** → newest branch only (`main`).
- **Critical fixes** → fix on `main` first, then cherry-pick back to maintenance branches if needed.
- **Cherry-picks and compat:** Keep compat surfaces tiny and stable. If a cherry-pick conflicts due to compat differences, re-implement the **smallest equivalent fix** on that branch—do not port refactors. This keeps emergency-only lines from turning into merge hell.
- Older branches publish only when something breaks badly; publishing from those branches must remain repeatable and safe.

---

## Architecture Summary

- **Monorepo:** All mods under `mods/<mod_id>/` with `common/`, `fabric/`, `neoforge/`.
- **Explicit mod list** in `settings.gradle` (no auto-discovery): maintain a single `modIds = ["xp_stream", "saturation_regen", ...]` list and derive `include(...)` from it so the list is not repeated in multiple places and stays less error-prone.
- **Per-branch versions** in root `gradle.properties` (no `libs.versions.toml` for now).
- **Per-mod metadata** in `mods/<mod_id>/gradle.properties` (see Config split below).
- **Group** = repo-wide from root (`maven_group`). **Version** = per-mod from per-mod properties (set by root script).
- **Publishing:** One publish task per mod (primary); optional `publishAll` that depends on each. Publish = Modrinth + CurseForge, uploading the **loader-built JARs** (Fabric and NeoForge artifacts), not just Maven components. Maven publishing remains separate/optional. Tokens via env/CI only.
- **No cross-mod dependencies.** Defer `build-logic/` and convention plugins until 3–5 mods or duplication hurts.
- **Compat:** Each mod has `common/.../compat/` for snapshot-to-snapshot or loader-specific logic; core logic calls compat helpers so breakage is isolated.
- **Docs layout:** Root **`README.md`** describes the workspace. Each mod’s player-facing copy and release history live in **`mods/<mod_id>/README.md`** and **`mods/<mod_id>/CHANGELOG.md`**. Root **`CHANGELOG.md`** records repo-wide changes only (not per-mod version history).

---

## Config Split

### Per-branch (root) — `gradle.properties` at repo root

- `minecraft_version`
- `fabric_loader_version`, `fabric_loom_version`, `fabric_api_version`
- `neoforge_version`, `moddevgradle_version`
- Java toolchain (e.g. `org.gradle.java.home` or toolchain version)
- `maven_group` (repo-wide)
- `org.gradle.jvmargs` (optional)

**Rule:** Branch upgrade = update these values + fix compile errors.

### Per-mod — `mods/<mod_id>/gradle.properties`

- `mod_id`
- `mod_name`
- `archives_base_name`
- `mod_version`
- `modrinth_project_id` (for publishing)
- `curseforge_project_id` (for publishing)

**Rule:** Root build config loads each `mods/<id>/gradle.properties` once per mod and applies those properties to `:mods:<id>:common`, `:mods:<id>:fabric`, and `:mods:<id>:neoforge` **early** (e.g. before subproject build scripts run), so every subproject can read `mod_id`, `mod_name`, and `version`. Version is **not** set in `allprojects`; it comes from this loader.

---

## Versioning Model

- **Repo:** May use tags as a release train (optional).
- **Each mod** has its own `mod_version` in its `mods/<id>/gradle.properties`.
- **Normal release:** Bump all mod versions; run `publishAll` (or each mod’s publish task).
- **One-mod hotfix:** Bump only that mod’s `mod_version`, run that mod’s publish task only. Other mods are not re-uploaded.

---

## Migration Phases (Follow in Order)

### Phase 1: Structural migration — XP_Stream under `mods/xp_stream/` (purely structural)

**Goal:** Move current layout into `mods/xp_stream/` and wire Gradle so the project still builds. No per-mod `gradle.properties` and no root loader script yet; root continues to hold mod metadata for this phase.

1. **Create directory layout**
   - Create `mods/xp_stream/common/`, `mods/xp_stream/fabric/`, `mods/xp_stream/neoforge/`.
   - Move contents of `common/` → `mods/xp_stream/common/`, `fabric/` → `mods/xp_stream/fabric/`, `neoforge/` → `mods/xp_stream/neoforge/`.
   - Remove now-empty `common/`, `fabric/`, `neoforge/` at root.

2. **Update `settings.gradle`**
   - Set `rootProject.name` (e.g. keep `"XP_Stream"` or switch to repo name; your choice).
   - Define an explicit **mod list** and derive includes from it: e.g. `modIds = ["xp_stream"]` and then `include(modIds.collect { id -> [":mods:${id}:common", ":mods:${id}:fabric", ":mods:${id}:neoforge"] }.flatten())` (or equivalent), using **canonical Gradle project paths (leading `:`)**. This way the mod list lives in one place and is not repeated.
   - Ensure project directories map correctly (Gradle default: path under root from project path).
   - **Loom / pluginManagement:** Keep the Fabric Loom (and any other loader plugin) version pinned as a **literal in `settings.gradle` per branch** for now; do not move to a version catalog or shared property yet.

3. **Update root `build.gradle` (minimal wire only)**
   - Root `gradle.properties` stays unchanged (still holds `mod_version`, `mod_id`, `mod_name`, `archives_base_name`).
   - Ensure **only** subprojects under `:mods:` (e.g. `:mods:xp_stream:common`, `:mods:xp_stream:fabric`, `:mods:xp_stream:neoforge`) receive `group`, `version`, and mod identity from the **root project** so their build scripts can read `project.mod_id`, `project.version`, etc.—e.g. in `allprojects` or `subprojects`, set these from `rootProject.findProperty(...)` only when `project.path.startsWith(":mods:")`. Do **not** apply this to future root-level or non-mod utility modules, if any. Do **not** add per-mod file loading or the full loader script—that belongs in Phase 2.

4. **Update loader module build files**
   - In `mods/xp_stream/fabric/build.gradle` and `mods/xp_stream/neoforge/build.gradle`:
     - Change `project(":common")` → `project(":mods:xp_stream:common")`.
   - Leave plugin application and dependency blocks otherwise the same (explicit per-mod build files; no build-logic yet).

5. **Verify**
   - Run `./gradlew :mods:xp_stream:fabric:build` and `./gradlew :mods:xp_stream:neoforge:build` (or equivalent). Fix any path or property reference errors until both succeed.

---

### Phase 2: Per-mod metadata + root loader script — root no longer holds mod identity

**Goal:** Introduce per-mod `gradle.properties` and the root script that loads them. Root `gradle.properties` then holds only branch-scoped versions and repo-wide `maven_group`. All mod identity and version live in `mods/<id>/gradle.properties`.

1. **Create per-mod properties and root loader script**
   - Create `mods/xp_stream/gradle.properties` with: `mod_id`, `mod_name`, `archives_base_name`, `mod_version`, and placeholders for `modrinth_project_id`, `curseforge_project_id` (if not yet used).
   - In root `build.gradle` (or `settings.gradle`), add the **root loader script**: for each mod, read `mods/<id>/gradle.properties` and apply those properties to `:mods:<id>:common`, `:mods:<id>:fabric`, and `:mods:<id>:neoforge`. Set `version` and mod identity on those projects so that when their `build.gradle` is evaluated, `project.mod_id`, `project.mod_name`, `project.version`, etc. are already set.
   - **Timing:** Run this script **early** (e.g. in `gradle.beforeProject` or in a `subprojects` block that runs before subproject build scripts), so every mod subproject can read `mod_id`, `mod_name`, and `version` without relying on root.

2. **Strip mod metadata from root `gradle.properties`**
   - Remove: `mod_version`, `mod_id`, `mod_name`, `archives_base_name`.
   - Keep: `maven_group`, all version and toolchain properties (MC, Fabric, NeoForge, Java, etc.).

3. **Update root `build.gradle`**
   - Remove `version = project.mod_version` from `allprojects`; keep `group = project.maven_group`. Version and mod identity for mod subprojects now come only from the per-mod loader script.

4. **Verify**
   - Build both loaders again; run client once if possible. Confirm JAR names and manifest/mod metadata (e.g. `fabric.mod.json`, `neoforge.mods.toml`) show correct mod name and version from per-mod properties.

---

### Phase 3: Build parity and sanity check

**Goal:** Confirm behavior matches pre-migration (same artifacts, same run behavior).

1. Run full build: `./gradlew clean build` (or build each mod’s fabric and neoforge).
2. Run Fabric and NeoForge clients; quick in-game check (e.g. XP_Stream behavior).
3. Optionally diff JAR contents or manifest vs a pre-migration build if you have one.

**Sanity notes (verify during Phase 3, not blockers):**
- **Run configs:** Confirm dev run configs still work for both loaders (Fabric dev run + NeoForge client/server runs), not just `jar`/build.
- **JAR naming/output paths:** Confirm the JAR names and output paths are what you expect, especially since loader JARs include common output (e.g. `mods/<id>/fabric/build/libs/<name>-fabric-<mc_version>.jar`).

---

### Phase 4: Standardized publishing (per-mod + optional publishAll)

**Goal:** One publish task per mod (primary); optional `publishAll`; Modrinth + CurseForge per mod; tokens from env/CI only.

1. **Per-mod publish task**
   - For each mod, define a task (e.g. `publishXp_stream` or `publishMod` scoped to that mod) that:
     - Publishes to **Modrinth** and **CurseForge** using that mod’s `modrinth_project_id` and `curseforge_project_id` from `mods/<id>/gradle.properties`.
     - **Uploads the loader-built JARs:** Modrinth and CurseForge tasks must upload the **actual mod artifacts**—the Fabric and NeoForge JARs produced by the loader builds (e.g. `xp_stream-fabric-<mc_version>.jar`, `xp_stream-neoforge-<mc_version>.jar`), not just Maven publication components.
     - Reads `MODRINTH_TOKEN` and `CURSEFORGE_TOKEN` from environment (or CI secrets); never store tokens in repo.
   - Plugin decision:
     - Use `com.modrinth.minotaur` for Modrinth publishing.
     - Use `io.github.themrmilchmann.curseforge-publish` for CurseForge publishing.
     - Do not plan around `CurseGradle` unless the chosen CurseForge plugin later proves insufficient.
   - Maven publishing can remain as currently set up (separate/optional).

2. **Optional `publishAll`**
   - Add a root (or root-level) task `publishAll` that depends on each mod’s publish task. Useful for a full release train.

3. **Document**
   - In README or `Docs/`, document the `just` workflow as the primary entrypoint for publishing, with raw Gradle task paths as a lower-level fallback.
   - In README or `Docs/`, document how to run “publish XP_Stream only” and “publish all mods,” and that tokens must be set in env or CI.

---

### Phase 5: Add second mod — scaffold `saturation_regen`

**Goal:** Add a new mod under `mods/saturation_regen/` without changing XP_Stream behavior. New scaffold only (no “move existing code”).

1. **Scaffold layout**
   - Create `mods/saturation_regen/common/`, `mods/saturation_regen/fabric/`, `mods/saturation_regen/neoforge/`.
   - Create `mods/saturation_regen/gradle.properties` with: `mod_id`, `mod_name`, `archives_base_name`, `mod_version`, and placeholder/empty `modrinth_project_id`, `curseforge_project_id`.

2. **Register in root script and settings**
   - Add `"saturation_regen"` to the `modIds` list in `settings.gradle` (includes are derived from `modIds`, so no separate `include(...)` lines needed).
   - Extend the root script that loads per-mod properties to also load `mods/saturation_regen/gradle.properties` and apply it to `:mods:saturation_regen:common|fabric|neoforge`.

3. **Copy and adapt loader build files**
   - Copy `mods/xp_stream/fabric/build.gradle` → `mods/saturation_regen/fabric/build.gradle` and replace `:mods:xp_stream:common` with `:mods:saturation_regen:common` and any mod-id references (they should already use `project.mod_id` etc. from the per-mod properties).
   - Same for `mods/xp_stream/neoforge/build.gradle` → `mods/saturation_regen/neoforge/build.gradle`.
   - Add minimal `common/build.gradle` for `saturation_regen` (e.g. same as xp_stream common: Java only, no loader deps).
   - Add minimal source sets (e.g. empty or stub main class) and loader metadata (`fabric.mod.json`, `neoforge.mods.toml`) so the project compiles and can be run.

4. **Verify**
   - `./gradlew :mods:saturation_regen:fabric:build` and `:mods:saturation_regen:neoforge:build` succeed. Optionally run client with both mods.

---

## Project Paths Reference

| Mod             | Common              | Fabric               | NeoForge              |
|-----------------|---------------------|----------------------|------------------------|
| xp_stream       | `:mods:xp_stream:common`   | `:mods:xp_stream:fabric`   | `:mods:xp_stream:neoforge`   |
| saturation_regen| `:mods:saturation_regen:common` | `:mods:saturation_regen:fabric` | `:mods:saturation_regen:neoforge` |

---

## Deferred / Later

- **Version catalog (`libs.versions.toml`):** Revisit only if dependency sprawl becomes annoying.
- **`build-logic/` and convention plugins:** Introduce when you have 3–5 mods and duplication hurts; consider moving per-mod properties loading there then.
- **Cross-mod shared code:** Not planned; add only if it clearly hurts (then extract to a shared module or convention).

---

## Checklist (high level)

- [x] Phase 1: Structural migration — XP_Stream under `mods/xp_stream/` (canonical paths, minimal root wire), builds pass.
- [x] Phase 2: Per-mod gradle.properties + root loader script; root stripped of mod metadata; per-mod props applied early.
- [x] Phase 3: Build parity and sanity check (build + run both loaders).
- [x] Phase 4: Per-mod publish tasks (Modrinth + CurseForge upload loader-built JARs), optional publishAll, tokens from env/CI.
- [x] Phase 5: Scaffold `mods/saturation_regen/`, add to settings and root script, copy/adapt build files, verify builds.
