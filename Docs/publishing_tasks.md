# Publishing Tasks

This document tracks the remaining work needed to finish the repo's publishing plan for Modrinth and CurseForge.

## Current State

Implemented already:

- Per-mod publish tasks exist in Gradle for both mods.
- Per-loader publish tasks exist for Fabric and NeoForge.
- The repo uses `just` as the primary command entrypoint.
- Plugin choices are recorded:
  - Modrinth: `com.modrinth.minotaur`
  - CurseForge: `io.github.themrmilchmann.curseforge-publish`
- Publishing documentation exists in `Docs/publishing.md`.

Not finished yet:

- The publish path has not been validated with a real publish-ready configuration in this repo.
- There is no CI-backed publishing workflow yet.
- The publish path still needs real publish verification and failure-mode validation.
- Supported environment metadata still needs to be confirmed and set appropriately on each platform.

## Goal

Finish a safe, repeatable publishing workflow that:

- uses `just` as the default entrypoint
- publishes the real Fabric and NeoForge JARs
- supports one-mod releases and full release trains
- keeps tokens out of the repo
- is documented clearly enough to follow without tribal knowledge

## Required Tasks

### 1. Configure per-mod platform IDs

Status: completed

Tasks:

- Fill in `modrinth_project_id` for `xp_stream` in `mods/xp_stream/gradle.properties`.
- Fill in `curseforge_project_id` for `xp_stream` in `mods/xp_stream/gradle.properties`.
- Leave `saturation_regen` platform IDs blank until that mod is intentionally made publishable.

Done when:

- Each publishable mod has explicit platform IDs in its own `gradle.properties`.

### 2. Validate environment-based auth

Status: completed locally

Tasks:

- Confirm `MODRINTH_TOKEN` works with the chosen Modrinth project.
- Confirm `CURSEFORGE_TOKEN` works with the chosen CurseForge project.
- Verify publish commands fail safely when tokens are missing.
- Verify no tokens are stored in tracked files, scripts, or docs examples.

Done when:

- Local publishing can authenticate entirely from environment variables.

### 3. Verify artifact selection and publish behavior

Status: partially implemented, not fully verified

Tasks:

- Record that local preflight passed for:
  - `just xp-stream-publish-check`
  - `just publish-check xp_stream fabric`
  - `just publish-check xp_stream neoforge`
- Confirm the uploaded file source is the loader-built JAR from `:mods:<mod_id>:fabric` and `:mods:<mod_id>:neoforge`.
- Confirm the published version names match the intended loader-specific pattern.
- Confirm one Modrinth version is created per loader, each with exactly one file.
- Confirm CurseForge creates one file per loader with the intended display name and game version.
- Confirm the current game version mapping works for the branch's `minecraft_version`.

Done when:

- `xp_stream` can be published successfully to both platforms from the documented commands.

### 3a. Add changelog / release notes metadata from per-mod `CHANGELOG.md`

Status: implemented in repo, not publish-verified

Tasks:

- Reuse the version-section extraction approach already used by `scripts/github-release.ps1`.
- Wire the matching `## [<mod_version>]` section from **`mods/<mod_id>/CHANGELOG.md`** into the Modrinth publish metadata.
- Wire the matching `## [<mod_version>]` section from **`mods/<mod_id>/CHANGELOG.md`** into the CurseForge publish metadata if the plugin supports it.
- Verify the published text format is acceptable on each platform.

Done when:

- Modrinth and CurseForge receive the release notes for the current `mod_version` from that mod’s changelog file, or the repo clearly documents any platform limitation.

### 3b. Set supported environment metadata

Status: not started

Tasks:

- Set the intended support stance to:
  - server: supported
  - client: works on clients
- Confirm whether each platform/plugin exposes that metadata at publish time or whether it must be managed on the platform project page.
- Apply the metadata in the repo where supported.
- Document any platform-side manual step if the metadata is project-level rather than version-level.

Done when:

- The published/project metadata clearly communicates that the mod is server-oriented and also works on clients.

### 4. Keep `saturation_regen` scaffolded-only for now

Status: completed

Tasks:

- Leave `modrinth_project_id` and `curseforge_project_id` blank for `saturation_regen` for now.
- Document that `saturation_regen` publish wiring exists, but the mod is not intended for public publishing yet.
- Ensure publishing docs and release docs do not imply that `saturation_regen` is release-ready.

Done when:

- The intended non-publishable status of `saturation_regen` is explicitly documented.

### 5. Add a separate `release_checklist.md`

Status: completed

Tasks:

- Write a short checklist that covers:
  - bump `mod_version`
  - update **`mods/<mod_id>/CHANGELOG.md`**
  - build and smoke-test the mod
  - run the `just` publish command
  - create the GitHub release if needed
- Create `Docs/release_checklist.md`.
- Link the checklist from `Docs/publishing.md`.
- Make it clear which steps are required for one-mod hotfixes versus full multi-mod releases.

Done when:

- A maintainer can follow one checklist from version bump through publish and release metadata.

### 6. Confirm error handling for partial publishes

Status: completed

Tasks:

- Document what to do if Modrinth succeeds and CurseForge fails.
- Document what to do if Fabric succeeds and NeoForge fails.
- Document whether rerunning the same `just publish` command is safe after a partial success.
- Note any platform-specific constraints, especially around duplicate version numbers or game version validation.

Done when:

- Recovery steps for common publish failure modes are written down.

### 7. Keep publishing manual/local for now

Status: completed

Tasks:

- Document that publishing is intentionally manual/local for now.
- Do not add GitHub Actions publishing steps to the current implementation plan.
- If CI is revisited later, treat it as a separate follow-up project instead of part of the current publishing completion work.

Done when:

- The repo clearly documents that manual/local publishing is the current intended workflow.

### 8. Update related docs for publishing consistency

Status: completed

Tasks:

- Update `Docs/justfile.md` so publish guidance matches the `just`-first workflow and current publishing commands.
- Update `AGENTS.md` so agent instructions reflect the current publishing workflow and documentation locations.
- Update `Docs/technicalBrief.md` so its publishing and release notes reflect the current manual/local plan and current docs.

Done when:

- The main supporting docs are aligned with `Docs/publishing.md`, `Docs/publishing_tasks.md`, and the current repo workflow.

## Nice-to-Have Tasks

### 9. Add publish readiness checks

Status: completed

Implemented:

- Add a small preflight script or recipe that checks:
  - required tokens are present
  - project IDs are set
  - target mod exists
  - loader argument is valid
- Expose the check through `just publish-check <mod> [loader]` and `just xp-stream-publish-check`.

### 10. Add safer docs for one-loader publishes

Status: completed

Implemented:

- Expand docs for `just publish <mod> <loader>`.
- Clarify when publishing only Fabric or only NeoForge is acceptable.

### 11. Add CI verification without full publishing

Possible follow-up work:

- Add a CI workflow that builds both loaders and verifies publishing configuration without actually uploading.

## Suggested Execution Order

1. Configure project IDs for `xp_stream`.
2. Validate local tokens.
3. Run the first successful `xp_stream` preflight with real project IDs and tokens.
4. Run the first real end-to-end publish verification for `xp_stream`.
5. Verify that publish-time changelog text appears correctly on Modrinth and CurseForge.
6. Set and document supported environment metadata on each platform.
7. Update verification notes after the first successful end-to-end publish.

## Open Questions

No open questions right now.
