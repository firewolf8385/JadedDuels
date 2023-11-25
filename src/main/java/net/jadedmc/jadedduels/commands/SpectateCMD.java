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
package net.jadedmc.jadedduels.commands;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.Game;
import net.jadedmc.jadedduels.game.GameType;
import net.jadedmc.jadedduels.gui.SpectateGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateCMD extends AbstractCommand {
    private final JadedDuelsPlugin plugin;

    public SpectateCMD(JadedDuelsPlugin plugin) {
        super("spectate", "", false);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(player.getWorld().equals(plugin.duelEventManager().world())) {
            new SpectateGUI(plugin, GameType.TOURNAMENT).open(player);
            return;
        }

        Game game = plugin.gameManager().game(player);

        if(game == null) {
            new SpectateGUI(plugin, GameType.UNRANKED).open(player);
            return;
        }

        if(game.gameType() == GameType.TOURNAMENT) {
            new SpectateGUI(plugin, GameType.TOURNAMENT).open(player);
            return;
        }

        new SpectateGUI(plugin, GameType.UNRANKED).open(player);
    }
}