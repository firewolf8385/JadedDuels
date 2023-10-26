package net.jadedmc.jadedduels.commands;

import net.jadedmc.jadedcore.utils.gui.CustomGUI;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedduels.game.tournament.BestOf;
import net.jadedmc.jadedduels.game.tournament.EventStatus;
import net.jadedmc.jadedduels.game.tournament.EventType;
import net.jadedmc.jadedutils.chat.ChatUtils;
import net.jadedmc.jadedutils.items.ItemBuilder;
import net.jadedmc.jadedutils.items.SkullBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class CreateCMD extends AbstractCommand {
    private final JadedDuelsPlugin plugin;

    public CreateCMD(JadedDuelsPlugin plugin) {
        super("create", "tournament.use", false);
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {

        // Make sure no other tournaments are currently running.
        if(plugin.duelEventManager().activeEvent() != null) {
            ChatUtils.chat(sender, "&c&lError &8» &cThere is already a tournament active!");
            return;
        }

        Player player = (Player) sender;
        plugin.duelEventManager().host(player);
        new SetKitGUI().open(player);

        // Update scoreboard
        // TODO: Tournament scoreboard. Bukkit.getOnlinePlayers().forEach(p -> new EventScoreboard(plugin, p));
    }

    private class SetKitGUI extends CustomGUI {
        public SetKitGUI() {
            super(54, "Select A Kit");
            addFiller(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

            int i = 0;
            for(Kit kit : plugin.kitManager().kits()) {
                setItem(i + 9, new ItemBuilder(kit.iconMaterial())
                                .setDisplayName("<green><bold>" + kit.name())
                                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                                .addLore("").addLore("<green>Click to Select!").build(),
                        (p, a) -> {
                            plugin.duelEventManager().kit(kit);
                            new SetBracketGUI().open(p);
                        });
                i++;
            }
        }
    }

    private class SetBracketGUI extends CustomGUI {
        public SetBracketGUI() {
            super(54, "Select Bracket");
            addFiller(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

            ItemStack singleElim = new SkullBuilder("6d65ce83f1aa5b6e84f9b233595140d5b6beceb62b6d0c67d1a1d83625ffd")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Single Elimination")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(20, singleElim, (p,a) -> {
                plugin.duelEventManager().eventType(EventType.SINGLE_ELIMINATION);
                new SetBestOfGUI().open(p);
            });

            ItemStack doubleElim = new SkullBuilder("dd54d1f8fbf91b1e7f55f1bdb25e2e33baf6f46ad8afbe08ffe757d3075e3")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Double Elimination")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(24, doubleElim, (p,a) -> {
                plugin.duelEventManager().eventType(EventType.DOUBLE_ELIMINATION);
                new SetBestOfGUI().open(p);
            });
        }
    }

    private class SetBestOfGUI extends CustomGUI {
        public SetBestOfGUI() {
            super(54, "Select Best Of");
            addFiller(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

            ItemStack bo1 = new SkullBuilder("6d65ce83f1aa5b6e84f9b233595140d5b6beceb62b6d0c67d1a1d83625ffd")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Best of 1")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(19, bo1, (p,a) -> {
                plugin.duelEventManager().bestOf(BestOf.ONE);
                plugin.duelEventManager().eventStatus(EventStatus.WAITING);
                p.closeInventory();

                // TODO: Update Bungeecord.
            });

            ItemStack bo3 = new SkullBuilder("21e4ea59b54cc99416bc9f624548ddac2a38eea6a2dbf6e4ccd83cec7ac969")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Best of 3")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(21, bo3, (p,a) -> {
                plugin.duelEventManager().bestOf(BestOf.THREE);
                plugin.duelEventManager().eventStatus(EventStatus.WAITING);
                p.closeInventory();
                // TODO: Update Bungeecord.
            });

            ItemStack bo5 = new SkullBuilder("84c8c3710da2559a291adc39629e9ccea31ca9d3d3586bfea6e6e06124b3c")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Best of 5")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(23, bo5, (p,a) -> {
                plugin.duelEventManager().bestOf(BestOf.FIVE);
                plugin.duelEventManager().eventStatus(EventStatus.WAITING);
                p.closeInventory();

                // TODO: Update Bungeecord.
            });

            ItemStack bo7 = new SkullBuilder("24bde79f84fc5f3f1fbc5bc01071066bd20cd263a1654d64d60d84248ba9cd")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Best of 7")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(25, bo7, (p,a) -> {

                plugin.duelEventManager().bestOf(BestOf.SEVEN);
                plugin.duelEventManager().eventStatus(EventStatus.WAITING);
                p.closeInventory();

                // TODO: Update Bungeecord.
            });
        }
    }
}