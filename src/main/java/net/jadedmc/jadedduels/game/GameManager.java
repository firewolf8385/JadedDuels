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

import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedcore.servers.Server;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.arena.Arena;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedduels.game.teams.TeamColor;
import net.jadedmc.jadedutils.FileUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class GameManager {
    private final JadedDuelsPlugin plugin;
    private final Collection<Game> games = new HashSet<>();

    /**
     * Creates the Game Manager.
     * @param plugin Instance of the plugin.
     */
    public GameManager(final JadedDuelsPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates a game with a random arena and sends it to Redis.
     * @param kit Kit the game is using.
     * @param gameType Type of game.
     * @param teams Teams to add to the game.
     */
    public void createGame(Kit kit, GameType gameType, List<UUID>... teams) {
        // Finds an arena
        List<Arena> arenas = new ArrayList<>(plugin.arenaManager().getArenas(kit, gameType));
        Collections.shuffle(arenas);

        createGame(arenas.get(0), kit, gameType, teams);
    }

    /**
     * Creates a game and sends it to Redis.
     * @param arena Arena the game is in.
     * @param kit Kit the game is using.
     * @param gameType Type of game.
     * @param teams Teams to add to the game.
     */
    public void createGame(Arena arena, Kit kit, GameType gameType, List<UUID>... teams) {
        JadedAPI.getServers().thenAccept(servers -> {
            UUID gameUUID = UUID.randomUUID();

            String serverName = "";
            {
                System.out.println("Servers Found: " + servers.size());
                int count = 999;

                // Loop through all online servers looking for a server to send the game to.
                for(Server server : servers) {
                    // Make sure the server is a duels server.
                    if(!server.mode().equalsIgnoreCase("DUELS")) {
                        continue;
                    }

                    // Make sure the server isn't a lobby server.
                    if(!server.type().equalsIgnoreCase("GAME")) {
                        continue;
                    }

                    // Make sure there is room for another game.
                    if(server.online() + 2 >= server.capacity()) {
                        continue;
                    }

                    //
                    if(server.online() < count) {
                        count = server.online();
                        serverName = server.name();
                    }
                }

                // If no server is found, give up ¯\_(ツ)_/¯
                if(count == 999) {
                    return;
                }
            }

            System.out.println("Writing Document...");
            // Create the document to eventually send to Redis.
            Document document = new Document()
                    .append("uuid", gameUUID.toString())
                    .append("arena", arena.fileName())
                    .append("kit", kit.id())
                    .append("gameType", gameType.toString())
                    .append("gameState", GameState.SETUP.toString())
                    .append("server", serverName);


            // Create teams.
            List<TeamColor> availableColors = new ArrayList<>();
            availableColors.add(TeamColor.RED);
            availableColors.add(TeamColor.BLUE);
            availableColors.add(TeamColor.GREEN);
            availableColors.add(TeamColor.YELLOW);

            Document teamsDocument = new Document();
            for(List<UUID> team : teams) {
                // Assign team color.
                TeamColor color = availableColors.get(0);
                availableColors.remove(0);

                // Load uuids.
                List<String> uuids = new ArrayList<>();
                team.forEach(uuid -> uuids.add(uuid.toString()));

                // Add to document.
                Document teamDocument = new Document().append("uuids", uuids);
                teamsDocument.append(color.toString(), teamDocument);
            }
            document.append("teams", teamsDocument);

            // Update Redis
            JadedAPI.getRedis().set("games:" + gameUUID, document.toJson());
            JadedAPI.getRedis().publish("game", "create " + gameUUID);
        }).whenComplete((result, error) -> error.printStackTrace());
    }

    /**
     * Deletes a game that is no longer needed.
     * This also deletes its temporary world folder.
     * @param game Game to delete.
     */
    public void deleteGame(Game game) {
        games.remove(game);
        File worldFolder = game.world().getWorldFolder();
        Bukkit.unloadWorld(game.world(), false);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> FileUtils.deleteDirectory(worldFolder));
    }

    /**
     * Loads a game from a document.
     * Generally loaded from Redis.
     * @param document Document to create game with.
     * @return Created Game.
     */
    public CompletableFuture<Game> fromDocument(Document document) {
        // Makes a copy of the arena.
        CompletableFuture<World> worldCopy = JadedAPI.getPlugin().worldManager().copyWorld(document.getString("arena"), document.getString("uuid"));

        // Creates a game using that info.
        return worldCopy.thenCompose(world -> CompletableFuture.supplyAsync(() -> {
            Game game = new Game(plugin, world, document);
            games.add(game);

            return game;
        }));
    }

    /**
     * Get the game a given player is currently in.
     * Null if not in a game.
     * @param player Player to get game of.
     * @return Game they are in.
     */
    public Game game(Player player) {
        // Makes a copy of the active games to prevent ConcurrentModificationException.
        List<Game> games = new ArrayList<>(this.games);

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
        List<Game> games = new ArrayList<>(this.games);

        for(Game game : games) {
            if(game.world().equals(world)) {
                return game;
            }
        }

        return null;
    }

    /**
     * Get all active games.
     * @return Collection of all current games.
     */
    public Collection<Game> games() {
        return new HashSet<>(games);
    }
}