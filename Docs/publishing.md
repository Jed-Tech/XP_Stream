# Publishing (Modrinth + CurseForge)

Publishing uses one Modrinth version per loader and one CurseForge file per loader for each mod release. A mod keeps a single `mod_version`, and the published version names include the loader so users can tell the files apart.

## Workflow

Use `just` for the normal publishing workflow. Raw Gradle task paths are still available for lower-level or troubleshooting use.

Common commands:

- Check publish readiness for `xp_stream`: `just xp-stream-publish-check`
- Publish `xp_stream`: `just xp-stream-publish`
- Publish `saturation_regen`: `just saturation-regen-publish`
- Check publish readiness with the parameterized recipe: `just publish-check xp_stream`
- Publish one mod with **both** loaders (default): `just publish xp_stream` (same as `just publish xp_stream all`)
- Publish **one** loader only: `just publish xp_stream fabric` or `just publish xp_stream neoforge`
- Publish **one** platform only for a loader: `just publish xp_stream fabric curseforge` or `just publish saturation_regen neoforge modrinth`

Recommended live-release flow:

1. Run preflight for the exact mod and loader you intend to publish.
2. Publish one loader at a time rather than both loaders in one command.
3. If a run partially succeeds, retry only the missing platform task.
4. Verify the released file on Modrinth and CurseForge after Gradle succeeds.

Lower-level Gradle tasks:

- Publish one mod: `.\gradlew publishXp_Stream`
- Publish all mods: `.\gradlew publishAll`
- Fabric to Modrinth only: `.\gradlew :mods:xp_stream:publishFabric:modrinth`
- Fabric to CurseForge only: `.\gradlew :mods:xp_stream:publishFabric:publishFabricPublicationToCurseForge`
- NeoForge to Modrinth only: `.\gradlew :mods:xp_stream:publishNeoForge:modrinth`
- NeoForge to CurseForge only: `.\gradlew :mods:xp_stream:neoforge:publishNeoForgePublicationToCurseForge`

## Release Notes (Changelogs)

Modrinth and CurseForge take release notes from **`mods/<mod_id>/CHANGELOG.md`**. Gradle pulls the `## [<mod_version>]` block that matches **`mod_version`** in `mods/<mod_id>/gradle.properties`. The root **`CHANGELOG.md`** is for **repository / tooling** changes only, not mod releases. Root **`README.md`** links each mod's user-facing README under **`mods/<mod_id>/README.md`**.

## Plugin Decisions

These choices are intentional and are the current standard for this repo:

- Modrinth plugin: `com.modrinth.minotaur`
- CurseForge plugin: `io.github.themrmilchmann.curseforge-publish`

Why these plugins:

- They upload the actual loader-built JARs, not just Maven publications.
- They fit the per-mod and per-loader task model used by this repo.
- They work with tokens supplied through environment variables or CI secrets.
- CurseForge uses a mixed strategy for `xp_stream`: NeoForge uses the ModDevGradle integration, while Fabric keeps a manual publication that derives CurseForge game versions from `minecraft_version`.

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
- `just publish-check saturation_regen neoforge`

The preflight check verifies:

- the target mod exists
- the loader argument is valid
- `mod_id`, `mod_name`, and `mod_version` are present
- `modrinth_project_id` and `curseforge_project_id` are present
- `MODRINTH_TOKEN` and `CURSEFORGE_TOKEN` are set

It does not upload anything.

It also does **not** verify:

- whether the version already exists on Modrinth or CurseForge
- whether CurseForge currently accepts the target Minecraft version slug
- whether the remote upload will avoid transient API or timeout failures

## Partial Failure Handling

If a publish only partially succeeds:

- do not immediately rerun the full publish command blindly
- identify which platform and loader already succeeded
- retry only the missing platform or loader task when possible
- verify whether the platform allows retrying the same version number cleanly
- if CurseForge fails because of game version validation, fix that mapping issue before retrying CurseForge
- if CurseForge fails with a timeout, retry only the CurseForge task before changing repo config

Typical targeted retry commands:

- `just publish xp_stream fabric curseforge`
- `just publish xp_stream neoforge curseforge`
- `just publish saturation_regen fabric curseforge`
- `just publish saturation_regen neoforge curseforge`
- `.\gradlew :mods:xp_stream:publishFabric:publishFabricPublicationToCurseForge`
- `.\gradlew :mods:xp_stream:neoforge:publishNeoForgePublicationToCurseForge`
- `.\gradlew :mods:saturation_regen:publishFabric:publishFabricPublicationToCurseForge`
- `.\gradlew :mods:saturation_regen:publishNeoForge:publishNeoforgePublicationToCurseForge`
- add `--info` when you want more CurseForge logging during a retry

NeoForge CurseForge task locations differ by mod:

- `xp_stream` publishes NeoForge to CurseForge from `:mods:xp_stream:neoforge`
- `saturation_regen` publishes NeoForge to CurseForge from `:mods:saturation_regen:publishNeoForge`

## Notes

- Publishing is currently a manual/local workflow in this repo. There is no repo-managed GitHub Actions publishing pipeline yet.
- CurseForge uses family-level Minecraft version labels. Snapshot, pre-release, and release-candidate builds may need to map to the base Minecraft family slug that CurseForge currently exposes.
- For the `26.2` family, CurseForge uses slug `26-2`. Builds like `26.2`, `26.2-pre-*`, and `26.2-rc-*` should map to that family label rather than inventing a `26-2-0` slug.
- Modrinth prints a clear success URL after upload. CurseForge may succeed more quietly, so if Gradle succeeds without a CurseForge error, verify on the project page when needed.
- Use `Docs/release_checklist.md` as the step-by-step operator flow for version bump, publish, and GitHub release work.
