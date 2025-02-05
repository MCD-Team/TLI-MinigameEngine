package mc.tli.minigame_engine.instance;

import mc.tli.minigame_engine.GameState;
import mc.tli.minigame_engine.managers.ConfigManager;
import mc.tli.minigame_engine.TliMinigameEngine;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {
    private TliMinigameEngine main;
    private Arena arena;
    private int countdownSeconds;
    private int kickCountdown;
    public Countdown(TliMinigameEngine main, Arena arena) {
        this.main = main;
        this.arena = arena;
        this.countdownSeconds = ConfigManager.getCountDownSeconds();
        this.kickCountdown = ConfigManager.getKickCountdown();
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
        if(kickCountdown == 0){
            cancel();
            arena.kickPlayers();
            return;
        }
        //do something when set time is left
//        if(countdownSeconds <= 10) {arena.sendMessage()}
        kickCountdown--;
        countdownSeconds--;
    }
    public void startKick(){
        runTaskTimer(main, 0, 20);
    }
}
