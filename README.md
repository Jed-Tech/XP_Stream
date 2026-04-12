# XP_Stream (mono-repo)

This repository builds and maintains multiple Minecraft mods from one Gradle workspace.

| Mod | Description | Loaders |
| --- | --- | --- |
| **[XP Stream](mods/xp_stream/README.md)** | Faster XP orb pickup -- vanilla feel, smoother absorption. | Fabric, NeoForge |
| **[Saturation Regen](mods/saturation_regen/README.md)** | Natural regeneration that respects saturation without forcing a full hunger bar first. | Fabric, NeoForge |

| Mod | Modrinth | CurseForge |
| --- | --- | --- |
| **XP Stream** | [modrinth.com/mod/xp-stream](https://modrinth.com/mod/xp-stream) | [curseforge.com/minecraft/mc-mods/xp-stream](https://www.curseforge.com/minecraft/mc-mods/xp-stream) |
| **Saturation Regen** | [modrinth.com/mod/saturation-regen](https://modrinth.com/mod/saturation-regen) | [curseforge.com/minecraft/mc-mods/saturation-regen](https://www.curseforge.com/minecraft/mc-mods/saturation-regen) |

Design notes, publishing, and internal docs live under [Docs/](Docs/).

---

## Requirements

- **Java** -- use the toolchain configured by this repo (see `just doctor` and Gradle).
- **Git** -- for version control and release tags.

---

## Build & run (`just`)

From the repo root:

- List recipes: `just`
- Build everything: `just build-all`
- Build one mod: `just build xp_stream` or `just build saturation_regen`
- Run a dev client: `just run-client <mod> <fabric|neoforge>`

See [Docs/justfile.md](Docs/justfile.md) for more detail.

---

## Changelogs

- **Repository / workspace** -- [CHANGELOG.md](CHANGELOG.md) (tooling, layout, shared infrastructure).
- **XP Stream** -- [mods/xp_stream/CHANGELOG.md](mods/xp_stream/CHANGELOG.md).
- **Saturation Regen** -- [mods/saturation_regen/CHANGELOG.md](mods/saturation_regen/CHANGELOG.md).

Release automation and publishing are documented in [Docs/publishing.md](Docs/publishing.md), [Docs/release_checklist.md](Docs/release_checklist.md), and [Docs/github_release.md](Docs/github_release.md).
