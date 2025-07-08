package mc.tli.minigame_engine.instance;

import org.bukkit.Location;

import java.util.HashMap;

public abstract class Game {
    public static HashMap<Integer,Location> islandLocations = new HashMap<Integer,Location>();
    private final Arena arena = null;
    public abstract void StartGame();
}
