package org.hexa.hungergameshexa.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Barrel;
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

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DropLootManager implements Listener {
    public final Set<Location> openedChests = new HashSet<>();
    public final List<LootItem> lootItems = new ArrayList<>();
    private final Map<Location, Long> scheduledOpenings = new HashMap<>();
    private final HungerGamesHexa plugin;

    public DropLootManager(HungerGamesHexa plugin, FileConfiguration lootConfig){
        this.plugin = plugin;
        ConfigurationSection itemSection = lootConfig.getConfigurationSection("dropLoot");

        if(itemSection==null){
            Bukkit.getLogger().severe("Falta dropLoot en config.yml :))");
        }

        for(String key : itemSection.getKeys(false)){
            ConfigurationSection section = itemSection.getConfigurationSection(key);
            lootItems.add(new LootItem(section));
        }
    }
   @EventHandler
   public void onBarrelOpen(InventoryOpenEvent event) {

       InventoryHolder holder = event.getInventory().getHolder();
       if (!(holder instanceof Barrel)) {
           return;
       }

       Barrel barrel = (Barrel) holder;
       Location location = barrel.getLocation();
       Player player = (Player) event.getPlayer();

       String dropOpening = plugin.getConfig().getString("message.drop-opening", "&fUn jugador está abriendo el drop en &7X: %d, Z: %d");
       String dropOpeningMessage = String.format(dropOpening, location.getBlockX(), location.getBlockZ());

       String dropOpeningWait = plugin.getConfig().getString("messages.drop-opening-wait", "&fEl drop se esta abriendo, tienes que esperar %d segundos.");


       if (scheduledOpenings.containsKey(location)) {
           long scheduledTime = scheduledOpenings.get(location);
           long currentTime = System.currentTimeMillis();


           if (currentTime < scheduledTime) {
               event.setCancelled(true);
               long timeLeft = (scheduledTime - currentTime) / 1000;

               String dropOpeningWaitMessage = String.format(dropOpeningWait, timeLeft);

               player.sendMessage(ChatUtil.format(dropOpeningWaitMessage));
               player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10 , 0);
           }
           return;
       }
       scheduledOpenings.put(location, System.currentTimeMillis() + 20000);
       event.setCancelled(true);
       fill(barrel.getInventory());
       Bukkit.broadcastMessage(ChatUtil.format(dropOpeningMessage));

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

}
