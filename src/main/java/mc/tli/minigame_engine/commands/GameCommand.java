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
    private TliMinigameEngine plugin;
    public GameCommand(TliMinigameEngine plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            if(args.length == 1 && args[0].equalsIgnoreCase("leave")){
                Arena arena = plugin.getArenaManager().getArena(player);

                if(arena!= null){
                    if(!arena.getPlayers().isEmpty()){
                        arena.removePlayer(player);
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "You are not in an arena");
                }
            } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                player.sendMessage(ChatColor.GREEN + "Arenas:");
                for(Arena arena : plugin.getArenaManager().getArenas()){
                    player.sendMessage(ChatColor.GREEN+"-"+arena.getId());
                }
            }
            {
                player.sendMessage(ChatColor.RED + "Usage: /game leave");
            }
        }
        return false;
    }
}
