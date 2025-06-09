// Main class
package com.blbilink.blbilogin;

import com.blbilink.blbilogin.load.LoadConfig;
import com.blbilink.blbilogin.load.LoadFunction;
import com.blbilink.blbilogin.modules.events.CheckOnline;
import com.blbilink.blbilogin.modules.events.LoginAction;
import com.blbilink.blbilogin.modules.stats.JoinCounter;
import com.blbilink.blbilogin.vars.Configvar;
import org.blbilink.blbiLibrary.I18n;
import org.blbilink.blbiLibrary.Metrics;
import org.blbilink.blbiLibrary.utils.ConfigUtil;
import org.blbilink.blbiLibrary.utils.FoliaUtil;
import org.blbilink.blbiLibrary.utils.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public final class BlbiLogin extends JavaPlugin implements Listener {
    public static BlbiLogin plugin;
    public I18n i18n;
    public ConfigUtil config;
    public FoliaUtil foliaUtil;
    public JoinCounter joinCounter;

    @Override
    public void onEnable() {
        plugin = this;

        try {
            // Initialize configuration first
            saveDefaultConfig();
            getConfig().options().copyDefaults(true);
            saveConfig();
            
            // Initialize plugin components
            LoadConfig.loadConfig(this);
            LoadFunction loadFunction = new LoadFunction(this);
            loadFunction.loadFunction();
            LoginAction.INSTANCE.sync(this);
            CheckOnline.INSTANCE.sync(this);
            foliaUtil = new FoliaUtil(this);
            
            joinCounter = new JoinCounter(this);
            
            // Register events
            getServer().getPluginManager().registerEvents(this, this);
            
            // Verify running on a Folia server
            foliaUtil.checkFolia(true);
            
            // Create plugin directory if it doesn't exist
            File pluginDir = new File(getDataFolder(), "");
            if (!pluginDir.exists()) {
                pluginDir.mkdirs();
            }
            
            // Print logo
            getLogger().info(TextUtil.getLogo(
                    "Loading...",
                    "BLBILOGIN",
                    "SpigotMC: https://www.spigotmc.org/resources/117672/",
                    plugin,
                    Arrays.asList("EggFine", "ImFoxerARG"),
                    Arrays.asList("Mgazul")));
            
            getLogger().info("6b6t login system injected.");
            
            // Load bStats metrics
            Metrics metrics = new Metrics(this, 22490);
            metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
            
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to enable BlbiLogin plugin", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        try {
            // Print logo
            getLogger().info(TextUtil.getLogo(
                    "Disabling...",
                    "BLBILOGIN",
                    "SpigotMC: https://www.spigotmc.org/resources/117672/",
                    plugin,
                    Arrays.asList("EggFine"),
                    Arrays.asList("Mgazul")));
            
            if (joinCounter != null) {
                joinCounter.save();
            }
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Error during plugin disable", e);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e == null || e.getPlayer() == null) return;
        
        // Check if the player actually moved (not just head rotation)
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && 
            e.getFrom().getBlockY() == e.getTo().getBlockY() && 
            e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }
        
        if (Configvar.noLoginPlayerList != null && 
            Configvar.noLoginPlayerList.contains(e.getPlayer().getName()) && 
            Configvar.config != null && 
            Configvar.config.getBoolean("noLoginPlayerCantMove", true)) {
            
            if (i18n != null) {
                String msgNoLoginTryMove = i18n.as("logNoLoginTryMove", false, e.getPlayer().getName());
                this.getLogger().info(msgNoLoginTryMove);
            } else {
                this.getLogger().info("Unlogged player " + e.getPlayer().getName() + " tried to move and was blocked.");
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e) {
        if (e == null || e.getPlayer() == null) return;
        
        if (Configvar.noLoginPlayerList != null && 
            Configvar.noLoginPlayerList.contains(e.getPlayer().getName()) && 
            Configvar.config != null && 
            Configvar.config.getBoolean("noLoginPlayerCantBreak", true)) {
            
            getLogger().info("Unlogged player " + e.getPlayer().getName() + 
                " tried to break block " + e.getBlock().getType().name() + " and was blocked.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerHurt(EntityDamageEvent e) {
        if (e == null || !(e.getEntity() instanceof Player)) return;
        
        Player player = (Player) e.getEntity();
        
        if (Configvar.noLoginPlayerList != null && 
            Configvar.noLoginPlayerList.contains(player.getName()) && 
            Configvar.config != null && 
            Configvar.config.getBoolean("noLoginPlayerCantHurt", true)) {
            
            EntityDamageEvent.DamageCause currentDamageCause = e.getCause();
            String damageCauseName = currentDamageCause.name();
            
            getLogger().info("Unlogged player " + player.getName() + 
                " received damage " + damageCauseName + " and was blocked.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event == null || event.getPlayer() == null) return;
        
        Player player = event.getPlayer();
        
        // Run location-based operations synchronously
        new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = player.getLocation();
                String world = player.getWorld().getName();
                String heldItem = player.getInventory().getItemInMainHand().getType().toString();
                
                // Armor (Helmet to Boots)
                ItemStack[] armor = player.getInventory().getArmorContents();
                String helmet = armor.length > 3 && armor[3] != null ? armor[3].getType().toString() : "None";
                String chest = armor.length > 2 && armor[2] != null ? armor[2].getType().toString() : "None";
                String legs = armor.length > 1 && armor[1] != null ? armor[1].getType().toString() : "None";
                String boots = armor.length > 0 && armor[0] != null ? armor[0].getType().toString() : "None";
                
                String gamemode = player.getGameMode().toString();
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                
                String logLine = String.format(
                    "[Time: %s] [World: %s] [X: %d, Y: %d, Z: %d] [Gamemode: %s] [Player: %s] [Held: %s] [Armor: %s, %s, %s, %s] %s",
                    timestamp,
                    world,
                    loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
                    gamemode,
                    player.getName(),
                    heldItem,
                    helmet, chest, legs, boots,
                    event.getMessage()
                );
                
                // Write to file asynchronously
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            File logDir = new File(getDataFolder(), "");
                            if (!logDir.exists()) {
                                logDir.mkdirs();
                            }
                            
                            File logFile = new File(getDataFolder(), "LoraMemory.txt");
                            try (FileWriter fw = new FileWriter(logFile, true)) {
                                fw.write(logLine + "\n");
                            }
                        } catch (IOException e) {
                            getLogger().log(Level.WARNING, "Failed to write to LoraMemory.txt", e);
                        }
                    }
                }.runTaskAsynchronously(plugin);
            }
        }.runTask(plugin);
    }
}
