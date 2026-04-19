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
- `fd`
- `node`
- `npm`
- `python`
- `uv`
- Context7 MCP

## Tool Usage
- `just` = run `./justfile` tasks (build, clean, doctor, …).
- `git` = commits, history, diffs.
- `gh` = GitHub: PRs, issues, Actions, `gh api`.
- `jq` = query/filter JSON (stdin or files).
- `rg` = search file contents by pattern (ripgrep).
- `fd` = find files by name/path; use `rg` to search inside file contents.
- **Context7 MCP** = up-to-date docs for libraries, frameworks, SDKs, APIs, CLIs, and cloud services.

## Environment
- On tool failure, check (`--version`) then stop and inform the user.
- **Java:** `monorepo/1.21.1` targets **Java 21** (Gradle toolchain). `main` (26.1.x) may target a newer JDK; see root `build.gradle` / `gradle.properties` on the checked-out branch.

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