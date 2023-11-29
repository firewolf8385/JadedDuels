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
package net.jadedmc.jadedduels.player;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This class manages the Duels Player object, which stores data specific to the Duels game mode.
 */
public class DuelsPlayerManager {
    private final JadedDuelsPlugin plugin;
    private final Map<Player, DuelsPlayer> duelsPlayers = new HashMap<>();

    /**
     * Initializes the Duels Player Manager.
     * @param plugin Instance of the plugin.
     */
    public DuelsPlayerManager(final JadedDuelsPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Add a player to the player list.
     * @param player Player to add.
     * @return DuelsPlayer completable future.
     */
    public CompletableFuture<DuelsPlayer> addPlayer(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            DuelsPlayer duelsPlayer = new DuelsPlayer(plugin, player.getUniqueId());
            duelsPlayers.put(player, duelsPlayer);
            return duelsPlayer;
        });
    }

    /**
     * Loads a Duels Player without storing it in the manager.
     * Useful for stats viewers.
     * @param uuid UUID of the player.
     * @return Corresponding Duels Player.
     */
    public CompletableFuture<DuelsPlayer> loadPlayer(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            return new DuelsPlayer(plugin, uuid);
        });
    }

    /**
     * Get the DuelsPlayer of a player
     * @param player Player to get DuelsPlayer of.
     * @return DuelsPlayer of the player.
     */
    public DuelsPlayer player(Player player) {
        return duelsPlayers.get(player);
    }

    /**
     * Get a DuelsPlayer from a UUID.
     * @param uuid UUID of the player.
     * @return DuelsPlayer of the player.
     */
    public DuelsPlayer player(UUID uuid) {
        return player(plugin.getServer().getPlayer(uuid));
    }

    /**
     * Remove a player from the player list.
     * @param player Player to remove.
     */
    public void removePlayer(Player player) {
        duelsPlayers.remove(player);
    }
}