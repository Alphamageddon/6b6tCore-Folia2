package me.txmc.core.antiillegal.check.checks;

import me.txmc.core.antiillegal.check.Check;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

/**
 * Sanitizes written books to reasonable limits.
 */
public class BookCheck implements Check {
    private static final int MAX_PAGES = 50;
    private static final int MAX_PAGE_LENGTH = 32000;

    @Override
    public boolean check(ItemStack item) {
        if (!(item.getItemMeta() instanceof BookMeta meta)) return false;
        if (meta.getPageCount() > MAX_PAGES) return true;
        for (String page : meta.getPages()) {
            if (page.length() > MAX_PAGE_LENGTH) return true;
        }
        return false;
    }

    @Override
    public boolean shouldCheck(ItemStack item) {
        return item != null && item.getItemMeta() instanceof BookMeta;
    }

    @Override
    public void fix(ItemStack item) {
        if (!(item.getItemMeta() instanceof BookMeta meta)) return;
        while (meta.getPageCount() > MAX_PAGES) {
            meta.removePage(meta.getPageCount());
        }
        List<String> pages = meta.getPages();
        for (int i = 0; i < pages.size(); i++) {
            String text = pages.get(i);
            if (text.length() > MAX_PAGE_LENGTH) {
                pages.set(i, text.substring(0, MAX_PAGE_LENGTH));
            }
        }
        meta.setPages(pages);
        item.setItemMeta(meta);
    }
}
