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
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.arena.Arena;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedduels.game.lobby.LobbyUtils;
import net.jadedmc.jadedduels.game.team.Team;
import net.jadedmc.jadedduels.game.team.TeamManager;
import net.jadedmc.jadedduels.game.tournament.team.EventTeam;
import net.jadedmc.jadedduels.utils.GameUtils;
import net.jadedmc.jadedduels.utils.Timer;
import net.jadedmc.jadedutils.chat.ChatUtils;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
    private Timer timer;
    private final TeamManager teamManager;

    private GameState gameState;
    private final Collection<Player> spectators = new HashSet<>();
    private final Match match;
    private int round = 0;
    private int pointsNeeded;
    private final Collection<Block> blocks = new HashSet<>();


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
        this.match = null;
        this.pointsNeeded = 1;

        plugin.gameManager().addGame(this);

        // World Game Rules.
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.DO_TILE_DROPS, false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_MOB_LOOT, false);
            world.setGameRule(GameRule.KEEP_INVENTORY, true);

            world.setDifficulty(Difficulty.HARD);
        });
    }

    public Game(final JadedDuelsPlugin plugin, final Kit kit, final GameType gameType, final Arena arena, final World world, final UUID uuid, final Match match) {
        this.plugin = plugin;
        this.kit = kit;
        this.gameType = gameType;
        this.arena = arena;
        this.world = world;
        this.uuid = uuid;

        this.timer = new Timer(plugin);
        this.teamManager = new TeamManager(plugin);
        this.match = match;
        this.pointsNeeded = plugin.duelEventManager().bestOf().neededWins();

        plugin.gameManager().addGame(this);

        // World Game Rules.
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.DO_TILE_DROPS, false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_MOB_LOOT, false);
            world.setGameRule(GameRule.KEEP_INVENTORY, true);

            world.setDifficulty(Difficulty.HARD);
        });
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void startGame() {
        if(gameType == GameType.UNRANKED) {
            plugin.queueManager().addPlaying(kit, players().size());
        }

        for(Player player : players()) {
            List<Player> opponents = new ArrayList<>();

            Team team = this.teamManager.team(player);
            for(Team opposingTeam : this.teamManager.teams()) {
                if(team.equals(opposingTeam)) {
                    continue;
                }

                opponents.addAll(opposingTeam.players());
            }

            ChatUtils.chat(player, "&8&m+-----------------------***-----------------------+");
            ChatUtils.chat(player, ChatUtils.centerText("&a&l" + kit.name() + " Duel"));
            ChatUtils.chat(player, "");

            // Display the opponents label, based on the number.
            if(opponents.size() == 1) {
                ChatUtils.chat(player, ChatUtils.centerText("&aOpponent:"));
            }
            else {
                ChatUtils.chat(player, ChatUtils.centerText("&aOpponents:"));
            }

            // Lists the opponents.
            for(Player opponent : opponents) {
                ChatUtils.chat(player, ChatUtils.centerText(opponent.getName()));
            }

            ChatUtils.chat(player, "");
            ChatUtils.chat(player, "&8&m+-----------------------***-----------------------+");
        }

        startRound();
    }

    private void startRound() {
        round++;
        teamManager.reset();

        // Remove old entities at the start of each round.
        for(Entity entity : world.getEntities()) {
            if(entity instanceof Player) {
                continue;
            }

            entity.remove();
        }

        for(Player player : players()) {
            spectators.remove(player);
            player.setCollidable(true);
            player.setArrowsInBody(0);
        }

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

        // Show invisible players for round reset.
        for(Player player : players()) {
            for(Player other : players()) {
                if(player.equals(other)) {
                    continue;
                }

                player.showPlayer(plugin, other);
            }

            // Show each player to each spectator.
            for(Player spectator : spectators()) {
                spectator.showPlayer(plugin, player);
            }
        }

        roundCountdown();
    }

    private void roundCountdown() {
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
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> runRound(), 1);
                        cancel();
                    }
                }
            }
        };
        countdown.runTaskTimer(plugin, 0, 20);
    }

    private void runRound() {
        if(gameState == GameState.RUNNING) {
            return;
        }

        gameState = GameState.RUNNING;
        timer = new Timer(plugin);
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

    public void endRound(Team winner) {
        if(gameState == GameState.END) {
            return;
        }

        winner.addPoint();
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

        if(gameType == GameType.TOURNAMENT) {
            Team loser = winner;
            for(Team team : teamManager.teams()) {
                if(team.equals(winner)) {
                    continue;
                }

                loser = team;
            }

            broadcast("");
            broadcast(ChatUtils.centerText("&aScore: &f" + winner.score() + " - " + loser.score()));
        }

        broadcast(" ");
        broadcast("&8&m+-----------------------***-----------------------+");

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

            if(winner.score() < pointsNeeded) {
                startRound();
            }
            else {
                if(gameType == GameType.UNRANKED) {
                    plugin.queueManager().removePlaying(kit, players().size());
                }

                // Show hidden players
                for(Player player : world.getPlayers()) {
                    player.spigot().getHiddenPlayers().forEach(hidden -> player.showPlayer(plugin, hidden));
                }

                // Runs tournament specific code.
                if(gameType == GameType.TOURNAMENT) {
                    Team loser = winner;
                    for(Team team : teamManager.teams()) {
                        if(team.equals(winner)) {
                            continue;
                        }

                        loser = team;
                    }

                    plugin.duelEventManager().activeEvent().addResults(match, winner, loser);
                    plugin.duelEventManager().activeEvent().broadcast("&a&lTournament &8» &f" + winner.eventTeam().name() + " &ahas defeated &f" + loser.eventTeam().name() + " &7(&f" + winner.score() + " &7-&f " + loser.score() + "&7)&a.");

                    // Replace this with tournament lobby stuff.
                    players().forEach(player -> LobbyUtils.sendToTournamentLobby(plugin, player));
                    spectators().forEach(player -> LobbyUtils.sendToTournamentLobby(plugin, player));
                }
                else {
                    players().forEach(player -> LobbyUtils.sendToLobby(plugin, player));
                    spectators().forEach(player -> LobbyUtils.sendToLobby(plugin, player));
                }

                for(Team team : teamManager.teams()) {
                    team.players().clear();
                    team.alivePlayers().clear();
                    team.deadPlayers().clear();
                }


                spectators.clear();
                teamManager.teams().clear();

                plugin.gameManager().deleteGame(this);
            }
        }, 100);
    }

    /**
     * Ends the game.
     * @param winner The winning team.
     */
    private void end(Team winner) {
        if(gameState == GameState.END) {
            return;
        }

        winner.addPoint();
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

            plugin.queueManager().removePlaying(kit, players().size());

            // Show hidden players
            for(Player player : world.getPlayers()) {
                player.spigot().getHiddenPlayers().forEach(hidden -> player.showPlayer(plugin, hidden));
            }

            // Runs tournament specific code.
            if(gameType == GameType.TOURNAMENT) {
                Team loser = winner;
                for(Team team : teamManager.teams()) {
                    if(team.equals(winner)) {
                        continue;
                    }

                    loser = team;
                }

                plugin.duelEventManager().activeEvent().addResults(match, winner, loser);
                plugin.duelEventManager().activeEvent().broadcast("&a&lTournament &8» &f" + winner.eventTeam().name() + " &ahas defeated &f" + loser.eventTeam().name() + " &7(&f" + winner.score() + " &7-&f " + loser.score() + "&7)&a.");

                // Replace this with tournament lobby stuff.
                players().forEach(player -> LobbyUtils.sendToLobby(plugin, player));
                spectators().forEach(player -> LobbyUtils.sendToLobby(plugin, player));
            }
            else {
                players().forEach(player -> LobbyUtils.sendToLobby(plugin, player));
                spectators().forEach(player -> LobbyUtils.sendToLobby(plugin, player));
            }

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

    public void addBlock(Block block) {
        blocks.add(block);
    }

    /**
     * Add a player to the game.
     * @param player Player to add.
     */
    public void addPlayer(Player player) {
        List<Player> members = new ArrayList<>();
        members.add(player);
        teamManager.createTeam(members);

        // Update player's chat channel.
        if(JadedChat.getChannel(player).isDefaultChannel()) {
            if(gameType == GameType.TOURNAMENT) {
                JadedChat.setChannel(player, JadedChat.getChannel("TOURNAMENT"));
            }
            else {
                JadedChat.setChannel(player, JadedChat.getChannel("GAME"));
            }
        }
    }

    /**
     * Add a player to the arena.
     * @param players Players to add.
     */
    public void addPlayers(List<Player> players) {
        teamManager.createTeam(players);
    }

    public void addPlayers(EventTeam team) {
        teamManager.createTeam(team);

        for(Player player : team.players()) {

            // Update player's chat channel.
            if(JadedChat.getChannel(player).isDefaultChannel()) {
                if(gameType == GameType.TOURNAMENT) {
                    JadedChat.setChannel(player, JadedChat.getChannel("TOURNAMENT"));
                }
                else {
                    JadedChat.setChannel(player, JadedChat.getChannel("GAME"));
                }
            }
        }
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
        spectators.add(spectator);

        // Doesn't teleport player if they were in the game before.
        if(teamManager.team(spectator) == null) {
            if(gameType != GameType.TOURNAMENT) {
                spectator.teleport(arena.spectatorSpawn(this.world));
            }
            else {
                spectator.teleport(arena.tournamentSpawn(this.world));
            }
        }

        if(gameType != GameType.TOURNAMENT) {
            spectator.getInventory().clear();
            spectator.getInventory().setArmorContents(null);
            spectator.setAllowFlight(true);
            spectator.setFlying(true);
            spectator.setMaxHealth(20.0);
            spectator.setHealth(20.0);
            spectator.setFoodLevel(20);
            spectator.setGameMode(GameMode.ADVENTURE);

            // Prevents player from interfering.
            spectator.setCollidable(false);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for(Player pl : world.getPlayers()) {
                    pl.hidePlayer(plugin, spectator);

                    if(spectators.contains(pl)) {
                        spectator.hidePlayer(plugin, pl);
                    }
                }
            }, 2);
        }
        else {
            spectator.getInventory().clear();
            spectator.getInventory().setArmorContents(null);
            spectator.setMaxHealth(20.0);
            spectator.setHealth(20.0);
            spectator.setFoodLevel(20);
            spectator.setGameMode(GameMode.ADVENTURE);

            if(teamManager.team(spectator) != null) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for(Player pl : world.getPlayers()) {
                        pl.hidePlayer(plugin, spectator);

                        if(spectators.contains(pl)) {
                            spectator.hidePlayer(plugin, pl);
                        }
                    }
                }, 2);
            }
        }


        ItemStack leave = new ItemBuilder(XMaterial.RED_BED)
                .setDisplayName("<red>Leave Match")
                .build();
        spectator.getInventory().setItem(8, leave);

        // Update player's chat channel.
        if(JadedChat.getChannel(spectator).isDefaultChannel()) {
            if(gameType == GameType.TOURNAMENT) {
                JadedChat.setChannel(spectator, JadedChat.getChannel("TOURNAMENT"));
            }
            else {
                JadedChat.setChannel(spectator, JadedChat.getChannel("GAME"));
            }
        }
    }

    public Collection<Block> blocks() {
        return blocks;
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
        if(spectators.contains(player)) {
            removeSpectator(player);
            return;
        }

        plugin.queueManager().removePlaying(kit, 1);

        broadcast(teamManager.team(player).teamColor().chatColor() + player.getName() + " disconnected.");
        teamManager.team(player).killPlayer(player);
        player.getLocation().getWorld().strikeLightningEffect(player.getLocation());

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

        for(Player spectator : spectators) {
            // TODO: Spectators player.showPlayer(spectator);
        }
    }

    /**
     * Runs when a player is killed.
     * @param player Player who was killed.
     */
    public void playerKilled(Player player) {
        if(spectators.contains(player)) {
            return;
        }

        player.getLocation().getWorld().strikeLightningEffect(player.getLocation());
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
                    endRound(winner);
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

        player.getLocation().getWorld().strikeLightningEffect(player.getLocation());
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
                    endRound(winner);
                    break;
                }
            }
        }
    }

    public void removeBlock(Block block) {
        blocks.remove(block);
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

        teamManager.team(player).removePlayer(player);

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
        spectators.remove(player);

        for(Player pl : world.getPlayers()) {
            pl.showPlayer(plugin, player);
        }

        if(gameType == GameType.TOURNAMENT) {
            LobbyUtils.sendToTournamentLobby(plugin, player);
        }
        else {
            LobbyUtils.sendToLobby(plugin, player);
        }
    }

    /**
     * Get the world the game is being played in.
     * @return Game world.
     */
    public World world() {
        return world;
    }
}
