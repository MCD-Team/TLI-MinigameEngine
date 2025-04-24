package mc.tli.minigame_engine.instance;

import mc.tli.minigame_engine.GameState;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.HashMap;

abstract class Game {
    public static HashMap<Integer,Location> islandLocations = new HashMap<Integer,Location>();
    abstract void StartGame();
}
