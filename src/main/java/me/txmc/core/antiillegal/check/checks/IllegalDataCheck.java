package me.txmc.core.antiillegal.check.checks;

import me.txmc.core.antiillegal.check.Check;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Check for illegal NBT data on items.
 * <p>
 * Part of the 6b6t anti-illegal system.
 * </p>
 */
public class IllegalDataCheck implements Check {
    @Override
    public boolean check(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return meta != null && !meta.getPersistentDataContainer().isEmpty();
    }

    @Override
    public boolean shouldCheck(ItemStack item) {
        return item != null && item.hasItemMeta();
    }

    @Override
    public void fix(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        for (NamespacedKey key : meta.getPersistentDataContainer().getKeys()) {
            meta.getPersistentDataContainer().remove(key);
        }
        item.setItemMeta(meta);
    }
}
