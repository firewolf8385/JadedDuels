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
package net.jadedmc.jadedduels.commands;

import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.Game;
import net.jadedmc.jadedduels.game.lobby.LobbyUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Runs the /leave command, which causes the player to leave whatever they are in.
 * Works for:
 *   - Games
 *   - Tournament lobby
 *   - Duels lobby
 */
public class LeaveCMD extends AbstractCommand {
    private final JadedDuelsPlugin plugin;

    /**
     * Creates the command.
     * @param plugin Instance of the plugin.
     */
    public LeaveCMD(final JadedDuelsPlugin plugin) {
        super("leave", "", false);
        this.plugin = plugin;
    }

    /**
     * Runs when the command is executed.
     * @param sender The Command Sender.
     * @param args Arguments of the command.
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Game game = plugin.gameManager().game(player);

        // Remove the player from their current game if they are in one.
        if(game != null) {
            game.removePlayer(player);
            return;
        }

        // Move the player to the duels lobby if they are in the tournament lobby.
        if(player.getWorld().equals(plugin.duelEventManager().world())) {
            LobbyUtils.sendToLobby(plugin, player);
            return;
        }

        // Sends the player to the lobby.
        JadedAPI.sendBungeecordMessage(player, "BungeeCord", "Connect", "lobby");
    }
}