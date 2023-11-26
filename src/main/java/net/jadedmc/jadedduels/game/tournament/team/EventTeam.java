package net.jadedmc.jadedduels.game.tournament.team;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventTeam {
    private final List<UUID> playerUUIDs = new ArrayList<>();
    private Long challongeID;
    private String name = "";

    public void addPlayer(Player player) {
        playerUUIDs.add(player.getUniqueId());

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

    public List<Player> players() {
        List<Player> teamPlayers = new ArrayList<>();

        for(UUID playerUUID : playerUUIDs) {
            Player player = Bukkit.getPlayer(playerUUID);

            if(player == null || !player.isOnline()) {
                continue;
            }

            teamPlayers.add(player);
        }

        return teamPlayers;
    }
}
