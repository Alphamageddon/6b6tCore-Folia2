package me.txmc.core.antiillegal.check.checks;

import com.google.common.collect.Multimap;
import me.txmc.core.antiillegal.check.Check;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Removes unsafe attribute modifiers from items.
 */
public class AttributeCheck implements Check {
    @Override
    public boolean check(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        Multimap<Attribute, AttributeModifier> mods = meta.getAttributeModifiers();
        return mods != null && !mods.isEmpty();
    }

    @Override
    public boolean shouldCheck(ItemStack item) {
        return item != null && item.hasItemMeta();
    }

    @Override
    public void fix(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        for (Attribute attr : Attribute.values()) {
            meta.removeAttributeModifier(attr);
        }
        item.setItemMeta(meta);
    }
}
