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
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

public class IronPotKit extends Kit {

    public IronPotKit(JadedDuelsPlugin plugin) {
        super(plugin, "iron_pot", "Iron Pot");
        iconMaterial(Material.IRON_CHESTPLATE);

        ItemStack helmet = new ItemBuilder(Material.IRON_HELMET)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.MENDING, 1)
                .setUnbreakable(true)
                .build();

        ItemStack chestplate = new ItemBuilder(Material.IRON_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.MENDING, 1)
                .setUnbreakable(true)
                .build();

        ItemStack leggings = new ItemBuilder(Material.IRON_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.MENDING, 1)
                .setUnbreakable(true)
                .build();

        ItemStack boots = new ItemBuilder(Material.IRON_BOOTS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.MENDING, 1)
                .setUnbreakable(true)
                .build();

        ItemStack sword = new ItemBuilder(Material.DIAMOND_SWORD)
                .addEnchantment(Enchantment.DAMAGE_ALL, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .addEnchantment(Enchantment.SWEEPING_EDGE, 3)
                .addEnchantment(Enchantment.MENDING,1)
                .build();
        ItemStack bow = new ItemBuilder(Material.BOW)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 3)
                .build();
        ItemStack arrows = new ItemStack(Material.ARROW, 8);
        ItemStack healthPotion = new PotionBuilder(Material.SPLASH_POTION)
                .setPotionData(PotionType.INSTANT_HEAL, false, true)
                .build();
        ItemStack strengthPotion = new PotionBuilder(Material.SPLASH_POTION)
                .setPotionData(PotionType.STRENGTH, false, false)
                .build();
        ItemStack speedPotion = new PotionBuilder(Material.SPLASH_POTION)
                .setPotionData(PotionType.SPEED, false, false)
                .build();
        ItemStack goldenApples = new ItemStack(Material.GOLDEN_APPLE, 12);
        ItemStack experienceBottles = new ItemStack(Material.EXPERIENCE_BOTTLE, 16);

        addItem(0, sword);
        addItem(1, experienceBottles);
        addItem(27, bow);
        addItem(28, arrows);

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);
        addItem(40, goldenApples);

        int[] healingPotions = new int[]{2,3,4,5,6,9,10,11,12,13,14,15,18,19,20,21,22,23,24,29,30,31,32,33};
        int[] speedPotions = new int[]{7,16,25,34};
        int[] strengthPotions = new int[]{8,17,26,35};

        for(int i : healingPotions) {
            addItem(i, healthPotion);
        }

        for(int i : speedPotions) {
            addItem(i, speedPotion);
        }

        for(int i : strengthPotions) {
            addItem(i, strengthPotion);
        }
    }
}