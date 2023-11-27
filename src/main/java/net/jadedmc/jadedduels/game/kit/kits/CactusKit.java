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
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CactusKit extends Kit {
    private final JadedDuelsPlugin plugin;
    public CactusKit(JadedDuelsPlugin plugin) {
        super(plugin, "cactus", "Cactus");
        this.plugin = plugin;

        iconMaterial(Material.CACTUS);
        gameMode(GameMode.SURVIVAL);
        maxHealth(1);
        startingHealth(1);
        hunger(false);

        addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200000, 100, true));

        voidLevel(50);

        ItemStack cactus = new ItemBuilder(Material.CACTUS, 16).build();
        ItemStack egg = new ItemStack(Material.EGG);

        addItem(0, cactus);
        addItem(1, egg);
    }

    @Override
    public void onProjectileLaunch(Player player, Game game, ProjectileLaunchEvent event) {
        if(!(event.getEntity() instanceof Egg)) {
            return;
        }

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!game.spectators().contains(player)) {
                player.getInventory().setItem(1, new ItemStack(Material.EGG));
            }
        }, 50);
    }
}