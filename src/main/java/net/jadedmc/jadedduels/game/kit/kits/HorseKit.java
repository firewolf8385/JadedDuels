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
import net.jadedmc.jadedduels.game.Game;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HorseKit extends Kit {

    public HorseKit(JadedDuelsPlugin plugin) {
        super(plugin, "horse", "Horse");
        iconMaterial(Material.DIAMOND_HORSE_ARMOR);
        exitVehicle(false);
        naturalRegeneration(false);
        hunger(false);

        ItemStack helmet = new ItemBuilder(Material.IRON_HELMET)
                .addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1)
                .build();

        ItemStack chestplate = new ItemBuilder(Material.IRON_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .build();

        ItemStack leggings = new ItemBuilder(Material.IRON_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1)
                .build();

        ItemStack boots = new ItemBuilder(Material.DIAMOND_BOOTS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .build();

        ItemStack shield = new ItemStack(Material.SHIELD);

        ItemStack sword = new ItemBuilder(Material.IRON_SWORD)
                .addEnchantment(Enchantment.DAMAGE_ALL, 1)
                .build();

        ItemStack axe = new ItemBuilder(Material.IRON_AXE)
                .addEnchantment(Enchantment.DURABILITY, 1)
                .build();

        ItemStack bow = new ItemBuilder(Material.BOW)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .build();

        ItemStack crossbow = new ItemBuilder(Material.CROSSBOW)
                .addEnchantment(Enchantment.PIERCING, 1)
                .build();

        ItemStack goldenApples = new ItemStack(Material.GOLDEN_APPLE, 2);

        ItemStack arrows = new ItemStack(Material.ARROW, 64);

        addItem(0, sword);
        addItem(1, axe);
        addItem(2, bow);
        addItem(3, crossbow);
        addItem(4, goldenApples);
        addItem(7, arrows);
        addItem(8, arrows);

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(40, shield);
    }

    @Override
    public void onKitApply(Game game, Player player) {
        Horse horse = (Horse) game.world().spawnEntity(player.getLocation(), EntityType.HORSE);
        horse.setTamed(true); // Sets horse to tamed
        horse.setOwner(player); // Makes the horse the player's
        horse.setColor(Horse.Color.BROWN);
        horse.setMaxHealth(40);
        horse.setHealth(40);
        horse.setAdult();
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1)); // Gives horse saddle
        horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_HORSE_ARMOR)); // Gives the horse armor
        horse.teleport(player);
        horse.setPassenger(player);
        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.24);
    }
}