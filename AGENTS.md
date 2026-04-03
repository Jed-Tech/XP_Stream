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
- **Context7 MCP** = up-to-date docs for libraries, frameworks, SDKs, APIs, CLIs, and cloud services. Prefer over web search for official docs. Read the MCP tool schema before calling.

## Environment
- On tool failure, check (`--version`) then stop and inform the user.

## Basic Commands
- List commands: `just`
- Build all: `just build-all`
- Clean: `just clean`
- Check toolchain: `just doctor`
- Run Fabric client: `just xp-stream-fabric-client`
- Run NeoForge client: `just xp-stream-neoforge-client`

More command recipes available in `./justfile`.

## Conventions
- Use `just` over Gradle.
- See Docs/justfile.md.

## Constraints
- Do not publish unless explicitly requested
- Do not change versioning or publishing config without approval
- Use the repo-configured Java toolchain
