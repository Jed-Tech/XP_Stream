# Release Checklist

This checklist is the operator workflow for releasing a mod from this repo.

## Scope

Current intended release target:

- `xp_stream`: publishable when project IDs and tokens are configured
- `saturation_regen`: not a normal public release target yet

Current release model:

- publishing is manual/local
- `just` is the primary command entrypoint
- GitHub release metadata is handled separately from Modrinth and CurseForge publishing

## One-Mod Release Checklist

Use this flow for a normal `xp_stream` release or hotfix.

1. Update `mods/xp_stream/gradle.properties` with the target `mod_version`.
2. Update `mods/xp_stream/CHANGELOG.md` so it includes a `## [<mod_version>]` section for that release.
3. Confirm the branch's Minecraft and loader versions are correct for the release line.
4. Build the mod with `just xp-stream-build`.
5. Smoke-test the mod on the loaders you intend to publish.
6. Run `just xp-stream-publish-check`.
7. Confirm `mods/xp_stream/gradle.properties` contains the correct `modrinth_project_id` and `curseforge_project_id`.
8. Publish with `just xp-stream-publish`.
9. Verify both loader uploads on Modrinth and CurseForge:
   - Fabric upload present
   - NeoForge upload present
   - version names match the expected loader-specific naming
   - game version is correct
10. On CurseForge, review any file metadata that still needs manual adjustment.
    - Example: Environment may still need to be set on the CurseForge website.
11. Create the GitHub release metadata:
    - dry run: `just xp-stream-github-release-dry-run`
    - real release: `just xp-stream-github-release`
12. Verify the GitHub release notes match the intended `mods/xp_stream/CHANGELOG.md` entry.

## Full Release Train Checklist

Use this flow only when more than one mod is intentionally release-ready.

1. Update `mod_version` for each mod that is part of the release train.
2. Update each mod's `mods/<mod_id>/CHANGELOG.md` with the `## [<mod_version>]` sections needed for the released mods.
3. Build and smoke-test each released mod.
4. Confirm all required platform IDs and local tokens are configured.
5. Publish with `.\gradlew publishAll` or the equivalent per-mod `just` commands.
6. Verify each expected platform and loader upload.
7. Create GitHub release metadata for any mod that uses it.

## Failure Handling

If a publish only partially succeeds:

- Do not immediately rerun blindly.
- Check which platform and loader already succeeded.
- Confirm whether the target platform allows retrying the same version number cleanly.
- Retry only the missing platform or loader task when possible.
- `xp_stream` uses different raw CurseForge task paths by loader:
  - NeoForge: `:mods:xp_stream:neoforge:publishNeoForgePublicationToCurseForge`
  - Fabric: `:mods:xp_stream:publishFabric:publishFabricPublicationToCurseForge`
- If Modrinth succeeds and CurseForge fails because of game version validation, fix the CurseForge mapping issue before retrying CurseForge.
- Use `just publish <mod> <loader>` if you only need to retry one loader path.

## References

- `Docs/publishing.md`
- `Docs/github_release.md`
