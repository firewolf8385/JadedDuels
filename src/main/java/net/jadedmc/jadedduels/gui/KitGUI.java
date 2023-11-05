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
import net.jadedmc.jadedduels.game.GameType;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class KitGUI extends CustomGUI {

    public KitGUI(JadedDuelsPlugin plugin) {
        super(27, "Kits");
        addFiller(0,1,2,3,4,5,6,7,8,9,10,17,18,19,20,21,22,23,24,25,26);

        int[] iconSlots = new int[]{10,11,12,13,14,15,16};
        int i = 0;
        for(Kit kit : plugin.kitManager().activeKits()) {

            ItemStack item = new ItemBuilder(kit.iconMaterial(), 1)
                    .setDisplayName("<green>" + kit.name())
                    .addLore("<gray>Playing: " + plugin.queueManager().getPlaying(kit))
                    .addLore("<gray>Queuing: " + plugin.queueManager().getQueueing(kit, GameType.UNRANKED))
                    .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .addFlag(ItemFlag.HIDE_ITEM_SPECIFICS)
                    .build();

            setItem(iconSlots[i], item, (pl, a) -> {
                pl.closeInventory();
                plugin.queueManager().addPlayer(pl, kit, GameType.UNRANKED);
            });
            i++;
        }
    }
}