package com.jedtech.xp_stream.mixin;

import com.jedtech.xp_stream.XpStreamConfig;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Burst Pickup Mixin for ExperienceOrbEntity.
 * 
 * When an XP orb is picked up, this mixin also collects other orbs that are
 * currently colliding with the player, up to a configurable cap. This eliminates
 * the vanilla 2-tick delay between picking up orbs that arrived simultaneously.
 * 
 * Design: Collision-based (no radius expansion) to preserve the vanilla feel
 * of orbs flying toward the player. Only orbs that have actually reached the
 * player get collected.
 */
@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin {

    /**
     * Re-entrancy guard to prevent infinite loops.
     * When we call onPlayerCollision on burst orbs, this mixin would fire again.
     * The guard ensures we only process the initial triggering orb's burst.
     */
    @Unique
    private static boolean xp_stream$inBurst = false;

    @Inject(method = "onPlayerCollision", at = @At("TAIL"))
    private void xp_stream$burstPickup(PlayerEntity player, CallbackInfo ci) {
        // Skip if we're already processing a burst (re-entrancy guard)
        if (xp_stream$inBurst) return;
        
        // Get config (loaded during mod init)
        XpStreamConfig config = XpStreamConfig.get();
        
        // Skip if burst is disabled
        if (config.getMaxBurstOrbs() <= 0) return;
        
        // Server-side only
        if (!(player.getEntityWorld() instanceof ServerWorld serverWorld)) return;

        try {
            xp_stream$inBurst = true;
            
            ExperienceOrbEntity self = (ExperienceOrbEntity)(Object)this;
            
            // Query orbs currently colliding with the player (no radius expansion)
            Box playerBox = player.getBoundingBox();
            
            List<ExperienceOrbEntity> collidingOrbs = serverWorld.getEntitiesByType(
                TypeFilter.instanceOf(ExperienceOrbEntity.class),
                playerBox,
                orb -> orb.isAlive() && orb != self
            );
            
            if (collidingOrbs.isEmpty()) return;
            
            if (config.isDebug()) {
                System.out.println("[XP_Stream] Burst pickup: " + 
                    Math.min(collidingOrbs.size(), config.getMaxBurstOrbs()) + 
                    " orbs (of " + collidingOrbs.size() + " colliding)");
            }
            
            int picked = 0;
            for (ExperienceOrbEntity orb : collidingOrbs) {
                if (picked >= config.getMaxBurstOrbs()) break;
                
                // Reset the pickup delay so vanilla logic processes the orb.
                // Without this, the delay set by the triggering orb would block
                // all burst pickups.
                player.experiencePickUpDelay = 0;
                
                // Trigger vanilla pickup path (preserves Mending, XP award, etc.)
                orb.onPlayerCollision(player);
                
                picked++;
            }
            
        } finally {
            xp_stream$inBurst = false;
        }
    }
}
















