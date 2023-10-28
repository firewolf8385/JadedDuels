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

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.tournament.EventStatus;
import net.jadedmc.jadedduels.game.tournament.EventType;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.command.CommandSender;

public class CancelCMD extends AbstractCommand {
    private JadedDuelsPlugin plugin;

    public CancelCMD(JadedDuelsPlugin plugin) {
        super("cancel", "tournament.use", true);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        switch (plugin.duelEventManager().eventStatus()) {
            case NONE -> ChatUtils.chat(sender, "&c&lError &8» &cThere is no tournament to cancel!");
            case RUNNING -> ChatUtils.chat(sender, "&c&lError &8» &cThe tournament is already running!");
            case WAITING -> {
                plugin.duelEventManager().eventStatus(EventStatus.NONE);
                plugin.duelEventManager().eventType(EventType.NONE);
                plugin.duelEventManager().host(null);
                plugin.duelEventManager().kit(null);
                ChatUtils.broadcast(plugin.duelEventManager().world(), "&a&lTournament &8» &aThe tournament has been canceled.");

                // TODO: Update bungeecord.
            }
        }
    }
}