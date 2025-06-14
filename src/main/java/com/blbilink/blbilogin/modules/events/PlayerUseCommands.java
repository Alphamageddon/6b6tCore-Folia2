package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerUseCommands implements Listener {
    @EventHandler
    public void onPlayerCommandSend(PlayerCommandPreprocessEvent e) {
        List<String> cmds = new ArrayList<>(List.of("/login", "/l", "/reg", "/register", "/kill", "/suicide"));
        cmds.addAll(Configvar.config.getStringList("noLoginPlayerAllowUseCommand"));
        String message = e.getMessage();
        boolean isAllowedCommand = false;

        if (Configvar.noLoginPlayerList.contains(e.getPlayer().getName())) {
            for (String s : cmds) {
                if (message.startsWith(s)) {
                    isAllowedCommand = true;
                    break; // Stop loop once a matching command is found
                }
            }
            if (!isAllowedCommand && Configvar.config.getBoolean("noLoginPlayerCantUseCommand")) {
                e.setCancelled(true);
            }
        }
    }
}