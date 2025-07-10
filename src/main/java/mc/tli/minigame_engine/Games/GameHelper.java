package mc.tli.minigame_engine.Games;

import mc.tli.minigame_engine.TliMinigameEngine;
import me.sk8ingduck.friendsystem.SpigotAPI;
import me.sk8ingduck.friendsystem.manager.PartyManager;

import java.util.*;

public class GameHelper {
    SpigotAPI api = SpigotAPI.getInstance();
    PartyManager partyManager = api.getPartyManager();

    private final TliMinigameEngine mainClassName;

    public GameHelper(TliMinigameEngine mainClassName) {
        this.mainClassName = mainClassName;
    }

    public void makeTeams(String[] players, String gameName, Integer teamSize) {
        Map<String, List<String>> partyGroups = new HashMap<>();
        List<String> team1 = new ArrayList<>(teamSize / 2);
        List<String> team2 = new ArrayList<>(teamSize / 2);
        Set<String> addedPlayers = new HashSet<>();

        if (players == null || players.length < 1)
            throw new IllegalArgumentException("Players array cannot be null or empty");
        if (gameName == null || gameName.isEmpty())
            throw new IllegalArgumentException("Game name cannot be null or empty");
        if (teamSize == null || teamSize <= 0)
            throw new IllegalArgumentException("Team size must be greater than 0");

        int maxPerTeam = teamSize / 2;

        // Group players by party
        for (String player : players) {
            if (player == null || player.isEmpty()) continue;

            UUID uuid = UUID.fromString(player);
            String partyName = partyManager.getParty(uuid) != null ? partyManager.getParty(uuid).toString() : null;

            if (partyName != null && !partyName.equalsIgnoreCase("null")) {
                partyGroups.computeIfAbsent(partyName, k -> new ArrayList<>()).add(player);
            }
        }

        // All party members added together
        for (List<String> partyMembers : partyGroups.values()) {
            for (String player : partyMembers) {
                if (addedPlayers.contains(player)) continue;

                if (team1.size() < maxPerTeam) {
                    team1.add(player);
                    addedPlayers.add(player);
                } else if (team2.size() < maxPerTeam) {
                    team2.add(player);
                    addedPlayers.add(player);
                } else {
                    mainClassName.getLogger().warning("Both teams full, cannot add player " + player);
                }
            }
        }

        // Add solo players
        for (String player : players) {
            if (addedPlayers.contains(player)) continue;

            if (team1.size() < maxPerTeam) {
                team1.add(player);
                addedPlayers.add(player);
            } else if (team2.size() < maxPerTeam) {
                team2.add(player);
                addedPlayers.add(player);
            } else {
                mainClassName.getLogger().warning("Both teams full, cannot add solo player " + player);
            }
        }

        // Debug output
        mainClassName.getLogger().info(gameName + " - Team 1 (" + team1.size() + "): " + team1);
        mainClassName.getLogger().info(gameName + " - Team 2 (" + team2.size() + "): " + team2);
    }

    public void addToQueue() {

    }

    public void makeQueue() {

    }
}
