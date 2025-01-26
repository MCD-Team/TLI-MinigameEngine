package mc.tli.minigame_engine;

import mc.tli.minigame_engine.Managers.ConfigManager;
import mc.tli.minigame_engine.instance.Testgame;
import mc.tli.minigame_engine.moderation.banUser;
import org.bukkit.plugin.java.JavaPlugin;

public final class TliMinigameEngine extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigManager.initConfig(this);
        getCommand("minigameban").setExecutor(new banUser(null,null));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
