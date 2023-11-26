package net.jadedmc.jadedduels.commands;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.tournament.EventStatus;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCMD extends AbstractCommand {
    private final JadedDuelsPlugin plugin;

    public StartCMD(JadedDuelsPlugin plugin) {
        super("start", "tournament.use", false);

        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        // Make sure no other tournaments are currently running.
        if(plugin.duelEventManager().activeEvent() != null) {
            ChatUtils.chat(sender, "&c&lError &8» &cThere is already a tournament active!");
            return;
        }

        // Make sure a tournament exists before starting one.
        if(plugin.duelEventManager().eventStatus() != EventStatus.WAITING) {
            ChatUtils.chat(sender, "&c&lError &8» &cYou have to create a tournament first!!");
            return;
        }

        int playingCount = plugin.duelEventManager().world().getPlayerCount();
        if(!plugin.duelEventManager().hostPlaying()) {
           playingCount--;
        }

        // Make sure there are enough players to start a tournament.
        if(playingCount < plugin.duelEventManager().teamSize().minimumPlayers()) {
            ChatUtils.chat(sender, "&cError &8» &cThere are not enough players to start!");
            return;
        }

        Player player = (Player) sender;

        // Make sure the host is the one running the command.
        if(!player.equals(plugin.duelEventManager().host())) {
            ChatUtils.chat(sender, "&cError &8» &cOnly the host can run that command!!");
            return;
        }

        // Update bungeecord
        // TODO: Update bungeecord.

        ChatUtils.broadcast(plugin.duelEventManager().world(), "&a&lTournament &8» &aGenerating Brackets");
        plugin.duelEventManager().create();
    }
}