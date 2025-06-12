package me.txmc.core.antiillegal.listeners;

import me.txmc.core.antiillegal.AntiIllegalMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Checks items that move through inventories.
 */
public class InventoryListeners implements Listener {
    private final AntiIllegalMain main;

    public InventoryListeners(AntiIllegalMain main) {
        this.main = main;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item != null) {
            main.checkFixItem(item, event);
        }
    }
}
