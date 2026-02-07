# XP_Stream

**Faster XP pickup. Same vanilla feel.**

XP_Stream removes the vanilla delay when collecting experience orbs. Large XP drops from mob farms, enchanting, or boss fights are absorbed smoothly instead of creating an ankle swarm that follows you around.

---

## What It Does

- **Speeds up XP absorption** — large clusters are collected in seconds, not minutes
- **Preserves vanilla mechanics** — orbs still fly toward you, Mending still works
- **No XP loss** — every orb awards its full value
- **Keeps the visual and audio effects** — you'll still see orbs flowing in, just without the backlog

You get the satisfying rush of XP without the waiting.

---

## Configuration

XP_Stream works out of the box with sensible defaults. If you want to tweak it:

**File:** `config/xp_stream.json`

```json
{
  "maxBurstOrbs": 6,
  "debug": false
}
```

| Setting | Default | What it does |
|---------|---------|--------------|
| `maxBurstOrbs` | 6 | How many extra orbs to grab per pickup (higher = faster xp orb pickup) |
| `debug` | false | Log pickup events to console (for troubleshooting) |

---

## Installation

**Fabric:**
1. Install [Fabric Loader](https://fabricmc.net/) for Minecraft 26.1
2. Drop `xp_stream-fabric-26.1.jar` into your `mods` folder (server or client for singleplayer)

**NeoForge:**
1. Install [NeoForge](https://neoforged.net/) for Minecraft 26.1
2. Drop `xp_stream-neoforge-26.1.jar` into your `mods` folder (server or client for singleplayer)

**Server-side only** — clients connecting to your server don't need the mod.

---

## For Server Admins

- **No client requirement** — players connect with vanilla clients
- **Minimal performance impact** — logic only runs when orbs are picked up
- **Safe for multiplayer** — each player's pickup is independent
- **Easy to disable** — remove the jar or set `maxBurstOrbs` to 0

---

## Compatibility

- **Minecraft:** 26.1
- **Loaders:** 
  - ✅ Fabric (Fabric Loader ≥0.18.4)
  - ✅ NeoForge (for Minecraft 26.1)
- **Other mods:** Should work with any mod that doesn't alter XP orb collision

---

## License

XP_Stream is licensed under **CC BY-NC-ND 4.0**.

- ✅ Share freely (including in modpacks)
- ✅ Attribution required
- ❌ No commercial use
- ❌ No modifications or derivatives

See the [LICENSE](LICENSE) file or [Creative Commons](https://creativecommons.org/licenses/by-nc-nd/4.0/) for full terms.

---

## Links

- [GitHub](https://github.com/Jed-Tech/XP_Stream)
- [Issues & Feedback](https://github.com/Jed-Tech/XP_Stream/issues)
- [Changelog](CHANGELOG.md) — Version history and changes
