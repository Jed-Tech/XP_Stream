# `specs/`

This directory holds **change folders** for SPAR work. Each subfolder under a stage directory is one named change. Repository root = `<root>`. Paths are `specs/<stage>/<change-name>/`.

## Layout

| Folder | Role |
|--------|------|
| **`active/`** | Changes in the SPAR pipeline (specify -> plan -> act). **Retain** archives from here to **`completed/`**. |
| **`completed/`** | Closed-out changes: the folder moved here as a unit after retention; no duplicate under **`active/`**. |

`<change-name>` is one folder per change: short, descriptive, and safe as a directory name.

In each change folder, the spec file is **`<change-name>_spec.md`** (underscore before `spec.md`; spelling matches the folder). **`plan.md`** keeps that name.

Use the SPAR skills for phase guidance: `spar-specify`, `spar-plan`, `spar-act`, and `spar-retain`.
