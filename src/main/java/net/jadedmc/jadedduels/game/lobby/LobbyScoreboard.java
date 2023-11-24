package net.jadedmc.jadedduels.game.lobby;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.utils.DateUtils;
import net.jadedmc.jadedduels.utils.scoreboard.CustomScoreboard;
import net.jadedmc.jadedduels.utils.scoreboard.ScoreHelper;
import org.bukkit.entity.Player;

/**
 * This class creates and displays the lobby scoreboard.
 */
public class LobbyScoreboard extends CustomScoreboard {
    private final JadedDuelsPlugin plugin;

    /**
     * Links the player with the scoreboard.
     * @param plugin Instance of the plugin.
     * @param player Player to create scoreboard for.
     */
    public LobbyScoreboard(JadedDuelsPlugin plugin, Player player) {
        super(player);
        this.plugin = plugin;

        CustomScoreboard.getPlayers().put(player.getUniqueId(), this);
        update(player);
    }

    /**
     * Updates the scoreboard for a specific player.
     * @param player Player to update scoreboard for.
     */
    public void update(Player player) {
        ScoreHelper helper;

        if(ScoreHelper.hasScore(player)) {
            helper = ScoreHelper.getByPlayer(player);
        }
        else {
            helper = ScoreHelper.createScore(player);
        }

        // Sets up the scoreboard.
        helper.setTitle("&a&lDuels");
        helper.setSlot(15, "&7" + DateUtils.currentDateToString());
        helper.setSlot(14, "");
        helper.setSlot(13, "&aPlaying:");
        helper.setSlot(12, "  &aDuels: &f" + plugin.gameManager().playing());
        helper.setSlot(11, "  &aTournament: &f" + plugin.duelEventManager().players().size());
        helper.setSlot(10, "  &aQueue: &f" + plugin.queueManager().getQueueing());
        helper.setSlot(9, "");
        helper.removeSlot(8);
        helper.removeSlot(7);
        helper.removeSlot(6);
        helper.removeSlot(5);
        helper.removeSlot(4);
        helper.setSlot(3, "&aWins: &f0");
        helper.setSlot(2, "");
        helper.setSlot(1, "&aplay.jadedmc.net");
    }
}