package net.jadedmc.jadedduels.game.tournament;

import net.jadedmc.jadedcore.JadedAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TournamentTeam {
    private final List<UUID> playerUUIDs = new ArrayList<>();
    private Long challongeID;
    private String name = "";

    public void addPlayer(JadedAPI.NetworkPlayer player) {
        playerUUIDs.add(player.getUniqueID());

        StringBuilder nameBuilder = new StringBuilder(name);

        if(playerUUIDs.size() != 1) {
            nameBuilder.append(", ");
        }

        nameBuilder.append(player.getName());
        name = nameBuilder.toString();
    }

    public void challongeID(long challongeID) {
        this.challongeID = challongeID;
    }

    public Long challongeID() {
        return challongeID;
    }

    public String name() {
        return name;
    }

    public List<UUID> players() {
        return playerUUIDs;
    }
}