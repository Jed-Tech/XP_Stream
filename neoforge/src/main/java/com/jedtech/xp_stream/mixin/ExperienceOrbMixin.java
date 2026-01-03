package com.jedtech.xp_stream.mixin;

import com.jedtech.xp_stream.XpStreamConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Burst Pickup Mixin for ExperienceOrb (NeoForge/Mojmap).
 * 
 * When an XP orb is picked up, this mixin also collects other orbs that are
 * currently colliding with the player, up to a configurable cap. This eliminates
 * the vanilla 2-tick delay between picking up orbs that arrived simultaneously.
 * 
 * Design: Collision-based (no radius expansion) to preserve the vanilla feel
 * of orbs flying toward the player. Only orbs that have actually reached the
 * player get collected.
 */
@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {

    /**
     * Re-entrancy guard to prevent infinite loops.
     * When we call playerTouch on burst orbs, this mixin would fire again.
     * The guard ensures we only process the initial triggering orb's burst.
     */
    @Unique
    private static boolean xp_stream$inBurst = false;

    @Inject(method = "playerTouch", at = @At("TAIL"))
    private void xp_stream$burstPickup(Player player, CallbackInfo ci) {
        // Skip if we're already processing a burst (re-entrancy guard)
        if (xp_stream$inBurst) return;
        
        // Get config (loaded during mod init)
        XpStreamConfig config = XpStreamConfig.get();
        
        // Skip if burst is disabled
        if (config.getMaxBurstOrbs() <= 0) return;
        
        // Server-side only
        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        try {
            xp_stream$inBurst = true;
            
            ExperienceOrb self = (ExperienceOrb)(Object)this;
            
            // Query orbs currently colliding with the player (no radius expansion)
            AABB playerBox = player.getBoundingBox();
            
            List<ExperienceOrb> collidingOrbs = serverLevel.getEntitiesOfClass(
                ExperienceOrb.class,
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
            for (ExperienceOrb orb : collidingOrbs) {
                if (picked >= config.getMaxBurstOrbs()) break;
                
                // Reset the pickup delay so vanilla logic processes the orb.
                // Without this, the delay set by the triggering orb would block
                // all burst pickups.
                player.takeXpDelay = 0;
                
                // Trigger vanilla pickup path (preserves Mending, XP award, etc.)
                orb.playerTouch(player);
                
                picked++;
            }
            
        } finally {
            xp_stream$inBurst = false;
        }
    }
}




