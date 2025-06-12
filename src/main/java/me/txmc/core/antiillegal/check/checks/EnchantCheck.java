package me.txmc.core.antiillegal.check.checks;

import me.txmc.core.antiillegal.check.Check;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Limits enchantments to their maximum allowed levels.
 */
public class EnchantCheck implements Check {
    @Override
    public boolean check(ItemStack item) {
        for (Map.Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()) {
            if (e.getValue() > e.getKey().getMaxLevel()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldCheck(ItemStack item) {
        return item != null && !item.getEnchantments().isEmpty();
    }

    @Override
    public void fix(ItemStack item) {
        Map<Enchantment, Integer> enchants = new HashMap<>(item.getEnchantments());
        enchants.forEach(item::removeEnchantment);
        enchants.forEach((ench, lvl) -> item.addEnchantment(ench, Math.min(lvl, ench.getMaxLevel())));
    }
}
