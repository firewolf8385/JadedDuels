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
import net.jadedmc.jadedduels.utils.DateUtils;
import net.jadedmc.jadedduels.utils.scoreboard.CustomScoreboard;
import net.jadedmc.jadedduels.utils.scoreboard.ScoreHelper;
import net.jadedmc.jadedutils.MathUtils;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class BowSpleefKit extends Kit {
    private final JadedDuelsPlugin plugin;
    private final Map<Player, Integer> doubleJumps = new HashMap<>();
    private final Map<Player, Integer> repulsors = new HashMap<>();
    private final Map<Player, Integer> tripleShots = new HashMap<>();
    private final Set<Player> doubleJumpDelay = new HashSet<>();

    public BowSpleefKit(JadedDuelsPlugin plugin) {
        super(plugin, "bow_spleef", "Bow Spleef");
        this.plugin = plugin;
        iconMaterial(Material.TNT);
        gameMode(GameMode.ADVENTURE);
        hunger(false);

        takeDamage(false);
        voidLevel(51);

        ItemStack bow = new ItemBuilder(Material.BOW)
                .addEnchantment(Enchantment.ARROW_FIRE, 1)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .setUnbreakable(true)
                .build();
        addItem(0, bow);

        ItemStack feather = new ItemBuilder(Material.FEATHER)
                .setDisplayName("<green><bold>Double Jump")
                .build();
        addItem(1, feather);

        ItemStack arrows = new ItemBuilder(Material.ARROW, 1).build();
        addItem(35, arrows);
    }

    private int doubleJumps(Player player) {
        if(doubleJumps.containsKey(player)) {
            return doubleJumps.get(player);
        }

        return 0;
    }

    private int repulsors(Player player) {
        if(repulsors.containsKey(player)) {
            return repulsors.get(player);
        }

        return 0;
    }

    private int tripleShots(Player player) {
        if(tripleShots.containsKey(player)) {
            return tripleShots.get(player);
        }

        return 0;
    }

    private void useDoubleJump(Player player) {
        doubleJumps.put(player, doubleJumps.get(player) - 1);
    }

    private void useRepulsor(Player player) {
        repulsors.put(player, repulsors.get(player) - 1);
    }

    private void useTripleShot(Player player) {
        tripleShots.put(player, tripleShots.get(player) - 1);
    }

    @Override
    public void onGamePlayerLeave(Game game, Player player) {
        doubleJumps.remove(player);
        repulsors.remove(player);
        tripleShots.remove(player);
        doubleJumpDelay.remove(player);
    }

    @Override
    public void onKitApply(Game game, Player player) {
        doubleJumps.put(player, 10);
        repulsors.put(player, 10);
        tripleShots.put(player, 10);

        player.setAllowFlight(true);
    }

    @Override
    public void onNamedItemClick(Game game, Player player, String item) {
        if(!item.equals("Double Jump")) {
            return;
        }

        useDoubleJump(player);

        doubleJumpDelay.add(player);
        player.setAllowFlight(false);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            doubleJumpDelay.remove(player);
            player.setAllowFlight(true);
        }, 15);
        player.setFlying(false);

        Vector vector = player.getLocation().getDirection().normalize().multiply(0.5).add(new Vector(0, 0.8, 0));
        player.setVelocity(vector);
    }

    @Override
    public void onPlayerInteract(Game game, PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(game.spectators().contains(player)) {
            return;
        }

        if(event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if(event.getItem() == null) {
            return;
        }

        if(event.getItem().getType() != Material.BOW) {
            return;
        }

        if(tripleShots(player) < 1) {
           return;
        }

        useTripleShot(player);

        Arrow arrow = player.launchProjectile(Arrow.class);
        arrow.setVelocity(player.getLocation().getDirection().multiply(1.9));
        arrow.setFireTicks(100);

        Arrow arrow2 = player.launchProjectile(Arrow.class);
        arrow2.setVelocity(MathUtils.rotateVector(arrow.getVelocity().clone(), 0.21816616).multiply(1.1));
        arrow2.setFireTicks(100);

        Arrow arrow3 = player.launchProjectile(Arrow.class);
        arrow3.setVelocity(MathUtils.rotateVector(arrow.getVelocity().clone(), -0.21816616).multiply(1.1));
        arrow3.setFireTicks(100);
    }

    @Override
    public void onPlayerToggleFlight(Game game, PlayerToggleFlightEvent event) {
        if(game.spectators().contains(event.getPlayer())) {
            return;
        }

        Player player = event.getPlayer();

        // Prevents players from double jumping too often.
        if(doubleJumpDelay.contains(player)) {
            return;
        }

        if(doubleJumps(player) < 1) {
            event.setCancelled(true);
            player.setFlying(false);
            player.setAllowFlight(false);
            return;
        }

        useDoubleJump(event.getPlayer());

        doubleJumpDelay.add(player);
        player.setAllowFlight(false);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            doubleJumpDelay.remove(player);
            player.setAllowFlight(true);
        }, 15);
        player.setFlying(false);

        Vector vector = player.getLocation().getDirection().normalize().multiply(0.5).add(new Vector(0, 0.8, 0));
        player.setVelocity(vector);

        event.setCancelled(true);
    }

    @Override
    public void onPlayerToggleSneak(Game game, PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if(repulsors(player) < 1) {
            return;
        }

        useRepulsor(event.getPlayer());

        for(Entity entity : player.getNearbyEntities(6, 6, 6)) {
            if(entity instanceof TNTPrimed || entity instanceof Arrow) {
                continue;
            }

            // Prevent repulsors from pushing spectators.
            if(entity instanceof Player) {
                if(game.spectators().contains((Player) entity)) {
                    continue;
                }
            }

            Location location = player.getLocation();
            location.setY(player.getLocation().getY() - 2.5);
            Vector direction = entity.getLocation().toVector().subtract(location.toVector()).normalize().multiply(new Vector(0.48, 1.15, 0.48));
            entity.setVelocity(entity.getVelocity().add(direction));
        }
    }

    @Override
    public CustomScoreboard scoreboard(Game game, Player player) {
        return new BowSpleefScoreboard(player, game);
    }

    private class BowSpleefScoreboard extends CustomScoreboard {
        private final Game game;

        public BowSpleefScoreboard(Player player, Game game) {
            super(player);
            this.game = game;
            update(player);
        }

        public void update(Player player) {
            ScoreHelper helper;

            if(ScoreHelper.hasScore(player)) {
                helper = ScoreHelper.getByPlayer(player);
            }
            else {
                helper = ScoreHelper.createScore(player);
            }

            switch (game.gameType()) {
                default -> {
                    helper.setTitle("&a&lDuels");

                    helper.setSlot(15, "&7" + DateUtils.currentDateToString());
                    helper.setSlot(14, "");
                    helper.setSlot(13, "&aTime: &f" + game.timer().toString());
                    helper.setSlot(12, "");
                    helper.setSlot(11, "&aKit:");
                    helper.setSlot(10, "  &f" + game.kit().name());
                    helper.setSlot(9, "");
                    helper.setSlot(8, "&aAbilities:");
                    helper.setSlot(7, "  &aDouble Jump: &f" + doubleJumps(player));
                    helper.setSlot(6, "  &aTriple Shot: &f" + tripleShots(player));
                    helper.setSlot(5, "  &aRepulsor: &f" + repulsors(player));
                    helper.removeSlot(4);
                    helper.removeSlot(3);
                    helper.setSlot(2, "");
                    helper.setSlot(1, "&aplay.jadedmc.net");
                }
            }
        }
    }
}