package mc.tli.minigame_engine.instance;
//The arena class holds all methods related to arena's it holds players and the game state

import mc.tli.minigame_engine.GameState;

import mc.tli.minigame_engine.TliMinigameEngine;
import mc.tli.minigame_engine.commands.banUser;

import mc.tli.minigame_engine.managers.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.OminousItemSpawner;
import org.bukkit.entity.Player;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class Arena {
    private final Integer id;
    private final Location arenaSpawn;
    private Countdown countdown;
    private GameState state;
    private final List<UUID> players;
    private Testgame testgame;
    private final TliMinigameEngine minigame;
    private final banUser banCommand;
    private final ConfigManager configManager;

    //constructor to get the id and the spawn location of the arena and a reference to the main class
    public Arena(int id, Location arenaSpawn, TliMinigameEngine minigame) {
        this.id = id;
        this.arenaSpawn = arenaSpawn;
        this.state = GameState.QUEUEING;
        this.players = new ArrayList<>();
        this.countdown = new Countdown(minigame,this);
        this.testgame = new Testgame(this);
        this.minigame = minigame;
        this.banCommand = new banUser(minigame);
        this.configManager = minigame.getConfigManager();

    }

    public void startGame(){
        testgame.StartGame();
    }

    public void reset(boolean isPlayerRemoved,boolean kickPlayers){
//  player hase been removed do magic dion wants
        if(isPlayerRemoved)
        {
            //TO DO add title screen and stop the game
            teleportPlayers(players,configManager.getLobbyLocation(),minigame);
        }
        if(kickPlayers){
            teleportPlayers(players,configManager.getLobbyLocation(),minigame);
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

    //adds player to the arena and teleports them to the spawn location
    public void addPlayer(Player player){
        players.add(player.getUniqueId());
        minigame.getLogger().info("Added player " + player.getName());
        player.teleport(arenaSpawn);
        if(state.equals(GameState.QUEUEING) && players.size() >= configManager.getRequiredPlayers() && players.size() <= configManager.getMaxPlayers()){
            countdown.start();
            sendMessage("Enough players have joined starting countdown");
        }
    }

    //removes player from the arena and teleports them to the lobby location
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


    //make sure message is not empty to prevent nullPointerException then loop over the players in the arena and send the message
    public void sendMessage(String message){
        if(!message.isEmpty()){
            for(UUID uuid : players){
                Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(message);
            }
        }else{
            System.out.println("Message is empty");
        }
    }

    //all Integers are put in are counted in miliseconds
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut){
        Component titleMsg =  Component.text(title);
        Component subtitleMsg =  Component.text(subtitle);
        Title titleToScreen = Title.title(
                titleMsg,
                subtitleMsg,
                Title.Times.times(
                        Duration.ofMillis(fadeIn),
                        Duration.ofMillis(stay),
                        Duration.ofMillis(fadeOut)
                )
        );
        for(UUID uuid : players){
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).showTitle(titleToScreen);
        }
    }

    //kick all players in the arena and teleport them to the lobby location
    public void kickPlayers(){
        for(UUID uuid : players){
            if(Bukkit.getPlayer(uuid) == null){
                minigame.getLogger().info("Player is null skipping player");
                continue;
            }

            removePlayer(Objects.requireNonNull(Bukkit.getPlayer(uuid)));
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).teleport(configManager.getLobbyLocation());
        }
    }

    //adds a bossbar to the players in the arena
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

    //method to create a bossbar in Don't Repeat Yourself friendly way
    public static BossBar createBossbar(String Title, BarColor color, BarStyle style){
       return Bukkit.createBossBar(Title, color, style);
    }

    //method to remove the bossbar from all players in the arena
    public static void removeBossbar(BossBar bossbar){
        bossbar.removeAll();
    }

    //method to teleport all players in the arena to a location
    public static void teleportPlayers(List<UUID> players, Location location, TliMinigameEngine minigame){
        if(location == null){
            minigame.getLogger().info("Location is null skipping teleport");
            System.out.println("Location is null skipping teleport");
            return;
        }
        for(UUID p : players){
            if(p != null){
                Objects.requireNonNull(Bukkit.getPlayer(p)).teleport(location);
            }
        }
    }

    //method to add a Title to all players in the arena (all Integers are in game ticks)
    public static void addTitle(List<Player>players,String Title,String subText,int fadeIn,int stayTime,int fadeOut){
        for(Player p : players){
            if(p != null){
                p.sendTitle(Title, subText, fadeIn, stayTime, fadeOut);
            }
        }
    }

    //Getters
    public int getId(){
        return id;
    }

    public GameState getState(){
        return state;
    }

    public List<UUID> getPlayers(){
        return players;
    }

    public Testgame getGame(){
        return testgame;
    }
    //Setters
    //set's the arena state based on the GAMESTATE enum
    public void setState(GameState state){
        this.state = state;
    }
}
