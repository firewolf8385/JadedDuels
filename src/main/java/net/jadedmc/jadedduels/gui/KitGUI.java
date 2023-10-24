package net.jadedmc.jadedduels.gui;

import net.jadedmc.jadedcore.utils.gui.CustomGUI;
import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.GameType;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class KitGUI extends CustomGUI {

    public KitGUI(JadedDuelsPlugin plugin) {
        super(27, "Kits");
        addFiller(0,1,2,3,4,5,6,7,8,9,10,17,18,19,20,21,22,23,24,25,26);

        int[] iconSlots = new int[]{10,11,12,13,14,15,16};
        int i = 0;
        for(Kit kit : plugin.kitManager().kits()) {

            ItemStack item = new ItemBuilder(kit.iconMaterial(), 1)
                    .setDisplayName("<green>" + kit.name())
                    .addLore("<gray>Playing: " + plugin.queueManager().getPlaying(kit))
                    .addLore("<gray>Queuing: " + plugin.queueManager().getQueueing(kit, GameType.UNRANKED))
                    .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .build();

            setItem(iconSlots[i], item, (pl, a) -> {
                pl.closeInventory();
                plugin.queueManager().addPlayer(pl, kit, GameType.UNRANKED);
            });
            i++;
        }
    }
}