package me.txmc.core.antiillegal.listeners;

import me.txmc.core.antiillegal.AntiIllegalMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Handles miscellaneous item checks.
 */
public class MiscListeners implements Listener {
    private final AntiIllegalMain main;

    public MiscListeners(AntiIllegalMain main) {
        this.main = main;
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        ItemStack item = event.getEntity().getItemStack();
        main.checkFixItem(item, null);
    }
}
