package mc.tli.minigame_engine.managers;

import mc.tli.minigame_engine.TliMinigameEngine;
import mc.tli.minigame_engine.instance.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArenaManager {
    private List<Arena> arenas = new ArrayList<>();
    public ArenaManager(TliMinigameEngine main) {
        FileConfiguration config = main.getConfig();
        for(String str: Objects.requireNonNull(config.getConfigurationSection("arenas")).getKeys(false)) {
            arenas.add(new Arena(Integer.parseInt(str),new Location(
                    Bukkit.getWorld(config.getString("arenas."+str+".world")),
                    config.getDouble("arenas."+str+".x"),
                    config.getDouble("arenas."+str+".y"),
                    config.getDouble("arenas."+str+".z"),
                    (float)config.getDouble("arenas."+str+".yaw"),
                    (float)config.getDouble("arenas."+str+".pitch"))
            ,main));
        }
    }
    public List<Arena> getArenas() {return arenas;}
    public Arena getArena(Player player) {
        for(Arena arena: arenas) {
            if(arena.getPlayers().contains(player)) return arena;
        }
        return null;
    }
    public Arena getArena(int id) {
        for(Arena arena: arenas) {
            if(arena.getId() == id) {return arena;}

        }
        return null;
    }
}
