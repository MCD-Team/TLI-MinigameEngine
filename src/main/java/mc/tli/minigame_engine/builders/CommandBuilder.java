package mc.tli.minigame_engine.builders;

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class CommandBuilder {
    private final JavaPlugin main;
    private String name;
    private String description;
    private String usage;
    private String permission;
    private String permissionMessage;
    private List<String> aliases = new ArrayList<>();
    private BiFunction<CommandSender, String[], Boolean> executor;
    private boolean isPlayerOnly = false;
    private BiFunction<CommandSender, String[], List<String>> tabCompleter;

    public CommandBuilder(JavaPlugin main, String commandName) {
        this.main = main;
        this.name = commandName.toLowerCase();
    }
    public CommandBuilder setDescription(String description) {
        this.description = description;
        return this;
    }
    public CommandBuilder setUsage(String usage) {
        this.usage = usage;
        return this;
    }
    public CommandBuilder setPermission(String permission) {
        this.permission = permission;
        return this;
    }
    public CommandBuilder setPermissionMessage(String permissionMessage) {
        this.permissionMessage = permissionMessage;
        return this;
    }
    public CommandBuilder setAliases(List<String> aliases) {
        this.aliases = aliases;
        return this;
    }
    public CommandBuilder setExecutor(BiFunction<CommandSender, String[], Boolean> executor) {
        this.executor = executor;
        return this;
    }
    public CommandBuilder setPlayerOnly(boolean playerOnly) {
        isPlayerOnly = playerOnly;
        return this;
    }
    public CommandBuilder setTabCompleter(BiFunction<CommandSender, String[], List<String>> tabCompleter) {
        this.tabCompleter = tabCompleter;
        return this;
    }
    public void registerCommand(){
        PluginCommand command = main.getCommand(name);
        if (command == null) {
            main.getLogger().warning("Command " + name + " not found in plugin.yml");
            return;
        }
        command.setDescription(description);
        command.setUsage(usage);
        command.setPermission(permission);
        command.setPermissionMessage(permissionMessage);
        command.setAliases(aliases);
        command.setExecutor((sender, cmd, label, args) -> {
            if (isPlayerOnly && !(sender instanceof org.bukkit.entity.Player)) {
                sender.sendMessage("This command can only be run by a player.");
                return true;
            }
            if(permission != null && !sender.hasPermission(permission)) {
                sender.sendMessage(permissionMessage);
                return true;
            }
            if (executor == null) {
                sender.sendMessage("No executor set for command " + name);
                return true;
            }
            return executor.apply(sender, args);
        });
        command.setTabCompleter((sender, cmd, label, args) -> {;
            if (tabCompleter == null) {
                return null;
            }
            return tabCompleter.apply(sender, args);
        });

    }
}
