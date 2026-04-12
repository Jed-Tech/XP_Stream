package com.jedtech.saturation_regen.mixin;

import com.jedtech.saturation_regen.SaturationRegenConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Keep NeoForge behavior aligned with the Fabric implementation: widen the fast natural-regen branch to
 * {@code food > penalty && food < 20} so 7-19 matches the same 10-tick cadence as full hunger when saturation allows.
 */
@Mixin(FoodData.class)
public abstract class FoodDataMixin {

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 20))
    private int saturationRegen$replaceFullFoodBarThresholdForSaturationRegen(int original, ServerPlayer player) {
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
