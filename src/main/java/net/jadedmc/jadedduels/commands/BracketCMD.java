package net.jadedmc.jadedduels.commands;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.command.CommandSender;

public class BracketCMD extends AbstractCommand {
    private final JadedDuelsPlugin plugin;

    public BracketCMD(JadedDuelsPlugin plugin) {
        super("bracket", "", true);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(plugin.duelEventManager().activeEvent() == null) {
            ChatUtils.chat(sender, "&c&lError &8» &cThere is no tournament currently active!");
            return;
        }

        String url = "https://challonge.com/" + plugin.duelEventManager().activeEvent().tournament().getUrl();
        ChatUtils.chat(sender, "&a&lTournament&8» &aBracket: &f<click:open_url:'" + url + "'>" + url + "</click>");
    }
}
