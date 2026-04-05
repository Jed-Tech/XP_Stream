# Saturation Regen

**Let saturation trigger healing — full hunger bar not required.**

Vanilla often makes **saturation** feel underused for healing. You can eat great food, have saturation available, but you still won't heal because Vanilla wants you to be "full" first.

Saturation Regen keeps **vanilla healing behavior and vanilla saturation drain**, but lets natural regen start sooner — so topping off every drumstick isn’t required.

---

## ⚙️ What It Does

- Allows natural regeneration when:
  - **Saturation > 0**
  - and Hunger is above 6 points (3 drumsticks) -- *this is configurable*
- Uses **vanilla timing and exhaustion** — no extra healing loops
- Respects the **`naturalRegeneration`** gamerule
- **Server-side Fabric** — works automatically with connected vanilla clients

---

## 🧠 Vanilla-Plus Design

This mod does **not**:
- Add new healing mechanics
- Override potion or effect-based healing
- Change regen speed

It only adjusts **when vanilla regen is allowed to start**, keeping saturation meaningful in real gameplay.

---

## 📦 Installation

1. Install Fabric Loader
2. Drop the mod jar into your `mods` folder
3. Start the server