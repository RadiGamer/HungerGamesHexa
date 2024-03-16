package org.hexa.hungergameshexa.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.hexa.hungergameshexa.HungerGamesHexa;

public class LootConfigCommand implements CommandExecutor {

    private final HungerGamesHexa plugin;

    public LootConfigCommand(HungerGamesHexa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Coloca los items para los cofres");

        player.openInventory(inv);
        return true;
    }
}
