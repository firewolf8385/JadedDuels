package net.jadedmc.jadedduels.listeners;

import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.Game;
import net.jadedmc.jadedutils.chat.ChatUtils;
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
            return;
        }

        if(!JadedAPI.getPlugin().lobbyManager().isLobbyWorld(player.getWorld())) {
            ChatUtils.chat(player, "<red>Game not found! Sending you back to the lobby.");
            JadedAPI.sendToLobby(player, net.jadedmc.jadedcore.games.Game.DUELS_MODERN);
        }
    }
}