package mc.tli.minigame_engine;

import mc.tLIUtils.Utils;
import mc.tli.minigame_engine.commands.GameCommand;
import mc.tli.minigame_engine.listeners.MenuListener;
import mc.tli.minigame_engine.managers.ArenaManager;
import mc.tli.minigame_engine.managers.ConfigManager;
import mc.tli.minigame_engine.commands.banUser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TliMinigameEngine extends JavaPlugin {
    public static Utils utils;
    private ArenaManager arenamanger;
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        try{
            ConfigManager.initConfig(this);
        }catch(NullPointerException e){
            System.out.println("Error initializing config");
        }

        utils = (Utils)Bukkit.getServer().getPluginManager().getPlugin("TLI-Utils");
        this.arenamanger = new ArenaManager(this);
        arenamanger.addArena();

        //register commands
        getCommand("moderationban").setExecutor(new banUser(this));
        getCommand("game").setExecutor(new GameCommand(this));
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);
    }
    public ArenaManager getArenaManager() {
        return arenamanger;
    }
    public Utils getUtils() {
        return utils;
    }
}
