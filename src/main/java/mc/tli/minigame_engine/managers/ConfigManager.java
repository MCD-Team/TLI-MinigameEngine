package mc.tli.minigame_engine.managers;
//This class contains all logic linked to the config

import mc.tli.minigame_engine.TliMinigameEngine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class ConfigManager {
    private FileConfiguration config;
    private final TliMinigameEngine main;
    public ConfigManager (TliMinigameEngine main){
        this.main = main;
        config = main.getConfig();
    }
    //Method to initialize the config
    public void initConfig(){

        if(!main.getDataFolder().exists()){
            main.getDataFolder().mkdir();
            System.out.println("Data folder created restart server");
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
    //Getters for the config values
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
