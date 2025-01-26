package mc.tli.minigame_engine.Managers;

import mc.tli.minigame_engine.TliMinigameEngine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
    private  static FileConfiguration config;
    public static void initConfig(TliMinigameEngine main) {
        ConfigManager.config = main.getConfig();
        main.saveConfig();
    }
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
