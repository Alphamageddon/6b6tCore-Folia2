package com.blbilink.blbilogin.modules.dupe;

import org.bukkit.Material;
import org.bukkit.entity.ChestBoat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestBoatDupeListener implements Listener {

    public ChestBoatDupeListener(long cooldownSeconds) {
        // Cooldown logic removed
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        if (!(event.getExited() instanceof Player player)) return;
        if (!(event.getVehicle() instanceof ChestBoat chestBoat)) return;

        Inventory inv = chestBoat.getInventory();

        for (ItemStack item : inv.getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                player.getWorld().dropItemNaturally(player.getLocation(), item.clone());
            }
        }
    }
}
