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
package net.jadedmc.jadedduels.game.duels;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.GameType;
import net.jadedmc.jadedduels.game.arena.Arena;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Manages the creation of duel requests and duel games.
 */
public class DuelManager {
    private final JadedDuelsPlugin plugin;
    private final Set<DuelRequest> duelRequests = new HashSet<>();

    /**
     * Creates the Duel manager.
     * @param plugin Instance of the plugin.
     */
    public DuelManager(JadedDuelsPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when a player accepts a duel request.
     * @param sender The sender of the duel request.
     * @param receiver The receiver of the duel request.
     */
    public void acceptDuelRequest(Player sender, Player receiver) {
        // Exit if the duel request does not exist anymore.
        if(!hasDuelRequest(sender, receiver)) {
            return;
        }

        DuelRequest duelRequest = duelRequest(sender);
        this.duelRequests.remove(duelRequest);

        plugin.gameManager().createGame(duelRequest.arena(), duelRequest.kit(), GameType.DUEL, duelRequest.pointsNeeded(), List.of(sender.getUniqueId()), List.of(receiver.getUniqueId()));
    }

    /**
     * Add a duel request to the storage.
     * @param player Player sending the duel request.
     * @param receiver Player receiving the duel request.
     * @param kit Kit the duel is for.
     */
    public void addRequest(Player player, Player receiver, Kit kit, int pointsNeeded) {
        // Finds an arena
        List<Arena> arenas = new ArrayList<>(plugin.arenaManager().getArenas(kit, GameType.DUEL));
        Collections.shuffle(arenas);

        // Saves the duel request.
        DuelRequest duelRequest = new DuelRequest(player, receiver, kit, arenas.get(0), pointsNeeded);
        duelRequests.add(duelRequest);

        // Makes it expire after 1 minute.
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if(hasDuelRequest(player, receiver)) {
                ChatUtils.chat(player, "<green>Your duel request to <white>" + receiver.getName() + " <green>has expired.");
            }

            duelRequests.remove(duelRequest);
        }, 1200);

        // Sends a message to the sender.
        ChatUtils.chat(player, "&aDuel request sent to &f" + receiver.getName() + " &ain &f" + kit.name() + "&a.");

        // Sends a message to the target.
        ChatUtils.chat(receiver, "&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        ChatUtils.chat(receiver, ChatUtils.centerText("&a&lDuel Request"));
        ChatUtils.chat(receiver, "");
        ChatUtils.chat(receiver, ChatUtils.centerText("&f" + player.getName() + " &7wants to duel you in &f" + kit.name() + "&7!"));
        ChatUtils.chat(receiver, "");
        ChatUtils.chat(receiver, "  <dark_gray>» <click:run_command:'/duel accept " + player.getName() + "'><hover:show_text:'<green>Click to accept'><green>/duel accept " + player.getName() + "</hover></click>");
        ChatUtils.chat(receiver, "  <dark_gray>» <click:run_command:'/duel deny " + player.getName() + "'><hover:show_text:'<red>Click to deny'><red>/duel deny " + player.getName() + "</hover></click>");
        ChatUtils.chat(receiver, "");
        ChatUtils.chat(receiver, "&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }

    /**
     * Gets the player's current duel request.
     * @param player Player to get duel request of.
     * @return Player's duel request.
     */
    public DuelRequest duelRequest(Player player) {
        for(DuelRequest duelRequest : duelRequests) {
            if(duelRequest.sender().equals(player) || duelRequest.receiver().equals(player)) {
                return duelRequest;
            }
        }

        return null;
    }

    /**
     * Checks if a duel request exists.
     * @param sender Sender of the duel request.
     * @param receiver Receiver of the duel request.
     * @return Whether the request exists.
     */
    public boolean hasDuelRequest(Player sender, Player receiver) {
        for(DuelRequest duelRequest : duelRequests) {
            if(duelRequest.sender().equals(sender) && duelRequest.receiver().equals(receiver)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get if a player has an active duel request.
     * @param player Player to check.
     * @return Whether they have a duel request active already.
     */
    public boolean hasDuelRequest(Player player) {
        for(DuelRequest duelRequest : duelRequests) {
            if(duelRequest.sender().equals(player)) {
                return true;
            }
        }

        return false;
    }
}