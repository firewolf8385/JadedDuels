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

import com.cryptomorin.xseries.XSound;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.arena.Arena;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedduels.game.lobby.LobbyUtils;
import net.jadedmc.jadedduels.game.team.Team;
import net.jadedmc.jadedduels.game.team.TeamManager;
import net.jadedmc.jadedduels.utils.GameUtils;
import net.jadedmc.jadedduels.utils.LocationUtils;
import net.jadedmc.jadedduels.utils.Timer;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Represents a singular match.
 */
public class Game {
    private final JadedDuelsPlugin plugin;

    // Starting variables.
    private final Kit kit;
    private final Arena arena;
    private final World world;
    private final UUID uuid;
    private final GameType gameType;
    private final Timer timer;
    private final TeamManager teamManager;

    private GameState gameState;
    private final Collection<Player> spectators = new HashSet<>();


    /**
     * Creates the Game object.
     * @param plugin Instance of the plugin.
     * @param kit Kit being used in the game.
     * @param gameType Type of game being played.
     * @param arena Arena being used.
     * @param world World the game is being played in.
     * @param uuid UUID of the game.
     */
    public Game(final JadedDuelsPlugin plugin, final Kit kit, final GameType gameType, final Arena arena, final World world, final UUID uuid) {
        this.plugin = plugin;
        this.kit = kit;
        this.gameType = gameType;
        this.arena = arena;
        this.world = world;
        this.uuid = uuid;

        this.timer = new Timer(plugin);
        this.teamManager = new TeamManager(plugin);

        plugin.gameManager().addGame(this);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Starts the game.
     */
    public void start() {

        // Spawn teams.
        int spawnCount = 0;
        List<Location> spawns = arena.spawns(world);


        for(Team team : this.teamManager.teams()) {

            // Loop back if we run out of spawns.
            if(spawnCount == spawns.size()) {
                spawnCount = 0;
            }

            // Spawn in each player in the team.
            for(Player player : team.players()) {
                player.teleport(spawns.get(spawnCount));
                kit.apply(player);
                new GameScoreboard(player, this).update(player);
            }

            spawnCount++;
        }

        // TODO: Send start message.

        countdown();
    }

    private void countdown() {
        // Make sure the game isn't already in countdown.
        if(gameState == GameState.COUNTDOWN) {
            return;
        }

        gameState = GameState.COUNTDOWN;

        BukkitRunnable countdown = new  BukkitRunnable() {
            int counter = 4;
            public void run() {
                counter--;

                if(gameState == GameState.END) {
                    cancel();
                }

                if(counter  != 0) {
                    broadcast("&aStarting in " + counter + "...");
                    for (Player player : players()) {
                        player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound(), 1, 1);
                    }
                }
                else {
                    for(Player player : players()) {
                        player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound(), 1, 2);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> running(), 1);
                        cancel();
                    }
                }
            }
        };
        countdown.runTaskTimer(plugin, 0, 20);
    }

    /**
     * Marks the game as running.
     */
    private void running() {
        if(gameState == GameState.RUNNING) {
            return;
        }

        gameState = GameState.RUNNING;
        timer.start();

        // Spawn teams.
        int spawnCount = 0;
        List<Location> spawns = arena.spawns(world);


        for(Team team : this.teamManager.teams()) {
            // Loop back if we run out of spawns.
            if(spawnCount == spawns.size()) {
                spawnCount = 0;
            }

            // Spawn in each player in the team.
            for(Player player : team.players()) {
                player.teleport(spawns.get(spawnCount));
                player.closeInventory();
                player.setFireTicks(0);
            }

            spawnCount++;
        }
    }

    /**
     * Ends the game.
     * @param winner The winning team.
     */
    private void end(Team winner) {
        if(gameState == GameState.END) {
            return;
        }

        gameState = GameState.END;
        timer.stop();

        broadcast("&8&m+-----------------------***-----------------------+");
        broadcast(" ");
        broadcast(ChatUtils.centerText("&a&l" + kit.name() + " Duel &7- &f&l" + timer));
        broadcast(" ");
        if(winner.players().size() > 1) {
            broadcast(ChatUtils.centerText("&aWinners:"));
        }
        else {
            broadcast(ChatUtils.centerText("&aWinner:"));
        }

        for(Player player : winner.players()) {
            if(teamManager.team(player).deadPlayers().contains(player)) {
                broadcast(ChatUtils.centerText("&f" + player.getName() + " &a(&c0%&a)"));
            }
            else {
                broadcast(ChatUtils.centerText("&f" + player.getName() + " &a(" + GameUtils.getFormattedHealth(player) + "&a)"));
            }
        }
        broadcast(" ");
        broadcast("&8&m+-----------------------***-----------------------+");

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            players().forEach(player -> LobbyUtils.sendToLobby(plugin, player));
            spectators().forEach(player -> LobbyUtils.sendToLobby(plugin, player));

            for(Team team : teamManager.teams()) {
                team.players().clear();
                team.alivePlayers().clear();
                team.deadPlayers().clear();
            }


            spectators.clear();
            teamManager.teams().clear();

            plugin.gameManager().deleteGame(this);
        }, 100);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Add a player to the game.
     * @param player Player to add.
     */
    public void addPlayer(Player player) {
        System.out.println("Added " + player.getName() + " to game.");
        List<Player> members = new ArrayList<>();
        members.add(player);
        teamManager.createTeam(members);
    }

    /**
     * Add a player to the arena.
     * @param players Players to add.
     */
    public void addPlayers(List<Player> players) {
        teamManager.createTeam(players);
    }

    /**
     * Get the arena being used in the game.
     * @return Arena being used.
     */
    public Arena arena() {
        return arena;
    }

    /**
     * Add a spectator to the game.
     * @param spectator Spectator to add.
     */
    public void addSpectator(Player spectator) {
        // TODO: Spectators
    }

    /**
     * Broadcast a message to the arena.
     * @param message Message to broadcast.
     */
    public void broadcast(String message) {
        ChatUtils.broadcast(this.world, message);
    }

    /**
     * Get the current state of the game.
     * @return Game state.
     */
    public GameState gameState() {
        return gameState;
    }

    /**
     * Get the current game type.
     * @return Game Type of the game.
     */
    public GameType gameType() {
        return gameType;
    }

    /**
     * Get the kit used in the game.
     * @return Kit being used.
     */
    public Kit kit() {
        return kit;
    }

    /**
     * Get all players in the game.
     * Includes spectators.
     * @return Collection of all players.
     */
    public Collection<Player> players() {
        Collection<Player> players = new HashSet<>();

        // Add all players on teams.
        for(Team team : this.teamManager.teams()) {
            players.addAll(team.players());
        }

        return players;
    }

    /**
     * Get all current spectators.
     * @return All current spectators.
     */
    public Collection<Player> spectators() {
        return spectators;
    }

    /**
     * Get the game's Team Manager.
     * @return TeamManager.
     */
    public TeamManager teamManager() {
        return teamManager;
    }

    /**
     * Get the game timer.
     * @return Game timer.
     */
    public Timer timer() {
        return timer;
    }

    /**
     * Runs when a played disconnects.
     * @param player Player who disconnected.
     */
    public void playerDisconnect(Player player) {
        // TODO: Player disconnect logic.
    }

    /**
     * Runs when a player is killed.
     * @param player Player who was killed.
     */
    public void playerKilled(Player player) {
        if(spectators.contains(player)) {
            return;
        }

        player.getLocation().getWorld().strikeLightning(player.getLocation());
        addSpectator(player);
        teamManager.team(player).killPlayer(player);
        broadcast(teamManager.team(player).teamColor().chatColor()  + player.getName() + " &ahas died!");

        // Prevents stuff from breaking if the game is already over.
        if(gameState == GameState.END) {
            return;
        }

        for(Team team : teamManager.teams()) {
            if(team.alivePlayers().size() == 0) {
                teamManager.killTeam(team);

                if(teamManager.aliveTeams().size() == 1) {
                    Team winner = teamManager.aliveTeams().get(0);
                    end(winner);
                    break;
                }
            }
        }
    }

    /**
     * Runs when a player is killed.
     * @param player Player who was killed.
     * @param killer Player who killed the player.
     */
    public void playerKilled(Player player, Player killer) {
        if(spectators.contains(player)) {
            return;
        }

        player.getLocation().getWorld().strikeLightning(player.getLocation());
        addSpectator(player);
        teamManager.team(player).killPlayer(player);
        broadcast(teamManager.team(player).teamColor().chatColor()  + player.getName() + " &awas killed by " + teamManager.team(killer).teamColor().chatColor() + killer.getName() + " &a(" + GameUtils.getFormattedHealth(killer) + "&a)");

        // Prevents stuff from breaking if the game is already over.
        if(gameState == GameState.END) {
            return;
        }

        for(Team team : teamManager.teams()) {
            if(team.alivePlayers().size() == 0) {
                teamManager.killTeam(team);

                if(teamManager.aliveTeams().size() == 1) {
                    Team winner = teamManager.aliveTeams().get(0);
                    end(winner);
                    break;
                }
            }
        }
    }

    /**
     * Removes a player from the game.
     * @param player Player to remove.
     */
    public void removePlayer(Player player) {
        // Removes the player if they are a spectator.
        if(spectators.contains(player)) {
            removeSpectator(player);
            return;
        }

        // TODO: Send removed players to the lobby. LobbyUtils.sendToLobby(plugin, player);

        teamManager.team(player).killPlayer(player);
        player.getLocation().getWorld().strikeLightning(player.getLocation());

        for(Team team : teamManager.teams()) {
            if(team.alivePlayers().size() == 0) {
                teamManager.killTeam(team);

                if(teamManager.aliveTeams().size() == 1) {
                    Team winner = teamManager.aliveTeams().get(0);
                    end(winner);
                    break;
                }
            }
        }
    }

    /**
     * Remove a spectator from the game.
     * @param player Spectator to remove.
     */
    public void removeSpectator(Player player) {
        // TODO: remove spectator.
    }

    /**
     * Get the world the game is being played in.
     * @return Game world.
     */
    public World world() {
        return world;
    }
}
