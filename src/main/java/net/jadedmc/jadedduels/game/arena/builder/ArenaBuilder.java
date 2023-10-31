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
package net.jadedmc.jadedduels.game.arena.builder;

import net.jadedmc.jadedduels.game.arena.Arena;
import net.jadedmc.jadedduels.game.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Stores data of an arena that is still being set up.
 */
public class ArenaBuilder {
    private final Plugin plugin;
    private Location spectatorSpawn;
    private String name;
    private String builders;
    private String id;
    private int voidLevel = -1;
    private final Collection<Kit> kits = new HashSet<>();
    private final List<Location> spawns = new ArrayList<>();
    private boolean editMode = false;
    private boolean tournamentMap = false;
    private Location tournamentSpawn = null;

    /**
     * Creates the arena builder.
     * @param plugin Instance of the plugin.
     */
    public ArenaBuilder(final Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates an arena builder using an existing arena.
     * Used to edit the existing arena.
     * @param plugin Instance of the plugin.
     * @param arena Arena to be edited.
     */
    public ArenaBuilder(final Plugin plugin, Arena arena) {
        this.plugin = plugin;
        this.id = arena.id();
        this.builders = arena.builders();
        this.spectatorSpawn = arena.spectatorSpawn(Bukkit.getWorld(id));
        this.voidLevel = arena.voidLevel();
        this.name = arena.name();

        kits.addAll(arena.kits());
        editMode = true;

        spawns.addAll(arena.spawns(Bukkit.getWorld(id)));
    }

    /**
     * Adds a supported kit to the arena.
     * @param kit Kit to add.
     */
    public void addKit(Kit kit) {
        kits.add(kit);
    }

    /**
     * Adds a team spawn to the arena.
     * @param location Spawn to add.
     */
    public void addSpawn(Location location) {
        spawns.add(location);
    }

    /**
     * Set the builders of the arena.
     * @param builders Arena builders.
     */
    public void builders(String builders) {
        this.builders = builders;
    }

    /**
     * Get if the arena builder is in edit mode.
     * @return If in edit mode.
     */
    public boolean editMode() {
        return editMode;
    }

    /**
     * Get the id of the arena being created.
     * @return Arena id.
     */
    public String id() {
        return id;
    }

    /**
     * Set the id of the arena.
     * @param id Arena id.
     */
    public void id(String id) {
        this.id = id;
    }

    /**
     * Checks if the arena is ready to be saved.
     * @return  Whether the arena can be saved.
     */
    public boolean isSet() {
        // Make sure the id is set.
        if(id == null) {
            System.out.println("ID not set");
            return false;
        }

        // Make sure the name is set.
        if(name == null) {
            System.out.println("name not set");
            return false;
        }

        // Make sure modes are set.
        if(kits.size() == 0) {
            System.out.println("kits not set");
            return false;
        }

        // Make sure the waiting area is set.
        if(spectatorSpawn == null) {
            System.out.println("waiting area not set");
            return false;
        }

        // Make sure the spawns have been set.
        if(spawns.size() < 2) {
            System.out.println("Spawns not set.");
            return false;
        }

        // Make sure the tournament spawn has been set if needed.
        if(tournamentMap && tournamentSpawn == null) {
            System.out.println("Tournament spawn not set.");
            return false;
        }

        return true;
    }

    /**
     * Gets all kits the arena is set for.
     * @return All kits.
     */
    public Collection<Kit> kits() {
        return kits;
    }

    /**
     * Set the name of the arena.
     * @param name Arena's name.
     */
    public void name(String name) {
        this.name = name;
    }

    /**
     * Gets all spawns the arena currently has.
     * @return All spawns.
     */
    public List<Location> spawns() {
        return spawns;
    }

    /**
     * Sets the location where spectators should spawn.
     * @param spectatorSpawn Spawn point for spectators.
     */
    public void spectatorSpawn(Location spectatorSpawn) {
        this.spectatorSpawn = spectatorSpawn;
    }

    /**
     * Set if the map should be a tournament map.
     * @param tournamentMap Whether the map is a tournament map.
     */
    public void tournamentMap(boolean tournamentMap) {
        this.tournamentMap = tournamentMap;
    }

    /**
     * Set the tournament spawn.
     * @param tournamentSpawn New tournament spawn.
     */
    public void tournamentSpawn(Location tournamentSpawn) {
        this.tournamentSpawn = tournamentSpawn;
    }

    /**
     * Set the void level of the arena.
     * @param voidLevel Arena void level.
     */
    public void voidLevel(int voidLevel) {
        this.voidLevel = voidLevel;
    }

    /**
     * Saves the Arena to a configuration file.
     */
    public void save() {
       try {
           File file = new File(plugin.getDataFolder(), "/arenas/" + id + ".yml");
           if(file.exists()) {
               file.delete();
           }

           file.createNewFile();

           FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
           configuration.set("name", name);
           configuration.set("builders", builders);
           configuration.set("tournamentMap", tournamentMap);

           if(voidLevel != -1) {
               configuration.set("voidLevel", voidLevel);
           }

           // Spectator Spawn Location
           {
               ConfigurationSection waitingSection = configuration.createSection("spectatorSpawn");
               waitingSection.set("world", Bukkit.getWorlds().get(0).getName());
               waitingSection.set("x", spectatorSpawn.getX());
               waitingSection.set("y", spectatorSpawn.getY());
               waitingSection.set("z", spectatorSpawn.getZ());
               waitingSection.set("yaw", spectatorSpawn.getYaw());
               waitingSection.set("pitch", spectatorSpawn.getPitch());
           }

           // Tournament Spawn
           if(tournamentMap) {
               ConfigurationSection waitingSection = configuration.createSection("tournamentSpawn");
               waitingSection.set("world", Bukkit.getWorlds().get(0).getName());
               waitingSection.set("x", tournamentSpawn.getX());
               waitingSection.set("y", tournamentSpawn.getY());
               waitingSection.set("z", tournamentSpawn.getZ());
               waitingSection.set("yaw", tournamentSpawn.getYaw());
               waitingSection.set("pitch", tournamentSpawn.getPitch());
           }

           // Kits
           {
               List<String> kitStrings = new ArrayList<>();
               for(Kit kit : kits) {
                   kitStrings.add(kit.id());
               }

               configuration.set("kits", kitStrings);
           }

           // Team Spawns
           {
               ConfigurationSection teamSpawnsSection = configuration.createSection("teamSpawns");

               int i = 1;
               for(Location location : spawns) {
                   ConfigurationSection spawnSection = teamSpawnsSection.createSection("" + i);
                   spawnSection.set("world", Bukkit.getWorlds().get(0).getName());
                   spawnSection.set("x", location.getX());
                   spawnSection.set("y", location.getY());
                   spawnSection.set("z", location.getZ());
                   spawnSection.set("yaw", location.getYaw());
                   spawnSection.set("pitch", location.getPitch());

                   i++;
               }
           }

           // Saves the file.
           configuration.save(file);
       }
       catch (IOException exception) {
           exception.printStackTrace();
       }
    }
}
