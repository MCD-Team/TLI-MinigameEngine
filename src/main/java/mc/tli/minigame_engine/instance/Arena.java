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
    private final Location arenaSpawn;
    private Countdown countdown;
    private GameState state;
    private final List<UUID> players;
    private Testgame testgame;
    private final TliMinigameEngine minigame;
    private final banUser banCommand;
    private final ConfigManager configManager;
    public Arena(int id, Location arenaSpawn, TliMinigameEngine minigame) {
        this.configManager = new ConfigManager(minigame);
        this.id = id;
        this.arenaSpawn = arenaSpawn;
        this.state = GameState.QUEUEING;
        this.players = new ArrayList<>();
        this.countdown = new Countdown(minigame,this);
        this.testgame = new Testgame(this);
        this.minigame = minigame;
        this.banCommand = new banUser(minigame);
    }
    public void startGame(){
        testgame.StartGame();
    }
    public void reset(boolean isPlayerRemoved,boolean kickPlayers){
//  player hase been removed do magic dion wants
        if(isPlayerRemoved)
        {
            //TO DO add title screen and stop the game
            teleportPlayers(players,configManager.getLobbyLocation());
        }
        if(kickPlayers){
            teleportPlayers(players,configManager.getLobbyLocation());
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
        System.out.println("Player added to arena at:" + arenaSpawn);
        player.teleport(arenaSpawn);
        if(state.equals(GameState.QUEUEING) && players.size()>= configManager.getRequiredPlayers()&&players.size()<=configManager.getMaxPlayers()){
            countdown.start();
            sendMessage("Enough players have joined starting countdown");
        }
    }
    public Testgame getGame(){
        return testgame;
    }
    public void removePlayer(Player player){
        players.remove(player.getUniqueId());
        player.teleport(configManager.getLobbyLocation());
        if(state.equals(GameState.COUNTINGDOWN)&&players.size()<configManager.getRequiredPlayers()){
            sendMessage("To many players have left canceling countdown");
            countdown.cancel();
            return;
        }
        if(state.equals(GameState.LIVE)){
            if (players.size()<configManager.getPlayerTreshold()) {
                sendMessage("To many people have left kicking all players in ");
                kickPlayers();
            }
        }
    }
    public void setState(GameState state){
        this.state = state;
    }
    //make sure message is not empty to prevent nullPointerException then loop over the players in the arena and send the message
    public void sendMessage(String message){
        if(!message.isEmpty()){
            for(UUID uuid : players){
                Bukkit.getPlayer(uuid).sendMessage(message);
            }
        }else{
            System.out.println("Message is empty");
        }
    }
    //all ints are put in are counted in game ticks
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut){
        for(UUID uuid : players){
            Bukkit.getPlayer(uuid).sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }
    public void kickPlayers(){
        for(UUID uuid : players){
            Bukkit.getPlayer(uuid).teleport(configManager.getLobbyLocation());
        }
    }
    public static void addBossbar(List<UUID> uuids,BossBar bossBar){
        final List<Player>players = new ArrayList<>();
        for(UUID uuid: uuids){
            players.add(Bukkit.getPlayer(uuid));
        }
        for(Player p : players){
            if(p != null){
                bossBar.addPlayer(p);
            }
        }
    }
    public static BossBar createBossbar(String Title, BarColor color, BarStyle style){
       return Bukkit.createBossBar(Title, color, style);
    }
    public static void removeBossbar(BossBar bossbar){
        bossbar.removeAll();
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
