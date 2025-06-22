package mc.tli.minigame_engine;

import mc.tli.minigame_engine.builders.CommandBuilder;
import mc.tli.minigame_engine.commands.GameCommand;
import mc.tli.minigame_engine.listeners.MenuListener;
import mc.tli.minigame_engine.managers.ArenaManager;
import mc.tli.minigame_engine.managers.ConfigManager;
import mc.tli.minigame_engine.commands.banUser;

import mc.tliUtils.Utils;
import mc.tliUtils.guis.Guis;
import mc.tliUtils.guis.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

public final class TliMinigameEngine extends JavaPlugin {
    private ArenaManager arenamanger;
    private ConfigManager configmanager;
    public static Logger logger;
    //needed to create a utils instance to use the guis and utilities
    private static Utils utils;
    private Guis guis;
    private Utilities utilities;

    @Override
    public void onEnable() {
        utils = Utils.getInstance();
        guis = Utils.getGuis();
        utilities = Utils.getUtilities();
        logger = this.getLogger();
        if(guis == null){
            getLogger().warning("Guis is null");
        }else if(utilities == null){
            getLogger().warning("utilities is null");
        }

        this.arenamanger = new ArenaManager(this);
        this.configmanager = new ConfigManager(this);

        Bukkit.getScheduler().runTask(this, () -> {
            if (Bukkit.getWorlds().isEmpty()) {
                getLogger().severe("NO WORLDS LOADED! Check server logs");
            } else {
                getLogger().info("Loaded worlds: " + Bukkit.getWorlds());
            }
        });
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        try{
            configmanager.initConfig();
        }catch(NullPointerException e){
            System.out.println("Error initializing config");
        }
        arenamanger.addArena();

        //register commands
        Objects.requireNonNull(getCommand("moderationban")).setExecutor(new banUser(this));
        Objects.requireNonNull(getCommand("game")).setExecutor(new GameCommand(this));
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);
        new CommandBuilder(this,"test")
                .setDescription("Test command")
                .setUsage("/test")
                .setPermission("tliutils.test")
                .setPermissionMessage("You do not have permission to use this command")
                .setExecutor((sender, args) -> {
                    sender.sendMessage("Test command executed");
                    return true;
                })
                .setTabCompleter((sender,args) ->{
                    if(args.length == 1){
                        return Arrays.asList("arg1","arg2");
                    }
                    sender.sendMessage("Wrong number of arguments");
                    return null;
                })
                .registerCommand();
    }
    //Getters
    public ArenaManager getArenaManager() {
        return arenamanger;
    }
    public ConfigManager getConfigManager() {
        return configmanager;
    }
}
