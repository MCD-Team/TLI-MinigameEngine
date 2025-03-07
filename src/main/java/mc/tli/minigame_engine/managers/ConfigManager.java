package mc.tli.minigame_engine.managers;

import mc.tli.minigame_engine.TliMinigameEngine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private static FileConfiguration config;
    public ConfigManager (TliMinigameEngine main){
        config = main.getConfig();
    }
    public static void initConfig(TliMinigameEngine main){

        if(!main.getDataFolder().exists()){
            main.getDataFolder().mkdir();
        }

        if(config == null){
            System.out.println("Config is null");
            main.reloadConfig();
            config = main.getConfig();
        }
        main.saveConfig();
    }
    public static int getPlayerTreshold(){return config.getInt("playerTreshold");}
    public static int getKickCountdown(){return config.getInt("kickCountdown");}
    public static int getRequiredPlayers(){return config.getInt("required-players");}
    public static int getMaxPlayers(){return config.getInt("max-players");}
    public static int getCountDownSeconds(){return config.getInt("countdown-seconds");}
    public static Location getLobbyLocation(){
        return new Location(
                Bukkit.getWorld(config.getString("lobby-spawn.world")),
                config.getDouble("lobby-spawn.x"),
                config.getDouble("lobby-spawn.y"),
                config.getDouble("lobby-spawn.z"),
                (float)config.getDouble("lobby-spawn.yaw"),
                (float)config.getDouble("lobby-spawn.pitch"));
    }
}
