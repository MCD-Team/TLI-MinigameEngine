package mc.tli.minigame_engine;

import mc.tLIUtils.Utils;
import mc.tli.minigame_engine.Managers.ConfigManager;
import mc.tli.minigame_engine.instance.Testgame;
import mc.tli.minigame_engine.moderation.banUser;
import org.bukkit.Bukkit;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public final class TliMinigameEngine extends JavaPlugin {
    public static Utils utils;
    @Override
    public void onEnable() {
       utils = (Utils)Bukkit.getServer().getPluginManager().getPlugin("TLI-Utils");
        ConfigManager.initConfig(this);
        getCommand("minigameban").setExecutor(new banUser(null,null));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
