# Publishing (Modrinth + CurseForge)

Publishing uses one Modrinth version per loader and one CurseForge file per loader for each mod release. A mod keeps a single `mod_version`, and the published version names include the loader so users can tell the files apart.

## Workflow

Use `just` for the normal publishing workflow. Raw Gradle task paths are still available for lower-level or troubleshooting use.

Common commands:

- Check publish readiness for `xp_stream`: `just xp-stream-publish-check`
- Publish `xp_stream`: `just xp-stream-publish`
- Publish `saturation_regen`: `just saturation-regen-publish`
- Check publish readiness with the parameterized recipe: `just publish-check xp_stream`
- Publish one mod with the parameterized recipe: `just publish xp_stream`
- Publish one loader only: `just publish xp_stream fabric`

Lower-level Gradle tasks:

- Publish one mod: `.\gradlew publishXp_Stream`
- Publish all mods: `.\gradlew publishAll`
- Fabric to Modrinth only: `.\gradlew :mods:xp_stream:publishFabric:modrinth`
- Fabric to CurseForge only: `.\gradlew :mods:xp_stream:publishFabric:publishToCurseForge`
- NeoForge to Modrinth only: `.\gradlew :mods:xp_stream:publishNeoForge:modrinth`
- NeoForge to CurseForge only: `.\gradlew :mods:xp_stream:publishNeoForge:publishToCurseForge`

## Release notes (changelogs)

Modrinth and CurseForge take release notes from **`mods/<mod_id>/CHANGELOG.md`**. Gradle pulls the `## [<mod_version>]` block that matches **`mod_version`** in `mods/<mod_id>/gradle.properties`. The root **`CHANGELOG.md`** is for **repository / tooling** changes only, not mod releases. Root **`README.md`** links each mod’s user-facing README under **`mods/<mod_id>/README.md`**.

## Plugin Decisions

These choices are intentional and are the current standard for this repo:

- Modrinth plugin: `com.modrinth.minotaur`
- CurseForge plugin: `io.github.themrmilchmann.curseforge-publish`

Why these plugins:

- They upload the actual loader-built JARs, not just Maven publications.
- They fit the per-mod and per-loader task model used by this repo.
- They work with tokens supplied through environment variables or CI secrets.

Not selected right now:

- `CurseGradle` is not part of the current plan or implementation unless the current CurseForge plugin later proves limiting.

## Tokens (env/CI only)

- `MODRINTH_TOKEN`: Modrinth API token, created at [modrinth.com/settings/account](https://modrinth.com/settings/account)
- `CURSEFORGE_TOKEN`: CurseForge API token, created at [curseforge.com/account/api-tokens](https://curseforge.com/account/api-tokens)

Never commit tokens. Set them in the environment or in CI secrets.

## Per-Mod Project IDs

Each mod sets its platform IDs in `mods/<mod_id>/gradle.properties`:

- `modrinth_project_id`: Modrinth project ID or slug
- `curseforge_project_id`: CurseForge project ID

If either value is empty, that platform is skipped for that mod.

## What Gets Uploaded

Publishing uploads the loader-built mod artifacts:

- Fabric JAR from `:mods:<mod_id>:fabric`
- NeoForge JAR from `:mods:<mod_id>:neoforge`

This is separate from Maven publishing. Maven publishing can still exist for local or repository use, but it is not the path used for Modrinth or CurseForge uploads.

## Version Names

Modrinth and CurseForge version names are set per loader, for example:

- `XP_Stream 1.1.1 (Fabric) for 26.1.1`
- `XP_Stream 1.1.1 (NeoForge) for 26.1.1`

Each Modrinth version should have exactly one file: the Fabric JAR or the NeoForge JAR for that loader-specific release entry.

## Preflight Check

Before a real publish, run the preflight check:

- `just xp-stream-publish-check`
- `just publish-check xp_stream`
- `just publish-check xp_stream fabric`

The preflight check verifies:

- the target mod exists
- the loader argument is valid
- `mod_id`, `mod_name`, and `mod_version` are present
- `modrinth_project_id` and `curseforge_project_id` are present
- `MODRINTH_TOKEN` and `CURSEFORGE_TOKEN` are set

It does not upload anything.

## Partial Failure Handling

If a publish only partially succeeds:

- do not immediately rerun the full publish command blindly
- identify which platform and loader already succeeded
- retry only the missing platform or loader task when possible
- verify whether the platform allows retrying the same version number cleanly
- if CurseForge fails because of game version validation, fix that mapping issue before retrying CurseForge

## Notes

- Publishing is currently a manual/local workflow in this repo. There is no repo-managed GitHub Actions publishing pipeline yet.
- CurseForge may not recognize some snapshot game versions. If CurseForge rejects a snapshot publish because of game version validation, publish that snapshot build to Modrinth only or adjust the CurseForge game version mapping before retrying.
- Use `Docs/release_checklist.md` as the step-by-step operator flow for version bump, publish, and GitHub release work.
