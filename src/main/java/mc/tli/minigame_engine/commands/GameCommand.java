package mc.tli.minigame_engine.commands;

import mc.tli.minigame_engine.TliMinigameEngine;
import mc.tli.minigame_engine.instance.Arena;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GameCommand implements CommandExecutor {
    private final TliMinigameEngine plugin;
    public GameCommand(TliMinigameEngine plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player player) {
            Arena arena = plugin.getArenaManager().getArena(player);
            commandSender.sendMessage("DEBUG: Arena is " + (arena == null ? "null" : "not null"));
            if(arena == null) {
               plugin.getArenaManager().addArena();
               commandSender.sendMessage(NamedTextColor.GREEN + "Arena added to the game!");
               arena = plugin.getArenaManager().getArena(player);
               commandSender.sendMessage("DEBUG: Arena is " + (arena == null ? "null" : "not null"));
            }

            //check if there is 1 argument given and if the string given contains leave then remove the player from the arena
            if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
                if (arena != null) {
                    if (!arena.getPlayers().isEmpty()) {
                        arena.removePlayer(player);
                    }else{
                        player.sendMessage(NamedTextColor.RED + "No arena's found");
                    }
                } else {
                    player.sendMessage(NamedTextColor.RED + "You are not in an arena");
                }
            } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                player.sendMessage(NamedTextColor.GREEN + "Arenas:");
                if(plugin.getArenaManager().getArenas().isEmpty()) {
                    player.sendMessage(NamedTextColor.RED + "No arenas found");
                }
                for (Arena ar : plugin.getArenaManager().getArenas()) {
                    player.sendMessage(NamedTextColor.GREEN + "-" + ar.getId());
                }
                return false;

            }else {
                player.sendMessage(NamedTextColor.RED + "Usage: /game leave");
            }
        }
        return false;
    }
}
