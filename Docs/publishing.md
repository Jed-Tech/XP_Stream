# Publishing (Modrinth + CurseForge)

Publishing uses **one Modrinth version per loader** and **one CurseForge file per loader** per mod release (same `mod_version`, different version names so users can tell them apart).

## Tokens (env/CI only)

- **MODRINTH_TOKEN** — Modrinth API token (create at [modrinth.com/settings/account](https://modrinth.com/settings/account))
- **CURSEFORGE_TOKEN** — CurseForge API token ([curseforge.com/account/api-tokens](https://curseforge.com/account/api-tokens))

Never commit tokens. Set them in the environment or in CI secrets.

## Per-mod project IDs

Each mod sets its platform IDs in `mods/<mod_id>/gradle.properties`:

- `modrinth_project_id` — Modrinth project ID (or slug)
- `curseforge_project_id` — CurseForge project ID

If either is empty, that platform is skipped for that mod.

## Tasks

- **Publish one mod:**  
  `.\gradlew publishXp_Stream`  
  (Runs Modrinth + CurseForge for Fabric and NeoForge: four uploads, two Modrinth versions, two CurseForge files.)

- **Publish all mods:**  
  `.\gradlew publishAll`

Per-loader tasks (if you need them alone):

- `.\gradlew :mods:xp_stream:publishFabric:modrinth` — Fabric to Modrinth only  
- `.\gradlew :mods:xp_stream:publishFabric:publishToCurseForge` — Fabric to CurseForge only  
- `.\gradlew :mods:xp_stream:publishNeoForge:modrinth` — NeoForge to Modrinth only  
- `.\gradlew :mods:xp_stream:publishNeoForge:publishToCurseForge` — NeoForge to CurseForge only  

## Version names

Modrinth/CurseForge version names are set per loader, e.g.:

- `XP_Stream 0.3.4 (Fabric) for 26.1-snapshot-7`
- `XP_Stream 0.3.4 (NeoForge) for 26.1-snapshot-7`

So each Modrinth version has **exactly one file** (Fabric JAR or NeoForge JAR).

**Snapshot publishing note:** CurseForge may not recognize snapshot game versions (e.g. 26.1-snapshot-7). If CurseForge publish fails due to game version validation, publish snapshots to Modrinth only, or adjust the CurseForge game version mapping accordingly.
