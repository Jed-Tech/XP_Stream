# GitHub Release Automation

This repo includes a small GitHub release helper that works **per mod** (`-Mod` / `just github-release <mod>`).

## Purpose

Run a `just` command that:

- reads `mod_version` and `mod_name` from `mods/<mod>/gradle.properties`
- creates or reuses the matching git tag in the form `v<mod_version>`
- creates a GitHub release for that tag
- uses the matching section from **`mods/<mod>/CHANGELOG.md`** as the release notes
- does not upload `.jar` assets

## Commands

Default mod is `xp_stream`. Examples:

- Dry run: `just github-release-dry-run xp_stream` (alias: `just xp-stream-github-release-dry-run`)
- Real release: `just github-release xp_stream` (alias: `just xp-stream-github-release`)
- Another mod: `just github-release-dry-run saturation_regen`

## Behavior

The script:

1. Reads `mod_version` and `mod_name` from `mods/<mod>/gradle.properties`
2. Builds the tag name as `v<mod_version>`
3. Extracts the `## [<mod_version>]` section from **`mods/<mod>/CHANGELOG.md`**
4. Verifies `gh` is authenticated
5. Checks whether the tag already exists locally
6. Checks whether the tag already exists on `origin`
7. Checks whether the GitHub release already exists
8. Creates the tag if needed
9. Pushes the tag to `origin` if needed
10. Creates the GitHub release with the changelog section as the release body

## Expected release format (example: `xp_stream`)

- Tag: `v1.1.1`
- Title: `XP_Stream 1.1.1` (from `mod_name` + version)
- Notes: the `## [1.1.1]` section from **`mods/xp_stream/CHANGELOG.md`**

## Requirements

- `gh` must be installed and authenticated
- `git` must be installed
- **`mods/<mod>/CHANGELOG.md`** must contain a `## [<mod_version>]` section matching the current `mod_version`
- **`mods/<mod>/gradle.properties`** must contain `mod_version` and `mod_name`

## Notes

- The dry-run command does not create a tag or a GitHub release.
- The real release command fails if the GitHub release already exists.
- If the tag already exists, the script reuses it instead of creating a second tag.
- The real release command pushes the tag to `origin` before creating the GitHub release, so the release is tied to the exact tag you created.
- This automation is intentionally scoped to GitHub release metadata only. It does not build artifacts or upload release assets.
- **Repository-level** history (not mod releases) is tracked in the root **`CHANGELOG.md`**.
