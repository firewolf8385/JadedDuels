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

import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.features.channels.events.ChannelSwitchEvent;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChannelSwitchListener implements Listener {
    private final JadedDuelsPlugin plugin;

    public ChannelSwitchListener(JadedDuelsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSwitch(ChannelSwitchEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.gameManager().game(player);

        if(game == null) {
            if(event.getToChannel().name().equalsIgnoreCase("GAME") || event.getToChannel().name().equalsIgnoreCase("TEAM")) {
                if(event.getPlayer().getWorld().equals(plugin.duelEventManager().world())) {
                    event.setToChannel(JadedChat.getChannel("TOURNAMENT"));
                }
                else {
                    event.setToChannel(JadedChat.getDefaultChannel());
                }
            }
        }
        else {
            if(event.getToChannel().equals(JadedChat.getDefaultChannel())) {
                if(event.getPlayer().getWorld().equals(plugin.duelEventManager().world())) {
                    event.setToChannel(JadedChat.getChannel("TOURNAMENT"));
                }
                else {
                    event.setToChannel(JadedChat.getChannel("GAME"));
                }
            }
        }
    }
}