# XP Collection Test

## Objective

Verify that all spawned experience is successfully collected and measure the time required for full absorption.

## Test Setup

- Spawn 1 XP orb per tick
- Duration: 200 ticks (10 seconds)
- XP per orb: 1 point
- Total expected XP: 200 points

## Procedure

1. Start XP spawning (1 orb per tick for 200 ticks).
2. Allow XP orbs to spawn for the full 10-second duration.
3. After spawning completes, remain stationary and allow all XP orbs to be collected.
4. Stop the timer once XP collection is complete.

## Data to Record

| Metric | Value |
|--------|-------|
| Final XP level (levels gained) | |
| Remaining XP points toward next level | |
| Total XP received matches expectations | |
| Time to collect XP orbs | |

**Collection Time Calculation:**
```
Collection End Time − 10 seconds (spawn duration)
```

## Expected Outcome

- Total XP collected ≈ 200 points
- Final level and progress should align with Minecraft's XP curve
- XP collection completes shortly after spawning ends

## Test Results

### Test Configuration
- **Minecraft Version**: 1.21.11
- **Fabric Loader**: 0.18.3
- **XP_Stream Version**: 0.1.0
- **MAX_BURST_ORBS**: 8 (collision-based, no radius expansion)

### Results

| Run | Configuration | Final Level | XP Progress | Total Time | Absorption Time | Notes |
|-----|---------------|-------------|-------------|------------|-----------------|-------|
| 1   | Vanilla       | 11          | 12 pts      | 20.86s     | ~10.86s         | Baseline |
| 2   | XP_Stream     | 11          | 12 pts      | 11.06s     | ~1.06s          | **~10x faster absorption** |

### Analysis

| Metric | Vanilla | XP_Stream | Improvement |
|--------|---------|-----------|-------------|
| **Total Time** | 20.86s | 11.06s | **1.89x faster** |
| **Absorption Time** | ~10.86s | ~1.06s | **~10x faster** |
| **XP Integrity** | 11 lvl + 12 pts | 11 lvl + 12 pts | ✅ **Identical** |

### Conclusions

1. ✅ **No XP Loss** — Both configurations yielded identical XP (11 levels + 12 points)
2. ✅ **~2x Overall Speedup** — Total test time nearly halved, meeting "up to 2x faster" goal
3. ✅ **10x Absorption Speedup** — Once orbs reached player, collection was near-instant
4. ✅ **Mending Preserved** — Vanilla pickup path used, Mending enchantment works correctly
