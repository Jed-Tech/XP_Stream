# justfile.md

## Purpose
- `justfile` is the command entrypoint for common repo workflows.
- Prefer `just` for routine tasks instead of typing raw Gradle project paths.

## Repo Shape
- Mods: `xp_stream`, `saturation_regen`
- Loaders: `fabric`, `neoforge`

## How We Use It
- Top-level aliases cover the common happy path for each mod.
- Parameterized recipes cover advanced or less common cases.
- Use `just` or `just --list` to discover available commands.

## Common Examples
- `just xp-stream-build`
- `just xp-stream-fabric-client`
- `just xp-stream-neoforge-client`
- `just xp-stream-publish-check`
- `just xp-stream-publish`
- `just publish-check xp_stream`
- `just publish xp_stream fabric`
- `just saturation-regen-build`
- `just build-all`
- `just clean`
- `just tasks xp_stream fabric`

## Conventions
- Prefer clear repo-specific aliases for common tasks.
- Keep parameterized recipes small and predictable.
- Prefer `./gradlew.bat` over global `gradle`.
- Keep examples and defaults centered on `xp_stream`.
- Treat publishing as explicit, manual/local work rather than a default workflow.

## Publishing Notes
- Use `just xp-stream-publish-check` before a real `xp_stream` publish.
- Use `just xp-stream-publish` for the normal `xp_stream` publish path.
- Use `just publish-check <mod> [loader]` to validate publishing prerequisites without uploading anything.
- Use `just publish <mod> <loader>` when you need a single-loader publish.
- `saturation_regen` publish commands exist, but that mod is scaffolded-only for now.
- See `Docs/publishing.md` for publishing details and `Docs/release_checklist.md` for the operator checklist.

## Maintaining the justfile
- Validate Gradle task paths before adding new wrappers.
- Check available tasks with `./gradlew.bat :mods:<mod>:<loader>:tasks --all`.
- Add aliases only for commands the repo will use often.
- Keep publish commands explicit and never make them the default path.
