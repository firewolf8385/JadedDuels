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
package net.jadedmc.jadedduels.commands;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.arena.Arena;
import net.jadedmc.jadedduels.game.arena.ArenaChunkGenerator;
import net.jadedmc.jadedduels.game.arena.builder.ArenaBuilder;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedduels.utils.FileUtils;
import net.jadedmc.jadedduels.utils.LocationUtils;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;

/**
 * Manages the /arena command, which is used for setting up new arenas.
 */
public class ArenaCMD extends AbstractCommand {
    private final JadedDuelsPlugin plugin;

    /**
     * Creates the command.
     * @param plugin Instance of the plugin.
     */
    public ArenaCMD(final JadedDuelsPlugin plugin) {
        super("arena", "duels.admin", false);
        this.plugin = plugin;
    }


    /**
     * Executes the command.
     * @param sender The Command Sender.
     * @param args Arguments of the command.
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        // Makes sure an argument is given.
        if(args.length == 0) {
            return;
        }

        switch (args[0].toLowerCase()) {
            case "create" -> createCMD(player, args);
            case "setname" -> setNameCMD(player, args);
            case "addkit" -> addKit(player, args);
            case "setvoidlevel" -> setVoidLevel(player, args);
            case "setspectatorspawn" -> setSpectatorSpawn(player);
            case "settournamentmap" -> setTournamentMap(player);
            case "settournamentspawn" -> setTournamentSpawn(player);
            case "addspawn" -> addSpawn(player);
            case "finish" -> finishCMD(player);
            case "edit" -> editCMD(player, args);
        }
    }

    /**
     * Runs the /arena create command.
     * This command starts the arena creation process.
     * @param player Player running the command.
     * @param args Command arguments.
     */
    private void createCMD(Player player, String[] args) {
        if(plugin.arenaManager().arenaBuilder() != null) {
            ChatUtils.chat(player, "&cError &8» &cThere is already an arena being set up.");
            return;
        }

        // Makes sure the command is being used properly.
        if(args.length == 1) {
            ChatUtils.chat(player, "&cUsage &8» &c/arena create [id]");
            return;
        }

        // Gets the arena id.
        String id = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");

        // Starts the arena setup process.
        plugin.arenaManager().arenaBuilder(new ArenaBuilder(plugin));
        plugin.arenaManager().arenaBuilder().id(id);

        // Creates the arena world.
        WorldCreator worldCreator = new WorldCreator(id);
        worldCreator.generator(new ArenaChunkGenerator());
        World world = Bukkit.createWorld(worldCreator);

        // Sets world settings.
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.DISABLE_RAIDS, true);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
        world.setGameRule(GameRule.DO_MOB_LOOT, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        world.setGameRule(GameRule.DO_TILE_DROPS, false);
        world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setClearWeatherDuration(Integer.MAX_VALUE);
        world.setTime(6000);
        world.getWorldBorder().setCenter(world.getSpawnLocation());
        world.getWorldBorder().setSize(210);

        player.setGameMode(GameMode.CREATIVE);
        player.teleport(world.getSpawnLocation());
        player.setFlying(true);

        ChatUtils.chat(player, "&a&lDuels &8» &aCreated an arena with the id &f" + id + "&a.");
        ChatUtils.chat(player, "&a&lDuels &8» &aNext, set the arena name with &f/arena setname [name]&a.");
    }

    /**
     * Runs the /arena setname command.
     * This command sets the name of the arena.
     * @param player Player running the command.
     * @param args Command arguments.
     */
    private void setNameCMD(Player player, String[] args) {
        // Makes sure there an arena is being set up.
        if(plugin.arenaManager().arenaBuilder() == null) {
            ChatUtils.chat(player, "&cError &8» &cYou need to create an arena first! /arena create");
            return;
        }

        // Makes sure the command is being used properly.
        if(args.length == 1) {
            ChatUtils.chat(player, "&cUsage &8» &c/arena setname [name]");
            return;
        }

        // Gets the arena name.
        String name = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");

        // Sets the arena name.
        plugin.arenaManager().arenaBuilder().name(name);
        ChatUtils.chat(player, "&a&lDuels &8» &aArena name set to &f" + name + "&a.");
        ChatUtils.chat(player, "&a&lDuels &8» &aNext, add all allowable modes with &f/arena addkit [kit]&a.");
    }

