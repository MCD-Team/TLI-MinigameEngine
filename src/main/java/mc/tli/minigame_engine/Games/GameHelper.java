package mc.tli.minigame_engine.Games;

import mc.tli.minigame_engine.TliMinigameEngine;
import me.sk8ingduck.friendsystem.SpigotAPI;
import me.sk8ingduck.friendsystem.manager.PartyManager;

import java.util.HashMap;
import java.util.UUID;

public class GameHelper {
    SpigotAPI api = SpigotAPI.getInstance();
    PartyManager partyManager = api.getPartyManager();

    private TliMinigameEngine mainClassName;

    public GameHelper(TliMinigameEngine mainClassName) {
        this.mainClassName = mainClassName;
    }

    public void makeTeams(String[] players, String gameName, Integer teamSize) {

        HashMap<String, String> parties = new HashMap<>();

        if (players == null || players.length < 1) {
            mainClassName.getLogger().warning("" + gameName + " - Players array is null or empty. Cannot create teams.");
            throw new IllegalArgumentException("Players array cannot be null or empty");
        }

        if (gameName == null || gameName.isEmpty()) {
            mainClassName.getLogger().warning("Game name is null or empty. Cannot create teams.");
            throw new IllegalArgumentException("Game name cannot be null or empty");
        }

        if (teamSize == null || teamSize <= 0) {
            mainClassName.getLogger().warning("Team size is null or less than 1. Cannot create teams.");
            throw new IllegalArgumentException("Team size must be greater than 0");
        }

        for (String player : players) {
            String party = partyManager.getParty(UUID.fromString(player)).toString();
            if (!player.isEmpty()) {
                {
                    parties.put(player, party);
                }
            }
        }


    }

}
