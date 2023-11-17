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

public class NetheritePotKit extends Kit {

    public NetheritePotKit(JadedDuelsPlugin plugin) {
        super(plugin, "netherite_pot", "Netherite Potion");
        iconMaterial(Material.NETHERITE_HELMET);

        ItemStack helmet = new ItemBuilder(Material.NETHERITE_HELMET)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.MENDING, 1)
                .build();

        ItemStack chestplate = new ItemBuilder(Material.NETHERITE_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.MENDING, 1)
                .build();

        ItemStack leggings = new ItemBuilder(Material.NETHERITE_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.MENDING, 1)
                .build();

        ItemStack boots = new ItemBuilder(Material.NETHERITE_BOOTS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.MENDING, 1)
                .build();

        ItemStack sword = new ItemBuilder(Material.NETHERITE_SWORD)
                .addEnchantment(Enchantment.DAMAGE_ALL, 5)
                .addEnchantment(Enchantment.FIRE_ASPECT, 2)
                .build();

        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);

        ItemStack healing = new PotionBuilder(Material.SPLASH_POTION)
                .setPotionData(PotionType.INSTANT_HEAL, false, true)
                .build();

        ItemStack speed = new PotionBuilder(Material.SPLASH_POTION)
                .setPotionData(PotionType.SPEED, false, true)
                .build();

        ItemStack strength = new PotionBuilder(Material.SPLASH_POTION)
                .setPotionData(PotionType.STRENGTH, false, true)
                .build();

        ItemStack fireRes =new PotionBuilder(Material.SPLASH_POTION)
                .setPotionData(PotionType.FIRE_RESISTANCE, false, false)
                .build();

        ItemStack experienceBottles = new ItemStack(Material.EXPERIENCE_BOTTLE, 64);

        ItemStack gapples = new ItemStack(Material.GOLDEN_APPLE, 64);

        addItem(0, sword);
        addItem(8, totem);
        addItem(9, experienceBottles);
        addItem(10, experienceBottles);
        addItem(11, totem);
        addItem(12, totem);

        int[] healingSlots = new int[]{4,5,6,7,13,14,15,16,17,23,24,25,32,33,34};
        int[] strengthSlots = new int[]{1,27,28,29,30,31};
        int[] speedSlots = new int[]{2,18,19,20,21,22};
        int[] fireResSlots = new int[]{3,26,35};

        for(int slot : healingSlots) addItem(slot, healing);
        for(int slot : speedSlots) addItem(slot, speed);
        for(int slot : strengthSlots) addItem(slot, strength);
        for(int slot : fireResSlots) addItem(slot, fireRes);

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(40, gapples);
    }

}
