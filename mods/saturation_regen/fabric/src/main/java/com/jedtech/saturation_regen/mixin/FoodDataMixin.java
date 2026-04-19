package com.jedtech.saturation_regen.mixin;

import com.jedtech.saturation_regen.SaturationRegenConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Minecraft 1.21.1 {@link FoodData#tick(ServerPlayer)} (Mojang mappings): vanilla orders exhaustion/saturation/hunger
 * updates, then evaluates natural regen. The <strong>fast</strong> saturation-driven branch (10-tick cadence when hurt)
 * normally requires {@code food >= 20} plus saturation; a <strong>slower</strong> branch covers roughly {@code food >= 18}.
 * <p>
 * This mod replaces the {@code 20} in the fast-branch hunger check so that, when {@code food > penalty && food < 20},
 * the fast branch can run (threshold becomes {@code penalty + 1}) — same cadence and exhaustion as vanilla’s fast path
 * when conditions match. At or below the penalty, return {@code 21} so the fast branch does not apply. Hunger 18–19
 * stay in the widened range (see 0.2.0 release notes) rather than falling back to vanilla’s slower step alone.
 * <p>
 * Server-only: no change on logical client (preserves spec: server-authoritative regen).
 */
@Mixin(FoodData.class)
public abstract class FoodDataMixin {

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 20))
    private int saturationRegen$replaceFullFoodBarThresholdForSaturationRegen(int original, ServerPlayer player) {
        if (player.level().isClientSide()) {
            return original;
        }
        FoodData self = (FoodData) (Object) this;
        int food = self.getFoodLevel();
        int penalty = SaturationRegenConfig.get().getRegenHungerPenaltyLevel();
        if (food >= 20) {
            return 20;
        }
        if (food > penalty && food < 20) {
            return penalty + 1;
        }
        return 21;
    }
}
