# NeoForge 26.1 Snapshot [N] Update Plan — Template

**How to use:** Copy this file to `Docs/26.1_snapshot[N]_neoforge_UpdatePlan.md` (e.g. `26.1_snapshot8_neoforge_UpdatePlan.md`). Replace placeholders `[N]`, `[NEOFORGE_VERSION]`, `[MC_VERSION]`, and any `[PREVIOUS_...]` with the target and current values. Then follow the phases.

**Running Gradle:** Run all `.\gradlew` (or `./gradlew`) commands yourself in your own terminal. Do not have Cursor/AI run them—the AI terminal often fails or misbehaves with Gradle, so you should execute these commands locally to avoid errors.

---

## Overview

This document outlines the plan to align XP_Stream’s **NeoForge** build with **NeoForge [NEOFORGE_VERSION]**, which bundles **Minecraft 26.1 Snapshot [N]**. NeoForge ships one version per Minecraft snapshot; supporting “their bundle” means building and testing against this exact pair.

**Target NeoForge version:** `[NEOFORGE_VERSION]`  
**Target Minecraft version (for NeoForge bundle):** `[MC_VERSION]` (e.g. `26.1-snapshot-8`)  
**Previous NeoForge version:** `[PREVIOUS_NEOFORGE_VERSION]`  
**Previous Minecraft version (in gradle.properties):** `[PREVIOUS_MC_VERSION]`  
**Java requirement:** Java 25 (already configured)

---

## Why “Support Their Bundle”

- Each NeoForge 26.1 build is tied to one Minecraft snapshot.
- Building the mod against the **same** Minecraft version that NeoForge bundles avoids version mismatches.
- **Get the current version:** The most up-to-date list of NeoForge builds is the Maven metadata XML. Check it for `<latest>` and `<release>`: [neoforge maven-metadata.xml](https://maven.neoforged.net/releases/net/neoforged/neoforge/maven-metadata.xml). Use that version; moving to the latest often avoids patch failures from older snapshot artifacts.

---

## Compatibility Assessment

- Skim the [Minecraft 26.1 Snapshot [N]](https://www.minecraft.net/en-us/article/minecraft-26-1-snapshot-[N]) (or feedback.minecraft.net) changelog for changes to:
  - `ExperienceOrb`, `playerTouch`, player pickup delay, entity collision
  - `ServerLevel`, `Player`, `AABB`, `getEntitiesOfClass`
- If no such changes: **version/config-only** upgrade. If there are changes: note them and plan code/target updates.

**Conclusion:** [ No code changes expected | Code changes needed: … ]

---

## Step-by-Step Plan

### Phase 1: Version verification

- [ ] Read NeoForge Maven `maven-metadata.xml` (or directory listing) for `<latest>` / `<release>` and set `[NEOFORGE_VERSION]` and `[MC_VERSION]` (e.g. `26.1-snapshot-[N]`) to match.
- [ ] Confirm ModDevGradle version (e.g. 2.0.140 or newer) supports the target snapshot.
- [ ] Optionally skim Minecraft Snapshot [N] release notes for entity/collision changes.

### Phase 2: Update Gradle configuration

- [ ] **`gradle.properties`**
  - Set `neoforge_version=[NEOFORGE_VERSION]`.
  - Set `minecraft_version=[MC_VERSION]`.
  - Leave Fabric-related properties and Java 25 as-is. (Fabric’s `fabric.mod.json` version range keeps support for multiple 26.x snapshots.)
- [ ] **`neoforge.mods.toml`** — Usually no change; version range `[26.1-snapshot-1,)` typically suffices. Tighten only if desired.

### Phase 3: Build and test (NeoForge only)

- [ ] **Clean toolchain** (recommended to avoid patch/cache issues):
  1. `.\gradlew --stop`
  2. Clear NeoForm cache (PowerShell): `Remove-Item -Recurse -Force "$env:USERPROFILE\.gradle\caches\neoformruntime" -ErrorAction SilentlyContinue`
  3. `.\gradlew clean :neoforge:build --refresh-dependencies`
- [ ] If build fails at `applyNeoforgePatches`: try moving to the **latest** NeoForge version from Maven (newer snapshot artifacts often have fixed patches). Then repeat clean toolchain + build.
- [ ] Run client: `.\gradlew :neoforge:runClient`
- [ ] Confirm mod loads, no mixin errors, burst pickup works in-game.

### Phase 4: Fabric (optional / separate)

- [ ] With the same `minecraft_version`, run `.\gradlew :fabric:build` and `.\gradlew :fabric:runClient` when ready. Fabric version range in `fabric.mod.json` keeps compatibility with other 26.x snapshots.

### Phase 5: Documentation and release

- [ ] Update README / compatibility table with NeoForge version and MC snapshot when releasing.
- [ ] Add CHANGELOG entry for NeoForge Snapshot [N] support.
- [ ] Bump `mod_version` in `gradle.properties` if releasing.

---

## Summary of intended changes

| File / item         | Current                    | After update        |
|---------------------|----------------------------|---------------------|
| `gradle.properties` | `minecraft_version=...`, `neoforge_version=...` | `minecraft_version=[MC_VERSION]`, `neoforge_version=[NEOFORGE_VERSION]` |
| `neoforge.mods.toml`| (optional)                 | No change typical   |
| Code                | —                          | No changes expected (or list if any) |

---

## Rollback

If something breaks:

1. Revert `gradle.properties`: restore `minecraft_version` and `neoforge_version` to the previous working pair.
2. Run `.\gradlew clean :neoforge:build` to confirm.

---

## References

- **NeoForge builds (most up-to-date):** [neoforge maven-metadata.xml](https://maven.neoforged.net/releases/net/neoforged/neoforge/maven-metadata.xml) — use `<latest>` / `<release>` for the current version
- [NeoForge 26.1 Snapshots](https://neoforged.net/news/26.1snapshots/)
- [NeoForge Maven (neoforge) directory](https://maven.neoforged.net/releases/net/neoforged/neoforge/) — version list
- Minecraft Snapshot release notes: [feedback.minecraft.net](https://feedback.minecraft.net/hc/en-us/sections/360002267532-Snapshot-Information-and-Changelogs) or minecraft.net articles
- Prior plans: `Docs/26.1_snapshot7_neoforge_UpdatePlan.md`, `Docs/26.1_Snapshot3_UpdatePlan.md`, etc.

---

**Plan created:** [DATE]  
**Status:** Not started

---

## Run in your terminal

Run these **yourself** in your own terminal (not Cursor’s AI terminal) from the project root. Do not have the AI run `.\gradlew`—run the commands locally to avoid terminal errors.

**PowerShell (Windows):**

1. Clean toolchain and build:
   ```powershell
   cd "c:\Users\jedde\GithubRepos\Minecraft\XP_Stream"
   .\gradlew --stop
   Remove-Item -Recurse -Force "$env:USERPROFILE\.gradle\caches\neoformruntime" -ErrorAction SilentlyContinue
   .\gradlew clean :neoforge:build --refresh-dependencies
   ```

2. Run client to verify:
   ```powershell
   .\gradlew :neoforge:runClient
   ```

**Note:** Use `Remove-Item` in PowerShell; `rmdir /s /q` is cmd.exe syntax and will fail in PowerShell.

---

## Implementation status

(After completing the update, fill in: versions set, build result, client test result, any issues and fixes.)

- **Version:** …
- **Build:** …
- **Client:** …
- **Fabric:** …