    /**
     * Runs the /arena addkit command.
     * This command adds to the list of kits that the arena is allowed to be used in.
     * @param player Player running the command.
     * @param args Command arguments.
     */
    private void addKit(Player player, String[] args) {
        // Makes sure there an arena is being set up.
        if(plugin.arenaManager().arenaBuilder() == null) {
            ChatUtils.chat(player, "&cError &8» &cYou need to create an arena first! /arena create");
            return;
        }

        // Makes sure the command is being used properly.
        if(args.length == 1) {
            ChatUtils.chat(player, "&cUsage &8» &c/arena addkit [kit]");
            return;
        }

        Kit kit = plugin.kitManager().kit(args[1]);

        if(kit == null) {
            ChatUtils.chat(player, "&cError &8» &cThat is not a valid kit!");
            return;
        }

        plugin.arenaManager().arenaBuilder().addKit(kit);

        ChatUtils.chat(player, "&a&lDuels &8» &aAdded &f" + args[1] + "&a as a valid kit.");
        ChatUtils.chat(player, "&a&lDuels &8» &aNext, set the spectator spawn with &f/arena setspectatorspawn&a.");
    }

    /**
     * Runs the /arena setvoidlevel command.
     * This command sets the y level in which players should die and respawn.
     * @param player Player running the command.
     * @param args Command arguments.
     */
    private void setVoidLevel(Player player, String[] args) {
        // Makes sure there an arena is being set up.
        if(plugin.arenaManager().arenaBuilder() == null) {
            ChatUtils.chat(player, "&cError &8» &cYou need to create an arena first! /arena create");
            return;
        }

        // Makes sure the command is being used properly.
        if(args.length == 1) {
            ChatUtils.chat(player, "&cUsage &8» &c/arena setvoidlevel [y-level]");
            return;
        }

        // Gets the team size from the command.
        int voidLevel = Integer.parseInt(args[1]);

        // Sets the team size.
        plugin.arenaManager().arenaBuilder().voidLevel(voidLevel);
        ChatUtils.chat(player, "&a&lDuels &8» &aVoid level has been set to &f" + voidLevel + "&a.");
    }

    /**
     * Runs the /arena setspectatorspawn command.
     * This command sets the spectator spawn for the new arena.
     * @param player Player running the command.
     */
    private void setSpectatorSpawn(Player player) {
        // Makes sure there an arena is being set up.
        if(plugin.arenaManager().arenaBuilder() == null) {
            ChatUtils.chat(player, "&cError &8» &cYou need to create an arena first! /arena create");
            return;
        }

        // Sets the waiting area spawn.
        plugin.arenaManager().arenaBuilder().spectatorSpawn(player.getLocation());
        ChatUtils.chat(player, "&a&lDuels &8» &aYou have set the spectator spawn to your location.");
        ChatUtils.chat(player, "&a&lDuels &8» &aNext, add your spawns with &f/arena addspawn.");
    }

    private void setTournamentMap(Player player) {
        // Makes sure there an arena is being set up.
        if(plugin.arenaManager().arenaBuilder() == null) {
            ChatUtils.chat(player, "&cError &8» &cYou need to create an arena first! /arena create");
            return;
        }

        plugin.arenaManager().arenaBuilder().tournamentMap(true);
        ChatUtils.chat(player, "&a&lDuels &8» &aSet the map as a tournament map.");
        ChatUtils.chat(player, "&a&lDuels &8» &aSet the tournament spawn with &f/arena settournamentspawn&a.");
    }

    private void setTournamentSpawn(Player player) {
        // Makes sure there an arena is being set up.
        if(plugin.arenaManager().arenaBuilder() == null) {
            ChatUtils.chat(player, "&cError &8» &cYou need to create an arena first! /arena create");
            return;
        }

        // Sets the waiting area spawn.
        plugin.arenaManager().arenaBuilder().tournamentSpawn(player.getLocation());
        ChatUtils.chat(player, "&a&lDuels &8» &aYou have set the tournament spawn to your location.");
        ChatUtils.chat(player, "&a&lDuels &8» &aNext, add your spawns with &f/arena addspawn.");
    }

