package com.jedtech.saturation_regen.mixin;

import com.jedtech.saturation_regen.fabric.SaturationRegenConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Minecraft 26.1.1 {@link FoodData#tick(ServerPlayer)}: exhaustion drain; then if {@code naturalRegen && saturation > 0
 * && hurt && food >= 20}, fast sat-based heal every 10 ticks; else if {@code naturalRegen && food >= 18 && hurt}, slow
 * heal every 80 ticks (+6 exhaustion each); …
 * <p>
 * If we only lowered the {@code >= 20} threshold globally, food 18–19 with saturation would incorrectly take the
 * <strong>fast</strong> branch. In vanilla, 18–19 stays on the <strong>slow</strong> branch (first branch fails
 * because food &lt; 20), so exhaustion per heal matches vanilla. We therefore choose the comparison constant
 * dynamically: keep fast-branch behavior for {@code food >= 20} and for {@code food > penalty && food < 18}, but use
 * an impossible threshold ({@code 21}) for 18–19 so the slow path still applies.
 */
@Mixin(FoodData.class)
public abstract class FoodDataMixin {

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 20))
    private int saturationRegen$replaceFullFoodBarThresholdForSaturationRegen(int original, ServerPlayer player) {
        FoodData self = (FoodData) (Object) this;
        int food = self.getFoodLevel();
        int p = SaturationRegenConfig.get().getRegenHungerPenaltyLevel();
        if (food >= 20) {
            return 20;
        }
        if (food > p && food < 18) {
            return p + 1;
        }
        return 21;
    }
}
