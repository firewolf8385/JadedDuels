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
import net.jadedmc.jadedduels.utils.PotionBuilder;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

public class HSGKit extends Kit {

    public HSGKit(JadedDuelsPlugin plugin) {
        super(plugin, "hsg", "HSG");
        iconMaterial(Material.TURTLE_HELMET);
        gameMode(GameMode.SURVIVAL);

        ItemStack helmet = new ItemBuilder(Material.TURTLE_HELMET)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .setUnbreakable(true)
                .build();

        ItemStack chestplate = new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .setUnbreakable(true)
                .build();

        ItemStack leggings = new ItemBuilder(Material.DIAMOND_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .setUnbreakable(true)
                .build();

        ItemStack boots = new ItemBuilder(Material.DIAMOND_BOOTS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .setUnbreakable(true)
                .build();

        ItemStack sword = new ItemBuilder(Material.DIAMOND_SWORD)
                .addEnchantment(Enchantment.DAMAGE_ALL, 3)
                .build();
        ItemStack crossbow = new ItemStack(Material.CROSSBOW);
        ItemStack goldenApples = new ItemStack(Material.GOLDEN_APPLE, 12);
        ItemStack trident = new ItemBuilder(Material.TRIDENT)
                .addEnchantment(Enchantment.LOYALTY, 1)
                .build();
        ItemStack pufferfish = new ItemStack(Material.PUFFERFISH_BUCKET);
        ItemStack water = new ItemStack(Material.WATER_BUCKET);
        ItemStack lava = new ItemStack(Material.LAVA_BUCKET);
        ItemStack oakPlanks = new ItemStack(Material.OAK_PLANKS);
        ItemStack cobblestone = new ItemStack(Material.COBBLESTONE, 64);
        ItemStack weaknessArrows = new PotionBuilder(Material.TIPPED_ARROW)
                .setPotionData(PotionType.WEAKNESS, false, false)
                .asItemBuilder()
                .setAmount(2)
                .build();
        ItemStack arrows = new ItemStack(Material.ARROW,  6);
        ItemStack goldenCarrots = new ItemStack(Material.GOLDEN_CARROT, 16);
        ItemStack pickaxe = new ItemBuilder(Material.IRON_PICKAXE)
                .addEnchantment(Enchantment.DIG_SPEED, 3)
                .build();
        ItemStack axe = new ItemBuilder(Material.IRON_AXE)
                .addEnchantment(Enchantment.DIG_SPEED, 3)
                .build();
        ItemStack enderPearl = new ItemStack(Material.ENDER_PEARL);

        addItem(0, sword);
        addItem(1, crossbow);
        addItem(2, crossbow);
        addItem(3, goldenApples);
        addItem(4, trident);
        addItem(5, pufferfish);
        addItem(6, water);
        addItem(7, lava);
        addItem(8, cobblestone);
        addItem(9, weaknessArrows);
        addItem(10, arrows);
        addItem(15, goldenCarrots);
        addItem(16, pickaxe);
        addItem(17, axe);
        addItem(30, enderPearl);
        addItem(33, water);
        addItem(34, lava);
        addItem(35, oakPlanks);

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);
    }
}
