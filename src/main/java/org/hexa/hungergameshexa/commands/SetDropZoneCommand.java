package org.hexa.hungergameshexa.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class SetDropZoneCommand implements CommandExecutor, TabCompleter {

    private final HungerGamesHexa plugin;


    public SetDropZoneCommand(HungerGamesHexa plugin) {
        this.plugin = plugin;
        plugin.getCommand("setdropzone").setExecutor(this);
        plugin.getCommand("setdropzone").setTabCompleter(this);

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

        String zoneName = args[0];
        String corner = args[1].toLowerCase();

        if (!(corner.equals("corner1") || corner.equals("corner2"))) {
            player.sendMessage(ChatColor.RED + "Esquina invalida. Usa 'corner1' o 'corner2'.");

            return true;
        }
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(player.getLocation().getX());
        coordinates.add(player.getLocation().getZ());

        plugin.getConfig().set("drop-zones." + zoneName + "." + corner, coordinates);
        plugin.saveConfig();

        player.sendMessage(ChatUtil.format("&aHas establecido " + corner + " de la zona '" + zoneName));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            List<String> corners = new ArrayList<>();
            corners.add("corner1");
            corners.add("corner2");
            return corners;
        }
        return new ArrayList<>();
    }
}
