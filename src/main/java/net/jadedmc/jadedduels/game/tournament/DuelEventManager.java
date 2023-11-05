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
package net.jadedmc.jadedduels.game.tournament;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.Game;
import net.jadedmc.jadedduels.game.GameType;
import net.jadedmc.jadedduels.game.arena.ArenaChunkGenerator;
import net.jadedmc.jadedduels.game.kit.Kit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages the current event and event settings.
 */
public class DuelEventManager {
    private final JadedDuelsPlugin plugin;
    private Player host;
    private EventType eventType;
    private DuelEvent activeEvent;
    private Kit kit;
    private EventStatus eventStatus;
    private BestOf bestOf;
    private final World tournamentWorld;

    /**
     * Creates the Duel Event Manager.
     * @param plugin Plugin instance.
     */
    public DuelEventManager(JadedDuelsPlugin plugin) {
        this.plugin = plugin;

        // Create the tournament world if it does not already exist.
        WorldCreator worldCreator = new WorldCreator("tournament");
        worldCreator.generator(new ArenaChunkGenerator());
        tournamentWorld = plugin.getServer().createWorld(worldCreator);

        // Reset the event data
        reset();
    }

    /**
     * Get the active event.
     * @return active event.
     */
    public DuelEvent activeEvent() {
        return activeEvent;
    }

    /**
     * Change the active event.
     * @param activeEvent new active event.
     */
    public void activeEvent(DuelEvent activeEvent) {
        this.activeEvent = activeEvent;
    }

    /**
     * Get the current best of.
     * @return Current best of.
     */
    public BestOf bestOf() {
        return bestOf;
    }

    /**
     * Change the best of.
     * @param bestOf new best of.
     */
    public void bestOf(BestOf bestOf) {
        this.bestOf = bestOf;
    }

    /**
     * Creates a new event using the existing settings.
     */
    public void create() {
        activeEvent = new DuelEvent(plugin);
    }

    /**
     * Get the current event status.
     * @return current event status.
     */
    public EventStatus eventStatus() {
        return eventStatus;
    }

    /**
     * Change the event status.
     * @param eventStatus new event status.
     */
    public void eventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    /**
     * Get the current event type.
     * @return current event type.
     */
    public EventType eventType() {
        return eventType;
    }

    /**
     * Change the event type.
     * @param eventType new event type.
     */
    public void eventType(EventType eventType) {
        this.eventType = eventType;
    }

    /**
     * Get the current host.
     * Is null if there is none.
     * @return current host.
     */
    public Player host() {
        return host;
    }

    /**
     * Change the host.
     * @param host new host.
     */
    public void host(Player host) {
        this.host = host;
    }

    /**
     * Get the current kit selected.
     * Is null if not set.
     * @return current kit selected.
     */
    public Kit kit() {
        return kit;
    }

    /**
     * Change the current kit.
     * @param kit new kit.
     */
    public void kit(Kit kit) {
        this.kit = kit;
    }

    /**
     * Get all tournament players.
     * Both in the tournament world, and in tournament games.
     * @return All tournament players.
     */
    public Collection<Player> players() {
        Set<Player> players = new HashSet<>();
        players.addAll(tournamentWorld.getPlayers());

        for(Game game : plugin.gameManager().activeGames()) {
            if(game.gameType() == GameType.TOURNAMENT) {
                players.addAll(game.players());
                players.addAll(game.spectators());
            }
        }

        return players;
    }

    /**
     * Resets an event. Used if an event is canceled or ended.
     */
    public void reset() {
        activeEvent = null;
        eventType = EventType.NONE;
        eventStatus = EventStatus.NONE;
        host = null;
        kit = null;
    }

    /**
     * Get the tournament world.
     * @return Tournament world.
     */
    public World world() {
        return tournamentWorld;
    }
}