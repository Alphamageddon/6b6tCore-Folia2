package me.txmc.core.antiillegal.check.checks;

import me.txmc.core.antiillegal.check.Check;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Caps item damage to the maximum allowed by the material.
 */
public class DurabilityCheck implements Check {
    @Override
    public boolean check(ItemStack item) {
        if (!(item.getItemMeta() instanceof Damageable meta)) return false;
        int max = item.getType().getMaxDurability();
        return meta.getDamage() > max;
    }

    @Override
    public boolean shouldCheck(ItemStack item) {
        return item != null && item.getType().getMaxDurability() > 0 && item.getItemMeta() instanceof Damageable;
    }

    @Override
    public void fix(ItemStack item) {
        ItemMeta im = item.getItemMeta();
        if (!(im instanceof Damageable meta)) return;
        int max = item.getType().getMaxDurability();
        if (meta.getDamage() > max) meta.setDamage(max);
        item.setItemMeta(im);
    }
}
