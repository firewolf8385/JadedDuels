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
package net.jadedmc.jadedduels.game.queue;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.GameType;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedutils.DateUtils;
import net.jadedmc.jadedutils.Timer;
import net.jadedmc.jadedutils.scoreboard.CustomScoreboard;
import net.jadedmc.jadedutils.scoreboard.ScoreHelper;
import org.bukkit.entity.Player;

public class QueueScoreboard extends CustomScoreboard {
    private final JadedDuelsPlugin plugin;
    private final Kit kit;
    private final Timer timer;
    private final GameType gameType;

    public QueueScoreboard(JadedDuelsPlugin plugin, Player player, Kit kit, Timer timer, GameType gameType) {
        super(player);
        this.plugin = plugin;
        this.kit = kit;
        this.timer = timer;
        this.gameType = gameType;

        update(player);
    }

    /**
     * Updates the scoreboard for a specific player.
     * @param player Player to update scoreboard for.
     */
    public void update(Player player) {
        ScoreHelper helper;

        if(ScoreHelper.hasScore(player)) {
            helper = ScoreHelper.getByPlayer(player);
        }
        else {
            helper = ScoreHelper.createScore(player);
        }

        // Sets up the scoreboard.
        helper.setTitle("&a&lDuels");
        helper.setSlot(15, "&7" + DateUtils.currentDateToString());
        helper.setSlot(14, "");
        helper.removeSlot(13);
        helper.removeSlot(12);
        helper.removeSlot(11);
        helper.removeSlot(10);
        helper.removeSlot(9);
        helper.removeSlot(8);
        helper.removeSlot(7);
        helper.removeSlot(6);
        helper.setSlot(5, "&aQueue");
        helper.setSlot(4, "  &aKit: &f" + kit.name());
        helper.setSlot(3, "  &aTime: &f" + timer.toString());
        helper.setSlot(2, "");
        helper.setSlot(1, "&aplay.jadedmc.net");
    }
}