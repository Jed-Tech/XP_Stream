# NeoForge Update Plan Template

Copy this file to a new doc in `Docs/` and replace the placeholders with the target versions for the update you are planning.

## Overview

This template is for updating the repo to a newer NeoForge version and its matching Minecraft line.

Fill in:

- target NeoForge version
- target Minecraft version
- previous NeoForge version
- previous Minecraft version

## Planning Checklist

1. Verify the latest target NeoForge version from the NeoForge Maven metadata.
2. Confirm the matching Minecraft version for that NeoForge build.
3. Confirm the current ModDevGradle version is suitable for the target.
4. Decide whether the change looks like:
   - version-only
   - version plus code updates

## Repo Changes

Usually update:

- root `gradle.properties`
- any docs for the update cycle
- changelog entries if the update is being released

Usually review:

- NeoForge loader metadata
- runtime behavior on the NeoForge client
- any shared code that depends on changed Minecraft behavior

## Verification Checklist

1. Run `just build-all`.
2. Run the NeoForge client for the affected mod.
3. Confirm the mod loads without mixin or loader errors.
4. Perform a quick gameplay sanity test.
5. If the branch also targets Fabric, confirm Fabric still builds.

## Release Notes

If the update is part of a release:

1. update the mod version as needed
2. update the per-mod changelog entry
3. update any release docs for the cycle

## References

- NeoForge Maven metadata:
  - [maven-metadata.xml](https://maven.neoforged.net/releases/net/neoforged/neoforge/maven-metadata.xml)
- Repo release docs:
  - `Docs/publishing.md`
  - `Docs/release_checklist.md`
