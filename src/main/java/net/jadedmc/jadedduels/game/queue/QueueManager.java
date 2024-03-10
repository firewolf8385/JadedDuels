package net.jadedmc.jadedduels.game.queue;

import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.GameType;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedutils.Timer;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class QueueManager {
    private final JadedDuelsPlugin plugin;
    private final Map<Kit, Map<Player, Timer>> queue = new HashMap<>();

    public QueueManager(final JadedDuelsPlugin plugin) {
        this.plugin = plugin;
    }

    public void addToQueue(Player player, Kit kit) {
        UUID uuid = player.getUniqueId();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try(Jedis jedis = JadedAPI.getRedis().jedisPool().getResource()) {
                String otherPlayer = jedis.get("queue:" + kit.id());

                if(otherPlayer == null) {
                    jedis.set("queue:" + kit.id(), uuid.toString());

                    if(queue.get(kit) != null) {
                        Timer timer = new Timer(plugin);
                        timer.start();
                        queue.get(kit).put(player, timer);
                    }
                    else {
                        Map<Player, Timer> kitQueue = new HashMap<>();
                        Timer timer = new Timer(plugin);
                        timer.start();
                        kitQueue.put(player, timer);
                        queue.put(kit, kitQueue);
                    }

                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        new QueueScoreboard(plugin, player).addPlayer(player);
                        player.getInventory().setItem(6, new ItemBuilder(Material.RED_BED).setDisplayName("&cLeave Queue").build());
                    });
                    return;
                }

                jedis.del("queue:" + kit.id());
                plugin.gameManager().createGame(kit, GameType.RANKED, List.of(UUID.fromString(otherPlayer)), List.of(uuid));
            }
        });
    }

    public Kit getKit(Player player) {
        for(Kit kit : queue.keySet()) {
            if(queue.get(kit).containsKey(player)) {
                return kit;
            }
        }

        return null;
    }

    public Timer getTimer(Player player) {
        for(Kit kit : queue.keySet()) {
            if(queue.get(kit).containsKey(player)) {
                return queue.get(kit).get(player);
            }
        }

        return null;
    }

    public void leaveQueue(Player player) {
        Kit kit = getKit(player);

        if(kit == null) {
            return;
        }

        queue.get(kit).remove(player);
        JadedAPI.getPlugin().lobbyManager().getLobbyScoreboard(player).addPlayer(player);
        player.getInventory().clear(6);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try(Jedis jedis = JadedAPI.getRedis().jedisPool().getResource()) {
                jedis.del("queue:" + kit.id());
            }
        });
    }

    public void removePlayer(Player player) {
        leaveQueue(player);

        for(Kit kit : queue.keySet()) {
            queue.get(kit).remove(player);
        }
    }
}
