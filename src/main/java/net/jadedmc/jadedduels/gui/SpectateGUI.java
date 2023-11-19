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
package net.jadedmc.jadedduels.gui;

import net.jadedmc.jadedcore.utils.gui.CustomGUI;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.Game;
import net.jadedmc.jadedduels.game.GameType;
import net.jadedmc.jadedduels.game.team.Team;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class SpectateGUI extends CustomGUI {

    public SpectateGUI(JadedDuelsPlugin plugin, GameType gameType) {
        super(54, "Current Games");
        addFiller(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

        if(gameType == GameType.TOURNAMENT) {
            int slot = 9;
            for(Game game : plugin.gameManager().activeGames()) {
                if(game.gameType() != GameType.TOURNAMENT) {
                    continue;
                }

                ItemBuilder builder = new ItemBuilder(game.kit().iconMaterial())
                        .setDisplayName("<green>" + game.kit().name() + " <gray>(" + game.arena().name() + ")")
                        .addLore("");

                Team team1 = game.teamManager().teams().get(0);
                Team team2 = game.teamManager().teams().get(1);

                builder.addLore(team1.teamColor().chatColor() + team1.teamColor().displayName() + ":");
                for(Player player : team1.players()) {
                    builder.addLore("  " + team1.teamColor().chatColor() + player.getName());
                }

                builder.addLore("");

                builder.addLore(team2.teamColor().chatColor() + team2.teamColor().displayName() + ":");
                for(Player player : team2.players()) {
                    builder.addLore("  " + team2.teamColor().chatColor() + player.getName());
                }

                builder.addLore("");

                builder.addLore(team1.teamColor().chatColor() + "" + team1.score() + " <gray>- " + team2.teamColor().chatColor() + "" + team2.score());

                builder.addFlag(ItemFlag.HIDE_ATTRIBUTES);

                setItem(slot, builder.build(), (p, a) -> {
                    Game oldGame = plugin.gameManager().game(p);
                    if(oldGame != null) {
                        oldGame.removePlayer(p);
                    }

                    game.addSpectator(p);
                });
                slot++;
            }
        }
        else {
            int slot = 9;
            for(Game game : plugin.gameManager().activeGames()) {
                if(game.gameType() == GameType.TOURNAMENT) {
                    continue;
                }

                ItemBuilder builder = new ItemBuilder(game.kit().iconMaterial())
                        .setDisplayName("<green>" + game.kit().name() + " <gray>(" + game.arena().name() + ")")
                        .addLore("");

                for(Team team : game.teamManager().teams()) {
                    builder.addLore(team.teamColor().chatColor() + team.teamColor().displayName() + ":");

                    for(Player player : team.players()) {
                        builder.addLore("  " + team.teamColor().chatColor() + player.getName());
                    }

                    builder.addLore("");
                }

                setItem(slot, builder.build(), (p, a) -> {
                    Game oldGame = plugin.gameManager().game(p);
                    if(oldGame != null) {
                        oldGame.removePlayer(p);
                    }

                    game.addSpectator(p);
                });
                slot++;
            }
        }
    }
}
