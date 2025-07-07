package mc.tli.minigame_engine.managers;
//This Class holds all methods related to the arenas it also holds the list of arena's

import mc.tli.minigame_engine.TliMinigameEngine;
import mc.tli.minigame_engine.instance.Arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class ArenaManager {
    private final List<Arena> arenas = new ArrayList<>();
    private final Map<String, Boolean> worldStatus = new HashMap<>(); // Track enabled/disabled worlds
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
            plugin.getLogger().warning("No arenas found in config");
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

    /**
     * Creates a simple void world with only one block at spawn
     * @param worldName The name of the world to create
     * @return The created world, or null if creation failed
     */
    public World createSimpleVoidWorld(String worldName) {
        try {
            // Generate unique world name if it already exists
            String uniqueWorldName = worldName;
            int counter = 1;
            while (Bukkit.getWorld(uniqueWorldName) != null) {
                uniqueWorldName = worldName + "-" + counter;
                counter++;
            }

            // Create world with void generator
            WorldCreator worldCreator = new WorldCreator(uniqueWorldName);
            worldCreator.type(WorldType.FLAT);
            worldCreator.generator(new VoidWorldGenerator());

            World world = worldCreator.createWorld();

            if (world != null) {
                // Set spawn location at 0, 64, 0
                world.setSpawnLocation(0, 64, 0);

                // Place a single block at spawn (stone block)
                Location spawnLocation = new Location(world, 0, 63, 0);
                world.getBlockAt(spawnLocation).setType(Material.STONE);

                // Enable the world by default
                worldStatus.put(uniqueWorldName, true);

                plugin.getLogger().info("Created void world: " + uniqueWorldName);
                return world;
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to create void world: " + worldName);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Enable a world (allow players to join)
     * @param worldName The name of the world to enable
     * @return true if successful, false if world doesn't exist
     */
    public boolean enableWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return false;
        }

        worldStatus.put(worldName, true);
        plugin.getLogger().info("Enabled world: " + worldName);
        return true;
    }

    /**
     * Disable a world (prevent players from joining)
     * @param worldName The name of the world to disable
     * @param kickPlayers Whether to kick existing players from the world
     * @return true if successful, false if world doesn't exist
     */
    public boolean disableWorld(String worldName, boolean kickPlayers) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return false;
        }

        worldStatus.put(worldName, false);

        if (kickPlayers) {
            // Kick all players from the disabled world
            World mainWorld = Bukkit.getWorlds().get(0); // Get main world as fallback
            for (Player player : world.getPlayers()) {
                player.teleport(mainWorld.getSpawnLocation());
                player.sendMessage("§cYou have been moved because the world §e" + worldName + "§c was disabled.");
            }
        }

        plugin.getLogger().info("Disabled world: " + worldName);
        return true;
    }

    /**
     * Check if a world is enabled
     * @param worldName The name of the world to check
     * @return true if enabled, false if disabled or doesn't exist
     */
    public boolean isWorldEnabled(String worldName) {
        return worldStatus.getOrDefault(worldName, false);
    }

    /**
     * Check if a player can join a world
     * @param player The player trying to join
     * @param worldName The world they're trying to join
     * @return true if they can join, false otherwise
     */
    public boolean canPlayerJoinWorld(Player player, String worldName) {
        // Check if world exists
        if (Bukkit.getWorld(worldName) == null) {
            return false;
        }

        // Check if world is enabled
        if (!isWorldEnabled(worldName)) {
            // Allow bypass permission for admins
            if (player.hasPermission("tli.world.bypass")) {
                return true;
            }
            return false;
        }

        return true;
    }

    /**
     * Get all worlds and their status
     * @return Map of world names to their enabled status
     */
    public Map<String, Boolean> getAllWorldStatus() {
        Map<String, Boolean> allWorlds = new HashMap<>();

        // Add all loaded worlds
        for (World world : Bukkit.getWorlds()) {
            allWorlds.put(world.getName(), isWorldEnabled(world.getName()));
        }

        return allWorlds;
    }

    /**
     * Load world status from config (call this on plugin enable)
     */
    public void loadWorldStatus() {
        FileConfiguration config = plugin.getConfig();
        if (config.contains("world-status")) {
            for (String worldName : config.getConfigurationSection("world-status").getKeys(false)) {
                boolean enabled = config.getBoolean("world-status." + worldName, true);
                worldStatus.put(worldName, enabled);
            }
        }

        // Set default status for any worlds not in config
        for (World world : Bukkit.getWorlds()) {
            if (!worldStatus.containsKey(world.getName())) {
                worldStatus.put(world.getName(), true); // Default to enabled
            }
        }
    }

    /**
     * Save world status to config
     */
    public void saveWorldStatus() {
        FileConfiguration config = plugin.getConfig();
        for (Map.Entry<String, Boolean> entry : worldStatus.entrySet()) {
            config.set("world-status." + entry.getKey(), entry.getValue());
        }
        plugin.saveConfig();
    }

    /**
     * Simple void world generator that creates empty chunks
     */
    private static class VoidWorldGenerator extends ChunkGenerator {
        @Override
        public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
            // Return empty chunk data (void world)
            return createChunkData(world);
        }
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