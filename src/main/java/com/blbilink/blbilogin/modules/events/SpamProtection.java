package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class SpamProtection implements Listener {
    private final Map<UUID, Long> lastMessage = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        long interval = Configvar.config.getLong("spamProtectionCooldown");
        if (interval <= 0) return;

        Player player = event.getPlayer();
        long now = System.currentTimeMillis();
        long last = lastMessage.getOrDefault(player.getUniqueId(), 0L);
        if (now - last < interval) {
            event.setCancelled(true);
            player.sendMessage(plugin.i18n.as("msgSpamDetected", true, player.getName()));
            return;
        }
        lastMessage.put(player.getUniqueId(), now);
    }
}
