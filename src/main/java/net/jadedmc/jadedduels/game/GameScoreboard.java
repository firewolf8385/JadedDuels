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
package net.jadedmc.jadedduels.game;

import net.jadedmc.jadedduels.game.team.Team;
import net.jadedmc.jadedduels.utils.DateUtils;
import net.jadedmc.jadedduels.utils.GameUtils;
import net.jadedmc.jadedduels.utils.scoreboard.CustomScoreboard;
import net.jadedmc.jadedduels.utils.scoreboard.ScoreHelper;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameScoreboard extends CustomScoreboard {
    private final Game game;

    public GameScoreboard(Player player, Game game) {
        super(player);
        this.game = game;
        update(player);
    }

    public void update(Player player) {
        ScoreHelper helper;

        if(ScoreHelper.hasScore(player)) {
            helper = ScoreHelper.getByPlayer(player);
        }
        else {
            helper = ScoreHelper.createScore(player);
        }

        switch (game.gameType()) {
            default -> {
                List<Player> opponents = new ArrayList<>();

                Team team = game.teamManager().team(player);

                for(Team opposingTeam : game.teamManager().teams()) {
                    if(team.equals(opposingTeam)) {
                        continue;
                    }

                    opponents.addAll(opposingTeam.players());
                }

                helper.setTitle("&a&lDUELS");

                helper.setSlot(15, "&7" + DateUtils.currentDateToString());
                helper.setSlot(14, "");
                helper.setSlot(13, "&aTime: &f" + game.timer().toString());
                helper.setSlot(12, "");
                helper.setSlot(11, "&aKit:");
                helper.setSlot(10, "  &f" + game.kit().name());
                helper.setSlot(9, "");

                int slot = 7;
                if(opponents.size() < 3 && opponents.size() > 0) {
                    if(opponents.size() == 1) {
                        helper.setSlot(8, "&aOpponent:");
                    }
                    else {
                        helper.setSlot(8, "&aOpponents:");
                    }

                    for(Player opponent : opponents) {
                        helper.setSlot(slot, "  " + game.teamManager().team(opponent).teamColor().chatColor()  + opponent.getName());
                        slot--;

                        if(game.teamManager().team(opponent).deadPlayers().contains(opponent)) {
                            helper.setSlot(slot, "  &c0% &7- " + GameUtils.getFormattedPing(opponent));
                        }
                        else {
                            helper.setSlot(slot, "  " + GameUtils.getFormattedHealth(opponent) + " &7- " + GameUtils.getFormattedPing(opponent));
                        }

                        slot--;
                    }
                }
                else {
                    helper.setSlot(8, "&aOpponents: &f" + opponents.size());
                }

                for(int i = 3; i < slot; i++) {
                    helper.removeSlot(i);
                }

                helper.setSlot(2, "");
                helper.setSlot(1, "&aplay.jadedmc.net");
            }
        }
    }
}