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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Represents a group of players working together in a Game.
 */
public class Team {
    private final List<UUID> players = new ArrayList<>();
    private final Set<UUID> alivePlayers = new HashSet<>();
    private final Set<UUID> deadPlayers = new HashSet<>();
    private int score = 0;
    private final TeamColor teamColor;
    private final  int id;

    /**
     * Creates a team.
     * @param uuids UUIDs of all team members.
     * @param teamColor Team color being used.
     */
    public Team(List<String> uuids, TeamColor teamColor, int id) {
        // Load players.
        uuids.forEach(uuid -> players.add(UUID.fromString(uuid)));
        alivePlayers.addAll(players);

        // Cache team color.
        this.teamColor = teamColor;

        this.id = id;
    }

    /**
     * Adds a point to the team.
     */
    public void addPoint() {
        score++;
    }

    /**
     * Get all alive players on the team.
     * @return Set of alive players.
     */
    public Set<Player> alivePlayers() {
        Set<Player> onlinePlayers = new HashSet<>();

        // Loop through all stored team member uuids.
        for(UUID uuid : alivePlayers) {
            Player player = Bukkit.getPlayer(uuid);

            // Makes sure the player is valid.
            if(player == null || !player.isOnline()) {
                continue;
            }

            onlinePlayers.add(player);
        }

        // Return our created list.
        return onlinePlayers;
    }

    /**
     * Get all dead players on the team.
     * @return Set of dead players.
     */
    public Set<Player> deadPlayers() {
        Set<Player> onlinePlayers = new HashSet<>();

        // Loop through all stored team member uuids.
        for(UUID uuid : deadPlayers) {
            Player player = Bukkit.getPlayer(uuid);

            // Makes sure the player is valid.
            if(player == null || !player.isOnline()) {
                continue;
            }

            onlinePlayers.add(player);
        }

        // Return our created list.
        return onlinePlayers;
    }

    /**
     * Get the team's numerical id.
     * Used for tab list sorting.
     * @return Team id.
     */
    public int id() {
        return id;
    }

    /**
     * Add a player to the dead list,
     * and remove them from the alive list.
     * @param player Player to make dead.
     */
    public void killPlayer(Player player) {
        alivePlayers.remove(player.getUniqueId());
        deadPlayers.add(player.getUniqueId());
    }

    /**
     * Get all players on the team.
     * @return List of team players.
     */
    public List<Player> players() {
        List<Player> onlinePlayers = new ArrayList<>();

        // Loop through all stored team member uuids.
        for(UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);

            // Makes sure the player is valid.
            if(player == null || !player.isOnline()) {
                continue;
            }

            onlinePlayers.add(player);
        }

        // Return our created list.
        return onlinePlayers;
    }

    /**
     * Remove a player from the team.
     * @param player Player to remove.
     */
    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        alivePlayers.remove(player.getUniqueId());
        deadPlayers.remove(player.getUniqueId());
    }

    /**
     * Resets a team's alive and dead players.
     * Avoids removing any dead players that are no longer online.
     */
    public void reset() {
        new HashSet<>(deadPlayers()).forEach(player -> {
            alivePlayers.add(player.getUniqueId());
            deadPlayers.remove(player.getUniqueId());
        });
    }

    /**
     * Gets the score of the team.
     * @return Team score.
     */
    public int score() {
        return score;
    }

    /**
     * Get the team's current color.
     * @return Team's team color.
     */
    public TeamColor teamColor() {
        return teamColor;
    }

    /**
     * Get all player uuids on the team.
     * @return List of player uuids.
     */
    public List<UUID> uuids() {
        return players;
    }
}