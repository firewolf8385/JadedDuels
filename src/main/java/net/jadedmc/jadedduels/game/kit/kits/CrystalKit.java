package net.jadedmc.jadedduels.game.kit.kits;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.kit.Kit;
import net.jadedmc.jadedduels.utils.PotionBuilder;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

public class CrystalKit extends Kit {

    public CrystalKit(JadedDuelsPlugin plugin) {
        super(plugin, "crystal", "Crystal");

        iconMaterial(Material.END_CRYSTAL);
        gameMode(GameMode.SURVIVAL);
        voidLevel(-64);

        ItemStack enderpearls = new ItemStack(Material.ENDER_PEARL, 16);
        ItemStack experienceBottles = new ItemStack(Material.EXPERIENCE_BOTTLE, 64);
        ItemStack obsidian = new ItemStack(Material.OBSIDIAN, 64);
        ItemStack glowstone = new ItemStack(Material.GLOWSTONE, 64);
        ItemStack respawnAnchors = new ItemStack(Material.RESPAWN_ANCHOR, 64);
        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemStack gapples = new ItemStack(Material.GOLDEN_APPLE, 64);
        ItemStack crystals = new ItemStack(Material.END_CRYSTAL, 64);

        ItemStack arrows = new PotionBuilder(Material.TIPPED_ARROW)
                .setPotionData(PotionType.SLOW_FALLING, false, false)
                .asItemBuilder()
                .setAmount(64)
                .build();

        ItemStack sword = new ItemBuilder(Material.NETHERITE_SWORD)
                .addEnchantment(Enchantment.KNOCKBACK, 1)
                .addEnchantment(Enchantment.MENDING, 1)
                .addEnchantment(Enchantment.DAMAGE_ALL, 5)
                .addEnchantment(Enchantment.SWEEPING_EDGE, 3)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .build();

        ItemStack shield = new ItemBuilder(Material.SHIELD)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .addEnchantment(Enchantment.MENDING, 1)
                .build();

        ItemStack axe = new ItemBuilder(Material.NETHERITE_AXE)
                .addEnchantment(Enchantment.DIG_SPEED, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .addEnchantment(Enchantment.MENDING, 1)
                .build();

        ItemStack pickaxe = new ItemBuilder(Material.NETHERITE_PICKAXE)
                .addEnchantment(Enchantment.DIG_SPEED, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .addEnchantment(Enchantment.MENDING, 1)
                .build();

        ItemStack crossbow = new ItemBuilder(Material.CROSSBOW)
                .addEnchantment(Enchantment.MENDING, 1)
                .addEnchantment(Enchantment.MULTISHOT, 1)
                .addEnchantment(Enchantment.QUICK_CHARGE, 3)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .build();

        ItemStack helmet = new ItemBuilder(Material.NETHERITE_HELMET)
                .addEnchantment(Enchantment.WATER_WORKER, 1)
                .addEnchantment(Enchantment.MENDING, 1)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.OXYGEN, 3)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .build();
        ItemStack chestplate = new ItemBuilder(Material.NETHERITE_CHESTPLATE)
                .addEnchantment(Enchantment.MENDING, 1)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .build();

        ItemStack leggings = new ItemBuilder(Material.NETHERITE_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4)
                .addEnchantment(Enchantment.MENDING, 1)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .build();

        ItemStack boots = new ItemBuilder(Material.NETHERITE_BOOTS)
                .addEnchantment(Enchantment.DEPTH_STRIDER, 3)
                .addEnchantment(Enchantment.PROTECTION_FALL, 4)
                .addEnchantment(Enchantment.MENDING, 1)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .build();

        addItem(0, sword);
        addItem(1, enderpearls);
        addItem(2, gapples);
        addItem(3, obsidian);
        addItem(4, crystals);
        addItem(5, respawnAnchors);
        addItem(6, glowstone);
        addItem(7, crossbow);
        addItem(8, totem);
        addItem(9, enderpearls);
        addItem(10, enderpearls);
        addItem(11, experienceBottles);
        addItem(12, shield);
        addItem(13, axe);
        addItem(14, totem);
        addItem(15, totem);
        addItem(16, totem);
        addItem(17, totem);
        addItem(18, enderpearls);
        addItem(19, enderpearls);
        addItem(20, experienceBottles);
        addItem(21, arrows);
        addItem(22, pickaxe);
        addItem(23, totem);
        addItem(24, totem);
        addItem(25, totem);
        addItem(26, totem);
        addItem(27, enderpearls);
        addItem(28, enderpearls);
        addItem(29, experienceBottles);
        addItem(30, obsidian);
        addItem(31, crystals);
        addItem(32, totem);
        addItem(33, totem);
        addItem(34, totem);
        addItem(35, totem);

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(40, totem);
    }
}
