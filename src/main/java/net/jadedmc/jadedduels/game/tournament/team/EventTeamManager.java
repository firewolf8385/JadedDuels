package net.jadedmc.jadedduels.game.tournament.team;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventTeamManager {
    private final List<EventTeam> teams = new ArrayList<>();

    public EventTeam createTeam(Player player) {
        EventTeam team = new EventTeam();
        team.addPlayer(player);
        teams.add(team);
        return team;
    }

    public EventTeam createTeam(List<Player> players) {
        EventTeam team = new EventTeam();
        players.forEach(team::addPlayer);
        teams.add(team);
        return team;
    }

    public EventTeam team(Long challongeID) {
        for(EventTeam team : teams) {
            if(team.challongeID().equals(challongeID)) {
                return team;
            }
        }

        return null;
    }

    public EventTeam team(Player player) {
        for(EventTeam team : teams) {
            if(team.players().contains(player)) {
                return team;
            }
        }

        return null;
    }

    public EventTeam team(String name) {
        for(EventTeam team : teams) {
            if(team.name().equals(name)) {
                return team;
            }
        }

        return null;
    }

    public Collection<EventTeam> teams() {
        return teams;
    }
}