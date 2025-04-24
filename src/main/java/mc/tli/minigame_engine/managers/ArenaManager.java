package mc.tli.minigame_engine.managers;

import mc.tli.minigame_engine.TliMinigameEngine;
import mc.tli.minigame_engine.instance.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArenaManager {
    private final List<Arena> arenas = new ArrayList<>();
    private final TliMinigameEngine plugin;
    public ArenaManager(TliMinigameEngine main) {
        this.plugin = main;

    }
    public void addArena()
    {
        FileConfiguration config = plugin.getConfig();
        if(config != null){
            System.out.println("Config is not null");
        }

        for(String str: Objects.requireNonNull(config.getConfigurationSection("arenas")).getKeys(false)) {
            World world = Bukkit.getWorld(Objects.requireNonNull(config.getString("arenas." + str + ".world")));
            if(world == null) {
                plugin.getLogger().warning("Arenas world null");
                return;
            }
            arenas.add(new Arena(Integer.parseInt(str),new Location(
                    world,
                    config.getDouble("arenas."+str+".x"),
                    config.getDouble("arenas."+str+".y"),
                    config.getDouble("arenas."+str+".z"),
                    (float)config.getDouble("arenas."+str+".yaw"),
                    (float)config.getDouble("arenas."+str+".pitch"))
                    ,plugin));
        }
        if(arenas.isEmpty()) {
            System.out.println("No arena created due to missing config");
        }
    }
    public List<Arena> getArenas() {return arenas;}
    public Arena getArena(Player player) {
        for(Arena arena: arenas) {
            if(arena.getPlayers().contains(player.getUniqueId())) return arena;
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
