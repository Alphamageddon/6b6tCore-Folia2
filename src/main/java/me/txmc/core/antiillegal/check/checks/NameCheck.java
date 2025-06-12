package me.txmc.core.antiillegal.check.checks;

import me.txmc.core.antiillegal.check.Check;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Simple display name length limiter.
 */
public class NameCheck implements Check {
    private final int maxLength;

    public NameCheck(ConfigurationSection section) {
        this.maxLength = section != null ? section.getInt("AntiIllegal.MaxNameLength", 40) : 40;
    }

    @Override
    public boolean check(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasDisplayName() && meta.getDisplayName().length() > maxLength;
    }

    @Override
    public boolean shouldCheck(ItemStack item) {
        return item != null && item.hasItemMeta();
    }

    @Override
    public void fix(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName() && meta.getDisplayName().length() > maxLength) {
            meta.setDisplayName(meta.getDisplayName().substring(0, maxLength));
            item.setItemMeta(meta);
        }
    }
}
