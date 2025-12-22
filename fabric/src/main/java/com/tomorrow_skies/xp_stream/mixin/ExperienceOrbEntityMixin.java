package com.tomorrow_skies.xp_stream.mixin;

import com.tomorrow_skies.xp_stream.XpStreamConstants;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * v0.1 plan (Approach B):
 * - Hook orb pickup at the collision moment.
 * - After vanilla pickup runs for the triggering orb, also pick up nearby orbs in a tight radius, capped.
 *
 * Cursor: You must confirm the correct Yarn method name/signature in 1.21.11.
 */
@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin {

    // Simple re-entrancy guard
    private static boolean xp_stream$inBurst = false;

    @Inject(method = "onPlayerCollision", at = @At("TAIL"))
    private void xp_stream$burstPickup(PlayerEntity player, CallbackInfo ci) {
        if (xp_stream$inBurst) return;
        if (!(player.getEntityWorld() instanceof ServerWorld serverWorld)) return;

        try {
            xp_stream$inBurst = true;

            Box box = player.getBoundingBox().expand(XpStreamConstants.EXTRA_PICKUP_RADIUS_BLOCKS);

            // IMPLEMENTATION NOTE:
            // - Query nearby ExperienceOrbEntity in 'box'
            // - For up to MAX_EXTRA_ORBS_PER_EVENT:
            //   - invoke the vanilla orb pickup path for each orb (so Mending works)
            //   - discard the orb
            // - Avoid sound spam (ideally rely on the vanilla sound from the initial orb pickup)

        } finally {
            xp_stream$inBurst = false;
        }
    }
}
