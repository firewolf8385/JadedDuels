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
package net.jadedmc.jadedduels.game.kit.kits;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.kit.Kit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AxeKit extends Kit {

    /**
     * Creates the kit.
     * @param plugin Instance of the plugin.
     */
    public AxeKit(JadedDuelsPlugin plugin) {
        super(plugin, "axe", "Axe");
        iconMaterial(Material.DIAMOND_AXE);

        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);

        ItemStack shield = new ItemStack(Material.SHIELD);

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack crossbow = new ItemStack(Material.CROSSBOW);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack arrows = new ItemStack(Material.ARROW, 6);

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(40, shield);

        addItem(0, axe);
        addItem(1, sword);
        addItem(2, crossbow);
        addItem(3, bow);
        addItem(4, arrows);
    }

}
