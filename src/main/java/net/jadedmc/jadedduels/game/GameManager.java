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
package net.jadedmc.jadedduels.game;

import at.stefangeyer.challonge.model.Match;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.arena.Arena;
import net.jadedmc.jadedduels.game.arena.ArenaChunkGenerator;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedutils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class GameManager {
    private final JadedDuelsPlugin plugin;
    private final Collection<Game> activeGames = new HashSet<>();

    /**
     * Creates the Game Manager.
     * @param plugin Instance of the plugin.
     */
    public GameManager(final JadedDuelsPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets all currently existing games.
     * @return All active games.
     */
    public Collection<Game> activeGames() {
        return activeGames;
    }

    /**
     * Manually add a game. Used in duels.
     * @param game Game to add.
     */
    public void addGame(Game game) {
        activeGames.add(game);
    }

    /**
     * Creates a game
     * @param arena Arena the game should use.
     * @param kit Kit the game should use.
     * @param gameType Game type the game should use.
     * @return Created game.
     */
    public CompletableFuture<Game> createGame(Arena arena, Kit kit, GameType gameType) {
        UUID gameUUID = UUID.randomUUID();

        // Makes a copy of the arena with the generated uuid.
        CompletableFuture<File> arenaCopy = arena.arenaFile().createCopy(gameUUID.toString());

        return arenaCopy.thenCompose(file -> CompletableFuture.supplyAsync(() -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                WorldCreator worldCreator = new WorldCreator(gameUUID.toString());
                worldCreator.generator(new ArenaChunkGenerator());
                Bukkit.createWorld(worldCreator);
            });

            // Wait for the world to be generated.
            boolean loaded = false;
            World world = null;
            while(!loaded) {
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                for(World w : Bukkit.getWorlds()) {
                    if(w.getName().equals(gameUUID.toString())) {
                        loaded = true;
                        world = w;
                        break;
                    }
                }
            }

            return new Game(plugin, kit,gameType, arena, world, gameUUID);
        }));
    }

    public CompletableFuture<Game> createGame(Arena arena, Kit kit, GameType gameType, Match match) {
        UUID gameUUID = UUID.randomUUID();

        // Makes a copy of the arena with the generated uuid.
        CompletableFuture<File> arenaCopy = arena.arenaFile().createCopy(gameUUID.toString());

        // Creates the game.
        CompletableFuture<Game> gameCreation = CompletableFuture.supplyAsync(() -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                WorldCreator worldCreator = new WorldCreator(gameUUID.toString());
                worldCreator.generator(new ArenaChunkGenerator());
                Bukkit.createWorld(worldCreator);
            });

            // Wait for the world to be generated.
            boolean loaded = false;
            World world = null;
            while(!loaded) {
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                for(World w : Bukkit.getWorlds()) {
                    if(w.getName().equals(gameUUID.toString())) {
                        loaded = true;
                        world = w;
                        break;
                    }
                }
            }

            return new Game(plugin, kit,gameType, arena, world, gameUUID, match);
        });

        return arenaCopy.thenCompose(file -> gameCreation);
    }

    /**
     * Deletes a game that is no longer needed.
     * This also deletes its temporary world folder.
     * @param game Game to delete.
     */
    public void deleteGame(Game game) {
        activeGames.remove(game);
        File worldFolder = game.world().getWorldFolder();
        Bukkit.unloadWorld(game.world(), false);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            FileUtils.deleteDirectory(worldFolder);
        });
    }

    /**
     * Get the game a given player is currently in.
     * Null if not in a game.
     * @param player Player to get game of.
     * @return Game they are in.
     */
    public Game game(Player player) {
        // Makes a copy of the active games to prevent ConcurrentModificationException.
        List<Game> games = new ArrayList<>(activeGames);

        // Loop through each game looking for the player.
        for(Game game : games) {
            if(game.players().contains(player)) {
                return game;
            }

            if(game.spectators().contains(player)) {
                return game;
            }
        }

        return null;
    }

    /**
     * Get the game of a given world.
     * Returns null if there isn't one.
     * @param world World to get game of.
     * @return Game using that world.
     */
    public Game game(World world) {
        // Makes a copy of the active games to prevent ConcurrentModificationException.
        List<Game> games = new ArrayList<>(activeGames);

        for(Game game : games) {
            if(game.world().equals(world)) {
                return game;
            }
        }

        return null;
    }

    public int playing() {
        int playing = 0;

        for(Game game : activeGames) {
            if(game.gameType() == GameType.TOURNAMENT) {
                continue;
            }

            playing += game.players().size();
            playing += game.spectators().size();
        }

        return playing;
    }
}