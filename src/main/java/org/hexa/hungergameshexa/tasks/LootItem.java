package org.hexa.hungergameshexa.tasks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hexa.hungergameshexa.HungerGamesHexa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LootItem {
    public final Material material;
    private final String name;
    private final List<String> lore;
    private final Map<Enchantment, Integer> enchantmentToLevelMap = new HashMap<>();
    private final double chance;
    private final int minAmount;
    private final int maxAmount;
    private final Integer customModelData;

    public LootItem(ConfigurationSection section){
        Material material;

        try {
            material = Material.valueOf(section.getString("material"));
        } catch (Exception e) {
            material = Material.AIR;
        }
        this.material = material;
        this.name = section.getString("name");
        this.lore = section.getStringList("lore");

        ConfigurationSection enchantmentsSection = section.getConfigurationSection("enchantments");
        if (enchantmentsSection != null) {
            for (String enchantmentKey : enchantmentsSection.getKeys(false)) {
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentKey.toLowerCase()));
                if (enchantment != null) {
                    int level = enchantmentsSection.getInt(enchantmentKey);
                    enchantmentToLevelMap.put(enchantment, level);
                }
            }
        }
        this.chance = section.getDouble("chance");
        this.minAmount = section.getInt("minAmount");
        this.maxAmount = section.getInt("maxAmount");
        if (section.contains("customModelData")) {
            this.customModelData = section.getInt("customModelData");
        } else {
            this.customModelData = null;
        }
    }

    public boolean shouldFill(Random random) {
        return random.nextDouble() < chance;
    }

    public ItemStack make(ThreadLocalRandom random) {
        if (material == Material.BROWN_DYE && customModelData != null && customModelData == 14) {
            HungerGamesHexa plugin = HungerGamesHexa.getPlugin(HungerGamesHexa.class);
            if (!plugin.isModoTorneoEnabled()) {
                return new ItemStack(Material.AIR);
            }
        }

        int amount = random.nextInt(minAmount, maxAmount + 1);
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (name != null) {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        if (!enchantmentToLevelMap.isEmpty()) {
            for (Map.Entry<Enchantment, Integer> enchantEntry : enchantmentToLevelMap.entrySet()) {
                itemMeta.addEnchant(enchantEntry.getKey(), enchantEntry.getValue(), true);
            }
        }
        if (lore != null && !lore.isEmpty()) {
            itemMeta.setLore(lore);
        }
        if (customModelData != null) {
            itemMeta.setCustomModelData(customModelData);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}