package mc.tli.minigame_engine.instance;
//This class contains the countdown logic for the game

import mc.tli.minigame_engine.enums.GameState;
import mc.tli.minigame_engine.managers.ConfigManager;
import mc.tli.minigame_engine.TliMinigameEngine;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {
    private final TliMinigameEngine main;
    private final Arena arena;
    private int countdownSeconds;
    private final BossBar  bossBar = Arena.createBossbar("Game starting in...",BarColor.RED,BarStyle.SOLID);
    private final ConfigManager configManager;
    public Countdown(TliMinigameEngine main, Arena arena) {
        this.configManager = new ConfigManager(main);
        this.main = main;
        this.arena = arena;
        this.countdownSeconds = configManager.getCountDownSeconds();
    }
//set the arena state to countingdown and teleport all players currently in the arena
    public void start() {
        arena.setState(GameState.COUNTINGDOWN);
        Arena.addBossbar(arena.getPlayers(),bossBar);
        runTaskTimer(main, 0, 20);
    }
    public void startCounter(int delay, int duration) {
        runTaskTimer(main, delay, duration);
    }

//handel actual countdown logic
    @Override
    public void run() {
        if (countdownSeconds == 0) {
            cancel();
            Arena.removeBossbar(bossBar);
            arena.startGame();
            return;
        }
        bossBar.setTitle("Game starting in " + countdownSeconds + " seconds");
        bossBar.setProgress(countdownSeconds / (double) configManager.getCountDownSeconds());
        countdownSeconds--;
    }
}