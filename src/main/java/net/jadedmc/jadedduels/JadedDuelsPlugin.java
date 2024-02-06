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
package net.jadedmc.jadedduels;

import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.features.channels.channel.ChatChannel;
import net.jadedmc.jadedchat.features.channels.channel.ChatChannelBuilder;
import net.jadedmc.jadedchat.features.channels.fomat.ChatFormatBuilder;
import net.jadedmc.jadedduels.commands.AbstractCommand;
import net.jadedmc.jadedduels.game.GameManager;
import net.jadedmc.jadedduels.game.arena.ArenaManager;
import net.jadedmc.jadedduels.game.duel.DuelManager;
import net.jadedmc.jadedduels.game.kit.KitManager;
import net.jadedmc.jadedduels.game.tournament.DuelEventManager;
import net.jadedmc.jadedduels.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class JadedDuelsPlugin extends JavaPlugin {
    private ArenaManager arenaManager;
    private DuelManager duelManager;
    private GameManager gameManager;
    private KitManager kitManager;
    private SettingsManager settingsManager;
    private DuelEventManager duelEventManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        settingsManager = new SettingsManager(this);
        kitManager = new KitManager(this);

        // Load arenas.
        arenaManager = new ArenaManager(this);
        arenaManager.loadArenas();

        gameManager = new GameManager(this);
        duelEventManager = new DuelEventManager(this);
        duelManager = new DuelManager(this);

        AbstractCommand.registerCommands(this);

        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockExplodeListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockFormListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockFromToListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new ChannelMessageSendListener(this), this);
        getServer().getPluginManager().registerEvents(new ChannelSwitchListener(this), this);
        getServer().getPluginManager().registerEvents(new EntitySpawnListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityExplodeListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityRegainHealthListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChangeListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItemListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerEggThrowListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerToggleFlightListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerToggleSneakListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileLaunchListener(this), this);
        getServer().getPluginManager().registerEvents(new TNTPrimeListener(this), this);
        getServer().getPluginManager().registerEvents(new VehicleDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new VehicleExitListener(this), this);

        getServer().getPluginManager().registerEvents(new JadedJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new LobbyJoinListener(this), this);

        // Register placeholders
        new Placeholders(this).register();

        // Create Chat Channels
        if(!JadedChat.channelExists("GAME")) {
            ChatChannel gameChannel = new ChatChannelBuilder("GAME")
                    .setDisplayName("<green>GAME</green>")
                    .useDiscordSRV(true)
                    .addChatFormat(new ChatFormatBuilder("default")
                            .addSection("channel", "<green>[GAME]</green> ")
                            .addSection("team", "%duels_team_prefix% ")
                            .addSection("prefix", "%jadedcore_rank_chat_prefix%")
                            .addSection("player", "<gray>%player_name%")
                            .addSection("seperator", "<dark_gray> » ")
                            .addSection("message", "<gray><message>")
                            .build())
                    .build();
            gameChannel.saveToFile("game.yml");
            JadedChat.loadChannel(gameChannel);
        }

        if(!JadedChat.channelExists("TEAM")) {
            ChatChannel gameChannel = new ChatChannelBuilder("TEAM")
                    .setDisplayName("<white>TEAM</white>")
                    .addAlias("T")
                    .addAlias("TC")
                    .addChatFormat(new ChatFormatBuilder("default")
                            .addSection("channel", "<white>[TEAM]</white> ")
                            .addSection("team", "%duels_team_prefix% ")
                            .addSection("prefix", "%jadedcore_rank_chat_prefix%")
                            .addSection("player", "<gray>%player_name%")
                            .addSection("seperator", "<dark_gray> » ")
                            .addSection("message", "<gray><message>")
                            .build())
                    .build();
            gameChannel.saveToFile("team.yml");
            JadedChat.loadChannel(gameChannel);
        }

        if(!JadedChat.channelExists("TOURNAMENT")) {
            ChatChannel tournamentChannel = new ChatChannelBuilder("TOURNAMENT")
                    .setDisplayName("<red>Tournament</red>")
                    .addChatFormat(new ChatFormatBuilder("default")
                            .addSection("prefix", "%jadedcore_rank_chat_prefix%")
                            .addSection("player", "<gray>%player_name%")
                            .addSection("seperator", "<dark_gray> » ")
                            .addSection("message", "<gray><message>")
                            .build())
                    .build();
            tournamentChannel.saveToFile("tournament.yml");
            JadedChat.loadChannel(tournamentChannel);
        }
    }

    /**
     * Gets the Arena Manager.
     * @return ArenaManager.
     */
    public ArenaManager arenaManager() {
        return arenaManager;
    }

    /**
     * Gets the Game Manager.
     * @return GameManager.
     */
    public GameManager gameManager() {
        return gameManager;
    }

    /**
     * Gets the Kit Manager.
     * @return KitManager.
     */
    public KitManager kitManager() {
        return kitManager;
    }

    /**
     * Gets the Settings Manager.
     * @return SettingsManager.
     */
    public SettingsManager settingsManager() {
        return settingsManager;
    }

    /**
     * Gets the Tournament Manager.
     * @return Tournament manager.
     */
    public DuelEventManager duelEventManager() {
        return duelEventManager;
    }

    /**
     * Gets the Duel Manager.
     * @return Duel Manager.
     */
    public DuelManager duelManager() {
        return duelManager;
    }
}
