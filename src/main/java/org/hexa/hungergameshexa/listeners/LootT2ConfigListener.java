package org.hexa.hungergameshexa.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.hexa.hungergameshexa.HungerGamesHexa;

public class LootT2ConfigListener implements Listener {

    private final HungerGamesHexa plugin;

    public LootT2ConfigListener(HungerGamesHexa plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getView().getTitle().equals(ChatColor.LIGHT_PURPLE + "Coloca los items para los cofres" + ChatColor.WHITE + "T2"))) return;

        ItemStack[] items = event.getInventory().getContents();
        FileConfiguration config = plugin.getConfig();
        config.set("lootItemst2", null);

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item == null || item.getType() == Material.AIR) continue;

            String basePath = "lootItemst2." + i;
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
                if(item.getItemMeta().hasCustomModelData()){
                    int customModelData = item.getItemMeta().getCustomModelData();
                    config.set(basePath + ".customModelData", customModelData);
                }
            }
            config.set(basePath + ".minAmount", 1);
            config.set(basePath + ".maxAmount", 1);
            config.set(basePath + ".chance", 0.1);
        }

        plugin.saveConfig();
    }
}
