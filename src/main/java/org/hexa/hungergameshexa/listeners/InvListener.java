package org.hexa.hungergameshexa.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.manager.ChestManager;

public class InvListener implements Listener {
    private ChestManager chestManager;
    private Plugin plugin;

    public InvListener(HungerGamesHexa plugin,ChestManager chestManager){
        this.chestManager = chestManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack item = event.getCurrentItem();

        if (inventory == null || item == null || item.getType() == Material.AIR) {
            return;
        }

        if (event.getView().getTitle().equals("Selecciona un inventario")) {
            event.setCancelled(true);

            String nombreInventario = ChatColor.stripColor(item.getItemMeta().getDisplayName()); // Quita el color para comparar
            System.out.println("Abriendo inventario: " + nombreInventario); // TODO BORRAR ESTA LINEA

            Inventory nivelInventario = chestManager.loadInventory(nombreInventario);

            if (nivelInventario != null) {
                player.openInventory(nivelInventario);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Inventory inventory = event.getInventory();

        if(inventory!=null){
            String nombreInventario = event.getView().getTitle();
            chestManager.saveInventory(nombreInventario, inventory);
        }


    }
}
