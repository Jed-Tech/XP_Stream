# Release Checklist

This checklist is the operator workflow for releasing a mod from this repo.

## Scope

Default release scope:

- Assume all mods in the monorepo are part of the release unless specifically noted otherwise.

Current release model:

- publishing is manual/local
- `just` is the primary command entrypoint
- GitHub release metadata is handled separately from Modrinth and CurseForge publishing

## Release Setup

Before running the release flow, update versions, targets, and pins according to [Docs/broad_minecraft_family_policy.md](C:/Users/jedde/GithubRepos/Minecraft/XP_Stream/Docs/broad_minecraft_family_policy.md).

Known files to review for a release line include:

- root `gradle.properties`
- `mods/<mod_id>/fabric/src/main/resources/fabric.mod.json`
- `mods/<mod_id>/neoforge/src/main/resources/META-INF/neoforge.mods.toml`
- `mods/<mod_id>/gradle.properties`
- `mods/<mod_id>/CHANGELOG.md`
- any other release-line version, pin, or target files needed for that cycle

## One-Mod Release Checklist

Use this flow for one released mod.

1. Update release-line versions, targets, and pins according to the broad policy and the files listed above.
2. Update `mods/<mod_id>/gradle.properties` with the target `mod_version`.
3. Update `mods/<mod_id>/CHANGELOG.md` so it includes a `## [<mod_version>]` section for that release.
4. Build the mod with `just build <mod_id>`.
5. Smoke-test the mod on the loaders you intend to publish.
6. Run `just publish-check <mod_id>`.
7. Confirm `mods/<mod_id>/gradle.properties` contains the correct `modrinth_project_id` and `curseforge_project_id`.
8. Publish with `just publish <mod_id>`.
9. Verify both loader uploads on Modrinth and CurseForge:
   - Fabric upload present
   - NeoForge upload present
   - version names match the expected loader-specific naming
   - game version is correct
10. On CurseForge, review any file metadata that still needs manual adjustment.
    - Example: Environment may still need to be set on the CurseForge website.
11. Create the GitHub release metadata:
    - dry run: `just github-release-dry-run <mod_id>`
    - real release: `just github-release <mod_id>`
12. Verify the GitHub release notes match the intended `mods/<mod_id>/CHANGELOG.md` entry.

## Full Release Train Checklist

Use this flow when releasing the full monorepo set or any intentional multi-mod release train.

1. Update release-line versions, targets, and pins according to the broad policy and the files listed above.
2. Update `mod_version` for each mod that is part of the release train.
3. Update each released mod's `mods/<mod_id>/CHANGELOG.md` with the `## [<mod_version>]` sections needed for that release.
4. Build and smoke-test each released mod.
5. Confirm all required platform IDs and local tokens are configured.
6. Publish with the equivalent per-mod `just` commands or `.\gradlew publishAll` when appropriate.
7. Verify each expected platform and loader upload.
8. Create GitHub release metadata for any mod that uses it.

## Failure Handling

If a publish only partially succeeds:

- Do not immediately rerun blindly.
- Check which platform and loader already succeeded.
- Confirm whether the target platform allows retrying the same version number cleanly.
- Retry only the missing platform or loader task when possible.
- Raw CurseForge task paths vary by mod and loader.
- For `xp_stream`, the raw task paths are:
  - NeoForge: `:mods:xp_stream:neoforge:publishNeoForgePublicationToCurseForge`
  - Fabric: `:mods:xp_stream:publishFabric:publishFabricPublicationToCurseForge`
- If Modrinth succeeds and CurseForge fails because of game version validation, fix the CurseForge mapping issue before retrying CurseForge.
- Use `just publish <mod> <loader>` if you only need to retry one loader path.

## References

- `Docs/publishing.md`
- `Docs/github_release.md`
