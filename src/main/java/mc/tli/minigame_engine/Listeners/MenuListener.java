package mc.tli.minigame_engine.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getItem().getItemMeta().getDisplayName().equals("Join Game")){
            //Join game
        }
    }
}
