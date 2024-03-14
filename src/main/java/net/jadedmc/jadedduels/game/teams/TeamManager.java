/*
 * This file is part of JadedDuels, licensed under the MIT License.
 *
 *  Copyright (c) JadedMC
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package net.jadedmc.jadedduels.game.teams;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {
    private final List<Team> teams = new ArrayList<>();
    private final List<Team> aliveTeams = new ArrayList<>();

    public Team createTeam(List<String> players, TeamColor teamColor) {
        Team team = new Team(players, teamColor, teams().size() + 1);

        teams.add(team);
        aliveTeams.add(team);

        return team;
    }

    public Team createTeam(List<String> players, TeamColor teamColor, long challongeID) {
        Team team = new Team(players, teamColor, teams().size() + 1, challongeID);

        teams.add(team);
        aliveTeams.add(team);

        return team;
    }

    /**
     * Get all teams that are still alive.
     * @return All alive teams.
     */
    public List<Team> aliveTeams() {
        return aliveTeams;
    }

    /**
     * Get the team of a specific player.
     * Returns null if no team.
     * @param player Player to get team of.
     * @return Team the player is in.
     */
    public Team team(Player player) {
        for(Team team : teams()) {
            if(team.players().contains(player)) {
                return team;
            }
        }

        return null;
    }

    /**
     * Get all existing teams in the manager.
     * @return All existing teams.
     */
    public List<Team> teams() {
        return teams;
    }

    /**
     * Gets an opposing team given a team.
     * Kind of a hacky way to handle duels.
     * @param team Given team.
     * @return Opposing team.
     */
    public Team opposingTeam(Team team) {
        for(Team opposingTeam : teams) {
            if(team.equals(opposingTeam)) {
                continue;
            }

            return opposingTeam;
        }

        return null;
    }

    /**
     * Kill a team.
     */
    public void killTeam(Team team) {
        aliveTeams.remove(team);
    }

    /**
     * Resets all teams.
     * Done after the end of each round.
     */
    public void reset() {
        teams.forEach(Team::reset);

        aliveTeams.clear();

        teams.forEach(team -> {
            if(team.alivePlayers().size() > 0) {
                aliveTeams.add(team);
            }
        });
    }
}