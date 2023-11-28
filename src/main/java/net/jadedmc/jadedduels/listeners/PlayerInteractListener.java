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

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.Game;
import net.jadedmc.jadedduels.game.GameState;
import net.jadedmc.jadedduels.game.GameType;
import net.jadedmc.jadedduels.game.lobby.LobbyScoreboard;
import net.jadedmc.jadedduels.game.lobby.LobbyUtils;
import net.jadedmc.jadedduels.game.tournament.EventStatus;
import net.jadedmc.jadedduels.gui.KitGUI;
import net.jadedmc.jadedduels.gui.SpectateGUI;
import net.jadedmc.jadedduels.utils.LocationUtils;
import net.jadedmc.jadedutils.chat.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerInteractListener implements Listener {
    private final JadedDuelsPlugin plugin;

    public PlayerInteractListener(JadedDuelsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getAction() == Action.PHYSICAL && player.getWorld().equals(plugin.duelEventManager().world())) {
            Location boatSpawn = LocationUtils.fromConfig(plugin.settingsManager().getConfig().getConfigurationSection("BoatRace.spawn"));
            Boat boat = (Boat) player.getWorld().spawnEntity(boatSpawn, EntityType.BOAT);
            boat.addPassenger(player);
            return;
        }

        Game game = plugin.gameManager().game(player);

        if(game != null) {
            if(game.gameState() != GameState.RUNNING) {
                event.setCancelled(true);
                // Fixes visual glitch with throwables during countdown.
                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), player.getItemInHand());
                return;
            }
            else {
                game.kit().onPlayerInteract(game, event);
            }
        }

        // Prevent using items during game countdown.
        if(game != null && game.gameState() != GameState.RUNNING) {
            event.setCancelled(true);
        }

        // Exit if the item is null.
        if(event.getItem() == null) {
            return;
        }

        // Exit if item meta is null.
        if(event.getItem().getItemMeta() == null) {
            return;
        }

        String item = ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName());

        if(item == null) {
            return;
        }

        if(event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if(game != null) {
            game.kit().onNamedItemClick(game, player, item);
        }

        switch (item) {
            case "Kits" -> {
                new KitGUI(plugin).open(player);
                event.setCancelled(true);
            }

            case "Leave Queue" -> {
                plugin.queueManager().removePlayer(player);
                LobbyUtils.giveLobbyItems(player);
                new LobbyScoreboard(plugin, player);
                event.setCancelled(true);
            }

            case "Leave Match" -> {
                if(game != null && game.spectators().contains(player)) {
                    game.removeSpectator(player);
                }
                event.setCancelled(true);
            }

            case "Spectate" -> {
                if(player.getWorld().equals(plugin.duelEventManager().world())) {
                    new SpectateGUI(plugin, GameType.TOURNAMENT).open(player);
                    event.setCancelled(true);
                    return;
                }

                if(game == null) {
                    new SpectateGUI(plugin, GameType.UNRANKED).open(player);
                    event.setCancelled(true);
                    return;
                }

                if(game.gameType() == GameType.TOURNAMENT) {
                    new SpectateGUI(plugin, GameType.TOURNAMENT).open(player);
                    event.setCancelled(true);
                    return;
                }

                new SpectateGUI(plugin, GameType.UNRANKED).open(player);
                event.setCancelled(true);
            }

            case "Host" -> {
                plugin.getServer().dispatchCommand(player, "create");
                event.setCancelled(true);
            }

            case "Back to Duels" -> {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    // Cancel tournament if the player is the host.
                    if(plugin.duelEventManager().eventStatus() == EventStatus.WAITING
                            && plugin.duelEventManager().host().equals(player)) {
                        plugin.getServer().dispatchCommand(player, "cancel");
                    }

                    LobbyUtils.sendToLobby(plugin, player);
                }, 1);
                event.setCancelled(true);
            }

            case "Settings" -> {
                ChatUtils.chat(player, "&cComing soon.");
                event.setCancelled(true);
            }

            case "Visit Tournament Lobby" -> {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    LobbyUtils.sendToTournamentLobby(plugin, player);

                    // Add warning if the tournament is already running.
                    if(plugin.duelEventManager().eventStatus() == EventStatus.RUNNING) {
                        ChatUtils.chat(player, "&cUnfortunately the tournament has already started. You're still able to spectate though, using the spectate item in your inventory.");
                    }
                }, 1);

                event.setCancelled(true);
            }
        }

    }
}