package mc.tli.minigame_engine.commands;

import mc.tli.minigame_engine.TliMinigameEngine;
import mc.tli.minigame_engine.instance.Arena;
import mc.tli.minigame_engine.instance.Testgame;
import mc.tli.minigame_engine.managers.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public class banUser implements CommandExecutor {
    private final TliMinigameEngine main;
    private final ArenaManager arenaManager = new ArenaManager(new TliMinigameEngine());
    public banUser(TliMinigameEngine main) {
        this.main = main;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player){
            Arena arena = arenaManager.getArena((Player) commandSender);
            Testgame testgame = arena.getGame();
            if(args.length != 0) {
                if(args.length< 2){
                    commandSender.sendMessage(ChatColor.RED + "Usage: /ban player reason");
                    return true;
                }

                UUID uuid = Bukkit.getPlayer(args[0]).getUniqueId();
                if(uuid == null){
                    commandSender.sendMessage(ChatColor.RED + "Player not found");
                }


                Player target = Bukkit.getPlayer(uuid);
                if(target == null){
                    commandSender.sendMessage(ChatColor.RED + "Player not found");
                }
                if(arena.getPlayers().contains(target)){
                    target.banPlayer(args[1]);
                    target.banPlayerIP(args[1]);
                    target.kickPlayer(args[1]);
                    testgame.setPlayerRemoved(true);
                    commandSender.sendMessage(ChatColor.GREEN + "Player is now banned");
                    return true;
                }

            }
        }else{
            commandSender.sendMessage(ChatColor.RED + "Only players can use this command");
        }


        return false;
    }
}
