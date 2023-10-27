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
import net.jadedmc.jadedduels.game.GameState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class BlockBreakListener implements Listener {
    private final JadedDuelsPlugin plugin;

    public BlockBreakListener(JadedDuelsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(BlockBreakEvent event) {
        if(event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();

        if(player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        Game game = plugin.gameManager().game(player);

        if(game == null) {
            event.setCancelled(true);
            return;
        }

        // If the game isn't running, cancel the event.
        if(game.gameState() == GameState.COUNTDOWN || game.gameState() == GameState.END) {
            event.setCancelled(true);
            return;
        }

        // TODO: Kit block break event. game.kit().onBlockBreak(game, event);

        // Prevent spectators from placing/breaking blocks.
        if(game.spectators().contains(player)) {
            event.setCancelled(true);
            return;
        }

        if(!game.blocks().contains(event.getBlock())) {
            event.setCancelled(true);
            return;
        }

        // Get the drops from the block and add them to the inventory.
        Collection<ItemStack> drops = event.getBlock().getDrops(player.getInventory().getItemInMainHand());
        drops.forEach(drop -> player.getInventory().addItem(drop));
    }
}