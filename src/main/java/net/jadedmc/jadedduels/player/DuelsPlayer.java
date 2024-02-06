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
package net.jadedmc.jadedduels.player;

import com.cryptomorin.xseries.XSound;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Boat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DuelsPlayer {
    private final JadedDuelsPlugin plugin;
    private final UUID playerUUID;

    private final Map<String, KitData> kitData = new HashMap<>();

    // Settings
    private boolean allowDuelRequests = true;
    private String boatType = "OAK";
    // TODO: Implement shield disable sound setting.
    private String shieldDisableSound = "";
    private boolean showELO = true;
    private boolean showScoreboard = true;
    private String teamColor = "NONE";
    private boolean useHealthPercent = false;

    public DuelsPlayer(final JadedDuelsPlugin plugin, final UUID playerUUID) {
        this.plugin = plugin;
        this.playerUUID = playerUUID;

        // TODO: Load Data
        // Do on main thread, since being loaded with a completable future.
    }

    public boolean allowDuelRequests() {
        return allowDuelRequests;
    }

    public void allowDuelRequests(boolean allowDuelRequests) {
        this.allowDuelRequests = allowDuelRequests;
    }

    public Boat.Type boatType() {
        return Boat.Type.valueOf(boatType);
    }

    public void boatType(String boatType) {
        this.boatType = boatType;

        // TODO: Update MySQL.
    }

    public void playShieldDisableSound() {
        XSound.Record sound = XSound.parse(shieldDisableSound);

        if(sound == null) {
            return;
        }

        sound.soundPlayer().forPlayers(Bukkit.getPlayer(playerUUID)).play();
    }

    public void shieldDisableSound(String shieldDisableSound) {
        this.shieldDisableSound = shieldDisableSound;
    }

    public boolean showELO() {
        return showELO;
    }

    public void showELO(boolean showELO) {
        this.showELO = showELO;

        // TODO: Update MySQL.
    }

    public boolean showScoreboard() {
        return showScoreboard;
    }

    public void showScoreboard(boolean showScoreboard) {
        this.showScoreboard = showScoreboard;

        // TODO: MySQL
    }

    public String teamColor() {
        return teamColor;
    }

    public void teamColor(String teamColor) {
        this.teamColor = teamColor;

        // TODO: MySQL
    }

    public boolean useHealthPercent() {
        return useHealthPercent;
    }

    public void useHealthPercent(boolean useHealthPercent) {
        this.useHealthPercent = useHealthPercent;

        // TODO: MySQL
    }

    public UUID uuid() {
        return playerUUID;
    }
}