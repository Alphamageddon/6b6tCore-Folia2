package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockProtection implements Listener {
    private final int radius = 5;
    private final Location center;

    public BlockProtection() {
        String worldName = Configvar.config.getString("locationPos.world");
        if (worldName != null && !worldName.isEmpty()) {
            this.center = new Location(
                    Bukkit.getWorld(worldName),
                    Configvar.config.getDouble("locationPos.x"),
                    Configvar.config.getDouble("locationPos.y"),
                    Configvar.config.getDouble("locationPos.z")
            );
        } else {
            this.center = null;
        }
    }

    private boolean inProtectedZone(Location loc) {
        if (center == null) return false;
        if (loc.getWorld() == null || !loc.getWorld().equals(center.getWorld())) return false;
        return Math.abs(loc.getX() - center.getX()) <= radius &&
                Math.abs(loc.getY() - center.getY()) <= radius &&
                Math.abs(loc.getZ() - center.getZ()) <= radius;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Material type = event.getBlockPlaced().getType();
        if (type == Material.BEDROCK || type == Material.BARRIER) {
            event.setCancelled(true);
            return;
        }
        if (!event.getPlayer().isOp() && inProtectedZone(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().isOp() && inProtectedZone(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
}
