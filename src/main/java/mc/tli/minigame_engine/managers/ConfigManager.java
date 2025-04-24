package mc.tli.minigame_engine.managers;

import mc.tli.minigame_engine.TliMinigameEngine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class ConfigManager {
    private FileConfiguration config;
    public ConfigManager (TliMinigameEngine main){
        config = main.getConfig();
    }
    public void initConfig(TliMinigameEngine main){

        if(!main.getDataFolder().exists()){
            main.getDataFolder().mkdir();
        }

        if(this.config == null){
            System.out.println("Config is null");
            main.reloadConfig();
            config = main.getConfig();
        }else{
            System.out.println("Config is NOT null");
        }
        main.saveConfig();
    }
    public  int getPlayerTreshold(){return config.getInt("playerTreshold");}
    public  int getKickCountdown(){return config.getInt("kickCountdown");}
    public  int getRequiredPlayers(){return config.getInt("required-players");}
    public  int getMaxPlayers(){return config.getInt("max-players");}
    public  int getCountDownSeconds(){return config.getInt("countdown-seconds");}
    public  Location getLobbyLocation(){
        Location lobbyLocation = new Location(
                Bukkit.getWorld(Objects.requireNonNull(config.getString("lobby-spawn.world"))),
                config.getDouble("lobby-spawn.x"),
                config.getDouble("lobby-spawn.y"),
                config.getDouble("lobby-spawn.z"),
                (float)config.getDouble("lobby-spawn.yaw"),
                (float)config.getDouble("lobby-spawn.pitch"));
        if(lobbyLocation.getWorld() == null) {
            System.out.println("Lobby world is null");
            return null;
        }else{
            return lobbyLocation;
        }

    }
}
