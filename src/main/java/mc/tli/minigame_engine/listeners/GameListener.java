package mc.tli.minigame_engine.listeners;

import mc.tli.minigame_engine.GameState;
import mc.tli.minigame_engine.TliMinigameEngine;
import mc.tli.minigame_engine.instance.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class GameListener implements Listener {
    private TliMinigameEngine plugin;
    public GameListener(TliMinigameEngine plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Arena arena = plugin.getArenaManager().getArena(e.getPlayer());
        if (arena != null) {
            if(arena.getState().equals(GameState.LIVE)){
                arena.getGame().AddPoint(e.getPlayer());
            }else{
                e.setCancelled(true);
            }
        }

    }
}
