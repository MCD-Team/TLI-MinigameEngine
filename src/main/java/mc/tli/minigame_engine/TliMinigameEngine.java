package mc.tli.minigame_engine;

import mc.tli.minigame_engine.Managers.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TliMinigameEngine extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigManager.initConfig(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
