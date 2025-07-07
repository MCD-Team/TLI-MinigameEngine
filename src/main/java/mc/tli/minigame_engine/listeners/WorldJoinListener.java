package mc.tli.minigame_engine.listeners;

import mc.tli.minigame_engine.TliMinigameEngine;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WorldJoinListener implements Listener {
    private final TliMinigameEngine plugin;

    public WorldJoinListener(TliMinigameEngine plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        // Check if player is teleporting to a different world
        if (event.getTo() != null && event.getFrom().getWorld() != event.getTo().getWorld()) {
            String worldName = event.getTo().getWorld().getName();

            // Check if player can join the world
            if (!plugin.getArenaManager().canPlayerJoinWorld(player, worldName)) {
                event.setCancelled(true);
                player.sendMessage("§cYou cannot join the world §e" + worldName + "§c - it's currently disabled!");
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();

        // Check if player is going to a different world through portal
        if (event.getTo() != null && event.getFrom().getWorld() != event.getTo().getWorld()) {
            String worldName = event.getTo().getWorld().getName();

            // Check if player can join the world
            if (!plugin.getArenaManager().canPlayerJoinWorld(player, worldName)) {
                event.setCancelled(true);
                player.sendMessage("§cYou cannot enter the world §e" + worldName + "§c - it's currently disabled!");
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String newWorldName = player.getWorld().getName();

        // Welcome message for enabled worlds
        if (plugin.getArenaManager().isWorldEnabled(newWorldName)) {
            // Send a subtle message about the world
            player.sendMessage("§7You entered world: §e" + newWorldName);
        }
    }
}