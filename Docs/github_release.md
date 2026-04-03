# GitHub Release Automation

This repo includes a small GitHub release helper for `xp_stream`.

## Purpose

Run a `just` command that:

- reads `mod_version` from `mods/xp_stream/gradle.properties`
- creates or reuses the matching git tag in the form `v<mod_version>`
- creates a GitHub release for that tag
- uses the matching section from `CHANGELOG.md` as the release notes
- does not upload `.jar` assets

## Commands

- Dry run: `just xp-stream-github-release-dry-run`
- Real release: `just xp-stream-github-release`

## Behavior

The script:

1. Reads `mod_version` and `mod_name` from `mods/xp_stream/gradle.properties`
2. Builds the tag name as `v<mod_version>`
3. Extracts the `## [<mod_version>]` section from `CHANGELOG.md`
4. Verifies `gh` is authenticated
5. Checks whether the tag already exists locally
6. Checks whether the tag already exists on `origin`
7. Checks whether the GitHub release already exists
8. Creates the tag if needed
9. Pushes the tag to `origin` if needed
10. Creates the GitHub release with the changelog section as the release body

## Expected release format

- Tag: `v1.1.1`
- Title: `XP_Stream 1.1.1`
- Notes: the `## [1.1.1]` section from `CHANGELOG.md`

## Requirements

- `gh` must be installed and authenticated
- `git` must be installed
- `CHANGELOG.md` must contain a section matching the current `mod_version`
- `mods/xp_stream/gradle.properties` must contain `mod_version` and `mod_name`

## Notes

- The dry-run command does not create a tag or a GitHub release.
- The real release command fails if the GitHub release already exists.
- If the tag already exists, the script reuses it instead of creating a second tag.
- The real release command pushes the tag to `origin` before creating the GitHub release, so the release is tied to the exact tag you created.
- This automation is intentionally scoped to GitHub release metadata only. It does not build artifacts or upload release assets.
