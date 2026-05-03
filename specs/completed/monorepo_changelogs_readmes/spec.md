# monorepo_changelogs_readmes

## Summary

Split user-facing documentation so each mod owns its README and Keep a Changelog–style release notes, while the repository root documents the workspace. Gradle publish (Modrinth/CurseForge), `github-release.ps1`, and operator docs use **`mods/<mod_id>/CHANGELOG.md`** for the `## [<mod_version>]` contract.

**Delivered:** Per-mod READMEs and changelogs under `mods/xp_stream/` and `mods/saturation_regen/`; root README + workspace-only root `CHANGELOG.md`; script and Gradle paths updated; `saturation_regen` publish modules match `xp_stream` changelog wiring; `Docs/github_release.md`, `Docs/release_checklist.md`, `Docs/publishing_tasks.md`, `AGENTS.md`, and `Docs/technicalBrief.md` updated.

## Problem (historical)

- Root `README.md` was written entirely for **XP Stream**, not the multi-mod repo.
- Root `CHANGELOG.md` held **XP Stream** release history but was the only path used for publish and GitHub release.
- `github-release.ps1` accepted `-Mod` but read root `CHANGELOG.md`.
- `saturation_regen` publish Gradle lacked Modrinth/CurseForge changelog metadata parity with `xp_stream`.

## Goal

- **`mods/<mod_id>/README.md`** — mod-specific landing copy (XP Stream migrated from root; **Saturation Regen** added in the same user-value style as XP Stream).
- **`mods/<mod_id>/CHANGELOG.md`** — canonical release notes; **`## [<mod_version>]`** sections are the contract for Gradle extractors and `github-release.ps1`.
- **Root `README.md`** — mono-repo overview: mods list, `just`, links to per-mod READMEs, `Docs/`.
- **Root `CHANGELOG.md`** — repository/workspace only (not per-mod version histories).

## Scope

Delivered as planned: `mods/xp_stream/`, `mods/saturation_regen/`, repo root, `scripts/github-release.ps1`, all four publish `build.gradle` files, and operator docs plus **`AGENTS.md`** and **`Docs/technicalBrief.md`**.

## Non-Goals

- Changing versioning policy, tag format, or publish project IDs.
- Automating changelog generation from git commits.
- Resolving shared git tag namespace (`v<version>` without mod prefix) — follow-up only.

## Constraints

- **Keep a Changelog** + **`## [<mod_version>]`** parsing unchanged for Gradle and PowerShell.
- `just` recipe names unchanged.
- Publishing/version IDs unchanged beyond changelog path wiring.

## Success Criteria (met)

- `xp_stream` publish reads **`mods/xp_stream/CHANGELOG.md`**; fails clearly if the version section is missing.
- `just github-release <mod>` uses **`mods/<mod>/CHANGELOG.md`** for release body text.
- Root README is mono-repo scoped; XP Stream product copy is under **`mods/xp_stream/README.md`**.
- Root **`CHANGELOG.md`** is workspace-only; XP Stream history is in **`mods/xp_stream/CHANGELOG.md`**.
- **`mods/saturation_regen/README.md`** matches XP Stream’s structure and tone; **`mods/saturation_regen/CHANGELOG.md`** has **`## [0.1.0]`**; publish wiring matches `xp_stream`.
- Operator docs and **`Docs/technicalBrief.md`** describe per-mod changelog paths.

## Decisions

| Topic | Decision |
|-------|----------|
| Mod artifacts | `mods/<mod_id>/README.md` and `mods/<mod_id>/CHANGELOG.md` |
| Root changelog | Workspace-only; not a rollup of mod releases |
| Contract | `## [<mod_version>]` sections per mod file |
| First root `CHANGELOG.md` entry | **`## [2026-04-04]`** — documents split; no mod version history in that entry |
| `saturation_regen` `0.1.0` changelog | **`## [0.1.0]`** with user-facing bullets for publish/GitHub extractors |
| `saturation_regen` README | Same section pattern as XP Stream (tagline, emoji sections, config, installation) |

## Documentation Impact (final)

- Release/publishing docs and `AGENTS.md` — updated for per-mod paths.
- Root README — links to both mod READMEs.
- **`Docs/technicalBrief.md`** — mono-repo README/changelog layout and release/publish pointers aligned with implementation.
