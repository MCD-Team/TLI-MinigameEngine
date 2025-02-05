package mc.tli.minigame_engine.instance;

import mc.tli.minigame_engine.GameState;
import mc.tli.minigame_engine.managers.ConfigManager;
import mc.tli.minigame_engine.TliMinigameEngine;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Countdown extends BukkitRunnable {
    private TliMinigameEngine main;
    private Arena arena;
    private int countdownSeconds;
    private BossBar bossBar;

    public Countdown(TliMinigameEngine main, Arena arena) {
        this.main = main;
        this.arena = arena;
        this.countdownSeconds = ConfigManager.getCountDownSeconds();
        this.bossBar = Bukkit.createBossBar("Game starting in...", BarColor.RED, BarStyle.SOLID);
    }

    public void start() {
        arena.setState(GameState.COUNTINGDOWN);
        for (UUID uuid : arena.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                bossBar.addPlayer(player);
            }
        }
        runTaskTimer(main, 0, 20);
    }

    @Override
    public void run() {
        if (countdownSeconds == 0) {
            cancel();
            bossBar.removeAll();
            arena.startGame();
            return;
        }

        bossBar.setTitle("Game starting in " + countdownSeconds + " seconds");
        bossBar.setProgress(countdownSeconds / (double) ConfigManager.getCountDownSeconds());
        countdownSeconds--;
    }
}