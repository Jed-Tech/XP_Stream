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
- `just saturation-regen-build`
- `just build-all`
- `just clean`
- `just tasks xp_stream fabric`

## Conventions
- Prefer clear repo-specific aliases for common tasks.
- Keep parameterized recipes small and predictable.
- Prefer `./gradlew.bat` over global `gradle`.
- Keep examples and defaults centered on `xp_stream`.

## Maintaining the justfile
- Validate Gradle task paths before adding new wrappers.
- Check available tasks with `./gradlew.bat :mods:<mod>:<loader>:tasks --all`.
- Add aliases only for commands the repo will use often.
- Keep publish commands explicit and never make them the default path.
