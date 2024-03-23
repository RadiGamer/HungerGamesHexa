package org.hexa.hungergameshexa.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.manager.SpawnPointManager;
import org.hexa.hungergameshexa.util.ChatUtil;

public class SetSpawnCommand implements CommandExecutor {

    private final SpawnPointManager spawnPointManager;
    String errorConsola = "&c&lNo puedes usar este comando en la consola crack";
    String errorPermiso = "&c&lNo tienes permiso para ejecutar este comando";

    public SetSpawnCommand(SpawnPointManager spawnPointManager) {
        this.spawnPointManager = spawnPointManager;
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

        if (args.length != 1) {
            player.sendMessage(ChatUtil.format("&7Usalo asi: /setspawn <spawnpoint>"));
            return true;
        }

        int spawnPointIndex;
        try {
            spawnPointIndex = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatUtil.format("&cNumero no valido."));
            return true;
        }

        if (spawnPointIndex < 1 || spawnPointIndex > 16) {
            player.sendMessage(ChatUtil.format("&cDebe de ser un numero entre 1 y 16"));
            return true;
        }

        spawnPointManager.setSpawnPoint(player.getLocation(), spawnPointIndex);
        player.sendMessage(ChatUtil.format("&7Spawnpoint &e" + spawnPointIndex +" &7colocado"));
        spawnPointManager.saveSpawnPoint(spawnPointIndex, player.getLocation());
        return true;
    }
}
