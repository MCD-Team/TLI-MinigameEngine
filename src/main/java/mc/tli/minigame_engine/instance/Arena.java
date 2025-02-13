package mc.tli.minigame_engine.instance;

import mc.tli.minigame_engine.GameState;
import mc.tli.minigame_engine.managers.ConfigManager;

import mc.tli.minigame_engine.TliMinigameEngine;
import mc.tli.minigame_engine.commands.banUser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena {
    private final int id;
    private final Location spawn;
    private Countdown countdown;
    private GameState state;
    private final List<UUID> players;
    private Testgame testgame;
    private final TliMinigameEngine minigame;
    private final banUser banCommand;
    public Arena(int id, Location spawn, TliMinigameEngine minigame) {
        this.id = id;
        this.spawn = spawn;
        this.state = GameState.QUEUEING;
        this.players = new ArrayList<UUID>();
        this.countdown = new Countdown(minigame,this);
        this.testgame = new Testgame(this);
        this.minigame = minigame;
        this.banCommand = new banUser(minigame);
    }
    public void startGame(){
        testgame.start();
    }
    public void reset(boolean isPlayerRemoved,boolean kickPlayers){
//  player hase been removed do magic dion wants
        if(isPlayerRemoved)
        {
            //TO DO add title screen and stop the game
            teleportPlayers(players,ConfigManager.getLobbyLocation());
        }
        if(kickPlayers){
            teleportPlayers(players,ConfigManager.getLobbyLocation());
        }
        if(state == GameState.LIVE){
            state = GameState.FINISHED;
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
            sendMessage("Enough players have joined starting countdown");

        }
    }
    public Testgame getGame(){
        return testgame;
    }
    public void removePlayer(Player player){
        players.remove(player.getUniqueId());
        player.teleport(ConfigManager.getLobbyLocation());
        if(state.equals(GameState.COUNTINGDOWN)&&players.size()<ConfigManager.getRequiredPlayers()){
            sendMessage("To many players have left canceling countdown");
            countdown.cancel();
            return;
        }
        if(state.equals(GameState.LIVE)){
            if (players.size()< ConfigManager.getPlayerTreshold()) {
                sendMessage("To many people have left kicking all players in ");
                kickPlayers();
            }
            return;
        }
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
    public void kickPlayers(){
        for(UUID uuid : players){
            Bukkit.getPlayer(uuid).teleport(ConfigManager.getLobbyLocation());
        }
    }
    public static BossBar addBossbar(List<Player> players, String Title, BarColor color, BarStyle style){
        BossBar bossbar = Bukkit.createBossBar(Title, color, style);
        for(Player p : players){
            if(p != null){
                bossbar.addPlayer(p);
            }
        }
        return bossbar;
    }
    public static void removeBossbar(List<Player> players,BossBar bossbar){
        for(Player p : players){
            if(p != null){
                bossbar.removePlayer(p);
            }
        }
    }
    public static void teleportPlayers(List<UUID> players, Location location){
        for(UUID p : players){
            if(p != null){
                Bukkit.getPlayer(p).teleport(location);
            }
        }
    }
    public static void addTitle(List<Player>players,String Title,String subText,int fadeIn,int stayTime,int fadeOut){
        for(Player p : players){
            if(p != null){
                p.sendTitle(Title, subText, fadeIn, stayTime, fadeOut);
            }
        }
    }

}
