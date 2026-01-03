# XP Orb Absorption Test

This is a simple in-game test to summon 200 orbs.

## Setup

One-time setup (run once):

```minecraft
/scoreboard objectives add xpTimer dummy
```

## Command Block Layout

```
[Button] → [Impulse] → [Repeat] → [Chain]
```

## Command Blocks

### 1. Start Block 1️⃣

**Type:** Impulse  
**Redstone:** Needs Redstone  
**Conditional:** Unconditional

**Command:**

```minecraft
/scoreboard players set @p xpTimer 200
```

Triggered by the button.

### 2. XP Spawn Block 2️⃣

**Type:** Repeat  
**Redstone:** Always Active  
**Conditional:** Unconditional

**Command:**

```minecraft
/execute if score @p xpTimer matches 1.. run summon minecraft:experience_orb ~ ~ ~ {Value:1}
```

- Runs once per tick
- Spawns XP at this command block
- Only runs while xpTimer > 0

### 3. Countdown Block 3️⃣

**Type:** Chain  
**Redstone:** Always Active  
**Conditional:** Unconditional

**Command:**

```minecraft
/scoreboard players remove @p[scores={xpTimer=1..}] xpTimer 1
```

- Decrements timer every tick
- When it reaches 0, XP spawning stops automatically

## Optional Command Blocks

### Clear Experience Levels

**Type:** Impulse  
**Redstone:** Needs Redstone  
**Conditional:** Unconditional

**Command:**

```minecraft
/xp set @p 0 levels
```

Clears all experience levels from the player.

### Clear XP Points

**Type:** Impulse  
**Redstone:** Needs Redstone  
**Conditional:** Unconditional

**Command:**

```minecraft
/xp set @p 0 points
```

Clears all experience points from the player.

### Give Test Sword with Mending

**Type:** Impulse  
**Redstone:** Needs Redstone  
**Conditional:** Unconditional

**Command:**

```minecraft
/give @p diamond_sword[damage=780,enchantments={levels:{mending:1}}]
```

Gives a half-healed diamond sword with Mending enchantment for testing.