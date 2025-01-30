package mc.tli.minigame_engine.instance;

import mc.tli.minigame_engine.GameState;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.UUID;

public class Testgame implements Listener {
    private boolean isPlayerRemoved = false;
    private Arena arena;
    private HashMap<UUID,Integer> points;
    public Testgame(Arena arena) {
        this.arena = arena;
    }
    public void start(){
        arena.setState(GameState.LIVE);
        arena.sendMessage(ChatColor.WHITE+"Game started!");
        for(UUID uuid: arena.getPlayers()){
            points.put(uuid, 0);
        }
    }
    public void AddPoint(Player player){
        int playerPoints = points.get(player.getUniqueId())+1;
        if(playerPoints == 20){
            arena.sendMessage(ChatColor.RED + player.getName()+ "has won");
            arena.reset(isPlayerRemoved);
            return;
        }else{
            player.sendMessage(ChatColor.RED+"+1 point u now have"+points.toString()+"points");
            points.replace(player.getUniqueId(),playerPoints);
        }
    }

    @EventHandler
    private void onPlayerDead(PlayerDeathEvent p){
        Player k = p.getPlayer().getKiller();

        if (k != null) {
            AddPoint(k);
        }
    }

    public void setPlayerRemoved(boolean playerRemoved){
        this.isPlayerRemoved = playerRemoved;
    }
}
