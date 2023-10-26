package net.jadedmc.jadedduels.commands;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TournamentCMD extends AbstractCommand {
    private final JadedDuelsPlugin plugin;

    public TournamentCMD(JadedDuelsPlugin plugin) {
        super("tournament", "", false);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        player.teleport(plugin.duelEventManager().world().getSpawnLocation());
    }
}
