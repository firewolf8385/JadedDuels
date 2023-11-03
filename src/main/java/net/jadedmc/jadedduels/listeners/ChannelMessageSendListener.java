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
package net.jadedmc.jadedduels.listeners;

import net.jadedmc.jadedchat.features.channels.events.ChannelMessageSendEvent;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.Game;
import net.jadedmc.jadedduels.game.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ChannelMessageSendListener implements Listener {
    private final JadedDuelsPlugin plugin;

    public ChannelMessageSendListener(JadedDuelsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMessage(ChannelMessageSendEvent event) {

        switch (event.getChannel().name().toUpperCase()) {
            case "GAME" -> {
                gameChannel(event);
            }

            case "TEAM" -> {
                teamChannel(event);
            }
        }
    }

    private void gameChannel(ChannelMessageSendEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.gameManager().game(player);

        if(game == null) {
            event.setCancelled(true);
            return;
        }

        List<Player> viewers = new ArrayList<>(game.players());
        viewers.addAll(game.spectators());

        // Remove duplicates.
        Set<Player> set = new LinkedHashSet<>(viewers);
        viewers.clear();
        viewers.addAll(set);

        event.setViewers(viewers);
    }

    private void teamChannel(ChannelMessageSendEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.gameManager().game(player);

        if(game == null) {
            event.setCancelled(true);
            return;
        }

        Team team = game.teamManager().team(player);
        if(team == null) {
            event.setCancelled(true);
            return;
        }

        List<Player> viewers = new ArrayList<>(team.players());

        event.setViewers(viewers);
    }
}