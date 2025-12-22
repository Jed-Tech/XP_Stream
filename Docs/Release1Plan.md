## The Plan after Beta 1
The following plan is a rough guide. The Actual plan followed will depend on the results of the Beta 1 release and the objectives achieved.

## v0.2 — Stability & Edge Case Beta

### Focus
Harden the core behavior under real-world conditions.

### Additions
- Defensive handling for:
  - Large XP bursts (farms, grinders)
  - Multiple players near the same XP
  - Repeated pickup events in the same tick
- Clear internal limits to prevent runaway behavior
- Optional debug logging for troubleshooting

### Goals
- Stable behavior in high-XP environments
- Predictable multiplayer outcomes
- Clean server logs during normal play

### Success Criteria
- No reports of inconsistent XP pickup
- No multiplayer complaints about “stolen” XP
- Behavior feels consistent across environments

---

## v0.3 — Optional Configuration Beta

### Focus
Provide minimal admin control without expanding scope.

### Additions
- Simple configuration options (e.g. enable/disable, small tuning values)
- Sensible defaults that require no configuration

### Goals
- Increase server admin confidence
- Maintain drop-in usability

### Success Criteria
- Defaults feel correct out of the box
- Configuration changes behave as expected
- No added complexity for typical users

---

## v1.0 — Stable Release Criteria

XP_Stream will be considered ready for **1.0** when:

- Core XP absorption behavior is locked and well-understood
- No major behavioral bugs remain
- Documentation is finalized
- The design philosophy is consistently upheld
- Only bug fixes or non-breaking polish remain

At 1.0, XP_Stream’s behavior becomes contractually stable.

---

## Feedback & Iteration

During beta:
- Player feedback is prioritized over feature requests
- Any change that risks breaking vanilla feel is rejected
- New ideas are deferred to a future major version

The goal of beta is confidence, not expansion.