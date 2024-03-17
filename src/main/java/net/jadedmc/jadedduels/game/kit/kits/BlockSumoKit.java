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
import net.jadedmc.jadedutils.MathUtils;
import net.jadedmc.jadedutils.chat.ChatUtils;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BlockSumoKit extends Kit {

    public BlockSumoKit(final JadedDuelsPlugin plugin) {
        super(plugin, "block_sumo", "Block Sumo");
        iconMaterial(Material.WHITE_WOOL);
        maxDamage(0);
        gameMode(GameMode.SURVIVAL);
        dropItems(false);
        hunger(false);

        ItemStack helmet = new ItemBuilder(Material.LEATHER_HELMET)
                .setUnbreakable(true)
                .build();
        ItemStack chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .setUnbreakable(true)
                .build();
        ItemStack leggings = new ItemBuilder(Material.LEATHER_LEGGINGS)
                .setUnbreakable(true)
                .build();
        ItemStack boots = new ItemBuilder(Material.LEATHER_BOOTS)
                .setUnbreakable(true)
                .build();

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(0,new ItemBuilder(Material.WHITE_WOOL, 64).build());
        addItem(1, new ItemBuilder(Material.SHEARS).setUnbreakable(true).build());
    }

    @Override
    public void onBlockPlace(Game game, BlockPlaceEvent event) {
        Player player = event.getPlayer();
        player.getInventory().getItemInHand().setAmount(64);

        if(MathUtils.distance(game.arena().spectatorSpawn(game.world()), event.getBlock().getLocation()) > 22) {
            ChatUtils.chat(player, "&cError &8» &cYou cannot place blocks here!");
            event.setCancelled(true);
            return;
        }

        List<Material> variants = new ArrayList<>();
        variants.add(Material.WHITE_WOOL);
        variants.add(Material.LIGHT_GRAY_WOOL);
        variants.add(Material.GRAY_WOOL);
        variants.add(Material.BLACK_WOOL);
        variants.add(Material.BROWN_WOOL);
        variants.add(Material.RED_WOOL);
        variants.add(Material.ORANGE_WOOL);
        variants.add(Material.YELLOW_WOOL);
        variants.add(Material.LIME_WOOL);
        variants.add(Material.GREEN_WOOL);
        variants.add(Material.CYAN_WOOL);
        variants.add(Material.LIGHT_BLUE_WOOL);
        variants.add(Material.BLUE_WOOL);
        variants.add(Material.PURPLE_WOOL);
        variants.add(Material.MAGENTA_WOOL);
        variants.add(Material.PINK_WOOL);

        Collections.shuffle(variants);
        event.getBlock().setType(variants.get(0));
    }
}