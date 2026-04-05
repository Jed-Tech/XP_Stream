# AGENTS.md

## Scope
- Multi-mod Minecraft workspace
- Mods: `xp_stream`, `saturation_regen`
- Loaders: `fabric`, `neoforge`

## Available Tools
- `git`
- `gh`
- `winget`
- `choco`
- `java`
- `gradlew.bat`
- `just`
- `jq`
- `rg`
- `node`
- `npm`
- `python`
- `pip`
- Context7 MCP

## Tool Usage
- Use `just` for workflows.
- `git` = version control.
- `gh` = GitHub (PRs, issues, Actions).
- `jq` = JSON parsing.
- `rg` = code search.
- **Context7 MCP** = up-to-date docs for libraries, frameworks, SDKs, APIs, CLIs, and cloud services.

## Environment
- On tool failure, check (`--version`) then stop and inform the user.

## Basic Commands
- List commands: `just`
- Build all: `just build-all`
- Clean: `just clean`
- Check toolchain: `just doctor`
- Publish `xp_stream`: `just xp-stream-publish`
- GitHub release (xp_stream): `just github-release xp_stream`
- Run Fabric client: `just xp-stream-fabric-client`
- Run NeoForge client: `just xp-stream-neoforge-client`

More command recipes available in `./justfile`.

## Conventions
- Use `just` over Gradle.
- See Docs/justfile.md.
- For publishing and releases, see `Docs/publishing.md` and `Docs/release_checklist.md`.
- Per-mod release notes: **`mods/<mod_id>/CHANGELOG.md`** (`## [<mod_version>]` sections). Root **`CHANGELOG.md`** is repo/workspace only.

## Constraints
- Do not publish unless explicitly requested
- Do not change versioning or publishing config without approval
- Use the repo-configured Java toolchain

## Change Workflow (Skills-Driven)
- Begin plan-change discussion to clarify intent and naming
- Start with skill: plan-change
- Scope = `xp_stream`, `saturation_regen`, or `_Repo`
- Use Skills to perform each phase: plan-change, implement-change, document-change

### Structure
- Active: `Docs/<scope>/Specs/<change-name>/`
- Archive: `Docs/<scope>/Archive/<change-name>/`