    /**
     * Runs the /arena addspawn command.
     * This commands adds team spawn points to the new arena.
     * @param player Player running the command.
     */
    private void addSpawn(Player player) {
        // Makes sure there an arena is being set up.
        if(plugin.arenaManager().arenaBuilder() == null) {
            ChatUtils.chat(player, "&cError &8» &cYou need to create an arena first! /arena create");
            return;
        }

        plugin.arenaManager().arenaBuilder().addSpawn(player.getLocation());

        int spawnCount = plugin.arenaManager().arenaBuilder().spawns().size();
        ChatUtils.chat(player, "&a&lDuels &8» &aSet spawn point &f#" + spawnCount + "&a to your location.");

        if(spawnCount == 2) {
            ChatUtils.chat(player, "&a&lDuels &8» &aWhen you are done, finish the setup with &f/arena finish.");
        }
    }

    /**
     * Runs the /arena finish command.
     * This command checks if the arena is done and saves it if so.
     * @param player Player running the command.
     */
    private void finishCMD(Player player) {
        // Makes sure there an arena is being set up.
        if(plugin.arenaManager().arenaBuilder() == null) {
            ChatUtils.chat(player, "&cError &8» &cYou need to create an arena first! /arena create");
            return;
        }

        // Warn the player if setup is not complete.
        if(!plugin.arenaManager().arenaBuilder().isSet()) {
            ChatUtils.chat(player, "&cError &8» &cSetup not complete!");
            return;
        }

        ChatUtils.chat(player, "&a&lDuels &8» &aArena has been saved.");

        // Saves the arena.
        String id = plugin.arenaManager().arenaBuilder().id();
        plugin.arenaManager().arenaBuilder().save();

        // Remove all players from the world.
        World world = player.getWorld();
        String worldID = world.getName();
        File worldFolder = world.getWorldFolder();
        for(Player worldPlayer : world.getPlayers()) {
            worldPlayer.teleport(LocationUtils.getSpawn(plugin));
        }

        Bukkit.unloadWorld(world,true);

        // Saves the world where it belongs.
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            // Load applicable folders.
            File mapsFolder = new File(worldFolder.getParentFile(), "maps");
            File savedWorldFolder = new File(mapsFolder, worldID);

            // Delete the old save if in edit mode.
            if(plugin.arenaManager().arenaBuilder().editMode()) {
                FileUtils.deleteDirectory(savedWorldFolder);
            }

            // Copies the world to the maps folder.
            FileUtils.copyFileStructure(worldFolder, savedWorldFolder);

            // Deletes the previous world.
            FileUtils.deleteDirectory(worldFolder);

            plugin.getServer().getScheduler().runTask(plugin, () -> plugin.arenaManager().loadArena(id));
            plugin.arenaManager().arenaBuilder(null);
        });
    }

    /**
     * Runs the /arena edit command.
     * This command edits an existing arena.
     * @param player Player running the command.
     * @param args Command arguments.
     */
    private void editCMD(Player player, String[] args) {
        if(plugin.arenaManager().arenaBuilder() != null) {
            ChatUtils.chat(player, "&cError &8» &cThere is already an arena being set up.");
            return;
        }

        // Makes sure the command is being used properly.
        if(args.length == 1) {
            ChatUtils.chat(player, "&cUsage &8» &c/arena edit [id]");
            return;
        }

        // Gets the arena id.
        String id = args[1];
        System.out.println(id);

        // Makes sure the arena exists.
        if(plugin.arenaManager().getArena(id) == null) {
            ChatUtils.chat(player, "&cError &8» &cThat arena does not exist!");
            return;
        }

        Arena arena = plugin.arenaManager().getArena(id);
        arena.arenaFile().createCopy(arena.id()).thenAccept(file -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                plugin.arenaManager().arenaBuilder(new ArenaBuilder(plugin, arena));

                World world = Bukkit.createWorld(new WorldCreator(arena.id()));
                player.setGameMode(GameMode.CREATIVE);
                player.teleport(world.getSpawnLocation());
                player.setFlying(true);

                ChatUtils.chat(player, "&a&lDuels &8» &aYou are now editing &f" + arena.name() + "&a.");
                ChatUtils.chat(player, "&a&lDuels &8» &aWhen you are done, finish the arena with &f/arena finish&a.");
            });
        });
    }
}