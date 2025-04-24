package mc.tli.minigame_engine.managers;
//This Class holds all methods related to the arenas it also holds the list of arena's

import mc.tli.minigame_engine.TliMinigameEngine;
import mc.tli.minigame_engine.instance.Arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArenaManager {
    private final List<Arena> arenas = new ArrayList<>();
    private final TliMinigameEngine plugin;

    //get the instance of the main class good OOP practice
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
    private static String copyDirectory(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            // Maak elke subdirectory aan vóórdat we de bestanden erin kopiëren
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = target.resolve(source.relativize(dir));
                Files.createDirectories(targetDir);
                return FileVisitResult.CONTINUE;
            }

            // Kopieer elk bestand; overschrijf als het al bestaat
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path targetFile = target.resolve(source.relativize(file));
                Files.copy(file, targetFile,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.COPY_ATTRIBUTES);
                return FileVisitResult.CONTINUE;
            }
        });

        return target.getFileName().toString();
    }

    public static String createArena(String nameArena) throws IOException {
        // Generate a random ID for the arena
        int randomArenaID = (int)(Math.random() * 5) + 1;

        // Generate a random ID for the target arena
        int randomArenaID2 = (int)(Math.random() * 42);

        // Source location within worlds/arenas directory
        Path source = Paths.get("worlds/arenas/" + nameArena + "-" + randomArenaID);

        // Target location within worlds directory
        // This will copy to: worlds/[nameArena-randomArenaID]
        Path target = Paths.get("worlds/" + nameArena + "-" + randomArenaID2);


        // Checks if the directory in /worlds/arenas/ exists if not it returns a IOException
        if (!Files.exists(source)) {
            throw new IOException("Source arena doesn't exist: " + source);
        }

        // If the directory already exists with the same ID it will generate a new ID with a while loop
        if (Files.exists(target)) {
            while (Files.exists(target)) {
                //System.out.println("Target: " + target + " already exists, generating a new ID...");
                // Generate a new random ID until we find a non-existing target
                randomArenaID2 = (int) (Math.random() * 100);
                target = Paths.get("worlds/" + nameArena + "-" + randomArenaID2);
            }
        }

        // Create target directory if it doesn't exist
        Files.createDirectories(target.getParent());

        // Copies the directory and capture the arena name
        String arenaName = nameArena + "-" + randomArenaID2;
        copyDirectory(source, target);

        System.out.println("Arena copied: " + source);

        // Print the target directory for the Queue System to place them in the correct world
        return arenaName;
    }
    //Getters
    public List<Arena> getArenas() {
        return arenas;
    }

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
