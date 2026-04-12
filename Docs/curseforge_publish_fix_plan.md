# CurseForge Publish Fix Plan

## Goal
Make CurseForge publishing work for `xp_stream` on the current Gradle 9 toolchain without manually mapping Minecraft versions for each release.

## Why change the current setup
The current CurseForge wiring lives in the separate `publishFabric` and `publishNeoForge` projects. That structure works for Modrinth, but it prevents `io.github.themrmilchmann.curseforge-publish` from using its Fabric Loom and ModDevGradle integrations to infer loader and Minecraft version metadata.

That is why the current scripts ended up passing raw strings to `gameVersions`, which now fails on the current plugin/API expectations.

## Recommended approach
1. Upgrade `io.github.themrmilchmann.curseforge-publish` from `0.8.0` to `0.9.0`.
2. Use a mixed CurseForge strategy for `xp_stream`:
   - NeoForge: configure CurseForge in the actual loader project (`mods/xp_stream/neoforge/build.gradle`) and use the plugin's ModDevGradle integration.
   - Fabric: keep CurseForge publishing manual instead of using the plugin's implicit Fabric Loom integration.
3. Keep Modrinth publishing in the existing `publishFabric` / `publishNeoForge` projects.
4. For Fabric CurseForge publishing, derive proper `GameVersion` objects from the existing `minecraft_version` property instead of passing raw strings.
5. Update `justfile` so publish commands still represent one combined workflow, but route NeoForge and Fabric CurseForge through their appropriate task locations.
6. Update publishing docs so the troubleshooting paths match the new task locations and the mixed strategy is explicit.

## Expected benefits
- NeoForge CurseForge publishing can use the plugin's supported ModDevGradle integration cleanly.
- Fabric CurseForge publishing avoids the plugin's hard dependency on a `remapJar` task that does not exist in this unobfuscated 26.1.x setup.
- No extra hand-maintained Minecraft version field should be needed: Fabric can derive CurseForge `GameVersion` objects from `minecraft_version`.
- The repo keeps the current Modrinth structure, so the change stays focused.
- `just xp-stream-publish` remains the primary workflow.

## Scope
In scope:
- `xp_stream` CurseForge publishing
- `justfile` publish routing
- publishing docs that mention CurseForge task paths

Out of scope for this change:
- Modrinth behavior changes
- `saturation_regen` public release readiness
- broad compatibility tagging improvements for Modrinth / CurseForge

## Implementation steps
1. Add this plan document.
2. Move NeoForge CurseForge plugin/config into `mods/xp_stream/neoforge/build.gradle`.
3. Upgrade Fabric and NeoForge CurseForge plugin usage to `0.9.0`.
4. Keep Fabric CurseForge publishing outside the implicit Loom integration and implement manual `GameVersion` derivation from `minecraft_version`.
5. Strip obsolete CurseForge config from the old publish projects where appropriate, without disturbing Modrinth.
6. Update `justfile` combined publish ordering and CurseForge task paths.
7. Update `Docs/publishing.md` to show the new raw Gradle CurseForge commands.
8. Verify task discovery and build stability.
9. Re-run live CurseForge publishes one loader at a time:
   - NeoForge first
   - Fabric second

## Trial results (2026-04-11)
- NeoForge validated the loader-project integration approach:
  - moving CurseForge publishing into the NeoForge loader project worked
  - a live upload to CurseForge succeeded
- Fabric did not validate the same approach:
  - the plugin's Fabric Loom integration explicitly expects a `remapJar` task
  - this repo's unobfuscated 26.1.x Fabric setup does not provide `remapJar`
  - compatibility shims for `remapJar` caused Loom jar-task failures or upload-time file resolution failures
- Upstream source confirms the current Fabric integration hard-codes `from(tasks.named(\"remapJar\"))`
- Upstream source also confirms that CurseForge `GameVersion` objects can be derived mechanically from the Minecraft version, so Fabric does not need a second manually maintained version field

## Revised recommendation
Use a mixed strategy instead of a full loader-project migration:

1. Keep the NeoForge-side migration as the long-term target because it worked cleanly with ModDevGradle.
2. Do not force the same integration path for Fabric yet.
3. For Fabric:
   - keep publishing in the separate `publishFabric` project, or otherwise configure a manual CurseForge publication
   - derive proper CurseForge `GameVersion` objects from `minecraft_version`
   - use the real Fabric `jar` output directly instead of trying to recreate `remapJar`
4. Treat a full Fabric loader-project migration as a future follow-up only if upstream adds support for unobfuscated Loom setups without `remapJar`

This keeps the repo aligned with real behavior instead of assuming Fabric and NeoForge can share the exact same CurseForge wiring.

## Success criteria
- `:mods:xp_stream:neoforge:publishNeoForgePublicationToCurseForge` is available and succeeds.
- `:mods:xp_stream:fabric:publishFabricPublicationToCurseForge` is available and succeeds.
- `just xp-stream-publish` still represents the intended NeoForge-first, then Fabric workflow.
- No raw-string `gameVersions` wiring remains in the `xp_stream` publish path.
