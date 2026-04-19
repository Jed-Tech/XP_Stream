package com.jedtech.saturation_regen.mixin;

import com.jedtech.saturation_regen.SaturationRegenConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Same contract as Fabric (Phase 4a): Minecraft 1.21.1 {@link FoodData#tick(ServerPlayer)} (Mojang mappings on NeoForge).
 * The fast saturation-driven natural-regen branch normally gates on {@code food >= 20}; this mixin replaces that {@code 20}
 * constant so eligible hunger levels below 20 (above the configured penalty) use the same fast cadence when saturation
 * allows. Hunger 18–19 stay in the widened range (see mod changelog 0.2.0).
 * <p>
 * Server-only: no change on logical client (loader parity with Fabric; server-authoritative regen).
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
