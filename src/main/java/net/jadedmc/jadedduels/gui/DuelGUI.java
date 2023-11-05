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
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedutils.chat.ChatUtils;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * Manages the DuelGUI, which allows a player to duel another.
 */
public class DuelGUI extends CustomGUI {

    /**
     * Creates the main duel GUI.
     * @param plugin Instance of the plugin.
     * @param player Player sending the duel request.
     * @param target Target of the duel request.
     */
    public DuelGUI(final JadedDuelsPlugin plugin, final Player player, final Player target) {
        super(54, "Select A Kit");
        addFiller(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

        int i = 0;
        for(Kit kit : plugin.kitManager().kits()) {
            ItemStack kitItem = new ItemBuilder(kit.iconMaterial())
                    .setDisplayName("<green><bold>" + kit.name())
                    .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .addLore("").addLore("<green>Click to Send Request").build();
            setItem(i + 9, kitItem, (p,a) -> {
                if(target == null) {
                    ChatUtils.chat(player, "&cError &8» &cThat player is not online!");
                    player.closeInventory();
                    return;
                }

                if(plugin.gameManager().game(target) != null) {
                    ChatUtils.chat(player, "&cError &8» &c/That player is currently in a game!");
                    player.closeInventory();
                    return;
                }

                plugin.duelManager().addRequest(player, target, kit);
                player.closeInventory();
            });

            i++;
        }
    }
}