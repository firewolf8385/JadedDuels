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
package net.jadedmc.jadedduels.game.queue;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.GameType;
import net.jadedmc.jadedduels.game.arena.Arena;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedduels.utils.Timer;
import net.jadedmc.jadedutils.chat.ChatUtils;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Manages the queue for each kit.
 */
public class QueueManager {
    private final JadedDuelsPlugin plugin;
    private final Map<Player, Kit> unrankedQueue = new HashMap<>();
    private final Map<Player, Timer> timers = new HashMap<>();
    private final Map<Kit, Integer> playingKit = new HashMap<>();

    private int playing;

    public QueueManager(JadedDuelsPlugin plugin) {
        this.plugin = plugin;
        playing = 0;

        for(Kit kit : plugin.kitManager().kits()) {
            playingKit.put(kit, 0);
        }
    }

    /**
     * Add a player to the queue.
     * @param player Player ot add to the queue.
     * @param kit Kit to add them to.
     * @param gameType GameType to add them to.
     */
    public void addPlayer(Player player, Kit kit, GameType gameType) {
        if(unrankedQueue.containsKey(player)) {
            ChatUtils.chat(player, "&cError &8» &cYou are already in queue!");
            return;
        }

        Player other = getPlayer(kit, gameType);

        // If no one else is queueing that kit and GameType.
        if(other == null) {
            getQueue(gameType).put(player, kit);

            player.getInventory().clear();
            ItemStack leave = new ItemBuilder(Material.REDSTONE)
                    .setDisplayName("&cLeave Queue")
                    .build();
            player.getInventory().setItem(8, leave);

            Timer timer = new Timer(plugin);
            timer.start();
            timers.put(player, timer);
            new QueueScoreboard(plugin, player, kit, timer, gameType);
        }
        // If someone else is queueing that kit and GameType.
        else {
            removePlayer(other);

            List<Arena> arenas = new ArrayList<>(plugin.arenaManager().getArenas(kit));
            Collections.shuffle(arenas);
            plugin.gameManager().createGame(arenas.get(0), kit, gameType).thenAccept(game -> {
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    game.addPlayer(player);
                    game.addPlayer(other);
                    game.start();
                });
            });
        }
    }

    /**
     * Add to the playing counter.
     * @param playing Amount to add.
     */
    public void addPlaying(int playing) {
        this.playing += playing;
    }

    public void addPlaying(Kit kit, int playing) {
        this.playingKit.put(kit, this.playingKit.get(kit) + playing);
    }

    /**
     * Get a player in a specific queue.
     * @param kit Kit of the queue.
     * @param gameType GameType of the queue
     * @return Player in it. Null if none.
     */
    public Player getPlayer(Kit kit, GameType gameType) {
        Map<Player, Kit> queue = getQueue(gameType);

        for(Player player : queue.keySet()) {
            if(queue.get(player).equals(kit)) {
                return player;
            }
        }

        return null;
    }

    /**
     * get the number of players currently playing.
     * @return Amount of players playing.
     */
    public int getPlaying() {
        return playing;
    }

    public int getPlaying(Kit kit) {
        return playingKit.get(kit);
    }

    /**
     * Get the queue HashMap of a specific GameType
     * @param gameType GameType to get queue of.
     * @return Queue
     */
    public Map<Player, Kit> getQueue(GameType gameType) {
        if(gameType == GameType.UNRANKED) {
            return unrankedQueue;
        }

        return null;
    }

    /**
     * Get the amount of people queuing a specific kit.
     * @param kit Kit they could be queueing.
     * @param gameType GameType they could be queueing.
     * @return Amount of people queueing.
     */
    public int getQueueing(Kit kit, GameType gameType) {
        if(getQueue(gameType).containsValue(kit)) {
            return 1;
        }
        else {
            return 0;
        }
    }

    /**
     * Get the amount of players currently queueing.
     * @return Number of players queueing.
     */
    public int getQueueing() {
        return unrankedQueue.size();
    }

    /**
     * Remove a player from the queue.
     * @param player Player to remove.
     */
    public void removePlayer(Player player) {
        unrankedQueue.remove(player);

        Timer timer = timers.get(player);

        if(timer != null) {
            timer.stop();
            timers.remove(player);
        }
    }

    /**
     * Remove from the playing count.
     * @param playing Amount to remove.
     */
    public void removePlaying(int playing) {
        this.playing -= playing;
    }

    public void removePlaying(Kit kit, int playing) {
        this.playingKit.put(kit, this.playingKit.get(kit) - playing);
    }
}