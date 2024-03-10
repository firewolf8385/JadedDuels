package net.jadedmc.jadedduels.listeners;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final JadedDuelsPlugin plugin;

    public PlayerJoinListener(final JadedDuelsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        for(Game game : plugin.gameManager().games()) {
            if(!game.players().contains(player)) {
                continue;
            }

            game.addPlayer(player);
        }
    }
}