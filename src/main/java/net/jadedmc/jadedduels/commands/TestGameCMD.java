package net.jadedmc.jadedduels.commands;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.GameType;
import net.jadedmc.jadedduels.game.arena.Arena;
import net.jadedmc.jadedduels.game.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestGameCMD extends AbstractCommand {
    private final JadedDuelsPlugin plugin;

    public TestGameCMD(JadedDuelsPlugin plugin) {
        super("testgame", "duels.admin", false);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(args.length == 0) {
            return;
        }

        Kit kit = plugin.kitManager().kit(args[0]);

        if(kit == null) {
            return;
        }

        List<Arena> arenas = new ArrayList<>(plugin.arenaManager().getArenas(kit));
        Collections.shuffle(arenas);

        plugin.gameManager().createGame(arenas.get(0), kit, GameType.UNRANKED).thenAccept(game -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    game.addPlayer(player);
                }

                game.startGame();
            });
        });

    }
}
