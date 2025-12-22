# XP_Stream — Beta Release Plan

This document outlines the beta release strategy for **XP_Stream**, with the goal of validating behavior, preserving vanilla feel, and building confidence toward a stable 1.0 release.

The beta focuses on **absorbing XP faster** while keeping the XP shower intact and eliminating the ankle-swarm effect.

---

## Beta Objectives

- Validate that XP absorption is noticeably faster in real gameplay
- Confirm vanilla mechanics (XP values, Mending, ownership) are preserved
- Ensure behavior is intuitive in single-player and multiplayer
- Identify edge cases before locking the 1.0 contract
- Keep the implementation simple and maintainable

---

## v0.1 — Initial Beta (Core Behavior)

### Focus
Deliver immediate player-facing value with the smallest possible surface area.

### Behavior
- XP orbs spawn and move exactly as in vanilla
- Pickup delay remains unchanged
- When an XP orb collides with a player:
  - The orb is collected normally
  - Additional nearby XP orbs may be collected in the same pickup event
- XP is applied using vanilla mechanics
- A single vanilla XP pickup sound is played per pickup burst

### Goals
- XP feels faster to absorb
- XP shower visuals remain intact
- Ankle-swarm behavior is significantly reduced

### Out of Scope
- Configuration options
- Performance tuning
- Orb merging or clumping
- Long-range XP attraction

### Success Criteria
- No XP loss or duplication
- Mending behaves exactly like vanilla
- No noticeable gameplay surprises
- No performance regression compared to vanilla