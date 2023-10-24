package net.jadedmc.jadedduels.commands;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.gui.KitGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitsCMD extends AbstractCommand {
    private final JadedDuelsPlugin plugin;

    public KitsCMD(JadedDuelsPlugin plugin) {
        super("kits", "", false);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        new KitGUI(plugin).open(player);
    }
}
