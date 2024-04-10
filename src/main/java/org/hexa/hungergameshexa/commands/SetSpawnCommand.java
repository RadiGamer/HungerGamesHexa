package org.hexa.hungergameshexa.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.manager.SpawnPointManager;
import org.hexa.hungergameshexa.util.ChatUtil;

public class SetSpawnCommand implements CommandExecutor {

    private final SpawnPointManager spawnPointManager;
    private final HungerGamesHexa plugin;
    public SetSpawnCommand(HungerGamesHexa plugin, SpawnPointManager spawnPointManager) {
        this.spawnPointManager = spawnPointManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String errorConsola = plugin.getConfig().getString("messages.error-console");
        String errorPermiso = plugin.getConfig().getString("messages.error-permission");
        String setspawnSyntax = plugin.getConfig().getString("messages.setspawn-syntax");
        String setspawnNotValid = plugin.getConfig().getString("messages.setspawn-notvalid");
        String setspawnNumber = plugin.getConfig().getString("messages.setspawn-Number");
        String setspawnSet = plugin.getConfig().getString("messages.setspawn-set");



        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtil.format(errorConsola));
            return true;
        }

        if (!sender.hasPermission("hexa.admin")) {
            sender.sendMessage(ChatUtil.format(errorPermiso));
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(ChatUtil.format(setspawnSyntax));
            return true;
        }

        int spawnPointIndex;
        try {
            spawnPointIndex = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatUtil.format(setspawnNotValid));
            return true;
        }

        if (spawnPointIndex < 1 || spawnPointIndex > 16) {
            player.sendMessage(ChatUtil.format(setspawnNumber));
            return true;
        }

        String setspawnSetMessage = String.format(setspawnSet, spawnPointIndex);

        spawnPointManager.setSpawnPoint(player.getLocation(), spawnPointIndex);
        player.sendMessage(ChatUtil.format(setspawnSetMessage));
        spawnPointManager.saveSpawnPoint(spawnPointIndex, player.getLocation());
        return true;
    }
}
