# XP_Stream Release Plan

## v0.1.0 — Core Beta (Current)

### Delivered
- Collision-based burst pickup with configurable cap
- JSON configuration (`maxBurstOrbs`, `debug`)
- Mending compatibility via vanilla pickup path
- Re-entrancy guard to prevent infinite loops

### Test Results

| Scenario | Vanilla | XP_Stream | Speedup |
|----------|---------|-----------|---------|
| 1 orb/tick × 200 ticks | 10.86s | 1.06s | ~10x |
| 200 orbs instant | ~20s+ | 4.2s | ~5x |

- XP integrity confirmed (11 levels + 12 points in all tests)
- No XP loss observed

### Known Limitations
- Single-player tested only
- Mending repair not explicitly verified (assumed via vanilla path)

---

## v0.2 — Multiplayer & Edge Case Beta

### Focus
Verify behavior in real-world multiplayer and high-stress scenarios.

### Testing Goals
- [ ] Multiple players near the same XP cluster
- [ ] Explicit Mending repair verification
- [ ] Sustained XP farm load (enderman farm, etc.)
- [ ] Server performance impact with many players

### Possible Additions
- Config option: `pickupRadiusExpansion` (if collision-only proves too tight)
- Config option: `enableMod` toggle for server admins
- Logging level control (info vs debug)

### Success Criteria
- No "stolen XP" complaints in multiplayer
- Mending repair works identically to vanilla
- Stable TPS under sustained XP load

---

## v1.0 — Stable Release

XP_Stream will be considered ready for 1.0 when:

- [ ] Multiplayer edge cases verified
- [ ] Mending explicitly tested and documented
- [ ] No major behavioral bugs remain
- [ ] Config options are finalized and documented
- [ ] README and user documentation complete

At 1.0, XP_Stream's behavior becomes contractually stable.
Future changes will be bug fixes only until v2.0.

---

## Design Principles

- **Preserve vanilla feel** — faster absorption, same mechanics
- **No XP loss** — every orb awards full value
- **Mending compatibility** — always use vanilla pickup path
- **Minimal footprint** — no dependencies beyond Fabric Loader
- **Configurable but sensible defaults** — works out of the box

---

## Feedback & Iteration

During beta:
- Player feedback is prioritized over feature requests
- Any change that risks breaking vanilla feel is rejected
- New ideas are deferred to v2.0+

The goal of beta is confidence, not expansion.
