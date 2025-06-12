package me.txmc.core.antiillegal.check.checks;

import me.txmc.core.antiillegal.check.Check;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

/**
 * Removes excessively strong custom potion effects.
 */
public class PotionCheck implements Check {
    private static final int MAX_AMPLIFIER = 10;
    private static final int MAX_DURATION = 20 * 60 * 8; // 8 minutes

    @Override
    public boolean check(ItemStack item) {
        if (!(item.getItemMeta() instanceof PotionMeta meta)) return false;
        for (PotionEffect effect : meta.getCustomEffects()) {
            if (effect.getAmplifier() > MAX_AMPLIFIER || effect.getDuration() > MAX_DURATION) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldCheck(ItemStack item) {
        return item != null && item.getItemMeta() instanceof PotionMeta;
    }

    @Override
    public void fix(ItemStack item) {
        if (!(item.getItemMeta() instanceof PotionMeta meta)) return;
        for (PotionEffect effect : new ArrayList<>(meta.getCustomEffects())) {
            if (effect.getAmplifier() > MAX_AMPLIFIER || effect.getDuration() > MAX_DURATION) {
                meta.removeCustomEffect(effect.getType());
            }
        }
        item.setItemMeta(meta);
    }
}
