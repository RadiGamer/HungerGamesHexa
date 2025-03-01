package org.hexa.hungergameshexa.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.tasks.LootItem;
import org.hexa.hungergameshexa.util.ChatUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ChestTier2Manager implements Listener {
    public final Set<Location> openedChests = new HashSet<>();
    public final List<LootItem> lootItems = new ArrayList<>();
    private final HungerGamesHexa plugin;

    public ChestTier2Manager(FileConfiguration lootConfig, HungerGamesHexa plugin){
        this.plugin = plugin;
        ConfigurationSection itemSection = lootConfig.getConfigurationSection("lootItemst2");

        if(itemSection==null){
            Bukkit.getLogger().severe("Falta lootItemst2 en config.yml :))");
        }

        for(String key : itemSection.getKeys(false)){
            ConfigurationSection section = itemSection.getConfigurationSection(key);
            lootItems.add(new LootItem(section));
        }
    }
    @EventHandler
    public void onChestOpen(InventoryOpenEvent event){
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Chest) {
            Chest chest = (Chest) holder;
            Block block = chest.getBlock();
            if (block.getType() == Material.TRAPPED_CHEST) {
                if (hasBeenOpened(block.getLocation())) return;

                markAsOpened(block.getLocation());
                fill(chest.getBlockInventory());
            }
        }
    }
    public void fill(Inventory inventory){
        inventory.clear();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Set<LootItem> used = new HashSet<>();

        for(int slotIndex = 0; slotIndex<inventory.getSize(); slotIndex++){
            LootItem randomItem = lootItems.get(random.nextInt(lootItems.size()));
            if(used.contains(randomItem)) continue;
            used.add(randomItem);

            if(randomItem.shouldFill(random)){
                ItemStack itemStack = randomItem.make(random);
                inventory.setItem(slotIndex, itemStack);
            }
        }
    }

    public void markAsOpened(Location location){
        openedChests.add(location);
    }
    public boolean hasBeenOpened(Location location){
        return openedChests.contains(location);
    }
    public void resetChests(boolean announce){

        String chestRefill = plugin.getConfig().getString("messages.chest-refill");


        if(announce) {
            Bukkit.broadcastMessage(ChatUtil.format(chestRefill));
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 0);
            }
        }
        openedChests.clear();
    }


}
