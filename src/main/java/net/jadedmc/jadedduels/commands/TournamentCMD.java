package net.jadedmc.jadedduels.commands;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.lobby.LobbyUtils;
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

        LobbyUtils.sendToTournamentLobby(plugin, player);
    }
}
