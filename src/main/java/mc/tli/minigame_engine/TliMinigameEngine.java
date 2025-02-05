package mc.tli.minigame_engine;

import mc.tLIUtils.Utils;
import mc.tli.minigame_engine.listeners.ConnectListener;
import mc.tli.minigame_engine.listeners.GameListener;
import mc.tli.minigame_engine.managers.ArenaManager;
import mc.tli.minigame_engine.managers.ConfigManager;
import mc.tli.minigame_engine.moderation.banUser;
import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;


public final class TliMinigameEngine extends JavaPlugin {
    private ArenaManager arenaManager;
    public static Utils utils;
    @Override
    public void onEnable() {
       utils = (Utils)Bukkit.getServer().getPluginManager().getPlugin("TLI-Utils");
        ConfigManager.initConfig(this);
        Bukkit.getPluginManager().registerEvents(new GameListener(this),this);
        Bukkit.getPluginManager().registerEvents(new ConnectListener(this),this);
        arenaManager = new ArenaManager(this);
        getCommand("minigameban").setExecutor(new banUser(null,null));
    }
    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
