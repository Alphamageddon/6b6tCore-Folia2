package me.txmc.core.antiillegal.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Prevents stacked totems from bypassing normal limits.
 */
public class StackedTotemsListener implements Listener {
    @EventHandler
    public void onResurrect(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack main = player.getInventory().getItemInMainHand();
        if (main.getType() == Material.TOTEM_OF_UNDYING && main.getAmount() > 1) {
            main.setAmount(1);
        }
        ItemStack off = player.getInventory().getItemInOffHand();
        if (off.getType() == Material.TOTEM_OF_UNDYING && off.getAmount() > 1) {
            off.setAmount(1);
        }
    }
}
