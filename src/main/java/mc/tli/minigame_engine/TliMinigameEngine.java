package mc.tli.minigame_engine;

import mc.tLIUtils.Utils;
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
       utils = (Utils)Bukkit.getServer().getPluginManager().getPlugin("TLI-Utils");
        ConfigManager.initConfig(this);
        getCommand("minigameban").setExecutor(new banUser(null,null));
        this.arenamanger = new ArenaManager(this);
    }
    public ArenaManager getArenaManager() {
        return arenamanger;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
