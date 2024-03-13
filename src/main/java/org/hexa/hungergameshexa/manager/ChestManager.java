package org.hexa.hungergameshexa.manager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.hexa.hungergameshexa.HungerGamesHexa;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChestManager {
    private final Map<String, Inventory> inventories = new HashMap<>();
    private final File configFile;
    private final FileConfiguration config;

    public ChestManager(HungerGamesHexa plugin) {
        this.configFile = new File(plugin.getDataFolder(), "inventarios.yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);
        loadInventories();
        initializeConfig();
    }
    private void initializeConfig() {
        if (!configFile.exists()) {
            config.createSection("inventories");
            saveConfig();
        }
    }
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveInventory(String inventoryName, Inventory inventory) {
        config.set("inventories." + inventoryName + ".items" , inventory.getContents());
        saveConfig();

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadInventories() {
        if (!configFile.exists()) {
            // Create the default inventories in the YAML file if it doesn't exist
            config.set("inventories.FACIL", null);
            config.set("inventories.MEDIO", null);
            config.set("inventories.DIFICIL", null);
            config.set("inventories.PERSONALIZADO", null);

            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (config.contains("inventories")) {
            ConfigurationSection inventoriesSection = config.getConfigurationSection("inventories");

            if (inventoriesSection != null) {
                Map<String, Object> inventoryData = inventoriesSection.getValues(false);

                for (Map.Entry<String, Object> entry : inventoryData.entrySet()) {
                    String inventoryName = entry.getKey();
                    Object inventoryContents = entry.getValue();

                    if (inventoryContents instanceof List<?>) {
                        Inventory inventory = Bukkit.createInventory(null, 9 * 3, inventoryName);
                        inventory.setContents(((List<ItemStack>) inventoryContents).toArray(new ItemStack[0]));
                        inventories.put(inventoryName, inventory);
                    }
                }
            }
        }
    }
    public Inventory loadInventory(String inventoryName){
        ConfigurationSection inventorySection = config.getConfigurationSection("inventories." + inventoryName);
        if(inventorySection !=null){
            List<ItemStack> items = (List<ItemStack>) inventorySection.getList("items");
            if(items!=null) {
                Inventory inventory = Bukkit.createInventory(null, 9 * 3, inventoryName);
                inventory.setContents(inventorySection.getList("items").toArray(new ItemStack[0]));
                return inventory;
            }
        }
        return null;
    }
}
