package mc.tli.minigame_engine.listeners;

import mc.tli.minigame_engine.GameState;
import mc.tli.minigame_engine.TliMinigameEngine;
import mc.tli.minigame_engine.instance.Arena;
import mc.tli.minigame_engine.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.List;

public class MenuListener implements Listener {
    private final TliMinigameEngine plugin;
    public MenuListener(TliMinigameEngine plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(!(event.getItem() == null)){
            if(event.getItem().getItemMeta().getDisplayName().equals("Join Game")){
                List<Arena> Arenas =  plugin.getArenaManager().getArenas();
                if(Arenas != null){
                    if(!Arenas.isEmpty()){
                        for(Arena arena : Arenas){
                            //check if the arena is queueing or countdown and if the max player count hase NOT been reached then add the player
                            if(arena.getState() == GameState.QUEUEING || arena.getState() == GameState.COUNTINGDOWN && arena.getPlayers().size() <= ConfigManager.getMaxPlayers()){
                                arena.addPlayer(player);
                            }else{
                                player.sendMessage(ChatColor.RED + "An error hase occurred please try again");
                                return;
                            }
                        }
                    }else{
                        player.sendMessage(ChatColor.RED + "No arenas found making arena try again");
                    }
                }
            }
        }

    }
}
