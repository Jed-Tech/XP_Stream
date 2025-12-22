# XP_Stream (Fabric 1.21.11) — Technical Brief for Cursor

## Objective
Create a **server-side only** Fabric mod for Minecraft **1.21.11** that makes XP absorb faster while keeping the “XP shower” visual. The pain point is the **ankle swarm / slow drip** when many XP orbs are near the player.

## Approach (v0.1)
Use **collision-driven burst pickup** (Approach B):
- Keep everything vanilla until an XP orb collides with a player.
- On collision/pickup of one orb, immediately pick up **additional nearby XP orbs** in a very small radius around the player (“ankle zone”), capped.
- Do NOT pull orbs from far away. Do NOT merge orbs. Do NOT bank XP. Do NOT change XP values.

## Constraints (Design Contract)
- XP orbs still spawn and move as vanilla.
- Pickup delay behavior stays vanilla (we don’t change spawn delay).
- XP awarded must use **vanilla orb pickup mechanics** so **Mending** works exactly as vanilla.
- Multiplayer behavior should remain intuitive (closest/actual colliding player receives XP).
- No global scans. No per-tick scanning of all orbs. Logic should be local to collision events.
- No performance regression vs vanilla.

## Hook / Injection Point
Implement via **Mixin** into the vanilla class responsible for XP orb pickup:
- Target: `net.minecraft.entity.ExperienceOrbEntity`
- Method: `onPlayerCollision(PlayerEntity player)` (or the Yarn-named equivalent in 1.21.11)
- Inject at the point where vanilla would grant XP to the player (or at tail), then perform additional pickups.

## Burst Pickup Details
- Define constants for v0.1 (hardcoded, no config yet):
  - `EXTRA_PICKUP_RADIUS = 1.25` blocks (tight ankle zone)
  - `MAX_EXTRA_ORBS_PER_EVENT = 8` (cap)
- When `onPlayerCollision(player)` triggers:
  1) Let vanilla logic proceed (or call it once if we override).
  2) Find nearby `ExperienceOrbEntity` within `EXTRA_PICKUP_RADIUS` around player.
  3) For up to `MAX_EXTRA_ORBS_PER_EVENT`, apply the same vanilla pickup method for each orb and remove/discard it.
  4) Avoid double-processing the original orb and avoid re-entrancy loops.
  5) Play vanilla pickup sound **once** per burst (not for every orb). Ideally rely on vanilla sound from the first pickup, suppress additional sound if needed.

## Correctness Requirements
- **Mending** must work unchanged: ensure XP is applied through vanilla orb pickup path, not by directly adding XP via /xp or manipulating XP bar.
- No XP duplication/loss in farms: test with large XP volumes.
- Must work on dedicated server with no client mod required.

## Anti-Recursion / Double-Pickup Guard
- Need a guard to prevent collecting orbs that are already collected in the same call:
  - Use a simple boolean re-entrancy guard (ThreadLocal or static flag) or mark orbs with a transient tag/flag before collecting.
  - Ensure this guard is safe in a single-threaded tick context.

## Project Setup
- Fabric Loom project targeting Minecraft 1.21.11
- Yarn mappings for 1.21.11
- Mixin configured via `mixins.xp_stream.json`
- Mod id: `xp_stream`
- Repo name: `XP_Stream`

## Output Artifacts
- Working dev environment (runServer)
- Jar builds successfully
- Minimal README and LICENSE already handled separately

## Testing Checklist (local)
1) Kill mobs and confirm XP shower still appears.
2) Stand in a large XP pile and confirm absorption is much faster (ankle swarm reduced).
3) Use damaged Mending gear and confirm repairs happen normally.
4) Two players near XP: confirm pickup behavior is intuitive and not “sniped” from distance.
5) Verify no errors spam in logs under heavy XP.

## Deliverables for v0.1 PR
- Working burst pickup implementation
- Constants for radius + cap
- No config yet
- Minimal debug logging behind a constant (OFF by default)