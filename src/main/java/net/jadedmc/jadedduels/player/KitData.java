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

import net.jadedmc.jadedduels.JadedDuelsPlugin;

import java.util.HashMap;
import java.util.Map;

public class KitData {
    private final JadedDuelsPlugin plugin;
    private final DuelsPlayer duelsPlayer;
    private final String kitID;

    // Data
    private final Map<Integer, Integer> kitEditor = new HashMap<>();
    private int elo = 1400;
    private int wins = 0;
    private int losses = 0;
    private int winStreak = 0;
    private int bestWinStreak = 0;
    private int loseStreak = 0;
    private int worstLoseStreak = 0;

    public KitData(final JadedDuelsPlugin plugin, DuelsPlayer duelsPlayer, String kitID) {
        this.plugin = plugin;
        this.duelsPlayer = duelsPlayer;
        this.kitID = kitID;

        // TODO: MySQL.
    }

    public void addLoss() {
        winStreak = 0;
        losses++;
        loseStreak++;

        if(loseStreak > worstLoseStreak) {
            worstLoseStreak = loseStreak;
        }
    }

    public void addWin() {
        loseStreak = 0;
        wins++;
        winStreak++;

        if(winStreak > bestWinStreak) {
            bestWinStreak = winStreak;
        }
    }

    public int bestWinStreak() {
        return bestWinStreak;
    }

    public void clearKitEditor() {
        // TODO: MySQL
        // Loops through kit editor and deletes all entries related to this kit.
    }

    public int elo() {
        return elo;
    }

    public Map<Integer, Integer> kitEditor() {
        return kitEditor;
    }

    public int loseStreak() {
        return loseStreak;
    }

    public int losses() {
        return losses;
    }

    public void updateKitEditor() {
        // TODO: MySQL
        // Stores the updated kit editor into MySQL.
        // Starts by clearing it with clearKitEditor().
    }

    private void updateMySQL() {
        // TODO: Update MySQL.
        // Updates all values at once. Done at the end of addWin and addLoss methods.
    }

    public int wins() {
        return wins;
    }

    public int winStreak() {
        return winStreak;
    }
}
