# 1.21.1 Support Plan Overview

This document defines the high-level goals, scope, and phase structure for bringing the current multi-mod workspace to a dedicated `1.21.1` support line.

This overview is intentionally non-technical. Detailed implementation plans will live in separate phase documents.

---

## Summary

We want a `1.21.1` branch that supports both current mods in the modern monorepo structure:

- `xp_stream`
- `saturation_regen`

The branch will be created from `main` so we can keep the current multi-mod workspace shape, documentation model, and release workflow. From there, we will retarget the branch for `1.21.1` and port the needed mod behavior in a controlled, phased way.

This is a focused support effort, not a general rewrite. The goal is to produce a clean `1.21.1` branch for both mods without taking on unrelated modernization work.

---

## Primary Goals

- Create a dedicated `1.21.1` branch based on the current monorepo layout.
- Bring `xp_stream` to `1.21.1` with the newer Vanilla Pickup behavior included.
- Bring `saturation_regen` to `1.21.1`.
- Support both Fabric and NeoForge for both mods.
- Keep the work organized so each phase can be planned, reviewed, and validated separately.

---

## Why Branch From `main`

We are choosing to branch from `main` because the current workspace already contains the structure we want to preserve:

- both mods already exist in the repo
- the monorepo layout is already established
- per-mod documentation and changelogs already exist
- the current release workflow already assumes a multi-mod workspace

Using `main` as the base gives us the right long-term project shape immediately. The `1.21.1` effort then becomes a scoped retargeting and porting effort rather than an attempt to grow the older `1.21.1` branch into today’s structure.

---

## Scope

### In Scope

- A dedicated `1.21.1` branch for the current multi-mod workspace
- `xp_stream` support on `1.21.1`
- `xp_stream` Vanilla Pickup behavior on `1.21.1`
- `saturation_regen` support on `1.21.1`
- Fabric and NeoForge support for both mods
- The documentation and release updates needed to make the branch understandable and maintainable

### Out of Scope

- Publishing or releasing as part of the planning phase
- Unrelated refactors
- Broad cleanup that does not help the `1.21.1` goal
- New feature work outside the `1.21.1` support effort
- Changing versioning or publishing strategy without a separate decision

---

## Sequencing Strategy

The work will be organized by phase, with one shared branch setup phase first, then mod-focused delivery.

Recommended execution order:

1. Establish the `1.21.1` branch and shared branch-level groundwork
2. Complete `xp_stream` for Fabric
3. Complete `xp_stream` for NeoForge
4. Complete `saturation_regen` for Fabric
5. Complete `saturation_regen` for NeoForge
6. Finish branch-wide cleanup, validation, and release readiness review

This order keeps one mod in focus at a time while still moving each mod to full loader parity before shifting attention.

---

## Phase Overview

### Phase 1: Branch Foundation

Define the branch, confirm the overall target, and prepare the shared `1.21.1` workspace direction.

### Phase 2: XP Stream

Bring `xp_stream` to `1.21.1`, including the newer Vanilla Pickup behavior, and complete both loaders.

### Phase 3: Saturation Regen

Bring `saturation_regen` to `1.21.1` and complete both loaders.

### Phase 4: Branch Cleanup and Validation

Make sure the branch is understandable, internally consistent, and ready for future release work.

Separate documents will define the detailed goals, tasks, and acceptance criteria for each phase.

---

## Success Criteria

This plan is successful when:

- both mods exist on the dedicated `1.21.1` branch
- both mods support Fabric and NeoForge on that branch
- `xp_stream` includes the intended Vanilla Pickup improvements
- the branch has clear supporting documentation
- the branch is ready for release planning without requiring a major second round of restructuring

---

## Planning Notes

- This plan is meant to keep the work focused and staged.
- We are intentionally separating the high-level plan from the phase-level documents.
- If the branch cleanup at the end is small, this effort may naturally become a fully polished `1.21.1` support branch.
