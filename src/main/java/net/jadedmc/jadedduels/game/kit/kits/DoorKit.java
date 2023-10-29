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
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class DoorKit extends Kit {

    public DoorKit(JadedDuelsPlugin plugin) {
        super(plugin, "door", "Door");
        iconMaterial(Material.OAK_DOOR);
        gameMode(GameMode.SURVIVAL);

        ItemStack helmet = new ItemBuilder(Material.IRON_HELMET)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.DURABILITY, 1)
                .addEnchantment(Enchantment.MENDING, 1)
                .setUnbreakable(true)
                .build();

        ItemStack chestplate = new ItemBuilder(Material.IRON_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.DURABILITY, 1)
                .addEnchantment(Enchantment.MENDING, 1)
                .setUnbreakable(true)
                .build();

        ItemStack leggings = new ItemBuilder(Material.IRON_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.DURABILITY, 1)
                .addEnchantment(Enchantment.MENDING, 1)
                .setUnbreakable(true)
                .build();

        ItemStack boots = new ItemBuilder(Material.IRON_BOOTS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.DURABILITY, 1)
                .addEnchantment(Enchantment.MENDING, 1)
                .setUnbreakable(true)
                .build();

        ItemStack sword = new ItemBuilder(Material.STONE_SWORD)
                .addEnchantment(Enchantment.DAMAGE_ALL, 5)
                .addEnchantment(Enchantment.MENDING, 1)
                .build();
        ItemStack bow = new ItemBuilder(Material.BOW)
                .addEnchantment(Enchantment.DURABILITY, 2)
                .build();
        ItemStack arrows = new ItemStack(Material.ARROW, 8);
        ItemStack goldenApples = new ItemStack(Material.GOLDEN_APPLE, 16);
        ItemStack experienceBottles = new ItemStack(Material.EXPERIENCE_BOTTLE, 16);
        ItemStack crossbow = new ItemBuilder(Material.CROSSBOW)
                .addEnchantment(Enchantment.DURABILITY, 2)
                .addEnchantment(Enchantment.QUICK_CHARGE, 1)
                .build();
        ItemStack ironDoors = new ItemStack(Material.IRON_DOOR, 16);
        ItemStack woodDoors = new ItemStack(Material.OAK_DOOR, 25);
        ItemStack axe = new ItemBuilder(Material.STONE_AXE)
                .addEnchantment(Enchantment.DIG_SPEED, 2)
                .build();
        ItemStack flintAndSteel = new ItemStack(Material.FLINT_AND_STEEL);
        ItemStack steak = new ItemStack(Material.COOKED_BEEF, 8);

        addItem(0, sword);
        addItem(1, axe);
        addItem(2, crossbow);
        addItem(3, bow);
        addItem(5, flintAndSteel);
        addItem(6, steak);
        addItem(7, ironDoors);
        addItem(8, goldenApples);
        addItem(27, experienceBottles);
        addItem(35, arrows);

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);
        addItem(40, woodDoors);
    }
}