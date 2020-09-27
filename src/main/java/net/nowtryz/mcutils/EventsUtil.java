package net.nowtryz.mcutils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.Nullable;

public class EventsUtil {
    /**
     * Find the real damager, find the shooter if it was a projectile
     * @param event the damage event
     * @return the real damager
     */
    public static @Nullable Entity getRealDamager(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) event.getDamager()).getShooter();
            if (shooter instanceof Entity) return (Entity) shooter;
            return null;
        } else return event.getDamager();
    }
}
