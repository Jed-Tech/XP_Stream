# Vanilla Java Minecraft: XP orb pickup (source research)

**Scope:** Behavior as implemented in **Minecraft 26.1** common sources (**Mojang mappings**), from `minecraft-common-deobf-26.1-sources.jar` (Fabric Loom / Gradle cache).  
**Yarn note:** Mojmap `ExperienceOrb.playerTouch(Player)` is often named `onPlayerCollision` in Yarn; method bodies match the same pipeline.

---

## Pipeline in plain English

1. **Each tick**, `ExperienceOrb.tick()` runs movement, merge scan (periodic), **`followNearbyPlayer()`**, then **`move(MoverType.SELF, deltaMovement)`** with friction.
2. **Each tick**, for each **living, non-spectator** player, **`Player.aiStep()`** builds a **pickup AABB** (expanded player hitbox), runs **`level().getEntities(this, pickupArea)`**, collects XP orbs, and calls **`touch` on one randomly chosen orb**, which invokes **`ExperienceOrb.playerTouch(this)`**.
3. **`ExperienceOrb.playerTouch`** (server-side: `ServerPlayer` only) enforces **`takeXpDelay == 0`**, sets **`takeXpDelay = 2`**, calls **`player.take(this, 1)`** (client animation packet path), applies repair-via-XP if applicable, **`giveExperiencePoints`**, decrements count, discards when empty.

**Takeaway:** Final pickup is **not** modeled as “orb physics collides with strict `player.getBoundingBox()`”. It is “orb’s bounding box intersects the **player’s pickup volume**” as defined in **`Player.aiStep()`**.

---

## A. Which player the orb follows (homing target)

| Topic | Source fact |
|--------|----------------|
| **Method** | `ExperienceOrb.followNearbyPlayer()` (private), called from `ExperienceOrb.tick()`. |
| **How often** | Once per **game tick** per orb (when the normal `tick()` body runs). |
| **Target when `followingPlayer` is null / invalid** | `Player nearestPlayer = this.level().getNearestPlayer(this, 8.0);` |
| **Invalid / refresh** | If current `followingPlayer` is `null`, spectator, or `followingPlayer.distanceToSqr(this) > 64.0` (i.e. farther than **8** blocks entity-position to entity-position), selection runs again. |
| **`getNearestPlayer` logic** | `EntityGetter.getNearestPlayer(Entity source, double maxDist)` → uses `source.getX/Y/Z()`, compares `player.distanceToSqr(x, y, z) < maxDist * maxDist`, picks closest among players matching `EntitySelector.NO_SPECTATORS` (via the `maxDist, false` overload). |
| **Line of sight** | **No** — pure distance + spectator filter. |
| **Blocks (trapdoors, partial shapes)** | **Not consulted** for nearest-player selection. |

**Constants in `ExperienceOrb`:** The field `MAX_FOLLOW_DIST = 8` exists; **`followNearbyPlayer` uses literals `8.0` and `64.0`** in 26.1 sources (the named constant is unused in that class).

---

## B. How the orb moves toward the player

**Location:** `ExperienceOrb.tick()` → **`followNearbyPlayer()`** before **`move(...)`**.

**Homing vector** (after `followingPlayer != null`):

```text
delta = (player.x - orb.x,
         player.y + player.getEyeHeight() / 2.0 - orb.y,
         player.z - orb.z)
length = delta.lengthSqr()
power = 1.0 - sqrt(length) / 8.0
deltaMovement += normalize(delta) * (power * power * 0.1)
```

**Meaning:**

- Horizontal: **player feet X/Z** vs **orb X/Z** (`getX` / `getZ`).
- Vertical target Y: **`player.getY() + player.getEyeHeight() / 2.0`** (between feet and eye height, not eyes alone or hitbox center explicitly).
- Strength falls off with distance; **no separate “pickup snap”** branch here — proximity only affects `power`.

**Collision note:** Orb uses `level().noCollision(getBoundingBox())` to decide whether to apply gravity; that affects **movement vs blocks**, not LOS to the player.

---

## C. What triggers final pickup (XP award)

### Detection (player side)

In **`Player.aiStep()`** (after `super.aiStep()`), when `getHealth() > 0` and not spectator:

```text
If passenger with non-removed vehicle:
  pickupArea = getBoundingBox().minmax(vehicle.getBoundingBox()).inflate(1.0, 0.0, 1.0)
Else:
  pickupArea = getBoundingBox().inflate(1.0, 0.5, 1.0)

entities = level().getEntities(this, pickupArea)
```

- Non-orb entities: **`touch(each)`** (immediate).
- XP orbs: collect all in list; if non-empty, **`touch(Util.getRandom(orbs, random))`** — **one random orb per tick**.

### Callback chain

- `Player.touch(Entity entity)` → **`entity.playerTouch(this)`**
- For XP: **`ExperienceOrb.playerTouch(Player)`** → requires **`ServerPlayer`**, **`takeXpDelay == 0`**, then award / packet / count.

### Inflate semantics

`AABB.inflate(dx, dy, dz)` in 26.1 **expands each face** by that amount: `min -= d`, `max += d` per axis. So **`inflate(1.0, 0.5, 1.0)`** is **±1 block** on X/Z and **±0.5 block** on Y from the **player collision box**.

### `LivingEntity.take(Entity, int)`

Called from `ExperienceOrb.playerTouch` to send **`ClientboundTakeItemEntityPacket`** for tracking players — **not** the geometric overlap test. Overlap is entirely **`Player.aiStep`** + **`getEntities`**.

---

## D. Trapdoors and partial blocks

**From code:** The pickup query does **not** raycast or check block shapes between player and orb. If the **orb’s `getBoundingBox()`** intersects the **`pickupArea` AABB**, the orb can be selected.

- **Trapdoors between** player and orb **do not block** this query.
- Block **collision shapes matter** for **orb motion** (`move`, `noCollision`), not for **`Player.aiStep` pickup enumeration**.

---

## E. XP_Stream modeling

### Closest source-accurate sibling/candidate predicate

When one orb has just been picked up and the mod wants “which orbs would vanilla treat as in range”:

- Use the **same pickup volume as `Player.aiStep`**:  
  **`player.getBoundingBox().inflate(1.0, 0.5, 1.0)`** (and the **passenger + vehicle `minmax` + inflate(1,0,1)** branch if parity with mounted players matters).
- Test **intersection with each candidate orb’s `getBoundingBox()`** (same as `getEntities` behavior).

This is **not** the same as **strict** `player.getBoundingBox()` overlap; that mismatch explains setups where vanilla collects orbs **slightly above** the head (within the inflated Y extent) **without** requiring un-inflated hitbox intersection.

### Vanilla rate limit (design note)

Only **one random** orb per player per tick gets `playerTouch` from this loop; siblings can remain until later ticks.

---

## Recommendation for XP_Stream candidate selection

**Use the vanilla pickup AABB (`inflate(1.0, 0.5, 1.0)` + passenger special-case) as the baseline for “sibling” orbs**, not strict player hitbox overlap and not orb–player physics collision. Any additional configurable margin (e.g. `burstPickupMargin`) is intentionally **beyond** vanilla and should be documented as tuning on top of this baseline (see `Docs/inflate_playerAAB.md`).

---

## Source artifacts

Rebuild or inspect:  
`%USERPROFILE%\.gradle\caches\fabric-loom\minecraftMaven\net\minecraft\minecraft-common-deobf\26.1\minecraft-common-deobf-26.1-sources.jar`

**Primary classes:** `ExperienceOrb`, `Player`, `EntityGetter`, `AABB`, `LivingEntity` (`take`), `EntitySelector`.
