package org.hexa.hungergameshexa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.manager.SpawnPointManager;

public class SetSpawnCommand implements CommandExecutor {

    private final SpawnPointManager spawnPointManager;

    public SetSpawnCommand(SpawnPointManager spawnPointManager) {
        this.spawnPointManager = spawnPointManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("Usalo asi: /setspawn <spawnpoint>");
            return true;
        }

        int spawnPointIndex;
        try {
            spawnPointIndex = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Numero no valido.");
            return true;
        }

        if (spawnPointIndex < 1 || spawnPointIndex > 16) {
            player.sendMessage("Debe de ser un numero entre 1 y 16");
            return true;
        }

        spawnPointManager.setSpawnPoint(player.getLocation(), spawnPointIndex);
        player.sendMessage("Spawn point " + spawnPointIndex + " colocado");
        return true;
    }
}
