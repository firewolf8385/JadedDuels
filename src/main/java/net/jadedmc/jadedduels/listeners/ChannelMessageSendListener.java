package net.jadedmc.jadedduels.listeners;

import net.jadedmc.jadedchat.features.channels.events.ChannelMessageSendEvent;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.Game;
import net.jadedmc.jadedduels.game.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class ChannelMessageSendListener implements Listener {
    private final JadedDuelsPlugin plugin;

    public ChannelMessageSendListener(JadedDuelsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMessage(ChannelMessageSendEvent event) {

        switch (event.getChannel().name().toUpperCase()) {
            case "GAME" -> {
                gameChannel(event);
            }

            case "TEAM" -> {
                teamChannel(event);
            }
        }
    }

    private void gameChannel(ChannelMessageSendEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.gameManager().game(player);

        if(game == null) {
            event.setCancelled(true);
            return;
        }

        List<Player> viewers = new ArrayList<>(game.players());
        viewers.addAll(game.spectators());

        event.setViewers(viewers);
    }

    private void teamChannel(ChannelMessageSendEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.gameManager().game(player);

        if(game == null) {
            event.setCancelled(true);
            return;
        }

        Team team = game.teamManager().team(player);
        if(team == null) {
            event.setCancelled(true);
            return;
        }

        List<Player> viewers = new ArrayList<>(team.players());

        event.setViewers(viewers);
    }
}