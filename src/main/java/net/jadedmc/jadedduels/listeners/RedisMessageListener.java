package net.jadedmc.jadedduels.listeners;

import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedcore.events.RedisMessageEvent;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import redis.clients.jedis.Jedis;

import java.util.*;

public class RedisMessageListener implements Listener {
    private final JadedDuelsPlugin plugin;

    public RedisMessageListener(final JadedDuelsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMessage(RedisMessageEvent event) {
        if(event.getChannel().equalsIgnoreCase("game")) {
            String[] args = event.getMessage().split(" ");

            if(args.length > 2) {
                return;
            }

            switch(args[0].toLowerCase()) {
                case "create" -> {
                    plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                       String gameUUID = args[1];

                       try(Jedis jedis = JadedAPI.getRedis().jedisPool().getResource()) {
                           Document document = Document.parse(jedis.get("games:" + gameUUID));

                           if(!document.getString("server").equalsIgnoreCase(JadedAPI.getServerName())) {
                                return;
                           }

                           plugin.gameManager().fromDocument(document).whenComplete((result, error) -> error.printStackTrace());
                       }
                    });
                }

                case "setup" -> {
                    plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                        String gameUUID = args[1];

                        System.out.println("Setup received: " + gameUUID);

                        try(Jedis jedis = JadedAPI.getRedis().jedisPool().getResource()) {
                            Collection<String> playerUUIDs = new HashSet<>();

                            Document document = Document.parse(jedis.get("games:" + gameUUID));
                            String serverName = document.getString("server");

                            if(serverName.equalsIgnoreCase(JadedAPI.getServerName())) {
                                return;
                            }


                            Document teamsDocument = document.get("teams", Document.class);
                            Set<String> teamsList = teamsDocument.keySet();
                            System.out.println(teamsList);

                            for(String team : teamsList) {
                                Document teamDocument = teamsDocument.get(team, Document.class);
                                playerUUIDs.addAll(teamDocument.getList("uuids", String.class));
                            }

                            plugin.getServer().getScheduler().runTask(plugin, () -> {
                                for(Player player : plugin.getServer().getOnlinePlayers()) {
                                    if(!playerUUIDs.contains(player.getUniqueId().toString())) {
                                        continue;
                                    }

                                    JadedAPI.sendBungeecordMessage(player, "BungeeCord", "Connect", serverName);
                                }
                            });
                        }
                    });
                }
            }
        }
    }
}
