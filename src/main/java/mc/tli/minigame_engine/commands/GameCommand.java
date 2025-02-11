package mc.tli.minigame_engine.commands;

import mc.tli.minigame_engine.TliMinigameEngine;
import mc.tli.minigame_engine.instance.Arena;
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
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            Arena arena = plugin.getArenaManager().getArena(player);
            if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {


                if (arena != null) {
                    if (!arena.getPlayers().isEmpty()) {
                        arena.removePlayer(player);
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You are not in an arena");
                }
            } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                player.sendMessage(ChatColor.GREEN + "Arenas:");
                if(plugin.getArenaManager().getArenas().isEmpty()) {
                    player.sendMessage(ChatColor.RED + "No arenas found");
                }
                for (Arena ar : plugin.getArenaManager().getArenas()) {
                    player.sendMessage(ChatColor.GREEN + "-" + ar.getId());
                }


                return false;
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /game leave");
            }
        }
        return false;
    }
}
