package mc.tli.minigame_engine.commands;

import mc.tli.minigame_engine.TliMinigameEngine;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WorldCommand implements CommandExecutor, TabCompleter {
    private final TliMinigameEngine plugin;

    public WorldCommand(TliMinigameEngine plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "create":
                return handleCreate(player, args);
            case "enable":
                return handleEnable(player, args);
            case "disable":
                return handleDisable(player, args);
            case "list":
                return handleList(player);
            case "status":
                return handleStatus(player, args);
            case "tp":
            case "teleport":
                return handleTeleport(player, args);
            default:
                sendHelp(player);
                return true;
        }
    }

    private boolean handleCreate(Player player, String[] args) {
        if (!player.hasPermission("tli.world.create")) {
            player.sendMessage("§cYou don't have permission to create worlds!");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("§cUsage: /world create <world_name>");
            return true;
        }

        String worldName = args[1];

        // Validate world name
        if (worldName.length() < 3 || worldName.length() > 20) {
            player.sendMessage("§cWorld name must be between 3 and 20 characters!");
            return true;
        }

        if (!worldName.matches("^[a-zA-Z0-9_-]+$")) {
            player.sendMessage("§cWorld name can only contain letters, numbers, underscores, and hyphens!");
            return true;
        }

        player.sendMessage("§eCreating void world: " + worldName + "...");

        // Create the world asynchronously
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            World world = plugin.getArenaManager().createSimpleVoidWorld(worldName);

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                if (world != null) {
                    player.sendMessage("§aSuccessfully created void world: " + world.getName());
                    player.sendMessage("§7Use /world tp " + world.getName() + " to teleport to it!");
                } else {
                    player.sendMessage("§cFailed to create world: " + worldName);
                }
            });
        });

        return true;
    }

    private boolean handleEnable(Player player, String[] args) {
        if (!player.hasPermission("tli.world.manage")) {
            player.sendMessage("§cYou don't have permission to manage worlds!");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("§cUsage: /world enable <world_name>");
            return true;
        }

        String worldName = args[1];

        if (plugin.getArenaManager().enableWorld(worldName)) {
            player.sendMessage("§aEnabled world: " + worldName);
            plugin.getArenaManager().saveWorldStatus();
        } else {
            player.sendMessage("§cWorld not found: " + worldName);
        }

        return true;
    }

    private boolean handleDisable(Player player, String[] args) {
        if (!player.hasPermission("tli.world.manage")) {
            player.sendMessage("§cYou don't have permission to manage worlds!");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("§cUsage: /world disable <world_name> [kick_players]");
            return true;
        }

        String worldName = args[1];
        boolean kickPlayers = args.length > 2 && args[2].equalsIgnoreCase("true");

        if (plugin.getArenaManager().disableWorld(worldName, kickPlayers)) {
            player.sendMessage("§cDisabled world: " + worldName);
            if (kickPlayers) {
                player.sendMessage("§7Players have been kicked from the world.");
            }
            plugin.getArenaManager().saveWorldStatus();
        } else {
            player.sendMessage("§cWorld not found: " + worldName);
        }

        return true;
    }

    private boolean handleList(Player player) {
        if (!player.hasPermission("tli.world.list")) {
            player.sendMessage("§cYou don't have permission to list worlds!");
            return true;
        }

        Map<String, Boolean> worlds = plugin.getArenaManager().getAllWorldStatus();

        if (worlds.isEmpty()) {
            player.sendMessage("§cNo worlds found!");
            return true;
        }

        player.sendMessage("§6=== World List ===");
        for (Map.Entry<String, Boolean> entry : worlds.entrySet()) {
            String status = entry.getValue() ? "§aEnabled" : "§cDisabled";
            World world = Bukkit.getWorld(entry.getKey());
            int playerCount = world != null ? world.getPlayers().size() : 0;

            player.sendMessage("§e" + entry.getKey() + " §8- " + status + " §7(" + playerCount + " players)");
        }

        return true;
    }

    private boolean handleStatus(Player player, String[] args) {
        if (!player.hasPermission("tli.world.status")) {
            player.sendMessage("§cYou don't have permission to check world status!");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("§cUsage: /world status <world_name>");
            return true;
        }

        String worldName = args[1];
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            player.sendMessage("§cWorld not found: " + worldName);
            return true;
        }

        boolean enabled = plugin.getArenaManager().isWorldEnabled(worldName);
        String status = enabled ? "§aEnabled" : "§cDisabled";
        int playerCount = world.getPlayers().size();

        player.sendMessage("§6=== World Status ===");
        player.sendMessage("§eWorld: " + worldName);
        player.sendMessage("§eStatus: " + status);
        player.sendMessage("§ePlayers: " + playerCount);
        player.sendMessage("§eSpawn: " + world.getSpawnLocation().getBlockX() + ", " +
                world.getSpawnLocation().getBlockY() + ", " +
                world.getSpawnLocation().getBlockZ());

        return true;
    }

    private boolean handleTeleport(Player player, String[] args) {
        if (!player.hasPermission("tli.world.teleport")) {
            player.sendMessage("§cYou don't have permission to teleport between worlds!");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("§cUsage: /world tp <world_name>");
            return true;
        }

        String worldName = args[1];

        if (!plugin.getArenaManager().canPlayerJoinWorld(player, worldName)) {
            player.sendMessage("§cYou cannot join this world! It may be disabled or not exist.");
            return true;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage("§cWorld not found: " + worldName);
            return true;
        }

        player.teleport(world.getSpawnLocation());
        player.sendMessage("§aTeleported to world: " + worldName);

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage("§6=== World Commands ===");
        player.sendMessage("§e/world create <name> §7- Create a new void world");
        player.sendMessage("§e/world enable <name> §7- Enable a world");
        player.sendMessage("§e/world disable <name> [kick_players] §7- Disable a world");
        player.sendMessage("§e/world list §7- List all worlds");
        player.sendMessage("§e/world status <name> §7- Check world status");
        player.sendMessage("§e/world tp <name> §7- Teleport to a world");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // First argument - subcommands
            List<String> subCommands = Arrays.asList("create", "enable", "disable", "list", "status", "tp", "teleport");
            for (String subCommand : subCommands) {
                if (subCommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
        } else if (args.length == 2) {
            // Second argument - world names for most commands
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("enable") || subCommand.equals("disable") ||
                    subCommand.equals("status") || subCommand.equals("tp") || subCommand.equals("teleport")) {

                for (World world : Bukkit.getWorlds()) {
                    if (world.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(world.getName());
                    }
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("disable")) {
            // Third argument for disable command - kick_players option
            if ("true".startsWith(args[2].toLowerCase())) {
                completions.add("true");
            }
            if ("false".startsWith(args[2].toLowerCase())) {
                completions.add("false");
            }
        }

        return completions;
    }
}