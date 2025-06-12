package me.txmc.core.antiillegal.check.checks;

import me.txmc.core.antiillegal.check.Check;
import org.bukkit.inventory.ItemStack;

/**
 * Ensures stacks do not exceed their maximum size.
 */
public class OverStackCheck implements Check {
    @Override
    public boolean check(ItemStack item) {
        return item.getAmount() > item.getMaxStackSize();
    }

    @Override
    public boolean shouldCheck(ItemStack item) {
        return item != null && item.getMaxStackSize() > 0;
    }

    @Override
    public void fix(ItemStack item) {
        item.setAmount(item.getMaxStackSize());
    }
}
