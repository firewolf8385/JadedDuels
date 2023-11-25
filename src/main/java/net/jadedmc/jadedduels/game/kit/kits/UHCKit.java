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

import com.cryptomorin.xseries.XMaterial;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class UHCKit extends Kit {

    /**
     * Creates the kit.
     * @param plugin Instance of the plugin.
     */
    public UHCKit(JadedDuelsPlugin plugin) {
        super(plugin, "uhc", "UHC");
        iconMaterial(Material.GOLDEN_APPLE);
        gameMode(GameMode.SURVIVAL);
        naturalRegeneration(false);
        hunger(false);

        ItemStack helmet = new ItemBuilder(Material.DIAMOND_HELMET)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .build();

        ItemStack chestplate = new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .build();

        ItemStack leggings = new ItemBuilder(Material.DIAMOND_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .build();

        ItemStack boots = new ItemBuilder(Material.DIAMOND_BOOTS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .build();

        ItemStack shield = new ItemStack(Material.SHIELD);

        ItemStack sword = new ItemBuilder(XMaterial.DIAMOND_SWORD)
                .addEnchantment(Enchantment.DAMAGE_ALL, 4)
                .build();
        ItemStack axe = new ItemBuilder(Material.DIAMOND_AXE)
                .addEnchantment(Enchantment.DAMAGE_ALL, 1)
                .build();
        ItemStack bow = new ItemBuilder(Material.BOW)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 1)
                .build();
        ItemStack crossBow = new ItemBuilder(XMaterial.CROSSBOW)
                .addEnchantment(Enchantment.PIERCING, 1)
                .build();
        ItemStack water = new ItemStack(Material.WATER_BUCKET);
        ItemStack goldenApples = new ItemStack(Material.GOLDEN_APPLE, 13);
        ItemStack cobWebs = new ItemStack(Material.COBWEB, 8);
        ItemStack lava = new ItemStack(Material.LAVA_BUCKET);
        ItemStack cobblestone = new ItemStack(Material.COBBLESTONE, 64);
        ItemStack logs = new ItemStack(Material.OAK_LOG, 64);
        ItemStack pickaxe = new ItemBuilder(Material.DIAMOND_PICKAXE)
                .addEnchantment(Enchantment.DIG_SPEED, 4)
                .build();
        ItemStack arrows = new ItemStack(Material.ARROW, 16);

        addItem(0, sword);
        addItem(1, axe);
        addItem(2, bow);
        addItem(3, water);
        addItem(4, goldenApples);
        addItem(5, cobWebs);
        addItem(6, lava);
        addItem(7, lava);
        addItem(8, cobblestone);

        addItem(14, shield);
        addItem(21, water);
        addItem(22, water);
        addItem(23, pickaxe);
        addItem(26, logs);
        addItem(27, arrows);
        addItem(29, crossBow);
        addItem(31, water);
        addItem(35, cobblestone);

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(40, shield);
    }
}