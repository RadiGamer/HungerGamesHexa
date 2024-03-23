package org.hexa.hungergameshexa.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.util.ChatUtil;

import java.util.Objects;

public class LootConfigCommand implements CommandExecutor {
    private final HungerGamesHexa plugin;
    String errorConsola = "&c&lNo puedes usar este comando en la consola crack";
    String errorPermiso = "&c&lNo tienes permiso para ejecutar este comando";
    public LootConfigCommand(HungerGamesHexa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtil.format(errorConsola));
            return true;
        }

        if (!sender.hasPermission("hexa.admin")) {
            sender.sendMessage(ChatUtil.format(errorPermiso));
            return true;
        }

        Player player = (Player) sender;
        Inventory inv = Bukkit.createInventory(null, 9 * 6, ChatColor.BLUE + "Coloca los items para los cofres");

        plugin.reloadConfig();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("lootItems");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ConfigurationSection itemSection = section.getConfigurationSection(key);
                if (itemSection != null) {
                    Material material = Material.getMaterial(itemSection.getString("material"));
                    ItemStack item = new ItemStack(material != null ? material : Material.BARRIER);
                    ItemMeta meta = item.getItemMeta();

                    if (meta != null) {
                        if (itemSection.contains("name")) {
                            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemSection.getString("name")));
                        }
                        if (itemSection.contains("enchantments")) {
                            ConfigurationSection enchSection = itemSection.getConfigurationSection("enchantments");
                            for (String enchKey : Objects.requireNonNull(enchSection).getKeys(false)) {
                                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchKey.toLowerCase()));
                                if (enchantment != null) {
                                    meta.addEnchant(enchantment, enchSection.getInt(enchKey), true);
                                }
                            }
                        }
                        if(itemSection.contains("customModelData")){
                            int customModelData = itemSection.getInt("customModelData");
                            meta.setCustomModelData(customModelData);
                        }
                        item.setItemMeta(meta);
                    }

                    inv.addItem(item);
                }
            }
        }

        player.openInventory(inv);
        return true;
    }
}
