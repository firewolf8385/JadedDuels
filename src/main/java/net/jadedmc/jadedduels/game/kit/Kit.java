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
package net.jadedmc.jadedduels.game.kit;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores all information about a kit.
 */
public class Kit {
    private final JadedDuelsPlugin plugin;

    // Kit metadata
    private final String name;
    private final String id;
    private Material iconMaterial = Material.WOODEN_SWORD;

    // Maps
    private final Map<Integer, ItemStack> items = new HashMap<>();
    private final List<PotionEffect> potionEffects = new ArrayList<>();

    // Settings
    private GameMode gameMode = GameMode.ADVENTURE;
    private double maxHealth = 20.0;
    private double startingHealth = 20.0;
    private int startingHunger = 20;
    private float startingSaturation = 10;

    /**
     * Create a kit.
     * @param name Name of the kit.
     */
    public Kit(final JadedDuelsPlugin plugin, final String id, final String name) {
        this.plugin = plugin;
        this.id = id    ;
        this.name = name;
    }

    /**
     * Add an item to the kit.
     * @param slot Slot item is in.
     * @param item Item to add.
     */
    public void addItem(int slot, ItemStack item) {
        items.put(slot, item);
    }

    /**
     * Add a potion effect to the kit.
     * @param effect Potion effect to add.
     */
    public void addPotionEffect(PotionEffect effect) {
        potionEffects.add(effect);
    }

    /**
     * Apply a kit to a player.
     * @param player Player to apply kit to.
     */
    public void apply(Player player) {
        // Clear inventory.
        player.closeInventory();
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);

        // Clear potion effects.
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        // Give items
        for(int slot : items.keySet()) {
            player.getInventory().setItem(slot, items.get(slot));
        }

        // Set game mode/health/hunger/saturation.
        player.setGameMode(gameMode);
        player.setMaxHealth(maxHealth);

        if(startingHealth > maxHealth) {
            startingHealth = maxHealth;
        }

        player.setHealth(startingHealth);
        player.setFoodLevel(startingHunger);
        player.setSaturation(startingSaturation);

        // Apply potion effects to the kit.
        for(PotionEffect effect : potionEffects) {
            player.addPotionEffect(effect);
        }
    }

    /**
     * Get the Icon Material of the kit,
     * @return Icon Material.
     */
    public Material iconMaterial() {
        return iconMaterial;
    }

    /**
     * Change the Icon material of the kit.
     * @param iconMaterial New Icon material.
     */
    public void iconMaterial(Material iconMaterial) {
        this.iconMaterial = iconMaterial;
    }

    /**
     * Get the id of the kit.
     * @return Kit id.
     */
    public String id() {
        return id;
    }

    /**
     * Get the max health of the kit.
     * @return Kit's max health.
     */
    public double maxHealth() {
        return maxHealth;
    }

    /**
     * Change the max health of the kit.
     * @param maxHealth New max health.
     */
    public void maxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * Get the name of the kit.
     * @return Kit name.
     */
    public String name() {
        return name;
    }

    /**
     * Get the starting health of the kit.
     * @return Starting health.
     */
    public double startingHealth() {
        return startingHealth;
    }

    /**
     * Change the starting health of the kit.
     * @param startingHealth New starting health.
     */
    public void startingHealth(double startingHealth) {
        this.startingHealth = startingHealth;
    }

    /**
     * Get the starting hunger of the kit.
     * @return New starting hunger.
     */
    public int startingHunger() {
        return startingHunger;
    }

    /**
     * Change the starting hunger
     * @param startingHunger New starting health.
     */
    public void startingHunger(int startingHunger) {
        this.startingHunger = startingHunger;
    }
}
