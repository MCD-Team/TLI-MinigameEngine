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
    private ConfigManager configmanager;
    @Override
    public void onEnable() {
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
            configmanager.initConfig(this);
        }catch(NullPointerException e){
            System.out.println("Error initializing config");
        }
        utils = (Utils)Bukkit.getServer().getPluginManager().getPlugin("TLI-Utils");
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
