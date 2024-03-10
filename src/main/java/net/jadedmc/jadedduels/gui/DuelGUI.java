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
package net.jadedmc.jadedduels.gui;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.GameType;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedutils.chat.ChatUtils;
import net.jadedmc.jadedutils.gui.CustomGUI;
import net.jadedmc.jadedutils.items.ItemBuilder;
import net.jadedmc.jadedutils.items.SkullBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * Manages the DuelGUI, which allows a player to duel another.
 */
public class DuelGUI extends CustomGUI {

    /**
     * Creates the main duel GUI.
     * @param plugin Instance of the plugin.
     * @param player Player sending the duel request.
     * @param target Target of the duel request.
     */
    public DuelGUI(final JadedDuelsPlugin plugin, final Player player, final Player target) {
        super(54, "Select A Kit");
        addFiller(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

        int i = 0;
        for(Kit kit : plugin.kitManager().kits()) {
            // Skip games with no arenas set up yet.
            if(plugin.arenaManager().getArenas(kit, GameType.DUEL).size() == 0) {
                continue;
            }

            ItemStack kitItem = new ItemBuilder(kit.iconMaterial())
                    .setDisplayName("<green><bold>" + kit.name())
                    .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .addFlag(ItemFlag.HIDE_ITEM_SPECIFICS)
                    .addLore("").addLore("<green>Click to Send Request").build();
            setItem(i + 9, kitItem, (p,a) -> {
                if(target == null) {
                    ChatUtils.chat(player, "&cError &8» &cThat player is not online!");
                    player.closeInventory();
                    return;
                }

                if(plugin.gameManager().game(target) != null) {
                    ChatUtils.chat(player, "&cError &8» &c/That player is currently in a game!");
                    player.closeInventory();
                    return;
                }

                //plugin.duelManager().addRequest(player, target, kit);
                //player.closeInventory();
                new DuelBestOfGUI(plugin, player, target, kit).open(p);
            });

            i++;
        }
    }

    private class DuelBestOfGUI extends CustomGUI {
        public DuelBestOfGUI(final JadedDuelsPlugin plugin, final Player player, final Player target, Kit kit) {
            super(54, "Best Of...");
            addFiller(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

            ItemStack bo1 = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ2NWNlODNmMWFhNWI2ZTg0ZjliMjMzNTk1MTQwZDViNmJlY2ViNjJiNmQwYzY3ZDFhMWQ4MzYyNWZmZCJ9fX0=")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Best of 1")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(19, bo1, (p,a) -> {
                plugin.duelManager().addRequest(player, target, kit, 1);
                player.closeInventory();
            });

            ItemStack bo3 = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjFlNGVhNTliNTRjYzk5NDE2YmM5ZjYyNDU0OGRkYWMyYTM4ZWVhNmEyZGJmNmU0Y2NkODNjZWM3YWM5NjkifX19")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Best of 3")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(21, bo3, (p,a) -> {
                plugin.duelManager().addRequest(player, target, kit, 2);
                player.closeInventory();
            });

            ItemStack bo5 = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODRjOGMzNzEwZGEyNTU5YTI5MWFkYzM5NjI5ZTljY2VhMzFjYTlkM2QzNTg2YmZlYTZlNmUwNjEyNGIzYyJ9fX0=")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Best of 5")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(23, bo5, (p,a) -> {
                plugin.duelManager().addRequest(player, target, kit, 3);
                player.closeInventory();
            });

            ItemStack bo7 = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjRiZGU3OWY4NGZjNWYzZjFmYmM1YmMwMTA3MTA2NmJkMjBjZDI2M2ExNjU0ZDY0ZDYwZDg0MjQ4YmE5Y2QifX19")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Best of 7")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(25, bo7, (p,a) -> {
                plugin.duelManager().addRequest(player, target, kit, 4);
                player.closeInventory();
            });
        }
    }
}