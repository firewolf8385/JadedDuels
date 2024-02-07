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

import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.SettingsManager;
import net.jadedmc.jadedduels.game.arena.Arena;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Rotatable;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Stores data of an arena that is still being set up.
 */
public class ArenaBuilder {
    private final JadedDuelsPlugin plugin;
    private String spectatorSpawn = null;
    private String name;
    private String builders;
    private String id;
    private int voidLevel = -1;
    private final Collection<String> kits = new HashSet<>();
    private final List<String> spawns = new ArrayList<>();
    private boolean editMode = false;
    private String tournamentSpawn = null;
    private final World world;

    /**
     * Creates the arena builder.
     * @param plugin Instance of the plugin.
     */
    public ArenaBuilder(final JadedDuelsPlugin plugin, World world) {
        this.plugin = plugin;
        this.world = world;
    }

    /**
     * Creates an arena builder using an existing arena.
     * Used to edit the existing arena.
     * @param plugin Instance of the plugin.
     * @param arena Arena to be edited.
     */
    public ArenaBuilder(final JadedDuelsPlugin plugin, Arena arena, World world) {
        this.plugin = plugin;
        this.world = world;
        this.id = arena.fileName();
        this.builders = arena.builders();
        this.spectatorSpawn = arena.spectatorSpawnRaw();
        this.voidLevel = arena.voidLevel();
        this.name = arena.name();

        kits.addAll(arena.kitsRaw());
        editMode = true;

        spawns.addAll(arena.spawnsRaw());
    }

    /**
     * Adds a supported kit to the arena.
     * @param kit Kit to add.
     */
    public void addKit(String kit) {
        kits.add(kit);
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

    public boolean isSet() {
        spawns.clear();

        // Loop through all spawn locations.
        System.out.println("Loaded Chunks: " + world.getLoadedChunks().length);
        for(Chunk chunk : world.getLoadedChunks()) {
            World world = chunk.getWorld();
            for(int x = 0; x < 16; x++) {
                for(int y = 0; y < (world.getMaxHeight() - 1); y++) {
                    for(int z = 0; z < 16; z++) {
                        Block block = chunk.getBlock(x, y, z);

                        if(block.getType() == Material.OAK_SIGN) {
                            Sign sign = (Sign) block.getState();
                            String[] lines = sign.getLines();

                            if(lines.length < 2) {
                                System.out.println("Sign found with not enough lines!");
                                continue;
                            }

                            if(!lines[0].toLowerCase().equalsIgnoreCase("[Spawn]")) {
                                System.out.println(lines[0]);
                                continue;
                            }

                            Rotatable rotatable = (Rotatable) sign.getBlockData();
                            Vector vector = rotatable.getRotation().getDirection();
                            final double _2PI = 2 * Math.PI;
                            final double signX = vector.getX();
                            final double signZ = vector.getZ();

                            double theta = Math.atan2(-signX, signZ);
                            float yaw = (float) Math.toDegrees((theta + _2PI) % _2PI);

                            String locationString = "world," + block.getX() + "," + block.getY() + "," + block.getZ() + "," + yaw + ",0";
                            System.out.println("Sign Found: " + locationString + ": " + lines[1]);


                            switch(lines[1].toLowerCase()) {
                                case "tournament" -> tournamentSpawn = locationString;
                                case "spectate" -> spectatorSpawn = locationString;
                                default -> spawns.add(locationString);
                            }
                        }
                    }
                }
            }
        }

        if(spectatorSpawn == null) {
            System.out.println("No Spectator Spawn Found");
            return false;
        }

        if(spawns.size() < 2) {
            System.out.println("Not Enough Spawns! Found" + spawns.size());
            return false;
        }

        return true;
    }

    /**
     * Gets all kits the arena is set for.
     * @return All kits.
     */
    public Collection<String> kits() {
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
     * Set the void level of the arena.
     * @param voidLevel Arena void level.
     */
    public void voidLevel(int voidLevel) {
        this.voidLevel = voidLevel;
    }

    public void save() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            Document document = new Document("fileName", id)
                    .append("name", name)
                    .append("builders", builders)
                    .append("voidLevel", voidLevel)
                    .append("kits", kits).append("spectatorSpawn", spectatorSpawn)
                    .append("spawns", spawns);

            if(tournamentSpawn != null) {
                document.append("tournamentSpawn", tournamentSpawn);
            }

            // Add the document to MongoDB.
            if(!editMode) {
                JadedAPI.getMongoDB().client().getDatabase("duels").getCollection("maps").insertOne(document);
            }
            else {
                // Replaces the existing file.
                Document old = JadedAPI.getMongoDB().client().getDatabase("duels").getCollection("maps").find(eq("fileName", id)).first();
                JadedAPI.getMongoDB().client().getDatabase("duels").getCollection("maps").replaceOne(old, document);
            }

            File worldFolder = world.getWorldFolder();

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                world.getPlayers().forEach(player -> JadedAPI.getPlugin().lobbyManager().sendToLobby(player));
                Bukkit.unloadWorld(world, true);

                // Saves the world to MongoDB.
                JadedAPI.getPlugin().worldManager().saveWorld(worldFolder, id);

                // Load the new arena.
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                    plugin.arenaManager().loadArena(id);
                });
            });
        });
    }
}