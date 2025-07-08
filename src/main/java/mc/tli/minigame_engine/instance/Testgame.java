package mc.tli.minigame_engine.instance;
//This class holds all logic needed for the testgame
import mc.tli.minigame_engine.enums.GameState;

import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.UUID;

public class Testgame extends Game implements Listener{
    //var added for later custom banning system
    private boolean isPlayerRemoved = false;
    private final Arena arena;
    private final HashMap<UUID,Integer> points = new HashMap<>();
    //constructor to get the arena object
    public Testgame(Arena arena) {
        this.arena = arena;
    }
    @Override
    public void StartGame() {
        arena.setState(GameState.LIVE);
        arena.sendMessage(NamedTextColor.WHITE+"Game started!");
        for(UUID uuid: arena.getPlayers()){
            points.put(uuid, 0);
        }
    }
    public void AddPoint(Player player){
        int playerPoints = points.get(player.getUniqueId())+1;
        if(playerPoints == 20){
            arena.sendMessage(NamedTextColor.RED + player.getName()+ "has won");
//           TO DO arena.reset(isPlayerRemoved,false);
        }else{
            player.sendMessage(NamedTextColor.RED+"+1 point u now have"+playerPoints+"points");
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
