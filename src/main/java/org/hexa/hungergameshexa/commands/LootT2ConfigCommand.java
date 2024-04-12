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

import java.util.List;
import java.util.Objects;

public class LootT2ConfigCommand implements CommandExecutor {
    private final HungerGamesHexa plugin;

    public LootT2ConfigCommand(HungerGamesHexa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String errorConsola = plugin.getConfig().getString("messages.error-console");
        String errorPermiso = plugin.getConfig().getString("messages.error-permission");

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtil.format(errorConsola));
            return true;
        }

        if (!sender.hasPermission("hexa.admin")) {
            sender.sendMessage(ChatUtil.format(errorPermiso));
            return true;
        }

        Player player = (Player) sender;
        Inventory inv = Bukkit.createInventory(null, 9 * 3, ChatColor.LIGHT_PURPLE + "Coloca los items para los cofres " + ChatColor.WHITE + "T2");

        plugin.reloadConfig();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("lootItemst2");
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
                        if (itemSection.contains("lore")) {
                            List<String> lore = itemSection.getStringList("lore");
                            meta.setLore(lore);
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
