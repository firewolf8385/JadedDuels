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
package net.jadedmc.jadedduels.game.lobby;

import com.cryptomorin.xseries.XMaterial;
import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedcore.features.items.CustomItem;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.tournament.TournamentScoreboard;
import net.jadedmc.jadedduels.utils.LocationUtils;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

/**
 * A collection of repeated tasks used related to the lobby.
 */
public class LobbyUtils {

    /**
     * Sends a player to the lobby.
     * @param plugin Instance of the plugin.
     * @param player Player to send to the lobby.
     */
    public static void sendToLobby(final JadedDuelsPlugin plugin, Player player) {
        player.teleport(LocationUtils.getSpawn(plugin));
        player.setGameMode(GameMode.ADVENTURE);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setFireTicks(0);

        player.setAllowFlight(false);
        player.setFlying(false);
        player.setCollidable(true);

        player.setArrowsInBody(0);

        player.setExp(0);
        player.setLevel(0);

        // Remove potion effects.
        for(PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        new LobbyScoreboard(plugin, player).update(player);

        // Update player's chat channel.
        if(JadedChat.getChannel(player).equals(JadedChat.getChannel("GAME")) || JadedChat.getChannel(player).equals(JadedChat.getChannel("TEAM")) || JadedChat.getChannel(player).equals(JadedChat.getChannel("TOURNAMENT"))) {
            JadedChat.setChannel(player, JadedChat.getDefaultChannel());
        }

        giveLobbyItems(player);
    }

    /**
     * Give lobby items to the player.
     * @param player Player to give lobby items to.
     */
    public static void giveLobbyItems(Player player) {
        player.getInventory().clear();

        player.getInventory().setItem(0, CustomItem.GAME_SELECTOR.toItemStack());
        //player.getInventory().setItem(2, new ItemBuilder(Material.EMERALD).setDisplayName("&a&lShop").build());
        player.getInventory().setItem(3, new ItemBuilder(Material.ENDER_EYE).setDisplayName("<green><bold>Spectate").build());
        player.getInventory().setItem(4, new ItemBuilder(Material.NETHER_STAR).setDisplayName("<green><bold>Kits").build());
        //player.getInventory().setItem(7, new ItemBuilder(Material.PAPER).setDisplayName("&a&lStats").build());
        player.getInventory().setItem(8, new ItemBuilder(Material.GOLD_INGOT).setDisplayName("<green><bold>Visit Tournament Lobby").build());
    }

    public static void sendToTournamentLobby(final JadedDuelsPlugin plugin, Player player) {
        player.teleport(LocationUtils.getTournamentSpawn(plugin));
        player.setGameMode(GameMode.ADVENTURE);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setFireTicks(0);

        player.setAllowFlight(false);
        player.setFlying(false);
        player.setCollidable(true);

        player.setArrowsInBody(0);

        player.setExp(0);
        player.setLevel(0);

        // Remove potion effects.
        for(PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        new TournamentScoreboard(plugin, player).update(player);

        // Update player's chat channel.
        if(JadedChat.getChannel(player).equals(JadedChat.getChannel("GAME")) || JadedChat.getChannel(player).equals(JadedChat.getChannel("TEAM")) || JadedChat.getChannel(player).equals(JadedChat.getDefaultChannel())) {
            JadedChat.setChannel(player, JadedChat.getChannel("TOURNAMENT"));
        }

        giveTournamentItems(player);
    }

    public static void giveTournamentItems(Player player) {
        player.getInventory().clear();

        player.getInventory().setItem(1, new ItemBuilder(Material.ENDER_EYE).setDisplayName("<green><bold>Spectate").build());
        player.getInventory().setItem(7, new ItemBuilder(Material.COMPARATOR).setDisplayName("<green><bold>Settings").build());
        player.getInventory().setItem(8, new ItemBuilder(XMaterial.RED_DYE).setDisplayName("<green><bold>Back to Duels").build());

        if(player.hasPermission("tournament.host")) {
            player.getInventory().setItem(4, new ItemBuilder(Material.NETHER_STAR).setDisplayName("<green><bold>Host").build());
        }
    }
}