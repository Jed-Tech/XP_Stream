# Fabric Reference

This is a small Fabric-side reference for this repo.

## Scope

Use this document as a branch-agnostic reminder for:

- Fabric project structure
- common mixin patterns
- common troubleshooting points

Do not treat this file as a pinned version matrix. Check the current branch files for exact versions.

## Current Fabric Shape in This Repo

- On **`main`**, Fabric uses **unobfuscated** game jars (no Loom remap) with `net.fabricmc.fabric-loom`.
- On the **`monorepo/1.21.1`** line, Fabric uses **official Mojang mappings** via `net.fabricmc.fabric-loom-remap` and `mappings loom.officialMojangMappings()` (obfuscated game through 1.21.11).
- Fabric build logic lives under `mods/<mod_id>/fabric/`.
- Fabric publish logic may live in a separate `publishFabric` project.
- `just` is the preferred entrypoint for build and run commands.

Common commands:

- `just build-all`
- `just xp-stream-build`
- `just xp-stream-fabric-client`
- `just publish xp_stream fabric`

## Common Mixin Reminders

- Prefer small injections over overwrites.
- Keep server-authoritative gameplay logic on the logical server.
- Use `@Unique` for mixin-added state and helpers.
- Re-check target method names when Minecraft updates.
- If a mixin target moves or changes, inspect generated sources before changing code blindly.

## Common Fabric Patterns

Server-only guard:

```java
if (player.level().isClientSide()) return;
```

Re-entrancy guard:

```java
@Unique
private static boolean modid$inHook = false;
```

Vanilla-path behavior over custom shortcuts:

```java
orb.playerTouch(player);
```

## Troubleshooting

If Fabric build or run breaks after a version bump:

1. verify root `gradle.properties` pins
2. run `just build-all`
3. run the Fabric client task for the affected mod
4. inspect generated sources and current loader metadata
5. update mixin targets only when the code actually moved

If Fabric publishing breaks:

1. check `mods/<mod_id>/publishFabric/build.gradle`
2. confirm `MODRINTH_TOKEN` and `CURSEFORGE_TOKEN`
3. verify `modrinth_project_id` and `curseforge_project_id`
4. verify the changelog has the matching `## [<mod_version>]` entry

## Publishing Note

For `xp_stream`, Fabric CurseForge publishing may use a manual path depending on loader setup; confirm against `mods/xp_stream/publishFabric/build.gradle` on your branch.

See:

- `Docs/publishing.md`
- `Docs/release_checklist.md`
- `Docs/curseforge_publish_fix_plan.md`
