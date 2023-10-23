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

import net.jadedmc.jadedduels.commands.AbstractCommand;
import net.jadedmc.jadedduels.game.GameManager;
import net.jadedmc.jadedduels.game.arena.ArenaManager;
import net.jadedmc.jadedduels.game.kit.KitManager;
import net.jadedmc.jadedduels.listeners.EntityDamageByEntityListener;
import net.jadedmc.jadedduels.listeners.EntityDamageListener;
import net.jadedmc.jadedduels.listeners.PlayerDropItemListener;
import net.jadedmc.jadedduels.utils.scoreboard.ScoreboardUpdate;
import org.bukkit.plugin.java.JavaPlugin;

public final class JadedDuelsPlugin extends JavaPlugin {
    private ArenaManager arenaManager;
    private GameManager gameManager;
    private KitManager kitManager;
    private SettingsManager settingsManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        settingsManager = new SettingsManager(this);
        kitManager = new KitManager(this);

        // Load arenas.
        arenaManager = new ArenaManager(this);
        arenaManager.loadArenas();

        gameManager = new GameManager(this);

        AbstractCommand.registerCommands(this);

        getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItemListener(), this);

        // Updates scoreboards every second
        new ScoreboardUpdate().runTaskTimer(this, 20L, 20L);
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
}
