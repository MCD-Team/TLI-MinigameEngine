package mc.tli.minigame_engine.listeners;

import mc.tli.minigame_engine.TliMinigameEngine;
import mc.tli.minigame_engine.instance.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectListener implements Listener {
    private TliMinigameEngine plugin;
    public ConnectListener(TliMinigameEngine plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Arena arena = plugin.getArenaManager().getArena(event.getPlayer());
        if (arena != null) {
            arena.removePlayer(event.getPlayer());
        }

    }
}
