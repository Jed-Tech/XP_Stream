---
name: plan-change
description: >-
  Turns a change idea into a stable spec.md and actionable tasks.md under
  Docs/<scope>/Specs/<change-name>/. Opens with a fixed prompt for goals/context,
  then conversationally resolves scope and naming; proceeds through workflow
  steps in order. Tone: concise, cheerful, inquisitive. Does not create the Specs
  folder, spec.md, or tasks.md until scope and change-name are confirmed. Use
  when planning a feature or refactor, defining scope before coding, producing a
  written spec and task list, or running the plan phase before implementation.
---

# Plan change

## Mandatory first turn

**Start by asking — use this wording (or trivially equivalent):**

> I see you would like to start ideation and planning for a change. Can you provide me with some context or your goals for the change?

If the user already dropped hints in the same thread (e.g. a working title), add at most **one short** acknowledging clause, then still use the prompt above so goals stay explicit.

**Do not** create files, **do not** draft `spec.md` or `tasks.md`, and **do not** interrogate for scope, folder name, or success criteria in full — save that for after they reply.

Until scope and change name are confirmed or clearly settled: **do not** create `Docs/<scope>/Specs/<change-name>/` or write `spec.md` / `tasks.md`. A response that creates files or fabricates spec/tasks before that **does not** follow this skill.

## Voice

Be **concise**, **cheerful**, and **inquisitive** — like a helpful teammate, not a form or checklist read aloud.

Convert discussion into durable context and a clear execution plan.

## Conversation flow

### After the user responds

1. **Clarify intent and naming** (workflow §1): refine `<change-name>` as needed through dialogue.
2. **Resolve scope** (workflow §2): conversationally confirm `xp_stream` | `saturation_regen` | `_Repo` if still unclear; only ask what is missing.
3. **Then proceed sequentially** through workflow §3 onward (create folder → context → ideate → spec → …). Do not skip ahead (e.g. no `tasks.md` before `spec.md` is stable).

## Outputs

Create:

`Docs/<scope>/Specs/<change-name>/`

- `spec.md` — intent, constraints, decisions (not step-by-step)
- `tasks.md` — ordered, concrete, verifiable execution steps

**Scope** must be one of: `xp_stream`, `saturation_regen`, `_Repo`. If unclear, stop and ask.

---

## Workflow

### 1. Clarify intent and naming

- Understand the goal from discussion; help refine `<change-name>`.
- **Do not create files yet.** (See **Mandatory first turn** and **Conversation flow** for opener and sequencing.)

### 2. Resolve scope

Target: `xp_stream` | `saturation_regen` | `_Repo`. If ambiguous after the user’s first substantive reply, resolve conversationally before §3.

### 3. Create change folder

When scope and name are fixed:

1. Create `Docs/<scope>/Specs/<change-name>/`
2. Create empty `spec.md` and `tasks.md`, then populate in later steps.

### 4. Context gathering

Review relevant code and docs; use Context7 MCP (preferred) or web research when helpful.

### 5. Ideate

Explore approaches, pick a direction, note risks and unknowns.

### 6. Draft `spec.md`

Use this structure:

```markdown
# <change-name>

## Summary
## Problem
## Goal
## Scope
## Non-Goals
## Constraints
## Success Criteria
## Open Questions
## Decisions
## Documentation Impact
```

### 7. Surface unknowns

List gaps; ask the user where needed.

### 8. Finalize `spec.md`

Incorporate user input. Ensure intent is clear, scope is correct, and success criteria are testable. **Treat the spec as stable after this step.**

### 9. Create `tasks.md`

Only after `spec.md` is stable. Use:

```markdown
# Tasks - <change-name>

## Implementation Plan Summary
- High-level approach
- Major steps or phases
- Key sequencing or constraints

## Tasks
- [ ] Task 1
- [ ] Task 2
- [ ] Task 3

## Notes
- Validation ideas
- Risks
- Follow-ups
```

**Task rules:** ordered, concrete, verifiable; avoid vague items. Keep both files lightweight.

---

## Stop conditions

Stop and ask if:

- Scope is unclear
- Requirements are too vague for reliable tasks

---

## Completion

Plan phase is complete when:

- `spec.md` is stable
- `tasks.md` is actionable
- No critical unknowns remain

Then:

1. Summarize: scope, spec status, tasks readiness, remaining questions.
2. Ask: **Proceed to implementation?**
3. If yes: begin the **`implement-change`** skill. If not using that skill, execute `tasks.md` following repo conventions.

---

## Rules (summary)

| Artifact | Contains |
|----------|----------|
| `spec.md` | Intent, constraints, decisions — **not** step-by-step |
| `tasks.md` | Execution steps only — **after** spec is stable |

Do not generate `tasks.md` until `spec.md` is finalized.
