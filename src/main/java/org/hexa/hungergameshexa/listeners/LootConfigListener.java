package org.hexa.hungergameshexa.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.hexa.hungergameshexa.HungerGamesHexa;

public class LootConfigListener implements Listener {

    private final HungerGamesHexa plugin;

    public LootConfigListener(HungerGamesHexa plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getView().getTitle().equals(ChatColor.BLUE + "Coloca los items para los cofres"))) return;

        ItemStack[] items = event.getInventory().getContents();
        FileConfiguration config = plugin.getConfig();
        config.set("lootItems", null); // Clear the existing configuration section

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item == null || item.getType() == Material.AIR) continue;

            String basePath = "lootItems." + i;
            config.set(basePath + ".material", item.getType().toString());

            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName()) {
                    config.set(basePath + ".name", item.getItemMeta().getDisplayName());
                }
                if (item.getItemMeta().hasEnchants()) {
                    item.getEnchantments().forEach((enchant, level) -> {
                        String enchantPath = basePath + ".enchantments." + enchant.getKey().getKey();
                        config.set(enchantPath, level);
                    });
                }
            }
            config.set(basePath + ".minAmount", 1);
            config.set(basePath + ".maxAmount", 1);
            config.set(basePath + ".chance", 0.1);
        }

        plugin.saveConfig();
    }
}
