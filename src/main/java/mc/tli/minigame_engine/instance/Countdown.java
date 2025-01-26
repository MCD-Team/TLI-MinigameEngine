package mc.tli.minigame_engine.instance;

import mc.tli.minigame_engine.GameState;
import mc.tli.minigame_engine.Managers.ConfigManager;
import mc.tli.minigame_engine.TliMinigameEngine;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {
    private TliMinigameEngine main;
    private Arena arena;
    private int countdownSeconds;
    public Countdown(TliMinigameEngine main, Arena arena) {
        this.main = main;
        this.arena = arena;
        this.countdownSeconds = ConfigManager.getCountDownSeconds();
    }
    public void start() {
        arena.setState(GameState.COUNTINGDOWN);
        runTaskTimer(main, 0, 20);
    }
    @Override
    public void run() {
        if(countdownSeconds == 0) {
            cancel();
            arena.startGame();
            return;
        }
        //do something when set time is left
//        if(countdownSeconds <= 10) {arena.sendMessage()}
        countdownSeconds--;
    }
}
