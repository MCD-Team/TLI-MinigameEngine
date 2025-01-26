package mc.tli.minigame_engine.instance;

import mc.tli.minigame_engine.GameState;
import mc.tli.minigame_engine.Managers.ConfigManager;

import mc.tli.minigame_engine.TliMinigameEngine;
import mc.tli.minigame_engine.moderation.banUser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena {
    private int id;
    private Location spawn;
    private Countdown countdown;
    private GameState state;
    private List<UUID> players;
    private Testgame testgame;
    private TliMinigameEngine minigame;
    private banUser banCommand;
    public Arena(int id, Location spawn, TliMinigameEngine minigame) {
        this.id = id;
        this.spawn = spawn;
        this.state = GameState.QUEUEING;
        this.players = new ArrayList<UUID>();
        this.countdown = new Countdown(minigame,this);
        this.testgame = new Testgame(this);
        this.minigame = minigame;
        this.banCommand = new banUser(this,testgame);
    }
    public void startGame(){
        testgame.start();
    }
    public void reset(boolean isPlayerRemoved){
        if(isPlayerRemoved)
        {
            for(UUID p : players){
                Bukkit.getPlayer(p).teleport(ConfigManager.getLobbyLocation());
            }
        }
        players.clear();
        state = GameState.QUEUEING;
        countdown.cancel();
        countdown = new Countdown(minigame,this);
        testgame = new Testgame(this);
    }
    public int getId(){return id;}
    public GameState getState(){return state;}
    public List<UUID> getPlayers(){return players;}

    public void addPlayer(Player player){
        players.add(player.getUniqueId());
        player.teleport(spawn);
        if(state.equals(GameState.QUEUEING) &&players.size()>= ConfigManager.getRequiredPlayers()&&players.size()<=ConfigManager.getMaxPlayers()){
            countdown.start();

        }
    }

    public void removePlayer(Player player){
        players.remove(player.getUniqueId());
        player.teleport(ConfigManager.getLobbyLocation());
    }
    public void setState(GameState state){
        this.state = state;
    }
    public void sendMessage(String message){
        for(UUID uuid : players){
            Bukkit.getPlayer(uuid).sendMessage(message);
        }
    }
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut){
        for(UUID uuid : players){
            Bukkit.getPlayer(uuid).sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

}
