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
package net.jadedmc.jadedduels.listeners;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.Game;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.TNTPrimeEvent;

public class TNTPrimeListener implements Listener {
    private final JadedDuelsPlugin plugin;

    public TNTPrimeListener(JadedDuelsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTNTPrime(TNTPrimeEvent event) {
        if(event.getPrimingEntity() == null) {
            return;
        }

        Game game = plugin.gameManager().game(event.getPrimingEntity().getWorld());

        if(game == null) {
            return;
        }

        for(Entity entity : event.getPrimingEntity().getNearbyEntities(1, 1, 1)) {
            if(!(entity instanceof TNTPrimed tnt)) {
                continue;
            }

            if(tnt.getOrigin() == null) {
                continue;
            }

            Block block = tnt.getWorld().getBlockAt(tnt.getOrigin());
            game.addBlock(block, Material.TNT);
        }
    }
}