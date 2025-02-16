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
    private TliMinigameEngine main;
    public banUser(TliMinigameEngine main) {
        this.main = main;
    }
    private final ArenaManager arenaManager = new ArenaManager(main);
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player){
            //get the arena bassed of the command sender
            Arena arena = arenaManager.getArena((Player) commandSender);
            Testgame testgame = arena.getGame();
            if(args.length != 0) {
                if(args.length< 2){
                    commandSender.sendMessage(ChatColor.RED + "Usage: /ban player reason");
                    return true;
                }
                //get player based on the first argument passed in the command which should be a players name
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null){
                    commandSender.sendMessage(ChatColor.RED + "Player not found");
                }
                assert target != null;
                UUID uuid = target.getUniqueId();
                //the player hase been found ban him in all possible ways
                if(arena.getPlayers().contains(uuid)){
                    target.banPlayer(args[1]);
                    target.banPlayerIP(args[1]);
                    target.kickPlayer(args[1]);
                    testgame.setPlayerRemoved(true);
                    commandSender.sendMessage(ChatColor.GREEN +target.name().toString()+ " is now banned");
                    return true;
                }

            }
        }else{
            commandSender.sendMessage(ChatColor.RED + "Only players can use this command");
        }
        return false;
    }
}
