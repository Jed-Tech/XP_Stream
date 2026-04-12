# Technical Brief

## Overview

This repository is a multi-mod Minecraft workspace.

Current mods:

- `xp_stream`
- `saturation_regen`

Current loaders:

- Fabric
- NeoForge

Product-facing docs live next to each mod. Workspace docs live at the repo root.

## Repo Layout

| Location | Purpose |
|----------|---------|
| `README.md` | Workspace overview and common commands |
| `CHANGELOG.md` | Workspace and tooling changes only |
| `mods/<mod_id>/README.md` | Player-facing mod documentation |
| `mods/<mod_id>/CHANGELOG.md` | Per-mod release history and release note source |
| `mods/<mod_id>/gradle.properties` | Per-mod metadata, version, and publish IDs |
| `Docs/publishing.md` | Publishing workflow and raw task paths |
| `Docs/release_checklist.md` | Operator release checklist |
| `Docs/github_release.md` | GitHub release automation |

## Build Model

- The repo uses one shared Gradle workspace.
- Root `gradle.properties` pins the active Minecraft and loader toolchain versions for the branch.
- Each mod keeps its own `mod_version` in `mods/<mod_id>/gradle.properties`.
- `just` is the preferred command entrypoint for normal work.

Common commands:

- `just build-all`
- `just xp-stream-build`
- `just xp-stream-fabric-client`
- `just xp-stream-neoforge-client`
- `just xp-stream-publish`
- `just github-release xp_stream`

## Publishing Model

Publishing is manual/local.

For `xp_stream`:

- Modrinth publishes one version per loader.
- CurseForge publishes one file per loader.
- GitHub release metadata is handled separately.

Current CurseForge strategy for `xp_stream`:

- NeoForge uses the plugin's ModDevGradle integration.
- Fabric uses a manual CurseForge publication that derives the Minecraft game-version metadata from `minecraft_version`.

This split exists because the current Fabric Loom integration in `curseforge-publish` expects a `remapJar` task, while this repo's unobfuscated Fabric setup does not use `remapJar`.

## Release Notes Model

- Before publishing or creating a GitHub release, add or update the matching `## [<mod_version>]` section in `mods/<mod_id>/CHANGELOG.md`.
- Modrinth and CurseForge use that per-mod changelog entry as release notes.
- `just github-release <mod>` uses the same per-mod changelog entry for the GitHub release body.
- Root `CHANGELOG.md` is not used for mod release notes.

## Mod Status

`xp_stream`

- current intended public release target
- supports Fabric and NeoForge

`saturation_regen`

- Fabric gameplay is implemented
- NeoForge is not an active public release target yet
- publishing wiring exists, but normal public publishing is not the current focus

## Notes

- Some CurseForge metadata may still require manual review after upload.
- Future Minecraft version bumps should continue to update the root pins first, then mod versions and changelogs as needed.
- See `Docs/curseforge_publish_fix_plan.md` for the reasoning behind the current mixed CurseForge setup.
