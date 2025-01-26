package mc.tli.minigame_engine.instance;

import mc.tli.minigame_engine.GameState;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Testgame {
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
            arena.sendMessage(ChatColor.RED + player.getName()+ "hase won");
            arena.reset(isPlayerRemoved);
            return;
        }else{
            player.sendMessage(ChatColor.RED+"+1 point u now have"+points.toString()+"points");
            points.replace(player.getUniqueId(),playerPoints);
        }
    }
    public void setPlayerRemoved(boolean playerRemoved){
        this.isPlayerRemoved = playerRemoved;
    }
}
