package org.hexa.hungergameshexa.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.manager.ChestManager;
import org.hexa.hungergameshexa.manager.GameManager;

public class OpenGUICommand implements CommandExecutor {

    private ChestManager chestManager;



    public OpenGUICommand(HungerGamesHexa plugin) {
        this.chestManager= new ChestManager(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser ejecutado por un jugador.");
            return true;
        }

        Player jugador = (Player) sender;
        Inventory menu = Bukkit.createInventory(null, 9, "Selecciona un inventario");

        ItemStack facil = new ItemStack(Material.CHEST, 1);
        ItemMeta facilMeta = facil.getItemMeta();
        facilMeta.setDisplayName("FACIL");
        facil.setItemMeta(facilMeta);

        ItemStack medio = new ItemStack(Material.CHEST, 1);
        ItemMeta medioMeta = medio.getItemMeta();
        medioMeta.setDisplayName("MEDIO");
        medio.setItemMeta(medioMeta);

        ItemStack dificil = new ItemStack(Material.CHEST, 1);
        ItemMeta dificilMeta = dificil.getItemMeta();
        dificilMeta.setDisplayName("DIFICIL");
        dificil.setItemMeta(dificilMeta);

        ItemStack custom = new ItemStack(Material.CHEST, 1);
        ItemMeta customMeta = custom.getItemMeta();
        customMeta.setDisplayName("PERSONALIZADO");
        custom.setItemMeta(customMeta);

        menu.setItem(1, facil);
        menu.setItem(3, medio);
        menu.setItem(5, dificil);
        menu.setItem(7, custom);

        jugador.openInventory(menu);

        return true;
    }
}
