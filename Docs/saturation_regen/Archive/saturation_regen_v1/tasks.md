# Tasks - saturation_regen_v1

## Implementation Plan Summary

- Read **Minecraft 26.1.1** Mojang sources for **`net.minecraft.world.food.FoodData#tick(Player)`** (via Loom `genSources` / IDE) and map **exact** conditions that gate **natural regeneration** vs. other tick logic (exhaustion, saturation drain, hunger drain).
- Add **one (or few) targeted Fabric mixin(s)** on `FoodData` so the **food-driven natural regen** condition allows **`getSaturationLevel() > 0`** **without** requiring the vanilla hunger threshold, while **not** duplicating the full `tick` method.
- Enforce **server-only** execution plus eligibility: **`getFoodLevel() > regenHungerPenaltyLevel`** (default **6** ⇒ **7+** food — see spec **Config**). **No HP floor.**
- Add **Fabric server config**: **`regenHungerPenaltyLevel`**; wire defaults from code / `common`.
- Register the mixin in **`saturation_regen.fabric.mixins.json`**. **NeoForge:** explicitly **not** in this task list (later change).
- Do **not** alter vanilla **heal amount per tick**, **timer cadence**, or **difficulty/peaceful** branches inside `FoodData#tick`—only the **eligibility** gate for the existing natural-regen branch.
- Validate with **in-game / dev server** spot checks per success criteria; **do not** publish or bump versions unless explicitly requested.

## Tasks

- [x] **Sources** — Run Loom `genSources` for `:mods:saturation_regen:fabric` and locate **`FoodData#tick`**. Document (in code comments only, briefly) which **sub-conditions** control natural healing vs. timer/exhaustion/saturation hunger drain.
- [x] **Mixin strategy** — Choose **Redirect**, **ModifyExpressionValue**, **ModifyVariable**, or **@Inject** at a **single** well-defined `@At` after inspecting bytecode-friendly anchors. Prefer **smallest diff** that preserves vanilla order of operations inside `tick`.
- [x] **Implement `FoodData` mixin** — Under `com.jedtech.saturation_regen.mixin`, implement the chosen injection(s) so:
  - [x] Natural regen can run when **saturation > 0** even if hunger is **below** vanilla’s required level for regen.
  - [x] **`naturalRegeneration`** gamerule is still respected (same check as vanilla).
  - [x] **No** new tick loop; **no** manual `heal()` outside the branch vanilla would use for natural regen (avoid double-healing).
  - [x] **Server-only**: apply only when the player is on the **logical server** (e.g. `ServerPlayer` / `!level().isClientSide()` as appropriate).
  - [x] **Food gate**: if `getFoodLevel() <=` **configured** `regenHungerPenaltyLevel` (default **6**), **do not** allow this food natural regen branch — i.e. require **`getFoodLevel() > regenHungerPenaltyLevel`**.
- [x] **Config** — Implement **Fabric server** config file (under `config/`) with **`regenHungerPenaltyLevel`** (int, default **6**; predicate **`getFoodLevel() >`**); document filename and **restart** to apply. Comment that default **6** means **above 6 only** (**7+** food), same idea as vanilla sprint. (In-mod tick **debug logging removed** for zero hot-path overhead.)
- [x] **Mixin registration** — Add the mixin class name to **`fabric/src/main/resources/saturation_regen.fabric.mixins.json`** (`mixins` array).
- [x] **Build** — `just` / Gradle: build **Fabric** `saturation_regen` artifact; fix any mixin remap or access issues.
- [x] **Manual validation** (single-player integrated server or dedicated + vanilla client):
  - [x] **`getFoodLevel() > 6`** (i.e. **7+** with default config), **not full**, saturation **> 0**, regen **on** → heals on **vanilla cadence**; exhaustion/saturation drain **feels** vanilla.
  - [x] **Food level** **6** (or **`<= regenHungerPenaltyLevel`**) vs **7+** border with saturation — OK.
  - [x] `naturalRegeneration` **false** → no food natural regen (gamerule respected).
  - [x] **Regeneration** effect and **Instant Health** still behave vanilla; **Poison/Wither** unchanged in spot checks.

## Notes

- **Validation ideas:** `/gamerule naturalRegeneration`, creative vs survival, `/effect` Regeneration II, give food with high saturation / low hunger bar; test border **6 vs 7** food (strict **above 6**).
- **Risks:** Wrong `@At` causes **double heal** or **skipped exhaustion** — always compare against **unmodded** `FoodData#tick` order. Client-side `FoodData` calls: ensure **no** client-only healing side effects.
- **Follow-ups:** **NeoForge** mixin + config parity; publishing/versioning per `Docs/publishing.md`.
- **Implementation (done):** `FoodDataMixin` uses **`@ModifyConstant`** on **`intValue = 20`** in `FoodData#tick` → **`regenHungerPenaltyLevel + 1`** so vanilla **`>=`** matches **`>` penalty**. Config: **`config/saturation_regen.json`**; **restart** to reload. Vanilla **26.1.1** `FoodData#tick(ServerPlayer)` only (no separate client tick). **No** `LivingEntity` / `FoodData` tick injects for logging.
