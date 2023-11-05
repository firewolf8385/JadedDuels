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
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;

public class SpleefKit extends Kit {

    public SpleefKit(JadedDuelsPlugin plugin) {
        super(plugin, "spleef", "Spleef");
        iconMaterial(Material.SNOWBALL);
        gameMode(GameMode.SURVIVAL);
        addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200000, 100, true));
        voidLevel(50);

        addBreakableBlock(Material.SNOW_BLOCK);

        ItemStack shovel = new ItemBuilder(Material.DIAMOND_SHOVEL)
                .addEnchantment(Enchantment.DIG_SPEED, 5)
                .setUnbreakable(true)
                .build();
        addItem(0, shovel);
    }

    @Override
    public void onBlockBreak(Game game, BlockBreakEvent event) {
        if(event.getBlock().getType() != Material.SNOW_BLOCK) {
            return;
        }

        if(new Random().nextInt(4) == 0) {
            event.getPlayer().getInventory().addItem(new ItemStack(Material.SNOWBALL));
        }

        //game.addBlock(event.getBlock().getLocation(), Material.SNOW_BLOCK);
        event.getBlock().setType(Material.AIR);
        event.setCancelled(true);
    }

    @Override
    public void onProjectileHit(Game game, ProjectileHitEvent event) {
        if(event.getEntity() instanceof Snowball) {
            // Stole this off Spigot, not perfect but gets the job done.
            Location loc = event.getEntity().getLocation();
            Vector vec = event.getEntity().getVelocity();
            Location loc2 = new Location(loc.getWorld(), loc.getX()+vec.getX(), loc.getY()+vec.getY(), loc.getZ()+vec.getZ());

            if (loc2.getBlock().getType() == Material.SNOW_BLOCK) {
                //game.addBlock(loc2, Material.SNOW_BLOCK);
                loc2.getBlock().setType(Material.AIR);
            }
        }
    }
}
