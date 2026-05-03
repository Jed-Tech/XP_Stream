# Tasks - monorepo_changelogs_readmes

## Implementation Plan Summary

Per-mod README + `CHANGELOG.md`, root mono-repo README + workspace `CHANGELOG.md`, `github-release.ps1` and Gradle publish paths, `saturation_regen` changelog parity with `xp_stream`, operator docs, `AGENTS.md`, `Docs/technicalBrief.md`.

## Tasks

- [x] Copy root changelog → `mods/xp_stream/CHANGELOG.md`; new root `CHANGELOG.md` with `## [2026-04-04]`.
- [x] Move root README → `mods/xp_stream/README.md`; new root `README.md`.
- [x] Add `mods/saturation_regen/README.md` and `CHANGELOG.md` (`## [0.1.0]`).
- [x] Update `github-release.ps1` and publish `build.gradle` files (both mods).
- [x] Update `Docs/github_release.md`, `release_checklist.md`, `publishing_tasks.md`, `AGENTS.md`, `Docs/technicalBrief.md`.
- [x] Validate: `just xp-stream-publish-check`, `just github-release-dry-run` for both mods.

## Notes

- Validation recorded at implementation time; see archived spec for detail.
